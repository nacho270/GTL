package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.DestinatarioEmail;
import ar.com.textillevel.facade.api.remote.DestinatarioEmailFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.componentes.AutocompleteJComboBox;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogInputEmail extends JDialog {

	private static final long serialVersionUID = 1L;

	private AutocompleteJComboBox<DestinatarioEmail> cmbContacto;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel panelCentral;
	
	private String email;
	private DestinatarioEmailFacadeRemote destEmailFacade;
	
	public JDialogInputEmail(Frame padre, String title) {
		super(padre);
		setTitle(title);
		setUpComponentes();
		setUpScreen();
		getCmbContacto().requestFocus();
	}

	private void setUpScreen() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		pack();
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	protected JPanel getPanelCentral(){
		if(panelCentral == null) {
			panelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelCentral.add(getCmbContacto());
		}
		return panelCentral;
	}
	
	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	public AutocompleteJComboBox<DestinatarioEmail> getCmbContacto() {
		if(cmbContacto == null){

			cmbContacto = new AutocompleteJComboBox<DestinatarioEmail>(getDestEmailFacade().getAll()) {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean match(String param, DestinatarioEmail value) {
					return value.getEmail().indexOf(param) != -1;
				}

			};
		}
		return cmbContacto;
	}
	
	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String mailIngresado = null;
					Object selectedItem = getCmbContacto().getSelectedItem();
					if(selectedItem != null) {
						if(selectedItem instanceof DestinatarioEmail) {
							mailIngresado = ((DestinatarioEmail)selectedItem).getEmail();
						}
						if(selectedItem instanceof String && GenericUtils.isEmailValido((String)selectedItem)) {
							mailIngresado = (String)selectedItem;
						}
					}
					if(mailIngresado == null) {
						FWJOptionPane.showErrorMessage(JDialogInputEmail.this, "El mail ingresado es incorrecto", "Error");
						return;
					}
					setEmail(mailIngresado);
					dispose();
				}
			});
		}
		return btnAceptar;
	}
	
	public JButton getBtnCancelar() {
		if(btnCancelar == null){
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setEmail(null);
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private DestinatarioEmailFacadeRemote getDestEmailFacade() {
		if(destEmailFacade == null) {
			destEmailFacade = GTLBeanFactory.getInstance().getBean2(DestinatarioEmailFacadeRemote.class);
		}
		return destEmailFacade;
	}

	public String getEmail() {
		return email;
	}

	private void setEmail(String email) {
		this.email = email;
	}

}