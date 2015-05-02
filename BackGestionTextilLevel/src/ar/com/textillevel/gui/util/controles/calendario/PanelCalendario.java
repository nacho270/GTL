package ar.com.textillevel.gui.util.controles.calendario;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.gui.util.controles.calendario.renderers.DefaultEventosRenderer;
import ar.com.textillevel.gui.util.controles.calendario.renderers.EventosRenderer;

public class PanelCalendario extends JTabbedPane{

	private static final long serialVersionUID = 3431625734264320448L;

	public static final String[] MONTHS = new String[] {
		"Enero",
		"Febrero",
		"Marzo",
		"Abril",
		"Mayo",
		"Junio",
		"Julio",
		"Agosto",
		"Septiembre",
		"Octubre",
		"Noviembre",
		"Diciembre"
	};
	
	private final Map<String, PanelMes> mapaMeses = new LinkedHashMap<String, PanelMes>();
	private final Map<Integer, List<EventoCalendario>> mapaMesesEventos = new LinkedHashMap<Integer, List<EventoCalendario>>();

	public PanelCalendario(Date fechaDesde, Date fechaHasta, List<EventoCalendario> lista) {
		armarMapaPaneles(fechaDesde, fechaHasta,new DefaultEventosRenderer(),lista);
	}
	
	public PanelCalendario(Date fechaDesde, Date fechaHasta, EventosRenderer<? extends JComponent> renderer, List<EventoCalendario> lista) {
		armarMapaPaneles(fechaDesde, fechaHasta,renderer,lista);
	}

	private void armarMapaPaneles(Date fechaDesde, Date fechaHasta, EventosRenderer<? extends JComponent> renderer,List<EventoCalendario> lista) {
		int mesDesde = DateUtil.getMes(fechaDesde);
		int mesHasta = DateUtil.getMes(fechaHasta);
		
		//SE ASUME QUE SON DEL MISMO AÑO
		int anio = DateUtil.getAnio(fechaHasta);

		if (anio != DateUtil.getAnio(fechaDesde)) {
			return;
		}
		for(EventoCalendario e : lista){
			if(mapaMesesEventos.get(DateUtil.getMes(e.getFecha()))==null){
				mapaMesesEventos.put(DateUtil.getMes(e.getFecha()) , new ArrayList<EventoCalendario>());
			}
			mapaMesesEventos.get(DateUtil.getMes(e.getFecha())).add(e);
		}
		
		for (int i = mesDesde; i <= mesHasta; i++) {
			mapaMeses.put(MONTHS[i], new PanelMes(i, anio,mapaMesesEventos.get(i), renderer));
		}
		for (String mes : mapaMeses.keySet()) {
			addTab(mes, mapaMeses.get(mes));
		}
	}
}
