package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

public class JDialogAgregarModificarMateriaPrimaCantidad<F extends Formulable, T extends MateriaPrimaCantidad<F>> extends JDialog {

	private static final long serialVersionUID = -7227181759998277438L;

	private JComboBox cmbMateriasPrimas;
	private FWJTextField txtCantidad;
	private JComboBox cmbUnidad;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private List<F> matPrimasYaAgregadas;
	private List<F> allMatPrimasPosibles;
	private T matPrimaCantidadActual;
	private boolean acepto;
	private String descripcionTipoMateriaPrima;
	private boolean modoConsulta;

	public JDialogAgregarModificarMateriaPrimaCantidad(Dialog padre, String descripcionTipoMateriaPrima, List<F> allMatPrimasPosibles, List<F> matPrimasYaAgregadas, T mpc, boolean modoConsulta) {
		super(padre);
		this.modoConsulta = modoConsulta;
		this.descripcionTipoMateriaPrima = descripcionTipoMateriaPrima;
		this.allMatPrimasPosibles = allMatPrimasPosibles;
		this.matPrimasYaAgregadas = matPrimasYaAgregadas; 
		setMatPrimaCantidadActual(mpc);
		setUpScreen();
		setUpComponentes();
		loadData();
	}

	private void loadData() {
		getCmbMateriasPrimas().setSelectedItem(getMatPrimaCantidadActual().getMateriaPrima());
		getTxtCantidad().setText(GenericUtils.getDecimalFormat2().format(getMatPrimaCantidadActual().getCantidad()));
		getCmbUnidad().setSelectedItem(getMatPrimaCantidadActual().getUnidad());
	}
	
	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getPanelNorte(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelNorte(){
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel(descripcionTipoMateriaPrima + ": "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbMateriasPrimas(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
		panel.add(new JLabel("Cantidad: "),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtCantidad(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
		panel.add(new JLabel("Unidad: "),GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbUnidad(),GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

		return panel;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	private void setUpScreen() {
		setTitle("Alta/modificación de " + descripcionTipoMateriaPrima);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(300, 150));
		GuiUtil.centrar(this);
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
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
			btnAceptar.setVisible(!modoConsulta);
		}
		return btnAceptar;
	}

	@SuppressWarnings("unchecked")
	private void capturarDatos() {
		getMatPrimaCantidadActual().setCantidad(Float.valueOf(getTxtCantidad().getText().replace(",", ".")));
		getMatPrimaCantidadActual().setMateriaPrima((F)getCmbMateriasPrimas().getSelectedItem());
		getMatPrimaCantidadActual().setUnidad((EUnidad)getCmbUnidad().getSelectedItem());
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtCantidad().getText())){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar la cantidad", "Error");
			getTxtCantidad().requestFocus();
			return false;
		}
		if(!GenericUtils.esNumerico(getTxtCantidad().getText())){
			FWJOptionPane.showErrorMessage(this, "La cantidad debe ser numérica", "Error");
			getTxtCantidad().requestFocus();
			return false;
		}
		try{
			Float cantidad = Float.valueOf(getTxtCantidad().getText().replace(",", "."));
			if(cantidad<=0){
				FWJOptionPane.showErrorMessage(this, "La cantidad debe ser mayor a 0", "Error");
				getTxtCantidad().requestFocus();
				return false;
			}
		}catch(NumberFormatException nfe){
			FWJOptionPane.showErrorMessage(this, "Error en el formato de la cantidad", "Error");
			getTxtCantidad().requestFocus();
			return false;
		}
		if(getCmbUnidad().getSelectedItem() == null) {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar la unidad.", "Error");
			return false;
		}
		return true;
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton(modoConsulta ? "Cerrar" : "Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	private void salir() {
		if (!modoConsulta && FWJOptionPane.showQuestionMessage(JDialogAgregarModificarMateriaPrimaCantidad.this, "Va a salir sin grabar los cambios. Esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		} else {
			setAcepto(false);
			dispose();
		}
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public T getMatPrimaCantidadActual() {
		return matPrimaCantidadActual;
	}

	public void setMatPrimaCantidadActual(T quimicoCantidadActual) {
		this.matPrimaCantidadActual = quimicoCantidadActual;
	}

	@SuppressWarnings("unchecked")
	private JComboBox getCmbMateriasPrimas() {
		if(cmbMateriasPrimas == null){
			cmbMateriasPrimas = new JComboBox();
			CollectionUtils.filter(allMatPrimasPosibles, new Predicate() {
				public boolean evaluate(Object arg0) {
					F formulable = (F)arg0;
					if(formulable.equals(getMatPrimaCantidadActual().getMateriaPrima())){
						return true;
					}
					return !getMatPrimasAgregadas().contains(formulable);
				}
			});
			GuiUtil.llenarCombo(cmbMateriasPrimas, allMatPrimasPosibles, true);
			cmbMateriasPrimas.setEnabled(!modoConsulta);
		}
		return cmbMateriasPrimas;
	}

	private JComboBox getCmbUnidad() {
		if(cmbUnidad == null){
			cmbUnidad = new JComboBox();
			List<EUnidad> unidades = new ArrayList<EUnidad>();
			unidades.add(EUnidad.GRAMOS_POR_KILOS);
			unidades.add(EUnidad.GRAMOS_POR_LITROS);
			unidades.add(EUnidad.PORCENTAJE);
			GuiUtil.llenarCombo(cmbUnidad, unidades, true);
			cmbUnidad.setSelectedIndex(-1);
			cmbUnidad.setEnabled(!modoConsulta);
		}
		return cmbUnidad;
	}

	private FWJTextField getTxtCantidad() {
		if(txtCantidad==null){
			txtCantidad = new FWJTextField();
			txtCantidad.setEnabled(!modoConsulta);
		}
		return txtCantidad;
	}

	private List<F> getMatPrimasAgregadas() {
		return matPrimasYaAgregadas;
	}

}