package ar.com.textillevel.gui.modulos.cheques.cabecera;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.templates.modulo.cabecera.Cabecera;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.EnumTipoFecha;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.controles.PanelPaginador;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarPersona;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarProveedor;
import ar.com.textillevel.util.GTLBeanFactory;

public class CabeceraCheques extends Cabecera<ModeloCabeceraCheques> {

	private static final long serialVersionUID = -6178911074486418969L;

	public static final Integer MAX_ROWS = 30;

	private ChequeFacadeRemote chequeFacade;

	private PanelPaginador paginador;
	private PanelDatePicker txtFechaDesde;
	private PanelDatePicker txtFechaHasta;
	private JComboBox cmbEstadoCheque;
	private JComboBox cmbTipoBusquedaCliente;
	private JComboBox cmbTipoFecha;
	private JComboBox cmbBancos;
	private FWJTextField txtBusquedaCliente;
	private final JButton btnBuscar;
	private final JButton btnResetFiltros;
	private ModeloCabeceraCheques modeloCabeceraCheques;
	private JCheckBox chkUsarFecha;
	private FWJTextField txtMontoDesde;
	private FWJTextField txtMontoHasta;

	@Override
	public ModeloCabeceraCheques getModel() {
		if (modeloCabeceraCheques == null) {
			modeloCabeceraCheques = new ModeloCabeceraCheques();
		}
		modeloCabeceraCheques.setPaginaActual(getPaginador().getPageIndex());
		if(getChkUsarFecha().isSelected()){
			modeloCabeceraCheques.setFechaDesde(DateUtil.redondearFecha(getDPFechaDesde().getDate() != null ? getDPFechaDesde().getDate() : DateUtil.getHoy()));
			modeloCabeceraCheques.setFechaHasta(DateUtil.redondearFecha(getDPFechaHasta().getDate() != null ? getDPFechaHasta().getDate() : DateUtil.getHoy()));
		}else{
			modeloCabeceraCheques.setFechaDesde(null);
			modeloCabeceraCheques.setFechaHasta(null);
		}
		if (! StringUtil.isNullOrEmpty(getTxtMontoDesde().getText()) && !StringUtil.isNullOrEmpty(getTxtMontoHasta().getText())) {
			modeloCabeceraCheques.setMontoDesde(Double.valueOf(getTxtMontoDesde().getText().trim().replace(",", ".")));
			modeloCabeceraCheques.setMontoHasta(Double.valueOf(getTxtMontoHasta().getText().trim().replace(",", ".")));
		} else {
			modeloCabeceraCheques.setMontoDesde(null);
			modeloCabeceraCheques.setMontoHasta(null);
		}
		modeloCabeceraCheques.setIdBanco(getCmbBancos().getSelectedItem().equals("TODOS") ? null : ((Banco) getCmbBancos().getSelectedItem()).getId());
		modeloCabeceraCheques.setEstadoCheque(getCmbEstadoCheque().getSelectedItem().equals("TODOS") ?null:(EEstadoCheque)getCmbEstadoCheque().getSelectedItem());
		modeloCabeceraCheques.setTipoFecha((EnumTipoFecha)getCmbTipoFecha().getSelectedItem());
		if(((String)getCmbTipoBusquedaCliente().getSelectedItem()).equalsIgnoreCase("NUMERACION INTERNA")){
			modeloCabeceraCheques.setNumeracionCheque(getTxtBusquedaCliente().getText().trim().length()==0?null:(!GenericUtils.isSistemaTest()?"a":"b")+getTxtBusquedaCliente().getText());
			modeloCabeceraCheques.setNroCliente(null);
			modeloCabeceraCheques.setNombreProveedor(null);
			modeloCabeceraCheques.setNumeroCheque(null);
		}else if (((String)getCmbTipoBusquedaCliente().getSelectedItem()).equalsIgnoreCase("NOMBRE PROVEEDOR")) {
			modeloCabeceraCheques.setNumeracionCheque(null);
			modeloCabeceraCheques.setNroCliente(null);
			modeloCabeceraCheques.setNumeroCheque(null);
			modeloCabeceraCheques.setNombreProveedor(getTxtBusquedaCliente().getText().trim().length()==0?null:getTxtBusquedaCliente().getText());
		}else if (((String)getCmbTipoBusquedaCliente().getSelectedItem()).equalsIgnoreCase("NOMBRE PERSONA")) {
			modeloCabeceraCheques.setNumeracionCheque(null);
			modeloCabeceraCheques.setNroCliente(null);
			modeloCabeceraCheques.setNumeroCheque(null);
			modeloCabeceraCheques.setNombreProveedor(null);
			modeloCabeceraCheques.setNombrePersona(getTxtBusquedaCliente().getText().trim().length()==0?null:getTxtBusquedaCliente().getText());
		}else if (((String)getCmbTipoBusquedaCliente().getSelectedItem()).equalsIgnoreCase("NUMERO CHEQUE")){
			modeloCabeceraCheques.setNroCliente(null);
			modeloCabeceraCheques.setNumeracionCheque(null);
			modeloCabeceraCheques.setNombreProveedor(null);
			modeloCabeceraCheques.setNumeroCheque(getTxtBusquedaCliente().getText().trim().length()==0?null:getTxtBusquedaCliente().getText());
		}else {
			modeloCabeceraCheques.setNroCliente(getTxtBusquedaCliente().getText().trim().length()==0?null:Integer.valueOf(getTxtBusquedaCliente().getText()));
			modeloCabeceraCheques.setNumeracionCheque(null);
			modeloCabeceraCheques.setNombreProveedor(null);
			modeloCabeceraCheques.setNumeroCheque(null);
		}
		return modeloCabeceraCheques;
	}

	public CabeceraCheques() {
		super();
		setLayout(new GridBagLayout());
		JPanel panel2 = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.CENTER,5,2));
		
		JPanel panelArriba = new JPanel();
		panelArriba.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
		
		JPanel panelAbajo = new JPanel();
		panelAbajo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
		
		JPanel panelMasAbajo = new JPanel();
		panelMasAbajo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
		
		panel2.setBorder(BorderFactory.createEtchedBorder());
		panelArriba.add(new JLabel("Cliente: "));
		panelArriba.add(getCmbTipoBusquedaCliente());
		panelArriba.add(getTxtBusquedaCliente());
		panelArriba.add(new JLabel("Estado cheque: "));
		panelArriba.add(getCmbEstadoCheque());
		panelArriba.add(new JLabel("Banco: "));
		panelArriba.add(getCmbBancos());
		panelAbajo.add(new JLabel("Tipo de fecha: "));
		panelAbajo.add(getCmbTipoFecha());
		panelAbajo.add(getDPFechaDesde());
		panelAbajo.add(getDPFechaHasta());
		panelAbajo.add(getChkUsarFecha());
		btnBuscar = new JButton("Buscar");
		btnResetFiltros = new JButton("Resetear filtros");
		panelMasAbajo.add(new JLabel("Monto desde: "));
		panelMasAbajo.add(getTxtMontoDesde());
		panelMasAbajo.add(new JLabel("Monto Hasta: "));
		panelMasAbajo.add(getTxtMontoHasta());
		panelMasAbajo.add(btnBuscar);
		panelMasAbajo.add(btnResetFiltros);
		
		panel2.add(panelArriba);
		panel2.add(panelAbajo);
		panel2.add(panelMasAbajo);

		btnBuscar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				if (!getDPFechaDesde().getDate().after(getDPFechaHasta().getDate())) {
					if(getTxtBusquedaCliente().getText().trim().length()>0){
						if(((String)getCmbTipoBusquedaCliente().getSelectedItem()).equalsIgnoreCase("NUMERACION INTERNA")){
							if(getTxtBusquedaCliente().getText().length()<2){
								FWJOptionPane.showErrorMessage(CabeceraCheques.this, "La numeraci�n consta de al menos 2 caracteres", "Error");
								return;
							}
//							if(!getTxtBusquedaCliente().getText().matches("[a-zA-Z]{1}[0-9]+")){
							if(!getTxtBusquedaCliente().getText().matches("[0-9]+")){ //antes validaba el formato a455...ahora solo quiere numeros.
								FWJOptionPane.showErrorMessage(CabeceraCheques.this, "el formato de numeraci�n no es correcto", "Error");
								return;
							}
						}else if (((String)getCmbTipoBusquedaCliente().getSelectedItem()).equalsIgnoreCase("NOMBRE PROVEEDOR")||
								((String)getCmbTipoBusquedaCliente().getSelectedItem()).equalsIgnoreCase("NOMBRE PERSONA")) {
							
						}else {
							if(!GenericUtils.esNumerico(getTxtBusquedaCliente().getText())){
								FWJOptionPane.showErrorMessage(CabeceraCheques.this, "Solo puede ingresar n�meros para esta busqueda", "Error");
								return;
							}
						}
						if(StringUtil.isNullOrEmpty(getTxtMontoDesde().getText()) ^ StringUtil.isNullOrEmpty(getTxtMontoHasta().getText())) {
							FWJOptionPane.showErrorMessage(CabeceraCheques.this, "Debe ingresar ambos montos para realizar utilizarlos como criterio", "Error");
							return;
						} else if(!StringUtil.isNullOrEmpty(getTxtMontoDesde().getText()) && !StringUtil.isNullOrEmpty(getTxtMontoHasta().getText())) {
							if (!GenericUtils.esNumerico(getTxtMontoDesde().getText().trim())) {
								FWJOptionPane.showErrorMessage(CabeceraCheques.this, "El campo 'monto desde' es numerico", "Error");
								return;
							}
							if (!GenericUtils.esNumerico(getTxtMontoHasta().getText().trim())) {
								FWJOptionPane.showErrorMessage(CabeceraCheques.this, "El campo 'monto hasta' es numerico", "Error");
								return;
							}
							Double montoDesde = Double.valueOf(getTxtMontoDesde().getText().trim().replace(",", "."));
							Double montoHasta = Double.valueOf(getTxtMontoHasta().getText().trim().replace(",", "."));
							if(montoDesde > montoHasta) {
								FWJOptionPane.showErrorMessage(CabeceraCheques.this, "El 'monto desde' debe ser mayor al 'monto hasta'", "Error");
								return;
							}
						}
					}
					getPaginador().setPageIndex(1);
					refrescar();
					notificar();
				} else {
					FWJOptionPane.showErrorMessage(CabeceraCheques.this, "La 'fecha desde' no debe ser posterior a la 'fecha hasta'", "Error");
				}
			}
		});
		
		btnResetFiltros.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getCmbTipoBusquedaCliente().setSelectedIndex(0);
				getTxtBusquedaCliente().setText(null);
				getCmbEstadoCheque().setSelectedIndex(0);
				getCmbBancos().setSelectedIndex(0);
				getCmbTipoFecha().setSelectedIndex(0);
				getDPFechaDesde().setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 7));
				getDPFechaDesde().setSelectedDate(DateUtil.getHoy());
				getChkUsarFecha().setSelected(true);
				getTxtMontoDesde().setText(null);
				getTxtMontoHasta().setText(null);
			}
		});

		add(panel2, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.LINE_END, new Insets(15, 5, 0, 5), 0, 0));
		add(getPaginador(), new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.LINE_END, new Insets(15, 5, 0, 5), 0, 0));
	}

	private PanelPaginador getPaginador() {
		if (paginador == null) {
			paginador = new PanelPaginador();
			paginador.setRowsPageSize(MAX_ROWS);
			paginador.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					refrescar();
					notificar();
				}
			});
		}
		return paginador;
	}

	private void refrescar() {
		try {
			ModeloCabeceraCheques model = getModel();
			model.setPaginaActual(getPaginador().getPageIndex());
			Timestamp fechaDesde = null;
			Timestamp fechaHasta = null;
			if (model.getFechaDesde() != null)
				fechaDesde = new Timestamp(model.getFechaDesde().getTime());
			if (model.getFechaHasta() != null)
				fechaHasta = new Timestamp(model.getFechaHasta().getTime());
			if ((model.getFechaDesde() != null) && (model.getFechaHasta() != null)) {
				if (fechaHasta.before(fechaDesde)) {
					FWJOptionPane.showErrorMessage(this, "La fecha 'Hasta' debe ser mayor o igual que la fecha 'Desde'", "Validaci�n de fechas");
					return;
				}
			}
			Integer rows = this.getCantidadDeRegistros();
			getPaginador().setRowsCount(rows);
			getPaginador().setRowsPageSize(MAX_ROWS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected Integer getCantidadDeRegistros() {
		Date fechaDesde = getModel().getFechaDesde();
		Date fechaHasta = getModel().getFechaHasta();
		if(getModel().getNroCliente()!=null){
			return getChequeFacade().getCantidadDeCheques(getModel().getNroCliente(), getModel().getEstadoCheque(),
					(fechaDesde!=null?new java.sql.Date(fechaDesde.getTime()):null),(fechaHasta!=null? new java.sql.Date(DateUtil.getManiana(fechaHasta).getTime()):null),getModel().getTipoFecha(), getModel().getIdBanco(), getModel().getMontoDesde(), getModel().getMontoHasta());
		}else if( getModel().getNumeracionCheque()!=null){
			return getChequeFacade().getCantidadDeCheques(getModel().getNumeracionCheque(), getModel().getEstadoCheque(),
					(fechaDesde!=null?new java.sql.Date(fechaDesde.getTime()):null),(fechaHasta!=null? new java.sql.Date(DateUtil.getManiana(fechaHasta).getTime()):null),getModel().getTipoFecha(), getModel().getIdBanco(), getModel().getMontoDesde(), getModel().getMontoHasta());
		}else if(getModel().getNumeroCheque()!=null){
			return getChequeFacade().getCantidadDechequesPorNumeroDeCheque(getModel().getNumeroCheque(), getModel().getEstadoCheque(),
					(fechaDesde!=null?new java.sql.Date(fechaDesde.getTime()):null), (fechaHasta!=null?new java.sql.Date(DateUtil.getManiana(fechaHasta).getTime()):null),getModel().getTipoFecha(), getModel().getIdBanco(), getModel().getMontoDesde(), getModel().getMontoHasta());
		}else if(getModel().getNombrePersona()!=null){
			return getChequeFacade().getCantidadDeChequesPorFechaYPaginadoPorProveedor(getModel().getNombrePersona(), getModel().getEstadoCheque(),
					(fechaDesde != null?new java.sql.Date(fechaDesde.getTime()):null), (fechaHasta!=null?new java.sql.Date(DateUtil.getManiana(fechaHasta).getTime()):null),getModel().getTipoFecha(), getModel().getIdBanco(), getModel().getMontoDesde(), getModel().getMontoHasta());
		}else{
			return getChequeFacade().getCantidadDeChequesPorFechaYPaginadoPorProveedor(getModel().getNombreProveedor(), getModel().getEstadoCheque(),
					(fechaDesde != null?new java.sql.Date(fechaDesde.getTime()):null), (fechaHasta!=null?new java.sql.Date(DateUtil.getManiana(fechaHasta).getTime()):null),getModel().getTipoFecha(), getModel().getIdBanco(), getModel().getMontoDesde(), getModel().getMontoHasta());
		}
	}

	private PanelDatePicker getDPFechaDesde() {
		if (txtFechaDesde == null) {
			txtFechaDesde = new PanelDatePicker();
			txtFechaDesde.setCaption("Fecha desde:");
			txtFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 7));
		}
		return txtFechaDesde;
	}

	private PanelDatePicker getDPFechaHasta() {
		if (txtFechaHasta == null) {
			txtFechaHasta = new PanelDatePicker();
			txtFechaHasta.setCaption("Fecha hasta:");
		}
		return txtFechaHasta;
	}

	private ChequeFacadeRemote getChequeFacade() {
		if (chequeFacade == null) {
			chequeFacade = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
		}
		return chequeFacade;
	}

	public JComboBox getCmbEstadoCheque() {
		if(cmbEstadoCheque==null){
			cmbEstadoCheque = new JComboBox();
			cmbEstadoCheque.addItem("TODOS");
			for(EEstadoCheque e:EEstadoCheque.values()){
				cmbEstadoCheque.addItem(e);
			}
		}
		return cmbEstadoCheque;
	}

	public JComboBox getCmbTipoBusquedaCliente() {
		if(cmbTipoBusquedaCliente == null){
			cmbTipoBusquedaCliente = new JComboBox();
			cmbTipoBusquedaCliente.addItem("NUMERACION INTERNA");
			cmbTipoBusquedaCliente.addItem("NUMERO CLIENTE");
			cmbTipoBusquedaCliente.addItem("NOMBRE CLIENTE");
			cmbTipoBusquedaCliente.addItem("NOMBRE PROVEEDOR");
			cmbTipoBusquedaCliente.addItem("NUMERO CHEQUE");
			cmbTipoBusquedaCliente.addItem("NOMBRE PERSONA");
			cmbTipoBusquedaCliente.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						if(cmbTipoBusquedaCliente.getSelectedItem().equals("NOMBRE CLIENTE")){
							JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(null, EModoDialogo.MODO_NOMBRE);
							GuiUtil.centrar(dialogSeleccionarCliente);
							dialogSeleccionarCliente.setVisible(true);
							Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
							if (clienteElegido != null) {
								getTxtBusquedaCliente().setText(String.valueOf(clienteElegido.getNroCliente()));
							}
						} else if(cmbTipoBusquedaCliente.getSelectedItem().equals("NOMBRE PROVEEDOR")){
							JDialogSeleccionarProveedor dialogoProv = new JDialogSeleccionarProveedor(null);
							GuiUtil.centrar(dialogoProv);
							dialogoProv.setVisible(true);
							Proveedor prov = dialogoProv.getProveedor();
							if (prov != null) {
								getTxtBusquedaCliente().setText(prov.getNombreCorto());
							}
						} else if(cmbTipoBusquedaCliente.getSelectedItem().equals("NOMBRE PERSONA")){
							JDialogSeleccionarPersona dialogoProv = new JDialogSeleccionarPersona(null);
							GuiUtil.centrar(dialogoProv);
							dialogoProv.setVisible(true);
							Persona prov = dialogoProv.getPersona();
							if (prov != null) {
								getTxtBusquedaCliente().setText(prov.getRazonSocial());
							}
						} else{
							getTxtBusquedaCliente().setText("");
						}
					}
				}
			});
		}
		return cmbTipoBusquedaCliente;
	}

	public FWJTextField getTxtBusquedaCliente() {
		if(txtBusquedaCliente == null){
			txtBusquedaCliente = new FWJTextField();
			txtBusquedaCliente.setPreferredSize(new Dimension(100, 20));
			txtBusquedaCliente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnBuscar.doClick();
				}
			});
			txtBusquedaCliente.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					txtBusquedaCliente.setText(txtBusquedaCliente.getText().toUpperCase());
				}
			});
		}
		return txtBusquedaCliente;
	}
	
	public JComboBox getCmbTipoFecha() {
		if(cmbTipoFecha == null){
			cmbTipoFecha = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoFecha, Arrays.asList(EnumTipoFecha.values()), true);
		}
		return cmbTipoFecha;
	}
	
	public JCheckBox getChkUsarFecha() {
		if(chkUsarFecha == null){
			chkUsarFecha = new JCheckBox("Filtrar por fecha");
			chkUsarFecha.setSelected(true);
		}
		return chkUsarFecha;
	}

	public JComboBox getCmbBancos() {
		if (cmbBancos == null) {
			cmbBancos = new JComboBox();
			List<Banco> bancos = GTLBeanFactory.getInstance().getBean2(BancoFacadeRemote.class).getAllOrderByName();
			cmbBancos.addItem("TODOS");
			for(Banco b : bancos) {
				cmbBancos.addItem(b);
			}
		}
		return cmbBancos;
	}

	public FWJTextField getTxtMontoDesde() {
		if (txtMontoDesde == null) {
			txtMontoDesde = new FWJTextField();
			txtMontoDesde.setPreferredSize(new Dimension(100, 20));
		}
		return txtMontoDesde;
	}

	public FWJTextField getTxtMontoHasta() {
		if (txtMontoHasta == null) {
			txtMontoHasta = new FWJTextField();
			txtMontoHasta.setPreferredSize(new Dimension(100, 20));
		}
		return txtMontoHasta;
	}
}