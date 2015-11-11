/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.https://www.youtube.com/watch?v=ICF-RldvSIo
 */
package Presentacion;

import Capa_Datos.AccesoDatos;
import Capa_Datos.BdConexion;
import Capa_Negocio.AccesoUsuario;
import static Capa_Negocio.AddForms.adminInternalFrame;
import Capa_Negocio.FormatoFecha;
import Capa_Negocio.Peticiones;
import Capa_Negocio.Renderer_CheckBox;
import Capa_Negocio.Utilidades;
import static Presentacion.Principal.dp;
import Reportes.GeneraReportes;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import modelos.mGrupo;
//import modelos.mTipopago;

/**
 *
 * @author GLARA
 */
public class Ordenservicio extends javax.swing.JInternalFrame {

    /*El modelo se define en : Jtable-->propiedades-->model--> <User Code> */
    DefaultTableModel model2;
    DefaultComboBoxModel modelCombo;
    String[] titulos2 = {"Código", "Descripción", "Agregar"};//Titulos para Jtabla
    Peticiones peticiones = new Peticiones();
    public static Hashtable<String, String> hashGrupo = new Hashtable<>();
    public static Hashtable<String, String> hashTipopago = new Hashtable<>();
    AccesoDatos acceso = new AccesoDatos();
    static String idclientes = "", idvehiculos, iddetallegrupo = "";
    java.sql.Connection conn;//getConnection intentara establecer una conexión.

    /**
     * Creates new form Cliente
     */
    public Ordenservicio() {
        initComponents();
        setFiltroTexto();
        addEscapeKey();
        llenarcombogrupo();
        MostrarProductos();
        otrosproductos.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                //sumartotal();
                //formatotabla();
            }
        });
        JCheckBox check = new JCheckBox();
        otrosproductos.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(check));
        otrosproductos.getColumnModel().getColumn(2).setCellRenderer(new Renderer_CheckBox());
    }

    /*addEscapeKey agrega a este JInternalFrame un evento de cerrarVentana() al presionar la tecla "ESC" */
    private void addEscapeKey() {
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarVentana();
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }

    /*Este metodo visualiza una mensage de cinfirmación al usuario antes de Cerrar la ventana,
     * si por eror se intento cerrar el formulario devera indicar que "NO" para no perder los datos
     * que no haya Guardado de lo contrario presiona "SI" y se cerrara la ventana sin Guardar ningun dato. */
    private void cerrarVentana() {
        int nu = JOptionPane.showInternalConfirmDialog(this, "Todos los datos que no se ha guardado"
                + "se perderan.\n"
                + "¿Desea Cerrar esta ventana?", "Cerrar ventana", JOptionPane.YES_NO_OPTION);
        if (nu == JOptionPane.YES_OPTION || nu == 0) {
            Utilidades.setEditableTexto(this.JPanelGrupo, false, null, true, "");
            Utilidades.esObligatorio(this.JPanelGrupo, false);
            this.bntGuardar.setEnabled(false);
            //removejtable();
            nitcliente.setText("");
            nitcliente.requestFocus();
            this.dispose();
        }
    }

    public void removejtable2() {
        while (otrosproductos.getRowCount() != 0) {
            model2.removeRow(0);
        }
    }

    private void limpiartodo() {

        nitcliente.setText("");
        nitcliente.requestFocus();
        matricula.setText("");
        vehiculo.setText("");
        descripcion.setText("");
        modelo.setText("");
        marca.setText("");
        fechaorden.setDate(null); //setText("");
        fechaentrega.setDate(null);
        nombre.setText("");
        telefono.setText("");
        direccion.setText("");
        cempleado.setSelectedIndex(-1);
        Utilidades.esObligatorio(this.JPanelRecibo, false);
        Utilidades.esObligatorio(this.JPanelGrupo, false);
        Utilidades.esObligatorio(this.JPanelPago, false);
        nitcliente.requestFocus();
        llenarcombogrupo();
    }

    /*
     *Prepara los parametros para la consulta de datos que deseamos agregar al model del ComboBox
     *y se los envia a un metodo interno getRegistroCombo() 
     *
     */
    public static void llenarcombogrupo() {
        String Dato = "1";
        String[] campos = {"empleado.nombres", "empleado.apellidos", "empleado.idempleado"};
        String[] condiciones = {"empleado.estado"};
        String[] Id = {Dato};
        cempleado.removeAllItems();
        //String inner = " INNER JOIN alumnosengrupo ON grupo.idgrupo = alumnosengrupo.grupo_idgrupo INNER JOIN alumno ON alumnosengrupo.alumno_idalumno = alumno.idalumno ";
        getRegistroCombo("empleado", campos, condiciones, Id, "");

    }

    /*El metodo llenarcombo() envia los parametros para la consulta a la BD y el medoto
     *getRegistroCombo() se encarga de enviarlos a la capa de AccesoDatos.getRegistros()
     *quiern devolcera un ResultSet para luego obtener los valores y agregarlos al JConboBox
     *y a una Hashtable que nos servira para obtener el id y seleccionar valores.
     */
    public static void getRegistroCombo(String tabla, String[] campos, String[] campocondicion, String[] condicionid, String inner) {
        try {
            ResultSet rs;
            AccesoDatos ac = new AccesoDatos();

            rs = ac.getRegistros(tabla, campos, campocondicion, condicionid, inner);

            int cantcampos = campos.length;
            if (rs != null) {

                DefaultComboBoxModel modeloComboBox;
                modeloComboBox = new DefaultComboBoxModel();
                cempleado.setModel(modeloComboBox);

                modeloComboBox.addElement(new mGrupo("", "0"));
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    int count = 0;
                    rs.beforeFirst();//regresa el puntero al primer registro
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        count++;
                        modeloComboBox.addElement(new mGrupo(rs.getString(1) + " " + rs.getString(2), "" + rs.getInt(3)));
                        hashGrupo.put(rs.getString(1) + " " + rs.getString(2), "" + count);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//    /*
//     * Metodo para buscar un alumno por su codigo devuelde el id
//     */
//    public void balumnocodigo(String codigo) {
//        if (codigo.isEmpty()) {
//            nombre.setText("");
//            telefono.setText("");
//            estado.setText("");
//            cempleado.removeAllItems();
//            idclientes = "";
//            telefono.setText("");
//            direccion.setText("");
//
//        } else if (!codigo.isEmpty()) {
//
//            ResultSet rs;
//            AccesoDatos ac = new AccesoDatos();
//
//            String[] campos = {"alumno.codigo", "alumno.nombres", "alumno.apellidos", "DATE_FORMAT(alumno.fechanacimiento,'%d-%m-%Y')", "alumno.estado", "alumno.idalumno"};
//            String[] cond = {"alumno.codigo"};
//            String[] id = {codigo};
//
//            if (!id.equals(0)) {
//
//                rs = ac.getRegistros("alumno", campos, cond, id, "");
//
//                if (rs != null) {
//                    try {
//                        if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
//                            rs.beforeFirst();//regresa el puntero al primer registro
//                            while (rs.next()) {//mientras tenga registros que haga lo siguiente
//                                nitcliente.setText(rs.getString(1));
//                                llenarcombogrupo();
//                                nombre.setText(rs.getString(2) + " " + rs.getString(3));
//                                if (rs.getString(5).equals("0")) {
//                                    estado.setText("Inactivo");
//                                    estado.setForeground(Color.red);
//                                } else if (rs.getString(5).equals("1")) {
//                                    estado.setText("Activo");
//                                    estado.setForeground(Color.WHITE/*new java.awt.Color(102, 204, 0)*/);
//                                }
//                                idclientes = (rs.getString(6));
//                            }
//                        } else {
//                            JOptionPane.showInternalMessageDialog(this, " El codigo no fue encontrado ");
//                            limpiartodo();
//                        }
//                    } catch (SQLException e) {
//                        JOptionPane.showInternalMessageDialog(this, e);
//                    }
//                } else {
//                    JOptionPane.showInternalMessageDialog(this, " El codigo no fue encontrado ");
//                    limpiartodo();
//                }
//
//            }
//        }
//    }
    private void MostrarProductos() {

        String sql = "SELECT servicio.idservicio,servicio.descripcion FROM servicio order by servicio.descripcion";

        removejtable2();
        model2 = getRegistroPorLikell(model2, sql);
        Utilidades.ajustarAnchoColumnas(otrosproductos);
    }

    /**
     * Para una condicion WHERE condicionid LIKE '% campocondicion' * @param
     * modelo ,modelo de la JTable
     *
     * @param tabla , el nombre de la tabla a consultar en la BD
     * @param campocondicion , los campos de la tabla para las condiciones ejem:
     * id,estado etc
     * @return
     */
    public DefaultTableModel getRegistroPorLikell(DefaultTableModel modelo, String tabla) {
        try {

            ResultSet rs;

            rs = acceso.getRegistroProc(tabla);
            int cantcampos = 3;
            //if (rs != null) {
            if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                //int count = 0;
                rs.beforeFirst();//regresa el puntero al primer registro
                Object[] fila = new Object[cantcampos];

                while (rs.next()) {//mientras tenga registros que haga lo siguiente
                    fila[0] = rs.getString(1);
                    fila[1] = rs.getString(2);
                    //fila[2] = Float.parseFloat(rs.getString(3));
                    //fila[3] = 1.0;
                    //fila[4] = (Math.round((1.0 * Float.parseFloat(rs.getString(3))) * 100.0) / 100.0);
                    fila[2] = false;
                    modelo.addRow(fila);
                }

            } //} 
            else {
                // JOptionPane.showMessageDialog(null, "No se encontraron datos para la busqueda", "Mensage", JOptionPane.INFORMATION_MESSAGE);
            }
            rs.close();
            return modelo;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un Error :" + ex, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /* Este metodo se encarga de filtrar los datos que se deben ingresar en cada uno de los campos del formulario
     * podemos indicar que el usuario ingrese solo numeros , solo letras, numeros y letras, o cualquier caracter
     * tambien podemos validar si se aseptaran espacios en blanco en la cadena ingresada , para mas detalle visualizar
     * la clase TipoFiltro()  */
    private void setFiltroTexto() {
        //TipoFiltro.setFiltraEntrada(codigo.getDocument(), FiltroCampos.NUM_LETRAS, 45, false);
        //TipoFiltro.setFiltraEntrada(descripcion.getDocument(), FiltroCampos.NUM_LETRAS, 60, true);
        //TipoFiltro.setFiltraEntrada(dia.getDocument(), FiltroCampos.SOLO_LETRAS, 45, false);
        //TipoFiltro.setFiltraEntrada(profesor.getDocument(), FiltroCampos.NUM_LETRAS, 200, true);
        //TipoFiltro.setFiltraEntrada(cantalumnos.getDocument(), FiltroCampos.SOLO_NUMEROS, 5, true);
//        TipoFiltro.setFiltraEntrada(colegiatura.getDocument(), FiltroCampos.SOLO_NUMEROS, 12, false);
        //TipoFiltro.setFiltraEntrada(busqueda.getDocument(), FiltroCampos.NUM_LETRAS, 100, true);
    }

    public void idalumnosengrupo(String idalumno, String idgrupo) {

        String[] id = {idalumno, idgrupo};
        ResultSet rs;
        AccesoDatos ac = new AccesoDatos();
        String[] cond = {"alumnosengrupo.alumno_idalumno", "alumnosengrupo.grupo_idgrupo"};
        String[] campos = {"alumnosengrupo.iddetallegrupo", "alumnosengrupo.fechainicio", "alumnosengrupo.beca"};
        rs = ac.getRegistros("alumnosengrupo", campos, cond, id, "");

        if (rs != null) {
            try {
                if (rs.next()) {//verifica si esta vacio, pero desplaza el puntero al siguiente elemento
                    rs.beforeFirst();//regresa el puntero al primer registro
                    while (rs.next()) {//mientras tenga registros que haga lo siguiente
                        iddetallegrupo = (rs.getString(1));
                        String fechainicio = FormatoFecha.getFormato(rs.getDate(2), FormatoFecha.D_M_A);
                        //inicioalumno.setText(fechainicio);
                        float becac = Float.parseFloat(rs.getString(3));
                        telefono.setText("" + becac);
                    }
                }
            } catch (SQLException e) {
                iddetallegrupo = "";
                JOptionPane.showInternalMessageDialog(this, e);
            }
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
        java.awt.GridBagConstraints gridBagConstraints;

        panelImage = new elaprendiz.gui.panel.PanelImage();
        pnlActionButtons = new javax.swing.JPanel();
        bntGuardar = new elaprendiz.gui.button.ButtonRect();
        bntCancelar = new elaprendiz.gui.button.ButtonRect();
        bntSalir = new elaprendiz.gui.button.ButtonRect();
        JPanelGrupo = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cempleado = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        matricula = new elaprendiz.gui.textField.TextField();
        marca = new elaprendiz.gui.textField.TextField();
        modelo = new elaprendiz.gui.textField.TextField();
        vehiculo = new elaprendiz.gui.textField.TextField();
        jButton2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descripcion = new javax.swing.JTextArea();
        jLabel18 = new javax.swing.JLabel();
        JPanelTable = new javax.swing.JPanel();
        tbPane = new elaprendiz.gui.panel.TabbedPaneHeader();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        otrosproductos = new javax.swing.JTable();
        JPanelBusqueda = new javax.swing.JPanel();
        nitcliente = new elaprendiz.gui.textField.TextField();
        jLabel16 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        nombre = new elaprendiz.gui.textField.TextField();
        jLabel19 = new javax.swing.JLabel();
        estado = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        direccion = new elaprendiz.gui.textField.TextField();
        telefono = new elaprendiz.gui.textField.TextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        tbPane1 = new elaprendiz.gui.panel.TabbedPaneHeader();
        JPanelPago = new javax.swing.JPanel();
        JPanelRecibo = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        clockDigital2 = new elaprendiz.gui.varios.ClockDigital();
        jLabel23 = new javax.swing.JLabel();
        fechaorden = new com.toedter.calendar.JDateChooser();
        jLabel24 = new javax.swing.JLabel();
        fechaentrega = new com.toedter.calendar.JDateChooser();
        pnlPaginador1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setForeground(new java.awt.Color(0, 0, 0));
        setIconifiable(true);
        setTitle("Orden Servicio");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Orden Servicio"); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        panelImage.setLayout(null);

        pnlActionButtons.setBackground(java.awt.SystemColor.activeCaption);
        pnlActionButtons.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
        pnlActionButtons.setForeground(new java.awt.Color(204, 204, 204));
        pnlActionButtons.setPreferredSize(new java.awt.Dimension(786, 52));
        pnlActionButtons.setLayout(new java.awt.GridBagLayout());

        bntGuardar.setBackground(new java.awt.Color(51, 153, 255));
        bntGuardar.setMnemonic(KeyEvent.VK_G);
        bntGuardar.setText("Guardar");
        bntGuardar.setName("Guardar Pagos"); // NOI18N
        bntGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntGuardarActionPerformed(evt);
            }
        });
        pnlActionButtons.add(bntGuardar, new java.awt.GridBagConstraints());

        bntCancelar.setBackground(new java.awt.Color(51, 153, 255));
        bntCancelar.setMnemonic(KeyEvent.VK_X);
        bntCancelar.setText("Cancelar");
        bntCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntCancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 0);
        pnlActionButtons.add(bntCancelar, gridBagConstraints);

        bntSalir.setBackground(new java.awt.Color(51, 153, 255));
        bntSalir.setText("Salir    ");
        bntSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntSalirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 12, 93);
        pnlActionButtons.add(bntSalir, gridBagConstraints);

        panelImage.add(pnlActionButtons);
        pnlActionButtons.setBounds(0, 580, 880, 50);

        JPanelGrupo.setBackground(java.awt.SystemColor.activeCaption);
        JPanelGrupo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JPanelGrupo.setForeground(new java.awt.Color(204, 204, 204));
        JPanelGrupo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        JPanelGrupo.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Marca :");
        JPanelGrupo.add(jLabel10);
        jLabel10.setBounds(540, 10, 140, 20);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Matricula:");
        JPanelGrupo.add(jLabel3);
        jLabel3.setBounds(260, 10, 140, 20);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Modelo :");
        JPanelGrupo.add(jLabel13);
        jLabel13.setBounds(410, 10, 110, 20);

        cempleado.setEditable(true);
        cempleado.setName("Mecanico"); // NOI18N
        JPanelGrupo.add(cempleado);
        cempleado.setBounds(120, 70, 210, 24);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Vehiculo:");
        JPanelGrupo.add(jLabel7);
        jLabel7.setBounds(30, 30, 80, 27);

        matricula.setEditable(false);
        matricula.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        matricula.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(matricula);
        matricula.setBounds(260, 30, 140, 24);

        marca.setEditable(false);
        marca.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        marca.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(marca);
        marca.setBounds(540, 30, 140, 24);

        modelo.setEditable(false);
        modelo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        modelo.setPreferredSize(new java.awt.Dimension(120, 21));
        JPanelGrupo.add(modelo);
        modelo.setBounds(410, 30, 110, 24);

        vehiculo.setName("Vehiculo"); // NOI18N
        vehiculo.setPreferredSize(new java.awt.Dimension(250, 27));
        vehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehiculoActionPerformed(evt);
            }
        });
        JPanelGrupo.add(vehiculo);
        vehiculo.setBounds(120, 30, 97, 24);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar 2.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        JPanelGrupo.add(jButton2);
        jButton2.setBounds(220, 30, 20, 24);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel17.setText("Observacion :");
        JPanelGrupo.add(jLabel17);
        jLabel17.setBounds(10, 110, 100, 24);

        descripcion.setColumns(20);
        descripcion.setRows(5);
        jScrollPane1.setViewportView(descripcion);

        JPanelGrupo.add(jScrollPane1);
        jScrollPane1.setBounds(120, 100, 590, 40);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel18.setText("Técnico  :");
        JPanelGrupo.add(jLabel18);
        jLabel18.setBounds(10, 70, 100, 24);

        panelImage.add(JPanelGrupo);
        JPanelGrupo.setBounds(0, 180, 880, 150);

        JPanelTable.setOpaque(false);
        JPanelTable.setPreferredSize(new java.awt.Dimension(786, 402));
        JPanelTable.setLayout(new java.awt.BorderLayout());

        tbPane.setOpaque(true);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setOpaque(false);

        otrosproductos.setModel(model2 = new DefaultTableModel(null, titulos2)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if(column==2){
                        return true;
                    }else{
                        return false;}
                }
            });
            otrosproductos.setFocusCycleRoot(true);
            otrosproductos.setGridColor(new java.awt.Color(51, 51, 255));
            otrosproductos.setName("otrosproductos"); // NOI18N
            otrosproductos.setRowHeight(20);
            otrosproductos.setSelectionBackground(java.awt.SystemColor.activeCaption);
            jScrollPane2.setViewportView(otrosproductos);

            jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 756, 210));

            tbPane.addTab("Detalle de Sercvicios", jPanel4);

            JPanelTable.add(tbPane, java.awt.BorderLayout.CENTER);

            panelImage.add(JPanelTable);
            JPanelTable.setBounds(0, 330, 760, 250);

            JPanelBusqueda.setBackground(java.awt.SystemColor.inactiveCaption);
            JPanelBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            JPanelBusqueda.setLayout(null);

            nitcliente.setName("Cliente"); // NOI18N
            nitcliente.setPreferredSize(new java.awt.Dimension(250, 27));
            nitcliente.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    nitclienteActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(nitcliente);
            nitcliente.setBounds(120, 10, 97, 24);

            jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            jLabel16.setText("Cliente :");
            JPanelBusqueda.add(jLabel16);
            jLabel16.setBounds(10, 10, 100, 24);

            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar 2.png"))); // NOI18N
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
            JPanelBusqueda.add(jButton1);
            jButton1.setBounds(220, 10, 20, 24);

            nombre.setEditable(false);
            nombre.setPreferredSize(new java.awt.Dimension(250, 27));
            JPanelBusqueda.add(nombre);
            nombre.setBounds(440, 10, 270, 24);

            jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            jLabel19.setText("Alumno:");
            JPanelBusqueda.add(jLabel19);
            jLabel19.setBounds(310, 10, 120, 24);

            estado.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
            estado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            JPanelBusqueda.add(estado);
            estado.setBounds(700, 50, 110, 27);

            jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel12.setText("Dirección :");
            JPanelBusqueda.add(jLabel12);
            jLabel12.setBounds(360, 40, 80, 20);

            direccion.setEditable(false);
            direccion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
            direccion.setPreferredSize(new java.awt.Dimension(120, 21));
            JPanelBusqueda.add(direccion);
            direccion.setBounds(440, 40, 270, 24);

            telefono.setEditable(false);
            telefono.setHorizontalAlignment(javax.swing.JTextField.LEFT);
            telefono.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
            telefono.setPreferredSize(new java.awt.Dimension(120, 21));
            JPanelBusqueda.add(telefono);
            telefono.setBounds(740, 40, 120, 24);

            jLabel25.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
            jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel25.setText("Telefono :");
            JPanelBusqueda.add(jLabel25);
            jLabel25.setBounds(740, 17, 120, 20);

            panelImage.add(JPanelBusqueda);
            JPanelBusqueda.setBounds(0, 110, 880, 70);

            jPanel1.setBackground(new java.awt.Color(51, 51, 51));
            jPanel1.setLayout(new java.awt.BorderLayout());

            tbPane1.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
            tbPane1.setOpaque(true);

            JPanelPago.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            JPanelPago.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            tbPane1.addTab("============", JPanelPago);

            jPanel1.add(tbPane1, java.awt.BorderLayout.CENTER);
            tbPane1.getAccessibleContext().setAccessibleName("TOTAL");

            panelImage.add(jPanel1);
            jPanel1.setBounds(760, 330, 120, 250);

            JPanelRecibo.setBackground(java.awt.SystemColor.activeCaption);
            JPanelRecibo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            JPanelRecibo.setLayout(null);

            jLabel21.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
            jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel21.setText("Hora");
            JPanelRecibo.add(jLabel21);
            jLabel21.setBounds(690, 10, 100, 19);

            clockDigital2.setForeground(new java.awt.Color(255, 255, 255));
            clockDigital2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 16)); // NOI18N
            JPanelRecibo.add(clockDigital2);
            clockDigital2.setBounds(690, 30, 100, 27);

            jLabel23.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
            jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel23.setText("Fecha Orden");
            JPanelRecibo.add(jLabel23);
            jLabel23.setBounds(120, 10, 120, 19);

            fechaorden.setDate(Calendar.getInstance().getTime());
            fechaorden.setDateFormatString("dd/MM/yyyy");
            fechaorden.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
            fechaorden.setMaxSelectableDate(new java.util.Date(3093496470100000L));
            fechaorden.setMinSelectableDate(new java.util.Date(-62135744300000L));
            fechaorden.setPreferredSize(new java.awt.Dimension(120, 22));
            JPanelRecibo.add(fechaorden);
            fechaorden.setBounds(120, 30, 120, 27);

            jLabel24.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
            jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel24.setText("Fecha Entrega");
            JPanelRecibo.add(jLabel24);
            jLabel24.setBounds(260, 10, 120, 19);

            fechaentrega.setDate(Calendar.getInstance().getTime());
            fechaentrega.setDateFormatString("dd/MM/yyyy");
            fechaentrega.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
            fechaentrega.setMaxSelectableDate(new java.util.Date(3093496470100000L));
            fechaentrega.setMinSelectableDate(new java.util.Date(-62135744300000L));
            fechaentrega.setPreferredSize(new java.awt.Dimension(120, 22));
            JPanelRecibo.add(fechaentrega);
            fechaentrega.setBounds(260, 30, 120, 27);

            panelImage.add(JPanelRecibo);
            JPanelRecibo.setBounds(0, 40, 880, 70);

            pnlPaginador1.setBackground(new java.awt.Color(57, 104, 163));
            pnlPaginador1.setPreferredSize(new java.awt.Dimension(786, 40));
            pnlPaginador1.setLayout(new java.awt.GridBagLayout());

            jLabel11.setFont(new java.awt.Font("Script MT Bold", 1, 32)); // NOI18N
            jLabel11.setForeground(new java.awt.Color(255, 255, 255));
            jLabel11.setText("<--Orden de Servicio-->");
            pnlPaginador1.add(jLabel11, new java.awt.GridBagConstraints());

            panelImage.add(pnlPaginador1);
            pnlPaginador1.setBounds(0, 0, 880, 40);

            getContentPane().add(panelImage, java.awt.BorderLayout.CENTER);

            getAccessibleContext().setAccessibleName("Profesores");

            setBounds(0, 0, 890, 662);
        }// </editor-fold>//GEN-END:initComponents

    private void bntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntSalirActionPerformed
        cerrarVentana();
    }//GEN-LAST:event_bntSalirActionPerformed

    private void bntCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntCancelarActionPerformed
        // TODO add your handling code here:
        limpiartodo();
    }//GEN-LAST:event_bntCancelarActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cerrarVentana();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        BuscarCliente frmBuscarAlumno = new BuscarCliente();
        if (frmBuscarAlumno == null) {
            frmBuscarAlumno = new BuscarCliente();
        }
        adminInternalFrame(dp, frmBuscarAlumno);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void nitclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nitclienteActionPerformed
        // TODO add your handling code here:

        //balumnocodigo(nitcliente.getText());

    }//GEN-LAST:event_nitclienteActionPerformed

    private void bntGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntGuardarActionPerformed
        // TODO add your handling code here:
        if (AccesoUsuario.AccesosUsuario(bntGuardar.getName()) == true) {

            if (Utilidades.esObligatorio(this.JPanelRecibo, true)
                    || Utilidades.esObligatorio(this.JPanelGrupo, true)
                    || Utilidades.esObligatorio(this.JPanelBusqueda, true)
                    || Utilidades.esObligatorio(this.JPanelPago, true)) {
                JOptionPane.showInternalMessageDialog(this, "Los campos marcados son Obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (/*colegiaturas.getRowCount() == 0*/ /*&& colegiaturas.getSelectedRow() == -1*/ /*&&*/otrosproductos.getRowCount() == 0 || otrosproductos.getRowCount() == -1) {
                JOptionPane.showMessageDialog(null, "La tabla no contiene datos");

            } else { //Inicio de Guardar datos
                int resp = JOptionPane.showInternalConfirmDialog(this, "¿Desea Grabar el Registro?", "Pregunta", 0);
                if (resp == 0) {

                    //String printHorario = "";
                    modelo.getText();
                    marca.getText();
                    // printHorario = modelo.getText() + " a " + marca.getText() + " " + direccioncliente.getText();
                    //GUARDAR DATOS DE RECIBO***************************************
                    //**************************************************************
                    int idorden = 0, n = 0;
                    String sql = "";

                    String fechaord = FormatoFecha.getFormato(fechaorden.getCalendar().getTime(), FormatoFecha.A_M_D);
                    String fechaentreg = FormatoFecha.getFormato(fechaentrega.getCalendar().getTime(), FormatoFecha.A_M_D);
                    //mTipopago tipop = (mTipopago) cTipopago.getSelectedItem();
                    //String idtipop = tipop.getID();
                    //float total = Float.parseFloat(totalapagar.getText());

                    sql = "insert into ordenservicio (descripcion,idvehiculo,idcliente,fecha_orden,fecha_entrega,estado,idempleado,usuario_idusuario) values (?,?,?,?,?,?,?,?)";
                    //int op = 0;
                    PreparedStatement ps = null;
                    conn = BdConexion.getConexion();
                    mGrupo grup = (mGrupo) cempleado.getSelectedItem();
                    String cidempleado = grup.getID(); //idempleado
                    String idusuario = AccesoUsuario.getidUsuario(); //idusuario
                    String idclient = idclientes; //idcliente
                    String idvehicul = idvehiculos; //idvehiculo

                    try {
                        conn.setAutoCommit(false);
                        ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                        ps.setString(1, descripcion.getText());
                        ps.setInt(2, Integer.parseInt(idvehiculos));
                        ps.setInt(3, Integer.parseInt(idclientes));
                        ps.setString(4, fechaord);
                        ps.setString(5, fechaentreg);
                        ps.setString(6, "1");
                        ps.setInt(7, Integer.parseInt(cidempleado));
                        ps.setInt(8, Integer.parseInt(idusuario));

                        n = ps.executeUpdate();
                        if (n > 0) {
                            ResultSet rs = ps.getGeneratedKeys();
                            while (rs.next()) {
                                idorden = rs.getInt(1);//retorna el idrecibo guardado
                            }

                            //GUARDAR ***************
                            //******************************************************
                            boolean camprec = false;
                            int cant2 = model2.getRowCount();

                            for (int i = 0; i < cant2; i++) {//for pago otros productos
                                if (otrosproductos.getValueAt(i, 2).toString().equals("true")) {

                                    String id = (String) "" + otrosproductos.getValueAt(i, 0);
                                    //float canti = Float.parseFloat(otrosproductos.getValueAt(i, 3).toString());
                                    //float prec = Float.parseFloat(otrosproductos.getValueAt(i, 2).toString());

                                    String descriprecibo = "insert into ordenserviciodetalle (idservicio,idordenservicio) "
                                            + "values ('" + id + "','" + idorden + "')";
                                    camprec = true;
                                    n = ps.executeUpdate(descriprecibo);
                                }
                            }//fin for otros productos

                            if (!camprec) {
                                JOptionPane.showInternalMessageDialog(this, "No se ha marcado ningun Servicio", "Mensage", JOptionPane.INFORMATION_MESSAGE);
                                System.out.print(n);
                            }
                            if (n > 0) {
                                int resp2 = JOptionPane.showInternalConfirmDialog(this, "El Servicio se ha Guardado Correctamente\n ¿Desea realizar otro Pago de este Alumno?", "Pregunta", 0);
                                if (resp2 == 0) {
                                    
                                //    *******************************
                                //String fechaini = FormatoFecha.getFormato(fechainicio.getCalendar().getTime(), FormatoFecha.A_M_D);
                                //String fechafn = FormatoFecha.getFormato(fechafin.getCalendar().getTime(), FormatoFecha.A_M_D);
                                String nombrereporte = "report1.jasper";
                                Map parametros = new HashMap();
                                parametros.put("idordenservicio", idorden);
                                //parametros.put("fecha2", fechafn);
                                GeneraReportes.AbrirReporte(nombrereporte, parametros);

                                //          **********************
                                    
                                    MostrarProductos();
                                    limpiartodo();

                                } else {
                                    limpiartodo();
                                }
                            }
                            //FIN GUARDAR *************************************
                            //**************************************************************
                        }

                        conn.commit();// guarda todas las consultas si no ubo error
                        ps.close();
                        if (!conn.getAutoCommit()) {
                            conn.setAutoCommit(true);
                        }

                        //Recibodepago.comprobante(idrecibo, printHorario);
                    } catch (SQLException ex) {
                        try {
                            conn.rollback();// no guarda ninguna de las consultas ya que ubo error
                            ps.close();
                            if (!conn.getAutoCommit()) {
                                conn.setAutoCommit(true);
                            }
                        } catch (SQLException ex1) {
                            Logger.getLogger(Ordenservicio.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            }//Fin Guardar datos
        } else {
            JOptionPane.showInternalMessageDialog(this, "No tiene Acceso para realizar esta operación ");
        }
    }//GEN-LAST:event_bntGuardarActionPerformed

    private void vehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehiculoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vehiculoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        BuscarVehiculo frmBuscarAlumno = new BuscarVehiculo();
        if (frmBuscarAlumno == null) {
            frmBuscarAlumno = new BuscarVehiculo();
        }
        adminInternalFrame(dp, frmBuscarAlumno);
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelBusqueda;
    private javax.swing.JPanel JPanelGrupo;
    private javax.swing.JPanel JPanelPago;
    private javax.swing.JPanel JPanelRecibo;
    private javax.swing.JPanel JPanelTable;
    private elaprendiz.gui.button.ButtonRect bntCancelar;
    private elaprendiz.gui.button.ButtonRect bntGuardar;
    private elaprendiz.gui.button.ButtonRect bntSalir;
    public static javax.swing.JComboBox cempleado;
    private elaprendiz.gui.varios.ClockDigital clockDigital2;
    private javax.swing.JTextArea descripcion;
    public static elaprendiz.gui.textField.TextField direccion;
    public static javax.swing.JLabel estado;
    public static com.toedter.calendar.JDateChooser fechaentrega;
    public static com.toedter.calendar.JDateChooser fechaorden;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static elaprendiz.gui.textField.TextField marca;
    public static elaprendiz.gui.textField.TextField matricula;
    public static elaprendiz.gui.textField.TextField modelo;
    public static elaprendiz.gui.textField.TextField nitcliente;
    public static elaprendiz.gui.textField.TextField nombre;
    private javax.swing.JTable otrosproductos;
    private elaprendiz.gui.panel.PanelImage panelImage;
    private javax.swing.JPanel pnlActionButtons;
    private javax.swing.JPanel pnlPaginador1;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane;
    private elaprendiz.gui.panel.TabbedPaneHeader tbPane1;
    public static elaprendiz.gui.textField.TextField telefono;
    public static elaprendiz.gui.textField.TextField vehiculo;
    // End of variables declaration//GEN-END:variables
}
