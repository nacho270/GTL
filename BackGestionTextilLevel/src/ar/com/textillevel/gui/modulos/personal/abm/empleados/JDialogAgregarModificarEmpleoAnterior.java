package ar.com.textillevel.gui.modulos.personal.abm.empleados;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.InfoDireccion;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.DatosEmpleoAnterior;

public class JDialogAgregarModificarEmpleoAnterior extends JDialog {

	private static final long serialVersionUID = -979571680007335043L;

	private PanelDomicilioCompleto panelDomicilio;
	private FWJTextField txtEmpleador;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private FWJTextField txtTareas;
	private JCheckBox chkCertificado;
	private FWJTextField txtReferencia;
	
	private JButton btnAceptar;
	private JButton btnCancelar;

	private final Frame padre;

	private boolean acepto = false;

	private DatosEmpleoAnterior empleo;

	public JDialogAgregarModificarEmpleoAnterior(Frame padre){
		super(padre);
		this.padre = padre;
		setUpComponentes();
		setUpScreen();
		setEmpleo(new DatosEmpleoAnterior());
		getTxtEmpleador().requestFocus();
	}
	
	public JDialogAgregarModificarEmpleoAnterior(Frame padre, DatosEmpleoAnterior empleo){
		super(padre);
		this.padre = padre;
		setEmpleo(empleo);
		setUpComponentes();
		setUpScreen();
		loadData();
		getTxtEmpleador().requestFocus();
	}
	
	private void loadData() {
		getTxtEmpleador().setText(getEmpleo().getNombreEmpleador());
		getTxtTareas().setText(getEmpleo().getTareas());
		getChkCertificado().setSelected(getEmpleo().getCertificado());
		getPanelFechaDesde().setSelectedDate(getEmpleo().getFechaDesde());
		getPanelFechaHasta().setSelectedDate(getEmpleo().getFechaHasta());
		getPanelDomicilio().setDatos(getEmpleo().getDomicilioEmpleador());
		getTxtReferencia().setText(getEmpleo().getReferencia());
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Agregar/Modificar domicilio");
		setModal(true);
		setResizable(false);
		setSize(new Dimension(640, 330));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelNorte(),BorderLayout.NORTH);
		add(getPanelDomicilio(), BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelNorte(){
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Empleador: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtEmpleador(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		panel.add(getPanelFechaDesde(),GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getPanelFechaHasta(),GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(new JLabel("Tareas: "),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtTareas(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		panel.add(getChkCertificado(),GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(new JLabel("Referencia: "),GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtReferencia(),GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public DatosEmpleoAnterior getEmpleo() {
		return empleo;
	}

	public void setEmpleo(DatosEmpleoAnterior empleo) {
		this.empleo = empleo;
	}

	public PanelDomicilioCompleto getPanelDomicilio() {
		if(panelDomicilio == null){
			panelDomicilio = new PanelDomicilioCompleto(padre);
		}
		return panelDomicilio;
	}

	public FWJTextField getTxtEmpleador() {
		if(txtEmpleador == null){
			txtEmpleador = new FWJTextField();
		}
		return txtEmpleador;
	}

	public PanelDatePicker getPanelFechaDesde() {
		if(panelFechaDesde == null){
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Desde: ");
		}
		return panelFechaDesde;
	}

	public PanelDatePicker getPanelFechaHasta() {
		if(panelFechaHasta == null){
			panelFechaHasta = new PanelDatePicker();
			panelFechaHasta.setCaption("Hasta: ");
		}
		return panelFechaHasta;
	}

	public FWJTextField getTxtTareas() {
		if(txtTareas == null){
			txtTareas = new FWJTextField();
		}
		return txtTareas;
	}

	public JCheckBox getChkCertificado() {
		if(chkCertificado == null){
			chkCertificado = new JCheckBox("Certificado");
		}
		return chkCertificado;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						capturarDatos();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}
	
	private void capturarDatos() {
		getEmpleo().setCertificado(getChkCertificado().isSelected());
		getEmpleo().setFechaDesde(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
		getEmpleo().setFechaHasta(new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
		getEmpleo().setNombreEmpleador(getTxtEmpleador().getText().trim().toUpperCase());
		getEmpleo().setReferencia(getTxtReferencia().getText().trim().toUpperCase());
		getEmpleo().setTareas(getTxtTareas().getText().trim().toUpperCase());
		InfoDireccion infoDireccion = getPanelDomicilio().getPanelDomicilio().getPanelDireccion().getInfoDireccion();
		getEmpleo().getDomicilioEmpleador().setCalle(infoDireccion.getDireccion());
		getEmpleo().getDomicilioEmpleador().setInfoLocalidad(infoDireccion.getLocalidad());
		getEmpleo().getDomicilioEmpleador().setDepartamento(getPanelDomicilio().getTxtDto().getText().trim().toUpperCase());
		getEmpleo().getDomicilioEmpleador().setNumero(getPanelDomicilio().getTxtNumero().getValue());
		getEmpleo().getDomicilioEmpleador().setPiso(getPanelDomicilio().getTxtPiso().getText().trim().toUpperCase());
		getEmpleo().getDomicilioEmpleador().setTelefono(getPanelDomicilio().getPanelDomicilio().getPanelTelefono().getDatos());
	}

	private boolean validar() {
		if(getTxtEmpleador().getText().trim().length()==0){
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre del empleador", "Error");
			getTxtEmpleador().requestFocus();
			return false;
		}
		if(getPanelFechaDesde().getDate() == null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar una 'Fecha desde' válida", "Error");
			getPanelFechaDesde().requestFocus();
			return false;
		}
		if(getPanelFechaHasta().getDate() == null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar una 'Fecha hasta' válida", "Error");
			getPanelFechaHasta().requestFocus();
			return false;
		}
		if(getTxtReferencia().getText().trim().length()==0){
			FWJOptionPane.showErrorMessage(this, "Debe completar la referencia", "Error");
			getTxtReferencia().requestFocus();
			return false;
		}
		return getPanelDomicilio().validar();
	}

	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnCancelar;
	}
	
	public FWJTextField getTxtReferencia() {
		if(txtReferencia == null){
			txtReferencia = new FWJTextField();
		}
		return txtReferencia;
	}
}
