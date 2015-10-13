package ar.com.textillevel.gui.modulos.personal.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTablaSinBotones;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.to.DatosVencimientoContratoEmpleadoTO;

public class JDialogContratosPorVencer extends JDialog {

	private static final long serialVersionUID = -1272946053482960783L;

	private JButton btnAceptar;
	private PanelTablaContratos panelTabla;
	private List<DatosVencimientoContratoEmpleadoTO> datosVencimientos;

	public JDialogContratosPorVencer(Frame padre, List<DatosVencimientoContratoEmpleadoTO> datos) {
		super(padre);
		setDatosVencimientos(datos);
		setUpComponentes();
		setUpScreen();
		loadData();
	}

	private void loadData() {
		getPanelTabla().limpiar();
		for (DatosVencimientoContratoEmpleadoTO d : getDatosVencimientos()) {
			getPanelTabla().agregarElemento(d);
		}
	}

	private void setUpScreen() {
		setTitle("Contratos por vencer");
		setModal(true);
		pack();
		GuiUtil.centrar(this);
		setResizable(false);
	}

	private void setUpComponentes() {
		add(getPanelTabla(), BorderLayout.CENTER);
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		add(panelSur, BorderLayout.SOUTH);
	}

	private class PanelTablaContratos extends PanelTablaSinBotones<DatosVencimientoContratoEmpleadoTO> {

		private static final long serialVersionUID = 1547424835790020813L;

		private static final int CANT_COLS = 3;
		private static final int COL_NOMBRE_APELLIDO = 0;
		private static final int COL_FECHA_VENCIMIENTO = 1;
		private static final int COL_OBJ = 2;

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_NOMBRE_APELLIDO, "Nombre y apellido", 250, 250, true);
			tabla.setDateColumn(COL_FECHA_VENCIMIENTO, "Vencimiento", 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(DatosVencimientoContratoEmpleadoTO elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_NOMBRE_APELLIDO] = elemento.getNombre() + " " + elemento.getApellido();
			row[COL_FECHA_VENCIMIENTO] = elemento.getFechaVencimiento();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected DatosVencimientoContratoEmpleadoTO getElemento(int fila) {
			return (DatosVencimientoContratoEmpleadoTO) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public PanelTablaContratos getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaContratos();
		}
		return panelTabla;
	}

	public List<DatosVencimientoContratoEmpleadoTO> getDatosVencimientos() {
		return datosVencimientos;
	}

	public void setDatosVencimientos(List<DatosVencimientoContratoEmpleadoTO> datosVencimientos) {
		this.datosVencimientos = datosVencimientos;
	}
}
