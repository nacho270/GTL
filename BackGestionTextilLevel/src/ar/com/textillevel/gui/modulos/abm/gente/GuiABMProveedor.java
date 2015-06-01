package ar.com.textillevel.gui.modulos.abm.gente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.MaskFormatter;

import org.apache.commons.validator.EmailValidator;
import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextArea;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.VerticalFlowLayout;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.enums.ETipoRubro;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.gente.Provincia;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProvinciaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RubroPersonaFacadeRemote;
import ar.com.textillevel.gui.util.dialogs.JDialogCargarRubro;
import ar.com.textillevel.gui.util.panels.PanDatosDireccion;
import ar.com.textillevel.gui.util.panels.PanDatosTelefono;
import ar.com.textillevel.gui.util.panels.PanDireccionEvent;
import ar.com.textillevel.gui.util.panels.PanDireccionEventListener;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMProveedor extends GuiABMListaTemplate {

	private static final long serialVersionUID = -588517240659714474L;

	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;
	private static final int MAX_LONGITUD_CONTACTO = 50;
	private static final int MAX_LONGITUD_OBSERVACIONES = 1000;
	private static final int MAX_LONGITUD_NOMBRE_CORTO = 50;
	private static final int MAX_LONGITUD_EMAIL = 1000;
	private static final int MAX_LONGITUD_SKYPE = 50;
	private static final int MAX_LONGITUD_SITIO_WEB = 100;

	private JPanel tabDetalle, panDetalle;
	private CLJTextField txtRazonSocial, txtNombreCorto, txtContacto, txtIngBrutos;
	private JFormattedTextField txtCuit;
	private PanDatosTelefono panTelefonoFijo;
	private PanDatosTelefono panCelular;
	private PanDatosTelefono panFax;
	private PanDatosDireccion panDatosDireccionFiscal;
	private PanDatosDireccion panDatosDireccionReal;
	private JTextArea txtObservaciones;
	private CLJTextField txtEmail;
	private CLJTextField txtSkype;
	private JComboBox cmbRubro;
	private CLJTextField txtSitioWeb;
	private JComboBox cmbPosicionIva;
	private JComboBox cmbCondicionVenta;
	private JComboBox cmbProvincia;
	
	private Proveedor proveedorActual;

	private ProveedorFacadeRemote proveedorFacade;
	private List<InfoLocalidad> infoLocalidadList;
	private InfoLocalidadFacadeRemote infoLocalidadRemote;
	private RubroPersonaFacadeRemote rubroFacadeRemote;

	private JScrollPane jsp;
	private JPanel panDatos;

	private ProveedorFacadeRemote getProveedorFacade() {
		if (proveedorFacade == null) {
			proveedorFacade = GTLBeanFactory.getInstance().getBean2(ProveedorFacadeRemote.class);
		}
		return proveedorFacade;
	}

	public GuiABMProveedor(Integer idModulo) {
		super();
		setHijoCreado(true);
		createAndShowGUI();
		setEstadoInicial();
	}

	private void createAndShowGUI() {
		setTitle("Administrar Proveedores");
		panTabs.addTab("Datos de proveedor", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.CENTER);
			JLabel lblObligatorios = new JLabel("* Campos obligatorios");
			lblObligatorios.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblObligatorios.setForeground(Color.RED);
			tabDetalle.add(lblObligatorios, BorderLayout.SOUTH);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.add(getJsp(), BorderLayout.CENTER);
		}
		return panDetalle;
	}

	private JPanel getPanelOtrosDatos() {
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0));
		panel.setBorder(BorderFactory.createTitledBorder("OTROS DATOS"));

		JPanel panelArriba = new JPanel();
		panelArriba.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelArriba.add(new JLabel("CONTACTO: "));
		panelArriba.add(getTxtContacto());

		JPanel panelAbajo = new JPanel();
		panelAbajo.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelAbajo.add(new JLabel("OBSERVACIONES: "));
		JScrollPane jsp = new JScrollPane(getTxtObservaciones(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(500, 54));
		panelAbajo.add(jsp);

		panel.add(panelArriba);
		panel.add(panelAbajo);

		return panel;
	}

	private JPanel getPanTelefonos() {
		JPanel panTelefonos = new JPanel();
		panTelefonos.setLayout(new GridBagLayout());
		panTelefonos.setBorder(BorderFactory.createTitledBorder("Datos de contacto"));

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
		pnlEmail.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
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

	private CLJTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new CLJTextField(MAX_LONGITUD_RAZ_SOCIAL);
		}
		return txtRazonSocial;
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicionTemplate(false);
		habilitarTabSeleccionado(true);
		setModoEdicion(false);
		cargarLista();
		llenarComboRubro();
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void cargarLista() {
		List<Proveedor> provs = getProveedorFacade().getAllProveedoresOrderByName();
		lista.removeAll();
		for (Proveedor p : provs) {
			lista.addItem(p);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setProveedorActual(new Proveedor());
		getPanDatosDireccionReal().setDireccion(getProveedorActual().getDireccionReal(), getInfoLocalidadList());
		getPanDatosDireccionFiscal().setDireccion(getProveedorActual().getDireccionFiscal(), getInfoLocalidadList());
	}

	private void setProveedorActual(Proveedor proveedor) {
		this.proveedorActual = proveedor;

	}

	private Proveedor getProveedorActual() {
		return this.proveedorActual;
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		habilitarTabSeleccionado(true);
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (CLJOptionPane.showQuestionMessage(this, "Se eliminar\u00E1 al proveedor. Desea continuar?", "Administrar proveedores") == CLJOptionPane.YES_OPTION) {
				try {
					getProveedorFacade().eliminarProveedor(getProveedorActual().getId());
					lista.removeSelectedItem();
					itemSelectorSeleccionado(-1);
				} catch (RuntimeException e) {
					BossError.gestionarError(e);
				}
			}
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un item de la lista.", "Administrar provedores");
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if (validar()) {
			getProveedorActual().setContacto(getTxtContacto().getText().toUpperCase());
			getProveedorActual().setCuit(getTxtCuit().getText());
			getProveedorActual().setNombreCorto(getTxtNombreCorto().getText().toUpperCase());
			getProveedorActual().setObservaciones(getTxtObservaciones().getText().toUpperCase());
			getProveedorActual().setRazonSocial(getTxtRazonSocial().getText().toUpperCase());
			getProveedorActual().setDireccionFiscal(getPanDatosDireccionFiscal().getInfoDireccion());
			getProveedorActual().setDireccionReal(getPanDatosDireccionReal().getInfoDireccion());
			getProveedorActual().setTelefono(getPanTelefonoFijo().getDatos());
			getProveedorActual().setCelular(getPanCelular().getDatos());
			getProveedorActual().setEmail(getTxtEmail().getText());
			getProveedorActual().setSkype(getTxtSkype().getText());
			getProveedorActual().setFax(getPanFax().getDatos());
			getProveedorActual().setRubro((Rubro) getCmbRubro().getSelectedItem());
			getProveedorActual().setSitioWeb(getTxtSitioWeb().getText().trim());
			getProveedorActual().setNroIngresosBrutos(getTxtIngBrutos().getText());
			getProveedorActual().setPosicionIva((EPosicionIVA)getCmbPosicionIva().getSelectedItem());
			getProveedorActual().setCondicionVenta((CondicionDeVenta)getCmbCondicionVenta().getSelectedItem());
			getProveedorActual().setProvincia((Provincia)getCmbProvincia().getSelectedItem());
			// if(getProveedorFacade().existeProveedor(getProveedorActual().getRazonSocial(),
			// getProveedorActual().getCuit())){
			// CLJOptionPane.showErrorMessage(this,
			// "Los datos del proveedor ya se encuentran en el sistema.",
			// "Administrar proveedores");
			// return false;
			// }else{
			getProveedorFacade().guardarProveedor(getProveedorActual());
			CLJOptionPane.showInformationMessage(this, "Los datos del proveedor se han guardado con exito", "Administrar proveedores");
			return true;
			// }
		}
		return false;
	}

	private boolean validar() {
		String textoRazonSocial = getTxtRazonSocial().getText().trim();
		if (StringUtil.isNullOrEmpty(textoRazonSocial)) {
			CLJOptionPane.showErrorMessage(this, "Falta completar el campo 'RAZON SOCIAL'", "Advertencia");
			getTxtRazonSocial().requestFocus();
			return false;
		}

		if (getTxtCuit().getText().trim().length() < 13) {
			CLJOptionPane.showErrorMessage(this, "Debe completar el campo 'CUIT'.", "Advertencia");
			getTxtCuit().requestFocus();
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtNombreCorto().getText())) {
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el 'NOMBRE CORTO'", "Advertencia");
			getTxtNombreCorto().requestFocus();
			return false;
		}
		if (getTxtIngBrutos().getText().trim().length() == 0) {
			CLJOptionPane.showErrorMessage(this, "Debe completar el campo 'ING. BRUTOS'.", "Advertencia");
			getTxtIngBrutos().requestFocus();
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

		if (StringUtil.isNullOrEmpty(getPanCelular().getDatos()) && StringUtil.isNullOrEmpty(getPanTelefonoFijo().getDatos()) && StringUtil.isNullOrEmpty(getPanFax().getDatos())) {
			CLJOptionPane.showErrorMessage(GuiABMProveedor.this, "Debe ingresar al menos un telefono", "Administrar proveedores");
			return false;
		}
		String textoErrorTelefono = getPanTelefonoFijo().validar();
		if (textoErrorTelefono != null) {
			CLJOptionPane.showErrorMessage(GuiABMProveedor.this, StringW.wordWrap(textoErrorTelefono), "Advertencia");
			return false;
		}

		String textoErrorCelular = getPanCelular().validar();
		if (textoErrorCelular != null) {
			CLJOptionPane.showErrorMessage(GuiABMProveedor.this, StringW.wordWrap(textoErrorCelular), "Advertencia");
			return false;
		}

		String textoErrorFax = getPanFax().validar();
		if (textoErrorFax != null) {
			CLJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorFax), "Advertencia");
			return false;
		}

		String textoErrorDirFiscal = getPanDatosDireccionFiscal().validar();
		if (textoErrorDirFiscal != null) {
			CLJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorDirFiscal), "Advertencia");
			return false;
		}

		String textoErrorDirReal = getPanDatosDireccionReal().validar();
		if (textoErrorDirReal != null) {
			CLJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorDirReal), "Advertencia");
			return false;
		}

		String strMail = getTxtEmail().getText();
		if (!StringUtil.isNullOrEmpty(strMail) && !EmailValidator.getInstance().isValid(strMail.trim())) {
			CLJOptionPane.showErrorMessage(this, "El EMAIL ingresado no es válido", "Advertencia");
			getTxtEmail().requestFocus();
			return false;
		}

		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtRazonSocial().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un item de la lista.", "Administrar provedores");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {

	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setProveedorActual((Proveedor) lista.getSelectedValue());
		limpiarDatos();
		if (getProveedorActual() != null) {
			getTxtContacto().setText(getProveedorActual().getContacto());
			getTxtCuit().setText(getProveedorActual().getCuit());
			getTxtNombreCorto().setText(getProveedorActual().getNombreCorto());
			getTxtObservaciones().setText(getProveedorActual().getObservaciones());
			getTxtRazonSocial().setText(getProveedorActual().getRazonSocial());
			getPanDatosDireccionFiscal().setDireccion(getProveedorActual().getDireccionFiscal(), getInfoLocalidadList());
			getPanDatosDireccionReal().setDireccion(getProveedorActual().getDireccionReal(), getInfoLocalidadList());
			getPanCelular().setFullDatos(getProveedorActual().getCelular());
			getPanTelefonoFijo().setFullDatos(getProveedorActual().getTelefono());
			getPanFax().setFullDatos(getProveedorActual().getFax());
			getTxtEmail().setText(getProveedorActual().getEmail());
			getTxtSkype().setText(getProveedorActual().getSkype());
			getCmbRubro().setSelectedItem(getProveedorActual().getRubro());
			getTxtSitioWeb().setText(getProveedorActual().getSitioWeb());
			getTxtIngBrutos().setText(getProveedorActual().getNroIngresosBrutos());
			getCmbPosicionIva().setSelectedItem(getProveedorActual().getPosicionIva());
			getCmbCondicionVenta().setSelectedItem(getProveedorActual().getCondicionVenta());
			getCmbProvincia().setSelectedItem(getProveedorActual().getProvincia());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtContacto().setText("");
		getTxtCuit().setText("");
		getTxtNombreCorto().setText("");
		getTxtObservaciones().setText("");
		getTxtRazonSocial().setText("");
		getTxtEmail().setText("");
		getTxtSkype().setText("");
		getTxtSitioWeb().setText("");
		getPanDatosDireccionFiscal().limpiarDatos();
		getPanDatosDireccionReal().limpiarDatos();
		getPanFax().limpiarDatos();
		getPanCelular().limpiarDatos();
		getPanTelefonoFijo().limpiarDatos();
		getCmbRubro().setSelectedIndex(-1);
		getTxtIngBrutos().setText("");
		getCmbPosicionIva().setSelectedIndex(-1);
		getCmbCondicionVenta().setSelectedIndex(0);
		getCmbProvincia().setSelectedIndex(0);
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

	private CLJTextField getTxtNombreCorto() {
		if (txtNombreCorto == null) {
			txtNombreCorto = new CLJTextField(MAX_LONGITUD_NOMBRE_CORTO);
		}
		return txtNombreCorto;
	}

	private JTextArea getTxtObservaciones() {
		if (txtObservaciones == null) {
			txtObservaciones = new CLJTextArea(MAX_LONGITUD_OBSERVACIONES);
			txtObservaciones.setLineWrap(true);
			txtObservaciones.setBorder(BorderFactory.createLineBorder(Color.BLUE.darker()));
			txtObservaciones.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					}
				}
			});
		}
		return txtObservaciones;
	}

	private CLJTextField getTxtContacto() {
		if (txtContacto == null) {
			txtContacto = new CLJTextField(MAX_LONGITUD_CONTACTO);
			txtContacto.setPreferredSize(new Dimension(150, 20));
		}
		return txtContacto;
	}

	private JFormattedTextField getTxtCuit() {
		if (txtCuit == null) {
			try {
				txtCuit = new JFormattedTextField(new MaskFormatter("##-########-#"));
				txtCuit.addFocusListener(new FocusAdapter() {

					@Override
					public void focusLost(FocusEvent e) {
						try {
							txtCuit.commitEdit();
						} catch (ParseException e1) {
							// e1.printStackTrace();
						}
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtCuit;
	}

	private PanDatosDireccion getPanDatosDireccionFiscal() {
		if (panDatosDireccionFiscal == null) {
			panDatosDireccionFiscal = new PanDatosDireccion(GuiABMProveedor.this.getFrame(), "DIRECCION FISCAL");
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
		if (panDatosDireccionReal == null) {
			panDatosDireccionReal = new PanDatosDireccion(GuiABMProveedor.this.getFrame(), "DIRECCION REAL");
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
					if (infoLocalidadSelected.getCodigoArea().equals(11)) {
						if (getPanTelefonoFijo().getValorTelefono().length() > 8) {
							getPanTelefonoFijo().setValorTelefono(getPanTelefonoFijo().getValorTelefono().substring(0, 8));
						}
						getPanTelefonoFijo().setMaxLongTelefono(8);
					} else {
						getPanTelefonoFijo().setMaxLongTelefono(100);
					}
				}
			});
		}
		return panDatosDireccionReal;
	}

	private InfoLocalidadFacadeRemote getInfoLocalidadRemote() {
		if (infoLocalidadRemote == null) {
			infoLocalidadRemote = GTLBeanFactory.getInstance().getBean2(InfoLocalidadFacadeRemote.class);
		}
		return infoLocalidadRemote;
	}

	private List<InfoLocalidad> getInfoLocalidadList() {
		if (infoLocalidadList == null) {
			infoLocalidadList = getInfoLocalidadRemote().getAllInfoLocalidad();
			InfoLocalidad infoLocalidadOTRO = new InfoLocalidad();
			infoLocalidadOTRO.setId(-1);
			infoLocalidadOTRO.setNombreLocalidad("OTRO");
			infoLocalidadList.add(infoLocalidadOTRO);
		}
		return infoLocalidadList;
	}

	private PanDatosTelefono getPanTelefonoFijo() {
		if (panTelefonoFijo == null) {
			panTelefonoFijo = new PanDatosTelefono("TELEFONO", "-");
		}
		return panTelefonoFijo;
	}

	private PanDatosTelefono getPanCelular() {
		if (panCelular == null) {
			panCelular = new PanDatosTelefono("CELULAR", "-");
		}
		return panCelular;
	}

	private PanDatosTelefono getPanFax() {
		if (panFax == null) {
			panFax = new PanDatosTelefono("FAX", "-");
		}
		return panFax;
	}

	private CLJTextField getTxtEmail() {
		if (txtEmail == null) {
			txtEmail = new CLJTextField(MAX_LONGITUD_EMAIL);
			txtEmail.setPreferredSize(new Dimension(240, 20));
		}
		return txtEmail;
	}

	private CLJTextField getTxtSkype() {
		if (txtSkype == null) {
			txtSkype = new CLJTextField(MAX_LONGITUD_SKYPE);
			txtSkype.setPreferredSize(new Dimension(240, 20));
		}
		return txtSkype;
	}

	private CLJTextField getTxtSitioWeb() {
		if (txtSitioWeb == null) {
			txtSitioWeb = new CLJTextField(MAX_LONGITUD_SITIO_WEB);
		}
		return txtSitioWeb;
	}

	public RubroPersonaFacadeRemote getRubroFacadeRemote() {
		if (rubroFacadeRemote == null) {
			rubroFacadeRemote = GTLBeanFactory.getInstance().getBean2(RubroPersonaFacadeRemote.class);
		}
		return rubroFacadeRemote;
	}

	private void llenarComboRubro() {
		List<Rubro> allRubroPersona = getRubroFacadeRemote().getAllRubroByTipo(ETipoRubro.PROVEEDOR);
		Rubro rubroPersona = new Rubro();
		rubroPersona.setId(-1);
		rubroPersona.setNombre("OTRO");
		allRubroPersona.add(rubroPersona);
		GuiUtil.llenarCombo(getCmbRubro(), allRubroPersona, true);
	}

	private JComboBox getCmbRubro() {
		if (cmbRubro == null) {
			cmbRubro = new JComboBox();
			cmbRubro.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getCmbRubro().isEnabled()) {
						Rubro rubroSelected = ((Rubro) getCmbRubro().getSelectedItem());
						if (getCmbRubro().getSelectedItem() != null && rubroSelected.getId() == -1) {
							JDialogCargarRubro dialogCargaRubro = new JDialogCargarRubro(GuiABMProveedor.this.getFrame(), ETipoRubro.PROVEEDOR);
							GuiUtil.centrarEnFramePadre(dialogCargaRubro);
							dialogCargaRubro.setVisible(true);
							Rubro rubroNuevo = dialogCargaRubro.getRubro();
							if (rubroNuevo != null) {
								llenarComboRubro();
								getCmbRubro().setSelectedItem(rubroNuevo);
							} else {
								getCmbRubro().setSelectedItem(getProveedorActual().getRubro());
							}
						}
					}
				}
			});

		}
		return cmbRubro;
	}

	public JScrollPane getJsp() {
		if (jsp == null) {
			jsp = new JScrollPane(getPanDatos(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(650, 450));
		}
		return jsp;
	}

	public JPanel getPanDatos() {
		if (panDatos == null) {
			panDatos = new JPanel();
			panDatos.setLayout(new GridBagLayout());
			panDatos.setLayout(new GridBagLayout());
			panDatos.add(new JLabel("* RAZON SOCIAL: "), createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtRazonSocial(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 1, 0));
			panDatos.add(new JLabel("* NOMBRE CORTO: "), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtNombreCorto(), createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* C.U.I.T: "), createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtCuit(), createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* Provincia: "), createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getCmbProvincia(), createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* RUBRO: "), createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getCmbRubro(), createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* ING. BRUTOS: "), createGridBagConstraints(0, 5, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(getTxtIngBrutos(), createGridBagConstraints(1, 5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* CONDICION DE IVA: "), createGridBagConstraints(0, 6, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(getCmbPosicionIva(), createGridBagConstraints(1, 6, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* CONDICION DE VENTA: "), createGridBagConstraints(0, 7, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(getCmbCondicionVenta(), createGridBagConstraints(1, 7, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(getPanDatosDireccionFiscal(), createGridBagConstraints(0, 8, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDatos.add(getPanDatosDireccionReal(), createGridBagConstraints(0, 9, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDatos.add(getPanTelefonos(), createGridBagConstraints(0, 10, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 1, 0));
			panDatos.add(getPanelOtrosDatos(), createGridBagConstraints(0, 11, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0, 0, 0));
		}
		return panDatos;
	}

	private CLJTextField getTxtIngBrutos() {
		if (txtIngBrutos == null) {
			txtIngBrutos = new CLJTextField();
		}
		return txtIngBrutos;
	}
	
	private JComboBox getCmbPosicionIva() {
		if(cmbPosicionIva == null) {
			cmbPosicionIva = new JComboBox();
			GuiUtil.llenarCombo(cmbPosicionIva, Arrays.asList(EPosicionIVA.values()), true);
			cmbPosicionIva.setSelectedIndex(-1);
		}
		return cmbPosicionIva;
	}

	private JComboBox getCmbCondicionVenta() {
		if(cmbCondicionVenta == null){
			cmbCondicionVenta = new JComboBox();
			GuiUtil.llenarCombo(cmbCondicionVenta, GTLBeanFactory.getInstance().getBean2(CondicionDeVentaFacadeRemote.class).getAllOrderByName(), true);
		}
		return cmbCondicionVenta;
	}

	private JComboBox getCmbProvincia() {
		if(cmbProvincia == null){
			cmbProvincia = new JComboBox();
			GuiUtil.llenarCombo(cmbProvincia, GTLBeanFactory.getInstance().getBean2(ProvinciaFacadeRemote.class).getAllOrderByName(), true);
		}
		return cmbProvincia;
	}
}
