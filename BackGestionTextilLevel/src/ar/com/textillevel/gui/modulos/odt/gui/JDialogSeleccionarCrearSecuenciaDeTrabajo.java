package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import main.servicios.alertas.AlertaFactory;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.PanelVisualizadorSecuencias;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.event.SecuenciaBloqueadaEventData;
import ar.com.textillevel.gui.modulos.odt.gui.secuencias.event.SecuenciaBloqueadaEventListener;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.alertas.facade.api.remote.AlertaFacadeRemote;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.PasoSecuencia;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.SecuenciaODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.SecuenciaTipoProductoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.stock.ETipoInfoBajaStock;
import ar.com.textillevel.modulos.odt.to.stock.InfoBajaStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSeleccionarCrearSecuenciaDeTrabajo extends JDialog {

	private static final long serialVersionUID = 5022735590951140125L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	private OrdenDeTrabajo odt;
	private PanelVisualizadorSecuencias panelVisualizador;
	
	private final Map<PasoSecuenciaODT, List<InstruccionProcedimiento>> mapaPasosOdt = new LinkedHashMap<PasoSecuenciaODT, List<InstruccionProcedimiento>>();

	private boolean acepto;
	private SecuenciaTipoProducto secuenciaElegida;
	private boolean secuenciaBloqueada = false;
	private List<SecuenciaTipoProducto> secuenciasPosibles;
	private Map<ETipoInfoBajaStock, Set<InfoBajaStock>> mapaWarnings;

	private SecuenciaTipoProductoFacadeRemote secuenciaFacade;

	public JDialogSeleccionarCrearSecuenciaDeTrabajo(Frame frame, OrdenDeTrabajo odt) {
		super(frame);
		setOdt(odt);
		setSecuenciasPosibles(getSecuenciaFacade().getAllByTipoProductoYCliente(getOdt().getProducto().getTipo(), odt.getRemito().getCliente(), true));
		setUpComponentes();
		setUpScreen();
		getPanelVisualizador().llenarFiltro(odt.getRemito().getCliente());
		getPanelVisualizador().agregarElementos(getSecuenciasPosibles());
	}

	private void setUpScreen() {
		setTitle("Seleccionar secuencia");
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(660, 600));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelVisualizador(),BorderLayout.NORTH);
		
		JPanel panSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panSur.add(getBtnAceptar());
		panSur.add(getBtnCancelar());
		
		add(panSur,BorderLayout.SOUTH);
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public SecuenciaTipoProducto getSecuenciaElegida() {
		return secuenciaElegida;
	}

	public void setSecuenciaElegida(SecuenciaTipoProducto secuenciaElegida) {
		this.secuenciaElegida = secuenciaElegida;
	}

	public OrdenDeTrabajo getOdt() {
		return odt;
	}

	public void setOdt(OrdenDeTrabajo odt) {
		this.odt = odt;
	}

	public SecuenciaTipoProductoFacadeRemote getSecuenciaFacade() {
		if (secuenciaFacade == null) {
			secuenciaFacade = GTLBeanFactory.getInstance().getBean2(SecuenciaTipoProductoFacadeRemote.class);
		}
		return secuenciaFacade;
	}

	public List<SecuenciaTipoProducto> getSecuenciasPosibles() {
		return secuenciasPosibles;
	}

	public void setSecuenciasPosibles(List<SecuenciaTipoProducto> secuenciasPosibles) {
		this.secuenciasPosibles = secuenciasPosibles;
	}

	public PanelVisualizadorSecuencias getPanelVisualizador() {
		if (panelVisualizador == null) {
			panelVisualizador = new PanelVisualizadorSecuencias(JDialogSeleccionarCrearSecuenciaDeTrabajo.this, false);
			panelVisualizador.agregarBotonFijarSecuencia();
			panelVisualizador.setModoNoModificacion();
			panelVisualizador.addSecuenciaBloqueadaEventListener(new SecuenciaBloqueadaEventListener() {
				public void secuenciaBloqueada(SecuenciaBloqueadaEventData secuenciaBloqueadaEventData) {
					mapaPasosOdt.clear();
					secuenciaBloqueada = secuenciaBloqueadaEventData.getSecuencia()!=null;
					SecuenciaTipoProducto secuenciaBloqueada = secuenciaBloqueadaEventData.getSecuencia();
					setSecuenciaElegida(secuenciaBloqueada);
				}
			});
		}
		return panelVisualizador;
	}
	
	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Guardar secuencia");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(secuenciaBloqueada){
						PrecioMateriaPrimaFacadeRemote precioMPFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
						Map<MateriaPrima, Float> mapaStock = new HashMap<MateriaPrima, Float>();
						for(PasoSecuencia paso : getSecuenciaElegida().getPasos()){
							PasoSecuenciaODT pasoODT = paso.toPasoODT();
							List<InstruccionProcedimiento> instrucciones = pasoODT.getSubProceso().getPasos();
							ExplotadorInstrucciones explotador = new ExplotadorInstrucciones(getOdt());
							for(int i = 0; i< instrucciones.size();i++){
								InstruccionProcedimiento inst = instrucciones.get(i);
								inst.accept(explotador);
								instrucciones.set(i, explotador.getInstruccionExplotada());
								for(MateriaPrima mp : explotador.getMapaStock().keySet()){
									if(mapaStock.get(mp) == null){
										mapaStock.put(mp, 0f);
									}
									mapaStock.put(mp, mapaStock.get(mp) + explotador.getMapaStock().get(mp));
								}
							}
							mapaPasosOdt.put(pasoODT, instrucciones);
						}
						tratarStock(precioMPFacade, mapaStock);
					}else{
						FWJOptionPane.showErrorMessage(JDialogSeleccionarCrearSecuenciaDeTrabajo.this, "Debe elegir una secuencia", "Error");
					}
				}

				private void tratarStock(PrecioMateriaPrimaFacadeRemote precioMPFacade, Map<MateriaPrima, Float> mapaStock) {
					mapaWarnings = new HashMap<ETipoInfoBajaStock, Set<InfoBajaStock>>();
					for(ETipoInfoBajaStock t : ETipoInfoBajaStock.values()){
						mapaWarnings.put(t, new HashSet<InfoBajaStock>());
					}
					for(MateriaPrima mp : mapaStock.keySet()){
					 List<PrecioMateriaPrima> pmps = precioMPFacade.getPrecioMateriaPrimaByIdsMateriasPrimas(Collections.singletonList(mp.getId()));
						if(pmps == null || pmps.isEmpty()){
							throw new RuntimeException("No se se ha definido un proveedor para " + mp.getDescripcion());
						}
						Float cantidad = mapaStock.get(mp);
						PrecioMateriaPrima pmp = pmps.get(0);
						if(pmp.getStockActual().doubleValue() < cantidad){
							mapaWarnings.get(ETipoInfoBajaStock.STOCK_INSUFICIENTE).add(new InfoBajaStock(ETipoInfoBajaStock.STOCK_INSUFICIENTE, pmp, cantidad));
						}else if( (pmp.getStockActual().doubleValue() - cantidad) <= pmp.getPuntoDePedido().doubleValue()){
							mapaWarnings.get(ETipoInfoBajaStock.STOCK_DEBAJO_PTO_PEDIDO).add(new InfoBajaStock(ETipoInfoBajaStock.STOCK_DEBAJO_PTO_PEDIDO, pmp, cantidad));
						}else{
							mapaWarnings.get(ETipoInfoBajaStock.STOCK_OK).add(new InfoBajaStock(ETipoInfoBajaStock.STOCK_OK, pmp, cantidad));
						}
					}
					
					Set<InfoBajaStock> mpDebajoPtoReposicion = mapaWarnings.get(ETipoInfoBajaStock.STOCK_DEBAJO_PTO_PEDIDO);
					Set<InfoBajaStock> mpStockInsuficiente = mapaWarnings.get(ETipoInfoBajaStock.STOCK_INSUFICIENTE);
					if(mpDebajoPtoReposicion.isEmpty() && mpStockInsuficiente.isEmpty()){ //esta todo bien
						crearSecuenciaODT();
					}else{ //warnings
						if(!mpDebajoPtoReposicion.isEmpty()){
							FWJOptionPane.showWarningMessage(JDialogSeleccionarCrearSecuenciaDeTrabajo.this, "Las siguientes materias primas quedarán por debajo el punto de pedido:\n"+createListaWaringMP(mpDebajoPtoReposicion), "Advertencia");
							crearAlertas(mpDebajoPtoReposicion);
						}
						if(!mpStockInsuficiente.isEmpty()){
							if(FWJOptionPane.showQuestionMessage(JDialogSeleccionarCrearSecuenciaDeTrabajo.this, "No cuenta con stock suficiente de las siguientes materias primas. Desea continuar?\n"+createListaWaringMP(mpStockInsuficiente), "Pregunta")==FWJOptionPane.YES_OPTION){
								crearSecuenciaODT();
								crearAlertas(mpStockInsuficiente);
							}else{
								crearAlertas(mpStockInsuficiente);
							}
						}
					}
				}

				private void crearAlertas(Set<InfoBajaStock> infos){
					for(InfoBajaStock info : infos){
						GTLBeanFactory.getInstance().getBean2(AlertaFacadeRemote.class).crearAlerta(AlertaFactory.crearAlertaStock(info.getPrecioMateriaPrima()));
					}
				}
				
				private String createListaWaringMP(Set<InfoBajaStock> listado) {
					String ret = "";
					for(InfoBajaStock info : listado){
						String unidad = info.getPrecioMateriaPrima().getMateriaPrima().getUnidad().getDescripcion();
						ret += info.getPrecioMateriaPrima().getMateriaPrima().getDescripcion() + 
							". Cantidad necesaria: " + info.getCantidadADescontar() + " " + unidad +
							". Stock actual: " + GenericUtils.getDecimalFormat().format(info.getPrecioMateriaPrima().getStockInicial().doubleValue()) + " " + unidad +
							". Punto de pedido: " + GenericUtils.getDecimalFormat().format(info.getPrecioMateriaPrima().getPuntoDePedido().doubleValue()) + " " + unidad + "\n";
					}
					return ret;
				}

				private void crearSecuenciaODT() {
					SecuenciaODT secuenciaODT = new SecuenciaODT();
					secuenciaODT.setPasos(new ArrayList<PasoSecuenciaODT>(mapaPasosOdt.keySet()));
					secuenciaODT.setCliente(getOdt().getRemito().getCliente());
					secuenciaODT.setTipoProducto(getSecuenciaElegida().getTipoProducto());
					secuenciaODT.setNombre("Secuencia - ODT " + getOdt().getCodigo());
					secuenciaODT.setOdt(getOdt());
					getOdt().setSecuenciaDeTrabajo(secuenciaODT);
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	
	public Map<ETipoInfoBajaStock, Set<InfoBajaStock>> getMapaWarnings() {
		return mapaWarnings;
	}
}
