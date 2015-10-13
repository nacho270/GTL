package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.gente.Provincia;
import ar.com.textillevel.facade.api.remote.ImpuestoItemProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProvinciaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMImpuestos extends GuiABMListaTemplate {

	private static final long serialVersionUID = -5488581952862203736L;

	private static final int MAX_LONGITUD_IMPUESTO = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private FWJTextField txtNombreImpuesto;
	private FWJTextField txtPorcDescuento;
	private JComboBox cmbTipoImpuesto;
	private JComboBox cmbProvincia;

	private ImpuestoItemProveedorFacadeRemote impuestoFacade;
	private ImpuestoItemProveedor contenedorActual;


	public GuiABMImpuestos(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Impuestos");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información del impuesto", getTabDetalle());		
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
			panDetalle.add(getTxtNombreImpuesto(),createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("% de Descuento:"), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtPorcDescuento(),  createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("Tipo de Impuesto:"), createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoImpuesto(),  createGridBagConstraints(1, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("Provincia"),  createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbProvincia(),  createGridBagConstraints(1, 3,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panDetalle;
	}

	private JComboBox getCmbTipoImpuesto() {
		if(cmbTipoImpuesto == null) {
			cmbTipoImpuesto = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoImpuesto, Arrays.asList(ETipoImpuesto.values()), true);
			cmbTipoImpuesto.setSelectedIndex(-1);
			cmbTipoImpuesto.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						ETipoImpuesto tipo = (ETipoImpuesto)cmbTipoImpuesto.getSelectedItem();
						getCmbProvincia().setEnabled(tipo == ETipoImpuesto.INGRESOS_BRUTOS);
					}
				}
			});
		}
		return cmbTipoImpuesto;
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
	
	private FWJTextField getTxtNombreImpuesto() {
		if(txtNombreImpuesto == null){
			txtNombreImpuesto = new FWJTextField(MAX_LONGITUD_IMPUESTO);
		}
		return txtNombreImpuesto;
	}

	private FWJTextField getTxtPorcDescuento() {
		if(txtPorcDescuento == null){
			txtPorcDescuento = new FWJTextField();
		}
		return txtPorcDescuento;
	}

	@Override
	public void cargarLista() {
		List<ImpuestoItemProveedor> impuestoList = getImpuestoFacade().getAllOrderByName();
		lista.removeAll();
		for(ImpuestoItemProveedor c : impuestoList) {
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setImpuestoActual(new ImpuestoItemProveedor());
		getTxtNombreImpuesto().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el impuesto seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getImpuestoFacade().remove(getImpuestoActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			ImpuestoItemProveedor impuesto = getImpuestoFacade().save(getImpuestoActual());
			lista.setSelectedValue(impuesto, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(getTxtNombreImpuesto().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el nombre del impuesto.", this.getTitle());
			getTxtNombreImpuesto().requestFocus();
			return false;
		}
		
		if(getTxtPorcDescuento().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el porcentaje de descuento del impuesto.", this.getTitle());
			getTxtPorcDescuento().requestFocus();
			return false;
		}
		
		if(getCmbTipoImpuesto().getSelectedIndex() == -1) {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar el tipo de impuesto.", this.getTitle());
			return false;
		}
		String monto = getTxtPorcDescuento().getText().replace(',', '.');
		if(!GenericUtils.esNumerico(monto)){
			FWJOptionPane.showErrorMessage(this, "El campo debe ser numérico", this.getTitle());
			return false;
		}
		Double porcDescuento = new Double(monto);
		Provincia selectedItemProvincia = (Provincia)getCmbProvincia().getSelectedItem();
		if(getImpuestoFacade().existsOtroImpuestoWithParams(getImpuestoActual().getId(), porcDescuento, (ETipoImpuesto)getCmbTipoImpuesto().getSelectedItem(),selectedItemProvincia)) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap("Ya existe otro impuesto con el mismo porcentaje de descuento"+(selectedItemProvincia!=null?", tipo de impuesto y provincia.":" y tipo de impuesto.")), this.getTitle());
			return false;
		}
		if(((ETipoImpuesto)getCmbTipoImpuesto().getSelectedItem())==ETipoImpuesto.INGRESOS_BRUTOS){
			if(getCmbProvincia().getSelectedIndex()<0){
				FWJOptionPane.showErrorMessage(this, StringW.wordWrap("Debe elegir la provincia para este impuesto."), this.getTitle());
				return false;
			}
		}
		return true;
	}

	private void capturarSetearDatos() {
		getImpuestoActual().setPorcDescuento(new Double(getTxtPorcDescuento().getText().replace(',', '.')));
		getImpuestoActual().setNombre(getTxtNombreImpuesto().getText().trim().toUpperCase());
		ETipoImpuesto tipoImpuesto = (ETipoImpuesto)getCmbTipoImpuesto().getSelectedItem();
		getImpuestoActual().setTipoImpuesto(tipoImpuesto);
		if(tipoImpuesto == ETipoImpuesto.INGRESOS_BRUTOS){
			getImpuestoActual().setProvincia((Provincia)getCmbProvincia().getSelectedItem());
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombreImpuesto().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un impuesto.", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setImpuestoActual((ImpuestoItemProveedor)lista.getSelectedValue());
		limpiarDatos();
		if(getImpuestoActual() != null) {
			getTxtPorcDescuento().setText(String.valueOf(getImpuestoActual().getPorcDescuento()));
			getTxtNombreImpuesto().setText(getImpuestoActual().getNombre());
			getCmbTipoImpuesto().setSelectedItem(getImpuestoActual().getTipoImpuesto());
			if(getImpuestoActual().getTipoImpuesto() == ETipoImpuesto.INGRESOS_BRUTOS){
				getCmbProvincia().setSelectedItem(getImpuestoActual().getProvincia());
			}else{
				getCmbProvincia().setSelectedIndex(-1);
			}
			getCmbProvincia().setEnabled(false);
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtPorcDescuento().setText("");
		getTxtNombreImpuesto().setText("");
		getCmbTipoImpuesto().setSelectedIndex(-1);
		getCmbProvincia().setSelectedIndex(0);
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
		if(getImpuestoActual() != null && getImpuestoActual().getTipoImpuesto() != ETipoImpuesto.INGRESOS_BRUTOS){
			getCmbProvincia().setEnabled(false);
			getCmbProvincia().setSelectedIndex(-1);
		}
	}

	public ImpuestoItemProveedorFacadeRemote getImpuestoFacade() {
		if(impuestoFacade == null){
			impuestoFacade = GTLBeanFactory.getInstance().getBean2(ImpuestoItemProveedorFacadeRemote.class);
		}
		return impuestoFacade;
	}

	private ImpuestoItemProveedor getImpuestoActual() {
		return contenedorActual;
	}

	private void setImpuestoActual(ImpuestoItemProveedor contenedorActual) {
		this.contenedorActual = contenedorActual;
	}
	
	private JComboBox getCmbProvincia() {
		if(cmbProvincia == null){
			cmbProvincia = new JComboBox();
			GuiUtil.llenarCombo(cmbProvincia, GTLBeanFactory.getInstance().getBean2(ProvinciaFacadeRemote.class).getAllOrderByName(), true);
		}
		return cmbProvincia;
	}
}