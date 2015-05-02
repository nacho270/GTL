package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVentaDiferida;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMCondicionDeVenta extends GuiABMListaTemplate {

	private static final long serialVersionUID = -7099550007321815283L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;
	private CLJTextField txtNombreCondicion;
	private CLJNumericTextField txtDiasIniciales;
	private CLJNumericTextField txtDiasFinales;

	private CondicionDeVenta condicionDeVentaActual;
	private CondicionDeVentaFacadeRemote condicionFacade;

	public GuiABMCondicionDeVenta(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar condiciones de venta");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información de la condición de venta", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Nombre: "), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombreCondicion(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Días iniciales: "), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtDiasIniciales(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Días finales: "), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtDiasFinales(), createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panDetalle;
	}

	private GridBagConstraints createGridBagConstraints(int x, int y, int posicion, int fill, Insets insets, int cantX, int cantY, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = posicion;
		gbc.fill = fill;
		gbc.insets = insets;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = cantX;
		gbc.gridheight = cantY;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		return gbc;
	}

	@Override
	public void cargarLista() {
		List<CondicionDeVenta> conds = getCondicionFacade().getAllOrderByName();
		lista.removeAll();
		for (CondicionDeVenta c : conds) {
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setCondicionDeVentaActual(new CondicionDeVenta());
		getTxtNombreCondicion().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la condición de venta seleccionada?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getCondicionFacade().remove(getCondicionDeVentaActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if (validar()) {
			capturarSetearDatos();
			CondicionDeVenta condicion = getCondicionFacade().save(getCondicionDeVentaActual());
			lista.setSelectedValue(condicion, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if (getTxtNombreCondicion().getText().trim().length() == 0) {
			CLJOptionPane.showErrorMessage(this, "Debe completar el nombre de la condición.", "Advertencia");
			getTxtNombreCondicion().requestFocus();
			return false;
		}

		if (getTxtDiasIniciales().getValueWithNull() != null) {
			if (getTxtDiasFinales().getValueWithNull() == null) {
				CLJOptionPane.showErrorMessage(this, "Debe completar los días finales.", "Advertencia");
				getTxtDiasFinales().requestFocus();
				return false;
			}
		}
		return true;
	}

	private void capturarSetearDatos() {
		getCondicionDeVentaActual().setNombre(getTxtNombreCondicion().getText().toUpperCase());
		if (getTxtDiasFinales().getValueWithNull() != null) {
			CondicionDeVenta c = getCondicionDeVentaActual();
			CondicionDeVentaDiferida cd = new CondicionDeVentaDiferida();
			cd.setNombre(c.getNombre());
			cd.setId(c.getId());
			setCondicionDeVentaActual(cd);
			((CondicionDeVentaDiferida) getCondicionDeVentaActual()).setDiasFinales(getTxtDiasFinales().getValue());
			((CondicionDeVentaDiferida) getCondicionDeVentaActual()).setDiasIniciales(getTxtDiasIniciales().getValue());
			if (((CondicionDeVentaDiferida) getCondicionDeVentaActual()).getDiasIniciales() == null) {
				((CondicionDeVentaDiferida) getCondicionDeVentaActual()).setDiasIniciales(0);
			}
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombreCondicion().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar una condición", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setCondicionDeVentaActual((CondicionDeVenta) lista.getSelectedValue());
		limpiarDatos();
		if (getCondicionDeVentaActual() != null) {
			getTxtNombreCondicion().setText(getCondicionDeVentaActual().getNombre());
			if (getCondicionDeVentaActual() instanceof CondicionDeVentaDiferida) {
				getTxtDiasIniciales().setText(String.valueOf(((CondicionDeVentaDiferida) getCondicionDeVentaActual()).getDiasIniciales()));
				getTxtDiasFinales().setText(String.valueOf(((CondicionDeVentaDiferida) getCondicionDeVentaActual()).getDiasFinales()));
			}
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombreCondicion().setText("");
		getTxtDiasIniciales().setText("");
		getTxtDiasFinales().setText("");
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	public CondicionDeVenta getCondicionDeVentaActual() {
		return condicionDeVentaActual;
	}

	public void setCondicionDeVentaActual(CondicionDeVenta condicionDeVentaActual) {
		this.condicionDeVentaActual = condicionDeVentaActual;
	}

	public CondicionDeVentaFacadeRemote getCondicionFacade() {
		if (condicionFacade == null) {
			condicionFacade = GTLBeanFactory.getInstance().getBean2(CondicionDeVentaFacadeRemote.class);
		}
		return condicionFacade;
	}

	public CLJTextField getTxtNombreCondicion() {
		if (txtNombreCondicion == null) {
			txtNombreCondicion = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombreCondicion;
	}

	public CLJNumericTextField getTxtDiasIniciales() {
		if (txtDiasIniciales == null) {
			txtDiasIniciales = new CLJNumericTextField();
		}
		return txtDiasIniciales;
	}

	public CLJNumericTextField getTxtDiasFinales() {
		if (txtDiasFinales == null) {
			txtDiasFinales = new CLJNumericTextField();
		}
		return txtDiasFinales;
	}
}
