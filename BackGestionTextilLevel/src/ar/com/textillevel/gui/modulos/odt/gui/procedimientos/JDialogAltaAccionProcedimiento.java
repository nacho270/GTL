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

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.AccionProcedimiento;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.AccionProcedimientoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAltaAccionProcedimiento extends JDialog {

	private static final long serialVersionUID = -9175208734020691699L;

	private FWJTextField txtNombreAccion;
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
		setTitle("Datos de la acción");
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
		panel.add(new JLabel("ACCIÓN: "));
		panel.add(getTxtNombreAccion());
		return panel;
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	private FWJTextField getTxtNombreAccion() {
		if (txtNombreAccion == null) {
			txtNombreAccion = new FWJTextField();
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
						FWJOptionPane.showErrorMessage(JDialogAltaAccionProcedimiento.this, "Debe ingresar el nombre de la acción", "Error");
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
						FWJOptionPane.showErrorMessage(JDialogAltaAccionProcedimiento.this, StringW.wordWrap(e1.getMensajeError()), "Error");
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
