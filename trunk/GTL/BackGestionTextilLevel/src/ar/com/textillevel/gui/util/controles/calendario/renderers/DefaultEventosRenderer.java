package ar.com.textillevel.gui.util.controles.calendario.renderers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.controles.calendario.EventoCalendario;

public class DefaultEventosRenderer implements EventosRenderer<JLabel> {

	public JLabel getComponent(List<EventoCalendario> eventos) {
		JLabel label = new JLabel("");
		if(eventos!=null){
			List<String> eventosStr = new ArrayList<String>();
			for(EventoCalendario e : eventos){
				eventosStr.add(e.getDescripcion());
			}
			label = new JLabel("<html>"+StringUtil.getCadena(eventosStr, "<br>")+"</html>");
		}
		return label;
	}
}
