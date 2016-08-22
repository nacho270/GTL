package ar.com.textillevel.gui.modulos.odt.gui.maquinas;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorCosido;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorEstampado;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorHumedo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorSeco;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorTerminado;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.MaquinaFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.TipoMaquinaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMMaquina extends GuiABMListaTemplate {

	private static final long serialVersionUID = 5817318509804786243L;

	private static final int MAX_LONGITUD_NOMBRE = 50;
	private static final String PAN_HUMEDO = "HUMEDO";
	private static final String PAN_SECO = "SECO";
	private static final String PAN_ESTAMPERIA = "ESTAMPERIA";
	private static final String PAN_COSIDO = "COSIDO";
	private static final String PAN_TERMINADO = "TERMINADO";

	private JPanel tabDetalle;
	private JPanel panDetalle;
	private FWJTextField txtNombre;
	private JComboBox cmbTipoMaquina;

	private CardLayout cardLayout;
	private JPanel panDatosMaquina;
	private PanDatosMaquinaSectorHumedo panHumedo;
	private PanDatosMaquinaSectorSeco panSeco;
	private PanDatosMaquinaSectorEstamperia panEstamperia;
	private PanDatosMaquinaSectorCosido panCosido;
	private PanDatosMaquinaSectorTerminado panTerminado;

	private Maquina maquina;
	private MaquinaFacadeRemote maquinaFacade;
	private TipoMaquinaFacadeRemote tipoMaquinaFacade;

	public GuiABMMaquina(Integer idModulo) {
		setHijoCreado(true);
		setTitle("Administrar Máquinas");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Datos de la Máquina", getTabDetalle());
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
			panDetalle.add(new JLabel("NOMBRE:"), createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(), createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(new JLabel("TIPO DE MÁQUINA:"), createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoMaquina(), createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanDatosMaquina(), createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 10, 5, 5), 2, 1, 1, 1));			
		}
		return panDetalle;
	}

	private JComboBox getCmbTipoMaquina() {
		if(cmbTipoMaquina == null) {
			cmbTipoMaquina = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoMaquina, getTipoMaquinaFacade().getAllOrderByOrden(), true);
			cmbTipoMaquina.setSelectedIndex(-1);
			cmbTipoMaquina.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent ie) {
					if(ie.getStateChange() == ItemEvent.SELECTED) {
						TipoMaquina tp = (TipoMaquina)getCmbTipoMaquina().getSelectedItem();
						if(tp.getSectorMaquina() == ESectorMaquina.SECTOR_HUMEDO) {
							MaquinaSectorHumedo msh = new MaquinaSectorHumedo();
							getPanelHumedo().setMaquina(msh);
						}
						if(tp.getSectorMaquina() == ESectorMaquina.SECTOR_SECO) {
							MaquinaSectorSeco mss = new MaquinaSectorSeco();
							getPanelSeco().setMaquina(mss);
						}
						if(tp.getSectorMaquina() == ESectorMaquina.SECTOR_ESTAMPERIA) {
							MaquinaSectorEstampado mse = new MaquinaSectorEstampado();
							getPanelEstamperia().setMaquina(mse);
						}
						if(tp.getSectorMaquina() == ESectorMaquina.SECTOR_COSIDO) {
							MaquinaSectorCosido msc = new MaquinaSectorCosido();
							getPanelCosido().setMaquina(msc);
						}
						if(tp.getSectorMaquina() == ESectorMaquina.SECTOR_TERMINADO) {
							getPanelTerminado().setMaquina(null);
						}
						cambiarPanel(tp.getSectorMaquina());
					} else {
						getPanDatosMaquina().setVisible(false);
					}
				}

			});
		}
		return cmbTipoMaquina;
	}

	private FWJTextField getTxtNombre() {
		if(txtNombre == null) {
			txtNombre = new FWJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	@Override
	public void cargarLista() {
		List<Maquina> rubroList = getMaquinaFacade().getAllSorted();
		lista.clear();
		for(Maquina mp : rubroList) {
			lista.addItem(mp);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
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
			if(FWJOptionPane.showQuestionMessage(GuiABMMaquina.this, "¿Está seguro que desea eliminar la máquina seleccionada?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getMaquinaFacade().remove(getMaquinaActual());
				lista.setSelectedIndex(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			try {
				Maquina maquinaRefresh = getMaquinaFacade().save(capturarSetearDatos());
				lista.setSelectedValue(maquinaRefresh, true);
				return true;
			} catch (ValidacionException e) {
				FWJOptionPane.showErrorMessage(GuiABMMaquina.this, StringW.wordWrap(e.getMensajeError()), "Error");
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	private Maquina capturarSetearDatos() {
		Maquina maquina = null;
		ESectorMaquina sectorMaquina = ((TipoMaquina)getCmbTipoMaquina().getSelectedItem()).getSectorMaquina();
		if(sectorMaquina == ESectorMaquina.SECTOR_HUMEDO) {
			maquina = getPanelHumedo().getMaquinaConDatosSeteados();
		}
		if(sectorMaquina == ESectorMaquina.SECTOR_SECO) {
			maquina = getPanelSeco().getMaquinaConDatosSeteados();
		}
		if(sectorMaquina == ESectorMaquina.SECTOR_ESTAMPERIA) {
			maquina = getPanelEstamperia().getMaquinaConDatosSeteados();
		}
		if(sectorMaquina == ESectorMaquina.SECTOR_COSIDO) {
			maquina = getPanelCosido().getMaquinaConDatosSeteados();
		}
		if(sectorMaquina == ESectorMaquina.SECTOR_TERMINADO) {
			maquina = getPanelTerminado().getMaquinaConDatosSeteados();
		}
		maquina.setNombre(getTxtNombre().getText().trim().toUpperCase());
		maquina.setTipoMaquina((TipoMaquina)getCmbTipoMaquina().getSelectedItem());
		return maquina;
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtNombre().getText())) {
			FWJOptionPane.showErrorMessage(GuiABMMaquina.this, "Falta Completar el campo 'Nombre'", "Advertencia");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getCmbTipoMaquina().getSelectedItem() == null) {
			FWJOptionPane.showErrorMessage(GuiABMMaquina.this, "Falta seleccionar un tipo de máquina.", "Advertencia");
			return false;
		}

		ESectorMaquina sectorMaquina = ((TipoMaquina)getCmbTipoMaquina().getSelectedItem()).getSectorMaquina();
		if(sectorMaquina == ESectorMaquina.SECTOR_HUMEDO) {
			return getPanelHumedo().validar();
		}
		if(sectorMaquina == ESectorMaquina.SECTOR_SECO) {
			return getPanelSeco().validar();
		}
		if(sectorMaquina == ESectorMaquina.SECTOR_ESTAMPERIA) {
			return getPanelEstamperia().validar();
		}
		if(sectorMaquina == ESectorMaquina.SECTOR_COSIDO) {
			return getPanelCosido().validar();
		}
		if(sectorMaquina == ESectorMaquina.SECTOR_TERMINADO) {
			return getPanelTerminado().validar();
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
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar una máquina", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setMaquinaActual((Maquina)lista.getSelectedValue());
		getPanDatosMaquina().setVisible(true);
		if(getMaquinaActual() != null) {
			maquina = getMaquinaFacade().getByIdEager(getMaquinaActual().getId());
			getTxtNombre().setText(getMaquinaActual().getNombre());
			getCmbTipoMaquina().setSelectedItem(getMaquinaActual().getTipoMaquina());

			if(getMaquinaActual() instanceof MaquinaSectorHumedo) {
				MaquinaSectorHumedo msh = (MaquinaSectorHumedo)getMaquinaActual();
				getPanelHumedo().setMaquina(msh);
			}

			if(getMaquinaActual() instanceof MaquinaSectorSeco) {
				MaquinaSectorSeco mss = (MaquinaSectorSeco)getMaquinaActual();
				getPanelSeco().setMaquina(mss);
			}

			if(getMaquinaActual() instanceof MaquinaSectorEstampado) {
				MaquinaSectorEstampado mse = (MaquinaSectorEstampado)getMaquinaActual();
				getPanelEstamperia().setMaquina(mse);
			}

			if(getMaquinaActual() instanceof MaquinaSectorCosido) {
				MaquinaSectorCosido mse = (MaquinaSectorCosido)getMaquinaActual();
				getPanelCosido().setMaquina(mse);
			}

			if(getMaquinaActual() instanceof MaquinaSectorTerminado) {
				MaquinaSectorTerminado mst = (MaquinaSectorTerminado)getMaquinaActual();
				getPanelTerminado().setMaquina(mst);
			}

		} else {
			limpiarDatos();
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtNombre().setText(null);
		getCmbTipoMaquina().setSelectedIndex(-1);
		getPanelHumedo().limpiarDatos();
		getPanelSeco().limpiarDatos();
		getPanelEstamperia().limpiarDatos();
		getPanelCosido().limpiarDatos();
		getPanelTerminado().limpiarDatos();
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		TipoMaquina tp = (TipoMaquina)getCmbTipoMaquina().getSelectedItem();
		if(tp != null) {
			cambiarPanel(tp.getSectorMaquina());
		}
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
		if(estado && !isAgregar()) {//No se puede cambiar de tipo de máquina si es una modificación
			getCmbTipoMaquina().setEnabled(false);
			
			ESectorMaquina sectorMaquina = ((TipoMaquina)getCmbTipoMaquina().getSelectedItem()).getSectorMaquina();
			if(sectorMaquina == ESectorMaquina.SECTOR_TERMINADO) {
				getPanelTerminado().setDisabledComboTipoTerminado();
			}
		}
	}

	private JPanel getPanDatosMaquina() {
		if(panDatosMaquina == null) {
			panDatosMaquina = new JPanel();
			cardLayout = new CardLayout();
			panDatosMaquina = new JPanel(cardLayout);
			panDatosMaquina.setBorder(BorderFactory.createTitledBorder("Datos Máquina"));
			panDatosMaquina.add(PAN_HUMEDO, getPanelHumedo());
			panDatosMaquina.add(PAN_SECO, getPanelSeco());
			panDatosMaquina.add(PAN_ESTAMPERIA, getPanelEstamperia());
			panDatosMaquina.add(PAN_COSIDO, getPanelCosido());
			panDatosMaquina.add(PAN_TERMINADO, getPanelTerminado());
		}
		return panDatosMaquina;
	}

	private PanDatosMaquinaSectorSeco getPanelSeco() {
		if(panSeco == null) {
			panSeco = new PanDatosMaquinaSectorSeco();
		}
		return panSeco;
	}

	private PanDatosMaquinaSectorHumedo getPanelHumedo() {
		if(panHumedo == null) {
			panHumedo = new PanDatosMaquinaSectorHumedo();
		}
		return panHumedo;
	} 

	private PanDatosMaquinaSectorEstamperia getPanelEstamperia() {
		if(panEstamperia == null) {
			panEstamperia = new PanDatosMaquinaSectorEstamperia();
		}
		return panEstamperia;
	}

	private PanDatosMaquinaSectorCosido getPanelCosido() {
		if(panCosido == null) {
			panCosido = new PanDatosMaquinaSectorCosido();
		}
		return panCosido;
	}

	private PanDatosMaquinaSectorTerminado getPanelTerminado() {
		if(panTerminado == null) {
			panTerminado = new PanDatosMaquinaSectorTerminado();
		}
		return panTerminado;
	}

	private void cambiarPanel(ESectorMaquina sector) {
		if(sector == ESectorMaquina.SECTOR_HUMEDO){
			getPanDatosMaquina().setVisible(true);
			cardLayout.show(getPanDatosMaquina(), PAN_HUMEDO);
			return;
		}
		if(sector == ESectorMaquina.SECTOR_SECO){
			getPanDatosMaquina().setVisible(true);
			cardLayout.show(getPanDatosMaquina(), PAN_SECO);
			return;
		}
		if(sector == ESectorMaquina.SECTOR_ESTAMPERIA){
			getPanDatosMaquina().setVisible(true);
			cardLayout.show(getPanDatosMaquina(), PAN_ESTAMPERIA);
			return;
		}
		if(sector == ESectorMaquina.SECTOR_COSIDO){
			getPanDatosMaquina().setVisible(true);
			cardLayout.show(getPanDatosMaquina(), PAN_COSIDO);
			return;
		}
		if(sector == ESectorMaquina.SECTOR_TERMINADO){
			getPanDatosMaquina().setVisible(true);
			cardLayout.show(getPanDatosMaquina(), PAN_TERMINADO);
			return;
		}

		getPanDatosMaquina().setVisible(false);
	}

	private Maquina getMaquinaActual() {
		return maquina;
	}

	private void setMaquinaActual(Maquina maquina) {
		this.maquina = maquina;
	}

	private MaquinaFacadeRemote getMaquinaFacade() {
		if(maquinaFacade == null) {
			maquinaFacade = GTLBeanFactory.getInstance().getBean2(MaquinaFacadeRemote.class);
		}
		return maquinaFacade;
	}

	private TipoMaquinaFacadeRemote getTipoMaquinaFacade() {
		if(tipoMaquinaFacade == null) {
			tipoMaquinaFacade = GTLBeanFactory.getInstance().getBean2(TipoMaquinaFacadeRemote.class);
		}
		return tipoMaquinaFacade;
	}

}