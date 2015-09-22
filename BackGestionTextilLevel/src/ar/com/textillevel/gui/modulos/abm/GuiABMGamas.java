package ar.com.textillevel.gui.modulos.abm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.clarin.fwjava.componentes.CLCheckBoxList;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.articulos.Color;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.facade.api.remote.ColorFacadeRemote;
import ar.com.textillevel.facade.api.remote.GamaColorFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMGamas extends GuiABMListaTemplate {

	private static final long serialVersionUID = -6271525020719548718L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	
	private JPanel tabDetalle;
	private JPanel panDetalle;
	
	private CLJTextField txtNombreGama;
	private CLCheckBoxList<Color> listaColores;
	
	private ColorFacadeRemote colorFacade;
	private GamaColorFacadeRemote gamaColorFacade;
	private GamaColor gamaActual;
	
	public GuiABMGamas(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Gamas de colores");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información de la gama", getTabDetalle());		
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
			panDetalle.add(getTxtNombreGama(),  createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Colores: "), createGridBagConstraints(0, 1,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getScrollLista(),  createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 1));
		}
		return panDetalle;
	}

	private JScrollPane getScrollLista() {
		JScrollPane jsp = new JScrollPane(getListaColores());
		jsp.setPreferredSize(new Dimension(100, 450));
		return jsp;
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
		List<GamaColor> gamaColorList = getGamaColorFacade().getAllOrderByName();
		lista.removeAll();
		for(GamaColor c : gamaColorList) {
			lista.addItem(c);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setGamaActual(new GamaColor());
		getTxtNombreGama().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la gama seleccionada?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getGamaColorFacade().remove(getGamaActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			GamaColor color = getGamaColorFacade().save(getGamaActual());
			cargarLista();
			cargarColores();
			lista.setSelectedValue(color, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(getTxtNombreGama().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe completar el nombre de la gama.", "Advertencia");
			getTxtNombreGama().requestFocus();
			return false;
		}

		if(getListaColores().getSelectedValues().length==0){
			CLJOptionPane.showErrorMessage(this, "Debe elegir al menos un color para la gama.", "Advertencia");
			return false;
		}
		
		
		if(!okColores(getListaColores().getSelectedValues())){
			getListaColores().requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean okColores(Object[] selectedValues) {
		boolean ok = true;
		for(int i = 0; i<selectedValues.length;i++){
			Color color =(Color) selectedValues[i];
			if(color.getGama()!=null && !color.getGama().getId().equals(getGamaActual().getId())){
				CLJOptionPane.showErrorMessage(this, "El color " + color.getNombre() + " ya esta asociado a una gama.", "Advertencia");
				ok = false;
				break;
			}
		}
		return ok;
	}

	private void capturarSetearDatos() {
		getGamaActual().setNombre(getTxtNombreGama().getText().toUpperCase());
		Object[] coloresSeleccionados = getListaColores().getSelectedValues();
		List<Color> colores = new ArrayList<Color>();
		for(int i = 0; i<coloresSeleccionados.length;i++){
			colores.add((Color)coloresSeleccionados[i]);
		}
		getGamaActual().setColores(colores);
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombreGama().requestFocus();
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
		setGamaActual((GamaColor)lista.getSelectedValue());
		limpiarDatos();
		if(getGamaActual() != null) {
			getTxtNombreGama().setText(getGamaActual().getNombre());
			int i = -1;
			for(Object o : getListaColores().getItemList()){
				Color c = (Color) o;
				i++;
				for(Color color : getGamaActual().getColores()){
					if(color.getId().equals(c.getId())){
						getListaColores().setSelectedIndex(i);
					}
				}
			}
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombreGama().setText("");
		getListaColores().setAllSelectedItems(false);
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		cargarColores();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	private void cargarColores() {
		List<Color> colorList = getColorFacade().getAllOrderByNameGamaEager();
		getListaColores().removeAll();
		Object[] colores = new Object[colorList.size()];
		int i=0;
		for(Color c : colorList) {
			colores[i++] = c;
		}
		getListaColores().setValues(colores);
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);		
	}

	public GamaColor getGamaActual() {
		return gamaActual;
	}

	public void setGamaActual(GamaColor gamaActual) {
		this.gamaActual = gamaActual;
	}

	public CLJTextField getTxtNombreGama() {
		if(txtNombreGama == null){
			txtNombreGama = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombreGama;
	}

	public CLCheckBoxList<Color> getListaColores() {
		if(listaColores == null){
			listaColores = new CLCheckBoxList<Color>();
		}
		return listaColores;
	}

	public GamaColorFacadeRemote getGamaColorFacade() {
		if(gamaColorFacade == null){
			gamaColorFacade = GTLBeanFactory.getInstance().getBean2(GamaColorFacadeRemote.class);
		}
		return gamaColorFacade;
	}
	
	public ColorFacadeRemote getColorFacade() {
		if(colorFacade == null){
			colorFacade = GTLBeanFactory.getInstance().getBean2(ColorFacadeRemote.class);
		}
		return colorFacade;
	}
}
