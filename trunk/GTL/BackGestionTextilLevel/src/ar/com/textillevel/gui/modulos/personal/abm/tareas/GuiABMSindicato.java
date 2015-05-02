package ar.com.textillevel.gui.modulos.personal.abm.tareas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ETipoCobro;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ObraSocial;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.ObraSocialFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMSindicato extends GuiABMListaTemplate {

	private static final long serialVersionUID = 6151880574292501454L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private CLJTextField txtNombre;
	private JComboBox cmbTipoCobro;
	private JComboBox cmbObraSocial;

	private SindicatoFacadeRemote sindicatoFacade;
	private ObraSocialFacadeRemote obraSocialFacade;
	private Sindicato sindicatoActual;

	public GuiABMSindicato(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Sindicatos");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información del Sindicato", getTabDetalle());
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
			panDetalle.add(new JLabel("Tipo de Cobro: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoCobro(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Obra Social: "), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbObraSocial(),  GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panDetalle;
	}
	
	@Override
	public void cargarLista() {
		List<Sindicato> sindicatoList = getSindicatoFacade().getAllOrderByName();
		lista.removeAll();
		for(Sindicato s : sindicatoList) {
			lista.addItem(s);
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
		setSindicatoActual(new Sindicato());
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el Sindicato seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getSindicatoFacade().remove(getSindicatoActual());
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
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un sindicato", "Error");
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
			Sindicato sindicato = getSindicatoFacade().save(getSindicatoActual());
			lista.setSelectedValue(sindicato, true);
			return true;
		}
		return false;
	}
	
	private boolean validar() {
		if(getTxtNombre().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el nombre del Sindicato.", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getCmbTipoCobro().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un tipo de cobro.", "Advertencia");
			return false;
		}
		if(getCmbObraSocial().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar una obra social.", "Advertencia");
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getSindicatoActual().setNombre(getTxtNombre().getText().toUpperCase());
		getSindicatoActual().setTipoCobro((ETipoCobro)getCmbTipoCobro().getSelectedItem());
		getSindicatoActual().setObraSocial((ObraSocial)getCmbObraSocial().getSelectedItem());
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	@Override
	public void limpiarDatos() {
		getTxtNombre().setText("");
		getCmbTipoCobro().setSelectedIndex(-1);
		getCmbObraSocial().setSelectedIndex(-1);
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setSindicatoActual((Sindicato)lista.getSelectedValue());
		limpiarDatos();
		if(getSindicatoActual() != null) {
			getTxtNombre().setText(getSindicatoActual().getNombre());
			getCmbTipoCobro().setSelectedItem(getSindicatoActual().getTipoCobro());
			getCmbObraSocial().setSelectedItem(getSindicatoActual().getObraSocial());
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	public Sindicato getSindicatoActual() {
		return sindicatoActual;
	}

	public void setSindicatoActual(Sindicato sindicato) {
		this.sindicatoActual = sindicato;
	}

	private CLJTextField getTxtNombre() {
		if(txtNombre == null){
			txtNombre = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	private JComboBox getCmbTipoCobro() {
		if(cmbTipoCobro == null) {
			List<ETipoCobro> tipoCobroList = new ArrayList<ETipoCobro>();
			tipoCobroList.add(ETipoCobro.MENSUAL);
			tipoCobroList.add(ETipoCobro.QUINCENAL);
			cmbTipoCobro = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoCobro, tipoCobroList, true);
		}
		return cmbTipoCobro;
	}

	private JComboBox getCmbObraSocial() {
		if(cmbObraSocial == null) {
			cmbObraSocial = new JComboBox();
			GuiUtil.llenarCombo(getCmbObraSocial(), getObraSocialFacade().getAllOrderByName(), true);
		}
		return cmbObraSocial;
	}

	private ObraSocialFacadeRemote getObraSocialFacade() {
		if(obraSocialFacade == null){
			obraSocialFacade = GTLPersonalBeanFactory.getInstance().getBean2(ObraSocialFacadeRemote.class);
		}
		return obraSocialFacade;
	}
	
	private SindicatoFacadeRemote getSindicatoFacade() {
		if(sindicatoFacade == null){
			sindicatoFacade = GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class);
		}
		return sindicatoFacade;
	}

}
