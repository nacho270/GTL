package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.Servicio;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.ProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ServicioFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMServicios extends GuiABMListaTemplate{

	private static final long serialVersionUID = -4193424482048808415L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;
	
	private FWJTextField txtNombreServicio;
	private JComboBox cmbProveedor;
	
	private ServicioFacadeRemote servicioFacade;
	private Servicio servicioActual;
	
	public GuiABMServicios(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Servicios");
		constructGui();
		setEstadoInicial();
	}
	

	private void constructGui() {
		panTabs.addTab("Información del servicio", getTabDetalle());		
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
			panDetalle.add(getTxtNombreServicio(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Proveedor: "), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbProveedor(),  createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
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
	
	@Override
	public void cargarLista() {
		List<Servicio> servicioList = getServicioFacade().getAllOrderByNameEager();
		lista.removeAll();
		for(Servicio c : servicioList) {
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setServicioActual(new Servicio());
		getTxtNombreServicio().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el servicio seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getServicioFacade().remove(getServicioActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			Servicio color = getServicioFacade().save(getServicioActual());
			lista.setSelectedValue(color, true);
			cargarLista();
			return true;
		}
		return false;
	}
	
	private boolean validar() {
		if(getTxtNombreServicio().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre del servicio.", "Advertencia");
			getTxtNombreServicio().requestFocus();
			return false;
		}
		Proveedor proveedor = (Proveedor)getCmbProveedor().getSelectedItem();
		if(getServicioFacade().existeServicio(getTxtNombreServicio().getText().trim(),proveedor,getServicioActual().getId())){
			FWJOptionPane.showErrorMessage(this, "Ya existe un servicio llamado '"+getTxtNombreServicio().getText().trim()+"' para el proveedor '"+proveedor.getRazonSocial()+"'.", "Advertencia");
			getTxtNombreServicio().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getServicioActual().setNombre(getTxtNombreServicio().getText().toUpperCase());
		getServicioActual().setProveedor((Proveedor)getCmbProveedor().getSelectedItem());
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombreServicio().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un servicio", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setServicioActual((Servicio)lista.getSelectedValue());
		limpiarDatos();
		if(getServicioActual() != null) {
			getTxtNombreServicio().setText(getServicioActual().getNombre());
			getCmbProveedor().setSelectedItem(getServicioActual().getProveedor());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombreServicio().setText("");
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

	public Servicio getServicioActual() {
		return servicioActual;
	}

	public void setServicioActual(Servicio servicioActual) {
		this.servicioActual = servicioActual;
	}

	public FWJTextField getTxtNombreServicio() {
		if(txtNombreServicio == null){
			txtNombreServicio = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombreServicio;
	}


	public ServicioFacadeRemote getServicioFacade() {
		if(servicioFacade == null){
			servicioFacade = GTLBeanFactory.getInstance().getBean2(ServicioFacadeRemote.class);
		}
		return servicioFacade;
	}
	
	public JComboBox getCmbProveedor() {
		if(cmbProveedor == null){
			cmbProveedor = new JComboBox();
			GuiUtil.llenarCombo(cmbProveedor, GTLBeanFactory.getInstance().getBean2(ProveedorFacadeRemote.class).getAllProveedoresOrderByName(), true);
		}
		return cmbProveedor;
	}
}
