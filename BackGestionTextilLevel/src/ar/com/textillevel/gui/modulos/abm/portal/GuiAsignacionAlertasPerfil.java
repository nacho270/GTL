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

import ar.com.fwcommon.componentes.FWDobleLista;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.facade.api.remote.PerfilFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.alertas.entidades.TipoAlerta;
import ar.com.textillevel.modulos.alertas.facade.api.remote.TipoAlertaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiAsignacionAlertasPerfil extends GuiABMListaTemplate {

	private static final long serialVersionUID = 2943484074693759764L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private FWJTextField txtNombrePerfil;
	private FWDobleLista listasAlertas;
	private JCheckBox chkAdmin;

	private PerfilFacadeRemote perfilFacade;
	private TipoAlertaFacadeRemote moduloFacade;
	
	private Perfil perfilActual;
	private List<TipoAlerta> allTipoAlertas;

	public GuiAsignacionAlertasPerfil(Integer idModulo) {
		super();
		setHijoCreado(true);
		getBtnAgregar().setVisible(false);
		getBtnEliminar().setVisible(false);
		setTitle("Administrar Alertas por Perfiles de usuario");
		setAllTipoAlertas(getTipoAlertaFacadeFacade().getAllOrderByName());
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información de la alerta", getTabDetalle());
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
			panDetalle.add(getListasAlertas(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5), 3, 1, 1, 1));
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
	
	private void cargarListaAlertas() {
		getListasAlertas().setListaOriginal(getAllTipoAlertas());
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {}

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
				FWJOptionPane.showErrorMessage(this, vle.getMensajeError(), "Error");
			}
		}
		return false;
	}
	
	private boolean validar() {
		if(getTxtNombrePerfil().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre del perfil.", "Advertencia");
			getTxtNombrePerfil().requestFocus();
			return false;
		}
		
		if(getListasAlertas().getListaDestino().getModel().getSize()==0){
			FWJOptionPane.showErrorMessage(this, "Debe elegir módulos para asignar al perfil.", "Advertencia");
			return false;
		}
		return true;
	}
	
	private void capturarSetearDatos() {
		List<TipoAlerta> alertas = new ArrayList<TipoAlerta>();
		for(int i = 0; i < getListasAlertas().getListaDestino().getModel().getSize();i++){
			alertas.add((TipoAlerta)getListasAlertas().getListaDestino().getModel().getElementAt(i));
		}
		getPerfilActual().setTiposDeAlertas(alertas);
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un perfil", "Error");
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
			getListasAlertas().setListaDestino(getPerfilActual().getTiposDeAlertas());
			getListasAlertas().setListaOriginal((List<TipoAlerta>)GenericUtils.restaConjuntosOrdenada(getAllTipoAlertas(),getPerfilActual().getTiposDeAlertas()));
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombrePerfil().setText("");
		getChkAdmin().setSelected(false);
		getListasAlertas().setListaDestino(new ArrayList<TipoAlerta>());
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		cargarListaAlertas();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	private FWJTextField getTxtNombrePerfil() {
		if(txtNombrePerfil == null){
			txtNombrePerfil = new FWJTextField(MAX_LONGITUD_NOMBRE);
			txtNombrePerfil.setEditable(false);
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

	private FWDobleLista getListasAlertas() {
		if(listasAlertas == null){
			listasAlertas = new FWDobleLista("Alertas disponibles", "Alertas agregadas",FWDobleLista.VERTICAL_LAYOUT);
			listasAlertas.setPreferredSize(new Dimension(100, 400));
		}
		return listasAlertas;
	}

	private TipoAlertaFacadeRemote getTipoAlertaFacadeFacade() {
		if(moduloFacade == null){
			moduloFacade = GTLBeanFactory.getInstance().getBean2(TipoAlertaFacadeRemote.class);
		}
		return moduloFacade;
	}

	public List<TipoAlerta> getAllTipoAlertas() {
		return allTipoAlertas;
	}

	public void setAllTipoAlertas(List<TipoAlerta> allAlertas) {
		this.allTipoAlertas = allAlertas;
	}

	private JCheckBox getChkAdmin() {
		if(chkAdmin == null){
			chkAdmin = new JCheckBox("Perfil administrador");
		}
		return chkAdmin;
	}
}
