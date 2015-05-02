package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.VerticalFlowLayout;

public class JDialogCantFilasInput extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	private CLJNumericTextField txtCantFilas;
	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private Integer cantFilas;
	private String titulo;

	public JDialogCantFilasInput(JDialog owner, String titulo) {
		super(owner);
		this.titulo = titulo;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle(titulo);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(300, 100));
		setResizable(false);
		setModal(true);
	}
	
	private void setUpComponentes(){
		add(getPanelCarga(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelCarga(){
		JPanel pnl = new JPanel();
		pnl.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP));
		pnl.add(getPanelDatos());
		return pnl;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnAceptar());
			pnlBotones.add(getBtnCancelar());
		}
		return pnlBotones;
	}

	private JPanel getPanelDatos() {
		if(pnlDatos == null){
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridLayout(1,2,0,1));
			JLabel lbl = new JLabel("Cantidad: ");
			lbl.setPreferredSize(new Dimension(20,20));
			pnlDatos.add(lbl);
			pnlDatos.add(getTxtCantFilas());
		}
		return pnlDatos;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(!getTxtCantFilas().getText().trim().equalsIgnoreCase("")) {
							cantFilas = Integer.valueOf(getTxtCantFilas().getText());
							dispose();
						}else{
							CLJOptionPane.showErrorMessage(JDialogCantFilasInput.this, "Debe ingresar la cantidad de filas.", "Error");
						}
					}catch(RuntimeException re){
						BossError.gestionarError(re);
					}
				}
			});
		}
		return btnAceptar;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cantFilas = null;
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private CLJNumericTextField getTxtCantFilas() {
		if(txtCantFilas == null){
			txtCantFilas = new CLJNumericTextField(Long.MIN_VALUE, Long.MAX_VALUE);
			txtCantFilas.setPreferredSize(new Dimension(100, 20));
			txtCantFilas.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						getBtnAceptar().doClick();
					}
				}

			});
		}
		return txtCantFilas;
	}

	public Integer getCantFilas() {
		return cantFilas;
	}
	
	
}
