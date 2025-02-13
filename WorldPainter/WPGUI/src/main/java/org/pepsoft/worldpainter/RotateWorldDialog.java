/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RotateWorldDialog.java
 *
 * Created on Apr 14, 2012, 3:57:24 PM
 */
package org.pepsoft.worldpainter;

import org.pepsoft.util.ProgressReceiver;
import org.pepsoft.util.SubProgressReceiver;
import org.pepsoft.worldpainter.Dimension.Anchor;
import org.pepsoft.worldpainter.history.HistoryEntry;

import java.awt.*;

import static org.pepsoft.util.AwtUtils.doOnEventThread;

/**
 *
 * @author pepijn
 */
public class RotateWorldDialog extends WorldPainterDialog implements ProgressReceiver {
    /** Creates new form RotateWorldDialog */
    public RotateWorldDialog(Window parent, World2 world, Anchor anchor) {
        super(parent);
        this.world = world;
        this.anchor = anchor;
        final Dimension opposite = world.getDimension(new Anchor(anchor.dim, anchor.role, ! anchor.invert, 0));
        if (opposite != null) {
            oppositeAnchor = opposite.getAnchor();
        } else {
            oppositeAnchor = null;
        }
        
        initComponents();
        jCheckBox1.setEnabled(oppositeAnchor != null);

        getRootPane().setDefaultButton(buttonRotate);

        scaleToUI();
        setLocationRelativeTo(parent);
    }

    // ProgressReceiver
    
    @Override
    public synchronized void setProgress(final float progress) throws OperationCancelled {
        doOnEventThread(() -> jProgressBar1.setValue((int) (progress * 100)));
    }

    @Override
    public synchronized void exceptionThrown(final Throwable exception) {
        doOnEventThread(() -> {
            ErrorDialog errorDialog = new ErrorDialog(RotateWorldDialog.this);
            errorDialog.setException(exception);
            errorDialog.setVisible(true);
            cancel();
        });
    }

    @Override
    public synchronized void done() {
        doOnEventThread(this::ok);
    }

    @Override
    public synchronized void setMessage(final String message) throws OperationCancelled {
        doOnEventThread(() -> labelProgressMessage.setText(message));
    }

    @Override
    public synchronized void checkForCancellation() throws OperationCancelled {
        // Do nothing
    }

    @Override
    public void reset() {
        doOnEventThread(() -> jProgressBar1.setValue(0));
    }

    @Override
    public void subProgressStarted(SubProgressReceiver subProgressReceiver) throws OperationCancelled {
        // Do nothing
    }

    private void rotate() {
        buttonRotate.setEnabled(false);
        buttonCancel.setEnabled(false);
        final CoordinateTransform transform;
        final int degrees;
        if (jRadioButton1.isSelected()) {
            transform = CoordinateTransform.ROTATE_CLOCKWISE_90_DEGREES;
            degrees = 90;
        } else if (jRadioButton2.isSelected()) {
            transform = CoordinateTransform.ROTATE_180_DEGREES;
            degrees = 180;
        } else {
            transform = CoordinateTransform.ROTATE_CLOCKWISE_270_DEGREES;
            degrees = 270;
        }
        new Thread("World Rotator") {
            @Override
            public void run() {
                try {
                    if ((oppositeAnchor == null) || (! jCheckBox1.isSelected())) {
                        world.transform(anchor, transform, RotateWorldDialog.this);
                        world.addHistoryEntry(HistoryEntry.WORLD_DIMENSION_ROTATED, world.getDimension(anchor).getName(), degrees);
                    } else {
                        world.transform(anchor, transform, new SubProgressReceiver(RotateWorldDialog.this, 0.0f, 0.5f));
                        world.addHistoryEntry(HistoryEntry.WORLD_DIMENSION_ROTATED, world.getDimension(anchor).getName(), degrees);
                        world.transform(oppositeAnchor, transform, new SubProgressReceiver(RotateWorldDialog.this, 0.5f, 0.5f));
                        world.addHistoryEntry(HistoryEntry.WORLD_DIMENSION_ROTATED, world.getDimension(oppositeAnchor).getName(), degrees);
                    }
                    done();
                } catch (Throwable t) {
                    exceptionThrown(t);
                }
            }
        }.start();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        buttonCancel = new javax.swing.JButton();
        buttonRotate = new javax.swing.JButton();
        labelProgressMessage = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Rotate World");
        setResizable(false);

        jLabel1.setText("Choose a rotation angle and press the Rotate button to rotate the world:");

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(this::buttonCancelActionPerformed);

        buttonRotate.setText("Rotate");
        buttonRotate.addActionListener(this::buttonRotateActionPerformed);

        labelProgressMessage.setText(" ");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("90 degrees clockwise");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("180 degrees");

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("90 degrees anticlockwise");

        jLabel2.setText("<html><em>This operation cannot be undone!</em>   </html>");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("also rotate corresponding ceiling or surface");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonRotate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addComponent(jLabel1)
                            .addComponent(labelProgressMessage)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2)
                            .addComponent(jRadioButton3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addComponent(labelProgressMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCancel)
                    .addComponent(buttonRotate))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        cancel();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonRotateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRotateActionPerformed
        rotate();
    }//GEN-LAST:event_buttonRotateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonRotate;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JLabel labelProgressMessage;
    // End of variables declaration//GEN-END:variables

    private final World2 world;
    private final Anchor anchor, oppositeAnchor;

    private static final long serialVersionUID = 1L;
}