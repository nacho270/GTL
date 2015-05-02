package ar.com.textillevel.gui.modulos.odt.gui.procedimientos;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.AccionProcedimiento;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.AccionProcedimientoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAltaAccionProcedimiento extends JDialog {

	private static final long serialVersionUID = -9175208734020691699L;

	private CLJTextField txtNombreAccion;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private AccionProcedimientoFacadeRemote accionFacade;
	private AccionProcedimiento accion;
	private ESectorMaquina sector;

	public JDialogAltaAccionProcedimiento(Dialog padre, ESectorMaquina sector) {
		super(padre);
		this.sector = sector;
		setUpComponentes();
		setUpScreen();
		getTxtNombreAccion().requestFocus();
	}

	private void setUpScreen() {
		setTitle("Datos de la acci�n");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		pack();
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCentral(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private JPanel getPanelCentral() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(new JLabel("ACCI�N: "));
		panel.add(getTxtNombreAccion());
		return panel;
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	private CLJTextField getTxtNombreAccion() {
		if (txtNombreAccion == null) {
			txtNombreAccion = new CLJTextField();
			txtNombreAccion.setPreferredSize(new Dimension(200, 20));
			txtNombreAccion.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getBtnAceptar().doClick();
				}
			});
		}
		return txtNombreAccion;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getTxtNombreAccion().getText().trim().length() == 0) {
						CLJOptionPane.showErrorMessage(JDialogAltaAccionProcedimiento.this, "Debe ingresar el nombre de la acci�n", "Error");
						getTxtNombreAccion().requestFocus();
						return;
					}
					accion = new AccionProcedimiento();
					accion.setNombre(getTxtNombreAccion().getText().trim().toUpperCase());
					accion.setSectorMaquina(sector);
					try {
						accion = getAccionFacade().save(accion);
					} catch (ValidacionException e1) {
						accion = null;
						CLJOptionPane.showErrorMessage(JDialogAltaAccionProcedimiento.this, StringW.wordWrap(e1.getMensajeError()), "Error");
						return;
					}
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					accion = null;
					dispose();
				}
			});
		}
		return btnCancelar;
	}

	public static void main(String[] args) {
		new JDialogAltaAccionProcedimiento(null, ESectorMaquina.SECTOR_HUMEDO).setVisible(true);
	}

	private AccionProcedimientoFacadeRemote getAccionFacade() {
		if (accionFacade == null) {
			accionFacade = GTLBeanFactory.getInstance().getBean2(AccionProcedimientoFacadeRemote.class);
		}
		return accionFacade;
	}

	public AccionProcedimiento getAccion() {
		return accion;
	}

}
