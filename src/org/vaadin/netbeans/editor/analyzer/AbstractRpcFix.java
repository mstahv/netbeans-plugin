/*
 * Copyright 2000-2013 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.netbeans.editor.analyzer;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.GeneratorUtilities;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.TreeMaker;
import org.netbeans.api.java.source.WorkingCopy;
import org.netbeans.spi.editor.hints.ChangeInfo;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle;
import org.vaadin.netbeans.utils.JavaUtils;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePathScanner;

/**
 * @author denis
 */
abstract class AbstractRpcFix extends AbstractCreateFix {

    static final String CLIENT_RPC = "ClientRpc"; // NOI18N

    static final String CLIENT_RPC_TEMPLATE =
            "Templates/Vaadin/ClientRpcStub.java"; // NOI18N

    static final String SERVER_RPC = "ServerRpc"; // NOI18N

    static final String SERVER_RPC_TEMPLATE = "Templates/Vaadin/" + SERVER_RPC
            + "Stub.java"; // NOI18N

    AbstractRpcFix( FileObject fileObject, ElementHandle<TypeElement> handle,
            String varName, String varType )
    {
        super(fileObject);
        myHandle = handle;
        myRpcVar = varName;
        myRpcVarType = varType;
    }

    @Override
    public ChangeInfo implement() throws Exception {
        logUiUsage();
        searchClientPackage(false);
        if (getClientPackage() == null) {
            createClientPackage();
        }
        FileObject pkg = getClientPackage();

        String interfaceName = null;
        String ifaceFqn = getRpcVariableType();
        if (ifaceFqn == null) {
            interfaceName = requestInterfaceName(pkg);
            if (interfaceName == null) {
                return null;
            }
            if (!createFileObject(pkg, interfaceName)) {
                return null;
            }
            ClassPath classPath = ClassPath.getClassPath(pkg, ClassPath.SOURCE);
            ifaceFqn =
                    classPath.getResourceName(pkg, '.', false) + '.'
                            + interfaceName;
        }
        else {
            int index = ifaceFqn.lastIndexOf('.');
            interfaceName = ifaceFqn.substring(index + 1, ifaceFqn.length());
        }

        JavaSource javaSource = JavaSource.forFileObject(getFileObject());
        if (javaSource == null) {
            Logger.getLogger(AbstractRpcFix.class.getName()).log(Level.WARNING,
                    "JavaSource is null for file {0}",
                    getFileObject().getPath());
            return null;
        }

        return generateInterfaceUsage(javaSource, ifaceFqn, interfaceName);
    }

    protected ElementHandle<TypeElement> getTypeHandle() {
        return myHandle;
    }

    protected abstract String suggestInterfaceName( FileObject targetPkg );

    protected abstract String getRpcInterfaceTemplate();

    protected abstract String getInterfaceCreationDialogTitle();

    protected abstract ChangeInfo generateInterfaceUsage(
            JavaSource javaSource, final String ifaceFqn, final String ifaceName )
            throws IOException;

    protected String getRpcVariable() {
        return myRpcVar;
    }

    protected String getRpcVariableType() {
        return myRpcVarType;
    }

    protected String requestInterfaceName( FileObject targetPkg ) {
        final String name = suggestInterfaceName(targetPkg);
        return requestClassName(name);
    }

    protected String suggestInterfaceName( FileObject pkg, String prefix,
            String rpcBaseName )
    {
        StringBuilder name = new StringBuilder(prefix);
        name.append(rpcBaseName);
        int i = 1;
        String suggestedName = name.toString();
        while (pkg.getFileObject(suggestedName, JavaUtils.JAVA) != null) {
            StringBuilder current = new StringBuilder(name);
            suggestedName = current.append(i).toString();
            i++;
        }
        return suggestedName;
    }

    protected void addImport( String ifaceFqn, WorkingCopy copy,
            TreeMaker treeMaker )
    {
        addImport(ifaceFqn, getTypeHandle(), copy, treeMaker);
    }

    protected String findFreeGetterName( TypeElement clazz, String suffix ) {
        List<ExecutableElement> methods =
                ElementFilter.methodsIn(clazz.getEnclosedElements());
        Set<String> names = new HashSet<>();
        for (ExecutableElement method : methods) {
            names.add(method.getSimpleName().toString());
        }
        String getterName = "get" + suffix; //NOI18N
        String name = getterName;
        int i = 1;
        while (names.contains(getterName)) {
            name = getterName + i;
            i++;
        }
        return name;
    }

    protected ClassTree implement( String ifaceFqn, TreeMaker treeMaker,
            WorkingCopy copy, ClassTree body )
    {
        TypeElement iface = copy.getElements().getTypeElement(ifaceFqn);
        TypeElement object =
                copy.getElements().getTypeElement(Object.class.getName());
        if (iface == null || object == null) {
            return body;
        }
        GeneratorUtilities genUtilities = GeneratorUtilities.get(copy);

        Set<ExecutableElement> methods =
                new HashSet<>(ElementFilter.methodsIn(copy.getElements()
                        .getAllMembers(iface)));
        methods.removeAll(object.getEnclosedElements());

        List<? extends MethodTree> methodsTree =
                genUtilities.createAbstractMethodImplementations(object,
                        methods);
        for (MethodTree methodTree : methodsTree) {
            body = treeMaker.addClassMember(body, methodTree);
        }
        return body;
    }

    @NbBundle.Messages({
            "# {0} - className",
            "interfaceIsNotCreated=Unable to create an interface with name {0}.",
            "interfaceAlreadyExists=Interface {0} already exists" })
    private boolean createFileObject( FileObject pkg, String interfaceName ) {
        if (pkg.getFileObject(interfaceName, JavaUtils.JAVA_SUFFIX) != null) {
            NotifyDescriptor descriptor =
                    new NotifyDescriptor.Message(
                            Bundle.interfaceAlreadyExists(interfaceName),
                            NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(descriptor);
            return false;
        }
        try {
            DataObject dataObject =
                    JavaUtils
                            .createDataObjectFromTemplate(
                                    getRpcInterfaceTemplate(), pkg,
                                    interfaceName, null);
            if (dataObject != null) {
                EditorCookie cookie =
                        dataObject.getLookup().lookup(EditorCookie.class);
                if (cookie != null) {
                    cookie.open();
                }
            }
        }
        catch (IOException e) {
            Logger.getLogger(CreateServerRpcFix.class.getName()).log(
                    Level.INFO, null, e);
            NotifyDescriptor descriptor =
                    new NotifyDescriptor.Message(
                            Bundle.interfaceIsNotCreated(interfaceName),
                            NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(descriptor);
            return false;
        }
        return true;
    }

    protected static class CtorScanner extends
            TreePathScanner<MethodTree, Void>
    {

        private static final String THIS = "this";

        private static final String SUPER = "super";

        @Override
        public MethodTree visitMethodInvocation( MethodInvocationTree tree,
                Void p )
        {
            ExpressionTree methodSelect = tree.getMethodSelect();
            if (methodSelect instanceof IdentifierTree) {
                Name name = ((IdentifierTree) methodSelect).getName();
                if (THIS.contentEquals(name)) {
                    hasThis = true;
                }
                else if (SUPER.contentEquals(name)) {
                    hasSuper = true;
                }
            }
            return super.visitMethodInvocation(tree, p);
        }

        boolean hasThis() {
            return hasThis;
        }

        boolean hasSuper() {
            return hasSuper;
        }

        private boolean hasThis;

        private boolean hasSuper;

    }

    private ElementHandle<TypeElement> myHandle;

    private String myRpcVar;

    private String myRpcVarType;
}
