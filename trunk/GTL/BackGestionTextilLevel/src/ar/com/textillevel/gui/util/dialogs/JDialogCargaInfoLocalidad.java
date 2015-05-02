package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.com.textillevel.entidades.gente.InfoLocalidad;
import ar.com.textillevel.facade.api.remote.InfoLocalidadFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargaInfoLocalidad extends JDialog{

	private static final long serialVersionUID = 7364390484648139031L;
	
	private InfoLocalidadFacadeRemote infoLocalidadFacade;

	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private CLJTextField txtNombreLocalidad;
	private CLJNumericTextField txtCodArea;
	private CLJNumericTextField txtCodigoPostal;
	
	private JPanel pnlBotones;
	private JPanel pnlDatos;
	
	private InfoLocalidad infoLocalidadNueva;
	
	public JDialogCargaInfoLocalidad(Frame padre){
		super(padre);
		infoLocalidadFacade = GTLBeanFactory.getInstance().getBean2(InfoLocalidadFacadeRemote.class);
		setUpComponentes();
		setUpScreen();
	}
	
	private void setUpScreen(){
		setTitle("Carga localidad");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(300, 150));
		setResizable(false);
		setModal(true);
	}
	
	private void setUpComponentes(){
		add(getPanelDatos(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
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
			pnlDatos.setLayout(new GridLayout(3, 2,1,1));
			pnlDatos.add(new JLabel("Localidad: "));
			pnlDatos.add(getTxtNombreLocalidad());
			pnlDatos.add(new JLabel("Cod. Postal: "));
			pnlDatos.add(getTxtCodigoPostal());
			pnlDatos.add(new JLabel("Cod. Area: "));
			pnlDatos.add(getTxtCodArea());
		}
		return pnlDatos;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(validar()){
							InfoLocalidad newInfo = new InfoLocalidad();
							newInfo.setCodigoArea(getTxtCodArea().getValue());
							newInfo.setCodigoPostal(getTxtCodigoPostal().getValue());
							newInfo.setNombreLocalidad(getTxtNombreLocalidad().getText().toUpperCase());
							infoLocalidadNueva = infoLocalidadFacade.guardarInfoLocalidad(newInfo);
							CLJOptionPane.showInformationMessage(JDialogCargaInfoLocalidad.this, "Los datos se han guardado correctamente", "Carga localidad");
							dispose();
						}
					}catch(RuntimeException re){
						BossError.gestionarError(re);
					}
				}

				private boolean validar() {
					if(getTxtNombreLocalidad().getText().trim().length() == 0){
						CLJOptionPane.showErrorMessage(JDialogCargaInfoLocalidad.this, "Debe ingresar la localidad", JDialogCargaInfoLocalidad.this.getTitle());
						return false;
					}
					
					if(getTxtCodigoPostal().getText().trim().length() == 0){
						CLJOptionPane.showErrorMessage(JDialogCargaInfoLocalidad.this, "Debe ingresar el código postal", JDialogCargaInfoLocalidad.this.getTitle());
						return false;
					}
					
					if(getTxtCodArea().getText().trim().length() == 0){
						CLJOptionPane.showErrorMessage(JDialogCargaInfoLocalidad.this, "Debe ingresar el código de área", JDialogCargaInfoLocalidad.this.getTitle());
						return false;
					}
					
					if(getTxtCodArea().getText().equalsIgnoreCase(getTxtCodigoPostal().getText())){
						CLJOptionPane.showErrorMessage(JDialogCargaInfoLocalidad.this, "El código postal no puede conincidir con el código de área", JDialogCargaInfoLocalidad.this.getTitle());
						return false;
					}
					
					return true;
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
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private CLJTextField getTxtNombreLocalidad() {
		if(txtNombreLocalidad == null){
			txtNombreLocalidad = new CLJTextField();
		}
		return txtNombreLocalidad;
	}

	private CLJNumericTextField getTxtCodArea() {
		if(txtCodArea == null){
			txtCodArea = new CLJNumericTextField();
		}
		return txtCodArea;
	}

	private CLJNumericTextField getTxtCodigoPostal() {
		if(txtCodigoPostal == null){
			txtCodigoPostal = new CLJNumericTextField();
		}
		return txtCodigoPostal;
	}

	public InfoLocalidad getInfoLocalidad() {
		return infoLocalidadNueva;
	}

}
