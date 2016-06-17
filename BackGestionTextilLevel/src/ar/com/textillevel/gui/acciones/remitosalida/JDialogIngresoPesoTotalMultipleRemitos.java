package ar.com.textillevel.gui.acciones.remitosalida;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.gui.util.GenericUtils;

public class JDialogIngresoPesoTotalMultipleRemitos extends JDialog {

	private static final long serialVersionUID = 1L;

	private FWJTextField txtPesoTotal;
	private JButton btnCalcular;
	private JButton btnAceptar;
	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private List<RemitoSalida> remitosSalida;
	private PanelTablaRemitoSalidaConPeso panTabla;
	private boolean acepto;

	public JDialogIngresoPesoTotalMultipleRemitos(JDialog owner, List<RemitoSalida> remitosSalida) {
		super(owner);
		this.remitosSalida = remitosSalida;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle("Peso por Remito de Salida");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(300, 300));
		setResizable(false);
		setModal(true);
	}

	private void setUpComponentes(){
		setLayout(new GridBagLayout());
		add(getPanelDatos(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 1, 1, 1, 0));
		add(getPanTabla(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 1, 1, 1, 1));
		add(getPanelBotones(),GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 1, 1, 0, 0));
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
		}
		return pnlBotones;
	}

	private JPanel getPanelDatos() {
		if(pnlDatos == null){
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(5,5,5,5);
			JLabel lbl = new JLabel("Ingreso el peso total: ");
			pnlDatos.add(lbl, gc);
			gc = new GridBagConstraints();
			gc.insets = new Insets(5,5,5,5);
			gc.gridx = 1;
			gc.gridy = 0;
			gc.weightx = 1;
			gc.fill = GridBagConstraints.HORIZONTAL;
			pnlDatos.add(getTxtPesoTotal(), gc);
			gc = new GridBagConstraints();
			gc.insets = new Insets(5,5,5,5);
			gc.gridx = 2;
			gc.gridy = 0;
			pnlDatos.add(getBtnCalcular(), gc);
			
		}
		return pnlDatos;
	}

	private FWJTextField getTxtPesoTotal() {
		if(txtPesoTotal == null) {
			txtPesoTotal = new FWJTextField();
		}
		return txtPesoTotal;
	}

	private PanelTablaRemitoSalidaConPeso getPanTabla() {
		if(panTabla == null) {
			panTabla = new PanelTablaRemitoSalidaConPeso();
		}
		return panTabla;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String mensaje = getPanTabla().validar();
					if(getPanTabla().validar() != null) {
						FWJOptionPane.showErrorMessage(JDialogIngresoPesoTotalMultipleRemitos.this, StringW.wordWrap(mensaje), "Error");
						return;
					}
					capturarSetearDatos();
					acepto = true; 
					dispose();
				}

			});
		}
		return btnAceptar;
	}

	private void capturarSetearDatos() {
		getPanTabla().capturarSetearDatos();
	}

	public boolean isAcepto() {
		return acepto;
	}

	private JButton getBtnCalcular() {
		if(btnCalcular == null) {
			btnCalcular = new JButton("Calcular");
			btnCalcular.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String pesoTotalStr = getTxtPesoTotal().getText();
					if(StringUtil.isNullOrEmpty(pesoTotalStr) || !GenericUtils.esNumerico(pesoTotalStr)) {
						FWJOptionPane.showErrorMessage(JDialogIngresoPesoTotalMultipleRemitos.this, "Debe ingresar un peso total válido.", "Error");
						getTxtPesoTotal().requestFocus();
						return;
					}
					
					getPanTabla().limpiar();
					double pesoTotal = Double.valueOf(getTxtPesoTotal().getText().trim().replace(',', '.'));
					double totalMetros = 0;
					for(RemitoSalida rs : remitosSalida) {
						totalMetros = totalMetros + rs.getTotalMetros().doubleValue();
					}
					double gramaje = pesoTotal/totalMetros;
					List<RemitoSalidaConPeso> remitosConPeso = new ArrayList<JDialogIngresoPesoTotalMultipleRemitos.RemitoSalidaConPeso>();
					for(RemitoSalida rs : remitosSalida) {
						RemitoSalidaConPeso r = new RemitoSalidaConPeso(rs, new BigDecimal(rs.getTotalMetros().doubleValue() * gramaje));
						remitosConPeso.add(r);
					}
					getPanTabla().agregarElementos(remitosConPeso);
				}
			});
		}
		return btnCalcular;
	}

	private class RemitoSalidaConPeso {

		private RemitoSalida remitoSalida;
		private BigDecimal pesoTotal;

		private RemitoSalidaConPeso(RemitoSalida remitoSalida, BigDecimal pesoTotal) {
			this.remitoSalida = remitoSalida;
			this.pesoTotal = pesoTotal;
		}

		public RemitoSalida getRemitoSalida() {
			return remitoSalida;
		}

		public BigDecimal getPesoTotal() {
			return pesoTotal;
		}

	}

	
	private class PanelTablaRemitoSalidaConPeso extends PanelTabla<RemitoSalidaConPeso> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 3;
		private static final int COL_NRO_REMITO = 0;
		private static final int COL_PESO_TOTAL = 1;
		private static final int COL_OBJ = 2;

		public PanelTablaRemitoSalidaConPeso() {
			getBotonEliminar().setVisible(false);
			getBotonAgregar().setVisible(false);
		}

		public String validar() {
			if(getElementos().isEmpty()) {
				return "Debe calcular los pesos por remitos.";
			}
			if(getTabla().getFirstRowWithValue(COL_PESO_TOTAL, "0") != -1) {
				return "Debe completar el peso de todos los remitos.";
			}
			return null;
		}

		public void capturarSetearDatos() {
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				RemitoSalidaConPeso elemento = getElemento(i);
				Float peso = (Float)getTabla().getTypedValueAt(i, COL_PESO_TOTAL);
				elemento.getRemitoSalida().setPesoTotal(new BigDecimal(peso));
			}
		}

		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_NRO_REMITO, "NRO. DE REMITO", 100);
			tabla.setFloatColumn(COL_PESO_TOTAL, "PESO", 0f, Float.MAX_VALUE, 90, false);
			tabla.setStringColumn(COL_OBJ, "", 0);
			return tabla;
		}

		protected void agregarElemento(RemitoSalidaConPeso elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_NRO_REMITO] = elemento.getRemitoSalida().getNroRemito();
			row[COL_PESO_TOTAL] = elemento.getPesoTotal().floatValue();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		protected RemitoSalidaConPeso getElemento(int fila) {
			return (RemitoSalidaConPeso)getTabla().getValueAt(fila, COL_OBJ);
		}

		protected String validarElemento(int fila) {
			return null;
		}

	}

	public static void main(String[] args) {
		List<RemitoSalida> remitos = new ArrayList<RemitoSalida>();
		RemitoSalida rs = new RemitoSalida();
		PiezaRemito pr = new PiezaRemito();
		pr.setMetros(new BigDecimal(75));
		rs.setNroRemito(1);
		rs.getPiezas().add(pr);
		rs.setNroRemito(100);
		rs.setPorcentajeMerma(new BigDecimal(20));
		remitos.add(rs);
		rs = new RemitoSalida();
		rs.setNroRemito(2);
		pr = new PiezaRemito();
		pr.setMetros(new BigDecimal(175));
		rs.getPiezas().add(pr);
		rs.setNroRemito(102);
		rs.setPorcentajeMerma(new BigDecimal(-40));
		remitos.add(rs);
		rs = new RemitoSalida();
		rs.setNroRemito(3);
		pr = new PiezaRemito();
		pr.setMetros(new BigDecimal(25));
		rs.getPiezas().add(pr);
		rs.setNroRemito(103);
		rs.setPorcentajeMerma(new BigDecimal(40));
		remitos.add(rs);
		rs = new RemitoSalida();
		rs.setNroRemito(4);
		pr = new PiezaRemito();
		pr.setMetros(new BigDecimal(60));
		rs.getPiezas().add(pr);
		rs.setNroRemito(104);
		rs.setPorcentajeMerma(new BigDecimal(60));
		remitos.add(rs);
		JDialogIngresoPesoTotalMultipleRemitos dialogResultadoAltaRemitoSalida = new JDialogIngresoPesoTotalMultipleRemitos(new JDialog(), remitos);
		dialogResultadoAltaRemitoSalida.setVisible(true);
		if(dialogResultadoAltaRemitoSalida.isAcepto()) {
			for(RemitoSalida r : remitos) {
				System.out.println(r.getNroRemito() + "   " + r.getPesoTotal());
			}
		}
	}

}