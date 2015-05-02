package ar.com.textillevel.gui.modulos.personal.abm.calenlaboral;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
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
import javax.swing.JTextField;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;
import ar.com.textillevel.modulos.personal.facade.api.remote.CalendarioAnualFeriadoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogIngresarAnio extends JDialog {

	private static final long serialVersionUID = 8877955399644325580L;

	private Frame owner;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;
	private JComboBox cmbAnios;
	private JTextField txtAnioIngresado;
	private CalendarioAnualFeriado calendario;

	private CalendarioAnualFeriadoFacadeRemote calendarioFacade;
	
	public JDialogIngresarAnio(Frame owner) {
		super(owner);
		this.owner = owner;
		this.calendarioFacade = GTLPersonalBeanFactory.getInstance().getBean2(CalendarioAnualFeriadoFacadeRemote.class);
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Copiar Calendario");
		setResizable(false);
		setSize(new Dimension(200, 150));
		setModal(true);
	}

	private void setUpComponentes() {
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelCentral() {
		if(panelCentral == null){
			panelCentral = new JPanel();
			panelCentral.setLayout(new GridBagLayout());
			panelCentral.add(new JLabel("Calendario: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getCmbAnios(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Ingrese el año: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtAnioIngresado(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 1, 0));
		}
		return panelCentral;
	}

	private JComboBox getCmbAnios() {
		if(cmbAnios == null) {
			cmbAnios = new JComboBox();
			List<CalendarioAnualFeriado> allCalendarios = calendarioFacade.getAll();
			GuiUtil.llenarCombo(cmbAnios, allCalendarios, true);
			cmbAnios.setSelectedIndex(allCalendarios.size() - 1);
		}
		return cmbAnios;
	}

	private JPanel getPanelBotones() {
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						try {
							calendario = new CalendarioAnualFeriado();
							Integer anio = Integer.valueOf(getTxtAnioIngresado().getText().trim());
							calendario.setAnio(anio);
							CalendarioAnualFeriado calenEjemplo = (CalendarioAnualFeriado)getCmbAnios().getSelectedItem();
							calendario = calendarioFacade.copiarCalendario(calendario, calenEjemplo);
							dispose();
						} catch(ValidacionException ee) {
							CLJOptionPane.showErrorMessage(owner, StringW.wordWrap(ee.getMensajeError()), "Error");
							calendario = null;
						}
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getCmbAnios().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(owner, "Debe seleccionar un calendario.", "Error");
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtAnioIngresado().getText())) {
			CLJOptionPane.showErrorMessage(owner, "Debe ingresar un año.", "Error");
			getTxtAnioIngresado().requestFocus();
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}
	
	public JTextField getTxtAnioIngresado() {
		if(txtAnioIngresado == null) {
			txtAnioIngresado = new CLJNumericTextField();
		}
		return txtAnioIngresado;
	}

	public CalendarioAnualFeriado getCalendario() {
		return calendario;
	}

}