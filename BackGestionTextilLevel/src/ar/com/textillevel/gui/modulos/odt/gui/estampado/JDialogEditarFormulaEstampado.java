package ar.com.textillevel.gui.modulos.odt.gui.estampado;

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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.facade.api.remote.ColorFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado.FormulaEstampadoCliente;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogEditarFormulaEstampado extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private FWJTextField txtNombre;
	private JComboBox cmbColor;
	private TabPaneAnilinaPigmentoCantidad tabPaneAnilinas;

	private FormulaEstampadoCliente formula;
	private boolean acepto;
	private boolean modoConsulta;

	public JDialogEditarFormulaEstampado(Frame owner, FormulaEstampadoCliente formula, boolean modoConsulta) {
		super(owner);
		this.modoConsulta = modoConsulta;
		this.formula = formula;
		setTitle("Cargar Fórmula");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(470, 450));
		setResizable(false);
		setModal(true);
		construct();
		setDatos();
	}

	private void setDatos() {
		getTxtNombre().setText(formula.getNombre());
		getCmbColor().setSelectedItem(formula.getColor());
	}

	private void construct() {
		add(getPanelDatos(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	private JPanel getPanelDatos() {
		if(pnlDatos == null){
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridBagLayout());
			pnlDatos.add(new JLabel(" NOMBRE:"), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getTxtNombre(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(new JLabel(" COLOR:"), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getCmbColor(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getTabPaneAnilinaPigmento(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 1));
		}
		return pnlDatos;
	}		

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {

						formula = getTabPaneAnilinaPigmento().setearDatosEnFormula();

						formula.setNombre(getTxtNombre().getText().trim());
						formula.setColor((Color)getCmbColor().getSelectedItem());

						acepto = true;
						dispose();
					}
				}

			});
			
			btnAceptar.setVisible(!modoConsulta);
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtNombre().getText().trim())) {
			FWJOptionPane.showErrorMessage(JDialogEditarFormulaEstampado.this, "Falta ingresar el nombre.", "Error");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getCmbColor().getSelectedIndex() == -1) {
			FWJOptionPane.showErrorMessage(JDialogEditarFormulaEstampado.this, "Falta seleccionar color.", "Error");
			return false;
		}
		String msgValidacionTabPanel = getTabPaneAnilinaPigmento().validar();
		if(!StringUtil.isNullOrEmpty(msgValidacionTabPanel)) {
			FWJOptionPane.showErrorMessage(JDialogEditarFormulaEstampado.this, StringW.wordWrap(msgValidacionTabPanel), "Error");
			return false;
		}

		return true;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton(modoConsulta ? "Cerrar" : "Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					acepto = false;
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private FWJTextField getTxtNombre() {
		if(txtNombre == null) {
			txtNombre = new FWJTextField();
			txtNombre.setEnabled(!modoConsulta);
		}
		return txtNombre;
	}

	private JComboBox getCmbColor() {
		if(cmbColor == null) {
			cmbColor = new JComboBox();
			GuiUtil.llenarCombo(cmbColor, GTLBeanFactory.getInstance().getBean2(ColorFacadeRemote.class).getAllOrderByName(), true);
			cmbColor.setEnabled(!modoConsulta);			
		}
		return cmbColor;
	}

	private TabPaneAnilinaPigmentoCantidad getTabPaneAnilinaPigmento() {
		if(tabPaneAnilinas == null) {
			tabPaneAnilinas = new TabPaneAnilinaPigmentoCantidad(JDialogEditarFormulaEstampado.this, formula, modoConsulta);
		}
		return tabPaneAnilinas;
	}
	
	public boolean isAcepto() {
		return acepto;
	}

	public FormulaEstampadoCliente getFormula() {
		return formula;
	}

}
