package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.entidades.Dia;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.HorarioDia;
import ar.com.textillevel.modulos.personal.entidades.legajos.RangoDias;
import ar.com.textillevel.modulos.personal.entidades.legajos.RangoHorario;
import ar.com.textillevel.modulos.personal.facade.api.remote.DiaFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogAgregarModificarHorario extends JDialog {

	private static final long serialVersionUID = -3791646173419028814L;

	private JComboBox cmbDiaDesde;
	private JComboBox cmbDiaHasta;

	private JSpinner jsHorasDesde;
	private JSpinner jsMinutosDesde;
	private JSpinner jsHorasHasta;
	private JSpinner jsMinutosHasta;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private HorarioDia horarioDia;

	private List<HorarioDia> otrosDiasConfigurados;

	private boolean acepto;

	public JDialogAgregarModificarHorario(Dialog padre, List<HorarioDia> horariosExistentes) {
		super(padre);
		setUpScreen();
		setUpComponentes();
		setOtrosDiasConfigurados(horariosExistentes);
		setHorarioDia(new HorarioDia());
	}

	public JDialogAgregarModificarHorario(Dialog padre, HorarioDia horarioModificar, List<HorarioDia> horariosExistentes) {
		super(padre);
		setUpScreen();
		setUpComponentes();
		setOtrosDiasConfigurados(horariosExistentes);
		setHorarioDia(horarioModificar);
		loadData();
	}
	
	private void loadData(){
		getCmbDiaDesde().setSelectedItem(getHorarioDia().getRangoDias().getDiaDesde());
		getCmbDiaHasta().setSelectedItem(getHorarioDia().getRangoDias().getDiaHasta());
		getJsHorasDesde().setValue(getHorarioDia().getRangoHorario().getHoraDesde());
		getJsMinutosDesde().setValue(getHorarioDia().getRangoHorario().getMinutosDesde());
		getJsHorasHasta().setValue(getHorarioDia().getRangoHorario().getHoraHasta());
		getJsMinutosHasta().setValue(getHorarioDia().getRangoHorario().getMinutosHasta());
	}

	private void setUpComponentes() {
		add(getPanelCentro(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
		pack();
	}

	private JPanel getPanelCentro() {
		JPanel panelCentro = new JPanel(new GridBagLayout());
		panelCentro.add(new JLabel("Desde: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getCmbDiaDesde(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panelCentro.add(getJsHorasDesde(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(new JLabel(":"), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getJsMinutosDesde(), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		panelCentro.add(new JLabel("Hasta: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getCmbDiaHasta(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panelCentro.add(getJsHorasHasta(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(new JLabel(":"), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getJsMinutosHasta(), GenericUtils.createGridBagConstraints(4, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		return panelCentro;
	}

	private JPanel getPanelSur() {
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnCancelar());
		return panelSur;
	}

	private void setUpScreen() {
		setTitle("Alta/modificación horario");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		GuiUtil.centrar(this);
		setResizable(false);
	}

	public JComboBox getCmbDiaDesde() {
		if (cmbDiaDesde == null) {
			cmbDiaDesde = new JComboBox();
			GuiUtil.llenarCombo(cmbDiaDesde, GTLPersonalBeanFactory.getInstance().getBean2(DiaFacadeRemote.class).getAllDias(), true);
		}
		return cmbDiaDesde;
	}

	public JComboBox getCmbDiaHasta() {
		if (cmbDiaHasta == null) {
			cmbDiaHasta = new JComboBox();
			GuiUtil.llenarCombo(cmbDiaHasta, GTLPersonalBeanFactory.getInstance().getBean2(DiaFacadeRemote.class).getAllDias(), true);
		}
		return cmbDiaHasta;
	}

	public JSpinner getJsHorasDesde() {
		if (jsHorasDesde == null) {
			jsHorasDesde = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
			jsHorasDesde.setEditor(new JSpinner.NumberEditor(jsHorasDesde));
			jsHorasDesde.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsHorasDesde.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsHorasDesde;
	}

	public JSpinner getJsMinutosDesde() {
		if (jsMinutosDesde == null) {
			jsMinutosDesde = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
			jsMinutosDesde.setEditor(new JSpinner.NumberEditor(jsMinutosDesde));
			jsMinutosDesde.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsMinutosDesde.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsMinutosDesde;
	}

	public JSpinner getJsHorasHasta() {
		if (jsHorasHasta == null) {
			jsHorasHasta = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
			jsHorasHasta.setEditor(new JSpinner.NumberEditor(jsHorasHasta));
			jsHorasHasta.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsHorasHasta.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsHorasHasta;
	}

	public JSpinner getJsMinutosHasta() {
		if (jsMinutosHasta == null) {
			jsMinutosHasta = new JSpinner(new SpinnerNumberModel(0, 0,59, 1));
			jsMinutosHasta.setEditor(new JSpinner.NumberEditor(jsMinutosHasta));
			jsMinutosHasta.setPreferredSize(new Dimension(40, 23));
			JFormattedTextField ftf = ((JSpinner.DefaultEditor) jsMinutosHasta.getEditor()).getTextField();
			ftf.setEditable(true);
			ftf.setBackground(Color.WHITE);
		}
		return jsMinutosHasta;
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
	
	private void capturarDatos(){
		getHorarioDia().getRangoDias().setDiaDesde((Dia)getCmbDiaDesde().getSelectedItem());
		getHorarioDia().getRangoDias().setDiaHasta((Dia)getCmbDiaHasta().getSelectedItem());
		getHorarioDia().getRangoHorario().setHoraDesde((Integer)getJsHorasDesde().getValue());
		getHorarioDia().getRangoHorario().setMinutosDesde((Integer)getJsMinutosDesde().getValue());
		getHorarioDia().getRangoHorario().setHoraHasta((Integer)getJsHorasHasta().getValue());
		getHorarioDia().getRangoHorario().setMinutosHasta((Integer)getJsMinutosHasta().getValue());
	}

	private boolean validar() {
		Dia diaDesde = (Dia) getCmbDiaDesde().getSelectedItem();
		Dia diaHasta = (Dia) getCmbDiaHasta().getSelectedItem();
		if (diaDesde.getNroDia() > diaHasta.getNroDia()) {
			CLJOptionPane.showErrorMessage(this, "El 'Día desde' debe ser anterior al 'Día hasta'", "Error");
			return false;
		}
		if(diaDesde.getNroDia() == diaHasta.getNroDia()){
			Integer horaDesde = (Integer)getJsHorasDesde().getValue();
			Integer horaHasta = (Integer)getJsHorasHasta().getValue();
			if(horaDesde>horaHasta){
				CLJOptionPane.showErrorMessage(this, "La 'Hora desde' debe ser anterior a la 'Hora hasta'", "Error");
				return false;
			}else if(horaDesde == horaHasta){
				Integer minutosDesde = (Integer)getJsMinutosDesde().getValue();
				Integer minutosHasta = (Integer)getJsMinutosHasta().getValue();
				if(minutosDesde>=minutosHasta){
					CLJOptionPane.showErrorMessage(this, "El 'Horario desde' debe ser anterior al 'Horario hasta'", "Error");
					return false;
				}
			}
		}
		HorarioDia horarioDiaTemp = new HorarioDia();
		
		RangoDias rangoDiasTemp = new RangoDias();
		rangoDiasTemp.setDiaDesde(diaDesde);
		rangoDiasTemp.setDiaHasta(diaHasta);
		
		RangoHorario rangoHorarioTemp = new RangoHorario();
		rangoHorarioTemp.setHoraDesde((Integer)getJsHorasDesde().getValue());
		rangoHorarioTemp.setHoraHasta((Integer)getJsHorasHasta().getValue());
		rangoHorarioTemp.setMinutosDesde((Integer)getJsMinutosDesde().getValue());
		rangoHorarioTemp.setMinutosHasta((Integer)getJsMinutosHasta().getValue());
		
		horarioDiaTemp.setRangoDias(rangoDiasTemp);
		horarioDiaTemp.setRangoHorario(rangoHorarioTemp);
		
		for (HorarioDia horario : getOtrosDiasConfigurados()){
			if(horario.seSolapa(horarioDiaTemp)){
				CLJOptionPane.showErrorMessage(this, "El horario ingresado se solapa con otro ya existente.", "Error");
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

	public HorarioDia getHorarioDia() {
		return horarioDia;
	}

	public void setHorarioDia(HorarioDia horarioDia) {
		this.horarioDia = horarioDia;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public List<HorarioDia> getOtrosDiasConfigurados() {
		return otrosDiasConfigurados;
	}

	public void setOtrosDiasConfigurados(List<HorarioDia> otrosDiasConfigurados) {
		this.otrosDiasConfigurados = otrosDiasConfigurados;
	}
}
