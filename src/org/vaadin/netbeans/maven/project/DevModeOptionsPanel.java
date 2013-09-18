package org.vaadin.netbeans.maven.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.namespace.QName;

import org.apache.maven.project.MavenProject;
import org.netbeans.api.progress.ProgressUtils;
import org.netbeans.api.project.Project;
import org.netbeans.modules.maven.api.NbMavenProject;
import org.netbeans.modules.maven.api.customizer.ModelHandle2;
import org.netbeans.modules.maven.model.ModelOperation;
import org.netbeans.modules.maven.model.Utilities;
import org.netbeans.modules.maven.model.pom.Configuration;
import org.netbeans.modules.maven.model.pom.POMExtensibilityElement;
import org.netbeans.modules.maven.model.pom.POMModel;
import org.netbeans.modules.maven.model.pom.Plugin;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * @author denis
 */
public class DevModeOptionsPanel extends javax.swing.JPanel {

    private static final String NO_SERVER = "noServer";

    private static final String RUN_TARGET = "runTarget";

    private static final String PORT = "port";

    private static final String HOSTED_WEBAPP = "hostedWebapp";

    public DevModeOptionsPanel( Lookup context ) {
        initComponents();

        // Fix for #12596
        hostedWebappLbl.setVisible(false);
        myHostedWebApp.setVisible(false);
        portLbl.setVisible(false);
        myHostedPort.setVisible(false);
        myNoServer.setVisible(false);
        separator.setVisible(false);

        myDebugPort.setDocument(new NumericDocument(myDebugPort, 5));
        myHostedPort.setDocument(new NumericDocument(myHostedPort, 5));
        myTimeout.setDocument(new NumericDocument(myTimeout, -1));

        final ModelHandle2 handle = context.lookup(ModelHandle2.class);
        final DevModeModification operation = new DevModeModification(
                context.lookup(Project.class));

        myNoServer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                myHostedPort.setEditable(!myNoServer.isSelected());
                addModification(handle, operation);
            }
        });

        DocumentListener documentListener = new DocumentListener() {

            @Override
            public void removeUpdate( DocumentEvent e ) {
                update();
            }

            @Override
            public void insertUpdate( DocumentEvent e ) {
                update();
            }

            @Override
            public void changedUpdate( DocumentEvent e ) {
                update();
            }

            private void update() {
                addModification(handle, operation);
            }
        };

        myBindAddress.getDocument().addDocumentListener(documentListener);
        myDebugPort.getDocument().addDocumentListener(documentListener);
        myHostedPort.getDocument().addDocumentListener(documentListener);
        myHostedWebApp.getDocument().addDocumentListener(documentListener);
        myRunTarget.getDocument().addDocumentListener(documentListener);
        myTimeout.getDocument().addDocumentListener(documentListener);

        setValues(context);
    }

    @NbBundle.Messages("readingDebugOptions=Retrieving options...")
    private void setValues( final Lookup context ) {
        ProgressUtils.showProgressDialogAndRun(new InitRunnable(context),
                Bundle.readingDebugOptions());
    }

    private void addModification( ModelHandle2 handle,
            DevModeModification newOperation )
    {
        List<ModelOperation<POMModel>> pomOperations = handle
                .getPOMOperations();
        for (ModelOperation<POMModel> operation : pomOperations) {
            if (operation instanceof DevModeModification) {
                handle.removePOMModification(operation);
            }
        }
        handle.addPOMModification(newOperation);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bindLbl = new javax.swing.JLabel();
        hostedWebappLbl = new javax.swing.JLabel();
        portLbl = new javax.swing.JLabel();
        runTargetLbl = new javax.swing.JLabel();
        myHostedWebApp = new javax.swing.JTextField();
        myBindAddress = new javax.swing.JTextField();
        myHostedPort = new javax.swing.JTextField();
        myRunTarget = new javax.swing.JTextField();
        separator = new javax.swing.JSeparator();
        myNoServer = new javax.swing.JCheckBox();
        debugPortLbl = new javax.swing.JLabel();
        myDebugPort = new javax.swing.JTextField();
        attachTimeOutLbl = new javax.swing.JLabel();
        myTimeout = new javax.swing.JTextField();

        bindLbl.setLabelFor(myBindAddress);
        org.openide.awt.Mnemonics.setLocalizedText(bindLbl,
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "LBL_Bind")); // NOI18N

        hostedWebappLbl.setLabelFor(myHostedWebApp);
        org.openide.awt.Mnemonics.setLocalizedText(hostedWebappLbl,
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "LBL_HostedWebApp")); // NOI18N

        portLbl.setLabelFor(myHostedPort);
        org.openide.awt.Mnemonics.setLocalizedText(portLbl,
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "LBL_HostedPort")); // NOI18N

        runTargetLbl.setLabelFor(myRunTarget);
        org.openide.awt.Mnemonics.setLocalizedText(runTargetLbl,
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "LBL_RunTarget")); // NOI18N

        myHostedWebApp.setToolTipText(org.openide.util.NbBundle.getMessage(
                DevModeOptionsPanel.class, "TLTP_HostedWebApp")); // NOI18N

        myHostedPort.setText(org.openide.util.NbBundle.getMessage(
                DevModeOptionsPanel.class,
                "DevModeOptionsPanel.myHostedPort.text")); // NOI18N
        myHostedPort.setToolTipText(org.openide.util.NbBundle.getMessage(
                DevModeOptionsPanel.class, "TLTP_EmbeddedServerPort")); // NOI18N

        myRunTarget.setToolTipText(org.openide.util.NbBundle.getMessage(
                DevModeOptionsPanel.class, "TLTP_RunTarget")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(myNoServer,
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "LBL_NoServer")); // NOI18N

        debugPortLbl.setLabelFor(myDebugPort);
        org.openide.awt.Mnemonics.setLocalizedText(debugPortLbl,
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "LBL_DebugPort")); // NOI18N

        myDebugPort.setToolTipText(org.openide.util.NbBundle.getMessage(
                DevModeOptionsPanel.class, "TLTP_DebugPort")); // NOI18N

        attachTimeOutLbl.setLabelFor(myTimeout);
        org.openide.awt.Mnemonics.setLocalizedText(attachTimeOutLbl,
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "LBL_AttachDebugger")); // NOI18N

        myTimeout.setToolTipText(org.openide.util.NbBundle.getMessage(
                DevModeOptionsPanel.class, "TLTP_AttachTimeout")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(
                                                        separator,
                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addGroup(
                                                                        layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(
                                                                                        hostedWebappLbl)
                                                                                .addComponent(
                                                                                        bindLbl)
                                                                                .addComponent(
                                                                                        portLbl)
                                                                                .addComponent(
                                                                                        runTargetLbl)
                                                                                .addComponent(
                                                                                        debugPortLbl)
                                                                                .addComponent(
                                                                                        attachTimeOutLbl))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(
                                                                        layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(
                                                                                        myTimeout)
                                                                                .addComponent(
                                                                                        myHostedWebApp)
                                                                                .addComponent(
                                                                                        myBindAddress)
                                                                                .addComponent(
                                                                                        myHostedPort,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        332,
                                                                                        Short.MAX_VALUE)
                                                                                .addComponent(
                                                                                        myDebugPort)
                                                                                .addComponent(
                                                                                        myRunTarget,
                                                                                        javax.swing.GroupLayout.Alignment.TRAILING)))
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addComponent(
                                                                        myNoServer)
                                                                .addGap(0,
                                                                        0,
                                                                        Short.MAX_VALUE)))
                                .addContainerGap()));
        layout.setVerticalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(bindLbl)
                                                .addComponent(
                                                        myBindAddress,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(hostedWebappLbl)
                                                .addComponent(
                                                        myHostedWebApp,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(portLbl)
                                                .addComponent(
                                                        myHostedPort,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(
                                                        myDebugPort,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(debugPortLbl))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(attachTimeOutLbl)
                                                .addComponent(
                                                        myTimeout,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(
                                                        myRunTarget,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(runTargetLbl))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(separator,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(myNoServer)
                                .addContainerGap(
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));

        bindLbl.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSN_BindAddress")); // NOI18N
        bindLbl.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSD_BindAddress")); // NOI18N
        hostedWebappLbl.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSN_HostedWebApp")); // NOI18N
        hostedWebappLbl.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSD_HostedWebApp")); // NOI18N
        portLbl.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSN_HostedPort")); // NOI18N
        portLbl.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSD_HostedPort")); // NOI18N
        runTargetLbl.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSN_RunTarget")); // NOI18N
        runTargetLbl.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSD_RunTarget")); // NOI18N
        myHostedWebApp.getAccessibleContext().setAccessibleName(
                hostedWebappLbl.getAccessibleContext().getAccessibleName());
        myHostedWebApp.getAccessibleContext().setAccessibleDescription(
                hostedWebappLbl.getAccessibleContext()
                        .getAccessibleDescription());
        myBindAddress.getAccessibleContext().setAccessibleName(
                bindLbl.getAccessibleContext().getAccessibleName());
        myBindAddress.getAccessibleContext().setAccessibleDescription(
                bindLbl.getAccessibleContext().getAccessibleDescription());
        myHostedPort.getAccessibleContext().setAccessibleName(
                portLbl.getAccessibleContext().getAccessibleName());
        myHostedPort.getAccessibleContext().setAccessibleDescription(
                portLbl.getAccessibleContext().getAccessibleDescription());
        myRunTarget.getAccessibleContext().setAccessibleName(
                runTargetLbl.getAccessibleContext().getAccessibleName());
        myRunTarget.getAccessibleContext().setAccessibleDescription(
                runTargetLbl.getAccessibleContext().getAccessibleDescription());
        myNoServer.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSN_NoServer")); // NOI18N
        myNoServer.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSD_NoServer")); // NOI18N
        debugPortLbl.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSN_DebugPort")); // NOI18N
        debugPortLbl.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(DevModeOptionsPanel.class,
                        "ACSD_DebugPort")); // NOI18N
        myDebugPort.getAccessibleContext().setAccessibleName(
                debugPortLbl.getAccessibleContext().getAccessibleName());
        myDebugPort.getAccessibleContext().setAccessibleDescription(
                debugPortLbl.getAccessibleContext().getAccessibleDescription());
    }// </editor-fold>//GEN-END:initComponents
     // Variables declaration - do not modify//GEN-BEGIN:variables

    private javax.swing.JLabel attachTimeOutLbl;

    private javax.swing.JLabel bindLbl;

    private javax.swing.JLabel debugPortLbl;

    private javax.swing.JLabel hostedWebappLbl;

    private javax.swing.JTextField myBindAddress;

    private javax.swing.JTextField myDebugPort;

    private javax.swing.JTextField myHostedPort;

    private javax.swing.JTextField myHostedWebApp;

    private javax.swing.JCheckBox myNoServer;

    private javax.swing.JTextField myRunTarget;

    private javax.swing.JTextField myTimeout;

    private javax.swing.JLabel portLbl;

    private javax.swing.JLabel runTargetLbl;

    private javax.swing.JSeparator separator;

    // End of variables declaration//GEN-END:variables

    private final class DevModeModification implements ModelOperation<POMModel>
    {

        DevModeModification( Project project ) {
            myProject = project;
        }

        @Override
        public void performOperation( POMModel model ) {
            Plugin plugin = POMUtils.getVaadinPlugin(model);
            if (plugin == null) {
                /*
                 * This shouldn't happen : customizer shouldn't be available if
                 * there is no vaadin plugin
                 */
                return;
            }
            Configuration configuration = plugin.getConfiguration();
            if (configuration == null) {
                createConfiguration(plugin);
                return;
            }
            setOptions(configuration);
        }

        private void setOptions( Configuration configuration ) {
            List<POMExtensibilityElement> params = configuration
                    .getExtensibilityElements();
            Map<String, POMExtensibilityElement> values = new HashMap<>();
            for (POMExtensibilityElement param : params) {
                values.put(param.getQName().getLocalPart(), param);
            }

            InetSocketAddress defaultBindAddress = DebugUtils
                    .getDefaultBindAddress();

            POMExtensibilityElement webApp = values
                    .get(DebugUtils.BIND_ADDRESS);
            if (webApp != null
                    || !myBindAddress.getText().trim()
                            .equals(defaultBindAddress.getHostString()))
            {
                POMUtils.setTextField(DebugUtils.BIND_ADDRESS, values,
                        myBindAddress, configuration);
            }

            POMExtensibilityElement debugPort = values
                    .get(DebugUtils.DEBUG_PORT);
            if (debugPort != null
                    || !myDebugPort
                            .getText()
                            .trim()
                            .equals(String.valueOf(defaultBindAddress.getPort())))
            {
                POMUtils.setTextField(DebugUtils.DEBUG_PORT, values,
                        myDebugPort, configuration);
            }

            POMUtils.setTextField(HOSTED_WEBAPP, values, myHostedWebApp,
                    configuration);

            if (!myNoServer.isSelected()) {
                POMExtensibilityElement port = values.get(PORT);
                if (port != null
                        || myHostedPort
                                .getText()
                                .trim()
                                .equals(String.valueOf(defaultBindAddress
                                        .getPort())))
                {
                    POMUtils.setTextField(PORT, values, myHostedPort,
                            configuration);
                }
            }

            POMUtils.setTextField(RUN_TARGET, values, myRunTarget,
                    configuration);

            POMUtils.setBooleanVaue(NO_SERVER, values, myNoServer,
                    configuration);

            int timeout = DebugUtils.getAttachDebuggerTimout(myProject);
            String newTimeoutText = myTimeout.getText();
            // NumberFormatException shouldn't happen because of essence of the  field
            int newTimeout = Integer.parseInt(newTimeoutText);
            if (newTimeout != timeout) {
                DebugUtils.setAttachDebuggerTimout(myProject, newTimeout);
            }
        }

        private void createConfiguration( Plugin plugin ) {
            POMModel model = plugin.getModel();
            Configuration configuration = model.getFactory()
                    .createConfiguration();
            setOptions(configuration);
            plugin.setConfiguration(configuration);
        }

        private final Project myProject;

    }

    private final class InitRunnable implements Runnable,
            ModelOperation<POMModel>
    {

        InitRunnable( Lookup lookup ) {
            myLookup = lookup;
        }

        @Override
        public void performOperation( POMModel model ) {
            debugAddress = DebugUtils.getBindAddress(model);
            Plugin plugin = POMUtils.getVaadinPlugin(model);
            if (plugin == null) {
                return;
            }
            Configuration configuration = plugin.getConfiguration();
            if (configuration == null) {
                return;
            }
            List<POMExtensibilityElement> params = configuration
                    .getExtensibilityElements();
            for (POMExtensibilityElement param : params) {
                QName qName = param.getQName();
                String name = qName.getLocalPart();
                String value = param.getElementText().trim();
                switch (name) {
                    case HOSTED_WEBAPP:
                        hostedWebApp = value;
                        break;
                    case PORT:
                        try {
                            port = Integer.parseInt(value);
                        }
                        catch (NumberFormatException ignore) {
                        }
                        break;
                    case RUN_TARGET:
                        runTarget = value;
                        break;
                    case NO_SERVER:
                        noServer = Boolean.parseBoolean(value);
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void run() {
            Project project = myLookup.lookup(Project.class);
            NbMavenProject mvnProject = project.getLookup().lookup(
                    NbMavenProject.class);
            MavenProject mavenProject = mvnProject.getMavenProject();
            File file = mavenProject.getFile();
            FileObject pom = FileUtil
                    .toFileObject(FileUtil.normalizeFile(file));
            Utilities.performPOMModelOperations(pom,
                    Collections.singletonList(this));

            myBindAddress.setText(debugAddress.getHostString());
            myHostedWebApp.setText(hostedWebApp);
            myHostedPort.setText(String.valueOf(port));
            myDebugPort.setText(String.valueOf(debugAddress.getPort()));
            myRunTarget.setText(runTarget);

            myNoServer.setSelected(noServer);
            myHostedPort.setEditable(!myNoServer.isSelected());

            myTimeout.setText(String.valueOf(DebugUtils
                    .getAttachDebuggerTimout(project)));
        }

        private final Lookup myLookup;

        private String hostedWebApp;

        private int port = 8888;

        private String runTarget;

        private boolean noServer;

        private InetSocketAddress debugAddress;

    }
}
