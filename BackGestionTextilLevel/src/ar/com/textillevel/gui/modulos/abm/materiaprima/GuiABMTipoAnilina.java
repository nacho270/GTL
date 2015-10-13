package ar.com.textillevel.gui.modulos.abm.materiaprima;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;
import ar.com.textillevel.facade.api.remote.TipoAnilinaFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;
import ar.com.textillevel.util.GTLBeanFactory;
import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

public class GuiABMTipoAnilina extends GuiABMListaTemplate {
	
	private static final long serialVersionUID = -4193424482048808415L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;

	private FWJTextField txtNombreAnilina;
	private PanSeleccionTipoArticulos panSelTipoArticulos;

	private TipoAnilina tipoAnilinaActual;
	private TipoAnilinaFacadeRemote tipoAnilinaFacade;
	private TipoArticuloFacadeRemote tipoArticuloFacade;
	private List<TipoArticulo> allTipoArticulos;

	public GuiABMTipoAnilina(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar tipos de anilina");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información del tipo de anilina", getTabDetalle());		
	}
	
	private JPanel getTabDetalle() {
		if(tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}
	
	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Nombre: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtDescripcionAnilina(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getPanSelTipoArticulos(),  createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
		}
		return panDetalle;
	}

	@Override
	public void cargarLista() {
		List<TipoAnilina> articuloList = getTipoAnilinaFacade().getAllOrderByName();
		lista.removeAll();
		for(TipoAnilina c : articuloList) {
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setTipoAnilinaActual(new TipoAnilina());
		getTxtDescripcionAnilina().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el tipo de anilina seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getTipoAnilinaFacade().remove(getTipoAnilinaActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			TipoAnilina tipoAnilina = getTipoAnilinaFacade().save(getTipoAnilinaActual());
			lista.setSelectedValue(tipoAnilina, true);
			cargarLista();
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(getTxtDescripcionAnilina().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre del tipo de la anilina.", "Advertencia");
			getTxtDescripcionAnilina().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getTipoAnilinaActual().setDescripcion(getTxtDescripcionAnilina().getText().toUpperCase());
		getTipoAnilinaActual().getTiposArticulosSoportados().clear();
		getTipoAnilinaActual().getTiposArticulosSoportados().addAll(getPanSelTipoArticulos().getSelectedElements());
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtDescripcionAnilina().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un artículo", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		TipoAnilina ta = (TipoAnilina)lista.getSelectedValue();
		ta = getTipoAnilinaFacade().getByIdEager(ta.getId());
		setTipoAnilinaActual(ta);
		limpiarDatos();
		if(getTipoAnilinaActual() != null) {
			getTxtDescripcionAnilina().setText(getTipoAnilinaActual().getDescripcion());
			getPanSelTipoArticulos().setElementsAndSelectedElements(getAllTipoArticulos(), new ArrayList<TipoArticulo>(getTipoAnilinaActual().getTiposArticulosSoportados()));
		}
	}

	private List<TipoArticulo> getAllTipoArticulos() {
		if(allTipoArticulos == null) {
			allTipoArticulos = getTipoArticuloFacade().getAllTipoArticulos();
		}
		return allTipoArticulos;
	}

	@Override
	public void limpiarDatos() {
		getTxtDescripcionAnilina().setText("");
		getPanSelTipoArticulos().clear();
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);		
	}

	public TipoAnilina getTipoAnilinaActual() {
		return tipoAnilinaActual;
	}

	public void setTipoAnilinaActual(TipoAnilina tipoAnilina) {
		this.tipoAnilinaActual = tipoAnilina;
	}

	private FWJTextField getTxtDescripcionAnilina() {
		if(txtNombreAnilina == null){
			txtNombreAnilina = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombreAnilina;
	}

	private PanSeleccionTipoArticulos getPanSelTipoArticulos() {
		if(panSelTipoArticulos == null) {
			panSelTipoArticulos = new PanSeleccionTipoArticulos(GuiABMTipoAnilina.this.getFrame(), getTipoArticuloFacade().getAllTipoArticulos(), "Tipos de Artículos:");
		}
		return panSelTipoArticulos;
	}

	private TipoAnilinaFacadeRemote getTipoAnilinaFacade() {
		if(tipoAnilinaFacade == null){
			tipoAnilinaFacade = GTLBeanFactory.getInstance().getBean2(TipoAnilinaFacadeRemote.class);
		}
		return tipoAnilinaFacade;
	}

	private TipoArticuloFacadeRemote getTipoArticuloFacade() {
		if(tipoArticuloFacade == null){
			tipoArticuloFacade = GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class);
		}
		return tipoArticuloFacade;
	}

	private static class PanSeleccionTipoArticulos extends PanelSeleccionarElementos<TipoArticulo> {

		private static final long serialVersionUID = 1L;

		public PanSeleccionTipoArticulos(Frame owner, List<TipoArticulo> elements, String titleLabel) {
			super(owner, elements, titleLabel);
		}

	}

}
