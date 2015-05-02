package ar.com.textillevel.gui.util.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;

public class JDialogInputFechaDesdeHasta extends JDialog {

	private static final long serialVersionUID = -6172781105927699380L;

	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private Date fechaDesdeElegida;
	private Date fechaHastaElegida;

	private boolean acepto;

	public JDialogInputFechaDesdeHasta(Frame frame, String titulo) {
		super(frame);
		setUpComponentes();
		setUpScreen(titulo);
	}

	private void setUpScreen(String titulo) {
		setTitle(titulo);
		setModal(true);
		setSize(new Dimension(250, 150));
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelCentro(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private JPanel getPanelCentro() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		p.add(getPanelFechaDesde());
		p.add(getPanelFechaHasta());
		return p;
	}

	private JPanel getPanelSur() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		p.add(getBtnAceptar());
		p.add(getBtnCancelar());
		return p;
	}

	public PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Desde: ");
		}
		return panelFechaDesde;
	}

	public PanelDatePicker getPanelFechaHasta() {
		if (panelFechaHasta == null) {
			panelFechaHasta = new PanelDatePicker();
			panelFechaHasta.setCaption("Hasta: ");
		}
		return panelFechaHasta;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						setFechaDesdeElegida(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
						setFechaHastaElegida(new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if (getPanelFechaDesde().getDate() == null) {
			CLJOptionPane.showErrorMessage(this, "La 'fecha desde' ingresada es nula o inválida", "Error");
			return false;
		}
		if (getPanelFechaHasta().getDate() == null) {
			CLJOptionPane.showErrorMessage(this, "La 'fecha hasta' ingresada es nula o inválida", "Error");
			return false;
		}
		if (getPanelFechaDesde().getDate().after(getPanelFechaHasta().getDate())) {
			CLJOptionPane.showErrorMessage(this, "La 'fecha hasta' debe ser posterior a la 'Fecha desde'", "Error");
			return false;
		}
		return true;
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

	public Date getFechaDesdeElegida() {
		return fechaDesdeElegida;
	}

	public void setFechaDesdeElegida(Date fechaDesdeElegida) {
		this.fechaDesdeElegida = fechaDesdeElegida;
	}

	public Date getFechaHastaElegida() {
		return fechaHastaElegida;
	}

	public void setFechaHastaElegida(Date fechaHastaElegida) {
		this.fechaHastaElegida = fechaHastaElegida;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
}
