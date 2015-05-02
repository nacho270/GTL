package ar.com.textillevel.gui.acciones.proveedor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLBotonCalendario;
import ar.clarin.fwjava.componentes.CLDateField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.facade.api.remote.CorreccionFacturaProveedorFacadeRemote;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogCompletarDatosCorreccionFacturaProveedor extends JDialog {

	private static final long serialVersionUID = 1L;

	private CLDateField txtFecha;
	private CLBotonCalendario btnFecha;
	private JFormattedTextField txtNroCorreccion;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private CorreccionFacturaProveedor correccionFactura;
	private String tipoCorreccionFactura; 

	private CorreccionFacturaProveedorFacadeRemote correccionFacade;
	
	public JDialogCompletarDatosCorreccionFacturaProveedor(Frame owner, CorreccionFacturaProveedor correccionFactura, String tipoCorreccionFactura) {
		this.correccionFactura = correccionFactura;
		this.tipoCorreccionFactura = tipoCorreccionFactura;
		setTitle("Completar Datos de " + tipoCorreccionFactura);
		setSize(new Dimension(350, 200));
		setResizable(false);
		setModal(true);
		construct(tipoCorreccionFactura);
	}

	private void construct(String tipoCorreccionFactura2) {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		add(new JLabel("Fecha: "), gc);
		gc = GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		add(getTxtFecha(), gc);
		gc = GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0);
		add(getBtnFecha(), gc);
		gc = GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0);
		add(new JLabel("Número de "+tipoCorreccionFactura2+": "), gc);
		gc = GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 0, 0);
		add(getTxtNroCorreccion(), gc);
		gc = GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(20, 5, 0, 5), 3, 1, 0, 0);
		add(getPanelBotones(), gc);
	}

	private CLBotonCalendario getBtnFecha() {
		if(btnFecha == null) {
			btnFecha = new CLBotonCalendario(DateUtil.getHoy()) {

				private static final long serialVersionUID = 1L;

				@Override
				public void botonCalendarioPresionado() {
					Date selectedDate = getBtnFecha().getCalendario().getSelectedDate();
					if(selectedDate != null) {
						getTxtFecha().setFecha(selectedDate);
					}
				}

			};

		}
		return btnFecha;
	}

	private JFormattedTextField getTxtNroCorreccion() {
		if(txtNroCorreccion == null) {
			try {
				txtNroCorreccion = new JFormattedTextField(new MaskFormatter("####-########"));
				txtNroCorreccion.setFocusLostBehavior(JFormattedTextField.PERSIST);
				txtNroCorreccion.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
						try {
							txtNroCorreccion.commitEdit();
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtNroCorreccion;
	}

	private CLDateField getTxtFecha() {
		if(txtFecha == null) {
			txtFecha = new CLDateField();
			txtFecha.setFecha(DateUtil.getHoy());
		}
		return txtFecha;
	}

	private JPanel getPanelBotones() {
		JPanel panBotones = new JPanel();
		panBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panBotones.add(getBtnAceptar());
		panBotones.add(getBtnCancelar());
		return panBotones;
	}

	private JButton getBtnCancelar() {
		if(btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnAceptar() {
		if(btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						capturarSetearDatos();
						getCorreccionFacade().completarDatosCorreccion(correccionFactura, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
						CLJOptionPane.showInformationMessage(JDialogCompletarDatosCorreccionFacturaProveedor.this, "Los datos de la " + tipoCorreccionFactura + " han sido grabados con éxito.", getTitle());
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private void capturarSetearDatos() {
		correccionFactura.setFechaIngreso(getTxtFecha().getFecha());
		correccionFactura.setNroCorreccion(getTxtNroCorreccion().getText().trim());
	}

	private boolean validar() {
		if(getTxtFecha().getFecha() == null) {
			CLJOptionPane.showErrorMessage(JDialogCompletarDatosCorreccionFacturaProveedor.this, "Debe ingresar una fecha válida para la " + tipoCorreccionFactura + ".", getTitle());
			return false;
		}
		if(StringUtil.isNullOrEmpty(getTxtNroCorreccion().getText())) {
			CLJOptionPane.showErrorMessage(JDialogCompletarDatosCorreccionFacturaProveedor.this, "Debe ingresar el número de " + tipoCorreccionFactura + ".", getTitle());
			getTxtNroCorreccion().requestFocus();
			return false;
		}
		String regExpNroFactura = "[0-9]{4}-[0-9]{8}";
		Pattern p = Pattern.compile(regExpNroFactura);
		Matcher matcher = p.matcher(getTxtNroCorreccion().getText().trim());
		if(!matcher.matches()) {
			CLJOptionPane.showErrorMessage(JDialogCompletarDatosCorreccionFacturaProveedor.this, "Debe ingresar un número de " + tipoCorreccionFactura + " válido.", getTitle());
			getTxtNroCorreccion().requestFocus();
			return false;
		}
		return true;
	}

	private CorreccionFacturaProveedorFacadeRemote getCorreccionFacade() {
		if(correccionFacade == null) {
			correccionFacade = GTLBeanFactory.getInstance().getBean2(CorreccionFacturaProveedorFacadeRemote.class);
		}
		return correccionFacade;
	}

	public static void main(String[] args) {
		JDialogCompletarDatosCorreccionFacturaProveedor jDialogCompletarDatosCorreccionFacturaProveedor = new JDialogCompletarDatosCorreccionFacturaProveedor(null, null, "Nota de Débito");
		jDialogCompletarDatosCorreccionFacturaProveedor.setVisible(true);
	}

}