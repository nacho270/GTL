package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextArea;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class JDialogAgregarValeAnticipo extends JDialog {

	private static final long serialVersionUID = 345900246723654631L;

	private static final int MAX_LONG_CONCEPTO = 500;
	
	private boolean acepto;
	private ValeAnticipo valeAnticipo;
	private Empleado empleado;

	private PanelDatePicker panelFecha;
	private FWJTextField txtNombreEmpleado;
	private FWJTextField txtMonto;
	private FWJTextField txtMontoLetras;
	private FWJTextField txtUsuario;
	private FWJNumericTextField txtNumeroVale;
	private FWJTextArea txtConcepto;
	private JButton btnAceptar;
	private JButton btnCancelar;

	public JDialogAgregarValeAnticipo(Frame padre, Empleado empleado) {
		super(padre);
		setValeAnticipo(new ValeAnticipo());
		setEmpleado(empleado);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Agregar vale de anticipo");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(400, 250));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelNorte(),BorderLayout.NORTH);
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelCentro(){
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Empleado: "),GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNombreEmpleado(),GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
		JScrollPane jsp = new JScrollPane(getTxtConcepto(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		panel.add(new JLabel("Concepto: "),GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(jsp,GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 2, 1, 1));

		panel.add(new JLabel("Monto: "),GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtMonto(),GenericUtils.createGridBagConstraints(1, 3,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panel.add(getTxtMontoLetras(),GenericUtils.createGridBagConstraints(1, 4,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
		return panel;
	}
	
	private JPanel getPanelNorte(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("Nº vale: "));
		panel.add(getTxtNumeroVale());
		panel.add(getPanelFecha());
		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("Usuario: "));
		panel.add(getTxtUsuario());
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

	public ValeAnticipo getValeAnticipo() {
		return valeAnticipo;
	}

	public void setValeAnticipo(ValeAnticipo valeAnticipo) {
		this.valeAnticipo = valeAnticipo;
	}

	public PanelDatePicker getPanelFecha() {
		if (panelFecha == null) {
			panelFecha = new PanelDatePicker();
			panelFecha.setCaption("Fecha: ");
			panelFecha.setSelectedDate(DateUtil.getHoy());
		}
		return panelFecha;
	}

	public FWJTextField getTxtNombreEmpleado() {
		if (txtNombreEmpleado == null) {
			txtNombreEmpleado = new FWJTextField();
			txtNombreEmpleado.setText(getEmpleado().toString());
			txtNombreEmpleado.setEditable(false);
		}
		return txtNombreEmpleado;
	}

	public FWJTextField getTxtMonto() {
		if (txtMonto == null) {
			txtMonto = new FWJTextField();
			txtMonto.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if(GenericUtils.esNumerico(getTxtMonto().getText())){
						getTxtMontoLetras().setText(GenericUtils.convertirNumeroATexto(Double.valueOf(getTxtMonto().getText().replace(',', '.'))));
					}
				}
			});
		}
		return txtMonto;
	}

	public FWJTextField getTxtMontoLetras() {
		if (txtMontoLetras == null) {
			txtMontoLetras = new FWJTextField();
			txtMontoLetras.setEditable(false);
		}
		return txtMontoLetras;
	}

	public FWJTextField getTxtUsuario() {
		if (txtUsuario == null) {
			txtUsuario = new FWJTextField();
			txtUsuario.setEditable(false);
			txtUsuario.setPreferredSize(new Dimension(100, 20));
			txtUsuario.setText(GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
		}
		return txtUsuario;
	}

	public FWJNumericTextField getTxtNumeroVale() {
		if (txtNumeroVale == null) {
			txtNumeroVale = new FWJNumericTextField();
			txtNumeroVale.setEditable(false);
			txtNumeroVale.setColumns(10);
			txtNumeroVale.setHorizontalAlignment(FWJNumericTextField.CENTER);
			Integer proximoNumero = getEmpleado().getLegajo().getUltimoNumeroVale() + 1;
			txtNumeroVale.setValue(proximoNumero.longValue());
		}
		return txtNumeroVale;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
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
	
	private void capturarDatos(){
		getValeAnticipo().setConcepto(getTxtConcepto().getText().trim());
		getValeAnticipo().setFecha(new java.sql.Date(getPanelFecha().getDate().getTime()));
		getValeAnticipo().setLegajo(getEmpleado().getLegajo());
		getValeAnticipo().setMonto(new BigDecimal(Double.valueOf(getTxtMonto().getText().replace(',', '.'))));
		getValeAnticipo().setMontoLetras(getTxtMontoLetras().getText().trim());
		getValeAnticipo().setNroVale(getTxtNumeroVale().getValue());
		getValeAnticipo().setUsuarioLogueado(getTxtUsuario().getText().trim());
		getEmpleado().getLegajo().setUltimoNumeroVale(getEmpleado().getLegajo().getUltimoNumeroVale() + 1);
	}
	
	private boolean validar(){
		if(getPanelFecha().getDate()==null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar una fecha válida", "Error");
			getPanelFecha().requestFocus();
			return false;
		}
		if(getTxtConcepto().getText().trim().length()==0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el concepto", "Error");
			getTxtConcepto().requestFocus();
			return false;
		}
		if(getTxtMonto().getText().trim().length()==0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el monto", "Error");
			getTxtMonto().requestFocus();
			return false;
		}
		if(!GenericUtils.esNumerico(getTxtMonto().getText())){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar un monto válido", "Error");
			getTxtMonto().requestFocus();
			return false;
		}
		return true;
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

	public FWJTextArea getTxtConcepto() {
		if(txtConcepto == null){
			txtConcepto = new FWJTextArea(MAX_LONG_CONCEPTO);
		}
		return txtConcepto;
	}
}
