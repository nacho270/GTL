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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.entidades.Dia;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.TotalHorasPagoDia;
import ar.com.textillevel.modulos.personal.facade.api.remote.DiaFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogCargarTotalHorasPagoDia extends JDialog {

	private static final long serialVersionUID = 8877955399644325580L;

	private Frame owner;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;

	private JComboBox cmbDia;
	private FWJNumericTextField txtTotalHoras;
	private JCheckBox chkSeDiscriminaEnRS;

	private TotalHorasPagoDia totalHorasPagoDia;
	private DiaFacadeRemote diaFacade;
	private boolean acepto;
	
	public JDialogCargarTotalHorasPagoDia(Frame owner, TotalHorasPagoDia totalHorasPagoDia) {
		super(owner);
		this.owner = owner;
		this.totalHorasPagoDia = totalHorasPagoDia;
		this.diaFacade = GTLPersonalBeanFactory.getInstance().getBean2(DiaFacadeRemote.class);
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
		setDatos();
	}

	private void setDatos() {
		getCmbDia().setSelectedItem(totalHorasPagoDia.getDia());
		getTxtTotalHoras().setValue(totalHorasPagoDia.getTotalHoras().longValue());
		getChkSeDiscriminaEnRS().setSelected(totalHorasPagoDia.isDiscriminaEnRS());
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Total de horas a pagar por día");
		setResizable(false);
		setSize(new Dimension(240, 250));
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
			panelCentral.add(new JLabel("Día: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getCmbDia(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Total horas a pagar: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtTotalHoras(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(getChkSeDiscriminaEnRS(),  GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 2, 1, 1, 0));
			
		}
		return panelCentral;
	}

	private JComboBox getCmbDia() {
		if(cmbDia == null) {
			cmbDia = new JComboBox();
			List<Dia> allDias = diaFacade.getAllDias();
			GuiUtil.llenarCombo(cmbDia, allDias, true);
			cmbDia.setSelectedIndex(allDias.size() - 1);
		}
		return cmbDia;
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
						totalHorasPagoDia.setDia((Dia)getCmbDia().getSelectedItem());
						totalHorasPagoDia.setTotalHoras(getTxtTotalHoras().getValue());
						totalHorasPagoDia.setDiscriminaEnRS(getChkSeDiscriminaEnRS().isSelected());
						acepto = true;
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtTotalHoras().getText())) {
			FWJOptionPane.showErrorMessage(owner, "Debe ingresar un total de horas.", "Error");
			getTxtTotalHoras().requestFocus();
			return false;
		}
		if(getTxtTotalHoras().getValue()<=0) {
			FWJOptionPane.showErrorMessage(owner, "El total de horas debe ser mayor a cero.", "Error");
			getTxtTotalHoras().requestFocus();
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					acepto = false;
					dispose();
				}
			});
		}
		return btnSalir;
	}
	
	public FWJNumericTextField getTxtTotalHoras() {
		if(txtTotalHoras == null) {
			txtTotalHoras = new FWJNumericTextField();
		}
		return txtTotalHoras;
	}

	private JCheckBox getChkSeDiscriminaEnRS() {
		if(chkSeDiscriminaEnRS == null) {
			chkSeDiscriminaEnRS = new JCheckBox("Se discrimina en el recibo de sueldo");
		}
		return chkSeDiscriminaEnRS;
	}

	public boolean isAcepto() {
		return acepto;
	}

}