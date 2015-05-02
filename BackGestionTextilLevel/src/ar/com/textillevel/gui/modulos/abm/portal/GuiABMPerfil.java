package ar.com.textillevel.gui.modulos.abm.portal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLDobleLista;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.portal.Modulo;
import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.facade.api.remote.ModuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.PerfilFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMPerfil extends GuiABMListaTemplate {

	private static final long serialVersionUID = 2943484074693759764L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private CLJTextField txtNombrePerfil;
	private CLDobleLista listasModulos;
	private JCheckBox chkAdmin;

	private PerfilFacadeRemote perfilFacade;
	private ModuloFacadeRemote moduloFacade;
	
	private Perfil perfilActual;
	private List<Modulo> allModulos;

	public GuiABMPerfil(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Perfiles de usuario");
		setAllModulos(getModuloFacade().getAllOrderByName());
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información del módulo", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			panDetalle.add(getChkAdmin(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 1, 1, 1, 0));
			panDetalle.add(getTxtNombrePerfil(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 1, 1, 1, 0));
			panDetalle.add(getListasModulos(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5), 3, 1, 1, 1));
		}
		return panDetalle;
	}

	@Override
	public void cargarLista() {
		List<Perfil> perfiles = getPerfilFacade().getAllOrderByName();
		lista.removeAll();
		for (Perfil p : perfiles) {
			lista.addItem(p);
		}
	}
	
	private void cargarListaModulos() {
		getListasModulos().setListaOriginal(getAllModulos());
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setPerfilActual(new Perfil());
		getTxtNombrePerfil().requestFocus();
		getListasModulos().setListaOriginal(getAllModulos());
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (CLJOptionPane.showQuestionMessage(this,"¿Está seguro que desea eliminar el perfil seleccionado?","Confirmación") == CLJOptionPane.YES_OPTION){
				getPerfilFacade().remove(getPerfilActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			try{
				Perfil perfil = getPerfilFacade().save(getPerfilActual());
				lista.setSelectedValue(perfil, true);
				cargarLista();
				return true;
			}catch(ValidacionException vle){
				CLJOptionPane.showErrorMessage(this, vle.getMensajeError(), "Error");
			}
		}
		return false;
	}
	
	private boolean validar() {
		if(getTxtNombrePerfil().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el nombre del perfil.", "Advertencia");
			getTxtNombrePerfil().requestFocus();
			return false;
		}
		
		if(getListasModulos().getListaDestino().getModel().getSize()==0){
			CLJOptionPane.showErrorMessage(this, "Debe elegir módulos para asignar al perfil.", "Advertencia");
			return false;
		}
		return true;
	}
	
	private void capturarSetearDatos() {
		getPerfilActual().setNombre(getTxtNombrePerfil().getText());
		getPerfilActual().setIsAdmin(getChkAdmin().isSelected());
		List<Modulo> mods = new ArrayList<Modulo>();
		for(int i = 0; i < getListasModulos().getListaDestino().getModel().getSize();i++){
			mods.add((Modulo)getListasModulos().getListaDestino().getModel().getElementAt(i));
		}
		getPerfilActual().setModulos(mods);
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombrePerfil().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un perfil", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {

	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setPerfilActual((Perfil)lista.getSelectedValue());
		limpiarDatos();
		if(getPerfilActual() != null) {
			getTxtNombrePerfil().setText(getPerfilActual().getNombre());
			getChkAdmin().setSelected(getPerfilActual().getIsAdmin());
			getListasModulos().setListaDestino(getPerfilActual().getModulos());
			getListasModulos().setListaOriginal((List<Modulo>)GenericUtils.restaConjuntosOrdenada(getAllModulos(),getPerfilActual().getModulos()));
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombrePerfil().setText("");
		getChkAdmin().setSelected(false);
		getListasModulos().setListaDestino(new ArrayList<Modulo>());
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		cargarListaModulos();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	private CLJTextField getTxtNombrePerfil() {
		if(txtNombrePerfil == null){
			txtNombrePerfil = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombrePerfil;
	}

	private PerfilFacadeRemote getPerfilFacade() {
		if(perfilFacade == null){
			perfilFacade = GTLBeanFactory.getInstance().getBean2(PerfilFacadeRemote.class);
		}
		return perfilFacade;
	}

	private Perfil getPerfilActual() {
		return perfilActual;
	}

	private void setPerfilActual(Perfil perfilActual) {
		this.perfilActual = perfilActual;
	}

	private CLDobleLista getListasModulos() {
		if(listasModulos == null){
			listasModulos = new CLDobleLista("Módulos disponibles", "Módulos agregados",CLDobleLista.VERTICAL_LAYOUT);
			listasModulos.setPreferredSize(new Dimension(100, 400));
		}
		return listasModulos;
	}

	private ModuloFacadeRemote getModuloFacade() {
		if(moduloFacade == null){
			moduloFacade = GTLBeanFactory.getInstance().getBean2(ModuloFacadeRemote.class);
		}
		return moduloFacade;
	}

	public List<Modulo> getAllModulos() {
		return allModulos;
	}

	public void setAllModulos(List<Modulo> allModulos) {
		this.allModulos = allModulos;
	}

	private JCheckBox getChkAdmin() {
		if(chkAdmin == null){
			chkAdmin = new JCheckBox("Perfil administrador");
		}
		return chkAdmin;
	}
}
