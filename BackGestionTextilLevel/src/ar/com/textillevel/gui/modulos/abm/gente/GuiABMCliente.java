package ar.com.textillevel.gui.modulos.abm.gente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.MaskFormatter;

import org.apache.commons.validator.EmailValidator;
import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextArea;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.VerticalFlowLayout;
import ar.clarin.fwjava.componentes.error.CLRuntimeException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.gui.util.panels.PanDatosDireccion;
import ar.com.textillevel.gui.util.panels.PanDatosTelefono;
import ar.com.textillevel.gui.util.panels.PanDireccionEvent;
import ar.com.textillevel.gui.util.panels.PanDireccionEventListener;
import ar.com.textillevel.util.GTLBeanFactory;

/**
 *	Clase ABM de Clientes. 
 */
public class GuiABMCliente extends GuiABMListaTemplate {

	private static final long serialVersionUID = 8012369007737291095L;
	
	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;
	private static final int MAX_LONGITUD_CONTACTO = 50;
	private static final int MAX_LONGITUD_SKYPE = 50;
	private static final int MAX_LONGITUD_OBSERVACIONES = 1000;
	private static final int MAX_LONGITUD_EMAIL = 1000;
	
	private List<Cliente> clienteList;

	private JPanel tabDetalle;
	private JPanel panDetalle;
	private PanDatosTelefono panTelefonoFijo;
	private PanDatosTelefono panCelular;
	private PanDatosTelefono panFax;
	private CLJTextField txtRazonSocial;
	private CLJTextField txtContacto;
	private CLJTextField txtEmail;
	private JComboBox cmbPosicionIva;
	private JComboBox cmbCondicionVenta;
	private CLJTextField txtSkype;
	private JFormattedTextField txtCUIT;
	private CLJNumericTextField txtNroCliente;
	private PanDatosDireccion panDatosDireccionFiscal;
	private PanDatosDireccion panDatosDireccionReal;
	private CLJTextArea txtObservaciones;
	private Cliente clienteActual;
	private List<InfoLocalidad> infoLocalidadList;
	private ClienteFacadeRemote clienteFacadeRemote;
	private InfoLocalidadFacadeRemote infoLocalidadRemote;
	private JScrollPane jsp;
	private JPanel panDatos;

	public GuiABMCliente(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Clientes");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Datos del cliente", getTabDetalle());		
	}

	private JPanel getTabDetalle() {
		if(tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.CENTER);
			JLabel lblObligatorios = new JLabel("* Campos obligatorios.");
			lblObligatorios.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblObligatorios.setForeground(Color.RED);
			tabDetalle.add(lblObligatorios, BorderLayout.SOUTH);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.add(getJsp(), BorderLayout.CENTER);
		}
		return panDetalle;
	}

	private JComboBox getCmbPosicionIva() {
		if(cmbPosicionIva == null) {
			cmbPosicionIva = new JComboBox();
			GuiUtil.llenarCombo(cmbPosicionIva, Arrays.asList(EPosicionIVA.values()), true);
			cmbPosicionIva.setSelectedIndex(-1);
		}
		return cmbPosicionIva;
	}

	private CLJNumericTextField getTxtNroCliente() {
		if(txtNroCliente == null) {
			txtNroCliente = new CLJNumericTextField();
		}
		return txtNroCliente;
	}

	@Override
	public void cargarLista() {
		if(clienteList == null){
			clienteList = getClienteFacade().getAllOrderByName();
		}
		ordenarLista();
		lista.clear();
		for(Cliente c : clienteList) {
			lista.addItem(c);
		}
	}

	private void ordenarLista() {
		if(getCmbMaestro().getSelectedItem().equals("POR NOMBRE DE CLIENTE")){
			Collections.sort(clienteList, new Comparator<Cliente>() {
				public int compare(Cliente o1, Cliente o2) {
					return o1.getRazonSocial().compareTo(o2.getRazonSocial());
				}
			});
		}else{
			Collections.sort(clienteList, new Comparator<Cliente>() {
				public int compare(Cliente o1, Cliente o2) {
					return o1.getNroCliente().compareTo(o2.getNroCliente());
				}
			});
		}
	}

	private ClienteFacadeRemote getClienteFacade() {
		if(clienteFacadeRemote == null) {
			clienteFacadeRemote = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacadeRemote;
	}
	
	@Override
	public void botonAgregarPresionado(int arg0) {
		limpiarDatos();
		setClienteActual(new Cliente());
		getTxtNroCliente().requestFocus();
		getPanDatosDireccionReal().setDireccion(getClienteActual().getDireccionReal(), getInfoLocalidadList());		
		getPanDatosDireccionFiscal().setDireccion(getClienteActual().getDireccionFiscal(), getInfoLocalidadList());		
	}

	@Override
	public void botonCancelarPresionado(int arg0) {
		habilitarTabSeleccionado(true);
		setModoEdicion(false);
		if(getClienteActual().getId() != null && getClienteActual().getId() >0) {
			clienteList = getClienteFacade().getAllOrderByName();
			setClienteActual(buscarClienteEnLista(getClienteActual().getId()));
			lista.setSelectedValue(getClienteActual(), true);
			setDataGUIFromClienteActual();
		}
	}

	private Cliente buscarClienteEnLista(Integer idCliente) {
		for(Cliente c : clienteList) {
			if(c.getId().equals(idCliente)) {
				return c;
			}
		}
		return null;
	}

	@Override
	public void botonEliminarPresionado(int arg0) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(GuiABMCliente.this, "¿Está seguro que desea eliminar el cliente seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				try{
					getClienteFacade().remove(getClienteActual());
					itemSelectorSeleccionado(-1);
				}catch(CLRuntimeException cle){
					BossError.gestionarError(cle);
				}
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int arg0) {
		if(validar()) {
			capturarSetearDatos();
			try {
				Cliente clienteRefresh = getClienteFacade().save(getClienteActual());
				CLJOptionPane.showInformationMessage(this, "Los datos del cliente se han guardado con éxito", "Administrar Clientes");
				clienteList = getClienteFacade().getAllOrderByName();
				lista.setSelectedValue(clienteRefresh, true);
				return true;
			} catch (ValidacionException e) {
				CLJOptionPane.showErrorMessage(GuiABMCliente.this, StringW.wordWrap(e.getMensajeError()), "Error");
				getTxtCUIT().requestFocus();
			}
		}
		return false;
	}

	private void capturarSetearDatos() {
		getClienteActual().setNroCliente(getTxtNroCliente().getValue());
		getClienteActual().setRazonSocial(getTxtRazonSocial().getText().trim().toUpperCase());
		getClienteActual().setDireccionFiscal(getPanDatosDireccionFiscal().getInfoDireccion());
		getClienteActual().setDireccionReal(getPanDatosDireccionReal().getInfoDireccion());
		getClienteActual().setCuit(getTxtCUIT().getText().trim());
		getClienteActual().setContacto(getTxtContacto().getText().trim().toUpperCase());
		getClienteActual().setEmail(getTxtEmail().getText().trim());
		getClienteActual().setSkype(getTxtSkype().getText().trim());
		getClienteActual().setObservaciones(getTxtObservaciones().getText().trim().toUpperCase());
		getClienteActual().setTelefono(getPanTelefonoFijo().getDatos());
		getClienteActual().setCelular(getPanCelular().getDatos());
		getClienteActual().setFax(getPanFax().getDatos());
		getClienteActual().setPosicionIva((EPosicionIVA)getCmbPosicionIva().getSelectedItem());
		getClienteActual().setCondicionVenta((CondicionDeVenta)getCmbCondicionVenta().getSelectedItem());
	}

	private boolean validar() {
		Integer valTxtNroCliente = getTxtNroCliente().getText().trim().equalsIgnoreCase("") ? null : Integer.valueOf(getTxtNroCliente().getText().trim());
		if(valTxtNroCliente != null) {
			boolean existeNroCliente = getClienteFacade().existeNroCliente(getClienteActual().getId(), valTxtNroCliente);
			if(existeNroCliente) {
				CLJOptionPane.showErrorMessage(GuiABMCliente.this, StringW.wordWrap("El número de cliente " + valTxtNroCliente + " ya está asignado a otro cliente."), "Error");
				getTxtNroCliente().requestFocus();
				return false;
			}
		}
		String textoRazonSocial = getTxtRazonSocial().getText().trim();
		if(StringUtil.isNullOrEmpty(textoRazonSocial)) {
			CLJOptionPane.showErrorMessage(GuiABMCliente.this, "Falta completar el campo 'RAZON SOCIAL'", "Advertencia");
			getTxtRazonSocial().requestFocus();
			return false;
		}
		if(getTxtCUIT().getText().trim().length() < 13) {
			CLJOptionPane.showErrorMessage(this, "Debe completar el campo 'CUIT'.","Advertencia");
			getTxtCUIT().requestFocus();
			return false;
		}
		if(getCmbPosicionIva().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar el campo 'CONDICION DE IVA'.","Advertencia");
			return false;
		}
		
		if(getCmbCondicionVenta().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar el campo 'CONDICION DE VENTA'.","Advertencia");
			return false;
		}
		
		if(StringUtil.isNullOrEmpty(getPanCelular().getDatos()) &&  StringUtil.isNullOrEmpty(getPanTelefonoFijo().getDatos()) && StringUtil.isNullOrEmpty(getPanFax().getDatos())){
			CLJOptionPane.showErrorMessage(GuiABMCliente.this, "Debe ingresar al menos un teléfono", "Advertencia");
			return false;
		}
		String textoErrorDirFiscal = getPanDatosDireccionFiscal().validar();
		if(textoErrorDirFiscal != null) {
			CLJOptionPane.showErrorMessage(GuiABMCliente.this, StringW.wordWrap(textoErrorDirFiscal), "Advertencia");
			return false;
		}
		String textoErrorDirReal = getPanDatosDireccionReal().validar();
		if(textoErrorDirReal != null) {
			CLJOptionPane.showErrorMessage(GuiABMCliente.this, StringW.wordWrap(textoErrorDirReal), "Advertencia");
			return false;
		}
		String textoErrorTelefono = getPanTelefonoFijo().validar();
		if(textoErrorTelefono != null) {
			CLJOptionPane.showErrorMessage(GuiABMCliente.this, StringW.wordWrap(textoErrorTelefono), "Advertencia");
			return false;
		}
		String textoErrorCelular = getPanCelular().validar();
		if(textoErrorCelular != null) {
			CLJOptionPane.showErrorMessage(GuiABMCliente.this, StringW.wordWrap(textoErrorCelular), "Advertencia");
			return false;
		}
		String textoErrorFax = getPanFax().validar();
		if(textoErrorFax != null) {
			CLJOptionPane.showErrorMessage(GuiABMCliente.this, StringW.wordWrap(textoErrorFax), "Advertencia");
			return false;
		}
		String strMail = getTxtEmail().getText();
		if(!StringUtil.isNullOrEmpty(strMail) && !EmailValidator.getInstance().isValid(strMail.trim())) {
			CLJOptionPane.showErrorMessage(GuiABMCliente.this, "El EMAIL ingresado no es válido", "Advertencia");
			getTxtEmail().requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNroCliente().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un cliente", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		cargarLista();
	}

	@Override
	public void itemSelectorSeleccionado(int arg0) {
		limpiarDatos();
		setClienteActual((Cliente)lista.getSelectedValue());
		if(getClienteActual() != null) {
			setDataGUIFromClienteActual();
		}
	}

	private void setDataGUIFromClienteActual() {
		getTxtNroCliente().setValue(getClienteActual().getNroCliente().longValue());
		getTxtRazonSocial().setText(getClienteActual().getRazonSocial());
		getPanDatosDireccionFiscal().setDireccion(getClienteActual().getDireccionFiscal(), getInfoLocalidadList());
		getPanDatosDireccionReal().setDireccion(getClienteActual().getDireccionReal(), getInfoLocalidadList());
		getTxtCUIT().setText(getClienteActual().getCuit());
		getTxtContacto().setText(getClienteActual().getContacto());
		getTxtObservaciones().setText(getClienteActual().getObservaciones());
		getTxtEmail().setText(getClienteActual().getEmail());
		getTxtSkype().setText(getClienteActual().getSkype());
		getPanCelular().setFullDatos(getClienteActual().getCelular());
		getPanTelefonoFijo().setFullDatos(getClienteActual().getTelefono());
		getPanFax().setFullDatos(getClienteActual().getFax());
		getCmbPosicionIva().setSelectedItem(getClienteActual().getPosicionIva());
		getCmbCondicionVenta().setSelectedItem(getClienteActual().getCondicionVenta());
	}

	@Override
	public void limpiarDatos() {
		getTxtNroCliente().setValue(null);
		getTxtRazonSocial().setText("");
		getPanCelular().limpiarDatos();
		getPanTelefonoFijo().limpiarDatos();
		getPanFax().limpiarDatos();
		getTxtCUIT().setText("");
		getTxtContacto().setText("");
		getTxtObservaciones().setText("");
		getTxtEmail().setText("");
		getTxtSkype().setText("");
		getPanDatosDireccionFiscal().limpiarDatos();
		getPanDatosDireccionReal().limpiarDatos();
		getCmbPosicionIva().setSelectedIndex(-1);
		getCmbCondicionVenta().setSelectedIndex(0);
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicionTemplate(false);
		habilitarTabSeleccionado(true);
		setModoEdicion(false);
		List<String> ordenamiento = new ArrayList<String>();
		ordenamiento.add("POR NOMBRE DE CLIENTE");
		ordenamiento.add("POR NUMERO DE CLIENTE");
		setContenidoComboMaestro(ordenamiento, "Ordenar por: ");
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getPanDatos(), estado);
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

	private CLJTextField getTxtContacto() {
		if(txtContacto == null) {
			txtContacto = new CLJTextField(MAX_LONGITUD_CONTACTO);
			txtContacto.setPreferredSize(new Dimension(150, 20));
		}
		return txtContacto;
	}

	private CLJTextField getTxtRazonSocial() {
		if(txtRazonSocial == null) {
			txtRazonSocial = new CLJTextField(MAX_LONGITUD_RAZ_SOCIAL);
		}
		return txtRazonSocial;
	}

	private JPanel getPanelOtrosDatos(){
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP,0,0));
		panel.setBorder(BorderFactory.createTitledBorder("OTROS DATOS"));

		JPanel panelArriba = new JPanel();
		panelArriba.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelArriba.add(new JLabel("CONTACTO: "));
		panelArriba.add(getTxtContacto());

		JPanel panelAbajo = new JPanel();
		panelAbajo.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelAbajo.add(new JLabel("OBSERVACIONES: "));
		JScrollPane jsp = new JScrollPane(getTxtObservaciones(),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(500, 80));
		panelAbajo.add(jsp);

		panel.add(panelArriba);
		panel.add(panelAbajo);

		return panel;
	}

	
	private JPanel getPanTelefonos() {
		JPanel panTelefonos = new JPanel();
		panTelefonos.setLayout(new GridBagLayout());
		panTelefonos.setBorder(BorderFactory.createTitledBorder("DATOS DEL CONTACTO"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panTelefonos.add(getPanTelefonoFijo(), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panTelefonos.add(getPanCelular(), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JPanel pnlEmail = new JPanel();
		pnlEmail.setBorder(BorderFactory.createTitledBorder("INTERNET"));
		pnlEmail.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
		pnlEmail.add(new JLabel("EMAIL:"));
		pnlEmail.add(getTxtEmail());
		pnlEmail.add(new JLabel("SKYPE:"));
		pnlEmail.add(getTxtSkype());
		panTelefonos.add(pnlEmail, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panTelefonos.add(getPanFax(), gbc);
		return panTelefonos;
	}

	private PanDatosTelefono getPanFax() {
		if(panFax == null) {
			panFax = new PanDatosTelefono("FAX", "-");
		}
		return panFax;
	}

	private PanDatosTelefono getPanTelefonoFijo() {
		if(panTelefonoFijo == null) {
			panTelefonoFijo = new PanDatosTelefono("TELEFONO", "-");
		}
		return panTelefonoFijo;
	}

	private PanDatosTelefono getPanCelular() {
		if(panCelular == null) {
			panCelular = new PanDatosTelefono("CELULAR", "-");
		}
		return panCelular;
	}

	private JFormattedTextField getTxtCUIT() {
		if(txtCUIT == null){
			try {
				txtCUIT = new JFormattedTextField(new MaskFormatter("##-########-#"));
				txtCUIT.setFocusLostBehavior(JFormattedTextField.PERSIST);
				txtCUIT.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
						try {
							txtCUIT.commitEdit();
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtCUIT;
	}

	private CLJTextArea getTxtObservaciones() {
		if(txtObservaciones == null) {
			txtObservaciones = new CLJTextArea(MAX_LONGITUD_OBSERVACIONES);
			txtObservaciones.setPreferredSize(new Dimension(510, 50));
			txtObservaciones.setLineWrap(true);
			txtObservaciones.setBorder(BorderFactory.createLineBorder(Color.BLUE.darker()));
		}
		return txtObservaciones;
	}

	
	private Cliente getClienteActual() {
		return clienteActual;
	}

	private void setClienteActual(Cliente clienteActual) {
		this.clienteActual = clienteActual;
	}

	public void setModoEdicionTemplate(boolean estado) {
		super.setModoEdicionTemplate(estado);
		if(!estado && lista.getSelectedIndex() < 0) {
			getBtnEliminar().setEnabled(false);
			getBtnModificar().setEnabled(false);
		}
	}

	private PanDatosDireccion getPanDatosDireccionFiscal() {
		if(panDatosDireccionFiscal == null) {
			panDatosDireccionFiscal = new PanDatosDireccion(GuiABMCliente.this.getFrame(), "DIRECCION FISCAL");
			panDatosDireccionFiscal.addPanDireccionListener(new PanDireccionEventListener() {

				public void newInfoLocalidadAdded(PanDireccionEvent evt) {
					InfoLocalidad infoLocalidadSelected = evt.getInfoLocalidad();
					infoLocalidadList.clear();
					infoLocalidadList.addAll(getInfoLocalidadRemote().getAllInfoLocalidad());
					InfoLocalidad infoLocalidadOTRO = new InfoLocalidad();
					infoLocalidadOTRO.setId(PanDatosDireccion.OTRO);
					infoLocalidadOTRO.setNombreLocalidad("OTRO");
					infoLocalidadList.add(infoLocalidadOTRO);
					getPanDatosDireccionFiscal().seleccionarLocalidad(infoLocalidadSelected);
					getPanDatosDireccionReal().seleccionarLocalidad(getPanDatosDireccionReal().getInfoDireccion().getLocalidad());
				}

				public void newInfoLocalidadSelected(PanDireccionEvent evt) {
				}

			});
			
		}
		return panDatosDireccionFiscal;
	}

	private PanDatosDireccion getPanDatosDireccionReal() {
		if(panDatosDireccionReal == null) {
			panDatosDireccionReal = new PanDatosDireccion(GuiABMCliente.this.getFrame(), "DIRECCION REAL");
			panDatosDireccionReal.addPanDireccionListener(new PanDireccionEventListener() {

				public void newInfoLocalidadAdded(PanDireccionEvent evt) {
					InfoLocalidad infoLocalidadSelected = evt.getInfoLocalidad();
					infoLocalidadList.clear();
					infoLocalidadList.addAll(getInfoLocalidadRemote().getAllInfoLocalidad());
					InfoLocalidad infoLocalidadOTRO = new InfoLocalidad();
					infoLocalidadOTRO.setId(PanDatosDireccion.OTRO);
					infoLocalidadOTRO.setNombreLocalidad("OTRO");
					infoLocalidadList.add(infoLocalidadOTRO);
					getPanDatosDireccionReal().seleccionarLocalidad(infoLocalidadSelected);
					getPanDatosDireccionFiscal().seleccionarLocalidad(getPanDatosDireccionFiscal().getInfoDireccion().getLocalidad());
				}

				public void newInfoLocalidadSelected(PanDireccionEvent evt) {
					InfoLocalidad infoLocalidadSelected = evt.getInfoLocalidad();
					getPanTelefonoFijo().setCodArea(infoLocalidadSelected.getCodigoArea());
				}

			});

		}
		return panDatosDireccionReal;
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

	private CLJTextField getTxtEmail() {
		if(txtEmail == null) {
			txtEmail = new CLJTextField(MAX_LONGITUD_EMAIL);
			txtEmail.setPreferredSize(new Dimension(240, 20));			
		}
		return txtEmail;
	}

	private CLJTextField getTxtSkype() {
		if(txtSkype == null) {
			txtSkype = new CLJTextField(MAX_LONGITUD_SKYPE);
			txtSkype.setPreferredSize(new Dimension(240, 20));
		}
		return txtSkype;
	}
	
	public JScrollPane getJsp() {
		if(jsp == null){
			jsp = new JScrollPane(getPanDatos(),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(650, 450));
		}
		return jsp;
	}
	
	public JPanel getPanDatos() {
		if(panDatos == null){
			panDatos = new JPanel();
			panDatos.setLayout(new GridBagLayout());
			panDatos.add(new JLabel("* NRO. DE CLIENTE:"), createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtNroCliente(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDatos.add(new JLabel("* RAZON SOCIAL:"), createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtRazonSocial(), createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(new JLabel("* C.U.I.T.:"), createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtCUIT(), createGridBagConstraints(1, 2,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* CONDICION DE IVA:"), createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getCmbPosicionIva(), createGridBagConstraints(1, 3,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* CONDICION DE VENTA: "), createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getCmbCondicionVenta(), createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panDatos.add(getPanDatosDireccionFiscal(), createGridBagConstraints(0, 6,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDatos.add(getPanDatosDireccionReal(), createGridBagConstraints(0, 7,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDatos.add(getPanTelefonos(), createGridBagConstraints(0, 8,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 1, 0));
			panDatos.add(getPanelOtrosDatos(), createGridBagConstraints(0, 9,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0, 0, 1));
		}
		return panDatos;
	}
	
	private JComboBox getCmbCondicionVenta() {
		if(cmbCondicionVenta == null){
			cmbCondicionVenta = new JComboBox();
			GuiUtil.llenarCombo(cmbCondicionVenta, GTLBeanFactory.getInstance().getBean2(CondicionDeVentaFacadeRemote.class).getAllOrderByName(), true);
		}
		return cmbCondicionVenta;
	}
}