package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.ImageUtil;

public class JDialogPasswordInput extends JDialog {

	private static final long serialVersionUID = -6280455947912716623L;

	private boolean acepto;
	private JButton btnOk;
	private JButton btnCancel;
	private JPasswordField txtPass;
	private char[] password;
	private JPanel panelCentral;

	public JDialogPasswordInput(Frame padre, String titulo) {
		super(padre);
		setAcepto(false);
		setUpComponentes();
		setUpScreen(titulo);
	}

	private void setUpScreen(String titulo) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(380, 80));
		setTitle(titulo);
		setResizable(false);
		GuiUtil.centrar(this);
		setModal(true);
		setVisible(true);
	}

	private void setUpComponentes() {
		add(getPanelCentral(), BorderLayout.CENTER);
	}

	private JPanel getPanelCentral() {
		if (panelCentral == null) {
			panelCentral = new JPanel();
			panelCentral.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
			panelCentral.add(new JLabel(ImageUtil.loadIcon("ar/com/textillevel/imagenes/llave.png")));
			panelCentral.add(new JLabel("Clave: "));
			panelCentral.add(getTxtPass());
			panelCentral.add(getBtnOk());
			panelCentral.add(getBtnCancel());
			panelCentral.setBackground(Color.WHITE);
		}
		return panelCentral;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton("Aceptar");
			btnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						password = getTxtPass().getPassword();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnOk;
	}

	private boolean validar() {
		if (getTxtPass().getPassword().length == 0) {
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la clave", "Error");
			getTxtPass().requestFocus();
			return false;
		}
		return true;
	}

	private JPasswordField getTxtPass() {
		if (txtPass == null) {
			txtPass = new JPasswordField();
			txtPass.setPreferredSize(new Dimension(100, 20));
			txtPass.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnOk().doClick();
				}
			});
		}
		return txtPass;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	private JButton getBtnCancel() {
		if(btnCancel == null){
			btnCancel = new JButton("Cancelar");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancel;
	}
}
