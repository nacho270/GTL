package ar.com.textillevel.gui.modulos.personal.abm.presentismo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismoRangoMinutos;

public class JDialogAgregarModificarDescuentoPorRangoMinutos extends JDialog {

	private static final long serialVersionUID = -819445810048405624L;

	private FWJNumericTextField txtMinutosDesde;
	private FWJNumericTextField txtMinutosHasta;
	private FWJTextField txtPorcentajeDescuento;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentro;
	private JPanel panelSur;

	private List<DescuentoPresentismoRangoMinutos> descuentosExistentes;
	private DescuentoPresentismoRangoMinutos descuentoActual;
	private boolean acepto;

	public JDialogAgregarModificarDescuentoPorRangoMinutos(Frame padre, List<DescuentoPresentismoRangoMinutos> descuentosYaCargados) {
		super(padre);
		setDescuentosExistentes(descuentosYaCargados);
		setDescuentoActual(new DescuentoPresentismoRangoMinutos());
		setUpComponentes();
		setUpScreen();
	}

	public JDialogAgregarModificarDescuentoPorRangoMinutos(Frame padre, List<DescuentoPresentismoRangoMinutos> descuentosYaCargados, DescuentoPresentismoRangoMinutos descuentoElegido) {
		super(padre);
		setDescuentosExistentes(descuentosYaCargados);
		setDescuentoActual(descuentoElegido);
		setUpComponentes();
		setUpScreen();
		loadData();
	}

	private void loadData() {
		getTxtMinutosDesde().setValue(getDescuentoActual().getMinutosDesde().longValue());
		getTxtMinutosHasta().setValue(getDescuentoActual().getMinutosHasta().longValue());
		getTxtPorcentajeDescuento().setText(GenericUtils.getDecimalFormat().format(getDescuentoActual().getPorcentajeDescuento()));
	}

	private void setUpScreen() {
		setTitle("Agregar/modificar descuento presentismo por falta");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		pack();
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				setAcepto(false);
				dispose();
			}
		});
		add(getPanelCentro(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	public FWJNumericTextField getTxtMinutosDesde() {
		if (txtMinutosDesde == null) {
			txtMinutosDesde = new FWJNumericTextField();
			txtMinutosDesde.setPreferredSize(new Dimension(120, 20));
		}
		return txtMinutosDesde;
	}

	public FWJTextField getTxtPorcentajeDescuento() {
		if (txtPorcentajeDescuento == null) {
			txtPorcentajeDescuento = new FWJTextField();
			txtPorcentajeDescuento.setPreferredSize(new Dimension(120, 20));
		}
		return txtPorcentajeDescuento;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						getDescuentoActual().setMinutosDesde(getTxtMinutosDesde().getValue());
						getDescuentoActual().setMinutosHasta(getTxtMinutosHasta().getValue());
						getDescuentoActual().setPorcentajeDescuento(new BigDecimal(GenericUtils.getDoubleValue(getTxtPorcentajeDescuento().getText())));
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if (getTxtMinutosDesde().getValueWithNull() == null) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar la cantidad de faltas", "Error");
			getTxtMinutosDesde().requestFocus();
			return false;
		}
		if (getTxtPorcentajeDescuento().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el porcentaje de descuento", "Error");
			getTxtPorcentajeDescuento().requestFocus();
			return false;
		}
		if (!GenericUtils.esNumerico(getTxtPorcentajeDescuento().getText())) {
			FWJOptionPane.showErrorMessage(this, "El valor del porcentaje de descuento debe ser numérico", "Error");
			getTxtPorcentajeDescuento().requestFocus();
			return false;
		}
		if(getTxtMinutosDesde().getValue()>getTxtMinutosHasta().getValue()){
			FWJOptionPane.showErrorMessage(this, "Los minutos 'desde' deben ser menores que los minutos 'hasta'", "Error");
			getTxtMinutosDesde().requestFocus();
			return false;
		}
		if (getDescuentosExistentes() != null && !getDescuentosExistentes().isEmpty()) {
			for (DescuentoPresentismoRangoMinutos d : getDescuentosExistentes()) {
				if (d.seSolapa(getTxtMinutosDesde().getValue(), getTxtMinutosHasta().getValue()) && !d.equals(getDescuentoActual())) {
					FWJOptionPane.showErrorMessage(this, "El rango ingresado se solapa con un rango existente", "Error");
					getTxtMinutosDesde().requestFocus();
					return false;
				}
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

	public List<DescuentoPresentismoRangoMinutos> getDescuentosExistentes() {
		return descuentosExistentes;
	}

	public void setDescuentosExistentes(List<DescuentoPresentismoRangoMinutos> descuentosExistentes) {
		this.descuentosExistentes = descuentosExistentes;
	}

	public JPanel getPanelCentro() {
		if (panelCentro == null) {
			panelCentro = new JPanel(new GridBagLayout());
			panelCentro.add(new JLabel("Desde (minutos tarde): "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtMinutosDesde(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelCentro.add(new JLabel("Hasta (minutos tarde): "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtMinutosHasta(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			panelCentro.add(new JLabel("Descuento (%): "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtPorcentajeDescuento(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		}
		return panelCentro;
	}

	public JPanel getPanelSur() {
		if (panelSur == null) {
			panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnAceptar());
			panelSur.add(getBtnSalir());
		}
		return panelSur;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public DescuentoPresentismoRangoMinutos getDescuentoActual() {
		return descuentoActual;
	}

	public void setDescuentoActual(DescuentoPresentismoRangoMinutos descuentoActual) {
		this.descuentoActual = descuentoActual;
	}

	public FWJNumericTextField getTxtMinutosHasta() {
		if(txtMinutosHasta == null){
			txtMinutosHasta = new FWJNumericTextField();
		}
		return txtMinutosHasta;
	}
}
