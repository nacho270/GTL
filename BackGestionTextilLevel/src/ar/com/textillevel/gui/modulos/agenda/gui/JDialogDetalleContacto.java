package ar.com.textillevel.gui.modulos.agenda.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.MaskFormatter;

import org.apache.commons.validator.EmailValidator;
import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextArea;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.IAgendable;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.facade.api.remote.PersonaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.RubroPersonaFacadeRemote;
import ar.com.textillevel.gui.util.panels.PanDatosDireccion;
import ar.com.textillevel.gui.util.panels.PanDatosTelefono;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogDetalleContacto extends JDialog {

	private static final long serialVersionUID = -100823342041059915L;

	private IAgendable contacto;

	private static final int MAX_LONGITUD_RAZ_SOCIAL = 50;
	private static final int MAX_LONGITUD_CONTACTO = 50;
	private static final int MAX_LONGITUD_NOMBRE_CORTO = 50;
	private static final int MAX_LONGITUD_SKYPE = 50;
	private static final int MAX_LONGITUD_EMAIL = 1000;
	private static final int MAX_LONGITUD_NOMBRE_Y_APELLIDO = 100;
	private static final int MAX_LONGITUD_OBSERVACIONES = 1000;
	private static final int MAX_LONGITUD_SITIO_WEB = 100;

	private JPanel panDetalle;
	private FWJTextField txtRazonSocial;
	private FWJTextField txtNombreCorto;
	private FWJTextField txtContacto;
	private JFormattedTextField txtCuit;
	private FWJTextArea txtObservaciones;
	private JButton btnSalir;
	private FWJTextField txtEmail;
	private FWJTextField txtSkype;
	private FWJNumericTextField txtNroCliente;
	private PanDatosDireccion panDatosDireccionFiscal;
	private PanDatosDireccion panDatosDireccionReal;
	private PanDatosTelefono panTelefonoFijo;
	private PanDatosTelefono panCelular;
	private FWJTextField txtNombres;
	private FWJTextField txtApellido;
	private PanDatosDireccion panDatosDireccion;
	private JComboBox cmbRubro;
	private PanDatosTelefono panFax;
	private JComboBox cmbPosicionIva;
	private FWJTextField txtSitioWeb;
	private FWJTextField txtIngBrutos;
	private JComboBox cmbCondicionVenta;

	private JButton btnEditar;
	private JButton btnGrabar;
	private JButton btnEliminar;

	private boolean editando;

	private RubroPersonaFacadeRemote rubroFacadeRemote;
	private List<InfoLocalidad> infoLocalidadList;
	private InfoLocalidadFacadeRemote infoLocalidadRemote;

	public JDialogDetalleContacto(IAgendable contacto, Frame padre) {
		super(padre);
		this.contacto = contacto;
		this.createAndShowGUI();
		this.setModal(true);
		this.setVisible(true);
	}

	private void createAndShowGUI() {
		setUpScreen();
		setUpComponentes();
	}

	private void setUpScreen() {
		this.setTitle("Detalles de contacto");
		this.setSize(new Dimension(750, 600));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		if (this.getContacto() instanceof Cliente) {
			this.add(this.getPanDetalleCliente(), BorderLayout.CENTER);
			setClienteActual((Cliente) contacto);
			llenarDatosCliente(getClienteActual());
			GuiUtil.setEstadoPanel(this.getPanDetalleCliente(), false);
		} else if (this.getContacto() instanceof Proveedor) {
			this.add(this.getPanDetalleProveedor(), BorderLayout.CENTER);
			setProveedorActual((Proveedor) contacto);
			llenarDatosProveedor(getProveedorActual());
			GuiUtil.setEstadoPanel(this.getPanDetalleProveedor(), false);
		} else if (this.getContacto() instanceof Persona) {
			this.add(this.getPanDetallePersona(), BorderLayout.CENTER);
			setPersonaActual((Persona) contacto);
			llenarDatosPersona(getPersonaActual());
			GuiUtil.setEstadoPanel(this.getPanDetalleProveedor(), false);
		}

		this.add(getPanBotones(), BorderLayout.SOUTH);
		getBtnGrabar().setEnabled(false);
	}

	private JPanel getPanDetallePersona() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("NOMBRES:"), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 45, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombres(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("APELLIDO:"), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtApellido(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getPanDatosDireccion(), createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
			panDetalle.add(getPanTelefonos(), createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			panDetalle.add(new JLabel("RUBRO:"), createGridBagConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbRubro(), createGridBagConstraints(1, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("OBSERVACIONES:"), createGridBagConstraints(0, 6, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtObservaciones(), createGridBagConstraints(1, 6, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 4, 1, 1, 0.8));
		}
		return panDetalle;
	}

	private void llenarDatosProveedor(Proveedor proveedor) {
		getTxtContacto().setText(proveedor.getContacto());
		getTxtCuit().setText(proveedor.getCuit());
		getTxtNombreCorto().setText(proveedor.getNombreCorto());
		getTxtObservaciones().setText(proveedor.getObservaciones());
		getTxtRazonSocial().setText(proveedor.getRazonSocial());
		getPanDatosDireccionFiscal().setDireccion(proveedor.getDireccionFiscal(), getInfoLocalidadList());
		getPanDatosDireccionReal().setDireccion(proveedor.getDireccionReal(), getInfoLocalidadList());
		getPanCelular().setFullDatos(proveedor.getCelular());
		getPanTelefonoFijo().setFullDatos(proveedor.getTelefono());
		getPanFax().setFullDatos(proveedor.getFax());
		getTxtEmail().setText(proveedor.getEmail());
		getTxtSkype().setText(proveedor.getSkype());
		GuiUtil.llenarCombo(getCmbRubro(), getRubroFacadeRemote().getAll(), true);
		getCmbRubro().setSelectedItem(proveedor.getRubro());
		getTxtIngBrutos().setText(proveedor.getNroIngresosBrutos());
		getCmbPosicionIva().setSelectedItem(proveedor.getPosicionIva());
		getCmbCondicionVenta().setSelectedItem(proveedor.getCondicionVenta());
	}

	private void llenarDatosCliente(Cliente cliente) {
		getTxtNroCliente().setValue(getClienteActual().getNroCliente().longValue());
		getTxtRazonSocial().setText(getClienteActual().getRazonSocial());
		getPanDatosDireccionFiscal().setDireccion(getClienteActual().getDireccionFiscal(), getInfoLocalidadList());
		getPanDatosDireccionReal().setDireccion(getClienteActual().getDireccionReal(), getInfoLocalidadList());
		getTxtCuit().setText(getClienteActual().getCuit());
		getTxtContacto().setText(getClienteActual().getContacto());
		getTxtObservaciones().setText(getClienteActual().getObservaciones());
		getTxtEmail().setText(getClienteActual().getEmail());
		getTxtSkype().setText(getClienteActual().getSkype());
		getPanCelular().setFullDatos(getClienteActual().getCelular());
		getPanTelefonoFijo().setFullDatos(getClienteActual().getTelefono());
		getPanFax().setFullDatos(getClienteActual().getFax());
		getCmbPosicionIva().setSelectedItem(getClienteActual().getPosicionIva());
	}

	private void llenarDatosPersona(Persona persona) {
		getTxtApellido().setText(persona.getApellido());
		getTxtNombres().setText(persona.getNombres());
		getPanDatosDireccion().setDireccion(persona.getInfoDireccion(), getInfoLocalidadList());
		getTxtObservaciones().setText(persona.getObservaciones());
		getTxtEmail().setText(persona.getEmail());
		getPanCelular().setFullDatos(persona.getCelular());
		getPanTelefonoFijo().setFullDatos(persona.getTelefono());
		GuiUtil.llenarCombo(getCmbRubro(), getRubroFacadeRemote().getAll(), true);
		getCmbRubro().setSelectedItem(persona.getRubroPersona());
	}

	private JPanel getPanBotones() {
		JPanel pnlSur = new JPanel();
		pnlSur.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnlSur.add(getBtnGrabar());
		pnlSur.add(getBtnEliminar());
		pnlSur.add(getBtnEditar());
		pnlSur.add(getBtnSalir());
		return pnlSur;
	}

	private JPanel getPanDetalleCliente() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			JPanel panDatos = new JPanel();
			panDatos.setLayout(new GridBagLayout());
			panDatos.setLayout(new GridBagLayout());
			panDatos.add(new JLabel("* NRO. DE CLIENTE:"), createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtNroCliente(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDatos.add(new JLabel("* RAZON SOCIAL:"), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtRazonSocial(), createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(new JLabel("* C.U.I.T.:"), createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtCuit(), createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* CONDICION DE IVA:"), createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getCmbPosicionIva(), createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panDatos.add(getPanDatosDireccionFiscal(), createGridBagConstraints(0, 5, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDatos.add(getPanDatosDireccionReal(), createGridBagConstraints(0, 6, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDatos.add(getPanTelefonos(), createGridBagConstraints(0, 7, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 1, 0));
			panDatos.add(getPanelOtrosDatos(), createGridBagConstraints(0, 8, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0, 0, 1));
			JScrollPane jsp = new JScrollPane(panDatos, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(740, 500));
			panDetalle.add(jsp, BorderLayout.CENTER);
		}
		return panDetalle;
	}

	private JPanel getPanDetalleProveedor() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			JPanel panDatos = new JPanel();
			panDatos.setLayout(new GridBagLayout());
			panDatos.add(new JLabel("* RAZON SOCIAL: "), createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtRazonSocial(), createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 1, 0));
			panDatos.add(new JLabel("* NOMBRE CORTO: "), createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtNombreCorto(), createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* C.U.I.T: "), createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtCuit(), createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* RUBRO: "), createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getCmbRubro(), createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* ING. BRUTOS: "), createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getTxtIngBrutos(), createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* CONDICION DE IVA:"), createGridBagConstraints(0, 5, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatos.add(getCmbPosicionIva(), createGridBagConstraints(1, 5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(new JLabel("* CONDICION DE VENTA: "), createGridBagConstraints(0, 6, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panDatos.add(getCmbCondicionVenta(), createGridBagConstraints(1, 6, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 10), 1, 1, 0, 0));
			panDatos.add(getPanDatosDireccionFiscal(), createGridBagConstraints(0, 7, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDatos.add(getPanDatosDireccionReal(), createGridBagConstraints(0, 8, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 0, 0));
			panDatos.add(getPanTelefonos(), createGridBagConstraints(0, 9, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 5), 2, 1, 1, 0));
			panDatos.add(getPanelOtrosDatos(), createGridBagConstraints(0, 10, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0, 0, 0));
			JScrollPane jsp = new JScrollPane(panDatos, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setPreferredSize(new Dimension(740, 500));
			panDetalle.add(jsp, BorderLayout.CENTER);
		}
		return panDetalle;
	}

	private FWJTextField getTxtSitioWeb() {
		if (txtSitioWeb == null) {
			txtSitioWeb = new FWJTextField(MAX_LONGITUD_SITIO_WEB);
		}
		return txtSitioWeb;
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
		jsp.setPreferredSize(new Dimension(500, 44));
		panelAbajo.add(jsp);

		panel.add(panelArriba);
		panel.add(panelAbajo);

		return panel;
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

	private JComboBox getCmbPosicionIva() {
		if (cmbPosicionIva == null) {
			cmbPosicionIva = new JComboBox();
			GuiUtil.llenarCombo(cmbPosicionIva, Arrays.asList(EPosicionIVA.values()), true);
			cmbPosicionIva.setSelectedIndex(-1);
		}
		return cmbPosicionIva;
	}

	public IAgendable getContacto() {
		return contacto;
	}

	public void setContacto(IAgendable contacto) {
		this.contacto = contacto;
	}

	private FWJTextField getTxtContacto() {
		if (txtContacto == null) {
			txtContacto = new FWJTextField(MAX_LONGITUD_CONTACTO);
			txtContacto.setPreferredSize(new Dimension(150, 20));
		}
		return txtContacto;
	}

	private FWJTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new FWJTextField(MAX_LONGITUD_RAZ_SOCIAL);
		}
		return txtRazonSocial;
	}

	private JFormattedTextField getTxtCuit() {
		if (txtCuit == null) {
			try {
				txtCuit = new JFormattedTextField(new MaskFormatter("##-########-#"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtCuit;
	}

	private FWJTextArea getTxtObservaciones() {
		if (txtObservaciones == null) {
			txtObservaciones = new FWJTextArea(MAX_LONGITUD_OBSERVACIONES);
			txtObservaciones.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		}
		return txtObservaciones;
	}

	private FWJTextField getTxtNombreCorto() {
		if (txtNombreCorto == null) {
			txtNombreCorto = new FWJTextField(MAX_LONGITUD_NOMBRE_CORTO);
		}
		return txtNombreCorto;
	}

	public JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}

	private FWJTextField getTxtEmail() {
		if (txtEmail == null) {
			txtEmail = new FWJTextField(MAX_LONGITUD_EMAIL);
			txtEmail.setPreferredSize(new Dimension(250, 20));
		}
		return txtEmail;
	}

	private FWJTextField getTxtSkype() {
		if (txtSkype == null) {
			txtSkype = new FWJTextField(MAX_LONGITUD_SKYPE);
			txtSkype.setPreferredSize(new Dimension(150, 20));
		}
		return txtSkype;
	}

	private FWJNumericTextField getTxtNroCliente() {
		if (txtNroCliente == null) {
			txtNroCliente = new FWJNumericTextField();
		}
		return txtNroCliente;
	}

	private PanDatosDireccion getPanDatosDireccionFiscal() {
		if (panDatosDireccionFiscal == null) {
			panDatosDireccionFiscal = new PanDatosDireccion(null, "DIRECCION FISCAL");
		}
		return panDatosDireccionFiscal;
	}

	private PanDatosDireccion getPanDatosDireccionReal() {
		if (panDatosDireccionReal == null) {
			panDatosDireccionReal = new PanDatosDireccion(null, "DIRECCION REAL");
		}
		return panDatosDireccionReal;
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
		pnlEmail.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
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

	private InfoLocalidadFacadeRemote getInfoLocalidadRemote() {
		if (infoLocalidadRemote == null) {
			infoLocalidadRemote = GTLBeanFactory.getInstance().getBean2(InfoLocalidadFacadeRemote.class);
		}
		return infoLocalidadRemote;
	}

	private FWJTextField getTxtNombres() {
		if (txtNombres == null) {
			txtNombres = new FWJTextField(MAX_LONGITUD_NOMBRE_Y_APELLIDO);
		}
		return txtNombres;
	}

	private FWJTextField getTxtApellido() {
		if (txtApellido == null) {
			txtApellido = new FWJTextField(MAX_LONGITUD_NOMBRE_Y_APELLIDO);
		}
		return txtApellido;
	}

	private PanDatosDireccion getPanDatosDireccion() {
		if (panDatosDireccion == null) {
			panDatosDireccion = new PanDatosDireccion(null, "DIRECCION");
		}
		return panDatosDireccion;
	}

	private JComboBox getCmbRubro() {
		if (cmbRubro == null) {
			cmbRubro = new JComboBox();
		}
		return cmbRubro;
	}

	private PanDatosTelefono getPanFax() {
		if (panFax == null) {
			panFax = new PanDatosTelefono("FAX", "-");
		}
		return panFax;
	}

	public RubroPersonaFacadeRemote getRubroFacadeRemote() {
		if (rubroFacadeRemote == null) {
			rubroFacadeRemote = GTLBeanFactory.getInstance().getBean2(RubroPersonaFacadeRemote.class);
		}
		return rubroFacadeRemote;
	}

	/**
	 * 
	 * 
	 * METODOS PARA PODER EDITAR EN ESTE DIALOGO
	 * 
	 * 
	 * 
	 */

	private ProveedorFacadeRemote proveedorFacade;
	private PersonaFacadeRemote personaFacadeRemote;
	private ClienteFacadeRemote clienteFacadeRemote;

	private Cliente clienteActual;
	private Proveedor proveedorActual;
	private Persona personaActual;

	private void setProveedorActual(Proveedor proveedor) {
		this.proveedorActual = proveedor;

	}

	private Proveedor getProveedorActual() {
		return this.proveedorActual;
	}

	private boolean validarProveedor() {
		String textoRazonSocial = getTxtRazonSocial().getText().trim();
		if (StringUtil.isNullOrEmpty(textoRazonSocial)) {
			FWJOptionPane.showErrorMessage(this, "Falta completar el campo 'RAZON SOCIAL'", "Advertencia");
			getTxtRazonSocial().requestFocus();
			return false;
		}

		if (getTxtCuit().getText().trim().length() < 13) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el campo 'CUIT'.", "Advertencia");
			getTxtCuit().requestFocus();
			return false;
		}
		
		if (getTxtIngBrutos().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el campo 'ING. BRUTOS'.", "Advertencia");
			getTxtIngBrutos().requestFocus();
			return false;
		}
		
		if(getCmbPosicionIva().getSelectedItem() == null) {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar el campo 'CONDICION DE IVA'.","Advertencia");
			return false;
		}

		if(getCmbCondicionVenta().getSelectedItem() == null) {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar el campo 'CONDICION DE VENTA'.","Advertencia");
			return false;
		}
		
		if (StringUtil.isNullOrEmpty(getPanCelular().getDatos()) && StringUtil.isNullOrEmpty(getPanTelefonoFijo().getDatos()) && StringUtil.isNullOrEmpty(getPanFax().getDatos())) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un telefono", "Administrar proveedores");
			return false;
		}

		String textoErrorTelefono = getPanTelefonoFijo().validar();
		if (textoErrorTelefono != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorTelefono), "Advertencia");
			return false;
		}

		String textoErrorCelular = getPanCelular().validar();
		if (textoErrorCelular != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorCelular), "Advertencia");
			return false;
		}

		String textoErrorFax = getPanFax().validar();
		if (textoErrorFax != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorFax), "Advertencia");
			return false;
		}

		String textoErrorDirFiscal = getPanDatosDireccionFiscal().validar();
		if (textoErrorDirFiscal != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorDirFiscal), "Advertencia");
			return false;
		}

		String textoErrorDirReal = getPanDatosDireccionReal().validar();
		if (textoErrorDirReal != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorDirReal), "Advertencia");
			return false;
		}

		String strMail = getTxtEmail().getText();
		if (!StringUtil.isNullOrEmpty(strMail) && !EmailValidator.getInstance().isValid(strMail.trim())) {
			FWJOptionPane.showErrorMessage(this, "El EMAIL ingresado no es válido", "Advertencia");
			getTxtEmail().requestFocus();
			return false;
		}

		return true;
	}

	private Persona getPersonaActual() {
		return personaActual;
	}

	private void setPersonaActual(Persona personaActual) {
		this.personaActual = personaActual;
	}

	private boolean validarPersona() {
		String textoNombres = getTxtNombres().getText().trim();
		if (StringUtil.isNullOrEmpty(textoNombres)) {
			FWJOptionPane.showErrorMessage(this, "Falta completar el campo 'NOMBRES'", "Advertencia");
			getTxtNombres().requestFocus();
			return false;
		}
		String textoRazonSocial = getTxtApellido().getText().trim();
		if (StringUtil.isNullOrEmpty(textoRazonSocial)) {
			FWJOptionPane.showErrorMessage(this, "Falta completar el campo 'APELLIDO'", "Advertencia");
			getTxtApellido().requestFocus();
			return false;
		}
		String textoErrorTelefono = getPanTelefonoFijo().validar();
		if (textoErrorTelefono != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorTelefono), "Advertencia");
			return false;
		}
		String textoErrorCelular = getPanCelular().validar();
		if (textoErrorCelular != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorCelular), "Advertencia");
			return false;
		}
		String strMail = getTxtEmail().getText();
		if (!StringUtil.isNullOrEmpty(strMail) && !EmailValidator.getInstance().isValid(strMail.trim())) {
			FWJOptionPane.showErrorMessage(this, "El 'EMAIL' ingresado no es válido", "Advertencia");
			getTxtEmail().requestFocus();
			return false;
		}
		Rubro rubro = (Rubro) getCmbRubro().getSelectedItem();
		if (rubro == null) {
			FWJOptionPane.showErrorMessage(this, "Falta seleccionar el 'RUBRO'", "Advertencia");
			return false;
		}
		if (StringUtil.isNullOrEmpty(getPanCelular().getDatos()) && StringUtil.isNullOrEmpty(getPanTelefonoFijo().getDatos())) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un teléfono", "Advertencia");
			return false;
		}
		return true;
	}

	private Cliente getClienteActual() {
		return clienteActual;
	}

	private void setClienteActual(Cliente clienteActual) {
		this.clienteActual = clienteActual;
	}

	private boolean validarCliente() {
		Integer valTxtNroCliente = getTxtNroCliente().getText().trim().equalsIgnoreCase("") ? null : Integer.valueOf(getTxtNroCliente().getText().trim());
		if (valTxtNroCliente != null) {
			boolean existeNroCliente = getClienteFacade().existeNroCliente(getClienteActual().getId(), valTxtNroCliente);
			if (existeNroCliente) {
				FWJOptionPane.showErrorMessage(this, StringW.wordWrap("El número de cliente " + valTxtNroCliente + " ya está asignado a otro cliente."), "Error");
				getTxtNroCliente().requestFocus();
				return false;
			}
		}
		String textoRazonSocial = getTxtRazonSocial().getText().trim();
		if (StringUtil.isNullOrEmpty(textoRazonSocial)) {
			FWJOptionPane.showErrorMessage(this, "Falta completar el campo 'RAZON SOCIAL'", "Advertencia");
			getTxtRazonSocial().requestFocus();
			return false;
		}
		if (getTxtCuit().getText().trim().length() < 13) {
			FWJOptionPane.showErrorMessage(this, "Debe completar el campo 'CUIT'.", "Advertencia");
			getTxtCuit().requestFocus();
			return false;
		}
		if (getCmbPosicionIva().getSelectedItem() == null) {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar el campo 'CONDICION DE IVA'.", "Advertencia");
			return false;
		}

		if (StringUtil.isNullOrEmpty(getPanCelular().getDatos()) && StringUtil.isNullOrEmpty(getPanTelefonoFijo().getDatos()) && StringUtil.isNullOrEmpty(getPanFax().getDatos())) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un teléfono", "Advertencia");
			return false;
		}
		String textoErrorDirFiscal = getPanDatosDireccionFiscal().validar();
		if (textoErrorDirFiscal != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorDirFiscal), "Advertencia");
			return false;
		}
		String textoErrorDirReal = getPanDatosDireccionReal().validar();
		if (textoErrorDirReal != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorDirReal), "Advertencia");
			return false;
		}
		String textoErrorTelefono = getPanTelefonoFijo().validar();
		if (textoErrorTelefono != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorTelefono), "Advertencia");
			return false;
		}
		String textoErrorCelular = getPanCelular().validar();
		if (textoErrorCelular != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorCelular), "Advertencia");
			return false;
		}
		String textoErrorFax = getPanFax().validar();
		if (textoErrorFax != null) {
			FWJOptionPane.showErrorMessage(this, StringW.wordWrap(textoErrorFax), "Advertencia");
			return false;
		}
		String strMail = getTxtEmail().getText();
		if (!StringUtil.isNullOrEmpty(strMail) && !EmailValidator.getInstance().isValid(strMail.trim())) {
			FWJOptionPane.showErrorMessage(this, "El EMAIL ingresado no es válido", "Advertencia");
			getTxtEmail().requestFocus();
			return false;
		}
		return true;
	}

	public boolean grabarProveedor() {
		if (validarProveedor()) {
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
			// if(getProveedorFacade().existeProveedor(getProveedorActual().getRazonSocial(),
			// getProveedorActual().getCuit())){
			// CLJOptionPane.showErrorMessage(this,
			// "Los datos del proveedor ya se encuentran en el sistema.",
			// "Administrar proveedores");
			// return false;
			// }else{
			getProveedorFacade().guardarProveedor(getProveedorActual());
			FWJOptionPane.showInformationMessage(this, "Los datos del proveedor se han guardado con exito", "Administrar proveedores");
			// }
			return true;
		}
		return false;
	}

	public boolean grabarPersona() {
		if (validarPersona()) {
			capturarSetearDatosPersona();
			getPersonaFacade().save(getPersonaActual());
			FWJOptionPane.showInformationMessage(this, "Los datos de la persona se han guardado con éxito", "Administrar Clientes");
			return true;
		}
		return false;
	}

	private void capturarSetearDatosPersona() {
		getPersonaActual().setEmail(getTxtEmail().getText().trim());
		getPersonaActual().setObservaciones(getTxtObservaciones().getText().trim().toUpperCase());
		getPersonaActual().setTelefono(getPanTelefonoFijo().getDatos());
		getPersonaActual().setCelular(getPanCelular().getDatos());
		getPersonaActual().setInfoDireccion(getPanDatosDireccion().getInfoDireccion());
		getPersonaActual().setApellido(getTxtApellido().getText().trim().toUpperCase());
		getPersonaActual().setNombres(getTxtNombres().getText().trim().toUpperCase());
		getPersonaActual().setRubroPersona((Rubro) getCmbRubro().getSelectedItem());
	}

	public boolean grabarCliente() {
		if (validarCliente()) {
			capturarSetearDatosCliente();
			try {
				getClienteFacade().save(getClienteActual());
				FWJOptionPane.showInformationMessage(this, "Los datos del cliente se han guardado con éxito", "Administrar Clientes");
				return true;
			} catch (ValidacionException e) {
				FWJOptionPane.showErrorMessage(this, StringW.wordWrap(e.getMensajeError()), "Error");
			}
		}
		return false;
	}

	private void capturarSetearDatosCliente() {
		getClienteActual().setNroCliente(getTxtNroCliente().getValue());
		getClienteActual().setRazonSocial(getTxtRazonSocial().getText().trim().toUpperCase());
		getClienteActual().setDireccionFiscal(getPanDatosDireccionFiscal().getInfoDireccion());
		getClienteActual().setDireccionReal(getPanDatosDireccionReal().getInfoDireccion());
		getClienteActual().setCuit(getTxtCuit().getText().trim());
		getClienteActual().setContacto(getTxtContacto().getText().trim().toUpperCase());
		getClienteActual().setEmail(getTxtEmail().getText().trim());
		getClienteActual().setSkype(getTxtSkype().getText().trim());
		getClienteActual().setObservaciones(getTxtObservaciones().getText().trim().toUpperCase());
		getClienteActual().setTelefono(getPanTelefonoFijo().getDatos());
		getClienteActual().setCelular(getPanCelular().getDatos());
		getClienteActual().setFax(getPanFax().getDatos());
		getClienteActual().setPosicionIva((EPosicionIVA) getCmbPosicionIva().getSelectedItem());
	}

	private ClienteFacadeRemote getClienteFacade() {
		if (clienteFacadeRemote == null) {
			clienteFacadeRemote = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
		}
		return clienteFacadeRemote;
	}

	private PersonaFacadeRemote getPersonaFacade() {
		if (personaFacadeRemote == null) {
			personaFacadeRemote = GTLBeanFactory.getInstance().getBean2(PersonaFacadeRemote.class);
		}
		return personaFacadeRemote;
	}

	private ProveedorFacadeRemote getProveedorFacade() {
		if (proveedorFacade == null) {
			proveedorFacade = GTLBeanFactory.getInstance().getBean2(ProveedorFacadeRemote.class);
		}
		return proveedorFacade;
	}

	public boolean isEditando() {
		return editando;
	}

	public void setEditando(boolean editando) {
		this.editando = editando;
	}

	public JButton getBtnEditar() {
		if (btnEditar == null) {
			btnEditar = new JButton("Editar");
			btnEditar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					btnEditar.setEnabled(false);
					getBtnGrabar().setEnabled(true);
					setEstadoPanel(true);
				}
			});
		}
		return btnEditar;
	}

	private void setEstadoPanel(boolean estado) {
		if (getContacto() instanceof Cliente) {
			GuiUtil.setEstadoPanel(getPanDetalleCliente(), estado);
		}
		if (getContacto() instanceof Proveedor) {
			GuiUtil.setEstadoPanel(getPanDetalleProveedor(), estado);
		}
		if (getContacto() instanceof Persona) {
			GuiUtil.setEstadoPanel(getPanDetallePersona(), estado);
		}
	}

	public JButton getBtnGrabar() {
		if (btnGrabar == null) {
			btnGrabar = new JButton("Grabar");
			btnGrabar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					boolean ok = true;
					if (getContacto() instanceof Cliente) {
						ok = ok && grabarCliente();
					}
					if (getContacto() instanceof Proveedor) {
						ok = ok && grabarProveedor();
					}
					if (getContacto() instanceof Persona) {
						ok = ok && grabarPersona();
					}
					if (ok) {
						getBtnEditar().setEnabled(true);
						getBtnGrabar().setEnabled(false);
						setEstadoPanel(false);
					}
				}
			});
		}
		return btnGrabar;
	}

	public JButton getBtnEliminar() {
		if (btnEliminar == null) {
			btnEliminar = new JButton("Eliminar");
			btnEliminar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getContacto() instanceof Cliente) {
						eliminarCliente();
					}
					if (getContacto() instanceof Proveedor) {
						eliminarProveedor();
					}
					if (getContacto() instanceof Persona) {
						eliminarPersona();
					}

				}
			});
		}
		return btnEliminar;
	}

	public void eliminarCliente() {
		if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el cliente seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
			try {
				getClienteFacade().remove(getClienteActual());
				dispose();
			} catch (FWRuntimeException cle) {
				BossError.gestionarError(cle);
			}
		}
	}

	public void eliminarPersona() {
		if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar a la persona seleccionada?", "Confirmación") == FWJOptionPane.YES_OPTION) {
			try {
				getPersonaFacade().remove(getPersonaActual());
				dispose();
			} catch (FWRuntimeException ex) {
				BossError.gestionarError(ex);
			}
		}
	}

	public void eliminarProveedor() {
		if (FWJOptionPane.showQuestionMessage(this, "Se eliminar\u00E1 al proveedor. Desea continuar?", "Administrar proveedores") == FWJOptionPane.YES_OPTION) {
			try {
				getProveedorFacade().eliminarProveedor(getProveedorActual().getId());
				dispose();
			} catch (RuntimeException e) {
				BossError.gestionarError(e);
			}
		}
	}

	private FWJTextField getTxtIngBrutos() {
		if (txtIngBrutos == null) {
			txtIngBrutos = new FWJTextField();
		}
		return txtIngBrutos;
	}
	
	private JComboBox getCmbCondicionVenta() {
		if(cmbCondicionVenta == null){
			cmbCondicionVenta = new JComboBox();
			GuiUtil.llenarCombo(cmbCondicionVenta, GTLBeanFactory.getInstance().getBean2(CondicionDeVentaFacadeRemote.class).getAllOrderByName(), true);
		}
		return cmbCondicionVenta;
	}
}
