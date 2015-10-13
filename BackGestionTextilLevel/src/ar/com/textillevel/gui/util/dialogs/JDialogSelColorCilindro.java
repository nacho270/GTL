package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.NumUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.ColorCilindro;
import ar.com.textillevel.facade.api.remote.ColorFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.DecimalNumericTextField;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogSelColorCilindro extends JDialog {

	private static final long serialVersionUID = 1L;

	private JComboBox cmbCilindro;
	private JComboBox cmbColor;
	private DecimalNumericTextField txtMetrosColor;
	private DecimalNumericTextField txtKilosColor;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlBotones;
	
	private List<String> cilindrosDisponibles;
	private ColorCilindro colorCilindro;
	private boolean acepto;
	private boolean fondoSeEstampaEnCilindro;
	private ColorFacadeRemote colorFacade;

	public JDialogSelColorCilindro(JDialogEditarVariante owner, List<String> cilindrosDisponibles, boolean fondoSeEstampaEnCilindro) {
		super(owner);
		setModal(true);
		this.fondoSeEstampaEnCilindro = fondoSeEstampaEnCilindro;
		this.cilindrosDisponibles = cilindrosDisponibles;
		construct();
		setSize(400, 220);
		setTitle("Asignar Cilindro/Color");
	}

	private void construct() {
		setLayout(new FlowLayout());
		JPanel panelNorte = new JPanel(new GridBagLayout());
		panelNorte.add(new JLabel("CILINDRO: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getCmbCilindro(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panelNorte.add(new JLabel("COLOR: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getCmbColor(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panelNorte.add(new JLabel("METROS POR COLOR:"), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getTxtMetrosColor(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(new JLabel("KILOS POR COLOR:"), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelNorte.add(getTxtKilosColor(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(panelNorte,BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JComboBox getCmbCilindro() {
		if(cmbCilindro == null) {
			cmbCilindro = new JComboBox();
			GuiUtil.llenarCombo(cmbCilindro, cilindrosDisponibles, true);
		}
		return cmbCilindro;
	}

	private JComboBox getCmbColor() {
		if(cmbColor == null) {
			cmbColor = new JComboBox();
			List<Color> allColores = getColorFacade().getAllOrderByName();
			if(fondoSeEstampaEnCilindro) {
				Color color = new Color();
				color.setId(-1);
				color.setNombre("[FONDO VARIANTE]");
				allColores.set(0, color);
			}
			GuiUtil.llenarCombo(cmbColor, allColores, true);
		}
		return cmbColor;
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

	private JButton getBtnAceptar() {
		if(btnAceptar == null)  {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					if(validar()) {
						colorCilindro = new ColorCilindro();
						Color color = (Color)getCmbColor().getSelectedItem();
						if(color.getId() != -1) { // si es el fondo => color queda en NULL
							colorCilindro.setColor(color);
						}
						colorCilindro.setMetrosPorColor(getTxtMetrosColor().getValue());
						colorCilindro.setKilosPorColor(getTxtKilosColor().getValue());
						String cilindroItemStr = (String)getCmbCilindro().getSelectedItem();
						colorCilindro.setNroCilindro(NumUtil.esNumerico(cilindroItemStr) ? Integer.valueOf(cilindroItemStr) : null);
						acepto = true;
						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtMetrosColor().getText())) {
			FWJOptionPane.showErrorMessage(JDialogSelColorCilindro.this, "Debe ingresar los metros por color.", JDialogSelColorCilindro.this.getTitle());
			getTxtMetrosColor().requestFocus();
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtKilosColor().getText())) {
			FWJOptionPane.showErrorMessage(JDialogSelColorCilindro.this, "Debe ingresar los kilos por color.", JDialogSelColorCilindro.this.getTitle());
			getTxtKilosColor().requestFocus();
			return false;
		}
		return true;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null)  {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					acepto = false;
					dispose();
				}

			});
		}
		return btnCancelar;
	}
	
	private DecimalNumericTextField getTxtMetrosColor() {
		if(txtMetrosColor == null) {
			txtMetrosColor = new DecimalNumericTextField();
		}
		return txtMetrosColor;
	}

	private DecimalNumericTextField getTxtKilosColor() {
		if(txtKilosColor == null) {
			txtKilosColor = new DecimalNumericTextField();
		}
		return txtKilosColor;
	}

	private ColorFacadeRemote getColorFacade() {
		if(colorFacade == null) {
			colorFacade = GTLBeanFactory.getInstance().getBean2(ColorFacadeRemote.class);
		}
		return colorFacade;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public ColorCilindro getColorCilindro() {
		return colorCilindro;
	}

}