package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

public class JDialogAgregarModificarMateriaPrimaCantidad<F extends Formulable, T extends MateriaPrimaCantidad<F>> extends JDialog {

	private static final long serialVersionUID = -7227181759998277438L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	private List<PanelMateriaPrimaCantidadUnidad> paneles;
	private List<F> matPrimasYaAgregadas;
	private List<F> allMatPrimasPosibles;
	private List<T> mps;
	private boolean acepto;
	private String descripcionTipoMateriaPrima;
	private boolean modoConsulta;

	public JDialogAgregarModificarMateriaPrimaCantidad(Dialog padre, String descripcionTipoMateriaPrima, List<F> allMatPrimasPosibles, List<F> matPrimasYaAgregadas, List<T> mpc, boolean modoConsulta) {
		super(padre);
		this.modoConsulta = modoConsulta;
		this.descripcionTipoMateriaPrima = descripcionTipoMateriaPrima;
		this.allMatPrimasPosibles = allMatPrimasPosibles;
		this.matPrimasYaAgregadas = matPrimasYaAgregadas;
		this.mps = mpc;
		this.paneles = new ArrayList<PanelMateriaPrimaCantidadUnidad>();
		setUpScreen();
		setUpComponentes();
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		for (T m : this.mps) {
			PanelMateriaPrimaCantidadUnidad e = new PanelMateriaPrimaCantidadUnidad(m, descripcionTipoMateriaPrima);
			e.loadData();
			this.paneles.add(e);
		}
		JPanel p = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5));
		for (PanelMateriaPrimaCantidadUnidad pan : this.paneles) {
			p.add(pan);
		}
		add(p, BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
		pack();
		GuiUtil.centrar(this);
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	private void setUpScreen() {
		setTitle("Alta/modificación de " + descripcionTipoMateriaPrima);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						capturarDatos();
						setAcepto(true);
						dispose();
					}
				}

				private void capturarDatos() {
					List<T> mpsAGuardar = new ArrayList<T>();
					for(PanelMateriaPrimaCantidadUnidad p : paneles) {
						if(p.isCompleto()) {
							p.capturarDatos();
							mpsAGuardar.add(p.matPrimaCantidadActual);
						}
					}
					mps = mpsAGuardar;
				}

				private boolean validar() {
					List<PanelMateriaPrimaCantidadUnidad> panelesCompletos = new ArrayList<PanelMateriaPrimaCantidadUnidad>();
					for (PanelMateriaPrimaCantidadUnidad p : paneles) {
						if (p.isCompleto()) {
							panelesCompletos.add(p);
						}
					}
					if(panelesCompletos.isEmpty()) {
						FWJOptionPane.showErrorMessage(JDialogAgregarModificarMateriaPrimaCantidad.this, "Debe completar al menos un panel", "Error");
						return false;
					}
					Map<String, Integer> nombresMP = new HashMap<String, Integer>();
					for (PanelMateriaPrimaCantidadUnidad p : panelesCompletos) {
						String nombreMp = p.getNombreMP();
						if (nombresMP.get(nombreMp) == null) {
							nombresMP.put(nombreMp, new Integer(0));
						}
						nombresMP.put(nombreMp, new Integer(nombresMP.get(nombreMp).intValue() + 1));
						boolean valido = p.validar();
						if(!valido) {
							return false;
						}
					}
					for(String nombreMP : nombresMP.keySet()) {
						if(nombresMP.get(nombreMP).intValue() > 1) {
							FWJOptionPane.showErrorMessage(JDialogAgregarModificarMateriaPrimaCantidad.this, "Hay " + descripcionTipoMateriaPrima +"s repetidas: " + nombreMP, "Error");
							return false;
						}
					}
					return true;
				}
			});
			btnAceptar.setVisible(!modoConsulta);
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton(modoConsulta ? "Cerrar" : "Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	private void salir() {
		if (!modoConsulta && FWJOptionPane.showQuestionMessage(JDialogAgregarModificarMateriaPrimaCantidad.this, "Va a salir sin grabar los cambios. Esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		} else {
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

	public List<T> getMateriasPrimasCantidadActuales() {
		return this.mps;
	}

	private List<F> getMatPrimasAgregadas() {
		return matPrimasYaAgregadas;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private class PanelMateriaPrimaCantidadUnidad extends JPanel {

		private static final long serialVersionUID = -5945475837116396808L;

		private T matPrimaCantidadActual;
		private JComboBox cmbMateriasPrimas;
		private FWJTextField txtCantidad;
		private JComboBox cmbUnidad;

		public PanelMateriaPrimaCantidadUnidad(T mp, String descripcionTipoMP) {
			super(new GridBagLayout());
			setBorder(BorderFactory.createTitledBorder(""));
			this.matPrimaCantidadActual = mp;
			add(new JLabel(descripcionTipoMP + ": "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			add(getCmbMateriasPrimas(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

			add(new JLabel("Cantidad: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			add(getTxtCantidad(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 2, 1, 1));

			add(new JLabel("Unidad: "), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			add(getCmbUnidad(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		}

		public String getNombreMP() {
			return ((F)getCmbMateriasPrimas().getSelectedItem()).getDescripcion();
		}

		public void loadData() {
			getCmbMateriasPrimas().setSelectedItem(matPrimaCantidadActual.getMateriaPrima());
			getTxtCantidad().setText(GenericUtils.getDecimalFormat2().format(matPrimaCantidadActual.getCantidad()));
			getCmbUnidad().setSelectedItem(matPrimaCantidadActual.getUnidad() == null ? EUnidad.PORCENTAJE : matPrimaCantidadActual.getUnidad());
		}

		public boolean isCompleto() {
			if (getCmbMateriasPrimas().getSelectedItem() == null) {
				return false;
			}
			return true;
		}

		public void capturarDatos() {
			matPrimaCantidadActual.setCantidad(Float.valueOf(getTxtCantidad().getText().replace(",", ".")));
			matPrimaCantidadActual.setMateriaPrima((F) getCmbMateriasPrimas().getSelectedItem());
			matPrimaCantidadActual.setUnidad((EUnidad) getCmbUnidad().getSelectedItem());
		}

		public boolean validar() {
			if (StringUtil.isNullOrEmpty(getTxtCantidad().getText())) {
				FWJOptionPane.showErrorMessage(this, "Debe ingresar la cantidad", "Error");
				getTxtCantidad().requestFocus();
				return false;
			}
			if (!GenericUtils.esNumerico(getTxtCantidad().getText())) {
				FWJOptionPane.showErrorMessage(this, "La cantidad debe ser numérica", "Error");
				getTxtCantidad().requestFocus();
				return false;
			}
			try {
				Float cantidad = Float.valueOf(getTxtCantidad().getText().replace(",", "."));
				if (cantidad <= 0) {
					FWJOptionPane.showErrorMessage(this, "La cantidad debe ser mayor a 0", "Error");
					getTxtCantidad().requestFocus();
					return false;
				}
			} catch (NumberFormatException nfe) {
				FWJOptionPane.showErrorMessage(this, "Error en el formato de la cantidad", "Error");
				getTxtCantidad().requestFocus();
				return false;
			}
			if (getCmbUnidad().getSelectedItem() == null) {
				FWJOptionPane.showErrorMessage(this, "Debe seleccionar la unidad.", "Error");
				return false;
			}
			return true;
		}

		private JComboBox getCmbMateriasPrimas() {
			if (cmbMateriasPrimas == null) {
				cmbMateriasPrimas = new JComboBox();
				CollectionUtils.filter(allMatPrimasPosibles, new Predicate() {
					public boolean evaluate(Object arg0) {
						F formulable = (F) arg0;
						if (formulable.equals(matPrimaCantidadActual.getMateriaPrima())) {
							return true;
						}
						return !getMatPrimasAgregadas().contains(formulable);
					}
				});
				GuiUtil.llenarCombo(cmbMateriasPrimas, allMatPrimasPosibles, true);
				cmbMateriasPrimas.setEnabled(!modoConsulta);
			}
			return cmbMateriasPrimas;
		}

		private JComboBox getCmbUnidad() {
			if (cmbUnidad == null) {
				cmbUnidad = new JComboBox();
				List<EUnidad> unidades = new ArrayList<EUnidad>();
				unidades.add(EUnidad.GRAMOS_POR_KILOS);
				unidades.add(EUnidad.GRAMOS_POR_LITROS);
				unidades.add(EUnidad.PORCENTAJE);
				GuiUtil.llenarCombo(cmbUnidad, unidades, true);
				cmbUnidad.setSelectedIndex(-1);
				cmbUnidad.setEnabled(!modoConsulta);
			}
			return cmbUnidad;
		}

		private FWJTextField getTxtCantidad() {
			if (txtCantidad == null) {
				txtCantidad = new FWJTextField();
				Font font = txtCantidad.getFont();
				txtCantidad.setFont(new Font(font.getName(), Font.BOLD, 30));
				txtCantidad.setForeground(Color.RED);
				txtCantidad.setEnabled(!modoConsulta);
			}
			return txtCantidad;
		}
	}
}