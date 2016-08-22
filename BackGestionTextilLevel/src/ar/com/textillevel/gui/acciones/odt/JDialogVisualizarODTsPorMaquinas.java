package ar.com.textillevel.gui.acciones.odt;

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
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.GTLGlobalCache;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.acciones.odt.JFrameVisionGeneralProduccion.ModeloFiltro;
import ar.com.textillevel.gui.acciones.odt.componentes.PanelODTsMaquina;
import ar.com.textillevel.gui.acciones.odt.event.BotonADerechaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonAIzquierdaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonCambiarMaquinaActionListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonCambiarMaquinaEvent;
import ar.com.textillevel.gui.acciones.odt.event.BotonSubirBajarActionListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonSubirBajarEvent;
import ar.com.textillevel.gui.acciones.odt.event.WorkFlowODTEvent;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.EstadoActualMaquinaTO;
import ar.com.textillevel.modulos.odt.to.TipoMaquinaTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogVisualizarODTsPorMaquinas extends JDialog {

	private static final long serialVersionUID = -8178949729365744925L;
	private static final int CANT_MAX_PANEL_LINEA = 2;
	
	private JButton btnSalir;
	private JButton btnImprimir;
	private JScrollPane jsp;
	private JPanel panelCentral;
	
	private OrdenDeTrabajoFacadeRemote odtFacade;

	private final TipoMaquinaTO tipoMaquina;
	private final ModeloFiltro filtros;

	private List<EstadoActualMaquinaTO> estadosMaquinas;
	private boolean ultima;

	public JDialogVisualizarODTsPorMaquinas(Frame padre, TipoMaquinaTO tipoMaquinaTO, boolean ultima, ModeloFiltro filtros) {
		super(padre);
		this.tipoMaquina = tipoMaquinaTO;
		this.filtros = filtros;
		this.ultima = ultima;
		refreshView();
		setUpComponentes();
		setUpScreen();
	}
	
	private void setUpScreen() {
		setTitle("Estado de Maquinas: " + getTipoMaquina().getTipoMaquina().toUpperCase());
		setSize(GenericUtils.getDimensionPantalla());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(createLabelTitulo(),BorderLayout.NORTH);
		add(getJsp(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JLabel createLabelTitulo() {
		JLabel lblTitlo = new JLabel(getTipoMaquina().getTipoMaquina().toUpperCase());
		Font fuente = lblTitlo.getFont();
		Font fuenteNueva = new Font(fuente.getFontName(), Font.BOLD, 30);
		lblTitlo.setFont(fuenteNueva);
		lblTitlo.setHorizontalAlignment(JLabel.CENTER);
		lblTitlo.setForeground(Color.RED.darker());
		return lblTitlo;
	}
	
	private JPanel getPanelSur(){
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnImprimir());
		panelSur.add(getBtnSalir());
		return panelSur;
	}
	
	public JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_exit.png", "ar/com/textillevel/imagenes/b_exit.png"); 
			btnSalir.setMnemonic(KeyEvent.VK_S);
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnSalir;
	}

	public JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imp_listado.png", "ar/com/textillevel/imagenes/b_imp_listado_des.png"); 
			btnImprimir.setToolTipText("Imprimir listado");	
			btnImprimir.setEnabled(true);
			btnImprimir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
				}
			});
		}
		return btnImprimir;
	}

	public JScrollPane getJsp() {
		if(jsp == null){
			jsp = new JScrollPane(getPanelCentral(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		return jsp;
	}
	
	public JPanel getPanelCentral() {
		if(panelCentral == null){
			panelCentral = new JPanel(new GridBagLayout());
		}
		return panelCentral;
	}
	
	private void refreshView() {
		setEstadosMaquinas(getOdtFacade().getEstadoMaquinas(getTipoMaquina().getIdTipoMaquina(), null, null, null));
		getPanelCentral().removeAll();
		int indiceY = 0;
		int indiceX = 0;
		
		for(int i = 0; i< getEstadosMaquinas().size();i++){
			EstadoActualMaquinaTO e = getEstadosMaquinas().get(i);
			boolean ultima = i==(getEstadosMaquinas().size()-1);
			PanelODTsMaquina panelTipoMaquina = crearPanelMaquina(e);
			if(ultima && indiceX == 0) {
				getPanelCentral().add(panelTipoMaquina, GenericUtils.createGridBagConstraints(indiceX, indiceY, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 2, 1, 0, 0));
			} else {
				getPanelCentral().add(panelTipoMaquina, GenericUtils.createGridBagConstraints(indiceX, indiceY, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			}
			indiceX ++;
			if (indiceX == CANT_MAX_PANEL_LINEA) {
				indiceX = 0;
				indiceY++;
			}
		}
		getJsp().invalidate();
		getJsp().updateUI();
		invalidate();
	}

	private PanelODTsMaquina crearPanelMaquina(EstadoActualMaquinaTO e) {
		PanelODTsMaquina panelMaquina = new PanelODTsMaquina((Frame)getParent(),e,isUltima());
		panelMaquina.addBotonIzquierdaActionListener(new BotonAIzquierdaEventListener() {
			public void botonIzquierdaPersionado(WorkFlowODTEvent eventData) {
				getOdtFacade().registrarAvanceODT(eventData.getOdtTO().getId(), EAvanceODT.POR_COMENZAR,false, GTLGlobalCache.getInstance().getUsuarioSistema());
				refreshView();
			}
		});
		panelMaquina.addBotonDerechaActionListener(new BotonADerechaEventListener() {
			public void botonDerechaPersionado(WorkFlowODTEvent eventData) {
				if(eventData.isOficina()){
					if(FWJOptionPane.showQuestionMessage(JDialogVisualizarODTsPorMaquinas.this, "Va a pasar la Orden de Trabajo a 'Oficina'. Desea continuar?", "Pregunta") == FWJOptionPane.YES_OPTION){
						getOdtFacade().registrarAvanceODT(eventData.getOdtTO().getId(),EAvanceODT.FINALIZADO,true,GTLGlobalCache.getInstance().getUsuarioSistema());
						refreshView();
					}
				}else{
					getOdtFacade().registrarAvanceODT(eventData.getOdtTO().getId(),EAvanceODT.FINALIZADO,false,GTLGlobalCache.getInstance().getUsuarioSistema());
					refreshView();
				}
			}
		});
		panelMaquina.addBotonCambiarMaquinaActionListener(new BotonCambiarMaquinaActionListener() {
			public void botonCambiarMaquinaPresionado(BotonCambiarMaquinaEvent event) {
				JDialogSeleccionarMaquina dialog = new JDialogSeleccionarMaquina(JDialogVisualizarODTsPorMaquinas.this,event.getIdTipoMaquina(),event.getIdMaquina());
				dialog.setVisible(true);
				if(dialog.isAcepto()){
					getOdtFacade().registrarTransicionODT(event.getOdtTO().getId(), dialog.getMaquinaElegida(), GTLGlobalCache.getInstance().getUsuarioSistema());
					refreshView();
				}
			}
		});
		panelMaquina.addBotonSubirBajarActionListener(new BotonSubirBajarActionListener() {
			public void botonSubirBajarPresionado(BotonSubirBajarEvent event) {
				if(event.getAccion() == BotonSubirBajarEvent.SUBIR){
					getOdtFacade().subirODT(event.getOdt());
				}else{
					getOdtFacade().bajarODT(event.getOdt());
				}
				refreshView();
			}
		});
		panelMaquina.setSize(new Dimension(750, 300));
		panelMaquina.setPreferredSize(new Dimension(750, 300));
		return panelMaquina;
	}

	public OrdenDeTrabajoFacadeRemote getOdtFacade() {
		if (odtFacade == null) {
			odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		}
		return odtFacade;
	}

	public List<EstadoActualMaquinaTO> getEstadosMaquinas() {
		return estadosMaquinas;
	}

	public void setEstadosMaquinas(List<EstadoActualMaquinaTO> estadosMaquinas) {
		this.estadosMaquinas = estadosMaquinas;
	}

	public ModeloFiltro getFiltros() {
		return filtros;
	}

	public TipoMaquinaTO getTipoMaquina() {
		return tipoMaquina;
	}

	public boolean isUltima() {
		return ultima;
	}

	public void setUltima(boolean ultima) {
		this.ultima = ultima;
	}
}
