package ar.com.textillevel.gui.modulos.odt.gui.tenido;

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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.facade.api.remote.ColorFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.AnilinaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.FormulaTenidoCliente;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido.TenidoTipoArticulo;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogEditarFormula extends JDialog {

	private static final long serialVersionUID = 1L;

	private static float MAX_PROPORCION_ANILINA = 4.0f;

	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private CLJTextField txtNombre;
	private JComboBox cmbTipoArticulo;
	private JComboBox cmbColor;
	private TabPaneTipoArticuloAnilinas tabPaneAnilinas;

	private FormulaTenidoCliente formula;
	private boolean acepto;
	private boolean modoConsulta;

	public JDialogEditarFormula(Frame owner, FormulaTenidoCliente formula, boolean modoConsulta) {
		super(owner);
		this.modoConsulta = modoConsulta;
		this.formula = formula;
		setTitle("Cargar F�rmula");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(470, 450));
		setResizable(false);
		setModal(true);
		construct();
		setDatos();
	}

	private void setDatos() {
		getTxtNombre().setText(formula.getNombre());
		getCmbTipoArticulo().setSelectedItem(formula.getTipoArticulo());
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
			pnlDatos.add(new JLabel(" TIPO DE ARTICULO:"), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getCmbTipoArticulo(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			pnlDatos.add(getTabPaneAnilinas(), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 1));
		}
		return pnlDatos;
	}		
	
	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {

						formula = getTabPaneAnilinas().setearDatosEnFormula();

						for(TenidoTipoArticulo tta : formula.getTenidosComponentes()) {
							for(AnilinaCantidad ac : tta.getAnilinasCantidad()) {
								if(ac.getCantidad() > MAX_PROPORCION_ANILINA) {
									if(CLJOptionPane.showQuestionMessage(JDialogEditarFormula.this, "La proporcion '" + ac.getCantidad() + "' supera el " + MAX_PROPORCION_ANILINA + "% �Desea Continuar?", "Confirmaci�n") == CLJOptionPane.NO_OPTION) {
										return;
									}
								}
							}
						}

						formula.setNombre(getTxtNombre().getText().trim());
						formula.setTipoArticulo((TipoArticulo)getCmbTipoArticulo().getSelectedItem());
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
			CLJOptionPane.showErrorMessage(JDialogEditarFormula.this, "Falta ingresar el nombre.", "Error");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getCmbTipoArticulo().getSelectedIndex() == -1) {
			CLJOptionPane.showErrorMessage(JDialogEditarFormula.this, "Falta seleccionar el tipo de art�culo.", "Error");
			return false;
		}
		if(getCmbColor().getSelectedIndex() == -1) {
			CLJOptionPane.showErrorMessage(JDialogEditarFormula.this, "Falta seleccionar color.", "Error");
			return false;
		}
		String msgValidacionTabPanel = getTabPaneAnilinas().validar();
		if(!StringUtil.isNullOrEmpty(msgValidacionTabPanel)) {
			CLJOptionPane.showErrorMessage(JDialogEditarFormula.this, StringW.wordWrap(msgValidacionTabPanel), "Error");
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

	private CLJTextField getTxtNombre() {
		if(txtNombre == null) {
			txtNombre = new CLJTextField();
			txtNombre.setEnabled(!modoConsulta);
		}
		return txtNombre;
	}

	private JComboBox getCmbTipoArticulo() {
		if(cmbTipoArticulo == null) {
			cmbTipoArticulo = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoArticulo, GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class).getAllTipoArticulos(), true);
			cmbTipoArticulo.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent ie) {
					if(ie.getStateChange() == ItemEvent.SELECTED) {
						if(getCmbTipoArticulo().getSelectedIndex() != -1) {
							if(formula.getId() == null) { //Es un alta
								formula.getTenidosComponentes().clear();
								TipoArticulo ta = (TipoArticulo)getCmbTipoArticulo().getSelectedItem();
								if(ta.getTiposArticuloComponentes().isEmpty()) {
									TenidoTipoArticulo tta = new TenidoTipoArticulo();
									tta.setTipoArticulo(ta);
									formula.getTenidosComponentes().add(tta);
								} else {
									for(TipoArticulo tah : ta.getTiposArticuloComponentes()) {
										TenidoTipoArticulo ttah = new TenidoTipoArticulo();
										ttah.setTipoArticulo(tah);
										formula.getTenidosComponentes().add(ttah);
									}
								}
								tabPaneAnilinas.rebuildTabs(formula.getTenidosComponentes());
							}
						}
					}
				}

			});

			cmbTipoArticulo.setEnabled(!modoConsulta && formula != null && formula.getId() == null);
		}
		return cmbTipoArticulo;
	}

	private JComboBox getCmbColor() {
		if(cmbColor == null) {
			cmbColor = new JComboBox();
			GuiUtil.llenarCombo(cmbColor, GTLBeanFactory.getInstance().getBean2(ColorFacadeRemote.class).getAllOrderByName(), true);
			cmbColor.setEnabled(!modoConsulta);			
		}
		return cmbColor;
	}

	private TabPaneTipoArticuloAnilinas getTabPaneAnilinas() {
		if(tabPaneAnilinas == null) {
			tabPaneAnilinas = new TabPaneTipoArticuloAnilinas(JDialogEditarFormula.this, formula, modoConsulta);
		}
		return tabPaneAnilinas;
	}
	
	public boolean isAcepto() {
		return acepto;
	}

	public FormulaTenidoCliente getFormula() {
		return formula;
	}

}
