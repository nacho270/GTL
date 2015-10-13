package ar.com.textillevel.gui.util.controles;

import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWBotonCalendario;
import ar.com.fwcommon.componentes.FWDateField;

@SuppressWarnings("serial")
public class PanelDatePicker extends JPanel {

	private FWDateField txtFecha;
	private FWBotonCalendario btnFecha;
	private JLabel lblFecha;

	public PanelDatePicker() {
		super();
		construct();
		setVisible(true);
	}

	private void construct() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(getLblFecha());
		panel.add(getFecha());
		panel.add(getBtnCalendario());
		add(panel);
	}

	private JLabel getLblFecha() {
		if (lblFecha == null)
			lblFecha = new JLabel();
		return lblFecha;
	}

	private FWDateField getFecha() {
		if(txtFecha == null) {
			txtFecha = new FWDateField();
			txtFecha.setFecha(new java.sql.Date(System.currentTimeMillis()));
		}
		return txtFecha;
	}

	private FWBotonCalendario getBtnCalendario() {
		if (btnFecha == null) {
			btnFecha = new FWBotonCalendario() {

				private static final long serialVersionUID = -3859887013841770893L;

				@Override
				public void botonCalendarioPresionado() {
					java.sql.Date selectedDate = getCalendario().getSelectedDate();
					getFecha().setFecha(selectedDate);
					accionBotonCalendarioAdicional();
				}
			};
		}
		return btnFecha;
	}

	/**
	 * Para sobreescribir si se quiere hacer algo luego del boton presionado. Por ej: Validar una fecha
	 */
	public void accionBotonCalendarioAdicional() {
		
	}

	public String getCaption() {
		return getLblFecha().getText();
	}

	public void setCaption(String caption) {
		getLblFecha().setText(caption);
	}

	public Date getDate() {
		return getFecha().getFecha();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		txtFecha.setEnabled(enabled);
		btnFecha.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return txtFecha.isEnabled();
	}

	public void setSelectedDate(Date fecha){
		getBtnCalendario().getCalendario().setSelectedDate(new java.sql.Date(fecha.getTime()));
		getFecha().setFecha(new java.sql.Date(fecha.getTime()));
	}

	public void clearFecha() {
		getFecha().setFecha(null);
	}

	public void setEditable(boolean editable){
		txtFecha.setEditable(editable);
	}
}
