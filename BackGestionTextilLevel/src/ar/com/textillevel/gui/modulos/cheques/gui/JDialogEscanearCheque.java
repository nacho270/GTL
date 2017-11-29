package ar.com.textillevel.gui.modulos.cheques.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.util.GuiUtil;

public class JDialogEscanearCheque extends JDialog {

	private static final long serialVersionUID = 6005951655684335470L;

	private boolean acepto = false;
	private FWJNumericTextField fakeTextFieldCodigo;
	private String codigoEscaneado;
	
	public JDialogEscanearCheque(JDialog padre) {
		super(padre);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(410, 120));
		add(getFakeTextFieldCodigo(), BorderLayout.CENTER);
		JLabel lbl = new JLabel("Escanee el cheque...");
		lbl.setFont(new Font("SansSerif", Font.BOLD, 40));
		add(lbl, BorderLayout.CENTER);
		setResizable(false);
		setTitle("Escanee el cheque");
		GuiUtil.centrar(this);
		setResizable(false);
		setModal(true);
		pedirFoco();
		setVisible(true);
	}

	public boolean isAcepto() {
		return acepto;
	}

	public String getCodigoEscaneado() {
		return codigoEscaneado;
	}

	private void pedirFoco() {
		fakeTextFieldCodigo.requestFocus();
		fakeTextFieldCodigo.requestFocusInWindow();
	}
	
	public FWJNumericTextField getFakeTextFieldCodigo() {
		if(fakeTextFieldCodigo == null) {
			fakeTextFieldCodigo = new FWJNumericTextField();
			fakeTextFieldCodigo.addKeyListener(new KeyAdapter() {
	            @Override
	            public void keyPressed(final KeyEvent e) {
	                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                	codigoEscaneado = escapar(fakeTextFieldCodigo.getText().trim());
	                	acepto = true;
	                	dispose();
	                }
	            }
	            
				@Override
	            public void keyReleased(KeyEvent e) {
	            	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                	codigoEscaneado = escapar(fakeTextFieldCodigo.getText().trim());
	                	acepto = true;
	                	dispose();
	                }
	            }
			});
		}
		return fakeTextFieldCodigo;
	}

    private String escapar(String codigo) {
		return codigo.replaceAll("[;:]", "");
	}

}