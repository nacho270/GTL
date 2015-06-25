package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.VerticalFlowLayout;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.ImageUtil;
import ar.com.textillevel.modulos.fe.EstadoServidorAFIP;
import ar.com.textillevel.modulos.fe.EstadoServidorAFIP.EstadoAFIPWrapper;

public class JDialogEstadoServerAFIP extends JDialog {

	private static final long serialVersionUID = 5177698652696680802L;
	
	private PanelEstadoServicioAFIP panelEstadoAppServer;
	private PanelEstadoServicioAFIP panelEstadoAuthServer;
	private PanelEstadoServicioAFIP panelEstadoDBServer;
	private JButton btnAceptar;
	private EstadoServidorAFIP estadoAFIP;

	public JDialogEstadoServerAFIP(Frame owner, EstadoServidorAFIP estadoAfip) {
		super(owner);
		this.estadoAFIP = estadoAfip;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle("Estado de servicios de AFIP");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(470, 140));
		setResizable(false);
		GuiUtil.centrar(this);
		setModal(true);
	}
	
	private void setUpComponentes() {
		JPanel pCentro = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
		pCentro.add(getPanelEstadoAppServer());
		pCentro.add(getPanelEstadoAuthServer());
		pCentro.add(getPanelEstadoDBServer());
		add(pCentro, BorderLayout.CENTER);
		JPanel pSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pSur.add(getBtnAceptar());
		add(pSur, BorderLayout.SOUTH);
	}
	
	private class PanelEstadoServicioAFIP extends JPanel {
		
		private static final long serialVersionUID = -1697432342219657805L;

		public PanelEstadoServicioAFIP(EstadoAFIPWrapper estado) {
			setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 5, 5));
			JLabel lblNombre = new JLabel(estado.getNombre());
			Font font = lblNombre.getFont();
			lblNombre.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
			add(lblNombre);
			JLabel lblEstado = new JLabel();
			if(estado.isOK()) {
				lblEstado.setIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/chat/ok.png"));
			}else{
				lblEstado.setIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/chat/delete.png"));
			}
			add(lblEstado);
		}
	}

	public PanelEstadoServicioAFIP getPanelEstadoAppServer() {
		if(panelEstadoAppServer == null){
			panelEstadoAppServer = new PanelEstadoServicioAFIP(getEstadoAFIP().getAppServer());
		}
		return panelEstadoAppServer;
	}

	public PanelEstadoServicioAFIP getPanelEstadoAuthServer() {
		if(panelEstadoAuthServer == null){
			panelEstadoAuthServer = new PanelEstadoServicioAFIP(getEstadoAFIP().getAuthServer());
		}
		return panelEstadoAuthServer;
	}

	public PanelEstadoServicioAFIP getPanelEstadoDBServer() {
		if(panelEstadoDBServer==null){
			panelEstadoDBServer = new PanelEstadoServicioAFIP(getEstadoAFIP().getDbServer());
		}
		return panelEstadoDBServer;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public EstadoServidorAFIP getEstadoAFIP() {
		return estadoAFIP;
	}
}
