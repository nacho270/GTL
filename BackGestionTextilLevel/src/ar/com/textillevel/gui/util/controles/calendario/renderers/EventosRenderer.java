package ar.com.textillevel.gui.util.controles.calendario.renderers;

import java.util.List;

import javax.swing.JComponent;

import ar.com.textillevel.gui.util.controles.calendario.EventoCalendario;

public interface EventosRenderer<T extends JComponent> {

	public T getComponent(List<EventoCalendario> eventos);
}
