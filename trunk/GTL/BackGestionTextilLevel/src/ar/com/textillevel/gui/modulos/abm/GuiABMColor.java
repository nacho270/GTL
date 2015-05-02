package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.facade.api.remote.ColorFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMColor extends GuiABMListaTemplate{

	private static final long serialVersionUID = 3168808049165003001L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;
	
	private CLJTextField txtNombreColor;
	
	private ColorFacadeRemote colorFacade;
	private Color colorActual;
	
	public GuiABMColor(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Colores");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información del color", getTabDetalle());		
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
			panDetalle.add(new JLabel("Nombre: "), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombreColor(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
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
		List<Color> colorList = getColorFacade().getAllOrderByName();
		lista.removeAll();
		for(Color c : colorList) {
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setColorActual(new Color());
		getTxtNombreColor().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el color seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getColorFacade().remove(getColorActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			Color color = getColorFacade().save(getColorActual());
			lista.setSelectedValue(color, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(getTxtNombreColor().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el nombre del color.", "Advertencia");
			getTxtNombreColor().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getColorActual().setNombre(getTxtNombreColor().getText().toUpperCase());
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombreColor().requestFocus();
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un color", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setColorActual((Color)lista.getSelectedValue());
		limpiarDatos();
		if(getColorActual() != null) {
			getTxtNombreColor().setText(getColorActual().getNombre());
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombreColor().setText("");
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

	
	public Color getColorActual() {
		return colorActual;
	}
	
	public void setColorActual(Color colorActual) {
		this.colorActual = colorActual;
	}
	
	public ColorFacadeRemote getColorFacade() {
		if(colorFacade == null){
			colorFacade = GTLBeanFactory.getInstance().getBean2(ColorFacadeRemote.class);
		}
		return colorFacade;
	}

	public CLJTextField getTxtNombreColor() {
		if(txtNombreColor == null){
			txtNombreColor = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombreColor;
	}
}
