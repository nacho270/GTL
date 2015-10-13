package ar.com.textillevel.gui.acciones.odt.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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
import ar.com.textillevel.gui.acciones.odt.JFrameVisionGeneralProduccion.ModeloFiltro;
import ar.com.textillevel.gui.acciones.odt.event.BotonADerechaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonAIzquierdaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.LabelClickeadaEvent;
import ar.com.textillevel.gui.acciones.odt.event.LabelClickeadaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.WorkFlowODTAnteriorEventListener;
import ar.com.textillevel.gui.acciones.odt.event.WorkFlowODTEvent;
import ar.com.textillevel.gui.acciones.odt.event.WorkFlowODTSiguienteEventListener;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.LinkableLabel;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.to.EstadoActualTipoMaquinaTO;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class PanelEstadoActualMaquina extends JPanel {

	private static final long serialVersionUID = -8378527488132749611L;

	private final EventListenerList listeners = new EventListenerList();

	private Map<EAvanceODT, PanelTablaODTTipoMaquina> mapaTablas;
	private PanelTablaODT tablaODTsDisponibles;

	private LinkableLabel lblTitulo;

	private JButton btnPasarAIzquierda;
	private JButton btnpasarADerecha;

	private JButton btnMaquinaSiguiente;
	private JButton btnMaquinaAnterior;

	private final boolean mostrarBotonMaquinaSiguiente;
	private final boolean mostrarBotonMaquinaAnterior;

	private EstadoActualTipoMaquinaTO estadoMaquinas;
	private List<ODTTO> odtsDisponibles;

	private JPanel panelBotones;
	private JPanel panelTablasEstadoAvance;
	
	private final Frame padre;
	
	private final boolean isOdts;
	private ModeloFiltro datosFiltro;

	public PanelEstadoActualMaquina(Frame padre, EstadoActualTipoMaquinaTO estadoActual, boolean showBotonSiguiente, ModeloFiltro filtros) {
		this.padre = padre;
		this.datosFiltro = filtros;
		this.estadoMaquinas=estadoActual;
		this.mostrarBotonMaquinaAnterior = true; //siempre se podria volver atras... segun el dibujo de salem, puede pasar de cosido a ODT Disponible
		this.mostrarBotonMaquinaSiguiente = showBotonSiguiente;
		isOdts = false;
		constructForEstado();
		for(EAvanceODT e : mapaTablas.keySet()){
			List<ODTTO> odts = estadoActual.getOdtsPorEstado().get(e);
			if(odts!=null && !odts.isEmpty()){
				mapaTablas.get(e).agregarElementos(odts);
			}
		}
	}

	public PanelEstadoActualMaquina(Frame padre, List<ODTTO> odtsDisponibles, ModeloFiltro filtros) {
		this.padre = padre;
		this.datosFiltro = filtros;
		mostrarBotonMaquinaAnterior = false;
		mostrarBotonMaquinaSiguiente = true;
		isOdts = true;
		constructForODTsDisponibles();
		if(odtsDisponibles!=null && !odtsDisponibles.isEmpty()){
			getTablaODTsDisponibles().agregarElementos(odtsDisponibles);
		}
	}

	private void constructForEstado() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(getEstadoMaquinas().getTipoMaquina().getTipoMaquina().toUpperCase()));
		add(getLblTitulo(), BorderLayout.NORTH);
		add(getPanelTablasEstadoAvance(), BorderLayout.CENTER);
		add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private void constructForODTsDisponibles() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("ODTs DISPONIBLES"));
		add(createLblTitulo(), BorderLayout.NORTH);
		add(getTablaODTsDisponibles(), BorderLayout.CENTER);
		add(getPanelBotones(), BorderLayout.SOUTH);
	}
	
	private JLabel createLblTitulo() {
		JLabel l = new JLabel(isODTsDisponibles() ? "ODTs DISPONIBLES" : getEstadoMaquinas().getTipoMaquina().getTipoMaquina().toUpperCase());
		Font fuente = l.getFont();
		Font fuenteNueva = new Font(fuente.getFontName(), Font.BOLD, 15);
		l.setFont(fuenteNueva);
		l.setForeground(Color.RED.darker());
		l.setHorizontalAlignment(JLabel.CENTER);
		return l;
	}

	public JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelBotones.add(getBtnMaquinaAnterior());
			panelBotones.add(getBtnMaquinaSiguiente());
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
	
	public void addLabelClickeadaActionListener(LabelClickeadaEventListener listener) {
		listeners.add(LabelClickeadaEventListener.class, listener);
	}

	public void addBotonSiguienteActionListener(WorkFlowODTSiguienteEventListener listener) {
		listeners.add(WorkFlowODTSiguienteEventListener.class, listener);
	}

	public void addBotonAnteriorActionListener(WorkFlowODTAnteriorEventListener listener) {
		listeners.add(WorkFlowODTAnteriorEventListener.class, listener);
	}
	
	public void addBotonIzquierdaActionListener(BotonAIzquierdaEventListener listener) {
		listeners.add(BotonAIzquierdaEventListener.class, listener);
	}

	public void addBotonDerechaActionListener(BotonADerechaEventListener listener) {
		listeners.add(BotonADerechaEventListener.class, listener);
	}

	public JButton getBtnMaquinaSiguiente() {
		if (btnMaquinaSiguiente == null) {
			btnMaquinaSiguiente = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_next.png", "ar/com/textillevel/imagenes/btn_next_des.png");
			btnMaquinaSiguiente.setVisible(mostrarBotonMaquinaSiguiente);
			btnMaquinaSiguiente.setEnabled(false);
			btnMaquinaSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final WorkFlowODTSiguienteEventListener[] l = listeners.getListeners(WorkFlowODTSiguienteEventListener.class);
					final WorkFlowODTEvent wfe = new WorkFlowODTEvent(getODTTOSeleccionada(EAvanceODT.FINALIZADO),isODTsDisponibles()?-1:getEstadoMaquinas().getTipoMaquina().getOrdenTipoMaquina());
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							for (int i = 0; i < l.length; i++) {
								l[i].botonSiguienteWorkFlowPersionado(wfe);
							}
						}
					});
				}
			});
		}
		return btnMaquinaSiguiente;
	}
	
	public JButton getBtnMaquinaAnterior() {
		if (btnMaquinaAnterior == null) {
			btnMaquinaAnterior = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_previous.png", "ar/com/textillevel/imagenes/btn_previous_des.png");
			btnMaquinaAnterior.setVisible(mostrarBotonMaquinaAnterior);
			btnMaquinaAnterior.setEnabled(false);
			btnMaquinaAnterior.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final WorkFlowODTEvent wfe = new WorkFlowODTEvent(getODTTOSeleccionada(EAvanceODT.POR_COMENZAR),getEstadoMaquinas().getTipoMaquina().getOrdenTipoMaquina());
					final WorkFlowODTAnteriorEventListener[] l = listeners.getListeners(WorkFlowODTAnteriorEventListener.class);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							for (int i = 0; i < l.length; i++) {
								l[i].botonAnteriorWorkFlowPersionado(wfe);
							}
						}
					});
				}
			});
		}
		return btnMaquinaAnterior;
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
		if (btnpasarADerecha == null) {
			btnpasarADerecha = BossEstilos.createButton("ar/com/textillevel/imagenes/btn_right.png", "ar/com/textillevel/imagenes/btn_right_des.png");
			btnpasarADerecha.setEnabled(false);
			btnpasarADerecha.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final BotonADerechaEventListener[] l = listeners.getListeners(BotonADerechaEventListener.class);
					final WorkFlowODTEvent wfe = new WorkFlowODTEvent(getODTTOSeleccionada(EAvanceODT.POR_COMENZAR),EAvanceODT.FINALIZADO,!isMostrarBotonMaquinaSiguiente());
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
		return btnpasarADerecha;
	}
	
	private ODTTO getODTTOSeleccionada(EAvanceODT tipoAvance) {
		if (isODTsDisponibles()) {
			return getTablaODTsDisponibles().getElemento(getTablaODTsDisponibles().getTabla().getSelectedRow());
		} else {
			PanelTablaODT panelTablaODT = getMapaTablas().get(tipoAvance);
			return panelTablaODT.getElemento(panelTablaODT.getTabla().getSelectedRow());
		}
	}

	public EstadoActualTipoMaquinaTO getEstadoMaquinas() {
		return estadoMaquinas;
	}

	public void setEstadoMaquinas(EstadoActualTipoMaquinaTO estadoMaquinas) {
		this.estadoMaquinas = estadoMaquinas;
	}

	public List<ODTTO> getOdtsDisponibles() {
		return odtsDisponibles;
	}

	public void setOdtsDisponibles(List<ODTTO> odtsDisponibles) {
		this.odtsDisponibles = odtsDisponibles;
	}

	private boolean isODTsDisponibles() {
		//return getOdtsDisponibles() != null && !getOdtsDisponibles().isEmpty();
		return isOdts;
	}

	public Map<EAvanceODT, PanelTablaODTTipoMaquina> getMapaTablas() {
		if (mapaTablas == null) {
			mapaTablas = new HashMap<EAvanceODT,PanelTablaODTTipoMaquina>();
			Set<EAvanceODT> keySet = getEstadoMaquinas().getOdtsPorEstado().keySet();
			for (EAvanceODT e : keySet) {
				mapaTablas.put(e, new PanelTablaODTTipoMaquina(padre, e,!isMostrarBotonMaquinaSiguiente()));
			}
		}
		return mapaTablas;
	}

	public PanelTablaODT getTablaODTsDisponibles() {
		if (tablaODTsDisponibles == null) {
			tablaODTsDisponibles = new PanelTablaODTTipoMaquina(padre,"ODTs");
			tablaODTsDisponibles.setSize(new Dimension(500, 500));
			tablaODTsDisponibles.setPreferredSize(new Dimension(500, 500));
		}
		return tablaODTsDisponibles;
	}

	public boolean isMostrarBotonMaquinaSiguiente() {
		return mostrarBotonMaquinaSiguiente;
	}

	public boolean isMostrarBotonMaquinaAnterior() {
		return mostrarBotonMaquinaAnterior;
	}

	public LinkableLabel getLblTitulo() {
		if (lblTitulo == null) {
			lblTitulo = new LinkableLabel(isODTsDisponibles() ? "ODTs DISPONIBLES" : getEstadoMaquinas().getTipoMaquina().getTipoMaquina().toUpperCase() ,
						GenericUtils.colorToHexa(Color.RED.darker()),GenericUtils.colorToHexa(Color.RED.brighter())) {
				
				private static final long serialVersionUID = -8501543800565485436L;

				@Override
				public void labelClickeada(MouseEvent e) {
					final LabelClickeadaEventListener[] l = listeners.getListeners(LabelClickeadaEventListener.class);
					final LabelClickeadaEvent lce = new LabelClickeadaEvent(getPadre(),getEstadoMaquinas().getTipoMaquina(),!isMostrarBotonMaquinaSiguiente(), getDatosFiltro());
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							for (int i = 0; i < l.length; i++) {
								l[i].labelClickeada(lce);
							}
						}
					});
				}
			};
//			lblTitulo = new JLabel(isODTsDisponibles() ? "ODTs DISPONIBLES" : getEstadoMaquinas().getTipoMaquina().getTipoMaquina().toUpperCase());
			Font fuente = lblTitulo.getFont();
			Font fuenteNueva = new Font(fuente.getFontName(), Font.BOLD, 15);
			lblTitulo.setFont(fuenteNueva);
//			lblTitulo.setForeground(Color.RED.darker().toString());
			lblTitulo.setHorizontalAlignment(JLabel.CENTER);
		}
		return lblTitulo;
	}

	public Frame getPadre() {
		return padre;
	}
	
	public ModeloFiltro getDatosFiltro() {
		return datosFiltro;
	}
	
	public void setDatosFiltro(ModeloFiltro datosFiltro) {
		this.datosFiltro = datosFiltro;
	}
	
	private class PanelTablaODTTipoMaquina extends PanelTablaODT{

		private static final long serialVersionUID = -699055534292116651L;
		
		public PanelTablaODTTipoMaquina(Frame padre, EAvanceODT tipoAvance, boolean ultima) {
			super(padre, tipoAvance, ultima);
			getBotonBajar().setVisible(false);
			getBotonModificar().setVisible(false);
			getBotonSubir().setVisible(false);
		}
		
		public PanelTablaODTTipoMaquina(Frame padre, String header){
			super(padre,header);
			getBotonBajar().setVisible(false);
			getBotonModificar().setVisible(false);
			getBotonSubir().setVisible(false);
		}

		@Override
		protected void habilitarBotones(int rowSelected, EAvanceODT tipoAvance) {
			if (isODTsDisponibles() || tipoAvance == null) {
				getBtnMaquinaSiguiente().setEnabled(rowSelected!=-1);
			} else if (tipoAvance == EAvanceODT.POR_COMENZAR) {
				getBtnMaquinaAnterior().setEnabled(rowSelected!=-1);
				getBtnPasarADerecha().setEnabled(rowSelected!=-1);
				getBtnPasarAIzquierda().setEnabled(false);
			} else if (tipoAvance == EAvanceODT.FINALIZADO) {
				getBtnMaquinaSiguiente().setEnabled(rowSelected!=-1);
				getBtnPasarADerecha().setEnabled(false);
				getBtnPasarAIzquierda().setEnabled(rowSelected!=-1);
			} else {
				//no deberia llegar por ahora. Cuando este la automatizacion no deberian estar mas los botones
			}
			
		}
		
	}
}
