
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author FSBI10
 */
public class ZeigeLog extends javax.swing.JFrame {

    /**
     * Creates new form ZeigeLog
     */
    public ZeigeLog() throws IOException {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAbbrechen = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnAbbrechen.setText("OK");
        btnAbbrechen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbbrechenActionPerformed(evt);
            }
        });

        txtLog.setColumns(20);
        txtLog.setRows(5);
        txtLog.setEnabled(false);
        jScrollPane1.setViewportView(txtLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(301, 301, 301)
                .addComponent(btnAbbrechen)
                .addContainerGap(380, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnAbbrechen))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAbbrechenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbbrechenActionPerformed
        dispose();
    }//GEN-LAST:event_btnAbbrechenActionPerformed

    /**
     * @param args the command line arguments
     */
    public void LeseLog(String strFile) throws IOException
    {
        FileReader fr = null;
        String strLog = "";
        String line = "";

        try {
            fr = new FileReader(strFile);
            try (BufferedReader br = new BufferedReader(fr)) {
                while((line = br.readLine()) != null) {
                    strLog += line + "\r\n";
                }
            }        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ZeigeLog.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(ZeigeLog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
        txtLog.setText(strLog);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbbrechen;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}