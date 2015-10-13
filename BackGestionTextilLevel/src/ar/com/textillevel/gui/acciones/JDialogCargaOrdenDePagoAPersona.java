package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import main.GTLGlobalCache;
import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextArea;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersonaCheque;
import ar.com.textillevel.entidades.documentos.pagopersona.formapago.FormaPagoOrdenDePagoPersonaEfectivo;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDePagoPersonaFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionordendepagoapersona.ImprimirOrdenDePagoAPersonaHandler;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogAgregarCheque;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargaOrdenDePagoAPersona extends JDialog {

	private static final long serialVersionUID = -6699587271260976864L;

	private final Frame owner;
	private PanelTablaCheque panelTablaCheques;
	private PanelDatePicker panelFecha;
	private FWJTextField txtPersona;
	private FWJTextArea txtDetalle;
	private FWJTextField txtTotal;
	private FWJTextField txtTotalCheques;
	private FWJTextField txtEfectivo;
	private FWJTextField txtNroOrden;
	private JButton btnGuardar;
	private JButton btnImprimir;
	private JButton btnSalir;

	private Persona persona;
	private OrdenDePagoAPersona ordenDePago;
	private EEstadoPantalla estadoPantalla;
	
	private boolean consulta;
	private boolean edicion;

	private final ModeloCargaOrdenDePagoPersona modelo;
	
	private OrdenDePagoPersonaFacadeRemote ordenFacade;

	public JDialogCargaOrdenDePagoAPersona(Frame padre, Persona p) {
		super(padre);
		this.owner = padre;
		this.persona = p;
		this.consulta = false;
		this.edicion =false;
		this.modelo = new ModeloCargaOrdenDePagoPersona();
		setUpComponentes();
		setUpScreen();
		setDatos();
	}

	public JDialogCargaOrdenDePagoAPersona(Frame padre, OrdenDePagoAPersona ordenDePagoAPersona, boolean consulta) {
		super(padre);
		this.owner = padre;
		this.consulta=consulta;
		this.edicion = !consulta;
		this.ordenDePago = ordenDePagoAPersona;
		this.persona = ordenDePagoAPersona.getPersona();
		setUpComponentes();
		setUpScreen();
		modelo = new ModeloCargaOrdenDePagoPersona();
		getBtnGuardar().setEnabled(!consulta);
		GuiUtil.setEstadoPanel(getPanelTablaCheques(), !consulta);
		GuiUtil.setEstadoPanel(getPanelFecha(), !consulta);
		getTxtEfectivo().setEnabled(!consulta);
		getTxtDetalle().setEnabled(!consulta);
		getTxtPersona().setText(getPersona().getRazonSocial());
		getTxtNroOrden().setText(String.valueOf(ordenDePagoAPersona.getNroOrden()));
		getTxtDetalle().setText(ordenDePagoAPersona.getDetalle());
		getTxtTotal().setText(GenericUtils.getDecimalFormat().format(ordenDePagoAPersona.getMontoTotal().doubleValue()));
		Double totalCheque = 0d;
		Double totalEfectivo = 0d;
		List<FormaPagoOrdenDePagoPersona> formasDePago = new ArrayList<FormaPagoOrdenDePagoPersona>();
		List<Cheque> chequesUsados = new ArrayList<Cheque>();
		for(FormaPagoOrdenDePagoPersona fp : ordenDePagoAPersona.getFormasDePago()){
			if(fp instanceof FormaPagoOrdenDePagoPersonaCheque){
				Cheque cheque = ((FormaPagoOrdenDePagoPersonaCheque)fp).getCheque();
				getPanelTablaCheques().agregarElemento(cheque);
				totalCheque += cheque.getImporte().doubleValue();
				chequesUsados.add(cheque);
			}else{
				totalEfectivo = ((FormaPagoOrdenDePagoPersonaEfectivo)fp).getImporte().doubleValue();
				getTxtEfectivo().setText(String.valueOf(totalEfectivo));
			}
			if(!consulta){
				formasDePago.add(fp);
			}
		}
		modelo.setMonto(ordenDePagoAPersona.getMontoTotal().doubleValue());
		modelo.setTotalCheques(totalCheque.doubleValue());
		modelo.setTotalEfectivo(totalEfectivo);
		modelo.getChequesUtilizados().addAll(chequesUsados);
		modelo.getFormasDePago().addAll(formasDePago);
		getTxtTotalCheques().setText(GenericUtils.getDecimalFormat().format(totalCheque));
	}

	private void setDatos() {
		getTxtPersona().setText(getPersona().getRazonSocial());
		Integer proximoNumeroOrden = getOrdenFacade().getProximoNumeroOrden();
		if(proximoNumeroOrden==null){
			throw new RuntimeException("No se ha definido el número inicial de ordenes de pago a personas");
		}
		getTxtNroOrden().setText(String.valueOf(proximoNumeroOrden));
	}

	private void setUpScreen() {
		setTitle("Orden de pago a persona");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(800, 600));
		setModal(true);
		setResizable(false);
		pack();
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getPanelNorte(),BorderLayout.NORTH);
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	private JPanel getPanelNorte(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("Persona: "));
		panel.add(getTxtPersona());
		panel.add(new JLabel("Nº orden: "));
		panel.add(getTxtNroOrden());
		panel.add(new JLabel("Fecha: "));
		panel.add(getPanelFecha());
		return panel;
	}
	
	private JPanel getPanelCentro(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panelIzquierda = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));
		JPanel panelIzquierdaAbajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelIzquierdaAbajo.add(new JLabel("Total cheques: " ));
		panelIzquierdaAbajo.add(getTxtTotalCheques());
		panelIzquierda.add(getPanelTablaCheques());
		panelIzquierda.add(panelIzquierdaAbajo);
		
		JPanel panelDerecha = new JPanel(new GridBagLayout());
		panelDerecha.add(new JLabel("Efectivo: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		panelDerecha.add(getTxtEfectivo(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		panelDerecha.add(new JLabel("Detalle: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		JScrollPane jsp = new JScrollPane(getTxtDetalle(), JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelDerecha.add(jsp, GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 2, 1, 1));
		panelDerecha.add(new JSeparator(JSeparator.HORIZONTAL), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 2, 1, 1));
		panelDerecha.add(new JLabel("Total: "), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		panelDerecha.add(getTxtTotal(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		
		panel.add(panelIzquierda);
		panel.add(panelDerecha);
		
		return panel;
	}

	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnGuardar());
		panel.add(getBtnImprimir());
		panel.add(getBtnSalir());
		return panel;
	}
	
	private class PanelTablaCheque extends PanelTabla<Cheque> {

		private static final long serialVersionUID = -5569856677948560204L;
		private static final int CANT_COLS_TBL_CHEQUE = 5;
		private static final int COL_BANCO = 0;
		private static final int COL_NRO = 1;
		private static final int COL_CUIT = 2;
		private static final int COL_IMPORTE = 3;
		private static final int COL_OBJ = 4;

		private ChequeFacadeRemote chequeFacade;
		private BancoFacadeRemote bancoFacade;
		private List<Banco> bancoList;
		private JComboBox cmbBanco;
		private JButton btnBuscarCheque;

		public PanelTablaCheque() {
			agregarBoton(getBtnBuscarCheque());
			setPreferredSize(new Dimension(522, 200));
		}
		
		public JButton getBtnBuscarCheque() {
			if(btnBuscarCheque == null){
				btnBuscarCheque = BossEstilos.createButton("ar/com/textillevel/imagenes/b_buscar.png", "ar/com/textillevel/imagenes/b_buscar_des.png");
				btnBuscarCheque.setToolTipText("Buscar cheque");
				btnBuscarCheque.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JDialogBuscarAgregarCheques dialog = new JDialogBuscarAgregarCheques(JDialogCargaOrdenDePagoAPersona.this, new CondicionDeVenta(),modelo.getChequesUtilizados());
						dialog.setVisible(true);
						if (dialog.isAcepto()) {
							for (Cheque c : dialog.getChequesSeleccionados()) {
								agregarCheque(c);
								modelo.getChequesUtilizados().add(c);
							}
							actualizarModelo();
						}
					}
				});
			}
			return btnBuscarCheque;
		}

		private List<Banco> getBancoList() {
			if (bancoList == null) {
				bancoList = getBancoFacade().getAllOrderByName();
			}
			return bancoList;
		}

		private JComboBox getCmbBanco() {
			if (cmbBanco == null) {
				cmbBanco = new JComboBox();
				GuiUtil.llenarCombo(cmbBanco, getBancoList(), false);
			}
			return cmbBanco;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarCheque dialogAgregarCheque = new JDialogAgregarCheque(owner);
			boolean acepto = dialogAgregarCheque.isAcepto();
			if (acepto) {
				try {
					Cheque cheque = dialogAgregarCheque.getCheque();
					cheque = getChequeFacade().grabarCheque(cheque, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					handleSeleccionCheque(cheque);
					boolean agregaOtro = dialogAgregarCheque.isAgregaOtro();
					if (agregaOtro) {
						do {
							dialogAgregarCheque = new JDialogAgregarCheque(owner, cloneCheque(cheque), false, true, true);
							cheque = dialogAgregarCheque.getCheque();
							acepto = dialogAgregarCheque.isAcepto();
							agregaOtro = dialogAgregarCheque.isAgregaOtro();
							if (acepto) {
								cheque = getChequeFacade().grabarCheque(cheque, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
								handleSeleccionCheque(cheque);
							}
						} while (agregaOtro && acepto);
					}
					return false;
				} catch (FWException cle) {
					BossError.gestionarError(cle);
					return false;
				}
			}
			return false;
		}

		private Cheque cloneCheque(Cheque cheque) {
			Cheque newCheque = new Cheque();
			newCheque.setBanco(cheque.getBanco());
			newCheque.setBancoSalida(cheque.getBancoSalida());
			newCheque.setCapitalOInterior(cheque.getCapitalOInterior());
			newCheque.setCliente(cheque.getCliente());
			newCheque.setClienteSalida(cheque.getClienteSalida());
			newCheque.setCuit(cheque.getCuit());
			newCheque.setEstadoCheque(cheque.getEstadoCheque());
			newCheque.setFechaDeposito(cheque.getFechaDeposito());
			newCheque.setFechaEntrada(cheque.getFechaEntrada());
			newCheque.setFechaSalida(cheque.getFechaSalida());
			newCheque.setImporte(cheque.getImporte());
			newCheque.setNombreProveedorSalida(cheque.getNombreProveedorSalida());
			newCheque.setNumeracion(cheque.getNumeracion());
			newCheque.setNumero(cheque.getNumero());
			newCheque.setPersonaSalida(cheque.getPersonaSalida());
			newCheque.setProveedorSalida(cheque.getProveedorSalida());
			return newCheque;
		}

		private void handleSeleccionCheque(Cheque cheque) {
			agregarCheque(cheque);
			modelo.getChequesUtilizados().add(cheque);
			actualizarModelo();
		}

		@Override
		public boolean validarQuitar() {
			Cheque c = getElemento(getTabla().getSelectedRow());
			modelo.getChequesUtilizados().remove(c);
			actualizarModelo();
			return true;
		}
		
		@Override
		protected void botonQuitarPresionado() {
			super.botonQuitarPresionado();
		}

		@Override
		protected void agregarElemento(Cheque c) {
			Object[] row = new Object[CANT_COLS_TBL_CHEQUE];
			row[COL_BANCO] = c.getBanco().getNombre();
			row[COL_CUIT] = c.getCuit();
			row[COL_IMPORTE] = c.getImporte().toString();
			row[COL_NRO] = c.getNumero();
			row[COL_OBJ] = c;
			getTabla().addRow(row);
		}

		public void agregarCheque(Cheque c) {
			agregarElemento(c);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tablaCheques = new FWJTable(0, CANT_COLS_TBL_CHEQUE);
			tablaCheques.setComboColumn(COL_BANCO, "Banco", getCmbBanco(), 100, true);
			tablaCheques.setStringColumn(COL_NRO, "Nº", 50, 100, true);
			tablaCheques.setStringColumn(COL_CUIT, "C.U.I.T", 80, 80, true);
			tablaCheques.setFloatColumn(COL_IMPORTE, "Importe", 80, true);
			tablaCheques.setStringColumn(COL_OBJ, "", 0, 0, true);

			tablaCheques.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						int selectedRow = getTabla().getSelectedRow();
						if (selectedRow != -1) {
							Cheque cheque = (Cheque) getTabla().getValueAt(selectedRow, COL_OBJ);
							new JDialogAgregarCheque(owner, cheque, true, false);
						}
					}
				}
			});

			return tablaCheques;
		}

		@Override
		protected Cheque getElemento(int fila) {
			return (Cheque) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		private ChequeFacadeRemote getChequeFacade() {
			if (chequeFacade == null) {
				chequeFacade = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
			}
			return chequeFacade;
		}

		private BancoFacadeRemote getBancoFacade() {
			if (bancoFacade == null) {
				bancoFacade = GTLBeanFactory.getInstance().getBean2(BancoFacadeRemote.class);
			}
			return bancoFacade;
		}
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public OrdenDePagoAPersona getOrdenDePago() {
		return ordenDePago;
	}

	public void setOrdenDePago(OrdenDePagoAPersona ordenDePago) {
		this.ordenDePago = ordenDePago;
	}

	public boolean isConsulta() {
		return consulta;
	}

	public void setConsulta(boolean consulta) {
		this.consulta = consulta;
	}

	@Override
	public Frame getOwner() {
		return owner;
	}

	private PanelTablaCheque getPanelTablaCheques() {
		if (panelTablaCheques == null) {
			panelTablaCheques = new PanelTablaCheque();
		}
		return panelTablaCheques;
	}

	private PanelDatePicker getPanelFecha() {
		if (panelFecha == null) {
			panelFecha = new PanelDatePicker();
		}
		return panelFecha;
	}

	private FWJTextField getTxtPersona() {
		if (txtPersona == null) {
			txtPersona = new FWJTextField();
			txtPersona.setEditable(false);
			txtPersona.setPreferredSize(new Dimension(120,20));
		}
		return txtPersona;
	}
	
	private FWJTextField getTxtNroOrden() {
		if (txtNroOrden == null) {
			txtNroOrden = new FWJTextField();
			txtNroOrden.setEditable(false);
			txtNroOrden.setPreferredSize(new Dimension(120,20));
		}
		return txtNroOrden;
	}

	private FWJTextArea getTxtDetalle() {
		if (txtDetalle == null) {
			txtDetalle = new FWJTextArea(650);
			txtDetalle.setLineWrap(true);
			txtDetalle.setPreferredSize(new Dimension(300, 170));
		}
		return txtDetalle;
	}

	private FWJTextField getTxtTotal() {
		if (txtTotal == null) {
			txtTotal = new FWJTextField();
			txtTotal.setEditable(false);
		}
		return txtTotal;
	}

	private FWJTextField getTxtTotalCheques() {
		if (txtTotalCheques == null) {
			txtTotalCheques = new FWJTextField();
			txtTotalCheques.setEditable(false);
			txtTotalCheques.setPreferredSize(new Dimension(120,20));
		}
		return txtTotalCheques;
	}
	
	private FWJTextField getTxtEfectivo() {
		if (txtEfectivo == null) {
			txtEfectivo = new FWJTextField();
			txtEfectivo.setPreferredSize(new Dimension(100, 20));
			txtEfectivo.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					actualizarModelo();
				}
			});
		}
		return txtEfectivo;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setMnemonic(KeyEvent.VK_G);
			btnGuardar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					guardarOrden();
				}
			});
		}
		return btnGuardar;
	}
	
	private JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new JButton("Salir");
			btnSalir.setMnemonic(KeyEvent.VK_S);
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}
	
	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.setMnemonic(KeyEvent.VK_I);
			btnImprimir.setVisible(isEdicion() ||  isConsulta());
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					imprimirOrden();
				}

			});
		}
		return btnImprimir;
	}

	private void imprimirOrden() {
		ImprimirOrdenDePagoAPersonaHandler handler = new ImprimirOrdenDePagoAPersonaHandler(getOrdenDePago(),this);
		handler.imprimir();
	}

	private void actualizarModelo() {
		resetModelo();
		for(Cheque c : modelo.getChequesUtilizados()){
			FormaPagoOrdenDePagoPersonaCheque pfodppc = new FormaPagoOrdenDePagoPersonaCheque();
			pfodppc.setCheque(c);
			pfodppc.setFechaEmision(new Timestamp(getPanelFecha().getDate().getTime()));
			modelo.getFormasDePago().add(pfodppc);
			modelo.setTotalCheques(modelo.getTotalCheques() + c.getImporte().doubleValue());
		}
		
		if (getTxtEfectivo().getText().trim().length() > 0) {
			if (GenericUtils.esNumerico(getTxtEfectivo().getText().trim())) {
				modelo.setTotalEfectivo(Double.valueOf(getTxtEfectivo().getText().trim().replace(",", ".")));
				FormaPagoOrdenDePagoPersonaEfectivo fpope = new FormaPagoOrdenDePagoPersonaEfectivo();
				fpope.setImportePagoSimple(new BigDecimal(modelo.getTotalEfectivo()));
				modelo.getFormasDePago().add(fpope);
			} else {
				FWJOptionPane.showErrorMessage(this, "El campo es numérico", "Error");
				getTxtEfectivo().requestFocus();
				setEstadoPantalla(EEstadoPantalla.ESTADO_INVALIDO);
				return;
			}
		}
		
		modelo.setMonto(modelo.getMonto() + modelo.getTotalCheques() + modelo.getTotalEfectivo());
		
		getTxtTotal().setText(GenericUtils.getDecimalFormat().format(modelo.getMonto()));
		getTxtTotalCheques().setText(GenericUtils.getDecimalFormat().format(modelo.getTotalCheques()));
//		getTxtEfectivo().setText(GenericUtils.getDecimalFormat().format(modelo.getTotalEfectivo()));
		
		setEstadoPantalla(EEstadoPantalla.ESTADO_VALIDO);
	}

	private void resetModelo() {
		modelo.getFormasDePago().clear();
		modelo.setMonto(0d);
		modelo.setTotalCheques(0d);
		modelo.setTotalEfectivo(0d);
	}

	private void guardarOrden() {
		actualizarModelo();
		if(getEstadoPantalla()==EEstadoPantalla.ESTADO_VALIDO){
			if(!modelo.getFormasDePago().isEmpty() || !modelo.getTotalEfectivo().equals(0d)){
				if(!StringUtil.isNullOrEmpty(getTxtDetalle().getText())){
					try{
						OrdenDePagoAPersona orden =  isEdicion()?getOrdenDePago():new OrdenDePagoAPersona();
						orden.setDetalle(getTxtDetalle().getText().trim().toUpperCase());
						orden.setFecha(new java.sql.Date(getPanelFecha().getDate().getTime()));
						orden.setFormasDePago(modelo.getFormasDePago());
						orden.setMontoTotal(new BigDecimal(modelo.getMonto()));
						orden.setNroOrden(Integer.valueOf(getTxtNroOrden().getText()));
						orden.setPersona(getPersona());
						String usuario = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
						if(!isEdicion()){
							orden.setUsuarioCreador(usuario);
						}
						setOrdenDePago(isEdicion()?getOrdenFacade().editarOrden(orden,usuario):getOrdenFacade().guardarOrden(orden,usuario));
						FWJOptionPane.showInformationMessage(this, "Se ha guardado correctamente la órden de pago", "Información");
						if(FWJOptionPane.showQuestionMessage(this, "¿Desea imprimir la Orden?", "Confirmación") == FWJOptionPane.YES_OPTION) {
							imprimirOrden();
						}
						dispose();
					}catch(FWException cle){
						BossError.gestionarError(cle);
					}
				}else{
					FWJOptionPane.showErrorMessage(this, "Debe especificar los detalles del pago", "Error");
					getTxtDetalle().requestFocus();
					return;
				}
			}else{
				FWJOptionPane.showErrorMessage(this, "Debe elegir las formas de pago", "Error");
				return;
			}
		}else{
			FWJOptionPane.showErrorMessage(this, "Los datos ingresados son inválidos", "Error");
			return;
		}
	}
	
	private void salir(){
		if(FWJOptionPane.showQuestionMessage(this, "Desea salir?", "Pregunta") == FWJOptionPane.YES_OPTION){
			dispose();
		}
	}

	private class ModeloCargaOrdenDePagoPersona {

		private final List<FormaPagoOrdenDePagoPersona> formasDePago;
		private final List<Cheque> chequesUtilizados;
		private Double monto;
		private Double totalCheques;
		private Double totalEfectivo;
		
		public ModeloCargaOrdenDePagoPersona(){
			formasDePago = new ArrayList<FormaPagoOrdenDePagoPersona>();
			chequesUtilizados = new ArrayList<Cheque>();
			monto = 0d;
			totalCheques = 0d;
			totalEfectivo = 0d;
		}
		
		public List<FormaPagoOrdenDePagoPersona> getFormasDePago() {
			return formasDePago;
		}

		public List<Cheque> getChequesUtilizados() {
			return chequesUtilizados;
		}

		public Double getMonto() {
			return monto;
		}

		public void setMonto(Double monto) {
			this.monto = monto;
		}

		public Double getTotalCheques() {
			return totalCheques;
		}
		
		public void setTotalCheques(Double totalCheques) {
			this.totalCheques = totalCheques;
		}
		
		public Double getTotalEfectivo() {
			return totalEfectivo;
		}
		
		public void setTotalEfectivo(Double totalEfectivo) {
			this.totalEfectivo = totalEfectivo;
		}
	}
	
	private enum EEstadoPantalla {
		ESTADO_INVALIDO, ESTADO_VALIDO;
	}

	public EEstadoPantalla getEstadoPantalla() {
		return estadoPantalla;
	}

	public void setEstadoPantalla(EEstadoPantalla estadoPantalla) {
		this.estadoPantalla = estadoPantalla;
	}
	
	public OrdenDePagoPersonaFacadeRemote getOrdenFacade() {
		if(ordenFacade == null){
			ordenFacade = GTLBeanFactory.getInstance().getBean2(OrdenDePagoPersonaFacadeRemote.class);
		}
		return ordenFacade;
	}

	public boolean isEdicion() {
		return edicion;
	}

	public void setEdicion(boolean edicion) {
		this.edicion = edicion;
	}
}
