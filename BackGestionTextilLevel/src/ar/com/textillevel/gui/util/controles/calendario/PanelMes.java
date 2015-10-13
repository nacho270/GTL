package ar.com.textillevel.gui.util.controles.calendario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.calendario.renderers.DefaultEventosRenderer;
import ar.com.textillevel.gui.util.controles.calendario.renderers.EventosRenderer;

public class PanelMes extends JPanel {

	private static final long serialVersionUID = 8208864787756561580L;

	public static Color WEEK_DAYS_FOREGROUND = (Color) UIManager.get("List.foreground");
	public static Color SELECTED_DAY_FOREGROUND = (Color) UIManager.get("List.selectionForeground");
	public static Color SELECTED_DAY_BACKGROUND = (Color) UIManager.get("List.selectionBackground");
	public static Color DAYS_GRID_BACKGROUND = Color.WHITE;

	private GregorianCalendar calendar;
	
	private List<EventoCalendario> eventos;

	private PanelDiaMes[][] pDias;
	private FocusablePanel daysGrid;
	
	private PanelDiaMes panelDiaActual;

	private static final String[] DAYS = new String[] { "Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb" };

	private List<EventoCalendario> allEventos;
	private final Map<Integer, List<EventoCalendario>> mapaEventosDia = new LinkedHashMap<Integer, List<EventoCalendario>>();
	
	public PanelMes(int mes, int anio, List<EventoCalendario> eventos) {
		setAllEventos(eventos);
		setUpComponentes(mes, anio, new DefaultEventosRenderer());
	}

	public PanelMes(int mes, int anio, List<EventoCalendario> eventos, EventosRenderer<? extends JComponent> renderer) {
		setAllEventos(eventos);
		setUpComponentes(mes, anio,renderer);
	}
	
	private void setUpComponentes(int mes, int anio, EventosRenderer<? extends JComponent> renderer) {
		calendar = DateUtil.getGregorianCalendar();
		calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
		calendar.set(GregorianCalendar.MONTH, mes);
		calendar.set(GregorianCalendar.YEAR, anio);
		pDias = new PanelDiaMes[7][7];
		for (int i = 0; i < 7; i++) {
			pDias[0][i] = new PanelDiaMes(DAYS[i]);
		}
		
		int offset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
		int lastDay = calendar.getActualMaximum(Calendar.DATE);

		for (int i = 1; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				pDias[i][j] = new PanelDiaMes(null,null,renderer);
				pDias[i][j].addMouseListener(new AdaptadorEventosMouse());
			}
		}
		
		for(int i = 0; i < lastDay; i++) {
			pDias[(i + offset) / 7 + 1][(i + offset) % 7].setNroDia(i + 1);
			pDias[(i + offset) / 7 + 1][(i + offset) % 7].setEventosDia(mapaEventosDia.get(i+1));
			pDias[(i + offset) / 7 + 1][(i + offset) % 7].update();
		}

		daysGrid = new FocusablePanel(new GridLayout(7, 7, 5, 5));
		for (int i = 0; i < 7; i++){
			for (int j = 0; j < 7; j++){
				daysGrid.add(pDias[i][j]);
			}
		}
		Dimension dimensionPantalla = GenericUtils.getDimensionPantalla();
		daysGrid.setPreferredSize(new Dimension((int)dimensionPantalla.getWidth()-100,(int)dimensionPantalla.getHeight()-100));
		daysGrid.setBorder(BorderFactory.createLoweredBevelBorder());
		JPanel daysPanel = new JPanel();
		daysPanel.add(daysGrid);
		add(daysPanel, BorderLayout.CENTER);
	}

	public List<EventoCalendario> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventoCalendario> eventos) {
		this.eventos = eventos;
	}

	private class PanelDiaMes extends JPanel {

		private static final long serialVersionUID = -4757121690725695895L;

		private List<EventoCalendario> eventosDia;

		private JLabel lblDia;
		private JLabel lblDescripcion;
		
		private Integer nroDia;
		
		private EventosRenderer<? extends JComponent> renderer;
		
		public PanelDiaMes(List<EventoCalendario> eventosDia, Integer nroDia, EventosRenderer<? extends JComponent> renderer) {
			setEventosDia(eventosDia);
			setNroDia(nroDia);
			setRenderer(renderer);
			construirParaEventos(nroDia,eventosDia);
		}
		
		private void construirParaEventos(Integer nroDia, List<EventoCalendario> eventosDia2) {
			lblDia = crearLabelDiaEvento(nroDia);
			setBorder(BorderFactory.createLineBorder(SELECTED_DAY_BACKGROUND));
			setLayout(new GridBagLayout());
			add(lblDia,GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			if(getNroDia()!=null){
				add(getRenderer().getComponent(eventosDia2),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 0.3));
			}
		}
		
		private JLabel crearLabelDiaEvento(Integer dia) {
			JLabel lblDia2 = new JLabel( (dia==null?"":String.valueOf(dia)));
			lblDia2.setOpaque(true);
			Font font = lblDia2.getFont();
			lblDia2.setFont(new Font(font.getName(), Font.BOLD, font.getSize() + 5));
			lblDia2.setHorizontalAlignment(JLabel.CENTER);
			lblDia2.setVerticalAlignment(JLabel.CENTER);
			return lblDia2;
		}
		
		/* ******************************************************************************************************* */
		
		public PanelDiaMes(String dia) {
			construirParaTituloDia(dia);
		}

		private void construirParaTituloDia(String dia) {
			lblDia = crearLabelDia(dia);
			setLayout(new GridBagLayout());
			add(lblDia,GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			setBorder(BorderFactory.createLineBorder(SELECTED_DAY_BACKGROUND));
		}

		private JLabel crearLabelDia(String dia) {
			JLabel lblDia2 = new JLabel(dia);
			lblDia2.setOpaque(true);
			lblDia2.setForeground(SELECTED_DAY_BACKGROUND);
			Font font = lblDia2.getFont();
			lblDia2.setFont(new Font(font.getName(), Font.BOLD, font.getSize()+10));
			lblDia2.setHorizontalAlignment(JLabel.CENTER);
			lblDia2.setVerticalAlignment(JLabel.CENTER);
			return lblDia2;
		}
		
		/* ******************************************************************************************************* */

		public void update() {
			remove(lblDia);
			if(lblDescripcion!=null){
				remove(lblDescripcion);
			}
			construirParaEventos(getNroDia(),getEventosDia());
		}

		public List<EventoCalendario> getEventosDia() {
			return eventosDia;
		}

		public void setEventosDia(List<EventoCalendario> eventosDia) {
			this.eventosDia = eventosDia;
		}

		public Integer getNroDia() {
			return nroDia;
		}
		
		public void setNroDia(Integer nroDia) {
			this.nroDia = nroDia;
		}

		public void mouseClicked() {
			if(panelDiaActual != null){
				panelDiaActual.setBackground(null);
				panelDiaActual.setOpaque(false);
				panelDiaActual.repaint();
				panelDiaActual.lblDia.setForeground(Color.BLACK);
				panelDiaActual.lblDia.setBackground(null);
			}
			panelDiaActual = this;
			setOpaque(true);
			lblDia.setForeground(SELECTED_DAY_FOREGROUND);
			lblDia.setBackground(SELECTED_DAY_BACKGROUND);
			repaint();
		}

		
		public EventosRenderer<? extends JComponent> getRenderer() {
			return renderer;
		}

		
		public void setRenderer(EventosRenderer<? extends JComponent> renderer) {
			this.renderer = renderer;
		}
	}

	private static class FocusablePanel extends JPanel {

		private static final long serialVersionUID = 1843135464196644820L;

		public FocusablePanel(LayoutManager layout) {
			super(layout);
		}
	}
	
	private class AdaptadorEventosMouse extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			PanelDiaMes p = (PanelDiaMes)e.getSource();
			p.mouseClicked();
		}
	}
	
	public List<EventoCalendario> getAllEventos() {
		return allEventos;
	}

	public void setAllEventos(List<EventoCalendario> allEventos) {
		this.allEventos = allEventos;
		if(mapaEventosDia.keySet().isEmpty()){
			if(allEventos!=null){
				for(EventoCalendario e : allEventos){
					Integer dia = DateUtil.getDia(e.getFecha());
					if(mapaEventosDia.get(dia) == null){
						mapaEventosDia.put(dia, new ArrayList<EventoCalendario>());
					}
					mapaEventosDia.get(dia).add(e);
				}
			}
		}
	}
}
