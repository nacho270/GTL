package ar.com.textillevel.gui.util.controles.calendario.renderers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.controles.calendario.EventoCalendario;

public class EventosScrollTextAreaRenderer implements EventosRenderer<JScrollPane> {

	public JScrollPane getComponent(List<EventoCalendario> eventos) {
		JTextArea area = new JTextArea();
		if(eventos!=null){
			List<String> eventosStr = new ArrayList<String>();
			for(EventoCalendario e : eventos){
				eventosStr.add(e.getDescripcion());
			}
			area.setWrapStyleWord(true);
			area.setText(StringUtil.getCadena(eventosStr, "\n"));
			return new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		return new JScrollPane(null);
	}
}
