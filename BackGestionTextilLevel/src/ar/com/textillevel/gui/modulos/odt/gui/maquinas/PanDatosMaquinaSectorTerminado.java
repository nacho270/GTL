package ar.com.textillevel.gui.modulos.odt.gui.maquinas;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.modulos.odt.gui.validacion.ValidadorCamposMaquinaHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorTerminado;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorTerminadoCalandra;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorTerminadoFraccionado;
import ar.com.textillevel.modulos.odt.entidades.maquinas.MaquinaSectorTerminadoSanford;

public class PanDatosMaquinaSectorTerminado extends PanDatosMaquinaCommon {

	private static final long serialVersionUID = 1L;
	private static final String PAN_CALANDRA = "CALANDRA";
	private static final String PAN_FRACCIONADO = "FRACCIONADO";
	private static final String PAN_SANDFORD = "SANDFORD";

	private JComboBox cmbTipoTerminado;
	private CardLayout cardLayout;
	private JPanel subPanelMaquina;
	private PanDatosMaquinaSectorCalandra panelCalandra;
	private PanDatosMaquinaSectorTerminadoSandford panelSandford;
	private PanDatosMaquinaSectorTerminadoFraccionado panelFraccionado;

	public PanDatosMaquinaSectorTerminado() {
		super();
		construct();
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(new JLabel(" TIPO: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		add(getCmbTipoTerminado(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));		
		add(getSubPanelMaquina(), createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 10, 5, 5), 2, 1, 1, 1));
	}

	private JPanel getSubPanelMaquina() {
		if(subPanelMaquina == null) {
			cardLayout = new CardLayout();
			subPanelMaquina = new JPanel(cardLayout);
			subPanelMaquina.add(PAN_CALANDRA, getPanelCalandra());
			subPanelMaquina.add(PAN_SANDFORD, getPanelSandford());
			subPanelMaquina.add(PAN_FRACCIONADO, getPanelFraccionado());
		}
		return subPanelMaquina;
	}

	private PanDatosMaquinaSectorCalandra getPanelCalandra() {
		if(panelCalandra == null) {
			panelCalandra = new PanDatosMaquinaSectorCalandra();
		}
		return panelCalandra;
	}

	private PanDatosMaquinaSectorTerminadoSandford getPanelSandford() {
		if(panelSandford == null) {
			panelSandford = new PanDatosMaquinaSectorTerminadoSandford();
		}
		return panelSandford;
	}

	private PanDatosMaquinaSectorTerminadoFraccionado getPanelFraccionado() {
		if(panelFraccionado == null) {
			panelFraccionado = new PanDatosMaquinaSectorTerminadoFraccionado();
		}
		return panelFraccionado;
	}

	private JComboBox getCmbTipoTerminado() {
		if(cmbTipoTerminado == null) {
			cmbTipoTerminado = new JComboBox();
			GuiUtil.llenarCombo(getCmbTipoTerminado(), Arrays.asList(ETipoMaquinaTerminado.values()), false);
			cmbTipoTerminado.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						ETipoMaquinaTerminado tipoMaq = getTipoMaquinaSelected();
						if(tipoMaq == ETipoMaquinaTerminado.CALANDRA) {
							MaquinaSectorTerminadoCalandra mstc = new MaquinaSectorTerminadoCalandra(); 
							getPanelCalandra().setMaquina(mstc);
						}
						if(tipoMaq == ETipoMaquinaTerminado.SANDFORD) {
							MaquinaSectorTerminadoSanford msts = new MaquinaSectorTerminadoSanford(); 
							getPanelSandford().setMaquina(msts);
						}
						if(tipoMaq == ETipoMaquinaTerminado.FRACCIONADO) {
							MaquinaSectorTerminadoFraccionado mstf = new MaquinaSectorTerminadoFraccionado(); 
							getPanelFraccionado().setMaquina(mstf);
						}
						cambiarPanel(tipoMaq);
					} else {
						getSubPanelMaquina().setVisible(false);
					}
				}


			});
		}
		return cmbTipoTerminado;
	}

	private ETipoMaquinaTerminado getTipoMaquinaSelected() {
		return (ETipoMaquinaTerminado)getCmbTipoTerminado().getSelectedItem();
	}

	public void limpiarDatos() {
		super.limpiarDatos();
		getCmbTipoTerminado().setSelectedIndex(-1);
		getPanelCalandra().limpiarDatos();
		getPanelSandford().limpiarDatos();
		getPanelFraccionado().limpiarDatos();
	}

	public void setMaquina(MaquinaSectorTerminado mst) {
		if(mst != null && mst instanceof MaquinaSectorTerminadoCalandra) {
			getCmbTipoTerminado().setSelectedItem(ETipoMaquinaTerminado.CALANDRA);
			getPanelCalandra().setMaquina((MaquinaSectorTerminadoCalandra)mst);
		}
		if(mst != null && mst instanceof MaquinaSectorTerminadoSanford) {
			getCmbTipoTerminado().setSelectedItem(ETipoMaquinaTerminado.SANDFORD);
			getPanelSandford().setMaquina((MaquinaSectorTerminadoSanford)mst);
		}
		if(mst != null && mst instanceof MaquinaSectorTerminadoFraccionado) {
			getCmbTipoTerminado().setSelectedItem(ETipoMaquinaTerminado.FRACCIONADO);
			getPanelFraccionado().setMaquina((MaquinaSectorTerminadoFraccionado)mst);
		}
	}

	public MaquinaSectorTerminado getMaquinaConDatosSeteados() {
		if(getTipoMaquinaSelected() == ETipoMaquinaTerminado.CALANDRA) {
			return getPanelCalandra().getMaquinaConDatosSeteados();
		}
		if(getTipoMaquinaSelected() == ETipoMaquinaTerminado.SANDFORD) {
			return getPanelSandford().getMaquinaConDatosSeteados();
		}
		if(getTipoMaquinaSelected() == ETipoMaquinaTerminado.FRACCIONADO) {
			return getPanelFraccionado().getMaquinaConDatosSeteados();
		}
		return null;
	}

	private void cambiarPanel(ETipoMaquinaTerminado tipoMaquina) {
		if(tipoMaquina == ETipoMaquinaTerminado.CALANDRA) {
			getSubPanelMaquina().setVisible(true);
			cardLayout.show(getSubPanelMaquina(), PAN_CALANDRA);
			return;
		}
		if(tipoMaquina == ETipoMaquinaTerminado.SANDFORD) {
			getSubPanelMaquina().setVisible(true);
			cardLayout.show(getSubPanelMaquina(), PAN_SANDFORD);
			return;
		}
		if(tipoMaquina == ETipoMaquinaTerminado.FRACCIONADO) {
			getSubPanelMaquina().setVisible(true);
			cardLayout.show(getSubPanelMaquina(), PAN_FRACCIONADO);
			return;
		}
		getSubPanelMaquina().setVisible(false);
	}

	private static enum ETipoMaquinaTerminado {

		CALANDRA("CALANDRA"),
		FRACCIONADO("FRACCIONADO"),
		SANDFORD("SANDFOR");

		@SuppressWarnings("unused")
		private String tipo;

		private ETipoMaquinaTerminado(String tipo) {
			this.tipo = tipo;
		}

	}

	@Override
	public ValidadorCamposMaquinaHandler configureValidadorHandler() {
		ETipoMaquinaTerminado tipoMaquina = getTipoMaquinaSelected();
		if(tipoMaquina == ETipoMaquinaTerminado.CALANDRA) {
			return getPanelCalandra().configureValidadorHandler();
		}
		if(tipoMaquina == ETipoMaquinaTerminado.SANDFORD) {
			return getPanelSandford().configureValidadorHandler();
		}
		if(tipoMaquina == ETipoMaquinaTerminado.FRACCIONADO) {
			return getPanelFraccionado().configureValidadorHandler();
		}
		throw new IllegalStateException("No existe un tipo de máquina para " + tipoMaquina);
	}

	public void setDisabledComboTipoTerminado() {
		getCmbTipoTerminado().setEnabled(false);
	}

} 