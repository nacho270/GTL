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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.GTLGlobalCache;
import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.acciones.odt.componentes.PanelEstadoActualMaquina;
import ar.com.textillevel.gui.acciones.odt.event.BotonADerechaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.BotonAIzquierdaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.LabelClickeadaEvent;
import ar.com.textillevel.gui.acciones.odt.event.LabelClickeadaEventListener;
import ar.com.textillevel.gui.acciones.odt.event.WorkFlowODTAnteriorEventListener;
import ar.com.textillevel.gui.acciones.odt.event.WorkFlowODTEvent;
import ar.com.textillevel.gui.acciones.odt.event.WorkFlowODTSiguienteEventListener;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.EstadoActualTipoMaquinaTO;
import ar.com.textillevel.modulos.odt.to.EstadoGeneralODTsTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class JFrameVisionGeneralProduccion extends JFrame{

	private static final long serialVersionUID = -5751800810347027011L;

	private static final int CANT_MAX_PANEL_LINEA = 3;
	
	private JButton btnActualizarEstadoActual;
	private EstadoGeneralODTsTO estadoActual;
	
	private JScrollPane jsp;
	private JPanel panelCentral;
	
	private JPanel panelFiltros;
	
	private JButton btnSalir;
	private JButton btnImprimir;
	
	private OrdenDeTrabajoFacadeRemote odtFacade;
	private final ModeloFiltro datosFiltro;
	
	public JFrameVisionGeneralProduccion(Frame padre){
		datosFiltro = new ModeloFiltro();
		refreshView();
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setTitle("Estado de producción");
		GuiUtil.setFrameIcon(this, "ar/com/textillevel/imagenes/logogtl-ventana.jpg");
		setSize(GenericUtils.getDimensionPantalla());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
		setExtendedState(MAXIMIZED_BOTH);
	}

	private void setUpComponentes() {
		add(getPanelFiltros(),BorderLayout.NORTH);
		add(getJsp(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	public JPanel getPanelFiltros() {
		if(panelFiltros == null){
			panelFiltros = new JPanel(new BorderLayout());
			panelFiltros.add(createLabelTitulo(),BorderLayout.NORTH);
			panelFiltros.add(new PanelFiltros());
		}
		return panelFiltros;
	}


	private JLabel createLabelTitulo() {
		JLabel lblTitlo = new JLabel("ESTADO GENERAL");
		Font fuente = lblTitlo.getFont();
		Font fuenteNueva = new Font(fuente.getFontName(), Font.BOLD, 40);
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

	public JButton getBtnActualizarEstadoActual() {
		if(btnActualizarEstadoActual == null){
			btnActualizarEstadoActual = new JButton("Refrescar");
			btnActualizarEstadoActual.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refreshView();
				}
			});
		}
		return btnActualizarEstadoActual;
	}
	
	private void refreshView(){
		setEstadoActual(getOdtFacade().getEstadoDeProduccionActual(getDatosFiltro().getFechaDesde(),getDatosFiltro().getFechaHasta(),getDatosFiltro().getCliente()));
		
		getPanelCentral().removeAll();
		int indiceY = 0;
		int indiceX = 0;
		
		// agrego las ODT disponibles
		PanelEstadoActualMaquina panelODTSDisp = new PanelEstadoActualMaquina(JFrameVisionGeneralProduccion.this,getEstadoActual().getOdtsDisponibles(),getDatosFiltro());
		panelODTSDisp.addBotonSiguienteActionListener(new WorkFlowODTSiguienteEventListener() {
			public void botonSiguienteWorkFlowPersionado(WorkFlowODTEvent eventData) {
				JDialogSeleccionarMaquina dialog = new JDialogSeleccionarMaquina(JFrameVisionGeneralProduccion.this,(byte)-1,JDialogSeleccionarMaquina.MODO_SIGUIENTE);
				dialog.setVisible(true);
				if(dialog.isAcepto()){
					getOdtFacade().registrarTransicionODT(eventData.getOdtTO().getId(), dialog.getMaquinaElegida(), GTLGlobalCache.getInstance().getUsuarioSistema());
					refreshView();
				}
			}
		});
//		getPanelCentral().add(panelODTSDisp);//, GenericUtils.createGridBagConstraints(indiceX++, indiceY, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		panelODTSDisp.setSize(new Dimension(210, 300));
		panelODTSDisp.setPreferredSize(new Dimension(210, 300));
		getPanelCentral().add(panelODTSDisp, GenericUtils.createGridBagConstraints(indiceX, indiceY, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		indiceX++;
		
		// agrego las maquinas
		List<EstadoActualTipoMaquinaTO> estadoMaquinas = getEstadoActual().getEstadoMaquinas();
		for(int i = 0; i< estadoMaquinas.size();i++){
			EstadoActualTipoMaquinaTO e = estadoMaquinas.get(i);
			boolean ultima = i==(estadoMaquinas.size()-1);
			PanelEstadoActualMaquina panelTipoMaquina = crearPanelTipoMaquina(e, ultima);
//			getPanelCentral().add(panelTipoMaquina);
			if(ultima && indiceX == 0) {
				getPanelCentral().add(panelTipoMaquina, GenericUtils.createGridBagConstraints((int) Math.floor(CANT_MAX_PANEL_LINEA / 2), indiceY, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			} else {
				getPanelCentral().add(panelTipoMaquina, GenericUtils.createGridBagConstraints(indiceX, indiceY, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
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

	private PanelEstadoActualMaquina crearPanelTipoMaquina(EstadoActualTipoMaquinaTO e, boolean ultima) {
		PanelEstadoActualMaquina panelTipoMaquina = new PanelEstadoActualMaquina(JFrameVisionGeneralProduccion.this,e,!ultima,getDatosFiltro());
		panelTipoMaquina.addBotonAnteriorActionListener(new WorkFlowODTAnteriorEventListener() {
			public void botonAnteriorWorkFlowPersionado(WorkFlowODTEvent eventData) {
				if(eventData.getOrdenMaquina() > 1){
					JDialogSeleccionarMaquina dialog = new JDialogSeleccionarMaquina(JFrameVisionGeneralProduccion.this,eventData.getOrdenMaquina(),JDialogSeleccionarMaquina.MODO_ANTERIOR);
					dialog.setVisible(true);
					if(dialog.isAcepto()){
						getOdtFacade().registrarTransicionODT(eventData.getOdtTO().getId(), dialog.getMaquinaElegida(), GTLGlobalCache.getInstance().getUsuarioSistema());
						refreshView();
					}
				}else{
					if(FWJOptionPane.showQuestionMessage(JFrameVisionGeneralProduccion.this, "Va a detener la Orden de Trabajo. Desea continuar?", "Pregunta")==FWJOptionPane.YES_OPTION){
						getOdtFacade().detenerODT(eventData.getOdtTO().getId(), GTLGlobalCache.getInstance().getUsuarioSistema());
						refreshView();
					}
				}
			}
		});
		panelTipoMaquina.addBotonIzquierdaActionListener(new BotonAIzquierdaEventListener() {
			public void botonIzquierdaPersionado(WorkFlowODTEvent eventData) {
				getOdtFacade().registrarAvanceODT(eventData.getOdtTO().getId(), EAvanceODT.POR_COMENZAR,false, GTLGlobalCache.getInstance().getUsuarioSistema());
				refreshView();
			}
		});
		panelTipoMaquina.addBotonDerechaActionListener(new BotonADerechaEventListener() {
			public void botonDerechaPersionado(WorkFlowODTEvent eventData) {
				if(eventData.isOficina()){
					if(FWJOptionPane.showQuestionMessage(JFrameVisionGeneralProduccion.this, "Va a pasar la Orden de Trabajo a 'Oficina'. Desea continuar?", "Pregunta") == FWJOptionPane.YES_OPTION){
						getOdtFacade().registrarAvanceODT(eventData.getOdtTO().getId(),EAvanceODT.FINALIZADO,true,GTLGlobalCache.getInstance().getUsuarioSistema());
						refreshView();
					}
				}else{
					getOdtFacade().registrarAvanceODT(eventData.getOdtTO().getId(),EAvanceODT.FINALIZADO,false,GTLGlobalCache.getInstance().getUsuarioSistema());
					refreshView();
				}
			}
		});
		panelTipoMaquina.addLabelClickeadaActionListener(new LabelClickeadaEventListener() {
			public void labelClickeada(LabelClickeadaEvent event) {
				JDialogVisualizarODTsPorMaquinas jDialogVisualizarODTsPorMaquinas = new JDialogVisualizarODTsPorMaquinas(event.getPadre(),event.getTipoMaquina(),event.isUltima(), event.getDatosFiltro());
				jDialogVisualizarODTsPorMaquinas.setVisible(true);
				refreshView();
			}
		});
		if(!ultima){
			panelTipoMaquina.addBotonSiguienteActionListener(new WorkFlowODTSiguienteEventListener() {
				public void botonSiguienteWorkFlowPersionado(WorkFlowODTEvent eventData) {
					JDialogSeleccionarMaquina dialog = new JDialogSeleccionarMaquina(JFrameVisionGeneralProduccion.this,eventData.getOrdenMaquina(),JDialogSeleccionarMaquina.MODO_SIGUIENTE);
					dialog.setVisible(true);
					if(dialog.isAcepto()){
						getOdtFacade().registrarTransicionODT(eventData.getOdtTO().getId(), dialog.getMaquinaElegida(), GTLGlobalCache.getInstance().getUsuarioSistema());
						refreshView();
					}
				}
			});
		}
		
		panelTipoMaquina.setSize(new Dimension(440, 300));
		panelTipoMaquina.setPreferredSize(new Dimension(440, 300));
		
		return panelTipoMaquina;
	}

	public JPanel getPanelCentral() {
		if(panelCentral == null){
//			panelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelCentral = new JPanel(new GridBagLayout());
//			Dimension dimensionPantalla = GenericUtils.getDimensionPantalla();
//			double altoPantalla = dimensionPantalla.getHeight();
//			double anchoPantalla = dimensionPantalla.getWidth();
//			dimensionPantalla.setSize(anchoPantalla-100, altoPantalla-100);
//			panelCentral.setSize(dimensionPantalla);
//			panelCentral.setPreferredSize(dimensionPantalla);
		}
		return panelCentral;
	}
	
	public EstadoGeneralODTsTO getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(EstadoGeneralODTsTO estadoActual) {
		this.estadoActual = estadoActual;
	}

	public OrdenDeTrabajoFacadeRemote getOdtFacade() {
		if(odtFacade == null){
			odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		}
		return odtFacade;
	}

	public JScrollPane getJsp() {
		if(jsp == null){
			jsp = new JScrollPane(getPanelCentral(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//			Dimension dimensionPantalla = getPanelCentral().getSize();
//			double altoPantalla = dimensionPantalla.getHeight();
//			double anchoPantalla = dimensionPantalla.getWidth();
//			dimensionPantalla.setSize(anchoPantalla+500, altoPantalla+500);
//			jsp.setSize(new Dimension(1024, 768));
//			jsp.setPreferredSize(new Dimension(1024, 768));
		}
		return jsp;
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
	
	public class ModeloFiltro {
		private Cliente cliente;
		private Date fechaDesde;
		private Date fechaHasta;

		public void resetFilters() {
			cliente = null;
			fechaDesde = null;
			fechaHasta = null;
		}

		public Cliente getCliente() {
			return cliente;
		}

		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}

		public Date getFechaDesde() {
			return fechaDesde;
		}

		public void setFechaDesde(Date fechaDesde) {
			this.fechaDesde = fechaDesde;
		}

		public Date getFechaHasta() {
			return fechaHasta;
		}

		public void setFechaHasta(Date fechaHasta) {
			this.fechaHasta = fechaHasta;
		}
	}
	
	private class PanelFiltros extends JPanel{

		private static final long serialVersionUID = 7914804717141480317L;

		private JCheckBox chkFiltroFecha;
		private PanelDatePicker panelFechaDesde;
		private PanelDatePicker panelFechaHasta;
		private JComboBox cmbTipoBusquedaCliente;
		private FWJNumericTextField txtBusquedaCliente;
		private JButton btnBuscar;
		private JButton btnCleanFilters;
		
		public PanelFiltros() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			setBorder(BorderFactory.createTitledBorder("Filtros"));
			add(getChkFiltroFecha());
			add(getPanelFechaDesde());
			add(getPanelFechaHasta());
			add(getCmbTipoBusquedaCliente());
			add(getTxtBusquedaCliente());
			add(getBtnBuscar());
			add(getBtnCleanFilters());
		}

		private JComboBox getCmbTipoBusquedaCliente() {
			if (cmbTipoBusquedaCliente == null) {
				cmbTipoBusquedaCliente = new JComboBox();
				cmbTipoBusquedaCliente.addItem("ID");
				cmbTipoBusquedaCliente.addItem("NOMBRE");
				cmbTipoBusquedaCliente.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							if (cmbTipoBusquedaCliente.getSelectedItem().equals("NOMBRE")) {
								JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(null);
								GuiUtil.centrar(dialogSeleccionarCliente);
								dialogSeleccionarCliente.setVisible(true);
								Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
								if (clienteElegido != null) {
									getDatosFiltro().setCliente(clienteElegido);
								}
								getCmbTipoBusquedaCliente().setSelectedIndex(0);
							}
						}
					}
				});
			}
			return cmbTipoBusquedaCliente;
		}

		private FWJNumericTextField getTxtBusquedaCliente() {
			if (txtBusquedaCliente == null) {
				txtBusquedaCliente = new FWJNumericTextField();
				txtBusquedaCliente.setPreferredSize(new Dimension(100, 20));
				txtBusquedaCliente.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						getBtnBuscar().doClick();
					}
				});
			}
			return txtBusquedaCliente;
		}
		
		private JButton getBtnBuscar() {
			if(btnBuscar == null){
				btnBuscar = BossEstilos.createButton("ar/com/textillevel/imagenes/b_buscar_moderno.png","ar/com/textillevel/imagenes/b_buscar_moderno.png");
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(getChkFiltroFecha().isSelected()){
							if(getPanelFechaDesde().getDate() == null || getPanelFechaHasta().getDate() ==null){
								FWJOptionPane.showErrorMessage(JFrameVisionGeneralProduccion.this, "Alguna de las fechas son inválidas", "Error");
								return;
							}
							getDatosFiltro().setFechaDesde(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
							getDatosFiltro().setFechaHasta(DateUtil.getManiana(new java.sql.Date(getPanelFechaHasta().getDate().getTime())));
						}else{
							getDatosFiltro().setFechaDesde(null);
							getDatosFiltro().setFechaHasta(null);
						}
						refreshView();
					}
				});
			}
			return btnBuscar;
		}

		private JCheckBox getChkFiltroFecha() {
			if(chkFiltroFecha == null){
				chkFiltroFecha = new JCheckBox("Filtrar por fecha");
				chkFiltroFecha.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GuiUtil.setEstadoPanel(getPanelFechaDesde(), getChkFiltroFecha().isSelected());
						GuiUtil.setEstadoPanel(getPanelFechaHasta(), getChkFiltroFecha().isSelected());
					}
				});
			}
			return chkFiltroFecha;
		}

		private PanelDatePicker getPanelFechaDesde() {
			if(panelFechaDesde == null){
				panelFechaDesde = new PanelDatePicker();
				panelFechaDesde.setEnabled(false);
				panelFechaDesde.setCaption("Fecha desde: ");
				panelFechaDesde.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 7));
			}
			return panelFechaDesde;
		}

		private PanelDatePicker getPanelFechaHasta() {
			if(panelFechaHasta == null){
				panelFechaHasta = new PanelDatePicker();
				panelFechaHasta.setEnabled(false);
				panelFechaHasta.setCaption("Fecha hasta: ");
				panelFechaHasta.setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 7));
			}
			return panelFechaHasta;
		}

		private JButton getBtnCleanFilters() {
			if(btnCleanFilters == null){
				btnCleanFilters = new JButton("Borrar filtros");
				btnCleanFilters.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getDatosFiltro().resetFilters();
						getTxtBusquedaCliente().setText("");
						getCmbTipoBusquedaCliente().setSelectedIndex(0);
						getPanelFechaDesde().setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 7));
						getPanelFechaDesde().setEnabled(false);
						getPanelFechaHasta().setSelectedDate(DateUtil.restarDias(DateUtil.getHoy(), 7));
						getPanelFechaHasta().setEnabled(false);
						getChkFiltroFecha().setSelected(false);
					}
				});
			}
			return btnCleanFilters;
		}
	}

	public ModeloFiltro getDatosFiltro() {
		return datosFiltro;
	}
}
