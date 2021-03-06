package ar.com.textillevel.gui.acciones.odt;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import ar.com.fwcommon.componentes.FWDateField;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.gui.acciones.RemitoEntradaLinkeableLabel;
import ar.com.textillevel.gui.acciones.remitosalida.RemitoSalidaLinkeableLabel;
import ar.com.textillevel.gui.modulos.odt.gui.JDialogSeleccionarCrearSecuenciaDeTrabajo;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogVisualizarDetalleODT extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel panDetalle;
	private PanelTablaPieza panTablaPieza;
	private JPanel pnlBotones;
	private JButton btnCerrar;
	
	private FWJTextField txtTotalPiezasEntrada;
	private FWJTextField txtTotalMetrosEntrada;
	
	private FWDateField txtFechaEmision;
	private FWJTextField txtProducto;
	private OrdenDeTrabajo odt;
	private RemitoEntrada remitoEntrada;
	private JPanel panTotales; 
	private JTextField txtMetros;
	private JTextField txtPiezas;

	private JPanel panelDatosCliente; 

	private JTextField txtNroCliente;
	private JPanel panelDatosFactura;
	private boolean acepto;

	private RemitoEntradaLinkeableLabel remitoLinkeableLabel;
	private List<RemitoSalida> remitosSalida;
	private RemitoSalidaFacadeRemote remitoSalidaFacade;
	private JPanel panRS;
	
	public JDialogVisualizarDetalleODT(Frame owner, OrdenDeTrabajo odt) {
		super(owner);
		this.odt = odt;
		this.remitoEntrada = odt.getRemito();
		this.remitosSalida = getRemitoSalidaFacade().getRemitosSalidaByODT(odt.getId());
		setSize(new Dimension(630, 750));
		setTitle("Orden De Trabajo");
		construct();
		setDatos();
		setModal(true);
	}

	private RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if(remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
	}

	private void setDatos() {
		Cliente cliente = remitoEntrada.getCliente();
		getTxtFechaEmision().setFecha(remitoEntrada.getFechaEmision());
		getTxtProducto().setText(odt.toString());
		if(cliente.getDireccionReal() != null) {
			getTxtNroCliente().setText(cliente.getNroCliente()+"");
		}
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 1));
		add(getPanTotales(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
	}

	private JPanel getPanTotales() {
		if(panTotales == null) {
			panTotales = new JPanel();
			panTotales.setLayout(new GridBagLayout());
			panTotales.add(new JLabel("Total Piezas Salida:"), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panTotales.add(getTxtPiezas(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panTotales.add(new JLabel("Total Metros Salida:"), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panTotales.add(getTxtMetros(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
		}
		return panTotales;
	}

	private JTextField getTxtPiezas() {
		if(txtPiezas == null) {
			txtPiezas = new JTextField();
			txtPiezas.setEditable(false);
			txtPiezas.setText(odt.getPiezas().size()+"");
		}
		return txtPiezas;
	}

	private JTextField getTxtMetros() {
		if(txtMetros == null) {
			txtMetros = new JTextField();
			txtMetros.setEditable(false);
			txtMetros.setText(odt.getTotalMetros().toString());
		}
		return txtMetros;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			
			panDetalle.add(getPanelDatosCliente(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));

			panDetalle.add(getPanelDatosFactura(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 6, 1, 0, 0));

			panDetalle.add(new JLabel("ODT:"), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtProducto(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panDetalle.add(getPanTablaPieza(), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 6, 1, 1, 1));
		}
	
		return panDetalle;
	}

	private JPanel getPanelDatosCliente() {
		if(panelDatosCliente == null){
			panelDatosCliente = new JPanel();
			panelDatosCliente.setLayout(new GridBagLayout());
			panelDatosCliente.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosCliente.add(new JLabel("Cliente: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosCliente.add(getTxtNroCliente(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panelDatosCliente;
	}

	private JTextField getTxtNroCliente() {
		if(txtNroCliente == null) {
			txtNroCliente = new JTextField();
			txtNroCliente.setEditable(false);
		}
		return txtNroCliente;
	}

	private JPanel getPanelDatosFactura() {
		if(panelDatosFactura == null){
			panelDatosFactura = new JPanel();
			panelDatosFactura.setLayout(new GridBagLayout());
			panelDatosFactura.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			panelDatosFactura.add(new JLabel("Remito de Entrada N�: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getRemitoLinkeableLabel(), GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panelDatosFactura.add(new JLabel("Fecha de Ingreso:"), GenericUtils.createGridBagConstraints(2, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtFechaEmision(), GenericUtils.createGridBagConstraints(3, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			
			panelDatosFactura.add(new JLabel("Total Piezas Entrada: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtTotalPiezasEntrada(), GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panelDatosFactura.add(new JLabel("Total Metros Entrada: "), GenericUtils.createGridBagConstraints(2, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelDatosFactura.add(getTxtTotalMetrosEntrada(), GenericUtils.createGridBagConstraints(3, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));

			setInfoAdicional(panelDatosFactura, 1);
		}
		return panelDatosFactura;
	}

	private void setInfoAdicional(JPanel panelDatosFactura2, int currentY) {
		int y=currentY;
		if(!remitosSalida.isEmpty()) {
			y++;
			panelDatosFactura.add(getPanRS() , GenericUtils.createGridBagConstraints(0, y,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		if(odt.getSecuenciaDeTrabajo() != null) {
			panelDatosFactura.add(new SecuenciaODTLinkeableLabel(odt) , GenericUtils.createGridBagConstraints(y==currentY ? 0 : 1, y==currentY ? ++y : y ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
	}

	private class SecuenciaODTLinkeableLabel extends LinkableLabel {

		private static final long serialVersionUID = 1L;

		private OrdenDeTrabajo odt;

		public SecuenciaODTLinkeableLabel(OrdenDeTrabajo odt) {
			super("Ver Secuencia de Trabajo");
			this.odt = odt; 
		}

		@Override
		public void labelClickeada(MouseEvent e) {
			JDialogSeleccionarCrearSecuenciaDeTrabajo dS = new JDialogSeleccionarCrearSecuenciaDeTrabajo(GuiUtil.getFrameForComponent(JDialogVisualizarDetalleODT.this) ,odt);
			dS.setVisible(true);
		}

	}
	
	private FWJTextField getTxtTotalPiezasEntrada() {
		if(txtTotalPiezasEntrada == null) {
			txtTotalPiezasEntrada = new FWJTextField();
			txtTotalPiezasEntrada.setEditable(false);
			if(remitoEntrada.getId() != null) {
				txtTotalPiezasEntrada.setText(String.valueOf(remitoEntrada.getPiezas().size()));
			}
		}
		return txtTotalPiezasEntrada;
	}
	
	private FWJTextField getTxtTotalMetrosEntrada() {
		if(txtTotalMetrosEntrada == null) {
			txtTotalMetrosEntrada = new FWJTextField();
			txtTotalMetrosEntrada.setEditable(false);
			if(remitoEntrada.getId() != null) {
				txtTotalMetrosEntrada.setText(String.valueOf(odt.getTotalMetrosEntrada()));
			}
		}
		return txtTotalMetrosEntrada;
	}

	private FWJTextField getTxtProducto() {
		if(txtProducto == null) {
			txtProducto = new FWJTextField();
			txtProducto.setEditable(false);
		}
		return txtProducto;
	}

	private FWDateField getTxtFechaEmision() {
		if(txtFechaEmision == null) {
			txtFechaEmision = new FWDateField();
			txtFechaEmision.setFecha(new Date(odt.getFechaODT().getTime()));
			txtFechaEmision.setEditable(false);
		}
		return txtFechaEmision;
	}

	private RemitoEntradaLinkeableLabel getRemitoLinkeableLabel() {
		if(remitoLinkeableLabel == null) {
			this.remitoLinkeableLabel = new RemitoEntradaLinkeableLabel();
			this.remitoLinkeableLabel.setRemito(odt.getRemito());
		}
		return remitoLinkeableLabel;
	}	
	
	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnCerrar());
			
			getBtnCerrar().setEnabled(true);
		}
		return pnlBotones;
	}

	private JButton getBtnCerrar() {
		if(btnCerrar == null) {
			btnCerrar = new JButton("Cerrar");
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}

			});

		}
		return btnCerrar;
	}

	private PanelTablaPieza getPanTablaPieza() {
		if(panTablaPieza == null) {
			panTablaPieza = new PanelTablaPieza(odt);
		}
		return panTablaPieza;
	}

	public boolean isAceptpo() {
		return acepto;
	}

	private JPanel getPanRS() {
		if(panRS == null) {
			panRS = new JPanel(new FlowLayout());
			panRS.add(new JLabel("Remito(s) de Salida: "));
			for(RemitoSalida rs : remitosSalida) {
				RemitoSalidaLinkeableLabel rsLL = new RemitoSalidaLinkeableLabel();
				rsLL.setRemito(rs);
				panRS.add(rsLL);
			}
		}
		return panRS;
	}

	private class PanelTablaPieza extends PanelTabla<PiezaODT> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 6;
		private static final int COL_NRO_ORDEN_PIEZA_ODT = 0;
		private static final int COL_METROS_PIEZA_ENT = 1;
		private static final int COL_METROS_PIEZA_ODT = 2;
		private static final int COL_ES_DE_2DA = 3;
		private static final int COL_SALIDA = 4;
		private static final int COL_OBJ = 5;

		public PanelTablaPieza(OrdenDeTrabajo odt) {
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
			Collections.sort(odt.getPiezas());
			agregarElementos(odt.getPiezas());
		}

		@Override
		protected void agregarElemento(PiezaODT elemento) {
			Object[] row = getRow(elemento);
			getTabla().addRow(row);
		}

		private Object[] getRow(PiezaODT elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_NRO_ORDEN_PIEZA_ODT] = elemento.toString();
			row[COL_METROS_PIEZA_ENT] = elemento.getOrdenSubpieza() == null ||  elemento.getOrdenSubpieza().intValue() == 0 ?  elemento.getPiezaRemito().getMetros().toString() : "";
			row[COL_METROS_PIEZA_ODT] = elemento.getMetros() == null ? null : elemento.getMetros().toString();
			row[COL_ES_DE_2DA] = elemento.getEsDeSegunda() != null && elemento.getEsDeSegunda();
			row[COL_SALIDA] = !elemento.getPiezasSalida().isEmpty();
			row[COL_OBJ] = elemento;
			return row;
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaPiezaEntrada = new FWJTable(0, CANT_COLS);
			tablaPiezaEntrada.setStringColumn(COL_NRO_ORDEN_PIEZA_ODT, "NRO. DE PIEZA ODT", 120, 120, true);
			tablaPiezaEntrada.setAlignment(COL_NRO_ORDEN_PIEZA_ODT, FWJTable.CENTER_ALIGN);
			tablaPiezaEntrada.setFloatColumn(COL_METROS_PIEZA_ENT, "METROS ENTRADA", 100, true);
			tablaPiezaEntrada.setAlignment(COL_METROS_PIEZA_ENT, FWJTable.CENTER_ALIGN);
			tablaPiezaEntrada.setFloatColumn(COL_METROS_PIEZA_ODT, "METROS SALIDA", 100, true);
			tablaPiezaEntrada.setAlignment(COL_METROS_PIEZA_ODT, FWJTable.CENTER_ALIGN);
			tablaPiezaEntrada.setCheckColumn(COL_ES_DE_2DA, "2DA", 40, true);
			tablaPiezaEntrada.setHeaderAlignment(COL_ES_DE_2DA, FWJTable.CENTER_ALIGN);
			tablaPiezaEntrada.setStringColumn(COL_OBJ, "", 0, 0, true);
			tablaPiezaEntrada.setSelectionMode(FWJTable.SINGLE_SELECTION);
			tablaPiezaEntrada.setCheckColumn(COL_SALIDA, "EN SALIDA", 90, true);
			tablaPiezaEntrada.setHeaderAlignment(COL_SALIDA, FWJTable.CENTER_ALIGN);
			return tablaPiezaEntrada;
		}

		@Override
		protected void botonQuitarPresionado() {
		}

		@Override
		public boolean validarAgregar() {
			return false;
		}

		@Override
		public boolean validarQuitar() {
			return false;
		}

		@Override
		protected PiezaODT getElemento(int fila) {
			return (PiezaODT)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

	}

}