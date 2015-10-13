package ar.com.fwcommon.templates.main.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.util.DefaultModePrinter;
import ar.com.fwcommon.util.GuiSeleccionarImpresora;

@SuppressWarnings("serial")
public class MenuImpresion extends FWJMenu {

	private JCheckBoxMenuItem menuPrevisualizacion;
	private JMenuItem menuImpresionNormal;
	private JMenuItem menuImpresionPapeleta;

	public MenuImpresion() {
		super("Impresión", 'I');
		getMenuImpresionNormal().setText("Normal: " + System.getProperty("NombreImpresoraNormal"));
		getMenuImpresionPapeleta().setText("Papeleta: " + System.getProperty("NombreImpresoraPapeleta"));
		//Previsualización
		add(getMenuPrevisualizacion());
		addSeparator();
		//Impresion Normal
		add(getMenuImpresionNormal());
	}

	public void agregarMenuPrevisualizacion() {
		add(getMenuPrevisualizacion());
	}

	public void agregarMenuImpresionNormal() {
		add(getMenuImpresionNormal());
	}

	public void agregarMenuImpresionPapeleta() {
		add(getMenuImpresionPapeleta());
	}

	public JCheckBoxMenuItem getMenuPrevisualizacion() {
		if(menuPrevisualizacion == null) {
			menuPrevisualizacion = new JCheckBoxMenuItem("Previsualización");
			menuPrevisualizacion.setFont(BossEstilos.getDefaultFont());
			menuPrevisualizacion.addItemListener(new ImpresionItemListener());
			menuPrevisualizacion.setSelected(Boolean.valueOf(System.getProperty("verPreviewImpresion")));
		}
		return menuPrevisualizacion;
	}

	public JMenuItem getMenuImpresionNormal() {
		if(menuImpresionNormal == null) {
			menuImpresionNormal = new JMenuItem("Normal");
			menuImpresionNormal.setFont(BossEstilos.getDefaultFont());
			menuImpresionNormal.addActionListener(new ImpresionActionListener());
		}
		return menuImpresionNormal;
	}

	public JMenuItem getMenuImpresionPapeleta() {
		if(menuImpresionPapeleta == null) {
			menuImpresionPapeleta = new JMenuItem("Papeleta");
			menuImpresionPapeleta.setFont(BossEstilos.getDefaultFont());
			menuImpresionPapeleta.addActionListener(new ImpresionActionListener());
		}
		return menuImpresionPapeleta;
	}

	/** Clase listener de eventos del menú 'Impresión' */
	class ImpresionItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent evt) {
			if(evt.getStateChange() == ItemEvent.SELECTED) {
				System.setProperty("verPreviewImpresion", String.valueOf(true));
			} else {
				System.setProperty("verPreviewImpresion", String.valueOf(false));
			}
		}
	}

	/** Clase listener de eventos del menú 'Impresión' */
	class ImpresionActionListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			if(evt.getSource().equals(getMenuImpresionNormal())) {
				GuiSeleccionarImpresora guiSeleccionarImpresora = new GuiSeleccionarImpresora(getFrame(), new DefaultModePrinter());
				guiSeleccionarImpresora.setVisible(true);
				if(guiSeleccionarImpresora.getImpresoraSeleccionada() != null)
					getMenuImpresionNormal().setText("Normal: " + guiSeleccionarImpresora.getImpresoraSeleccionada());
			} else {
				GuiSeleccionarImpresora guiSeleccionarImpresora = new GuiSeleccionarImpresora(getFrame(), new DefaultModePrinter(DefaultModePrinter.PRINTER_PAPELETA));
				guiSeleccionarImpresora.setVisible(true);
				if(guiSeleccionarImpresora.getImpresoraSeleccionada() != null)
					getMenuImpresionPapeleta().setText("Papeleta: " + guiSeleccionarImpresora.getImpresoraSeleccionada());
			}
		}
	}

}