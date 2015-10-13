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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.InfoDireccion;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.UppercaseDocumentFilterSoloLetras;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.Domicilio;
import ar.com.textillevel.modulos.personal.entidades.legajos.familia.EParentesco;
import ar.com.textillevel.modulos.personal.entidades.legajos.familia.Familiar;

public class JDialogAgregarModificarFamiliar extends JDialog {

	private static final long serialVersionUID = 8007793460330790864L;

	private PanelDomicilioCompleto panelDomicilio;

	private PanelDatePicker panelFechaNacimiento;
	private JComboBox cmbParentesco;
	private FWJNumericTextField txtNroDoc;
	private FWJTextField txtNombre;
	private FWJTextField txtApellido;
	
	private JComboBox cmbListaFamiliares;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private final Frame padre;

	private boolean acepto = false;

	private Familiar familiar;
	private List<Familiar> listaFamiliares;

	public JDialogAgregarModificarFamiliar(Frame padre,List<Familiar> listaParaClonar) {
		super(padre);
		this.padre = padre;
		setListaFamiliares(listaParaClonar);
		setUpComponentes();
		setUpScreen();
		setFamiliar(new Familiar());
	}
	
	public JDialogAgregarModificarFamiliar(Frame padre, List<Familiar> listaParaClonar, Familiar familiar) {
		super(padre);
		this.padre = padre;
		setFamiliar(familiar);
		setListaFamiliares(listaParaClonar);
		setUpComponentes();
		setUpScreen();
		loadData();
	}

	private void loadData() {
		getCmbParentesco().setSelectedItem(getFamiliar().getParentesco());
		getTxtNroDoc().setValue(getFamiliar().getNroDocumento().longValue());
		getTxtNombre().setText(getFamiliar().getNombre());
		getTxtApellido().setText(getFamiliar().getApellido());
		getPanelDomicilio().setDatos(getFamiliar().getDomicilio());		
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Agregar/Modificar familiar");
		setModal(true);
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
		panel.add(new JLabel("Nombre: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNombre(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.5, 0));
		panel.add(new JLabel("Apellido: "),GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtApellido(),GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panel.add(new JLabel("Documento: "),GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNroDoc(),GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panel.add(getPanelFechaNacimiento(),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		panel.add(new JLabel("Parentesco: "),GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbParentesco(),GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panel.add(new JLabel("Copiar domicilio de: "),GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		panel.add(getCmbListaFamiliares(),GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
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

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public PanelDomicilioCompleto getPanelDomicilio() {
		if(panelDomicilio == null){
			panelDomicilio = new PanelDomicilioCompleto(padre);
		}
		return panelDomicilio;
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
		getFamiliar().setNombre(getTxtNombre().getText().trim().toUpperCase());
		getFamiliar().setApellido(getTxtApellido().getText().trim().toUpperCase());
		getFamiliar().setNroDocumento(getTxtNroDoc().getValue());
		getFamiliar().setFechaNacimiento(new java.sql.Date(getPanelFechaNacimiento().getDate().getTime()));
		getFamiliar().setParentesco((EParentesco)getCmbParentesco().getSelectedItem());
		InfoDireccion infoDireccion = getPanelDomicilio().getPanelDomicilio().getPanelDireccion().getInfoDireccion();
		getFamiliar().getDomicilio().setCalle(infoDireccion.getDireccion());
		getFamiliar().getDomicilio().setInfoLocalidad(infoDireccion.getLocalidad());
		getFamiliar().getDomicilio().setDepartamento(getPanelDomicilio().getTxtDto().getText().trim().toUpperCase());
		getFamiliar().getDomicilio().setNumero(getPanelDomicilio().getTxtNumero().getValue());
		getFamiliar().getDomicilio().setPiso(getPanelDomicilio().getTxtPiso().getText().trim().toUpperCase());
		getFamiliar().getDomicilio().setTelefono(getPanelDomicilio().getPanelDomicilio().getPanelTelefono().getDatos());
	}

	private boolean validar() {
		if(getTxtNombre().getText().trim().length()==0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el nombre del familiar", "Error");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getTxtApellido().getText().trim().length()==0){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el apellido del familiar", "Error");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getTxtNroDoc().getValueWithNull() == null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el documento del familiar", "Error");
			getTxtNombre().requestFocus();
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

	public PanelDatePicker getPanelFechaNacimiento() {
		if(panelFechaNacimiento == null){
			panelFechaNacimiento = new PanelDatePicker();
			panelFechaNacimiento.setCaption("Fecha de nacimiento: ");
		}
		return panelFechaNacimiento;
	}

	public JComboBox getCmbParentesco() {
		if(cmbParentesco == null){
			cmbParentesco = new JComboBox();
			GuiUtil.llenarCombo(cmbParentesco, Arrays.asList(EParentesco.values()), true);
		}
		return cmbParentesco;
	}

	public FWJNumericTextField getTxtNroDoc() {
		if(txtNroDoc == null){
			txtNroDoc = new FWJNumericTextField(0l,99999999l);
			txtNroDoc.setColumns(5);
		}
		return txtNroDoc;
	}

	public FWJTextField getTxtNombre() {
		if(txtNombre == null){
			txtNombre = new FWJTextField();
			txtNombre.setPreferredSize(new Dimension(120, 20));
			((AbstractDocument) txtNombre.getDocument()).setDocumentFilter(new UppercaseDocumentFilterSoloLetras());
		}
		return txtNombre;
	}

	public FWJTextField getTxtApellido() {
		if(txtApellido == null){
			txtApellido = new FWJTextField();
			txtApellido.setPreferredSize(new Dimension(120, 20));
			((AbstractDocument) txtApellido.getDocument()).setDocumentFilter(new UppercaseDocumentFilterSoloLetras());
		}
		return txtApellido;
	}
	
	public List<Familiar> getListaFamiliares() {
		return listaFamiliares;
	}

	public void setListaFamiliares(List<Familiar> listaFamiliares) {
		this.listaFamiliares = listaFamiliares;
	}

	public JComboBox getCmbListaFamiliares() {
		if(cmbListaFamiliares == null){
			cmbListaFamiliares = new JComboBox();
			if(getListaFamiliares()!=null && !listaFamiliares.isEmpty()){
				GuiUtil.llenarCombo(cmbListaFamiliares, getListaFamiliares(), true);
				cmbListaFamiliares.setSelectedItem(null);
				cmbListaFamiliares.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED){
							Domicilio dom = ((Familiar)cmbListaFamiliares.getSelectedItem()).getDomicilio();
							getPanelDomicilio().setDatos(dom);
						}
					}
				});
			}else{
				cmbListaFamiliares.setEnabled(false);
			}
		}
		return cmbListaFamiliares;
	}
}
