package ar.com.textillevel.gui.acciones.remitosalidaventatela;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.to.remitosalida.PiezaRemitoSalidaTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarStockTela extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel panDetalle;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JComboBox cmbArticulo;
	private JPanel pnlBotones;
	private FWJTable tablaStockPMP;
	private PrecioMateriaPrimaFacadeRemote remitoEntradaFacade;
	private ArticuloFacadeRemote articuloFacade;
	private PiezaRemitoSalidaTO piezaRemitoSalidaTO;
	private Collection<PiezaRemitoSalidaTO> piezasYaElegidas;

	public JDialogSeleccionarStockTela(Frame owner, Collection<PiezaRemitoSalidaTO> piezasYaElegidas) {
		super(owner);
		this.remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
		this.articuloFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
		this.piezasYaElegidas = piezasYaElegidas == null ? new ArrayList<PiezaRemitoSalidaTO>() : piezasYaElegidas;
		setModal(true);
		setSize(new Dimension(600, 550));
		setTitle("Ingresar el stock a utilizar");
		construct();
	}

	private void llenarTablaPMP() {
		Articulo art = ((Articulo)getCmbArticulo().getSelectedItem());
		if(art == null) {
			getTablaStockPMP().setNumRows(0);
		} else {
			List<PrecioMateriaPrima> pmpList = remitoEntradaFacade.getAllWithStockInicialDispByArticulo(art.getId());
			getTablaStockPMP().setNumRows(0);
			int row = 0;
			for(PrecioMateriaPrima pmp : pmpList) {
				BigDecimal stockConsumido = getStockConsumido(pmp);
				getTablaStockPMP().addRow();
				getTablaStockPMP().setValueAt(pmp.toString(), row, 0);
				getTablaStockPMP().setValueAt(pmp.getStockInicialDisponible().subtract(stockConsumido), row, 1);
				getTablaStockPMP().setValueAt(pmp, row, 3);
				row ++;
			}
		}
	}

	private BigDecimal getStockConsumido(PrecioMateriaPrima pmp) {
		BigDecimal stockConsumido = new BigDecimal(0);
		for(PiezaRemitoSalidaTO prto : piezasYaElegidas) {
			PrecioMateriaPrima pmpYaConsumido = prto.getPmpStockConsumido().keySet().iterator().next();
			if(pmpYaConsumido.equals(pmp)) {
				stockConsumido = stockConsumido.add(prto.getTotalMetrosStockConsumido());
			}
		}
		return stockConsumido;
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDetalle(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		add(getPanelBotones(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			
			JLabel lblTela = new JLabel("TELA: ");
			panDetalle.add(lblTela, createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbArticulo(), createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			
			JScrollPane scrollPane = new JScrollPane(getTablaStockPMP());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Remitos"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 1, 1));
		}
		return panDetalle;
	}

	private JComboBox getCmbArticulo() {
		if(cmbArticulo == null) {
			cmbArticulo = new JComboBox();
			cmbArticulo.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						llenarTablaPMP();
					}
				}
			});
			GuiUtil.llenarCombo(cmbArticulo, articuloFacade.getArticulosConAlgunaPMPConStockInicial(), true);
		}
		return cmbArticulo;
	}

	private FWJTable getTablaStockPMP() {
		if(tablaStockPMP == null) {
			tablaStockPMP = new FWJTable(0, 4);
			tablaStockPMP.setStringColumn(0, "TELA", 250, 250, true);
			tablaStockPMP.setStringColumn(1, "STOCK DISPONIBLE", 120, 120, true);
			tablaStockPMP.setFloatColumn(2, "STOCK ELEGIDO", 0f, Float.MAX_VALUE,  120, false);
			tablaStockPMP.setStringColumn(3, "", 0, 0, true);
			tablaStockPMP.setAlignment(0, FWJTable.CENTER_ALIGN);
		}
		return tablaStockPMP;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						capturarSetearDatos();
						dispose();
					}
				}


			});
		}
		return btnAceptar;
	}

	private void capturarSetearDatos() {
		piezaRemitoSalidaTO = new PiezaRemitoSalidaTO(); 
		for(int i = 0; i < getTablaStockPMP().getRowCount(); i++) {
			Float stockConsumir = (Float)getTablaStockPMP().getTypedValueAt(i, 2);
			PrecioMateriaPrima pmp = (PrecioMateriaPrima)getTablaStockPMP().getValueAt(i, 3);
			if(stockConsumir != null && stockConsumir > 0f) {
				piezaRemitoSalidaTO.addPmpStockConsumido(pmp, new BigDecimal(stockConsumir));
			}
		}
	}

	private boolean validar() {
		for(int i = 0; i < getTablaStockPMP().getRowCount(); i++) {
			Float stockConsumir = (Float)getTablaStockPMP().getTypedValueAt(i, 2);
			PrecioMateriaPrima pmp = (PrecioMateriaPrima)getTablaStockPMP().getValueAt(i, 3);
			BigDecimal stockConsumido = getStockConsumido(pmp);
			if(stockConsumir != null && (stockConsumir > 0 && pmp.getStockInicialDisponible().subtract(stockConsumido).floatValue() < stockConsumir)) {
				FWJOptionPane.showErrorMessage(JDialogSeleccionarStockTela.this, StringW.wordWrap("La cantidad a elegir debe ser mayor a cero y menor o igual al stock disponible"), "Error");
				return false;
			}
		}
		return true;
	}

	public PiezaRemitoSalidaTO getPiezaRemitoSalidaTO() {
		return piezaRemitoSalidaTO;
	}

}