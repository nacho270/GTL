package ar.com.fwcommon.componentes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ar.com.fwcommon.boss.BossEstilos;

/**
 * Componente que representa una versión reducida de CLBotonesTabla.
 */
@SuppressWarnings("serial")
public class FWBotonesTablaLight extends JPanel {

	private JButton btnAgregar;
	private JButton btnEliminar;
	private FWJTable tabla;
	private boolean modoEdicion;
	public static String iconoBtnAgregar = null;
	public static String iconoBtnAgregarDeshab = null;
	public static String iconoBtnEliminar = null;
	public static String iconoBtnEliminarDeshab = null;
	private static final String DEFAULT_ICON_BTN_AGREGAR = "ar/com/fwcommon/imagenes/b_agregar_fila.png";
	private static final String DEFAULT_ICON_BTN_AGREGAR_DESHAB = "ar/com/fwcommon/imagenes/b_agregar_fila_des.png";
	private static final String DEFAULT_ICON_BTN_ELIMINAR = "ar/com/fwcommon/imagenes/b_sacar_fila.png";
	private static final String DEFAULT_ICON_BTN_ELIMINAR_DESHAB = "ar/com/fwcommon/imagenes/b_sacar_fila_des.png";
	public static final int DEFAULT_WIDTH = 20;
	public static final int DEFAULT_HEIGHT = 50;

	/**
	 * Método constructor.
	 * @param tabla
	 */
	public FWBotonesTablaLight(FWJTable tabla) {
		this(tabla, true);
	}

	/**
	 * Método constructor.
	 * @param tabla
	 * @param modoEdicion
	 */
	public FWBotonesTablaLight(FWJTable tabla, boolean modoEdicion) {
		this.tabla = tabla;
		construct();
		setModoEdicion(modoEdicion);
	}

	//Construye gráficamente el componente
	private void construct() {
		setLayout(new BorderLayout(0, 10));
		//Botón 'Agregar'
		String iconoAgregar = (iconoBtnAgregar == null ? DEFAULT_ICON_BTN_AGREGAR : iconoBtnAgregar);
		String iconoAgregarDeshab = (iconoBtnAgregarDeshab == null ? DEFAULT_ICON_BTN_AGREGAR_DESHAB : iconoBtnAgregarDeshab);
		btnAgregar = BossEstilos.createButton(iconoAgregar, iconoAgregarDeshab);
		btnAgregar.setEnabled(false);
		btnAgregar.setToolTipText("Agregar fila");
		add(btnAgregar, BorderLayout.NORTH);
		btnAgregar.addActionListener(new BotonAgregarListener());
		//Botón 'Eliminar'
		String iconoEliminar = (iconoBtnEliminar == null ? DEFAULT_ICON_BTN_ELIMINAR : iconoBtnEliminar);
		String iconoEliminarDeshab = (iconoBtnEliminarDeshab == null ? DEFAULT_ICON_BTN_ELIMINAR_DESHAB : iconoBtnEliminarDeshab);
		btnEliminar = BossEstilos.createButton(iconoEliminar, iconoEliminarDeshab);
		btnEliminar.setEnabled(false);
		btnEliminar.setToolTipText("Eliminar fila");
		add(btnEliminar, BorderLayout.SOUTH);
		btnEliminar.addActionListener(new BotonEliminarListener());
		//Tabla
		tabla.getSelectionModel().addListSelectionListener(new TablaListener());
	}

	public boolean isModoEdicion() {
		return modoEdicion;
	}

	public void setModoEdicion(boolean modoEdicion) {
		this.modoEdicion = modoEdicion;
		//Lockeo de tabla y sincronización de los botones
		tabla.setTableLock(!modoEdicion);
		btnAgregar.setEnabled(modoEdicion);
		btnEliminar.setEnabled(modoEdicion ? (tabla.getSelectedRow() != -1) : false);
	}

	public FWJTable getTabla() {
		return tabla;
	}

	public JButton getBotonAgregar() {
		return btnAgregar;
	}

	public JButton getBotonEliminar() {
		return btnEliminar;
	}

	public void botonAgregarPresionado() {
	}

	public void botonQuitarPresionado() {
	}

	public boolean validarAgregar() {
	    return true;
	}

	public boolean validarQuitar() {
	    return true;
	}

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        btnAgregar.setEnabled(enabled);
        btnEliminar.setEnabled(enabled);
    }

    class BotonAgregarListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			if(validarAgregar()) {
				tabla.addRow();
				tabla.scroll(tabla.getRowCount() - 1);
				botonAgregarPresionado();
			}
		}
    }

    class BotonEliminarListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			if(tabla.getSelectedRow() != -1) {
				if(validarQuitar()) {
					tabla.removeRows(tabla.getSelectedRows());
					if(tabla.getRowCount() == 0) {
						btnEliminar.setEnabled(false);
					}
					botonQuitarPresionado();
				}
			}
		}
    }

    class TablaListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent evt) {
			if(modoEdicion) {
				btnEliminar.setEnabled((tabla.getSelectedRow() != -1));
			}
		}
	}

}