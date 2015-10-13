package ar.com.textillevel.gui.modulos.personal.abm.tareas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.PuestoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMPuesto extends GuiABMListaTemplate {

	private static final long serialVersionUID = 6151880574292501454L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private FWJTextField txtNombre;

	private SindicatoFacadeRemote sindicatoFacade;
	private PuestoFacadeRemote puestoFacade;
	
	private Puesto puestoActual;


	public GuiABMPuesto(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Puestos");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información del Puesto", getTabDetalle());		
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
		}
		return panDetalle;
	}
	
	@Override
	public void cargarLista() {
		Sindicato sindicatoSeleccionado = (Sindicato)getItemComboMaestroSeleccionado();
		if(sindicatoSeleccionado != null) {
			List<Puesto> puestoList = getPuestoFacade().getAllByIdSindicato(sindicatoSeleccionado.getId());
			lista.clear();
			for(Puesto p : puestoList) {
				lista.addItem(p);
			}
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		setContenidoComboMaestro(getSindicatoFacade().getAllOrderByName(), "Sindicatos: ");
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		Sindicato sindicato = (Sindicato)getItemComboMaestroSeleccionado();
		Puesto puesto = new Puesto();
		puesto.setSindicato(sindicato);
		setPuestoActual(puesto);
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el Puesto seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getPuestoFacade().remove(getPuestoActual());
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
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un puesto", "Error");
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
			Puesto puesto = getPuestoFacade().save(getPuestoActual());
			lista.setSelectedValue(puesto, true);
			return true;
		}
		return false;
	}
	
	private boolean validar() {
		if(getTxtNombre().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre del puesto.", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getPuestoActual().setNombre(getTxtNombre().getText().toUpperCase());
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	@Override
	public void limpiarDatos() {
		getTxtNombre().setText("");
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setPuestoActual((Puesto)lista.getSelectedValue());
		limpiarDatos();
		if(getPuestoActual() != null) {
			getTxtNombre().setText(getPuestoActual().getNombre());
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		cargarLista();
	}

	public Puesto getPuestoActual() {
		return puestoActual;
	}

	public void setPuestoActual(Puesto puesto) {
		this.puestoActual = puesto;
	}

	public FWJTextField getTxtNombre() {
		if(txtNombre == null){
			txtNombre = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	public SindicatoFacadeRemote getSindicatoFacade() {
		if(sindicatoFacade == null){
			sindicatoFacade = GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class);
		}
		return sindicatoFacade;
	}

	public PuestoFacadeRemote getPuestoFacade() {
		if(puestoFacade == null){
			puestoFacade = GTLPersonalBeanFactory.getInstance().getBean2(PuestoFacadeRemote.class);
		}
		return puestoFacade;
	}

}