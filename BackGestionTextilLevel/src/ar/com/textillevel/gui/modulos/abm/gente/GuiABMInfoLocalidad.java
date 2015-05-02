package ar.com.textillevel.gui.modulos.abm.gente;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;


public class GuiABMInfoLocalidad extends GuiABMListaTemplate{

	private static final long serialVersionUID = 9103529670458826165L;

	private static final int MAX_LONGITUD_LOCALIDAD = 50;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;
	
	private CLJTextField txtNombreLocalidad;
	private CLJNumericTextField txtCodArea;
	private CLJNumericTextField txtCodigoPostal;
	
	private InfoLocalidadFacadeRemote infoLocalidadFacade;
	private InfoLocalidad infolocalidadActual;
	
	public GuiABMInfoLocalidad(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Localidades");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información de localidad", getTabDetalle());		
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
			panDetalle.add(new JLabel("Localidad:"), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombreLocalidad(),createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Cod. Postal:"), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtCodigoPostal(),  createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("Cod. Area: "), createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtCodArea(),  createGridBagConstraints(1, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panDetalle;
	}
	
	private GridBagConstraints createGridBagConstraints(int x, int y, int posicion, int fill, Insets insets, int cantX, int cantY, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = posicion;
		gbc.fill = fill;
		gbc.insets = insets;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = cantX;
		gbc.gridheight = cantY;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		return gbc;
	}
	
	private CLJTextField getTxtNombreLocalidad() {
		if(txtNombreLocalidad == null){
			txtNombreLocalidad = new CLJTextField(MAX_LONGITUD_LOCALIDAD);
		}
		return txtNombreLocalidad;
	}

	private CLJNumericTextField getTxtCodArea() {
		if(txtCodArea == null){
			txtCodArea = new CLJNumericTextField();
		}
		return txtCodArea;
	}

	private CLJNumericTextField getTxtCodigoPostal() {
		if(txtCodigoPostal == null){
			txtCodigoPostal = new CLJNumericTextField();
		}
		return txtCodigoPostal;
	}

	@Override
	public void cargarLista() {
		List<InfoLocalidad> infoLocalidadList = getInfoLocalidadFacade().getAllInfoLocalidad();
		lista.removeAll();
		for(InfoLocalidad c : infoLocalidadList) {
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setInfolocalidadActual(new InfoLocalidad());
		getTxtNombreLocalidad().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el cliente seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getInfoLocalidadFacade().remove(getInfolocalidadActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			InfoLocalidad infoLocalidadRefresh = getInfoLocalidadFacade().guardarInfoLocalidad(getInfolocalidadActual());
			lista.setSelectedValue(infoLocalidadRefresh, true);
			return true;
		}
		return false;
	}
	
	private boolean validar() {
		if(getTxtNombreLocalidad().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la localidad", this.getTitle());
			return false;
		}
		
		if(getTxtCodigoPostal().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el código postal", this.getTitle());
			return false;
		}
		
		if(getTxtCodArea().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el código de área", this.getTitle());
			return false;
		}
		
		if(getTxtCodArea().getText().equalsIgnoreCase(getTxtCodigoPostal().getText())){
			CLJOptionPane.showErrorMessage(this, "El código postal no puede conincidir con el código de área", this.getTitle());
			return false;
		}
		
		return true;
	}
	
	private void capturarSetearDatos() {
		getInfolocalidadActual().setCodigoArea(getTxtCodArea().getValueWithNull());
		getInfolocalidadActual().setCodigoPostal(getTxtCodigoPostal().getValueWithNull());
		getInfolocalidadActual().setNombreLocalidad(getTxtNombreLocalidad().getText().trim().toUpperCase());

	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombreLocalidad().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un cliente", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setInfolocalidadActual((InfoLocalidad)lista.getSelectedValue());
		limpiarDatos();
		if(getInfolocalidadActual() != null) {
			getTxtCodArea().setValue(getInfolocalidadActual().getCodigoArea().longValue());
			getTxtCodigoPostal().setValue(getInfolocalidadActual().getCodigoPostal().longValue());
			getTxtNombreLocalidad().setText(getInfolocalidadActual().getNombreLocalidad());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtCodArea().setText("");
		getTxtCodigoPostal().setText("");
		getTxtNombreLocalidad().setText("");
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
	
	public InfoLocalidadFacadeRemote getInfoLocalidadFacade() {
		if(infoLocalidadFacade == null){
			infoLocalidadFacade = GTLBeanFactory.getInstance().getBean2(InfoLocalidadFacadeRemote.class);
		}
		return infoLocalidadFacade;
	}

	
	public InfoLocalidad getInfolocalidadActual() {
		return infolocalidadActual;
	}

	
	public void setInfolocalidadActual(InfoLocalidad infolocalidadActual) {
		this.infolocalidadActual = infolocalidadActual;
	}
	

}
