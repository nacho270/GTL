package ar.com.textillevel.gui.util.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJTable;

public class PanelBotonesAgregarQuitarSubirBajarModificar extends JPanel {

	private static final long serialVersionUID = -7718891968655478916L;

	private JButton btnSubir;
	private JButton btnBajar;
	private JButton btnModificar;
	private JButton btnAgregar;
	private JButton btnEliminar;

	private final FWJTable tabla;
	private boolean modoEdicion;

	public static String iconoBtnAgregar = null;
	public static String iconoBtnAgregarDeshab = null;
	public static String iconoBtnEliminar = null;
	public static String iconoBtnEliminarDeshab = null;
	public static String iconoBtnSubir = null;
	public static String iconoBtnSubirDeshab = null;
	public static String iconoBtnBajar = null;
	public static String iconoBtnBajarDeshab = null;
	public static String iconoBtnModificar = null;
	public static String iconoBtnModificarDeshab = null;

	private static final String DEFAULT_ICON_BTN_AGREGAR = "ar/com/fwcommon/imagenes/b_agregar_fila.png";
	private static final String DEFAULT_ICON_BTN_AGREGAR_DESHAB = "ar/com/fwcommon/imagenes/b_agregar_fila_des.png";
	private static final String DEFAULT_ICON_BTN_ELIMINAR = "ar/com/fwcommon/imagenes/b_sacar_fila.png";
	private static final String DEFAULT_ICON_BTN_ELIMINAR_DESHAB = "ar/com/fwcommon/imagenes/b_sacar_fila_des.png";
	private static final String DEFAULT_ICON_BTN_SUBIR = "ar/com/fwcommon/imagenes/b_subir.png";
	private static final String DEFAULT_ICON_BTN_SUBIR_DESHAB = "ar/com/fwcommon/imagenes/b_subir_des.png";
	private static final String DEFAULT_ICON_BTN_BAJAR = "ar/com/fwcommon/imagenes/b_bajar.png";
	private static final String DEFAULT_ICON_BTN_BAJAR_DESHAB = "ar/com/fwcommon/imagenes/b_bajar_des.png";
	private static final String DEFAULT_ICON_BTN_MODIFICAR = "ar/com/fwcommon/imagenes/b_modificar_fila.png";
	private static final String DEFAULT_ICON_BTN_MODIFICAR_DESHAB = "ar/com/fwcommon/imagenes/b_modificar_fila_des.png";

	public static final int DEFAULT_WIDTH = 20;
	public static final int DEFAULT_HEIGHT = 50;

	public PanelBotonesAgregarQuitarSubirBajarModificar(FWJTable tabla) {
		this(tabla, true);
	}

	public PanelBotonesAgregarQuitarSubirBajarModificar(FWJTable tabla, boolean modoEdicion) {
		this.tabla = tabla;
		construct();
		setModoEdicion(modoEdicion);
	}

	private void construct() {
		setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.gridy = 0;

		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.CENTER;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.gridy = 1;

		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.CENTER;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.gridy = 2;

		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.CENTER;
		gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints3.gridy = 3;

		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.CENTER;
		gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints4.gridy = 4;

		//Botón 'Agregar'
		String iconoAgregar = (iconoBtnAgregar == null ? DEFAULT_ICON_BTN_AGREGAR : iconoBtnAgregar);
		String iconoAgregarDeshab = (iconoBtnAgregarDeshab == null ? DEFAULT_ICON_BTN_AGREGAR_DESHAB : iconoBtnAgregarDeshab);
		btnAgregar = BossEstilos.createButton(iconoAgregar, iconoAgregarDeshab);
		btnAgregar.setEnabled(false);
		btnAgregar.setToolTipText("Agregar fila");
		add(btnAgregar, gridBagConstraints);
		btnAgregar.addActionListener(new BotonAgregarListener());

		//Botón 'Eliminar'
		String iconoEliminar = (iconoBtnEliminar == null ? DEFAULT_ICON_BTN_ELIMINAR : iconoBtnEliminar);
		String iconoEliminarDeshab = (iconoBtnEliminarDeshab == null ? DEFAULT_ICON_BTN_ELIMINAR_DESHAB : iconoBtnEliminarDeshab);
		btnEliminar = BossEstilos.createButton(iconoEliminar, iconoEliminarDeshab);
		btnEliminar.setEnabled(false);
		btnEliminar.setToolTipText("Eliminar fila");
		add(btnEliminar, gridBagConstraints1);
		btnEliminar.addActionListener(new BotonEliminarListener());

		// Botón 'Modificar'
		String iconoModificar = (iconoBtnModificar == null ? DEFAULT_ICON_BTN_MODIFICAR : iconoBtnBajar);
		String iconoModificarDeshab = (iconoBtnModificarDeshab == null ? DEFAULT_ICON_BTN_MODIFICAR_DESHAB : iconoBtnModificarDeshab);
		btnModificar = BossEstilos.createButton(iconoModificar, iconoModificarDeshab);
		btnModificar.setEnabled(false);
		btnModificar.setToolTipText("Modificar Fila");
		add(btnModificar, gridBagConstraints2);
		btnModificar.addActionListener(new BotonModificarListener());
		
		// Botón 'Subir'
		String iconoSubir = (iconoBtnSubir == null ? DEFAULT_ICON_BTN_SUBIR : iconoBtnSubir);
		String iconoSubirDeshab = (iconoBtnSubirDeshab == null ? DEFAULT_ICON_BTN_SUBIR_DESHAB : iconoBtnSubirDeshab);
		btnSubir = BossEstilos.createButton(iconoSubir, iconoSubirDeshab);
		btnSubir.setEnabled(false);
		btnSubir.setToolTipText("Subir Fila");
		add(btnSubir, gridBagConstraints3);
		btnSubir.addActionListener(new BotonSubirListener());

		// Botón 'Bajar'
		String iconoBajar = (iconoBtnBajar == null ? DEFAULT_ICON_BTN_BAJAR : iconoBtnBajar);
		String iconoBajarDeshab = (iconoBtnBajarDeshab == null ? DEFAULT_ICON_BTN_BAJAR_DESHAB : iconoBtnBajarDeshab);
		btnBajar = BossEstilos.createButton(iconoBajar, iconoBajarDeshab);
		btnBajar.setEnabled(false);
		btnBajar.setToolTipText("Bajar Fila");
		add(btnBajar, gridBagConstraints4);
		btnBajar.addActionListener(new BotonBajarListener());

		// Tabla
		tabla.getSelectionModel().addListSelectionListener(new TablaListener());
	}

	public boolean isModoEdicion() {
		return modoEdicion;
	}

	public void setModoEdicion(boolean modoEdicion) {
		this.modoEdicion = modoEdicion;
		tabla.setTableLock(!modoEdicion);
		btnSubir.setEnabled(modoEdicion ? (tabla.getSelectedRow() != -1) : false);
		btnBajar.setEnabled(modoEdicion ? (tabla.getSelectedRow() != -1) : false);
		btnModificar.setEnabled(modoEdicion ? (tabla.getSelectedRow() != -1) : false);
		btnAgregar.setEnabled(modoEdicion);
		btnEliminar.setEnabled(modoEdicion ? (tabla.getSelectedRow() != -1) : false);
	}

	public FWJTable getTabla() {
		return tabla;
	}

	public JButton getBotonAgregar() {
		return btnAgregar;
	}

	public JButton getBotonQuitar() {
		return btnEliminar;
	}

	public JButton getBotonSubir() {
		return btnSubir;
	}

	public JButton getBotonBajar() {
		return btnBajar;
	}

	public JButton getBotonModificar() {
		return btnModificar;
	}

	public void botonAgregarPresionado() {

	}

	public void botonQuitarPresionado() {

	}

	public void botonSubirPresionado() {
		
	}

	public void botonBajarPresionado() {
		
	}

	public void botonModificarPresionado() {
		
	}

	public void botonModificarPresionado(int filaseleccionada) {
		
	}

	public boolean validarAgregar() {
		return true;
	}

	public boolean validarQuitar() {
		return true;
	}

	public boolean validarSubir() {
		return true;
	}

	public boolean validarBajar() {
		return true;
	}

	public boolean validarModificar() {
		return true;
	}

	class BotonSubirListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			if (tabla.getSelectedRow() != -1) {
				if (validarSubir()) {
					botonSubirPresionado();
				}
			}
		}
	}

	class BotonBajarListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			if (tabla.getSelectedRow() != -1) {
				if (validarBajar()) {
					botonBajarPresionado();
				}
			}
		}
	}

	class BotonModificarListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			if (tabla.getSelectedRow() != -1) {
				if (validarModificar()) {
					int filaSeleccionada = getTabla().getSelectedRow();
					botonModificarPresionado(filaSeleccionada);
				}
			}
		}
	}

	class BotonAgregarListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			if (validarAgregar()) {
				tabla.addRow();
				tabla.scroll(tabla.getRowCount() - 1);
				botonAgregarPresionado();
			}
		}
	}

	class BotonEliminarListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			if (tabla.getSelectedRow() != -1) {
				if (validarQuitar()) {
					tabla.removeRows(tabla.getSelectedRows());
					if (tabla.getRowCount() == 0) {
						btnEliminar.setEnabled(false);
					}
					botonQuitarPresionado();
				}
			}
		}
	}

	class TablaListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent evt) {
			if (modoEdicion) {
				btnEliminar.setEnabled((tabla.getSelectedRow() != -1));
				btnModificar.setEnabled((tabla.getSelectedRow() != -1));
			}
		}
	}
}