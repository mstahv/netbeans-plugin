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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JPanel;

/**
 * @author denis
 */
public class UIComponentHint extends JPanel {

    /**
     * Creates new form UIComponentHint
     */
    public UIComponentHint( final Preferences prefs, final String key ) {
        initComponents();

        myUI.setSelected(prefs.getBoolean(key, false));

        myUI.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                prefs.putBoolean(key, myUI.isSelected());
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

        myUI = new javax.swing.JCheckBox();

        org.openide.awt.Mnemonics.setLocalizedText(myUI,
                org.openide.util.NbBundle.getMessage(UIComponentHint.class,
                        "LBL_EnableUI")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(myUI)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(myUI)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)));

        myUI.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(UIComponentHint.class,
                        "ACSN_EnableUI")); // NOI18N
        myUI.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(UIComponentHint.class,
                        "ACSD_EnableUI")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox myUI;

    // End of variables declaration//GEN-END:variables

}
