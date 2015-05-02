package ar.com.textillevel.gui.modulos.abm.materiaprima;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.proveedor.Bidon;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;
import ar.com.textillevel.facade.api.remote.ContenedorMateriaPrimaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMContenedoresMatPrima extends GuiABMListaTemplate{

	private static final long serialVersionUID = -5488581952862203736L;

	private static final int MAX_LONGITUD_CONTENEDOR = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private CLJTextField txtNombreContenedor;
	private CLJNumericTextField txtCapacidad;

	private ContenedorMateriaPrimaFacadeRemote contenedorFacade;
	private ContenedorMateriaPrima contenedorActual;

	public GuiABMContenedoresMatPrima(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Contenedores");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información del Contenedor", getTabDetalle());		
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
			panDetalle.add(new JLabel("Nombre:"), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombreContenedor(),createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Capacidad:"), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtCapacidad(),  createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
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
	
	private CLJTextField getTxtNombreContenedor() {
		if(txtNombreContenedor == null){
			txtNombreContenedor = new CLJTextField(MAX_LONGITUD_CONTENEDOR);
		}
		return txtNombreContenedor;
	}

	private CLJNumericTextField getTxtCapacidad() {
		if(txtCapacidad == null){
			txtCapacidad = new CLJNumericTextField();
		}
		return txtCapacidad;
	}

	@Override
	public void cargarLista() {
		List<ContenedorMateriaPrima> infoLocalidadList = getContenedorFacade().getAllOrderByName();
		lista.removeAll();
		for(ContenedorMateriaPrima c : infoLocalidadList) {
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setContenedorActual(new Bidon());
		getTxtNombreContenedor().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el contenedor seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getContenedorFacade().remove(getContenedorActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			ContenedorMateriaPrima contenedorRefresh = getContenedorFacade().guardarContenedor(getContenedorActual());
			lista.setSelectedValue(contenedorRefresh, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(getTxtNombreContenedor().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el nombre del contenedor", this.getTitle());
			getTxtNombreContenedor().requestFocus();
			return false;
		}
		
		if(getTxtCapacidad().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la capacidad del contenedor", this.getTitle());
			getTxtCapacidad().requestFocus();
			return false;
		}
		
		return true;
	}

	private void capturarSetearDatos() {
		((Bidon)getContenedorActual()).setCapacidad(new BigDecimal(getTxtCapacidad().getValueWithNull()));
		getContenedorActual().setNombre(getTxtNombreContenedor().getText().trim().toUpperCase());
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombreContenedor().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un contenedor", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setContenedorActual((ContenedorMateriaPrima)lista.getSelectedValue());
		limpiarDatos();
		if(getContenedorActual() != null) {
			getTxtCapacidad().setValue(((Bidon)getContenedorActual()).getCapacidad().longValue());
			getTxtNombreContenedor().setText(getContenedorActual().getNombre());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtCapacidad().setText("");
		getTxtNombreContenedor().setText("");
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
	
	public ContenedorMateriaPrimaFacadeRemote getContenedorFacade() {
		if(contenedorFacade == null){
			contenedorFacade = GTLBeanFactory.getInstance().getBean2(ContenedorMateriaPrimaFacadeRemote.class);
		}
		return contenedorFacade;
	}

	public ContenedorMateriaPrima getContenedorActual() {
		return contenedorActual;
	}

	
	public void setContenedorActual(ContenedorMateriaPrima contenedorActual) {
		this.contenedorActual = contenedorActual;
	}
	

}
