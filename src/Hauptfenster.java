

import com.demo.tree.checkbox.SicherungsObjekt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author FSBI10
 */
public class Hauptfenster extends javax.swing.JFrame {
    private SicherungsObjekt neueSicherung;


    /**
     * Creates new form Hauptfenster
     */
    public Hauptfenster() throws FileNotFoundException, IOException {
        neueSicherung = new SicherungsObjekt();
        initComponents(); // Komponenten initialisieren
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Backup Tool v1.0");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jButton2.setText("Daten sichern");
        jButton2.setToolTipText("Durchführen einer neuen Datensicherung...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveData(evt);
            }
        });

        jButton3.setText("Sicherung zurückspielen");
        jButton3.setToolTipText("Wiederherstellen von gesicherten Daten...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Beenden");
        jButton4.setToolTipText("Programm beenden...");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseProgram(evt);
            }
        });

        jMenu1.setText("Optionen");

        jMenuItem1.setText("Voreinstellungen");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(70, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CloseProgram(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseProgram
        System.exit(0);
    }//GEN-LAST:event_CloseProgram

    // Fenster für neue Sicherung zu Erstellen aufrufen und das Sicherungsobjekt mit übergeben (Peter Riehm)
    private void SaveData(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveData
        Datenauswahl Datenauswahlfenster = new Datenauswahl(neueSicherung);
        Datenauswahlfenster.setVisible(true);
        Datenauswahlfenster.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); //von Dirk geklaut
    }//GEN-LAST:event_SaveData
    
    // Menü für die Voreinstellungen aufrufen (Alexander Nann)
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try
        {
            NewJFrame Optionen = new NewJFrame(neueSicherung);
            Optionen.setVisible(true);
            Optionen.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); //von Dirk geklaut
        } catch (Exception e) {
            
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // Zurückspielen starten (Dirk Mohr)
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String strZipFile;
        JFileChooser zipwahl = new JFileChooser();
        zipwahl.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        zipwahl.setDialogTitle("Dateiauswahl");
        
        FileNameExtensionFilter zipFilter = new FileNameExtensionFilter("Zip file", "zip");
        zipwahl.setFileFilter(zipFilter);
        neueSicherung.getZielpfad();
        zipwahl.setCurrentDirectory(new File(neueSicherung.getZielpfad()));
        
        
        int nRet = zipwahl.showOpenDialog(null);
        
        if (nRet == JFileChooser.APPROVE_OPTION)
        {
            strZipFile = zipwahl.getSelectedFile().getPath();

            if (!strZipFile.isEmpty())
            {
                Zurueckpielen entzip = new Zurueckpielen();
                entzip.setZipfile(strZipFile);
                entzip.ZeigeZipInhalt();
                entzip.setVisible(true);
                entzip.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); //von Dirk geklaut
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        // JFrame zentriert zur Bildschirmmitte, also JFrame in der Mitte des Bildschirms positionieren:
        setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Hauptfenster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Hauptfenster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Hauptfenster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Hauptfenster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Hauptfenster().setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Hauptfenster.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Hauptfenster.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    // End of variables declaration//GEN-END:variables
}
