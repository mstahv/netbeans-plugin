/**
 *
 */
package org.vaadin.netbeans.code.generator;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle;
import org.vaadin.netbeans.VaadinSupport;
import org.vaadin.netbeans.model.ModelOperation;
import org.vaadin.netbeans.model.VaadinModel;

/**
 * @author denis
 */
public class ConnectorOnlyWidgetGenerator implements WidgetGenerator {

    private static final String SERVER_COMPONENT_FQN = "server_component_fqn";//NOI18N

    private static final String SERVER_COMPONENT = "server_component";//NOI18N

    private static final String COMPONENT_TEMPLATE = "Templates/Vaadin/ConnectorComponent.java"; // NOI18N

    private static final String CONNECTOR_TEMPLATE = "Templates/Vaadin/ConnectorConnector.java"; // NOI18N

    @Override
    @NbBundle.Messages({
            "generateServerComponent=Generate Server Side Component class",
            "generateGwtXml=Generate GWT Module XML file",
            "generateConnector=Generate Connector class" })
    public Set<FileObject> generate( WizardDescriptor wizard,
            ProgressHandle handle ) throws IOException
    {
        Set<FileObject> classes = new LinkedHashSet<>();
        FileObject targetPackage = Templates.getTargetFolder(wizard);
        String componentClassName = Templates.getTargetName(wizard);

        // Generate server component
        handle.progress(Bundle.generateServerComponent());
        DataObject dataObject = JavaUtils.createDataObjectFromTemplate(
                COMPONENT_TEMPLATE, targetPackage, componentClassName, null);

        FileObject serverComponent = dataObject.getPrimaryFile();
        classes.add(dataObject.getPrimaryFile());

        // Find/create gwt.xml and sources package
        Project project = Templates.getProject(wizard);
        VaadinSupport support = project.getLookup().lookup(VaadinSupport.class);
        final FileObject[] gwtXml = new FileObject[1];
        final String[] srcPath = new String[1];
        if (support != null) {
            support.runModelOperation(new ModelOperation() {

                @Override
                public void run( VaadinModel model ) {
                    gwtXml[0] = model.getGwtXml();
                    srcPath[0] = model.getSourcePath();
                }
            });
        }

        if (gwtXml[0] == null) {
            handle.progress(Bundle.generateGwtXml());
            gwtXml[0] = XmlUtils.createGwtXml(targetPackage);
            classes.add(gwtXml[0]);
            if (support != null) {
                XmlUtils.waitGwtXml(support, srcPath);
            }
        }
        FileObject clientPackage = XmlUtils.getClientWidgetPackage(gwtXml[0],
                srcPath[0], true);

        if (clientPackage == null) {
            Logger.getLogger(ConnectorOnlyWidgetGenerator.class.getName())
                    .severe("Unable to detect package for client side classes");
            return classes;
        }

        // Generate connector
        String connectorName = JavaUtils.getFreeName(clientPackage,
                componentClassName + CONNECTOR, JavaUtils.JAVA_SUFFIX);

        handle.progress(Bundle.generateConnector());
        Map<String, String> map = new HashMap<>();
        map.put(SERVER_COMPONENT, componentClassName);
        map.put(SERVER_COMPONENT_FQN, JavaUtils.getFqn(serverComponent));
        dataObject = JavaUtils.createDataObjectFromTemplate(CONNECTOR_TEMPLATE,
                clientPackage, connectorName, map);
        classes.add(dataObject.getPrimaryFile());

        return classes;
    }

}
