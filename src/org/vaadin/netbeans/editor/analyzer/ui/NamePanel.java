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
package org.vaadin.netbeans.editor.analyzer.ui;

import javax.swing.SwingUtilities;

/**
 * @author denis
 */
public class NamePanel extends javax.swing.JPanel {

    public NamePanel( String name ) {
        initComponents();
        if (name != null) {
            myName.setText(name);
        }

        selectText();
    }

    public String getClassName() {
        return myName.getText().toString();
    }

    private void selectText() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (SwingUtilities.getWindowAncestor(NamePanel.this) == null) {
                    selectText();
                }
                else {
                    myName.requestFocusInWindow();
                    myName.selectAll();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLbl = new javax.swing.JLabel();
        myName = new javax.swing.JTextField();

        nameLbl.setLabelFor(myName);
        org.openide.awt.Mnemonics.setLocalizedText(nameLbl,
                org.openide.util.NbBundle.getMessage(NamePanel.class,
                        "LBL_RpcInterfaceName")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(nameLbl)
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(myName,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        337, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(nameLbl)
                                                .addComponent(
                                                        myName,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));

        nameLbl.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(NamePanel.class,
                        "ACSN_RpcInterfaceName")); // NOI18N
        nameLbl.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(NamePanel.class,
                        "ACSD_RpcInterfaceName")); // NOI18N
        myName.getAccessibleContext().setAccessibleName(
                nameLbl.getAccessibleContext().getAccessibleName());
        myName.getAccessibleContext().setAccessibleDescription(
                nameLbl.getAccessibleContext().getAccessibleDescription());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField myName;

    private javax.swing.JLabel nameLbl;
    // End of variables declaration//GEN-END:variables
}
