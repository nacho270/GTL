package ar.com.textillevel.gui.modulos.personal.abm.vacaciones;

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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.PeriodoVacaciones;

public class JDialogInputDiasYAnios extends JDialog {

	private static final long serialVersionUID = 9211937232090896967L;

	private JPanel panelCentro;
	private JPanel panelSur;

	private FWJNumericTextField txtCantDias;
	private FWJNumericTextField txtAnios;

	private JButton btnAceptar;
	private JButton btnSalir;

	private boolean acepto;
	private PeriodoVacaciones periodoVacacionesActual;
	private List<PeriodoVacaciones> periodosYaCargados;
	
	public JDialogInputDiasYAnios(Frame padre,List<PeriodoVacaciones> periodosActuales) {
		super(padre);
		setAcepto(false);
		setPeriodosYaCargados(periodosActuales);
		setPeriodoVacacionesActual(new PeriodoVacaciones());
		setUpComponentes();
		setUpScreen();
	}

	public JDialogInputDiasYAnios(Frame padre, PeriodoVacaciones periodo,List<PeriodoVacaciones> periodosActuales) {
		super(padre);
		setAcepto(false);
		setPeriodosYaCargados(periodosActuales);
		setPeriodoVacacionesActual(periodo);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Agregar/modificar periodo");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(200, 150));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCentro(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public PeriodoVacaciones getPeriodoVacacionesActual() {
		return periodoVacacionesActual;
	}

	public void setPeriodoVacacionesActual(PeriodoVacaciones periodoVacacionesActual) {
		this.periodoVacacionesActual = periodoVacacionesActual;
	}

	public FWJNumericTextField getTxtCantDias() {
		if (txtCantDias == null) {
			txtCantDias = new FWJNumericTextField(0, 99);
			if (getPeriodoVacacionesActual() != null && getPeriodoVacacionesActual().getCantidadDias() != null) {
				txtCantDias.setValue(getPeriodoVacacionesActual().getCantidadDias().longValue());
			}
		}
		return txtCantDias;
	}

	public FWJNumericTextField getTxtAnios() {
		if (txtAnios == null) {
			txtAnios = new FWJNumericTextField(0, 99);
			if (getPeriodoVacacionesActual() != null && getPeriodoVacacionesActual().getAntiguedadAniosRequerida() != null) {
				txtAnios.setValue(getPeriodoVacacionesActual().getAntiguedadAniosRequerida().longValue());
			}
		}
		return txtAnios;
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
		getPeriodoVacacionesActual().setAntiguedadAniosRequerida(getTxtAnios().getValue());
		getPeriodoVacacionesActual().setCantidadDias(getTxtCantDias().getValue());
	}
	
	private boolean validar() {
		if(getTxtAnios().getValueWithNull()==null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar los años de antigüedad requeridos", "Error");
			getTxtAnios().requestFocus();
			return false;
		}
		if(getTxtCantDias().getValueWithNull()==null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar los días de vacaciones correspondientes", "Error");
			getTxtCantDias().requestFocus();
			return false;
		}
		for(PeriodoVacaciones p : getPeriodosYaCargados()){
			if(p.getAntiguedadAniosRequerida().equals(getTxtAnios().getValue())){
				FWJOptionPane.showErrorMessage(this, "Ya existe un periodo para " +getTxtAnios().getValue() +" años de antigüedad.", "Error");
				return false;
			}
		}
		return true;
	}

	public JButton getBtnSalir() {
		if (btnSalir == null) {
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

	public JPanel getPanelCentro() {
		if(panelCentro == null){
			panelCentro = new JPanel(new GridBagLayout());
			
			panelCentro.add(new JLabel("Antigüedad: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtAnios(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			
			panelCentro.add(new JLabel("Días: "),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtCantDias(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			
		}
		return panelCentro;
	}

	public JPanel getPanelSur() {
		if(panelSur == null){
			panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
			panelSur.add(getBtnAceptar());
			panelSur.add(getBtnSalir());
		}
		return panelSur;
	}

	
	public List<PeriodoVacaciones> getPeriodosYaCargados() {
		return periodosYaCargados;
	}

	
	public void setPeriodosYaCargados(List<PeriodoVacaciones> periodosYaCargados) {
		this.periodosYaCargados = periodosYaCargados;
	}
}
