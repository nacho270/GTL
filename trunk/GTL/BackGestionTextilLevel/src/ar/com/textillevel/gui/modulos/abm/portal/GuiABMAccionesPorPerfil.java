package ar.com.textillevel.gui.modulos.abm.portal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.clarin.fwjava.componentes.CLDobleLista;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
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

public class GuiABMAccionesPorPerfil extends GuiABMListaTemplate {

	private static final long serialVersionUID = 2943484074693759764L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;

	private CLJTextField txtNombrePerfil;
	private CLDobleLista listasModulos;
	private CLJTable tablaModulos;

	private PerfilFacadeRemote perfilFacade;
	private ModuloFacadeRemote moduloFacade;
	
	private Perfil perfilActual;
	private List<Modulo> allModulosConAcciones;

	public GuiABMAccionesPorPerfil(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Acciones de Perfil");
		setAllModulosConAcciones(getModuloFacade().getAllWithActions());
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información del módulo", getPanDetalle());
	}

	private JPanel getPanDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new GridBagLayout());
			tabDetalle.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(10, 10, 5, 5),1, 1, 0, 0));
			tabDetalle.add(getTxtNombrePerfil(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL, new Insets(10, 10,5, 5), 1, 1, 0, 0));
			tabDetalle.add(getPanTabla(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.NORTH,GridBagConstraints.BOTH, new Insets(10, 10,5, 5), 2, 1, 0, 0.2));
			tabDetalle.add(getListasModulos(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,5,0,5), 2, 1, 1, 0.8));
		}
		return tabDetalle;
	}

	private JScrollPane getPanTabla() {
		JScrollPane sp = new JScrollPane(getTablaModulos());
		return sp;
	}

	private CLJTable getTablaModulos() {
		if(tablaModulos == null) {
			tablaModulos = new CLJTable(0, 2);
			tablaModulos.setStringColumn(0, "MODULO", 600);
			tablaModulos.setStringColumn(1, "", 0);
		}
		return tablaModulos;
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
			getListasModulos().setListaDestino(getPerfilActual().getModulos());
			getListasModulos().setListaOriginal((List<Modulo>)GenericUtils.restaConjuntosOrdenada(getAllModulos(),getPerfilActual().getModulos()));
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombrePerfil().setText("");
		getListasModulos().setListaDestino(new ArrayList<Modulo>());
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		cargarListaModulos();
		llenarTablaModulos();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	private void llenarTablaModulos() {
		getTablaModulos().removeAllRows();
		for(Modulo m : allModulosConAcciones) {
			Object[] row = new Object[2];
			row[0] = m.getNombre();
			row[1] = m;
			getTablaModulos().addRow(row);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getPanDetalle(), estado);
	}

	private CLJTextField getTxtNombrePerfil() {
		if(txtNombrePerfil == null){
			txtNombrePerfil = new CLJTextField(MAX_LONGITUD_NOMBRE);
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

	private CLDobleLista getListasModulos() {
		if(listasModulos == null){
			listasModulos = new CLDobleLista("Acciones disponibles", "Acciones agregadas",CLDobleLista.VERTICAL_LAYOUT);
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
		return allModulosConAcciones;
	}

	public void setAllModulosConAcciones(List<Modulo> allModulosConAcciones) {
		this.allModulosConAcciones = allModulosConAcciones;
	}

}
