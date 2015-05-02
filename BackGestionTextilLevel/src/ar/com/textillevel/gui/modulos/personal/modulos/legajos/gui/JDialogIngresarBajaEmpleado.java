package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;

public class JDialogIngresarBajaEmpleado extends JDialog {

	private static final long serialVersionUID = -3821045755509979920L;

	private JButton btnAceptar;
	private JButton btnCancelar;
	private PanelDatePicker panelFechaBaja;
	private CLJTextField txtObservacionesBaja;

	private Date fechaElegida;
	private String observacionesIngresadas;
	
	private Date fechaAltaAfip;

	private boolean acepto;

	public JDialogIngresarBajaEmpleado(Frame padre, Date fechaAltaAfip) {
		super(padre);
		setUpComponentes();
		setUpScreen();
		setFechaAltaAfip(fechaAltaAfip);
	}

	private void setUpScreen() {
		setTitle("Dar de baja empleado");
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCentral(), BorderLayout.CENTER);
		add(getPanelSur(),BorderLayout.SOUTH);
	}

	private JPanel getPanelCentral() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(getPanelFechaBaja(),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		panel.add(new JLabel("Observaciones: "),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtObservacionesBaja(),GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		return panel;
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}
	
	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					java.util.Date date = getPanelFechaBaja().getDate();
					if(date == null){
						CLJOptionPane.showErrorMessage(JDialogIngresarBajaEmpleado.this, "Debe ingresar la fecha de baja", "Error");
						return;
					}else{
						Date fechaElegida2 = new Date(date.getTime());
						if(!fechaElegida2.before(getFechaAltaAfip())){
							setFechaElegida(fechaElegida2);
							setObservacionesIngresadas(getTxtObservacionesBaja().getText().trim().toUpperCase());
							setAcepto(true);
							dispose();
						}else{
							CLJOptionPane.showErrorMessage(JDialogIngresarBajaEmpleado.this, StringW.wordWrap("La fecha de baja debe ser igual o posterior a la última fecha de alta en la AFIP: " + DateUtil.dateToString(getFechaAltaAfip(), DateUtil.SHORT_DATE)), "Error");
							return;
						}
					}
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setAcepto(false);
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public PanelDatePicker getPanelFechaBaja() {
		if (panelFechaBaja == null) {
			panelFechaBaja = new PanelDatePicker();
			panelFechaBaja.clearFecha();
			panelFechaBaja.setCaption("Fecha de baja: ");
		}
		return panelFechaBaja;
	}

	public CLJTextField getTxtObservacionesBaja() {
		if (txtObservacionesBaja == null) {
			txtObservacionesBaja = new CLJTextField();
		}
		return txtObservacionesBaja;
	}

	public Date getFechaElegida() {
		return fechaElegida;
	}

	public void setFechaElegida(Date fechaElegida) {
		this.fechaElegida = fechaElegida;
	}

	public String getObservacionesIngresadas() {
		return observacionesIngresadas;
	}

	public void setObservacionesIngresadas(String observacionesIngresadas) {
		this.observacionesIngresadas = observacionesIngresadas;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	
	public Date getFechaAltaAfip() {
		return fechaAltaAfip;
	}

	
	public void setFechaAltaAfip(Date fechaAltaAfip) {
		this.fechaAltaAfip = fechaAltaAfip;
	}

}
