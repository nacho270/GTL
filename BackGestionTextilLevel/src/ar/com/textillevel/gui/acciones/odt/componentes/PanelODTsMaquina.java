package ar.com.textillevel.gui.acciones.odt.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.VerticalFlowLayout;
import ar.com.textillevel.gui.acciones.odt.JFrameVisionGeneralProduccion.EModoVisualizacionEstadoProduccion;
import ar.com.textillevel.gui.acciones.odt.event.BotonADerechaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonAIzquierdaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonCambiarMaquinaActionListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonCambiarMaquinaEvent;
import ar.com.textillevel.gui.acciones.odt.event.BotonSubirBajarActionListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonSubirBajarEvent;
import ar.com.textillevel.gui.acciones.odt.event.WorkFlowODTEvent;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.to.EstadoActualMaquinaTO;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class PanelODTsMaquina extends JPanel {

	private static final long serialVersionUID = 4525243205571814312L;

	private final EventListenerList listeners = new EventListenerList();
	private Map<EAvanceODT, PanelTablaODTMaquina> mapaTablas;
	
	private JPanel panelTablasEstadoAvance;
	private JPanel panelBotones;
	private JButton btnCambiarMaquina;
	private JButton btnPasarAIzquierda;
	private JButton btnPasarADerecha;
	
	private EstadoActualMaquinaTO estadoMaquina;
	private final Frame padre;
	private boolean ultima;
	
	public PanelODTsMaquina(Frame padre, EstadoActualMaquinaTO estadoMaquina, boolean ultima){
		this.estadoMaquina = estadoMaquina;
		this.padre = padre;
		this.ultima = ultima;
		construct();
		for(EAvanceODT e : mapaTablas.keySet()){
			List<ODTTO> odts = estadoMaquina.getOdtsPorEstado().get(e);
			Collections.sort(odts, new Comparator<ODTTO>() {
				public int compare(ODTTO o1, ODTTO o2) {
					if(o1.getOrdenEnMaquina() == null && o2.getOrdenEnMaquina() == null) {
						return 0;
					} else if(o1.getOrdenEnMaquina() == null && o2.getOrdenEnMaquina() != null) {
						return -1;
					} else if(o1.getOrdenEnMaquina() != null && o2.getOrdenEnMaquina() == null) {
						return 1;
					} else {
						return o1.getOrdenEnMaquina().compareTo(o2.getOrdenEnMaquina());
					}
				}
			});
			if(odts!=null && !odts.isEmpty()){
				mapaTablas.get(e).agregarElementos(odts);
			}
		}
	}
	
	private void construct() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(""));
		add(createLabelTitulo(), BorderLayout.NORTH);
		add(getPanelTablasEstadoAvance(), BorderLayout.CENTER);
		add(getPanelBotones(), BorderLayout.SOUTH);
	}
	
	private JLabel createLabelTitulo() {
		JLabel lblTitlo = new JLabel(getEstadoMaquina().getMaquina().getNombre().toUpperCase());
		Font fuente = lblTitlo.getFont();
		Font fuenteNueva = new Font(fuente.getFontName(), Font.BOLD, 40);
		lblTitlo.setFont(fuenteNueva);
		lblTitlo.setHorizontalAlignment(JLabel.CENTER);
		lblTitlo.setForeground(Color.RED.darker());
		return lblTitlo;
	}
	
	public JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelBotones.add(getBtnCambiarMaquina());
		}
		return panelBotones;
	}

	public JPanel getPanelTablasEstadoAvance() {
		if (panelTablasEstadoAvance == null) {
			panelTablasEstadoAvance = new JPanel(new GridBagLayout());
			panelTablasEstadoAvance.add(getMapaTablas().get(EAvanceODT.POR_COMENZAR), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 1, 3, 1, 1));
			
			JPanel panBotones = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 0, 5));
			panBotones.add(getBtnPasarAIzquierda());
			panBotones.add(getBtnPasarADerecha());
			
			panelTablasEstadoAvance.add(panBotones, GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 1, 1, 0, 0));
			panelTablasEstadoAvance.add(getMapaTablas().get(EAvanceODT.FINALIZADO), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 1, 3, 1, 1));
		}
		return panelTablasEstadoAvance;
	}
	
	private class PanelTablaODTMaquina extends PanelTablaODT{

		private static final long serialVersionUID = 4126642857601080280L;

		public PanelTablaODTMaquina(Frame padre, EAvanceODT tipoAvance, boolean ultima) {
			super(padre, tipoAvance, ultima, EModoVisualizacionEstadoProduccion.DESGLOSE_POR_AVANCE);
			getBotonModificar().setVisible(false);
			getBotonSubir().setVisible(tipoAvance == EAvanceODT.POR_COMENZAR);
			getBotonBajar().setVisible(tipoAvance == EAvanceODT.POR_COMENZAR);
			setModoConsulta(false);
		}
		
		@Override
		protected void botonBajarPresionado() {
			final BotonSubirBajarActionListener[] l = listeners.getListeners(BotonSubirBajarActionListener.class);
			final BotonSubirBajarEvent bsbe = new BotonSubirBajarEvent(BotonSubirBajarEvent.BAJAR,getODTTOSeleccionada(EAvanceODT.POR_COMENZAR));
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (int i = 0; i < l.length; i++) {
						l[i].botonSubirBajarPresionado(bsbe);
					}
				}
			});
		}

		@Override
		protected void botonSubirPresionado() {
			final BotonSubirBajarActionListener[] l = listeners.getListeners(BotonSubirBajarActionListener.class);
			final BotonSubirBajarEvent bsbe = new BotonSubirBajarEvent(BotonSubirBajarEvent.SUBIR,getODTTOSeleccionada(EAvanceODT.POR_COMENZAR));
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (int i = 0; i < l.length; i++) {
						l[i].botonSubirBajarPresionado(bsbe);
					}
				}
			});
		}
		
		@Override
		protected void habilitarBotones(int rowSelected) {
			ODTTO elemento = getElemento(rowSelected);
			EAvanceODT tipoAvance = elemento.getAvance();
			getBotonSubir().setEnabled(rowSelected>0);
			getBotonBajar().setEnabled(rowSelected<getTabla().getRowCount()-1);
			getBtnCambiarMaquina().setEnabled(rowSelected!=-1 && tipoAvance!=null && tipoAvance == EAvanceODT.POR_COMENZAR);
			if (tipoAvance == EAvanceODT.POR_COMENZAR) {
				getBtnPasarADerecha().setEnabled(rowSelected!=-1);
				getBtnPasarAIzquierda().setEnabled(false);
			} else if (tipoAvance == EAvanceODT.FINALIZADO) {
				getBtnPasarADerecha().setEnabled(false);
				getBtnPasarAIzquierda().setEnabled(rowSelected!=-1);
			} else {
				//no deberia llegar por ahora. Cuando este la automatizacion no deberian estar mas los botones
			}
		}
	}
	
	public Map<EAvanceODT, PanelTablaODTMaquina> getMapaTablas() {
		if (mapaTablas == null) {
			mapaTablas = new HashMap<EAvanceODT,PanelTablaODTMaquina>();
			Set<EAvanceODT> keySet = getEstadoMaquina().getOdtsPorEstado().keySet();
			for (EAvanceODT e : keySet) {
				mapaTablas.put(e, new PanelTablaODTMaquina(padre,e,isUltima()));
			}
		}
		return mapaTablas;
	}

	public EstadoActualMaquinaTO getEstadoMaquina() {
		return estadoMaquina;
	}

	public void setEstadoMaquina(EstadoActualMaquinaTO estadoMaquina) {
		this.estadoMaquina = estadoMaquina;
	}

	public JButton getBtnCambiarMaquina() {
		if(btnCambiarMaquina == null){
			btnCambiarMaquina = new JButton("Cambiar de máquina");
			btnCambiarMaquina.setEnabled(false);
			btnCambiarMaquina.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final BotonCambiarMaquinaActionListener[] l = listeners.getListeners(BotonCambiarMaquinaActionListener.class);
					final BotonCambiarMaquinaEvent wfe = new BotonCambiarMaquinaEvent(getODTTOSeleccionada(EAvanceODT.POR_COMENZAR),getEstadoMaquina().getMaquina().getIdTipoMaquina(),getEstadoMaquina().getMaquina().getId());
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							for (int i = 0; i < l.length; i++) {
								l[i].botonCambiarMaquinaPresionado(wfe);
							}
						}
					});
				}
			});
		}
		return btnCambiarMaquina;
	}

	public JButton getBtnPasarAIzquierda() {
		if (btnPasarAIzquierda == null) {
			btnPasarAIzquierda = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_left.png", "ar/com/textillevel/imagenes/btn_left_des.png");
			btnPasarAIzquierda.setEnabled(false);
			btnPasarAIzquierda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final BotonAIzquierdaEventListener[] l = listeners.getListeners(BotonAIzquierdaEventListener.class);
					final WorkFlowODTEvent wfe = new WorkFlowODTEvent(getODTTOSeleccionada(EAvanceODT.FINALIZADO),EAvanceODT.POR_COMENZAR,false);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							for (int i = 0; i < l.length; i++) {
								l[i].botonIzquierdaPersionado(wfe);
							}
						}
					});
				}
			});
		}
		return btnPasarAIzquierda;
	}

	public JButton getBtnPasarADerecha() {
		if (btnPasarADerecha == null) {
			btnPasarADerecha = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_right.png", "ar/com/textillevel/imagenes/btn_right_des.png");
			btnPasarADerecha.setEnabled(false);
			btnPasarADerecha.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final BotonADerechaEventListener[] l = listeners.getListeners(BotonADerechaEventListener.class);
					final WorkFlowODTEvent wfe = new WorkFlowODTEvent(getODTTOSeleccionada(EAvanceODT.POR_COMENZAR),EAvanceODT.FINALIZADO,isUltima());
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							for (int i = 0; i < l.length; i++) {
								l[i].botonDerechaPersionado(wfe);
							}
						}
					});
				}
			});
		}
		return btnPasarADerecha;
	}

	public void addBotonSubirBajarActionListener(BotonSubirBajarActionListener listener) {
		listeners.add(BotonSubirBajarActionListener.class, listener);
	}
	
	public void addBotonCambiarMaquinaActionListener(BotonCambiarMaquinaActionListener listener) {
		listeners.add(BotonCambiarMaquinaActionListener.class, listener);
	}
	
	public void addBotonIzquierdaActionListener(BotonAIzquierdaEventListener listener) {
		listeners.add(BotonAIzquierdaEventListener.class, listener);
	}

	public void addBotonDerechaActionListener(BotonADerechaEventListener listener) {
		listeners.add(BotonADerechaEventListener.class, listener);
	}
	
	private ODTTO getODTTOSeleccionada(EAvanceODT tipoAvance) {
		PanelTablaODT panelTablaODT = getMapaTablas().get(tipoAvance);
		return panelTablaODT.getElemento(panelTablaODT.getTabla().getSelectedRow());
	}

	public boolean isUltima() {
		return ultima;
	}

	public void setUltima(boolean ultima) {
		this.ultima = ultima;
	}
}
