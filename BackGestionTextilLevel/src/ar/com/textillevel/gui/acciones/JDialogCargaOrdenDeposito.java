package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.documentos.ordendedeposito.DepositoCheque;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.OrdenDeDepositoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.acciones.impresionordendedeposito.ImprimirOrdenDeDepositoHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargaOrdenDeposito extends JDialog {

	private static final long serialVersionUID = -2475044074638624386L;

	private final ParametrosGenerales paramGrales = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales();
	
	private JPanel panelCentro;
	private JPanel panelSur;
	private JPanel panelNorte;
	
	private JComboBox cmbBancos;

	private FWJTextField txtCantidadCheques;
	private FWJTextField txtTotalCheques;
	private FWJTextField txtTotalLetras;
	private FWJTextField txtPersonaResponsable;

	private FWJNumericTextField txtNroOrden;
	
	private FWJTextField txtBusquedaCheques;
	private JButton btnBuscar;

	private PanelDatePicker panelFecha;

	private PanelTablaCheques panelTablaCheques;

	private JButton btnGuardar;
	private JButton btnImprimir;
	private JButton btnSalir;

	private ModeloOrdenDeDeposito modelo;

	private BancoFacadeRemote bancoFacade;
	private OrdenDeDepositoFacadeRemote ordenFacade;
	
	private OrdenDeDeposito ordenFinal;
	
	private boolean consulta;
	
	/**
	 * Constructor para cargar una orden de deposito
	 * @param padre
	 */
	public JDialogCargaOrdenDeposito(Frame padre) {
		super(padre);
		modelo = new ModeloOrdenDeDeposito();
		setConsulta(false);
		setUpComponentes();
		setUpScreen();
		getTxtNroOrden().setText(String.valueOf(getOrdenFacade().getNewNroOrden()));
	}
	
	/**
	 * Constructor para consultar una orden de deposito
	 * @param padre
	 * @param orden
	 */
	public JDialogCargaOrdenDeposito(Frame padre, OrdenDeDeposito orden) {
		super(padre);
		setConsulta(true);
		setOrdenFinal(orden);
		setUpComponentes();
		setUpScreen();
		GuiUtil.setEstadoPanel(getPanelCentro(), false);
		GuiUtil.setEstadoPanel(getPanelNorte(), false);
		setDatosConsulta();
	}

	private void setDatosConsulta() {
		getTxtNroOrden().setText(String.valueOf(getOrdenFinal().getNroOrden()));
		getPanelFecha().setSelectedDate(getOrdenFinal().getFecha());
		getTxtTotalCheques().setText(GenericUtils.getDecimalFormat().format(getOrdenFinal().getMontoTotal().doubleValue()));
		getTxtCantidadCheques().setText(String.valueOf(getOrdenFinal().getDepositos().size()));
		getTxtTotalLetras().setText(getOrdenFinal().getTotalLetras());
		getCmbBancos().setSelectedItem(getOrdenFinal().getBanco());
		for(DepositoCheque dep : getOrdenFinal().getDepositos()){
			getPanelTablaCheques().agregarElemento(dep);
		}
		if(getOrdenFinal().getPersonaResponsable()!=null){
			getTxtPersonaResponsable().setText(getOrdenFinal().getPersonaResponsable());
		}
	}

	private void setUpScreen() {
		setTitle("Orden de depósito");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(650, 520));
		setResizable(false);
		GuiUtil.centrar(this);
		setModal(true);
		pack();
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getPanelNorte(), BorderLayout.NORTH);
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	private class PanelTablaCheques extends PanelTabla<DepositoCheque> {

		private static final long serialVersionUID = -5163142680052074494L;

		private static final int CANT_COLS = 5;
		private static final int COL_FECHA_DEPOSITO = 0;
		private static final int COL_NRO_CHEQUE = 1;
		private static final int COL_NRO_INTERNO = 2;
		private static final int COL_IMPORTE = 3;
		private static final int COL_OBJ = 4;

//		private JButton botonBuscarCheques;
		
		public PanelTablaCheques() {
			getBotonAgregar().setVisible(false);
//			agregarBoton(getBotonBuscarCheques());
			setPreferredSize(new Dimension(550, 300));
		}

		@Override
		protected void agregarElemento(DepositoCheque elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA_DEPOSITO] = elemento.getCheque().getFechaEntrada();
			row[COL_NRO_CHEQUE] =  elemento.getCheque().getNumero();
			row[COL_NRO_INTERNO] = elemento.getCheque().getNumeracion().toString();
			row[COL_IMPORTE] = elemento.getCheque().getImporte().doubleValue();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setDateColumn(COL_FECHA_DEPOSITO, "Fecha de depósito", 100, true);
			tabla.setStringColumn(COL_NRO_CHEQUE, "Número", 120, 120, true);
			tabla.setStringColumn(COL_NRO_INTERNO, "Número interno", 80, 80, true);
			tabla.setFloatColumn(COL_IMPORTE, "Importe", 120, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setHeaderAlignment(COL_FECHA_DEPOSITO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_NRO_CHEQUE, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_NRO_INTERNO, FWJTable.CENTER_ALIGN);
			tabla.setHeaderAlignment(COL_IMPORTE, FWJTable.CENTER_ALIGN);
			return tabla;
		}

		@Override
		protected DepositoCheque getElemento(int fila) {
			return (DepositoCheque)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarQuitar() {
			FWJTable tabla = getPanelTablaCheques().getTabla();
			DepositoCheque dep = modelo.getDepositos().get(tabla.getSelectedRow());
			modelo.getIdsYaUtilizados().remove(dep.getCheque().getId());
			modelo.getDepositos().remove(dep);
			tabla.removeRow(tabla.getSelectedRow());
			actualizar();
			return false;
		}
		
//		public JButton getBotonBuscarCheques() {
//			if(botonBuscarCheques==null){
//				botonBuscarCheques = new JButton("Cheques");
//				botonBuscarCheques.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						
//					}
//				});
//			}
//			return botonBuscarCheques;
//		}
	}

	private void actualizar(){
		getPanelTablaCheques().getTabla().removeAllRows();
		BigDecimal suma = new BigDecimal(0d);
		for(DepositoCheque d : modelo.getDepositos()){
			getPanelTablaCheques().agregarElemento(d);
			suma = suma.add(d.getCheque().getImporte());
		}
		modelo.setTotalCheques(suma);
		getTxtCantidadCheques().setText(String.valueOf(modelo.getDepositos().size()));
		getTxtTotalCheques().setText(GenericUtils.fixPrecioCero(GenericUtils.getDecimalFormat().format(modelo.getTotalCheques())));
		getTxtTotalLetras().setText(GenericUtils.convertirNumeroATexto(modelo.getTotalCheques().doubleValue()));
	}
	
	private class ModeloOrdenDeDeposito {
		private final List<DepositoCheque> depositos;
		private final List<Integer> idsYaUtilizados;
		private BigDecimal totalCheques;
		
		public ModeloOrdenDeDeposito(){
			depositos = new ArrayList<DepositoCheque>();
			idsYaUtilizados = new ArrayList<Integer>();
			totalCheques = new BigDecimal(0d);
		}
		
		public List<Integer> getIdsYaUtilizados() {
			return idsYaUtilizados;
		}
		
		public BigDecimal getTotalCheques() {
			return totalCheques;
		}

		public void setTotalCheques(BigDecimal totalCheques) {
			this.totalCheques = totalCheques;
		}
		
		public List<DepositoCheque> getDepositos() {
			return depositos;
		}
	}

	private JPanel getPanelCentro() {
		if(panelCentro == null){
			panelCentro = new JPanel();
			panelCentro.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
			panelCentro.add(getPanelCheques());
		}
		return panelCentro;
	}
	
	private JPanel getPanelCheques(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(""));
		panel.setLayout(new GridBagLayout());
		panel.add(getPanelTablaCheques(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 6, 1, 4, 1));
		
		panel.add(new JLabel("Cheques: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5),1, 1, 0, 0));
		panel.add(getTxtBusquedaCheques(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),2, 1, 0, 0));
		panel.add(getBtnBuscar(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),1, 1, 0, 0));
		
		JLabel lblIndicaciones = new JLabel("<html>Los números deben estar separados por un espacio y no deben contener la letra</html>");
		lblIndicaciones.setFont(new Font(lblIndicaciones.getFont().getName(), Font.BOLD, 12));
		panel.add(lblIndicaciones, GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),3, 1, 1, 0));
		
		panel.add(new JLabel("Cantidad de cheques: "), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5),1, 1, 0, 0));
		panel.add(getTxtCantidadCheques(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(new JLabel("Total cheques: "), GenericUtils.createGridBagConstraints(2, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtTotalCheques(), GenericUtils.createGridBagConstraints(3, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(new JLabel("Total letras: "), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtTotalLetras(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 0, 0));
		panel.add(new JLabel("Responsable del depósito: "), GenericUtils.createGridBagConstraints(0, 5, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtPersonaResponsable(), GenericUtils.createGridBagConstraints(1, 5, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 0, 0));
		return panel;
	}

	private JPanel getPanelNorte(){
		if(panelNorte == null){
			panelNorte = new JPanel();
			panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelNorte.add(new JLabel("Nº órden: " ));
			panelNorte.add(getTxtNroOrden());
			panelNorte.add(new JLabel("Fecha: "));
			panelNorte.add(getPanelFecha());
			panelNorte.add(new JLabel("Banco: "));
			panelNorte.add(getCmbBancos());
		}
		return panelNorte;
	}
	
	private JPanel getPanelSur() {
		if(panelSur == null){
			panelSur = new JPanel();
			panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnGuardar());
			panelSur.add(getBtnImprimir());
			panelSur.add(getBtnSalir());
		}
		return panelSur;
	}

	private JComboBox getCmbBancos() {
		if(cmbBancos == null){
			cmbBancos = new JComboBox();
			GuiUtil.llenarCombo(cmbBancos, getBancoFacade().getAllOrderByName(), true);
			Banco bancoDefault = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales().getBancoDefault();
			if(bancoDefault!=null){
				cmbBancos.setSelectedItem(bancoDefault);
			}
		}
		return cmbBancos;
	}

	private FWJTextField getTxtCantidadCheques() {
		if(txtCantidadCheques == null){
			txtCantidadCheques = new FWJTextField();
			txtCantidadCheques.setPreferredSize(new Dimension(120, 20));
			txtCantidadCheques.setEditable(false);
		}
		return txtCantidadCheques;
	}

	private FWJTextField getTxtTotalCheques() {
		if(txtTotalCheques == null){
			txtTotalCheques = new FWJTextField();
			txtTotalCheques.setPreferredSize(new Dimension(120,20));
			txtTotalCheques.setEditable(false);
		}
		return txtTotalCheques;
	}

	private FWJTextField getTxtTotalLetras() {
		if(txtTotalLetras == null){
			txtTotalLetras = new FWJTextField();
			txtTotalLetras.setPreferredSize(new Dimension(120, 20));
		}
		return txtTotalLetras;
	}

	private FWJNumericTextField getTxtNroOrden() {
		if(txtNroOrden == null){
			txtNroOrden = new FWJNumericTextField();
			txtNroOrden.setEditable(false);
			txtNroOrden.setPreferredSize(new Dimension(120, 20));
		}
		return txtNroOrden;
	}

	private PanelDatePicker getPanelFecha() {
		if(panelFecha == null){
			panelFecha = new PanelDatePicker();
		}
		return panelFecha;
	}

	private PanelTablaCheques getPanelTablaCheques() {
		if(panelTablaCheques == null){
			panelTablaCheques = new PanelTablaCheques();
		}
		return panelTablaCheques;
	}

	private JButton getBtnGuardar() {
		if(btnGuardar == null){
			btnGuardar = new JButton("Guardar");
			btnGuardar.setEnabled(!isConsulta());
			btnGuardar.setMnemonic(KeyEvent.VK_G);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						OrdenDeDeposito orden = new OrdenDeDeposito();
						orden.setDepositos(modelo.getDepositos());
						orden.setBanco((Banco)getCmbBancos().getSelectedItem());
						orden.setFecha(new java.sql.Date(getPanelFecha().getDate().getTime()));
						orden.setMontoTotal(modelo.getTotalCheques());
						orden.setNroOrden(Integer.valueOf(getTxtNroOrden().getText()));
						orden.setTotalLetras(getTxtTotalLetras().getText().trim());
						orden.setPersonaResponsable(getTxtPersonaResponsable().getText().trim());
						setOrdenFinal(getOrdenFacade().guardarOrden(orden, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName()));
						if(FWJOptionPane.showQuestionMessage(JDialogCargaOrdenDeposito.this, "Desea imprimir?", "Pregunta")==FWJOptionPane.YES_OPTION){
							imprimir();
						}
						dispose();
					}
				}
			});
		}
		return btnGuardar;
	}
	
	private boolean validar(){
		if(modelo.getDepositos()==null || (modelo.getDepositos()!=null && modelo.getDepositos().isEmpty())){
			FWJOptionPane.showErrorMessage(JDialogCargaOrdenDeposito.this, "Debe elegir los cheques", "Error");
			return false;
		}
		if(getCmbBancos().getSelectedItem()==null){
			FWJOptionPane.showErrorMessage(JDialogCargaOrdenDeposito.this, "Debe elegir un banco", "Error");
			return false;
		}
		if(getTxtPersonaResponsable().getText().trim().length()==0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar la persona responsable del depósito", "Error");
			return false;
		}
		if(getPanelFecha().getDate() == null) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar la fecha", "Error");
			return false;
		}
		return true;
	}

	private JButton getBtnImprimir() {
		if(btnImprimir == null){
			btnImprimir = new JButton("Imprimir");
			btnImprimir.setMnemonic(KeyEvent.VK_I);
			btnImprimir.setEnabled(isConsulta());
			btnImprimir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					imprimir();
				}
			});
		}
		return btnImprimir;
	}
	
	private void imprimir(){
		ImprimirOrdenDeDepositoHandler impresionHandler = new ImprimirOrdenDeDepositoHandler(getOrdenFinal(), this);
		impresionHandler.imprimir();
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

	private void salir() {
		if(!isConsulta()){
			if(FWJOptionPane.showQuestionMessage(JDialogCargaOrdenDeposito.this, "Desea salir?", "Pregunta") == FWJOptionPane.YES_OPTION){
				dispose();
			}
		}else{
			dispose();
		}
	}

	private BancoFacadeRemote getBancoFacade() {
		if(bancoFacade == null){
			bancoFacade = GTLBeanFactory.getInstance().getBean2(BancoFacadeRemote.class);
		}
		return bancoFacade;
	}

	private OrdenDeDepositoFacadeRemote getOrdenFacade() {
		if(ordenFacade == null){
			ordenFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeDepositoFacadeRemote.class);
		}
		return ordenFacade;
	}

	public OrdenDeDeposito getOrdenFinal() {
		return ordenFinal;
	}
	
	public void setOrdenFinal(OrdenDeDeposito ordenFinal) {
		this.ordenFinal = ordenFinal;
	}

	public boolean isConsulta() {
		return consulta;
	}
	
	public void setConsulta(boolean consulta) {
		this.consulta = consulta;
	}

	private FWJTextField getTxtPersonaResponsable() {
		if(txtPersonaResponsable == null){
			txtPersonaResponsable = new FWJTextField();
		}
		return txtPersonaResponsable;
	}
	
	private FWJTextField getTxtBusquedaCheques() {
		if (txtBusquedaCheques == null) {
			txtBusquedaCheques = new FWJTextField();
			txtBusquedaCheques.setPreferredSize(new Dimension(120, 20));
			txtBusquedaCheques.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnBuscar().doClick();
				}
			});
		}
		return txtBusquedaCheques;
	}
	

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(getTxtBusquedaCheques().getText().trim().length()>0){
						List<NumeracionCheque> lista = getListaNumeros();
						if(lista == null || (lista!=null && lista.isEmpty())){
							return;
						}
						JDialogBuscarChequesPorNumeroInterno d = new JDialogBuscarChequesPorNumeroInterno(JDialogCargaOrdenDeposito.this, modelo.getIdsYaUtilizados(),lista);
						if(d.isAcepto()){
							for(Cheque c : d.getChequesSeleccionados()){
								DepositoCheque deposito = new DepositoCheque();
								deposito.setCheque(c);
								modelo.getDepositos().add(deposito);
								modelo.getIdsYaUtilizados().add(c.getId());
							}
							actualizar();
							getTxtBusquedaCheques().setText("");
						}
					}else{
						FWJOptionPane.showErrorMessage(JDialogCargaOrdenDeposito.this, "Debe ingresar los números a buscar", "Error");
					}
				}
			});
		}
		return btnBuscar;
	}
	
	private List<NumeracionCheque> getListaNumeros() {
		List<NumeracionCheque> lista = new ArrayList<NumeracionCheque>();
		getTxtBusquedaCheques().setText(getTxtBusquedaCheques().getText().replaceAll("\\s+", " ").trim());
		String[] numeros = getTxtBusquedaCheques().getText().split(" ");
		for(int i = 0; i < numeros.length;i++){
			numeros[i]= numeros[i].replaceAll("\\s+", "").trim();
			if(!numeros[i].matches("[0-9]+\\s{0,1}")){
				FWJOptionPane.showErrorMessage(this, "<html>El número <b>" + numeros[i] + "</b> no tiene el formato correcto<html>", "Error");
				return null;
			}
			if(GenericUtils.esNumerico(numeros[i])){
				NumeracionCheque numeracion = new NumeracionCheque();
				numeracion.setLetra(new Character(paramGrales.getNumeracionCheque().getLetra()));
				numeracion.setNumero(Integer.valueOf(numeros[i]));
				lista.add(numeracion);
			}else{
				FWJOptionPane.showErrorMessage(this, "<html>El número <b>" + numeros[i] + "</b> no tiene el formato correcto<html>", "Error");
				return null;
			}
		}
		return lista;
	}

}
