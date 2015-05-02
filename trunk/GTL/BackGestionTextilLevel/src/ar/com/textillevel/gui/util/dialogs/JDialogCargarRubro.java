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
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.VerticalFlowLayout;
import ar.com.textillevel.entidades.enums.ETipoRubro;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.facade.api.remote.RubroPersonaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCargarRubro extends JDialog{

	private static final long serialVersionUID = 7364390484648139031L;
	

	private JButton btnAceptar;
	private JButton btnCancelar;
	private CLJTextField txtNombreRubro;
	private JPanel pnlBotones;
	private JPanel pnlDatos;

	private RubroPersonaFacadeRemote rubroPersonaFacade;
	private Rubro rubroNuevo;
	private ETipoRubro tipoRubro;

	public JDialogCargarRubro(Frame padre, ETipoRubro tipoRubro){
		super(padre);
		rubroPersonaFacade = GTLBeanFactory.getInstance().getBean2(RubroPersonaFacadeRemote.class);
		this.tipoRubro = tipoRubro;
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen(){
		setTitle("Carga rubro");
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
			JLabel lbl = new JLabel("Rubro: ");
			lbl.setPreferredSize(new Dimension(20,20));
			pnlDatos.add(lbl);
			pnlDatos.add(getTxtNombre());
		}
		return pnlDatos;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(!getTxtNombre().getText().trim().equalsIgnoreCase("")) {
							Rubro rubroPersona = new Rubro();
							rubroPersona.setNombre(getTxtNombre().getText().toUpperCase());
							rubroPersona.setTipoRubro(tipoRubro);
							rubroNuevo = rubroPersonaFacade.save(rubroPersona);
							CLJOptionPane.showInformationMessage(JDialogCargarRubro.this, "Los datos se han guardado correctamente", "Carga rubro");
							dispose();
						}else{
							CLJOptionPane.showErrorMessage(JDialogCargarRubro.this, "Debe completar los datos.", "Carga rubro");
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
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	private CLJTextField getTxtNombre() {
		if(txtNombreRubro == null){
			txtNombreRubro = new CLJTextField();
			txtNombreRubro.setPreferredSize(new Dimension(100, 20));
		}
		return txtNombreRubro;
	}

	public Rubro getRubro() {
		return rubroNuevo;
	}

}
