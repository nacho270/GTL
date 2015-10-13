package ar.com.textillevel.gui.modulos.personal.abm.contribuciones;

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
import java.math.BigDecimal;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.contribuciones.PeriodoContribucion;

public class JDialogCargarPeriodoContribucion extends JDialog {

	private static final long serialVersionUID = -3645869306074747707L;

	private Frame owner;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;

	private PanelDatePicker panFechaDesde;
	private PanelDatePicker panFechaHasta;
	private JTextField txtPorcentaje;
	private JTextField txtImporteFijo;
	private JCheckBox chkEsPorcentaje;
	private PeriodoContribucion periodoContribucion;

	private boolean acepto;

	public JDialogCargarPeriodoContribucion(Frame owner, PeriodoContribucion periodoContribucion) {
		super(owner);
		this.owner = owner;
		this.periodoContribucion = periodoContribucion;
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
		setDatos();
	}

	private void setDatos() {
		getPanFechaDesde().setSelectedDate(getPeriodoContribucion().getFechaDesde());
		getPanFechaHasta().setSelectedDate(getPeriodoContribucion().getFechaHasta());
		if(getPeriodoContribucion().getImporteFijo() != null) {
			getTxtImporteFijo().setText(GenericUtils.getDecimalFormat().format(getPeriodoContribucion().getImporteFijo().doubleValue()));
			getChkEsPorcentaje().setSelected(false);
			getTxtPorcentaje().setEditable(false);
		} else {
			getTxtPorcentaje().setText(GenericUtils.getDecimalFormat().format(getPeriodoContribucion().getPorcentaje().doubleValue()));
			getChkEsPorcentaje().setSelected(true);
			getTxtImporteFijo().setEditable(false);
		}
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Crear/Editar Período de Contribución");
		setResizable(false);
		setSize(new Dimension(500, 300));
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
			panelCentral.add(getPanFechaDesde(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getPanFechaHasta(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getChkEsPorcentaje(),  GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE,new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(new JLabel("Valor en Porcentaje: "), GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtPorcentaje(),  GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(new JLabel("Importe Fijo: "), GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtImporteFijo(),  GenericUtils.createGridBagConstraints(1, 3,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			
		}
		return panelCentral;
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
						acepto = true;
						if(getChkEsPorcentaje().isSelected()) {
							BigDecimal porcentaje = new BigDecimal(GenericUtils.getDoubleValueInJTextField(getTxtPorcentaje()));
							getPeriodoContribucion().setPorcentaje(porcentaje);
							getPeriodoContribucion().setImporteFijo(null);
						} else {
							BigDecimal importeFijo = new BigDecimal(GenericUtils.getDoubleValueInJTextField(getTxtImporteFijo()));
							getPeriodoContribucion().setImporteFijo(importeFijo);
							getPeriodoContribucion().setPorcentaje(null);
						}
						getPeriodoContribucion().setFechaDesde(new Date(getPanFechaDesde().getDate().getTime()));
						getPeriodoContribucion().setFechaHasta(new Date(getPanFechaHasta().getDate().getTime()));
						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	public PeriodoContribucion getPeriodoContribucion() {
		return periodoContribucion;
	}

	private boolean validar() {
		if(getPanFechaDesde().getDate() == null) {
			FWJOptionPane.showErrorMessage(owner, "Debe ingresar una fecha desde.", "Error");
			return false;
		}
		if(getPanFechaHasta().getDate() == null) {
			FWJOptionPane.showErrorMessage(owner, "Debe ingresar una fecha hasta.", "Error");
			return false;
		}
		if(getPanFechaDesde().getDate().after(getPanFechaHasta().getDate())) {
			FWJOptionPane.showErrorMessage(owner, "La fecha desde debe ser menor o igual a la fecha hasta.", "Error");
			return false;
		}
		if(getChkEsPorcentaje().isSelected()) {
			String valorPorcentajeStr = getTxtPorcentaje().getText().trim();
			if(StringUtil.isNullOrEmpty(valorPorcentajeStr)) {
				FWJOptionPane.showErrorMessage(owner, "Debe ingresar un porcentaje.", "Error");
				getTxtPorcentaje().requestFocus();
				return false;
			}
			if(!GenericUtils.esNumerico(valorPorcentajeStr)) {
				FWJOptionPane.showErrorMessage(owner, "El porcentaje debe ser numérico.", "Error");
				getTxtPorcentaje().requestFocus();
				return false;
			}
			double valorPorcentaje = GenericUtils.getDoubleValueInJTextField(getTxtPorcentaje());
			if(valorPorcentaje <= 0 || valorPorcentaje > 100) {
				FWJOptionPane.showErrorMessage(owner, "El porcentaje debe ser mayor a cero y menor o igual que 100.", "Error");
				getTxtPorcentaje().requestFocus();
				return false;
			}
		} else {
			String valorImporteFijoStr = getTxtImporteFijo().getText().trim();
			if(StringUtil.isNullOrEmpty(valorImporteFijoStr)) {
				FWJOptionPane.showErrorMessage(owner, "Debe ingresar un importe fijo.", "Error");
				getTxtImporteFijo().requestFocus();
				return false;
			}
			if(!GenericUtils.esNumerico(valorImporteFijoStr)) {
				FWJOptionPane.showErrorMessage(owner, "El importe fijo debe ser numérico.", "Error");
				getTxtImporteFijo().requestFocus();
				return false;
			}
			double valorImporteFijo = GenericUtils.getDoubleValueInJTextField(getTxtImporteFijo());
			if(valorImporteFijo <= 0) {
				FWJOptionPane.showErrorMessage(owner, "El importe fijo debe ser mayor a cero.", "Error");
				getTxtImporteFijo().requestFocus();
				return false;
			}
		}
		return true;
	}

	private JCheckBox getChkEsPorcentaje() {
		if(chkEsPorcentaje == null) {
			chkEsPorcentaje = new JCheckBox("Aplica por porcentaje");
			chkEsPorcentaje.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						getTxtImporteFijo().setText(null);
						getTxtImporteFijo().setEditable(false);
						getTxtPorcentaje().setEditable(true);
					} else {
						getTxtPorcentaje().setText(null);
						getTxtPorcentaje().setEditable(false);
						getTxtImporteFijo().setEditable(true);
					}
				}

			});
		}
		return chkEsPorcentaje;
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
	
	private JTextField getTxtPorcentaje() {
		if(txtPorcentaje == null) {
			txtPorcentaje = new JTextField();
		}
		return txtPorcentaje;
	}

	private JTextField getTxtImporteFijo() {
		if(txtImporteFijo == null) {
			txtImporteFijo = new JTextField();
		}
		return txtImporteFijo;
	}

	public boolean isAcepto() {
		return acepto;
	}

	private PanelDatePicker getPanFechaDesde() {
		if(panFechaDesde == null){
			panFechaDesde = new PanelDatePicker();
			panFechaDesde.setCaption("Fecha Desde: ");
		}
		return panFechaDesde;
	}

	private PanelDatePicker getPanFechaHasta() {
		if(panFechaHasta == null){
			panFechaHasta = new PanelDatePicker();
			panFechaHasta.setCaption("Fecha Hasta: ");
		}
		return panFechaHasta;
	}

}