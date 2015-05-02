package ar.com.textillevel.gui.modulos.personal.abm.presentismo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismoPorAusencia;

public class JDialogAgregarModificarDescuentoPorFalta extends JDialog {

	private static final long serialVersionUID = 683957088160450192L;

	private CLJNumericTextField txtCantidadFaltas;
	private CLJTextField txtPorcentajeDescuento;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentro;
	private JPanel panelSur;

	private List<DescuentoPresentismoPorAusencia> descuentosExistentes;
	private DescuentoPresentismoPorAusencia descuentoActual;
	private boolean acepto;
	
	public JDialogAgregarModificarDescuentoPorFalta(Frame padre, List<DescuentoPresentismoPorAusencia> descuentosYaCargados) {
		super(padre);
		setDescuentosExistentes(descuentosYaCargados);
		setDescuentoActual(new DescuentoPresentismoPorAusencia());
		setUpComponentes();
		setUpScreen();
	}
	
	public JDialogAgregarModificarDescuentoPorFalta(Frame padre, List<DescuentoPresentismoPorAusencia> descuentosYaCargados, DescuentoPresentismoPorAusencia descuentoElegido) {
		super(padre);
		setDescuentosExistentes(descuentosYaCargados);
		setDescuentoActual(descuentoElegido);
		setUpComponentes();
		setUpScreen();
		loadData();
	}

	private void loadData() {
		getTxtCantidadFaltas().setValue(getDescuentoActual().getCantidadFaltas().longValue());
		getTxtPorcentajeDescuento().setText(GenericUtils.getDecimalFormat().format(getDescuentoActual().getPorcentajeDescuento()));
	}

	private void setUpScreen() {
		setTitle("Agregar/modificar descuento presentismo por falta");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		pack();
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setAcepto(false);
				dispose();
			}
		});
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	public CLJNumericTextField getTxtCantidadFaltas() {
		if(txtCantidadFaltas == null){
			txtCantidadFaltas = new CLJNumericTextField();
			txtCantidadFaltas.setPreferredSize(new Dimension(120, 20));
		}
		return txtCantidadFaltas;
	}

	public CLJTextField getTxtPorcentajeDescuento() {
		if(txtPorcentajeDescuento == null){
			txtPorcentajeDescuento = new CLJTextField();
			txtPorcentajeDescuento.setPreferredSize(new Dimension(120, 20));
		}
		return txtPorcentajeDescuento;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						getDescuentoActual().setCantidadFaltas(getTxtCantidadFaltas().getValue());
						getDescuentoActual().setPorcentajeDescuento(new BigDecimal(GenericUtils.getDoubleValue(getTxtPorcentajeDescuento().getText())));
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}
	
	private boolean validar(){
		if(getTxtCantidadFaltas().getValueWithNull()==null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la cantidad de faltas", "Error");
			getTxtCantidadFaltas().requestFocus();
			return false;
		}
		if(getTxtPorcentajeDescuento().getText().trim().length()==0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el porcentaje de descuento", "Error");
			getTxtPorcentajeDescuento().requestFocus();
			return false;
		}
		if(!GenericUtils.esNumerico(getTxtPorcentajeDescuento().getText())){
			CLJOptionPane.showErrorMessage(this, "El valor del porcentaje de descuento debe ser numérico", "Error");
			getTxtPorcentajeDescuento().requestFocus();
			return false;
		}
		if(getDescuentosExistentes()!=null && !getDescuentosExistentes().isEmpty()){
			for(DescuentoPresentismoPorAusencia d : getDescuentosExistentes()){
				if(d.getCantidadFaltas().equals(getTxtCantidadFaltas().getValue()) && !d.equals(getDescuentoActual())){
					CLJOptionPane.showErrorMessage(this, "Ya ha ingresa el porcentaje de descuento para " + getTxtCantidadFaltas().getValue() + " falta/s.", "Error");
					getTxtCantidadFaltas().requestFocus();
					return false;
				}
			}
		}
		return true;
	}

	public JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public List<DescuentoPresentismoPorAusencia> getDescuentosExistentes() {
		return descuentosExistentes;
	}

	public void setDescuentosExistentes(List<DescuentoPresentismoPorAusencia> descuentosExistentes) {
		this.descuentosExistentes = descuentosExistentes;
	}

	public JPanel getPanelCentro() {
		if(panelCentro == null){
			panelCentro = new JPanel(new GridBagLayout());
			panelCentro.add(new JLabel("Faltas: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtCantidadFaltas(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelCentro.add(new JLabel("Descuento (%): "),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtPorcentajeDescuento(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		}
		return panelCentro;
	}

	public JPanel getPanelSur() {
		if(panelSur == null){
			panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnAceptar());
			panelSur.add(getBtnSalir());
		}
		return panelSur;
	}

	
	public boolean isAcepto() {
		return acepto;
	}

	
	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	
	public DescuentoPresentismoPorAusencia getDescuentoActual() {
		return descuentoActual;
	}

	
	public void setDescuentoActual(DescuentoPresentismoPorAusencia descuentoActual) {
		this.descuentoActual = descuentoActual;
	}
}