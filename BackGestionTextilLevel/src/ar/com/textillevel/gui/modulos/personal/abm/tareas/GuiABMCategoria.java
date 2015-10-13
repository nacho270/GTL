package ar.com.textillevel.gui.modulos.personal.abm.tareas;

import java.awt.BorderLayout;
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
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.panels.PanelSeleccionarElementos;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.CategoriaFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.PuestoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMCategoria extends GuiABMListaTemplate {

	private static final long serialVersionUID = 5490390741310236258L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private FWJTextField txtNombre;
	private PanelSeleccionarElementos<Puesto> panPuestos;

	private SindicatoFacadeRemote sindicatoFacade;
	private CategoriaFacadeRemote categoriaFacade;
	private PuestoFacadeRemote puestoFacade;
	
	private Categoria categoriaActual;

	public GuiABMCategoria(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Categorías");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información de la Categoría", getTabDetalle());		
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
			panDetalle.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanPuestos(),  GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
		}
		return panDetalle;
	}

	private PanelSeleccionarElementos<Puesto> getPanPuestos() {
		if(panPuestos == null) {
			panPuestos = new PanelSeleccionarElementos<Puesto>(GuiABMCategoria.this.getFrame(), new ArrayList<Puesto>(), "Puestos");
		}
		return panPuestos;
	}

	@Override
	public void cargarLista() {
		Sindicato sindicatoSeleccionado = (Sindicato)getItemComboMaestroSeleccionado();
		if(sindicatoSeleccionado != null) {
			List<Categoria> categoriaList = getCategoriaFacade().getAllByIdSindicato(sindicatoSeleccionado.getId());
			lista.clear();
			for(Categoria c : categoriaList) {
				lista.addItem(c);
			}
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		setContenidoComboMaestro(getSindicatoFacade().getAllOrderByName(), "Sindicato: ");
		cargarLista();
		Sindicato sindicatoSel = (Sindicato)getItemComboMaestroSeleccionado();
		if(sindicatoSel != null) {
			getPanPuestos().setElements(getPuestoFacade().getAllByIdSindicato(sindicatoSel.getId()));
		}
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		Sindicato sindicato = (Sindicato)getItemComboMaestroSeleccionado();
		Categoria categoria = new Categoria();
		categoria.setSindicato(sindicato);
		setCategoriaActual(categoria);
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la Categoría seleccionada?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getCategoriaFacade().remove(getCategoriaActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombre().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar una categoría", "Error");
			return false;
		}
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			Categoria categoria = getCategoriaFacade().save(getCategoriaActual());
			lista.setSelectedValue(categoria, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(getTxtNombre().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre de la categoría.", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getPanPuestos().getSelectedElements().isEmpty()) {
			FWJOptionPane.showErrorMessage(this, "Debe definir al menos un puesto para la categoría.", "Advertencia");
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getCategoriaActual().setNombre(getTxtNombre().getText().toUpperCase());
		getCategoriaActual().getPuestos().clear();
		getCategoriaActual().getPuestos().addAll(getPanPuestos().getSelectedElements());
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	@Override
	public void limpiarDatos() {
		getTxtNombre().setText("");
		getPanPuestos().clear();
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		if(nivelItemSelector != -1) {
			Categoria cat = (Categoria)lista.getSelectedValue();
			setCategoriaActual(getCategoriaFacade().getByIdEager(cat.getId()));
			limpiarDatos();
			if(getCategoriaActual() != null) {
				getTxtNombre().setText(getCategoriaActual().getNombre());
				getPanPuestos().setElementsAndSelectedElements(getPuestoFacade().getAllByIdSindicato(cat.getSindicato().getId()), new ArrayList<Puesto>(getCategoriaActual().getPuestos()));
			}
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		cargarLista();
		Sindicato sindicatoSel = (Sindicato)getItemComboMaestroSeleccionado();
		if(sindicatoSel != null) {
			getPanPuestos().setElements(getPuestoFacade().getAllByIdSindicato(sindicatoSel.getId()));
		}
	}

	public Categoria getCategoriaActual() {
		return categoriaActual;
	}

	public void setCategoriaActual(Categoria categoria) {
		this.categoriaActual = categoria;
	}

	public FWJTextField getTxtNombre() {
		if(txtNombre == null){
			txtNombre = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	private SindicatoFacadeRemote getSindicatoFacade() {
		if(sindicatoFacade == null){
			sindicatoFacade = GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class);
		}
		return sindicatoFacade;
	}

	private CategoriaFacadeRemote getCategoriaFacade() {
		if(categoriaFacade == null){
			categoriaFacade = GTLPersonalBeanFactory.getInstance().getBean2(CategoriaFacadeRemote .class);
		}
		return categoriaFacade;
	}

	private PuestoFacadeRemote getPuestoFacade() {
		if(puestoFacade == null){
			puestoFacade = GTLPersonalBeanFactory.getInstance().getBean2(PuestoFacadeRemote .class);
		}
		return puestoFacade;
	}

}