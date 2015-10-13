package ar.com.textillevel.gui.modulos.personal.abm.configuracion;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.commons.Art;
import ar.com.textillevel.modulos.personal.facade.api.remote.ArtFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMArt extends GuiABMListaTemplate {

	private static final long serialVersionUID = 1969434697882194662L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private FWJTextField txtNombre;

	private ArtFacadeRemote artFacade;
	private Art artActual;

	public GuiABMArt(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar ARTs");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información de la ART", getTabDetalle());		
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
			panDetalle.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
		}
		return panDetalle;
	}
	
	@Override
	public void cargarLista() {
		List<Art> artList = getArtFacade().getAllOrderByName();
		lista.removeAll();
		for(Art a : artList) {
			lista.addItem(a);
		}
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
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setArtActual(new Art());
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la ART seleccionada?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getArtFacade().remove(getArtActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombre().requestFocus();
			return true;
		} else {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar una ART", "Error");
			return false;
		}
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			capturarSetearDatos();
			Art art = getArtFacade().save(getArtActual());
			lista.setSelectedValue(art, true);
			return true;
		}
		return false;
	}
	
	private boolean validar() {
		if(getTxtNombre().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(this, "Debe completar el nombre de la ART.", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getArtActual().setNombre(getTxtNombre().getText().toUpperCase());
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	@Override
	public void limpiarDatos() {
		getTxtNombre().setText("");
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setArtActual((Art)lista.getSelectedValue());
		limpiarDatos();
		if(getArtActual() != null) {
			getTxtNombre().setText(getArtActual().getNombre());
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		// TODO Auto-generated method stub

	}

	public Art getArtActual() {
		return artActual;
	}

	public void setArtActual(Art artActual) {
		this.artActual = artActual;
	}

	public FWJTextField getTxtNombre() {
		if(txtNombre == null){
			txtNombre = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	public ArtFacadeRemote getArtFacade() {
		if(artFacade == null){
			artFacade = GTLPersonalBeanFactory.getInstance().getBean2(ArtFacadeRemote.class);
		}
		return artFacade;
	}
}
