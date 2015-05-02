package ar.com.textillevel.gui.modulos.abm.gente;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.enums.ETipoRubro;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.facade.api.remote.RubroPersonaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMRubro extends GuiABMListaTemplate {

	private static final long serialVersionUID = 5817318509804786243L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;
	private CLJTextField txtNombre;
	private JComboBox cmbTipoRubro;

	private Rubro rubro;
	private RubroPersonaFacadeRemote rubroFacadeRemote;

	public GuiABMRubro(Integer idModulo) {
		setHijoCreado(true);
		setTitle("Administrar Rubros");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Datos del Rubro", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if(tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Descripción:"), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(), createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Tipo:"), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoRubro(), createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		}
		return panDetalle;
	}

	private JComboBox getCmbTipoRubro() {
		if(cmbTipoRubro == null) {
			cmbTipoRubro = new JComboBox();
			List<ETipoRubro> tipoRubroList = new ArrayList<ETipoRubro>();
			tipoRubroList.add(ETipoRubro.PERSONA);
			tipoRubroList.add(ETipoRubro.PROVEEDOR);
			GuiUtil.llenarCombo(cmbTipoRubro, tipoRubroList, true);
			cmbTipoRubro.setSelectedIndex(-1);
		}
		return cmbTipoRubro;
	}

	private CLJTextField getTxtNombre() {
		if(txtNombre == null) {
			txtNombre = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	@Override
	public void cargarLista() {
		List<Rubro> rubroList = getRubroFacadeRemote().getAll();
		lista.clear();
		for(Rubro mp : rubroList) {
			lista.addItem(mp);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setRubroActual(new Rubro());
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(GuiABMRubro.this, "¿Está seguro que desea eliminar el rubro seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getRubroFacadeRemote().remove(getRubroActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			Rubro rubroRefresh = getRubroFacadeRemote().save(getRubroActual());
			lista.setSelectedValue(rubroRefresh, true);
			return true;
		}
		return false;
		
	}

	private void capturarSetearDatos() {
		getRubroActual().setNombre(getTxtNombre().getText().trim().toUpperCase());
		getRubroActual().setTipoRubro((ETipoRubro)getCmbTipoRubro().getSelectedItem());
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtNombre().getText())) {
			CLJOptionPane.showErrorMessage(GuiABMRubro.this, "Falta Completar el campo 'Nombre'", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getCmbTipoRubro().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(GuiABMRubro.this, "Falta seleccionar un tipo.", "Advertencia");
			return false;
		}
		return true;
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombre().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar una materia prima", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		Rubro selectedValue = (Rubro)lista.getSelectedValue();
		setRubroActual(selectedValue);
		if(getRubroActual() != null) {
			getTxtNombre().setText(getRubroActual().getNombre());
			getCmbTipoRubro().setSelectedItem(getRubroActual().getTipoRubro());
		} else {
			limpiarDatos();
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombre().setText(null);
		cmbTipoRubro.setSelectedIndex(-1);
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
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

	private Rubro getRubroActual() {
		return rubro;
	}

	private void setRubroActual(Rubro rubro) {
		this.rubro = rubro;
	}

	public RubroPersonaFacadeRemote getRubroFacadeRemote() {
		if(rubroFacadeRemote == null) {
			rubroFacadeRemote = GTLBeanFactory.getInstance().getBean2(RubroPersonaFacadeRemote.class);
		}
		return rubroFacadeRemote;
	}

}