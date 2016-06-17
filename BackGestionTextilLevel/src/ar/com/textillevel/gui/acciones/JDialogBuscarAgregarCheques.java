package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWCheckBoxList;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVentaDiferida;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogBuscarAgregarCheques extends JDialog {

	private static final long serialVersionUID = -5396796400215355847L;

	private FWCheckBoxList<Cheque> chkListCheques;
	private JCheckBox chkSeleccionarTodos;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private FWJTextField txtImporteDesde;
	private FWJTextField txtImporteHasta;
	private FWJTextField txtNumeroCheque;
	private FWJTextField txtNumeroInternoCheque;

	private JButton btnAceptar;
	private JButton btnCancelar;
	private JButton btnBuscar;

	private JPanel pnlCentral;
	private JPanel pnlSur;
	private JPanel pnlNorte;

	private ChequeFacadeRemote chequeFacade;

	private CondicionDeVenta condicion;
	private List<Cheque> chequesSeleccionados;
	private List<Cheque> chequesExcluidos;
	private boolean acepto;
	
	private boolean todosSeleccionados;
	
	private final ParametrosGenerales paramGrales = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales();
	
	public JDialogBuscarAgregarCheques(Dialog owner, CondicionDeVenta condicion, List<Cheque> excluidos) {
		super(owner);
		setCondicion(condicion);
		setChequesExcluidos(excluidos);
		chequesSeleccionados = new ArrayList<Cheque>();
		acepto=false;
		setTodosSeleccionados(false);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Buscar cheque");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 550));
		setModal(true);
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		this.add(getPnlNorte(),BorderLayout.NORTH);
		this.add(getPnlCentral(),BorderLayout.CENTER);
		this.add(getPnlSur(),BorderLayout.SOUTH);
	}

	private FWCheckBoxList<Cheque> getChkListCheques() {
		if (chkListCheques == null) {
			chkListCheques = new FWCheckBoxList<Cheque>(){

				private static final long serialVersionUID = 4501839806754196510L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if (seleccionado) {
						Cheque prod = (Cheque) item;
						if (!getChequesSeleccionados().contains(prod)) {
							getChequesSeleccionados().add(prod);
						}
					} else {
						getChequesSeleccionados().remove(item);
					}
				}
			};
		}
		return chkListCheques;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						acepto = true;
						dispose();
					}
				}

				private boolean validar() {
					if (getChkListCheques().getSelectedValues().length == 0) {
						FWJOptionPane.showErrorMessage(JDialogBuscarAgregarCheques.this, "Debe seleccionar al menos un cheque.", JDialogBuscarAgregarCheques.this.getTitle());
						return false;
					}
					return true;
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					acepto=false;
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker();
			if(getCondicion() instanceof CondicionDeVentaDiferida){
				panelFechaDesde.setSelectedDate(DateUtil.sumarDias(DateUtil.getHoy(), ((CondicionDeVentaDiferida)getCondicion()).getDiasIniciales()));
			}else{
				panelFechaDesde.setSelectedDate(DateUtil.getHoy());
			}
		}
		return panelFechaDesde;
	}

	private PanelDatePicker getPanelFechaHasta() {
		if (panelFechaHasta == null) {
			panelFechaHasta = new PanelDatePicker();
			if(getCondicion() instanceof CondicionDeVentaDiferida){
				panelFechaHasta.setSelectedDate(DateUtil.sumarDias(DateUtil.getHoy(), ((CondicionDeVentaDiferida)getCondicion()).getDiasFinales()));
			}else{
				panelFechaHasta.setSelectedDate(DateUtil.sumarDias(DateUtil.getHoy(),30));
			}
		}
		return panelFechaHasta;
	}

	public FWJTextField getTxtImporteDesde() {
		if (txtImporteDesde == null) {
			txtImporteDesde = new FWJTextField();
			txtImporteDesde.setPreferredSize(new Dimension(120,20));
		}
		return txtImporteDesde;
	}

	private FWJTextField getTxtImporteHasta() {
		if (txtImporteHasta == null) {
			txtImporteHasta = new FWJTextField();
			txtImporteHasta.setPreferredSize(new Dimension(120,20));
		}
		return txtImporteHasta;
	}

	private FWJTextField getTxtNumeroCheque() {
		if (txtNumeroCheque == null) {
			txtNumeroCheque = new FWJTextField();
			txtNumeroCheque.setPreferredSize(new Dimension(120,20));
		}
		return txtNumeroCheque;
	}

	public FWJTextField getTxtNumeroInternoCheque() {
		if (txtNumeroInternoCheque == null) {
			txtNumeroInternoCheque = new FWJTextField();
			txtNumeroInternoCheque.setPreferredSize(new Dimension(120,20));
		}
		return txtNumeroInternoCheque;
	}

	private JPanel getPnlCentral() {
		if (pnlCentral == null) {
			pnlCentral = new JPanel();
			JScrollPane jsp = new JScrollPane(getChkListCheques(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jsp.setPreferredSize(new Dimension(380, 200));
			pnlCentral.add(jsp, BorderLayout.CENTER);
		}
		return pnlCentral;
	}

	private JPanel getPnlSur() {
		if (pnlSur == null) {
			pnlSur = new JPanel();
			pnlSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			pnlSur.add(getChkSeleccionarTodos());
			pnlSur.add(getBtnAceptar());
			pnlSur.add(getBtnCancelar());
		}
		return pnlSur;
	}

	private JPanel getPnlNorte() {
		if (pnlNorte == null) {
			pnlNorte = new JPanel();
			pnlNorte.setLayout(new GridBagLayout());
			pnlNorte.add(new JLabel("Fecha depósito desde: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 10, 0, 5), 1, 1, 0, 0));
			pnlNorte.add(getPanelFechaDesde(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 2, 1, 1, 0));
			pnlNorte.add(new JLabel("Fecha depósito hasta: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
			pnlNorte.add(getPanelFechaHasta(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 2, 1, 1, 0));
			
			pnlNorte.add(new JLabel("Importe desde: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
			pnlNorte.add(getTxtImporteDesde(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 1, 1, 1, 0));
			pnlNorte.add(new JLabel("Importe hasta: "), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 0, 5), 1, 1, 0, 0));
			pnlNorte.add(getTxtImporteHasta(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 5), 1, 1, 1, 0));
			JLabel lblIndicaciones = new JLabel("<html>Los números deben estar separados por un espacio <br> y no deben contener la letra</html>");
			lblIndicaciones.setFont(new Font(lblIndicaciones.getFont().getName(), Font.BOLD, 12));
			pnlNorte.add(new JLabel("Númeracion interna: "), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlNorte.add(getTxtNumeroInternoCheque(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			pnlNorte.add(lblIndicaciones, GenericUtils.createGridBagConstraints(0, 5, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			pnlNorte.add(new JLabel("Número de cheque: "), GenericUtils.createGridBagConstraints(0, 6, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlNorte.add(getTxtNumeroCheque(), GenericUtils.createGridBagConstraints(1,6, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			
			pnlNorte.add(getBtnBuscar(), GenericUtils.createGridBagConstraints(1, 7, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return pnlNorte;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					buscar();
				}
			});
		}
		return btnBuscar;
	}

	private ChequeFacadeRemote getChequeFacade() {
		if (chequeFacade == null) {
			chequeFacade = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
		}
		return chequeFacade;
	}

	public CondicionDeVenta getCondicion() {
		return condicion;
	}

	public void setCondicion(CondicionDeVenta condicion) {
		this.condicion = condicion;
	}
	
	private void buscar(){
		Date fechaInicial = panelFechaDesde.getDate();
		Date fechaFinal = panelFechaHasta.getDate();
		BigDecimal importeInicial = null;
		BigDecimal importeFinal = null;
		String numeracionInterna = null;
		String numeroCheque = null;
		
		if(getTxtImporteDesde().getText().trim().length()!=0){
			if(!GenericUtils.esNumerico(getTxtImporteDesde().getText().trim())){
				FWJOptionPane.showErrorMessage(this, "El campo debe ser numérico", "Error");
				getTxtImporteDesde().requestFocus();
				return;
			}
			importeInicial = new BigDecimal(Double.valueOf(getTxtImporteDesde().getText().trim()));
		}
		
		if(getTxtImporteHasta().getText().trim().length()>0){
			if(!GenericUtils.esNumerico(getTxtImporteHasta().getText().trim())){
				FWJOptionPane.showErrorMessage(this, "El campo debe ser numérico", "Error");
				getTxtImporteHasta().requestFocus();
				return;
			}
			importeFinal = new BigDecimal(Double.valueOf(getTxtImporteHasta().getText().trim()));
		}
		
		if(getTxtNumeroInternoCheque().getText().trim().length()>0){
			if(getTxtNumeroInternoCheque().getText().trim().length()<2){
				FWJOptionPane.showErrorMessage(JDialogBuscarAgregarCheques.this, "La numeración consta de al menos 2 caracteres", "Error");
				return;
			}
//			if(!getTxtNumeroInternoCheque().getText().trim().matches("[a-zA-Z]{1}[0-9]+")){
//				CLJOptionPane.showErrorMessage(JDialogBuscarAgregarCheques.this, "el formato de numeración no es correcto", "Error");
//				return;
//			}
			numeracionInterna = getTxtNumeroInternoCheque().getText().trim();
		}
		
		if(getTxtNumeroCheque().getText().trim().length()>0){
			if(!GenericUtils.esNumerico(getTxtNumeroCheque().getText().trim())){
				FWJOptionPane.showErrorMessage(this, "El campo debe ser numérico", "Error");
				getTxtNumeroCheque().requestFocus();
				return;
			}
			numeroCheque = getTxtNumeroCheque().getText().trim();
		}
		java.sql.Date fechaInicialSQL = null;
		if(fechaInicial!=null){
			fechaInicialSQL = new java.sql.Date(fechaInicial.getTime());
		}
		java.sql.Date fechaFinalSQL = null;
		if(fechaFinal!=null){
			fechaFinalSQL = new java.sql.Date(fechaFinal.getTime());
		}
		List<NumeracionCheque> listaNumerosInternos = null;
		if(numeracionInterna!=null || numeroCheque!=null){ //si pone un numero especifico, no busco por fecha
			fechaInicialSQL = null;
			fechaFinalSQL = null;
			listaNumerosInternos = getListaNumeros();
			if(listaNumerosInternos==null){
				return;
			}
		}
		List<Cheque> lista = getChequeFacade().getChequesPorNumeracionNumeroFechaEImporte(listaNumerosInternos, numeroCheque,fechaInicialSQL,fechaFinalSQL,importeInicial, importeFinal,getChequesExcluidos());
		List<Cheque> lista2 = new ArrayList<Cheque>();
		if(numeracionInterna!=null || numeroCheque!=null){
			if(lista == null || (lista!=null && lista.isEmpty())){
				FWJOptionPane.showWarningMessage(this, "No se han encontrado resultados", "Búsqueda de cheques");
				return;
			}else{
				for(Cheque cheque : lista ){
					if(cheque.getEstadoCheque() == EEstadoCheque.EN_CARTERA){
						lista2.add(cheque);
					}else{
						FWJOptionPane.showWarningMessage(this, StringW.wordWrap("El cheque " + cheque.getNumeracion() + " no se puede utilizar debido a que tiene estado: " + cheque.getEstadoCheque()), "Búsqueda de cheques");
					}
				}
				getChkListCheques().setValues(lista2.toArray());
			}
		}else if(lista == null || (lista!=null && lista.isEmpty())){
			FWJOptionPane.showWarningMessage(this, "No se han encontrado resultados", "Búsqueda de cheques");
			return;
		}else{
			getChkListCheques().setValues(lista.toArray());
		}
	}

	private List<NumeracionCheque> getListaNumeros() {
		List<NumeracionCheque> lista = new ArrayList<NumeracionCheque>();
		getTxtNumeroInternoCheque().setText(getTxtNumeroInternoCheque().getText().replaceAll("\\s+", " ").trim());
		String[] numeros = getTxtNumeroInternoCheque().getText().split(" ");
		for(int i = 0; i < numeros.length;i++){
//			numeros[i]= numeros[i].replaceAll("\\s+", "").trim();
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
	
	public List<Cheque> getChequesSeleccionados() {
		return chequesSeleccionados;
	}
	
	public void setChequesSeleccionados(List<Cheque> chequesSeleccionados) {
		this.chequesSeleccionados = chequesSeleccionados;
	}

	
	public boolean isAcepto() {
		return acepto;
	}
	
	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
	
	public List<Cheque> getChequesExcluidos() {
		return chequesExcluidos;
	}
	
	public void setChequesExcluidos(List<Cheque> chequesExcluidos) {
		this.chequesExcluidos = chequesExcluidos;
	}

	private JCheckBox getChkSeleccionarTodos() {
		if(chkSeleccionarTodos == null){
			chkSeleccionarTodos = new JCheckBox("Seleccionar Todos");
			chkSeleccionarTodos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!isTodosSeleccionados()){
						getChkListCheques().setAllSelectedItems(true);
						for(int i = 0;i<getChkListCheques().getSelectedCount();i++){
							Cheque cheque =(Cheque) getChkListCheques().getItemAt(i);
							if(!getChequesSeleccionados().contains(cheque)){
								getChequesSeleccionados().add(cheque);
							}
						}
					}else{
						getChkListCheques().setAllSelectedItems(false);
						getChequesSeleccionados().clear();
					}
				}
			});
		}
		return chkSeleccionarTodos;
	}

	public boolean isTodosSeleccionados() {
		return todosSeleccionados;
	}
	
	public void setTodosSeleccionados(boolean todosSeleccionados) {
		this.todosSeleccionados = todosSeleccionados;
	}
}
