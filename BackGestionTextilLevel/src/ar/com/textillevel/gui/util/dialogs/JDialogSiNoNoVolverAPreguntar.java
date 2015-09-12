package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ar.clarin.fwjava.util.GuiUtil;

public class JDialogSiNoNoVolverAPreguntar extends JDialog {

	private static final long serialVersionUID = -6730313218245316408L;

	private int response;
	private boolean noVolverAPreguntar;
	private String texto;
	private String titulo;
	
	private JButton btnSi;
	private JButton btnNo;
	private JCheckBox chkNoVolverAPreguntar;
	
	public JDialogSiNoNoVolverAPreguntar(Frame padre, String texto, String titulo){
		super(padre);
		this.texto = texto;
		this.titulo = titulo;
		setUpComponentes();
		setUpScreen();
	}

	public JDialogSiNoNoVolverAPreguntar(Dialog padre, String texto, String titulo){
		super(padre);
		this.texto = texto;
		this.titulo = titulo;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpComponentes() {
		btnSi = new JButton("Si");
		btnSi.setMnemonic(KeyEvent.VK_S);
		btnSi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				response = JOptionPane.YES_OPTION;
				dispose();
			}
		});
		btnNo = new JButton("No");
		btnNo.setMnemonic(KeyEvent.VK_N);
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				response = JOptionPane.NO_OPTION;
				dispose();
			}
		});
		chkNoVolverAPreguntar = new JCheckBox("No volver a preguntar");
		chkNoVolverAPreguntar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				noVolverAPreguntar = chkNoVolverAPreguntar.isSelected();
			}
		});
		JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelCentro.add(new JLabel(texto));
		add(panelCentro, BorderLayout.CENTER);
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(btnSi);
		panelSur.add(btnNo);
		panelSur.add(chkNoVolverAPreguntar);
		add(panelSur, BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setTitle(titulo);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		GuiUtil.centrar(this);
		setModal(true);
		pack();
	}

	public int getResponse(){
		return response;
	}
	
	public boolean noVolverAPreguntar(){
		return noVolverAPreguntar;
	}
}
