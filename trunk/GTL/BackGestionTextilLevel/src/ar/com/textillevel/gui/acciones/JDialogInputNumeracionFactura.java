package ar.com.textillevel.gui.acciones;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.config.NumeracionFactura;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;

public class JDialogInputNumeracionFactura extends JDialog {

	private static final long serialVersionUID = -6221345742660613160L;

	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private CLJNumericTextField txtNroDesde;
	private CLJNumericTextField txtNroHasta;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private NumeracionFactura numeracionActual;
	private boolean acepto;

	public JDialogInputNumeracionFactura(Dialog padre, Date fechaDesde, Integer nroDesde){
		super(padre);
		setNumeracionActual(new NumeracionFactura());
		setUpScreen();
		setUpComponentes();
		getPanelFechaDesde().setSelectedDate(fechaDesde);
		getTxtNroDesde().setValue(nroDesde.longValue());
		getNumeracionActual().setFechaDesde(fechaDesde);
		getNumeracionActual().setNroDesde(nroDesde);
		GuiUtil.setEstadoPanel(getPanelFechaDesde(),false);
	}
	
	public JDialogInputNumeracionFactura(Dialog padre, NumeracionFactura numeracion){
		super(padre);
		setNumeracionActual(numeracion);
		setUpScreen();
		setUpComponentes();
	}
	
	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setAcepto(false);
				dispose();
			}
		});
		
		JPanel panelCentro = new JPanel(new GridBagLayout());
		panelCentro.add(getPanelFechaDesde(),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		panelCentro.add(getPanelFechaHasta(),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		
		panelCentro.add(new JLabel("Nro. desde: "),GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getTxtNroDesde(),GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		
		panelCentro.add(new JLabel("Nro. hasta: "),GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getTxtNroHasta(),GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
		
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(getBtnAceptar());
		panelSur.add(getBtnCancelar());
		
		add(panelCentro,BorderLayout.CENTER);
		add(panelSur,BorderLayout.SOUTH);
	}

	private void setUpScreen() {
		setTitle("Agregar/modificar numeraci�n");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(250, 230));
		setModal(true);
		GuiUtil.centrar(this);
	}

	public PanelDatePicker getPanelFechaDesde() {
		if(panelFechaDesde == null){
			panelFechaDesde = new PanelDatePicker(){

				private static final long serialVersionUID = 2939557077150805435L;

				@Override
				public void accionBotonCalendarioAdicional() {
					getNumeracionActual().setFechaDesde(new java.sql.Date(getDate().getTime()));
				}
			};
			panelFechaDesde.setCaption("Fecha desde: ");
			panelFechaDesde.setEditable(false);
			if(getNumeracionActual()!=null && getNumeracionActual().getFechaDesde()!=null){
				panelFechaDesde.setSelectedDate(getNumeracionActual().getFechaDesde());
			}
		}
		return panelFechaDesde;
	}

	public PanelDatePicker getPanelFechaHasta() {
		if(panelFechaHasta == null){
			panelFechaHasta = new PanelDatePicker(){

				private static final long serialVersionUID = 1163892561449235336L;

				@Override
				public void accionBotonCalendarioAdicional() {
					getNumeracionActual().setFechaHasta(new java.sql.Date(getDate().getTime()));

				};
			};
			panelFechaHasta.setCaption("Fecha hasta: ");
			panelFechaHasta.setEditable(false);
			if(getNumeracionActual()!=null && getNumeracionActual().getFechaHasta()!=null){
				panelFechaHasta.setSelectedDate(getNumeracionActual().getFechaHasta());
			}
		}
		return panelFechaHasta;
	}

	public CLJNumericTextField getTxtNroDesde() {
		if(txtNroDesde == null){
			txtNroDesde = new CLJNumericTextField(0l, Integer.MAX_VALUE);
			txtNroDesde.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					getNumeracionActual().setNroDesde(txtNroDesde.getValueWithNull());
				}
			});
			if(getNumeracionActual()!=null && getNumeracionActual().getNroDesde() != null){
				txtNroDesde.setValue(getNumeracionActual().getNroDesde().longValue());
			}
		}
		return txtNroDesde;
	}

	public CLJNumericTextField getTxtNroHasta() {
		if(txtNroHasta == null){
			txtNroHasta = new CLJNumericTextField(0l, Integer.MAX_VALUE);
			txtNroHasta.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					getNumeracionActual().setNroHasta(txtNroHasta.getValueWithNull());
				}
			});
			if(getNumeracionActual()!=null && getNumeracionActual().getNroHasta() != null){
				txtNroHasta.setValue(getNumeracionActual().getNroHasta().longValue());
			}
		}
		return txtNroHasta;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
	
	private boolean validar(){
		java.util.Date fechaDesde = getPanelFechaDesde().getDate();
		java.util.Date fechaHasta = getPanelFechaHasta().getDate();
		if(fechaDesde == null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la 'Fecha desde'", "Error");
			return false;
		}
		if(fechaHasta == null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la 'Fecha hasta'", "Error");
			return false;
		}
		Date fechaDesdeSql = new Date(fechaDesde.getTime());
		Date fechaHastaSql = new Date(fechaHasta.getTime());
		if(!fechaHastaSql.after(fechaDesdeSql)){
			CLJOptionPane.showErrorMessage(this, "La 'Fecha hasta' debe ser posterior a la 'Fecha desde'", "Error");
			return false;
		}
		Integer nroFacturaDesde = getTxtNroDesde().getValueWithNull();
		Integer nroFacturaHasta = getTxtNroHasta().getValueWithNull();
		if(nroFacturaDesde == null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el 'N�mero desde'", "Error");
			getTxtNroDesde().requestFocus();
			return false;
		}
		if(nroFacturaHasta == null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el 'N�mero hasta'", "Error");
			getTxtNroHasta().requestFocus();
			return false;
		}
		if(nroFacturaDesde>=nroFacturaHasta){
			CLJOptionPane.showErrorMessage(this, "El 'N�mero hasta' debe ser mayor al 'N�mero desde'", "Error");
			return false;
		}
		return validarAdicional(getNumeracionActual());
	}
	
	protected boolean validarAdicional(NumeracionFactura numeracionFactura) {
		return true;
	}

	public JButton getBtnAceptar() {
		if(btnAceptar == null){
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						getNumeracionActual().setFechaDesde(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
						getNumeracionActual().setFechaHasta(new java.sql.Date(getPanelFechaHasta().getDate().getTime()));
						getNumeracionActual().setNroDesde(getTxtNroDesde().getValue());
						getNumeracionActual().setNroHasta(getTxtNroHasta().getValue());
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnCancelar() {
		if(btnCancelar == null){
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

	
	public NumeracionFactura getNumeracionActual() {
		return numeracionActual;
	}

	
	public void setNumeracionActual(NumeracionFactura numeracionActual) {
		this.numeracionActual = numeracionActual;
	}
}
