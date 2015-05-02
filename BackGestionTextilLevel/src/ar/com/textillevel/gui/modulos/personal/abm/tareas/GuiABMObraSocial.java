package ar.com.textillevel.gui.modulos.personal.abm.tareas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ObraSocial;
import ar.com.textillevel.modulos.personal.facade.api.remote.ObraSocialFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMObraSocial extends GuiABMListaTemplate {

	private static final long serialVersionUID = -860110063934681566L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private CLJTextField txtNombre;

	private ObraSocialFacadeRemote obraSocialFacade;
	private ObraSocial obraSocialActual;

	public GuiABMObraSocial(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Obras Sociales");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información de la Obra Social", getTabDetalle());		
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
		List<ObraSocial> obraSocialList = getObraSocialFacade().getAllOrderByName();
		lista.removeAll();
		for(ObraSocial os : obraSocialList) {
			lista.addItem(os);
		}
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
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setObraSocialActual(new ObraSocial());
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la obra social seleccionada?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getObraSocialFacade().remove(getObraSocialActual());
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
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar una obra social", "Error");
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
			ObraSocial obraSocial = getObraSocialFacade().save(getObraSocialActual());
			lista.setSelectedValue(obraSocial, true);
			return true;
		}
		return false;
	}
	
	private boolean validar() {
		if(getTxtNombre().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el nombre de la obra social.", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getObraSocialActual().setNombre(getTxtNombre().getText().toUpperCase());
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
		setObraSocialActual((ObraSocial)lista.getSelectedValue());
		limpiarDatos();
		if(getObraSocialActual() != null) {
			getTxtNombre().setText(getObraSocialActual().getNombre());
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	public ObraSocial getObraSocialActual() {
		return obraSocialActual;
	}

	public void setObraSocialActual(ObraSocial obraSocial) {
		this.obraSocialActual = obraSocial;
	}

	private CLJTextField getTxtNombre() {
		if(txtNombre == null){
			txtNombre = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	public ObraSocialFacadeRemote getObraSocialFacade() {
		if(obraSocialFacade == null){
			obraSocialFacade = GTLPersonalBeanFactory.getInstance().getBean2(ObraSocialFacadeRemote.class);
		}
		return obraSocialFacade;
	}
}
