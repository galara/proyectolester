/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

//import BD.BdConexion;
import Capa_Datos.BdConexion;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author 30234
 */
public class GeneraReportes {

    static java.sql.Connection conn;//getConnection intentara establecer una conexión.

    public static void AbrirReporte(String nombrereporte, Map parametros) {
        try {
            String archivo = nombrereporte;
            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(archivo);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                //System.exit(3);
            }
            conn = BdConexion.getConexion();
            JasperPrint impresor = JasperFillManager.fillReport(masterReport, parametros, conn);
            JasperViewer jviewer = new JasperViewer(impresor, false);
            jviewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            //jviewer.setTitle("Comprovante de Salida");
            jviewer.setVisible(true);

//            JInternalFrame frame = new JInternalFrame("Reporte");
//            frame.getContentPane().add(new JRViewer(impresor));
//            frame.setName("visor");
//            frame.pack();
//            frame.setVisible(true);
//
//            frame.setSize(1050, 664);
//            frame.setName(nombrereporte);
//            AddForms.adminInternalFrame(panel_center, frame);
//            frame.setClosable(true);// icono de cerrar
//            frame.setMaximizable(true);
//            frame.setIconifiable(true);
//            frame.toFront();
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.toString());
        }
    }
}
