/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * principal.java
 *
 * Created on 18-02-2015, 14:59:34
 */
package BackupMySQL;

import Capa_Datos.BdConexion;
import Capa_Negocio.AccesoUsuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author GLARA
 */
public class Backup extends javax.swing.JInternalFrame {

    JFileChooser RealizarBackupMySQL = new JFileChooser();

    /**
     * Creates new form principal
     */
    public Backup() {
        initComponents();
    }

    void GenerarBackupMySQL() {
        int resp;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        
        String fecha = "" + c.get(Calendar.DATE);
        fecha = fecha + "-" + c.get(Calendar.MONTH);//String.valueOf(c.get(Calendar.MONTH));
        fecha = fecha + "-" + c.get(Calendar.YEAR);
        //String nombre = this.jTextField1.getText();
        //String pass = this.jTextField2.getText();
        RealizarBackupMySQL.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        resp = RealizarBackupMySQL.showSaveDialog(this);//JFileChooser de nombre RealizarBackupMySQL
        if (resp == JFileChooser.APPROVE_OPTION) {//Si el usuario presiona aceptar; se genera el Backup
            try {
                Runtime runtime = Runtime.getRuntime();
                File backupFile = new File(String.valueOf(RealizarBackupMySQL.getSelectedFile().toString())
                        + "_" + fecha + ".sql");
                FileWriter fw = new FileWriter(backupFile);
                Process child = runtime.exec("C:\\Archivos de programa\\MySQL\\MySQL Server 5.5\\bin\\mysqldump -u "+BdConexion.user+" -p"+BdConexion.pass+" --default-character_set=utf8 "+BdConexion.dataBase);
                InputStreamReader irs = new InputStreamReader(child.getInputStream());
                BufferedReader br = new BufferedReader(irs);

                String line;
                while ((line = br.readLine()) != null) {
                    fw.write(line + "\n");
                }
                fw.close();
                irs.close();
                br.close();

            } catch (IOException e) {
                JOptionPane.showInternalMessageDialog(this, "Error no se Genero el Backup por el siguiente motivo:" + e.getMessage(), "Verificar", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showInternalMessageDialog(this, "El Backup se ha Generado Correctamente", "Mensage", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else if (resp == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showInternalMessageDialog(this, "Ha sido cancelada la Generacion del Backup");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonAction1 = new elaprendiz.gui.button.ButtonAction();

        setClosable(true);
        setMinimumSize(new java.awt.Dimension(299, 93));
        setName("Crear Backup"); // NOI18N
        setPreferredSize(new java.awt.Dimension(299, 93));
        setRequestFocusEnabled(false);
        setVisible(true);

        buttonAction1.setText("Generar Backup BD");
        buttonAction1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        buttonAction1.setName("Crear Backup Backup"); // NOI18N
        buttonAction1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAction1ActionPerformed(evt);
            }
        });
        getContentPane().add(buttonAction1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAction1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAction1ActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(buttonAction1.getName()) == true) {
            GenerarBackupMySQL();
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");

        }

    }//GEN-LAST:event_buttonAction1ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private elaprendiz.gui.button.ButtonAction buttonAction1;
    // End of variables declaration//GEN-END:variables
}
