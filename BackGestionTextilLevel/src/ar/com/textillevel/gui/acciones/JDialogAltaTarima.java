package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.Tarima;
import ar.com.textillevel.facade.api.remote.TarimaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAltaTarima extends JDialog {

	private static final long serialVersionUID = 7014638560974798493L;

	private CLJNumericTextField txtNumeroTarima;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private TarimaFacadeRemote tarimaFacade;
	private Tarima tarima;

	public JDialogAltaTarima(Frame padre) {
		super(padre);
		setUpComponentes();
		setUpScreen();
		getTxtNumeroTarima().requestFocus();
	}

	private void setUpScreen() {
		setTitle("Datos de la Tarima");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		pack();
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}
	
	private JPanel getPanelCentral(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("NÚMERO DE TARIMA: "));
		panel.add(getTxtNumeroTarima());
		return panel;
	}

	private JPanel getPanelSur(){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	private CLJNumericTextField getTxtNumeroTarima() {
		if(txtNumeroTarima == null){
			txtNumeroTarima = new CLJNumericTextField();
			txtNumeroTarima.setPreferredSize(new Dimension(50, 20));
			txtNumeroTarima.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getBtnAceptar().doClick();
				}
			});
		}
		return txtNumeroTarima;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(getTxtNumeroTarima().getText().trim().length()==0){
						CLJOptionPane.showErrorMessage(JDialogAltaTarima.this, "Debe ingresar el número de tarima", "Error");
						getTxtNumeroTarima().requestFocus();
						return;
					}
					tarima = new Tarima();
					tarima.setNumero(getTxtNumeroTarima().getValue());
					try {
						tarima = getTarimaFacade().save(tarima);
					} catch (ValidacionException e1) {
						tarima = null;
						CLJOptionPane.showErrorMessage(JDialogAltaTarima.this,StringW.wordWrap(e1.getMensajeError()) ,"Error");
						return;
					}
					dispose();
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
					tarima = null;
					dispose();
				}
			});
		}
		return btnCancelar;
	}
	
	public static void main(String[]args){
		new JDialogAltaTarima(null).setVisible(true);
	}
	
	private TarimaFacadeRemote getTarimaFacade() {
		if(tarimaFacade == null) {
			tarimaFacade = GTLBeanFactory.getInstance().getBean2(TarimaFacadeRemote.class);
		}
		return tarimaFacade;
	}

	public Tarima getTarima() {
		return tarima;
	}

}
