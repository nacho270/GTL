package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

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
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.GTLGlobalCache;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.CartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.ETipoCartaDocumento;
import ar.com.textillevel.modulos.personal.facade.api.remote.CalendarioAnualFeriadoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SancionFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.CalendarioLaboralHelper;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogCartaDocumento extends JDialog {

	private static final long serialVersionUID = 1L;

	private PanelDatePicker fechaCD;
	private JTextArea txtMotivo;
	private JTextArea txtObservaciones;
	private CLJNumericTextField txtDiasSuspension;
	private JLabel lblDiasSuspencion;
	private CLJNumericTextField txtPlazoDiasJustif;
	private JLabel lblPlazoDiasJustif;
	private JTextField txtSancionesRelacionadas;

	private Frame padre;
	private JButton btnAceptar;
	private JButton btnSalir;
	private JPanel panelBotones;
	private JPanel panelGeneral;
	
	private CartaDocumento cartaDocumento;
	private boolean acepto;
	
	private SancionFacadeRemote sancionFacade;
	private CalendarioAnualFeriadoFacadeRemote calendarioFacade;


	public JDialogCartaDocumento(Frame padre, CartaDocumento cartaDocumento) {
		super(padre);
		setPadre(padre);
		setUpComponentes();
		setCartaDocumento(cartaDocumento);
		setDatos();
		setUpScreen();
		ocultarComponentesAcordeTipoCD();
	}

	private void setDatos() {
		getTxtSancionesRelacionadas().setText(StringUtil.getCadena(cartaDocumento.getSancionesAsociadas(), " - "));
	}

	private void ocultarComponentesAcordeTipoCD() {
		if(cartaDocumento.getTipoCD() != ETipoCartaDocumento.SANCION_POR_NO_JUSTIF) {
			getTxtDiasSuspension().setVisible(false);
			getLblDiasSuspencion().setVisible(false);
		}
		if(cartaDocumento.getTipoCD() != ETipoCartaDocumento.AVISO_JUSTIF_FALTA) {
			getTxtPlazoDiasJustif().setVisible(false);
			getLblPlazoDiasJustif().setVisible(false);
		}
	}

	private void setUpScreen() {
		setSize(new Dimension(520, 330));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Ingreso de Carta Documento");
		GuiUtil.centrar(this);
		setResizable(true);
		setModal(true);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		add(getPanelGeneral(), BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelBotones(){
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}
	
	private JPanel getPanelGeneral(){
		if(panelGeneral == null){
			panelGeneral = new JPanel();
			panelGeneral.setLayout(new GridBagLayout());
			panelGeneral.add(new JLabel(""), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
			panelGeneral.add(getFechaCD(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
			panelGeneral.add(new JLabel("Sanciones Relacionadas: "), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
			panelGeneral.add(getTxtSancionesRelacionadas(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 1, 1, 0, 0));
			panelGeneral.add(new JLabel("Texto de la Carta Documento: "),  GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelGeneral.add(getTxtMotivo(), GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 0.5));
			panelGeneral.add(getLblDiasSuspencion(),  GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelGeneral.add(getTxtDiasSuspension(), GenericUtils.createGridBagConstraints(1, 3,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelGeneral.add(getLblPlazoDiasJustif(),  GenericUtils.createGridBagConstraints(0, 4,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelGeneral.add(getTxtPlazoDiasJustif(), GenericUtils.createGridBagConstraints(1, 4,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelGeneral.add(new JLabel("Observaciones: "), GenericUtils.createGridBagConstraints(0, 5,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelGeneral.add(getTxtObservaciones(),  GenericUtils.createGridBagConstraints(1, 5,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 0.5));
		}
		return panelGeneral;
	}

	private JTextField getTxtSancionesRelacionadas() {
		if(txtSancionesRelacionadas == null) {
			txtSancionesRelacionadas = new JTextField();
			txtSancionesRelacionadas.setEditable(false);
		}
		return txtSancionesRelacionadas;
	}

	private JLabel getLblDiasSuspencion() {
		if(lblDiasSuspencion == null) {
			lblDiasSuspencion = new JLabel("Días de suspensión: ");
		}
		return lblDiasSuspencion;
	}

	private JLabel getLblPlazoDiasJustif() {
		if(lblPlazoDiasJustif == null) {
			lblPlazoDiasJustif = new JLabel("Plazo de días para justificar: ");
		}
		return lblPlazoDiasJustif;
	}
	
	public CartaDocumento getCartaDocumento() {
		return cartaDocumento;
	}

	private void setCartaDocumento(CartaDocumento cartaDocumento) {
		this.cartaDocumento = cartaDocumento;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public Frame getPadre() {
		return padre;
	}

	public void setPadre(Frame padre) {
		this.padre = padre;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					grabarApercibimiento();
				}
			});
		}
		return btnAceptar;
	}

	private void grabarApercibimiento() {
		if(validar()){
			capturarDatos();
			try {
				CartaDocumento cdSaved = getSancionFacade().ingresarCartaDocumento(getCartaDocumento(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				setCartaDocumento(cdSaved);
			} catch (ValidacionException e) {
				CLJOptionPane.showErrorMessage(JDialogCartaDocumento.this, e.getMensajeError(), "Error");
				setCartaDocumento(null);
				setAcepto(false);
				dispose();
				return;
			}
			setAcepto(true);
			dispose();
		}
	}

	private SancionFacadeRemote getSancionFacade() {
		if(sancionFacade == null) {
			sancionFacade = GTLPersonalBeanFactory.getInstance().getBean2(SancionFacadeRemote.class);
		}
		return sancionFacade;
	}

	private boolean validar() {
		if(getTxtMotivo().getText().trim().length() == 0){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el texto de la carta documento", "Error");
			getTxtMotivo().requestFocus();
			return false;
		}

		if(getTxtMotivo().getText().trim().length() > 700){
			CLJOptionPane.showErrorMessage(this, "La longitud máxima del motivo es de 700 caracteres", "Error");
			getTxtMotivo().requestFocus();
			return false;
		}

		if(getTxtObservaciones().getText().trim().length() > 700){
			CLJOptionPane.showErrorMessage(this, "La longitud máxima de las observaciones es de 700 caracteres", "Error");
			getTxtObservaciones().requestFocus();
			return false;
		}
		
		if(getFechaCD().getDate()==null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la fecha de entrada", "Error");
			getFechaCD().requestFocus();
			return false;
		}
		//TODO: realizar todas las validaciones
		return true;
	}

	private void capturarDatos() {
		getCartaDocumento().setFechaSancion(new java.sql.Date(getFechaCD().getDate().getTime()));
		getCartaDocumento().setMotivo(getTxtMotivo().getText().trim());
		getCartaDocumento().setObservaciones(getTxtObservaciones().getText().trim());
		if(getCartaDocumento().getTipoCD() == ETipoCartaDocumento.SANCION_POR_NO_JUSTIF) {
			getCartaDocumento().setCantDiasSuspencion(getTxtDiasSuspension().getValue());
			Date fechaIncorporacion = CalendarioLaboralHelper.calcularPrimerFechaHabil(getCalendarioFacade().getCalendarioActual(), getCartaDocumento().getFechaSancion(), getCartaDocumento().getCantDiasSuspencion());
			getCartaDocumento().setFechaIncorporacion(fechaIncorporacion);
		}
		if(getCartaDocumento().getTipoCD() == ETipoCartaDocumento.AVISO_JUSTIF_FALTA) {
			getCartaDocumento().setCantDiasPlazoJustif(getTxtPlazoDiasJustif().getValue());
		}
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnSalir;
	}

	private void salir() {
		setAcepto(false);
		dispose();
	}

	private JTextArea getTxtMotivo() {
		if (txtMotivo == null) {
			txtMotivo = new JTextArea();
			txtMotivo.setLineWrap(true);
			if(getCartaDocumento()!=null){
				txtMotivo.setText(getCartaDocumento().getMotivo());
			}
		}
		return txtMotivo;
	}
	
	private JTextArea getTxtObservaciones() {
		if (txtObservaciones == null) {
			txtObservaciones = new JTextArea();
			txtObservaciones.setLineWrap(true);
			if(getCartaDocumento()!=null){
				txtObservaciones.setText(getCartaDocumento().getObservaciones());
			}
		}
		return txtObservaciones;
	}

	private CLJNumericTextField getTxtDiasSuspension() {
		if(txtDiasSuspension == null) {
			txtDiasSuspension = new CLJNumericTextField();
			if(getCartaDocumento() != null) {
				txtDiasSuspension.setText(getCartaDocumento().getCantDiasSuspencion().toString());
			}
		}
		return txtDiasSuspension;
	}

	private CLJNumericTextField getTxtPlazoDiasJustif() {
		if(txtPlazoDiasJustif == null) {
			txtPlazoDiasJustif = new CLJNumericTextField();
			if(getCartaDocumento() != null) {
				txtPlazoDiasJustif.setText(getCartaDocumento().getCantDiasPlazoJustif().toString());
			}
		}
		return txtPlazoDiasJustif;
	}

	
	private PanelDatePicker getFechaCD() {
		if (fechaCD == null) {
			fechaCD = new PanelDatePicker();
			fechaCD.setCaption("Fecha:");
			if(getCartaDocumento()!=null){
				fechaCD.setSelectedDate(getCartaDocumento().getFechaSancion());
			}
		}
		return fechaCD;
	}
	
	public CalendarioAnualFeriadoFacadeRemote getCalendarioFacade() {
		if(calendarioFacade == null) {
			calendarioFacade = GTLPersonalBeanFactory.getInstance().getBean2(CalendarioAnualFeriadoFacadeRemote.class);
		}
		return calendarioFacade;
	}
	

}