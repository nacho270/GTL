package ar.com.textillevel.gui.modulos.odt.gui.procedimientos;

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
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarQuimicoCantidad extends JDialog {

	private static final long serialVersionUID = -7227181759998277438L;

	private JComboBox cmbQuimicos;
	private FWJTextField txtCantidad;
	private JComboBox cmbUnidad;
	
	private JButton btnAceptar;
	private JButton btnCancelar;

	private List<Quimico> quimicosAgregados;
	private QuimicoCantidad quimicoCantidadActual;
	private boolean acepto;

	public JDialogAgregarModificarQuimicoCantidad(Dialog padre, List<Quimico> quimicosAgregados) {
		super(padre);
		setQuimicosAgregados(quimicosAgregados);
		setQuimicoCantidadActual(new QuimicoCantidad());
		setUpScreen();
		setUpComponentes();
	}
	
	public JDialogAgregarModificarQuimicoCantidad(Dialog padre,List<Quimico> quimicosAgregados, QuimicoCantidad qc) {
		super(padre);
		setQuimicosAgregados(quimicosAgregados);
		setQuimicoCantidadActual(qc);
		setUpScreen();
		setUpComponentes();
		loadData();
	}
	
	private void loadData() {
		getCmbQuimicos().setSelectedItem(getQuimicoCantidadActual().getMateriaPrima());
		getTxtCantidad().setText(GenericUtils.getDecimalFormat2().format(getQuimicoCantidadActual().getCantidad()));
		getCmbUnidad().setSelectedItem(getQuimicoCantidadActual().getUnidad());
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
		panel.add(new JLabel("Quimico: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbQuimicos(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		
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
		setTitle("Alta/modificación de instrucciones");
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
		}
		return btnAceptar;
	}

	private void capturarDatos() {
		getQuimicoCantidadActual().setCantidad(Float.valueOf(getTxtCantidad().getText().replace(",", ".")));
		getQuimicoCantidadActual().setMateriaPrima((Quimico)getCmbQuimicos().getSelectedItem());
		getQuimicoCantidadActual().setUnidad((EUnidad)getCmbUnidad().getSelectedItem());
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
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	private void salir() {
		if (FWJOptionPane.showQuestionMessage(JDialogAgregarModificarQuimicoCantidad.this, "Va a salir sin grabar los cambios. Esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION) {
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

	public QuimicoCantidad getQuimicoCantidadActual() {
		return quimicoCantidadActual;
	}

	public void setQuimicoCantidadActual(QuimicoCantidad quimicoCantidadActual) {
		this.quimicoCantidadActual = quimicoCantidadActual;
	}

	public JComboBox getCmbQuimicos() {
		if(cmbQuimicos == null){
			cmbQuimicos = new JComboBox();
			List<MateriaPrima> quimicos = GTLBeanFactory.getInstance().getBean2(MateriaPrimaFacadeRemote.class).getAllOrderByTipos(false, ETipoMateriaPrima.QUIMICO);
			CollectionUtils.filter(quimicos, new Predicate() {
				public boolean evaluate(Object arg0) {
					Quimico q = (Quimico)arg0;
					if(q.equals(getQuimicoCantidadActual().getMateriaPrima())){
						return true;
					}
					return !getQuimicosAgregados().contains(q);
				}
			});
			GuiUtil.llenarCombo(cmbQuimicos, quimicos, true);
		}
		return cmbQuimicos;
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
		}
		return cmbUnidad;
	}

	public FWJTextField getTxtCantidad() {
		if(txtCantidad==null){
			txtCantidad = new FWJTextField();
		}
		return txtCantidad;
	}

	public List<Quimico> getQuimicosAgregados() {
		return quimicosAgregados;
	}
	
	public void setQuimicosAgregados(List<Quimico> quimicosAgregados) {
		this.quimicosAgregados = quimicosAgregados;
	}
}
