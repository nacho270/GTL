package ar.com.textillevel.gui.modulos.odt.gui.secuencias;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;
import ar.com.textillevel.modulos.odt.facade.api.remote.SecuenciaTipoProductoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ABMSecuenciasModelTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMSecuencias extends GuiABMListaTemplate{

	private static final long serialVersionUID = -849341320717409136L;

	private JPanel tabDetalle;
	private ABMSecuenciasModelTO model;
	private PanelVisualizadorSecuenciasABM panelVisualizador;
	
	private Cliente clienteActual;
	private SecuenciaTipoProductoFacadeRemote secuenciaFacade;
	
	public GuiABMSecuencias(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Secuencias");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información de la secuencia", getTabDetalle());
		getBtnAgregar().setVisible(false);
		getBtnEliminar().setVisible(false);
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanelVisualizador(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}

	@Override
	public void cargarLista() {
		for(ETipoProducto etp : ETipoProducto.getValuesAsSortedList()){
			if(etp.getSector()!=null){
				lista.addItem(etp);
			}
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) { }

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			GuiUtil.setEstadoPanel(getTabDetalle(), true);
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un tipo de máquina", "Error");
			return false;
		}
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
		getPanelVisualizador().limpiarFiltro();
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		getSecuenciaFacade().persistModel(getModel());
		FWJOptionPane.showInformationMessage(this, "Las secuencias se han grabado", "Informacion");
		getPanelVisualizador().limpiarFiltro();
		return true;
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), true);
		getPanelVisualizador().setModoConsulta(estado);
	}

	@Override
	public void limpiarDatos() {
		getPanelVisualizador().limpiarSecuencias();
		getPanelVisualizador().limpiarPasos();
		getPanelVisualizador().limpiarFiltro();
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		ETipoProducto tipoSeleccionado = (ETipoProducto)lista.getSelectedValue();
		List<SecuenciaTipoProducto> secuencias = getSecuenciaFacade().getAllByTipoProductoYCliente(tipoSeleccionado,null);
		setModel(new ABMSecuenciasModelTO());
		getModel().setSecuenciasAPersistir(secuencias);
		limpiarDatos();
		getPanelVisualizador().agregarElementos(getModel().getSecuenciasAPersistir());
	}

	@Override
	public void itemComboMaestroSeleccionado() { }
	
	private class PanelVisualizadorSecuenciasABM extends PanelVisualizadorSecuencias{

		private static final long serialVersionUID = -3755245950882997899L;

		public PanelVisualizadorSecuenciasABM(Frame frame, boolean edicion) {
			super(frame, edicion);
		}

		@Override
		public boolean validarAgregar() {
			JDialogCrearEditarSecuencia dialog = new JDialogCrearEditarSecuencia(GuiABMSecuencias.this.getFrame(), (ETipoProducto)lista.getSelectedValue(),getClienteActual());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getModel().getSecuenciasAPersistir().add(dialog.getSecuenciaFinal());
				refreshTable();
			}
			return false;
		}
		
		private void refreshTable() {
			super.limpiarSecuencias();
			super.agregarElementos(getModel().getSecuenciasAPersistir());
		}
		
		@Override
		public boolean validarQuitar() {
			int fila = super.getSelectedRowSecuencia();
			getModel().getSecuenciasAPersistir().remove(fila);
			SecuenciaTipoProducto elemento = super.getSecuencia(fila);
			if(elemento.getId()!=null){
				getModel().getSecuenciasABorrar().add(elemento);
			}
			refreshTable();
			return false;
		}
		
		@Override
		public void botonModificarPresionado(int filaSeleccionada) {
			JDialogCrearEditarSecuencia dialog = new JDialogCrearEditarSecuencia(GuiABMSecuencias.this.getFrame(), getModel().getSecuenciasAPersistir().get(filaSeleccionada));
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getModel().getSecuenciasAPersistir().set(filaSeleccionada,dialog.getSecuenciaFinal());
				refreshTable();
			}
		}
		
		@Override
		protected void botonLimpiarPresionado() {
			getPanelFiltro().setCliente(null);
			List<SecuenciaTipoProducto> sqsTemp = new ArrayList<SecuenciaTipoProducto>(getModel().getSecuenciasAPersistir());
			itemSelectorSeleccionado(lista.getSelectedIndex());
			for(SecuenciaTipoProducto s : sqsTemp){
				if(s.getId() == null && !getModel().getSecuenciasAPersistir().contains(s)){
					getModel().getSecuenciasAPersistir().add(s);
					agregarElemento(s);
				}
			}
		}
		
		@Override
		protected void clienteEncontrado(Cliente cliente) {
			if(cliente.getNroCliente().intValue() == 1){
				setClienteActual(null);				
			}else{
				setClienteActual(cliente);
			}
			ETipoProducto tipoSeleccionado = (ETipoProducto)lista.getSelectedValue();
			List<SecuenciaTipoProducto> secuencias = getSecuenciaFacade().getAllByTipoProductoYCliente(tipoSeleccionado,getClienteActual());
			setModel(new ABMSecuenciasModelTO());
			getModel().setSecuenciasAPersistir(secuencias);
			limpiarDatos();
			agregarElementos(getModel().getSecuenciasAPersistir());
		}
	}
	
	public ABMSecuenciasModelTO getModel() {
		return model;
	}

	public void setModel(ABMSecuenciasModelTO model) {
		this.model = model;
	}

	public SecuenciaTipoProductoFacadeRemote getSecuenciaFacade() {
		if(secuenciaFacade == null){
			secuenciaFacade = GTLBeanFactory.getInstance().getBean2(SecuenciaTipoProductoFacadeRemote.class);
		}
		return secuenciaFacade;
	}
	
	public Cliente getClienteActual() {
		return clienteActual;
	}

	public void setClienteActual(Cliente clienteActual) {
		this.clienteActual = clienteActual;
	}

	public PanelVisualizadorSecuenciasABM getPanelVisualizador() {
		if(panelVisualizador == null){
			panelVisualizador = new PanelVisualizadorSecuenciasABM(this.getFrame(), false);
		}
		return panelVisualizador;
	}
}
