package main.servicios.alertas.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;

public class JDialogVerNotificaciones extends JDialog {

	private static final long serialVersionUID = 6882152212322989005L;

	private static final int CANT_COLS = 3;
	private static final int COL_LEIDA = 0;
	private static final int COL_TEXTO = 1;
	private static final int COL_OBJ = 2;
	
	private final List<NotificacionUsuario> notificaciones;
	
	private FWJTable tablaNotificaciones;
	private JButton btnAceptar;
	
	public JDialogVerNotificaciones(Frame owner, List<NotificacionUsuario> notificaciones) {
		super(owner);
		this.notificaciones = notificaciones;
		setUpScreen();
		setUpComponentes();
		llenarTabla();
	}

	private void llenarTabla() {
		for(NotificacionUsuario nc : notificaciones) {
			getTablaNotificaciones().addRow(new Object[]{nc.getLeida(), nc.getTexto(), nc});
		}
	}

	private void setUpComponentes() {
		add(new JScrollPane(getTablaNotificaciones()),BorderLayout.CENTER);
		JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pnlSur.add(getBtnAceptar());
		add(pnlSur, BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Notificaciones");
		setSize(new Dimension(700,400));
		setResizable(true);
		GuiUtil.centrarEnFramePadre(this);
		setModal(true);
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}
	
	public FWJTable getTablaNotificaciones() {
		if (tablaNotificaciones == null) {
			tablaNotificaciones = new FWJTable(0, CANT_COLS);
			tablaNotificaciones.setCheckColumn(COL_LEIDA, "Leida", 70, true);
			tablaNotificaciones.setStringColumn(COL_TEXTO, "Notificación", 500, 500, true);
			tablaNotificaciones.setStringColumn(COL_OBJ, "", 0);
			tablaNotificaciones.setAllowHidingColumns(false);
			tablaNotificaciones.setAllowSorting(false);
			tablaNotificaciones.setReorderingAllowed(false);
			tablaNotificaciones.setHeaderAlignment(COL_LEIDA, FWJTable.CENTER_ALIGN);
			tablaNotificaciones.setHeaderAlignment(COL_TEXTO, FWJTable.CENTER_ALIGN);
		}
		return tablaNotificaciones;
	}
}
