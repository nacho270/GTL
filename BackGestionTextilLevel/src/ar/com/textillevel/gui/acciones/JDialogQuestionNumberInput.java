package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class JDialogQuestionNumberInput extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton btnSI;
	private JButton btnCancelar;
	private CLJNumericTextField txtNumberInput;
	private JPanel pnlBotones;
	private JPanel pnlDatos;
	private Integer numberInput;
	private final String titulo;
	private final String question;
	private final String textoLblInput;
	private boolean acepto;

	public JDialogQuestionNumberInput(Frame owner, String titulo, String question, String textoLblInput, Integer numberInputDefault) {
		super(owner);
		this.titulo = titulo;
		this.question = question.toUpperCase();
		this.textoLblInput = textoLblInput.toUpperCase();
		this.numberInput = numberInputDefault;  
		setAcepto(false);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle(titulo);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(330, 150));
		setResizable(false);
		setModal(true);
	}

	private void setUpComponentes(){
		add(getPanelCarga(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelCarga() {
		JPanel pnl = new JPanel();
		pnl.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP));
		pnl.add(getPanelDatos());
		return pnl;
	}

	private JPanel getPanelBotones() {
		if(pnlBotones == null){
			pnlBotones = new JPanel();
			pnlBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlBotones.add(getBtnSI());
			pnlBotones.add(getBtnNO());
		}
		return pnlBotones;
	}

	private JPanel getPanelDatos() {
		if(pnlDatos == null){
			pnlDatos = new JPanel();
			pnlDatos.setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = 0;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridwidth = 2;
			gc.weightx = 1;
			gc.insets = new Insets(0, 0, 10, 0);
			JLabel lblQuestion = new JLabel(question);
			Font fuente = lblQuestion.getFont();
			Font fuenteNueva = new Font(fuente.getName(), Font.BOLD, fuente.getSize()+2);
			lblQuestion.setFont(fuenteNueva);
			pnlDatos.add(lblQuestion, gc);
			gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = 1;
			gc.insets = new Insets(0, 0, 0, 5);
			JLabel lbl = new JLabel(textoLblInput);
			pnlDatos.add(lbl, gc);
			gc = new GridBagConstraints();
			gc.gridx = 1;
			gc.gridy = 1;
			gc.fill = GridBagConstraints.HORIZONTAL;
			pnlDatos.add(getTxtNumberInput(), gc);
		}
		return pnlDatos;
	}

	private JButton getBtnSI() {
		if(btnSI == null){
			btnSI = new JButton("SI");
			btnSI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(!getTxtNumberInput().getText().trim().equalsIgnoreCase("")) {
							numberInput = Integer.valueOf(getTxtNumberInput().getText());
							setAcepto(true);
							dispose();
						}else{
							CLJOptionPane.showErrorMessage(JDialogQuestionNumberInput.this, "Falta completar el campo '" + textoLblInput + "'", "Error");
						}
					}catch(RuntimeException re){
						BossError.gestionarError(re);
					}
				}
			});
		}
		return btnSI;
	}

	private JButton getBtnNO() {
		if(btnCancelar == null){
			btnCancelar = new JButton("NO");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					numberInput = null;
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private CLJNumericTextField getTxtNumberInput() {
		if(txtNumberInput == null){
			txtNumberInput = new CLJNumericTextField(Long.MIN_VALUE, Long.MAX_VALUE);
			txtNumberInput.setPreferredSize(new Dimension(100, 20));
			txtNumberInput.setValue(numberInput == null ? null : numberInput.longValue());
			txtNumberInput.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						getBtnSI().doClick();
					}
				}

			});
		}
		return txtNumberInput;
	}

	public Integer getNumberInput() {
		return numberInput;
	}

	
	public boolean isAcepto() {
		return acepto;
	}

	
	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

}
