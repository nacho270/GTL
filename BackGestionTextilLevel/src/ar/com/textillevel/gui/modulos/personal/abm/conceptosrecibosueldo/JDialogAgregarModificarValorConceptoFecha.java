package ar.com.textillevel.gui.modulos.personal.abm.conceptosrecibosueldo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ValorConceptoFecha;

public class JDialogAgregarModificarValorConceptoFecha extends JDialog {

	private static final long serialVersionUID = 2710204572164592189L;

	private ButtonGroup grupoRadios;
	private JRadioButton radioValorNumerico;
	private JRadioButton radioValorPorcentual;
	private FWJTextField txtValor;

	private PanelDatePicker panelFecha;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private ValorConceptoFecha valorActual;

	private boolean acepto;
	
	private List<ValorConceptoFecha> valoresYaElegidos;

	public JDialogAgregarModificarValorConceptoFecha(Frame padre, List<ValorConceptoFecha> valoresElegidos) {
		super(padre);
		setValorActual(new ValorConceptoFecha());
		setValoresYaElegidos(valoresElegidos);
		setUpComponentes();
		setUpScreen();
		setAcepto(false);
	}

	public JDialogAgregarModificarValorConceptoFecha(Frame padre, ValorConceptoFecha valorConcepto, List<ValorConceptoFecha> valoresElegidos) {
		super(padre);
		setValorActual(valorConcepto);
		setValoresYaElegidos(valoresElegidos);
		setUpComponentes();
		setUpScreen();
		setAcepto(false);
		loadData();
	}

	private void setUpComponentes() {
		add(getPanelCentro(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private JPanel getPanelCentro() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(getPanelFecha(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 3, 1, 1, 0));
		crearGrupoRadios();
		JPanel panelRadios = new JPanel(new GridBagLayout());
		panelRadios.setBorder(BorderFactory.createTitledBorder("Valor"));
		panelRadios.add(getRadioValorNumerico(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panelRadios.add(getRadioValorPorcentual(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panelRadios.add(new JLabel("Valor: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panelRadios.add(getTxtValor(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));

		panel.add(panelRadios, GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));

		return panel;
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Agregar/modificar valor concepto");
		setModal(true);
		setSize(new Dimension(300, 250));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void loadData() {
		getPanelFecha().setSelectedDate(getValorActual().getFechaDesde());
		if(getValorActual().getValorNumerico() != null){
			getRadioValorNumerico().setSelected(true);
			getTxtValor().setText(GenericUtils.getDecimalFormat().format(getValorActual().getValorNumerico()));
		}else{
			getRadioValorPorcentual().setSelected(true);
			getTxtValor().setText(GenericUtils.getDecimalFormat().format(getValorActual().getValorPorcentual()));
		}
	}

	public ValorConceptoFecha getValorActual() {
		return valorActual;
	}

	public void setValorActual(ValorConceptoFecha valorActual) {
		this.valorActual = valorActual;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public ButtonGroup crearGrupoRadios() {
		if (grupoRadios == null) {
			grupoRadios = new ButtonGroup();
			grupoRadios.add(getRadioValorPorcentual());
			grupoRadios.add(getRadioValorNumerico());
		}
		return grupoRadios;
	}

	public JRadioButton getRadioValorNumerico() {
		if (radioValorNumerico == null) {
			radioValorNumerico = new JRadioButton("Valor numérico", true);
		}
		return radioValorNumerico;
	}

	public JRadioButton getRadioValorPorcentual() {
		if (radioValorPorcentual == null) {
			radioValorPorcentual = new JRadioButton("Valor porcentual", false);
		}
		return radioValorPorcentual;
	}

	public PanelDatePicker getPanelFecha() {
		if (panelFecha == null) {
			panelFecha = new PanelDatePicker();
			panelFecha.setCaption("Válido desde: ");
			panelFecha.setSelectedDate(DateUtil.getHoy());
		}
		return panelFecha;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()){
						getValorActual().setFechaDesde(new java.sql.Date(getPanelFecha().getDate().getTime()));
						if(getRadioValorNumerico().isSelected()){
							getValorActual().setValorNumerico(new BigDecimal(GenericUtils.getDoubleValueInJTextField(getTxtValor())));
						}else{
							getValorActual().setValorPorcentual(new BigDecimal(GenericUtils.getDoubleValueInJTextField(getTxtValor())));
						}
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}
	
	private boolean validar(){
		if(getPanelFecha().getDate() == null){
			FWJOptionPane.showErrorMessage(this, "Debe elegir la fecha de validez", "Error");
			getPanelFecha().requestFocus();
			return false;
		}
		if(getTxtValor().getText().trim().length()==0){
			FWJOptionPane.showErrorMessage(this, "Debe elegir el valor", "Error");
			getTxtValor().requestFocus();
			return false;
		}
		if(!GenericUtils.esNumerico(getTxtValor().getText().trim())){
			FWJOptionPane.showErrorMessage(this, "El valor ingresado no es numérico", "Error");
			getTxtValor().requestFocus();
			return false;
		}
		for(ValorConceptoFecha valor : getValoresYaElegidos()){
			Date fecha = getPanelFecha().getDate();
			if(!fecha.after(valor.getFechaDesde())){
				FWJOptionPane.showErrorMessage(this, "Debe elegir un valor posterior a " + DateUtil.dateToString(valor.getFechaDesde()), "Error");
				return false;
			}
		}
		return true;
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
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

	public FWJTextField getTxtValor() {
		if (txtValor == null) {
			txtValor = new FWJTextField();
		}
		return txtValor;
	}

	
	public List<ValorConceptoFecha> getValoresYaElegidos() {
		return valoresYaElegidos;
	}

	
	public void setValoresYaElegidos(List<ValorConceptoFecha> valoresYaElegidos) {
		this.valoresYaElegidos = valoresYaElegidos;
	}
}
