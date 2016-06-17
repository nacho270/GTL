package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogInputDatosRechazaCheque extends JDialog {

	private static final long serialVersionUID = 5082948710608602282L;

	private PanelDatePicker panelFecha;
	private FWJTextField txtMotivo;
	private FWJTextField txtGastos;

	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;

	private boolean acepto;

	private Cheque cheque;
	
	private String usrAdmin;
	
	public JDialogInputDatosRechazaCheque(Frame padre, Cheque cheque, String usrAdmin) {
		super(padre);
		setCheque(cheque);
		setUsrAdmin(usrAdmin);
		setUpComponentes();
		setUpScreen();
	}

	private void setUpScreen() {
		setModal(true);
		setSize(new Dimension(300, 200));
		setTitle("Ingrese los datos");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
		setResizable(false);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent evt){
				salir();
			}
		});
		add(getPanelCentral(), BorderLayout.CENTER);
		add(getPanelBotones(), BorderLayout.SOUTH);
	}

	public PanelDatePicker getPanelFecha() {
		if (panelFecha == null) {
			panelFecha = new PanelDatePicker();
		}
		return panelFecha;
	}

	public FWJTextField getTxtMotivo() {
		if (txtMotivo == null) {
			txtMotivo = new FWJTextField(500);
		}
		return txtMotivo;
	}

	public FWJTextField getTxtGastos() {
		if (txtGastos == null) {
			txtGastos = new FWJTextField();
		}
		return txtGastos;
	}

	public JPanel getPanelCentral() {
		if (panelCentral == null) {
			panelCentral = new JPanel();
			panelCentral.setLayout(new GridBagLayout());
			panelCentral.add(new JLabel("Fecha: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelCentral.add(getPanelFecha(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelCentral.add(new JLabel("Motivo: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelCentral.add(getTxtMotivo(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
			panelCentral.add(new JLabel("Gastos: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelCentral.add(getTxtGastos(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
		}
		return panelCentral;
	}

	public JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						if(validar()){
							ChequeFacadeRemote cfr = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
							BigDecimal gastos = null;
							if(getTxtGastos().getText().trim().length()>0){
								gastos = new BigDecimal(getTxtGastos().getText());
							}
							java.sql.Date fecha = new java.sql.Date(getPanelFecha().getDate().getTime());
							CorreccionFactura correccionFactura = cfr.rechazarCheque(getCheque(), fecha, getTxtMotivo().getText(),gastos, getUsrAdmin(), !GenericUtils.isSistemaTest());
							FWJOptionPane.showInformationMessage(JDialogInputDatosRechazaCheque.this, "El cheque ha sido rechazado correctamente.\n\nSe generó la nota de débito " + correccionFactura.getNroFactura(), "Información");
							setAcepto(true);
							dispose();
						}
					}catch(ValidacionException cle) {
						FWJOptionPane.showErrorMessage(JDialogInputDatosRechazaCheque.this, StringW.wordWrap("Se ha producido un error al rechazar el cheque: " + cle.getMensajeError()), "Error");
					}
					catch(ValidacionExceptionSinRollback cle) {
						FWJOptionPane.showErrorMessage(JDialogInputDatosRechazaCheque.this, StringW.wordWrap("Se ha producido un error al rechazar el cheque: " + cle.getMensajeError()), "Error");
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar(){
		if(getTxtMotivo().getText().trim().length()==0){
			FWJOptionPane.showErrorMessage(JDialogInputDatosRechazaCheque.this, "Debe elegir un motivo", "Error");
			getTxtMotivo().requestFocus();
			return false;
		}
		
		if(getTxtGastos().getText().trim().length()>0 && !GenericUtils.esNumerico(getTxtGastos().getText())){
			FWJOptionPane.showErrorMessage(JDialogInputDatosRechazaCheque.this, "Solo puede ingresar números", "Error");
			getTxtGastos().requestFocus();
			return false;
		}
		return true;
	}
	
	public JButton getBtnSalir() {
		if(btnSalir == null){
			btnSalir =  new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}
	
	private void salir(){
		setAcepto(false);
		dispose();
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	
	public Cheque getCheque() {
		return cheque;
	}

	
	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

	
	public String getUsrAdmin() {
		return usrAdmin;
	}

	
	public void setUsrAdmin(String usrAdmin) {
		this.usrAdmin = usrAdmin;
	}
}
