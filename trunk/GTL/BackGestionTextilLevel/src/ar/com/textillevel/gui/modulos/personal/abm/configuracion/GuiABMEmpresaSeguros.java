package ar.com.textillevel.gui.modulos.personal.abm.configuracion;

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
import ar.com.textillevel.modulos.personal.entidades.commons.EmpresaSeguros;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpresaSegurosFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;


public class GuiABMEmpresaSeguros extends GuiABMListaTemplate {

	private static final long serialVersionUID = 1969434697882194662L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private CLJTextField txtNombre;

	private EmpresaSegurosFacadeRemote empresaSegurosFacade;
	private EmpresaSeguros empresaSegurosActual;

	public GuiABMEmpresaSeguros(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Empresas de seguro");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información de empresa de seguros", getTabDetalle());		
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
		List<EmpresaSeguros> empresaSegurosList = getEmpresaSegurosFacade().getAllOrderByName();
		lista.removeAll();
		for(EmpresaSeguros e : empresaSegurosList) {
			lista.addItem(e);
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
		setEmpresaSegurosActual(new EmpresaSeguros());
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la empresa de seguros seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getEmpresaSegurosFacade().remove(getEmpresaSegurosActual());
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
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar una empresa de seguros", "Error");
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
			EmpresaSeguros empresa = getEmpresaSegurosFacade().save(getEmpresaSegurosActual());
			lista.setSelectedValue(empresa, true);
			return true;
		}
		return false;
	}
	
	private boolean validar() {
		if(getTxtNombre().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el nombre de la empresa de seguros.", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getEmpresaSegurosActual().setNombre(getTxtNombre().getText().toUpperCase());
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
		setEmpresaSegurosActual((EmpresaSeguros)lista.getSelectedValue());
		limpiarDatos();
		if(getEmpresaSegurosActual() != null) {
			getTxtNombre().setText(getEmpresaSegurosActual().getNombre());
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		// TODO Auto-generated method stub

	}

	public EmpresaSeguros getEmpresaSegurosActual() {
		return empresaSegurosActual;
	}

	public void setEmpresaSegurosActual(EmpresaSeguros empresaSegurosActual) {
		this.empresaSegurosActual = empresaSegurosActual;
	}

	public CLJTextField getTxtNombre() {
		if(txtNombre == null){
			txtNombre = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	public EmpresaSegurosFacadeRemote getEmpresaSegurosFacade() {
		if(empresaSegurosFacade == null){
			empresaSegurosFacade = GTLPersonalBeanFactory.getInstance().getBean2(EmpresaSegurosFacadeRemote.class);
		}
		return empresaSegurosFacade;
	}
}
