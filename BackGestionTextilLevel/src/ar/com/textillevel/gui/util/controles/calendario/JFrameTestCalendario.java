package ar.com.textillevel.gui.util.controles.calendario;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.calendario.renderers.DefaultEventosRenderer;


public class JFrameTestCalendario extends JFrame{

	private static final long serialVersionUID = 2738129539909658687L;

	private final PanelCalendario pc;
	
	public JFrameTestCalendario(){
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		List<EventoCalendario> lista = new ArrayList<EventoCalendario>();
		lista.add(new EventoCalendario("1 aa hdashdadh asjdasd", DateUtil.stringToDate("22/04/2012")));
		lista.add(new EventoCalendario("2 bbkasduioaui odjhaoda sda", DateUtil.stringToDate("22/04/2012")));
		lista.add(new EventoCalendario("6+56 bbkasduioaui odjhaoda sda", DateUtil.stringToDate("22/04/2012")));
		lista.add(new EventoCalendario("2a6 bbkasduioaui odjhaoda sda", DateUtil.stringToDate("22/04/2012")));
		lista.add(new EventoCalendario("3 c c fsdfsdf", DateUtil.stringToDate("10/04/2012")));

		pc = new PanelCalendario(DateUtil.stringToDate("22/04/2012"), DateUtil.stringToDate("15/6/2012"), new DefaultEventosRenderer(),lista);
		add(new JScrollPane(pc, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),BorderLayout.CENTER);
		setSize(GenericUtils.getDimensionPantalla());
		GuiUtil.centrar(this);
	}
	
	public static void main(String[] args){
		new JFrameTestCalendario().setVisible(true);
	}
}
