package main.servicios.alertas.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.servicios.alertas.AccionNotificacion;
import main.servicios.alertas.ETipoNotificacionUI;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;
import ar.com.textillevel.modulos.notificaciones.facade.api.remote.NotificacionUsuarioFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

import com.lowagie.text.Font;

public class JDialogAccionNotificacion extends JDialog {

	private static final long serialVersionUID = -640691853104356961L;

	private NotificacionUsuarioFacadeRemote notificacionFacade = GTLBeanFactory.getInstance().getBean2(NotificacionUsuarioFacadeRemote.class);
	private NotificacionUsuario nc;

	public JDialogAccionNotificacion(Frame padre, NotificacionUsuario nc) {
		super(padre);
		this.nc = nc;
		setUpScreen();
		setUpComponentes();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setUpComponentes() {
		JLabel label = new JLabel(nc.getTexto());
		label.setFont(label.getFont().deriveFont(26f).deriveFont(Font.BOLD));
		label.setHorizontalAlignment(JLabel.CENTER);
		add(label, BorderLayout.CENTER);
		JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		ETipoNotificacionUI tipoNotificacionUI = ETipoNotificacionUI.by(nc.getTipo());
		for (final AccionNotificacion an : tipoNotificacionUI.getAcciones()) {
			JButton boton = new JButton(new AbstractAction(an.getTitulo()) {

				private static final long serialVersionUID = 2368144073667152535L;

				@Override
				public void actionPerformed(ActionEvent e) {
					an.ejecutar(nc.getIdRelacionado());
					notificacionFacade.marcarComoLeida(nc);
				}
			});
			panelAcciones.add(boton);
		}
		panelAcciones.add(new JButton(new AbstractAction("Cerrar") {

			private static final long serialVersionUID = -3324273751614783602L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}));
		add(panelAcciones, BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Notificacion");
		setSize(new Dimension(400,130));
		setResizable(false);
		GuiUtil.centrarEnFramePadre(this);
		setModal(true);
	}
}
