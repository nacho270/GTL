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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.com.fwcommon.componentes.FWCheckBoxListDialog;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementoEvent;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementoEventListener;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSelRemitoEntradaConPiezasParaVender extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final String TELA_TODAS = "TODAS";
	private JPanel panDetalle;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	private JPanel panFiltros;
	private FWJTable tablaRemitosEntrada;
	private Frame owner;
	private RemitoEntradaFacadeRemote remitoEntradaFacade;
	private ArticuloFacadeRemote articuloFacade;
	private OrdenDeTrabajoFacadeRemote odtFacade;
	private Map<RemitoEntrada, List<PiezaRemito>> resultMap;
	private Map<RemitoEntrada, List<OrdenDeTrabajo>> odtMap;
	private Set<RemitoEntrada> remitoEntradaSet;
	private JComboBox cmbTipoTela;
	private PanelSeleccionarElementos<Articulo> panSelArticulos;
	private PanelSeleccionarElementos<ETipoProducto> panSelTipoProducto;
	private List<PiezaRemito> piezasSeleccionadas;
	private List<PiezaRemito> piezasEntradaPersisted;

	public JDialogSelRemitoEntradaConPiezasParaVender(Frame owner, List<PiezaRemito> piezasSeleccionadas, List<PiezaRemito> piezasEntradaPersisted) {
		super(owner);
		this.resultMap = new HashMap<RemitoEntrada, List<PiezaRemito>>();
		this.owner = owner;
		this.remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		this.odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		this.articuloFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
		this.piezasSeleccionadas = piezasSeleccionadas;
		this.piezasEntradaPersisted = piezasEntradaPersisted;
		setModal(true);
		setSize(new Dimension(950, 550));
		setTitle("Seleccionar Piezas de Remitos Sin Salida");
		construct();
		inicializarListaRemitosAndODTMap();
		refreshTablaPiezas(new ArrayList<Articulo>(), new ArrayList<ETipoProducto>());
		colocarPiezasElegidas();
	}

	private void colocarPiezasElegidas() {
		for(int r = 0; r < getTablaRemitoEntrada().getRowCount(); r++) {
			setPiezaSeleccionada((RemitoEntrada)getTablaRemitoEntrada().getValueAt(r, 3), piezasSeleccionadas, r);
		}
	}

	private void inicializarListaRemitosAndODTMap() {
		this.odtMap = new HashMap<RemitoEntrada, List<OrdenDeTrabajo>>();
		remitoEntradaSet = new HashSet<RemitoEntrada>(remitoEntradaFacade.getRemitoEntradaConPiezasParaVender()); //piezas desde compra de tela 
		remitoEntradaSet.addAll(remitoEntradaFacade.getRemitoEntradaConPiezasNoAsociadasList()); //piezas desde entrada 01
		//Agrego los remitos de entrada de las piezas ya persistidas
		for(PiezaRemito pr : piezasEntradaPersisted) {
			RemitoEntrada re = remitoEntradaFacade.getByIdPiezaRemitoEntradaEager(pr.getId());
			remitoEntradaSet.add(re);
		}
		for(RemitoEntrada re : remitoEntradaSet) {
			odtMap.put(re, odtFacade.getOdtEagerByRemitoList(re.getId()));
		}
	}

	private void refreshTablaPiezas(List<Articulo> articuloList, List<ETipoProducto> tipoProductoList) {
		resultMap.clear();
		getTablaRemitoEntrada().setNumRows(0);
		int row = 0;
		for(RemitoEntrada re : remitoEntradaSet) {
			List<PiezaRemito> piezasFiltradas = filtrarPiezasByParams(re, re.getPiezas(), articuloList, tipoProductoList);
			if(!piezasFiltradas.isEmpty()) {
				getTablaRemitoEntrada().addRow();
				getTablaRemitoEntrada().setValueAt("Nro.: " + re.getNroRemito() + " - "  + DateUtil.dateToString(re.getFechaEmision()), row, 0);
				getTablaRemitoEntrada().setValueAt(getInfoPiezas(re, piezasFiltradas), row, 1);
				getTablaRemitoEntrada().setValueAt(re, row, 3);
				row ++;
			}
		}
	}

	private List<PiezaRemito> filtrarPiezasByParams(RemitoEntrada re, List<PiezaRemito> piezas, List<Articulo> articuloList, List<ETipoProducto> tipoProductoList) {
		List<PiezaRemito> piezasResult = new ArrayList<PiezaRemito>();
		String tipoTela = (String)getCmbTipoTela().getSelectedItem();
		for(PiezaRemito pr : piezas) {
			if(pr.getEnSalida() == null || (!pr.getEnSalida() || piezasEntradaPersisted.contains(pr))) {
				boolean cumpleCrudoOrTerminado = false;
				boolean cumpleTipoDeTela = false;
				boolean cumpleTipoDeProducto = false;
				OrdenDeTrabajo odt = getODT(pr, odtMap.get(re));
				//Filtro por tipo de tela
				boolean piezaCruda = odt == null;
				cumpleCrudoOrTerminado = tipoTela.equals(TELA_TODAS) ||
							   (piezaCruda && tipoTela.equals(ETipoTela.CRUDA.toString())) ||
							   (!piezaCruda && tipoTela.equals(ETipoTela.TERMINADA.toString()));
				//Filtro por tela
				cumpleTipoDeTela = articuloList.isEmpty() ||
								  (odt != null && articuloList.contains(odt.getProducto().getArticulo()));
				//Filtro por producto
				cumpleTipoDeProducto = tipoProductoList.isEmpty() ||
				  				       (odt != null && tipoProductoList.contains(odt.getProducto().getTipo()));
				//Si cumple todo la agrega
				if(cumpleCrudoOrTerminado && cumpleTipoDeTela && cumpleTipoDeProducto) {
					piezasResult.add(pr);
				}
			}
		}
		return piezasResult;
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
			panDetalle.add(getPanFiltros(), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 0, 0));
			panDetalle.add(getPanSelArticulos(), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 0, 0));
			panDetalle.add(getPanSelTipoDeProducto(), createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 0, 0));
			JScrollPane scrollPane = new JScrollPane(getTablaRemitoEntrada());
			scrollPane.setBorder(BorderFactory.createTitledBorder("Remitos"));
			panDetalle.add(scrollPane, createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 1, 1));
		}
		return panDetalle;
	}

	private JPanel getPanFiltros() {
		if(panFiltros == null) {
			panFiltros = new JPanel();
			panFiltros.setLayout(new FlowLayout());
			panFiltros.add(new JLabel("Tipo de Piezas: "));
			panFiltros.add(getCmbTipoTela());
		}
		return panFiltros;
	}
	
	private JComboBox getCmbTipoTela() {
		if(cmbTipoTela == null) {
			cmbTipoTela = new JComboBox();
			List<String> telaList = new ArrayList<String>();
			telaList.add(ETipoTela.CRUDA.toString());
			telaList.add(ETipoTela.TERMINADA.toString());
			telaList.add(TELA_TODAS);
			GuiUtil.llenarCombo(cmbTipoTela, telaList, true);
			cmbTipoTela.setSelectedItem(TELA_TODAS);
			cmbTipoTela.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						refreshTablaPiezas(new ArrayList<Articulo>(), new ArrayList<ETipoProducto>());
					}
				}

			});
		}
		return cmbTipoTela;
	}

	private PanelSeleccionarElementos<Articulo> getPanSelArticulos() {
		if(panSelArticulos == null) {
			panSelArticulos = new PanelSeleccionarElementos<Articulo>(owner, articuloFacade.getAllOrderByName(), "Telas");
			panSelArticulos.setSeparator(" - ");

			panSelArticulos.addPanelSeleccionarElementosListener(new PanelSeleccionarElementoEventListener<Articulo>() {

				public void elementsSelected(PanelSeleccionarElementoEvent<Articulo> evt) {
					refreshTablaPiezas(evt.getElements(), new ArrayList<ETipoProducto>());
				}

			});
		}
		return panSelArticulos;
	}

	private PanelSeleccionarElementos<ETipoProducto> getPanSelTipoDeProducto() {
		if(panSelTipoProducto == null) {
			panSelTipoProducto = new PanelSeleccionarElementos<ETipoProducto>(owner, Arrays.asList(ETipoProducto.values()), "Tipo de Productos");
			panSelTipoProducto.addPanelSeleccionarElementosListener(new PanelSeleccionarElementoEventListener<ETipoProducto>() {

				public void elementsSelected(PanelSeleccionarElementoEvent<ETipoProducto> evt) {
					refreshTablaPiezas(new ArrayList<Articulo>(), evt.getElements());
				}

			});
		}
		return panSelTipoProducto;
	}

	
	private FWJTable getTablaRemitoEntrada() {
		if(tablaRemitosEntrada == null) {
			tablaRemitosEntrada = new FWJTable(0, 4) {

				private static final long serialVersionUID = -2960448130069418277L;

				@Override
				public void newRowSelected(int newRow, int oldRow) {
						getBtnAceptar().setEnabled(newRow != -1);
				}

			};
			tablaRemitosEntrada.setStringColumn(0, "REMITO", 150, 150, true);

			tablaRemitosEntrada.setStringColumn(1, "PIEZAS DISPONIBLES", 390, 390, true);
			
			tablaRemitosEntrada.setStringColumn(2, "PIEZAS ELEGIDAS", 350, 350, true);

			tablaRemitosEntrada.setStringColumn(3, "", 0, 0, true);
			tablaRemitosEntrada.setAlignment(0, FWJTable.CENTER_ALIGN);
			tablaRemitosEntrada.addMouseListener(new MouseAdapter () {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						int selectedRow = getTablaRemitoEntrada().getSelectedRow();
						if(selectedRow != -1) {
							RemitoEntrada re = (RemitoEntrada)getTablaRemitoEntrada().getValueAt(selectedRow, 3);
							handleSeleccionRemitoEntrada(re);
						}
					}
				}

			});
		}
		return tablaRemitosEntrada;
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
					resultMap = null;
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
					dispose();
				}

			});
			btnAceptar.setEnabled(false);
		}
		return btnAceptar;
	}

	@SuppressWarnings("unchecked")
	private void handleSeleccionRemitoEntrada(RemitoEntrada re) {
		FWCheckBoxListDialog dialogo = new FWCheckBoxListDialog(owner);
		dialogo.setTitle("Seleccione las piezas");
		dialogo.setValores(toPRWrapper(re), true);
		dialogo.setVisible(true);
		List<PiezaRemitoWrapper> valoresSeleccionados = dialogo.getValoresSeleccionados();
		List<PiezaRemito> valPiezasRemitoSeleccionados = fromPRWrapper(valoresSeleccionados);
		setPiezaSeleccionada(re, valPiezasRemitoSeleccionados, getTablaRemitoEntrada().getSelectedRow());
	}

	private void setPiezaSeleccionada(RemitoEntrada re, List<PiezaRemito> valPiezasRemitoSeleccionados, int filaRemito) {
		String piezasListStr = getInfoPiezas(re, valPiezasRemitoSeleccionados);
		getTablaRemitoEntrada().setValueAt(piezasListStr, filaRemito, 2);
		List<PiezaRemito> piezasRemito = new ArrayList<PiezaRemito>();
		for(PiezaRemito pr : valPiezasRemitoSeleccionados) {
			if(re.getPiezas().contains(pr)) {
				piezasRemito.add(pr);
			}
		}
		resultMap.put(re, piezasRemito);
	}

	private List<PiezaRemito> fromPRWrapper(List<PiezaRemitoWrapper> valoresSeleccionados) {
		List<PiezaRemito> prList = new ArrayList<PiezaRemito>();
		for(PiezaRemitoWrapper prw : valoresSeleccionados) {
			prList.add(prw.getPr());
		}
		return prList;
	}

	private List<PiezaRemitoWrapper> toPRWrapper(RemitoEntrada re) {
		List<PiezaRemitoWrapper> prWrapperList = new ArrayList<JDialogSelRemitoEntradaConPiezasParaVender.PiezaRemitoWrapper>();
		for(PiezaRemito pr : re.getPiezas()) {
			if(pr.getEnSalida() == null || !pr.getEnSalida()) {
				prWrapperList.add(new PiezaRemitoWrapper(pr, getODT(pr, odtMap.get(re))));
			}
		}
		return prWrapperList;
	}

	private class PiezaRemitoWrapper {

		private PiezaRemito pr;
		private OrdenDeTrabajo odt;

		public PiezaRemitoWrapper(PiezaRemito pr, OrdenDeTrabajo odt) {
			this.pr = pr;
			this.odt = odt;
		}

		public PiezaRemito getPr() {
			return pr;
		}

		public String toString() {
			return "[" + pr.getMetros().toString() + (odt == null ? " CRUDO]" : " " + odt.getProducto().toString() + "]");
		}

	}
	
	private String getInfoPiezas(RemitoEntrada re, List<PiezaRemito> piezaRemitoList) {
		List<String> strList = new ArrayList<String>();
		for(PiezaRemito pr : piezaRemitoList) {
			if(re.getPiezas().contains(pr)) {
				OrdenDeTrabajo odt = getODT(pr, odtMap.get(re));
				strList.add("[" + pr.getMetros().toString() + (odt == null ? " CRUDO]" : " " + odt.getProducto().toString() + "]"));
			}
		}
		String piezasListStr = StringUtil.getCadena(strList, " ");
		return piezasListStr;
	}

	private OrdenDeTrabajo getODT(PiezaRemito pr, List<OrdenDeTrabajo> odtList) {
		for(OrdenDeTrabajo odt : odtList) {
			for(PiezaODT podt : odt.getPiezas()) {
				if(podt.getPiezaRemito()!= null && podt.getPiezaRemito().equals(pr)) {
					return odt;
				}
			}
		}
		return null;
	}

	public Map<RemitoEntrada, List<PiezaRemito>> getResult() {
		return resultMap;
	}

}