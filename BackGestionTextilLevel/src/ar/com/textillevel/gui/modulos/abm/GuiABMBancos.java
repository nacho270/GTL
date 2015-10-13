package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.gui.util.panels.PanDatosDireccion;
import ar.com.textillevel.gui.util.panels.PanDatosTelefono;
import ar.com.textillevel.gui.util.panels.PanDireccionEvent;
import ar.com.textillevel.gui.util.panels.PanDireccionEventListener;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMBancos extends GuiABMListaTemplate{

	private static final long serialVersionUID = 9213530685890726211L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;
	
	private FWJTextField txtNombreBanco;
	private FWJNumericTextField txtCodBanco;
	private PanDatosDireccion panDatosDireccion;
	private PanDatosTelefono panTelefono;

	private List<InfoLocalidad> infoLocalidadList;
	private InfoLocalidadFacadeRemote infoLocalidadRemote;

	private BancoFacadeRemote bancoFacade;
	private Banco bancoActual;
	
	public GuiABMBancos(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Bancos");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información del banco", getTabDetalle());		
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
			panDetalle.add(getTxtNombreBanco(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Código: "), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtCodBanco(),  createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanDatosDireccion(), createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDetalle.add(getPanTelefono(), createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
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
		List<Banco> bancoList = getBancoFacade().getAllOrderByName();
		lista.removeAll();
		for(Banco b : bancoList) {
			lista.addItem(b);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setBancoActual(new Banco());
		getTxtNombreBanco().requestFocus();
		getPanDatosDireccion().setDireccion(getBancoActual().getDireccion(), getInfoLocalidadList());		
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el banco seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getBancoFacade().remove(getBancoActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			Banco banco = getBancoFacade().save(getBancoActual());
			lista.setSelectedValue(banco, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(getTxtNombreBanco().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre del banco.", "Advertencia");
			getTxtNombreBanco().requestFocus();
			return false;
		}
		
		if(getTxtCodBanco().getValueWithNull() == null){
			FWJOptionPane.showErrorMessage(this, "Debe completar el código del banco.", "Advertencia");
			getTxtCodBanco().requestFocus();
			return false;
		}
		
		if(getBancoFacade().existeBanco(getTxtCodBanco().getValue())){
			FWJOptionPane.showErrorMessage(this, "El código de banco que ha ingresado, ya se esta utilizando. Por favor, ingrese un código distinto.", "Advertencia");
			getTxtCodBanco().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getBancoActual().setNombre(getTxtNombreBanco().getText().toUpperCase());
		getBancoActual().setCodigoBanco(getTxtCodBanco().getValueWithNull());
		getBancoActual().setTelefono(getPanTelefono().getDatos());
		getBancoActual().setDireccion(getPanDatosDireccion().getInfoDireccion());
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombreBanco().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un banco", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setBancoActual((Banco)lista.getSelectedValue());
		limpiarDatos();
		if(getBancoActual() != null) {
			getTxtNombreBanco().setText(getBancoActual().getNombre());
			getTxtCodBanco().setValue(getBancoActual().getCodigoBanco().longValue());
			getPanDatosDireccion().setDireccion(getBancoActual().getDireccion(), getInfoLocalidadList());
			getPanTelefono().setFullDatos(getBancoActual().getTelefono());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombreBanco().setText("");
		getTxtCodBanco().setText("");
		getPanTelefono().limpiarDatos();
		getPanDatosDireccion().limpiarDatos();
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

	
	public Banco getBancoActual() {
		return bancoActual;
	}
	
	public void setBancoActual(Banco bancoActual) {
		this.bancoActual = bancoActual;
	}
	
	public BancoFacadeRemote getBancoFacade() {
		if(bancoFacade == null){
			bancoFacade = GTLBeanFactory.getInstance().getBean2(BancoFacadeRemote.class);
		}
		return bancoFacade;
	}

	public FWJTextField getTxtNombreBanco() {
		if(txtNombreBanco == null){
			txtNombreBanco = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombreBanco;
	}
	
	private PanDatosTelefono getPanTelefono() {
		if(panTelefono == null) {
			panTelefono = new PanDatosTelefono("TELEFONO", "-");
		}
		return panTelefono;
	}
	
	private PanDatosDireccion getPanDatosDireccion() {
		if(panDatosDireccion == null) {
			panDatosDireccion = new PanDatosDireccion(GuiABMBancos.this.getFrame(), "DIRECCION");
			panDatosDireccion.addPanDireccionListener(new PanDireccionEventListener() {

				public void newInfoLocalidadAdded(PanDireccionEvent evt) {
					InfoLocalidad infoLocalidadSelected = evt.getInfoLocalidad();
					infoLocalidadList.clear();
					infoLocalidadList.addAll(getInfoLocalidadRemote().getAllInfoLocalidad());
					InfoLocalidad infoLocalidadOTRO = new InfoLocalidad();
					infoLocalidadOTRO.setId(PanDatosDireccion.OTRO);
					infoLocalidadOTRO.setNombreLocalidad("OTRO");
					infoLocalidadList.add(infoLocalidadOTRO);
					getPanDatosDireccion().seleccionarLocalidad(infoLocalidadSelected);
				}

				public void newInfoLocalidadSelected(PanDireccionEvent evt) {
					InfoLocalidad infoLocalidadSelected = evt.getInfoLocalidad();
					getPanTelefono().setCodArea(infoLocalidadSelected.getCodigoArea());
				}

			});

		}
		return panDatosDireccion;
	}
	
	private InfoLocalidadFacadeRemote getInfoLocalidadRemote() {
		if(infoLocalidadRemote == null) {
			infoLocalidadRemote = GTLBeanFactory.getInstance().getBean2(InfoLocalidadFacadeRemote.class);
		}
		return infoLocalidadRemote;
	}

	private List<InfoLocalidad> getInfoLocalidadList() {
		if(infoLocalidadList == null) {
			infoLocalidadList = getInfoLocalidadRemote().getAllInfoLocalidad();
			InfoLocalidad infoLocalidadOTRO = new InfoLocalidad();
			infoLocalidadOTRO.setId(-1);
			infoLocalidadOTRO.setNombreLocalidad("OTRO");
			infoLocalidadList.add(infoLocalidadOTRO);
		}
		return infoLocalidadList;
	}

	
	public FWJNumericTextField getTxtCodBanco() {
		if(txtCodBanco == null ){
			txtCodBanco = new FWJNumericTextField();
		}
		return txtCodBanco;
	}
}
