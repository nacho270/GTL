package main.acciones.informes;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;

import ar.com.fwcommon.templates.main.menu.FWJMenu;

public class MenuInformes extends FWJMenu{

	private static final long serialVersionUID = 1849667827454370225L;

	private final Frame frame;
	private JMenuItem itemInformeIVAVentas;
	private JMenuItem itemInformeProduccion;

	public MenuInformes(Frame frame) {
		super("Informes", 'I');
		setMnemonic(KeyEvent.VK_I);
		this.frame = frame;
		this.add(getItemInformeIVAVentas());
		this.add(getItemInformeProduccion());
	}

	public JMenuItem getItemInformeIVAVentas() {
		if(itemInformeIVAVentas == null){
			itemInformeIVAVentas = new JMenuItem(new InformeIVAVentasAction(frame));
			itemInformeIVAVentas.setText("IVA - Ventas");
		}
		return itemInformeIVAVentas;
	}

	
	public JMenuItem getItemInformeProduccion() {
		if(itemInformeProduccion == null){
			itemInformeProduccion = new JMenuItem(new InformeProduccionAction(frame));
			itemInformeProduccion.setText("Informe de produccion por Kilo/Metros");
		}
		return itemInformeProduccion;
	}
}
