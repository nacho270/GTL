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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.MaskFormatter;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.entidades.Dia;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;
import ar.com.textillevel.facade.api.remote.ParametrosGeneralesFacadeRemote;
import ar.com.textillevel.gui.modulos.personal.abm.tareas.handler.ValorHoraHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.UppercaseDocumentFilter;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.contratos.Contrato;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.ContratoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.HorarioDia;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RangoDias;
import ar.com.textillevel.modulos.personal.entidades.legajos.RangoHorario;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Puesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.CategoriaFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ContratoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.DiaFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.PuestoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogAgregarModificarLegajo extends JDialog {

	private static final long serialVersionUID = 8523908953834209726L;

	private static final int MAX_DURACION_CONTRATO = 90;
	private static final Integer CANT_MAX_HORAS_POR_DIA = 10;
	
	private JComboBox cmbSindicatos;
	private JComboBox cmbPuestos;
	private JComboBox cmbCategorias;
	private JComboBox cmbContratos;
	private CLJTextField txtPrecioHora;
	private CLJNumericTextField txtNumeroLegajo;
	private CLJTextField txtSueldoEstimadoQuincenal;
	private CLJTextField txtSueldoEstimadoMensual;
	private CLJTextField txtObservaciones;
	private CLJTextField txtOtrasObservaciones;
	private PanelDatePicker fechaAltaAfip;
	private CLJNumericTextField txtNroTarjeta;
	private PanelTablaHorarios tablaHorarios;
	private PanelDatePicker fechaAltaContrato;
	private CLJNumericTextField txtDuracionContrato;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JCheckBox chkAfiliadoASindicato;
	private JFormattedTextField txtCtaBanco;
	private JComboBox cmbBancoPago;
	private CLJTextField txtFechaFinContrato;

	private boolean acepto;
	private LegajoEmpleado legajo;
	private ContratoEmpleado contratoEmpleado;
	
	private DiaFacadeRemote diaFacade;

	public JDialogAgregarModificarLegajo(Frame frame) {
		super(frame);
		setLegajo(new LegajoEmpleado());
		setContratoEmpleado(new ContratoEmpleado());
		setUpScreen();
		setUpComponentes();
		getTxtNumeroLegajo().setText(String.valueOf(GTLPersonalBeanFactory.getInstance().getBean2(EmpleadoFacadeRemote.class).getProximoNumeroLegajo()));
		getTxtNumeroLegajo().requestFocus();
		getTablaHorarios().refreshTable(true);
	}

	public JDialogAgregarModificarLegajo(Frame frame, LegajoEmpleado legajoAModificar, ContratoEmpleado contratoEmpleado) {
		super(frame);
		setUpScreen();
		setUpComponentes();
		setLegajo(legajoAModificar);
		setContratoEmpleado(contratoEmpleado);
		loadData();
	}

	private void loadData() {
		getTxtNumeroLegajo().setValue(getLegajo().getNroLegajo().longValue());
		if(getLegajo().getNroTarjeta()!=null){
			getTxtNroTarjeta().setValue(getLegajo().getNroTarjeta().longValue());
		}
		getTxtObservaciones().setText(getLegajo().getObservaciones());
		getTxtOtrasObservaciones().setText(getLegajo().getOtrosComentarios());
		List<VigenciaEmpleado> historialVigencias = getLegajo().getHistorialVigencias();
		if(!historialVigencias.isEmpty()){
			VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size()-1);
			if (ultima.getFechaAlta() != null) {
				getFechaAltaAfip().setSelectedDate(ultima.getFechaAlta());
			}
		}
		getCmbSindicatos().setSelectedItem(getLegajo().getPuesto().getSindicato());
		getCmbPuestos().setSelectedItem(getLegajo().getPuesto());
		getCmbCategorias().setSelectedItem(getLegajo().getCategoria());
		getTxtPrecioHora().setText(GenericUtils.getDecimalFormat().format(getLegajo().getValorHora()));
		getTablaHorarios().refreshTable(false);
		getCmbContratos().setSelectedItem(getContratoEmpleado().getContrato());
		getPanelFechaAltaContrato().setSelectedDate(getContratoEmpleado().getFechaDesde());
		getTxtDuracionContrato().setValue(getContratoEmpleado().getCantidadDias().longValue());
		getChkAfiliadoASindicato().setSelected(getLegajo().getAfiliadoASindicato());
		calcularFechaDeFinDeContrato();
		if (getContratoEmpleado().getContrato().getTipoContrato() == ETipoContrato.EFECTIVO) {
			getCmbBancoPago().setSelectedItem(getLegajo().getBancoDePago());
			getTxtCtaBanco().setText(getLegajo().getNroCuentaBanco());
		}
	}

	private void setUpScreen() {
		setTitle("Alta/modificación de legajo");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(930, 500));
		setResizable(false);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		add(getPanelNorte(), BorderLayout.NORTH);
		add(getTablaHorarios(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private JPanel getPanelNorte() {
		JPanel panelCentro = new JPanel(new GridBagLayout());
		panelCentro.add(new JLabel("Legajo Nro: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getTxtNumeroLegajo(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelCentro.add(getFechaAltaAfip(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		JPanel panelCombos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelCombos.add(new JLabel("Sindicato: "));
		panelCombos.add(getCmbSindicatos());
		panelCombos.add(new JLabel("Puesto: "));
		panelCombos.add(getCmbPuestos());
		panelCombos.add(new JLabel("Categoria: "));
		panelCombos.add(getCmbCategorias());
		panelCombos.add(new JLabel("Valor hora: "));
		panelCombos.add(getTxtPrecioHora());
		panelCentro.add(panelCombos, GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 1, 1, 0));
		return panelCentro;
	}

	private JPanel getPanelSur() {
		JPanel panelSur = new JPanel(new GridBagLayout());
		panelSur.add(new JLabel("Sueldo estimado (quincena): "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getTxtSueldoEstimadoQuincenal(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(new JLabel("Sueldo estimado (mensual): "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getTxtSueldoEstimadoMensual(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		panelSur.add(new JLabel("Tarjeta Nro: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getTxtNroTarjeta(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		panelSur.add(getChkAfiliadoASindicato(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		panelSur.add(new JLabel("Tipo de contrato: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getCmbContratos(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getPanelFechaAltaContrato(), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(new JLabel("Duración (días): "), GenericUtils.createGridBagConstraints(3, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getTxtDuracionContrato(), GenericUtils.createGridBagConstraints(4, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(new JLabel("Fecha de fin: "), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getTxtFechaFinContrato(), GenericUtils.createGridBagConstraints(6, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		panelSur.add(new JLabel("Nº cuenta: "), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getTxtCtaBanco(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getCmbBancoPago(), GenericUtils.createGridBagConstraints(2, 3, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 4, 1, 1, 0));

		panelSur.add(new JLabel("Observaciones: "), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panelSur.add(getTxtObservaciones(), GenericUtils.createGridBagConstraints(1, 4, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 4, 1, 1, 0));

		if (GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin()) {
			panelSur.add(new JLabel("Otras observaciones: "), GenericUtils.createGridBagConstraints(0, 5, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelSur.add(getTxtOtrasObservaciones(), GenericUtils.createGridBagConstraints(1, 5, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 4, 1, 1, 0));
		}

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelBotones.add(getBtnAceptar());
		panelBotones.add(getBtnCancelar());

		panelSur.add(panelBotones, GenericUtils.createGridBagConstraints(1, 6, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 5, 1, 0, 0));

		return panelSur;
	}

	private class PanelTablaHorarios extends PanelTabla<HorarioDia> {

		private static final long serialVersionUID = -7340499308062112114L;

		private static final int CANT_COLS = 3;
		private static final int COL_RANGO_DIAS = 0;
		private static final int COL_RANGO_HORAS = 1;
		private static final int COL_OBJ = 2;

		public PanelTablaHorarios() {
			agregarBotonModificar();
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_RANGO_DIAS, "Días", 180, 180, true);
			tabla.setStringColumn(COL_RANGO_HORAS, "Horario", 180, 180, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(HorarioDia elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_RANGO_DIAS] = elemento.getRangoDias();
			row[COL_RANGO_HORAS] = elemento.getRangoHorario();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected HorarioDia getElemento(int fila) {
			return (HorarioDia) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarHorario dialog = new JDialogAgregarModificarHorario(JDialogAgregarModificarLegajo.this, getLegajo().getHorario());
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				getLegajo().getHorario().add(dialog.getHorarioDia());
				refreshTable(false);
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			int filaSeleccionda = getTabla().getSelectedRow();
			getLegajo().getHorario().remove(filaSeleccionda);
			refreshTable(false);
			return false;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			if (filaSeleccionada > -1) {
				HorarioDia hd = getElemento(getTabla().getSelectedRow());
				List<HorarioDia> horarios = new ArrayList<HorarioDia>(getLegajo().getHorario());
				horarios.remove(hd);
				JDialogAgregarModificarHorario dialog = new JDialogAgregarModificarHorario(JDialogAgregarModificarLegajo.this, hd, horarios);
				dialog.setVisible(true);
				if (dialog.isAcepto()) {
					horarios.add(filaSeleccionada, hd);
					horarios.set(filaSeleccionada, dialog.getHorarioDia());
					refreshTable(false);
				}
			}
		}

		private void refreshTable(boolean addHorarioDefault) {
			getTabla().removeAllRows();
			if(addHorarioDefault) {//Muestro por default 8:18
				getLegajo().getHorario().add(getHorarioDiaPorDefault());
			}
			for (HorarioDia hd : getLegajo().getHorario()) {
				agregarElemento(hd);
			}
			calcularSueldoEstimado();
		}
	}

	private void calcularSueldoEstimado() {
		if (getLegajo().getHorario().size() > 0 && getTxtPrecioHora().getText().trim().length() > 0 && GenericUtils.esNumerico(getTxtPrecioHora().getText().replaceAll("\\.", ""))) {
			String textoValorHora = getTxtPrecioHora().getText();
			Double valorHora = Double.valueOf(textoValorHora.replaceAll("\\.", "").replaceAll(",", "."));
			if (valorHora >= 0) {
				Double sueldoEstimado = 0d;
				for (HorarioDia hd : getLegajo().getHorario()) {
					sueldoEstimado += hd.getTotalDeHoras() * valorHora;
				}
				getTxtSueldoEstimadoQuincenal().setText(GenericUtils.getDecimalFormat().format(sueldoEstimado * 2));
				getTxtSueldoEstimadoMensual().setText(GenericUtils.getDecimalFormat().format(sueldoEstimado * 4));
			} else {
				getTxtSueldoEstimadoQuincenal().setText("");
				getTxtSueldoEstimadoMensual().setText("");
			}
		} else {
			getTxtSueldoEstimadoQuincenal().setText("");
			getTxtSueldoEstimadoMensual().setText("");
		}
	}

	private JComboBox getCmbSindicatos() {
		if (cmbSindicatos == null) {
			cmbSindicatos = new JComboBox();
			GuiUtil.llenarCombo(cmbSindicatos, GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class).getAllOrderByName(), true);
			cmbSindicatos.setSelectedIndex(-1);
			cmbSindicatos.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refrescarComboPuestos();
					}
				}
			});
		}
		return cmbSindicatos;
	}

	private JComboBox getCmbPuestos() {
		if (cmbPuestos == null) {
			cmbPuestos = new JComboBox();
			cmbPuestos.setEnabled(false);
			cmbPuestos.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refrescarComboCategorias();
					}
				}
			});
		}
		return cmbPuestos;
	}

	private JComboBox getCmbCategorias() {
		if (cmbCategorias == null) {
			cmbCategorias = new JComboBox();
			cmbCategorias.setEnabled(false);
			cmbCategorias.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refrescarPrecioPorHora();
					}
				}
			});
		}
		return cmbCategorias;
	}

	private JComboBox getCmbContratos() {
		if (cmbContratos == null) {
			cmbContratos = new JComboBox();
			GuiUtil.llenarCombo(cmbContratos, GTLPersonalBeanFactory.getInstance().getBean2(ContratoFacadeRemote.class).getAll(), true);
			cmbContratos.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						Contrato contrato = (Contrato) getCmbContratos().getSelectedItem();
						getTxtCtaBanco().setEnabled(contrato.getTipoContrato() == ETipoContrato.EFECTIVO);
						getCmbBancoPago().setEnabled(contrato.getTipoContrato() == ETipoContrato.EFECTIVO);
						getTxtDuracionContrato().setEnabled(contrato.getTipoContrato() != ETipoContrato.EFECTIVO);
						calcularFechaDeFinDeContrato();
					}
				}
			});
		}
		return cmbContratos;
	}

	private CLJTextField getTxtPrecioHora() {
		if (txtPrecioHora == null) {
			txtPrecioHora = new CLJTextField();
//			txtPrecioHora.setEditable(GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin());
			txtPrecioHora.setPreferredSize(new Dimension(80, 20));
			txtPrecioHora.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					calcularSueldoEstimado();
				}
			});
		}
		return txtPrecioHora;
	}

	private CLJNumericTextField getTxtNumeroLegajo() {
		if (txtNumeroLegajo == null) {
			txtNumeroLegajo = new CLJNumericTextField();
			txtNumeroLegajo.setPreferredSize(new Dimension(120, 20));
		}
		return txtNumeroLegajo;
	}

	private CLJTextField getTxtSueldoEstimadoQuincenal() {
		if (txtSueldoEstimadoQuincenal == null) {
			txtSueldoEstimadoQuincenal = new CLJTextField();
			txtSueldoEstimadoQuincenal.setEditable(false);
		}
		return txtSueldoEstimadoQuincenal;
	}

	private CLJTextField getTxtSueldoEstimadoMensual() {
		if (txtSueldoEstimadoMensual == null) {
			txtSueldoEstimadoMensual = new CLJTextField();
			txtSueldoEstimadoMensual.setEditable(false);
		}
		return txtSueldoEstimadoMensual;
	}

	private CLJTextField getTxtObservaciones() {
		if (txtObservaciones == null) {
			txtObservaciones = new CLJTextField();
			((AbstractDocument) txtObservaciones.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
		}
		return txtObservaciones;
	}

	private CLJTextField getTxtOtrasObservaciones() {
		if (txtOtrasObservaciones == null) {
			txtOtrasObservaciones = new CLJTextField();
			((AbstractDocument) txtOtrasObservaciones.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
		}
		return txtOtrasObservaciones;
	}

	private PanelDatePicker getFechaAltaAfip() {
		if (fechaAltaAfip == null) {
			fechaAltaAfip = new PanelDatePicker();
			fechaAltaAfip.setCaption("Fecha alta AFIP");
			fechaAltaAfip.clearFecha();
		}
		return fechaAltaAfip;
	}

	private CLJNumericTextField getTxtNroTarjeta() {
		if (txtNroTarjeta == null) {
			txtNroTarjeta = new CLJNumericTextField();
			txtNroTarjeta.setPreferredSize(new Dimension(120, 20));
		}
		return txtNroTarjeta;
	}

	private PanelTablaHorarios getTablaHorarios() {
		if (tablaHorarios == null) {
			tablaHorarios = new PanelTablaHorarios();
		}
		return tablaHorarios;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	private void refrescarComboPuestos() {
		Integer indiceSeleccionado = getCmbSindicatos().getSelectedIndex();
		if (indiceSeleccionado > -1) {
			Integer idSindicato = ((Sindicato) getCmbSindicatos().getItemAt(indiceSeleccionado)).getId();
			getCmbPuestos().setEnabled(true);
			GuiUtil.llenarCombo(getCmbPuestos(), GTLPersonalBeanFactory.getInstance().getBean2(PuestoFacadeRemote.class).getAllByIdSindicato(idSindicato), true);
		} else {
			getCmbPuestos().removeAllItems();
			getCmbPuestos().setEnabled(false);
			getCmbCategorias().removeAllItems();
			getCmbCategorias().setEnabled(false);
			getTxtPrecioHora().setText("");
		}
	}

	private void refrescarComboCategorias() {
		getCmbCategorias().setEnabled(true);
		Integer idPuesto = ((Puesto) getCmbPuestos().getSelectedItem()).getId();
		GuiUtil.llenarCombo(getCmbCategorias(), GTLPersonalBeanFactory.getInstance().getBean2(CategoriaFacadeRemote.class).getCategoriasPorPuesto(idPuesto), true);
	}

	private void refrescarPrecioPorHora() {
		Categoria cat = (Categoria) getCmbCategorias().getSelectedItem();
		Puesto puesto = (Puesto) getCmbPuestos().getSelectedItem();
		BigDecimal valorHora = ValorHoraHandler.getInstance().getValorHoraCategoriaPuesto(cat, puesto);
		if (valorHora == null) {
			CLJOptionPane.showErrorMessage(JDialogAgregarModificarLegajo.this,
					StringW.wordWrap("No existe una configuración para la fecha actual, que pueda determinar el valor de la hora para el puesto y la categoría seleccionada."), "Error");
			return;
		}
		getTxtPrecioHora().setText(GenericUtils.getDecimalFormat().format(valorHora));
	}

	private void salir() {
		if (CLJOptionPane.showQuestionMessage(this, "Va a salir sin guardar, está seguro?", "Pregunta") == CLJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		}
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						caputurarDatos();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private void caputurarDatos() {
		if(getLegajo().getFechaAlta()==null){
			getLegajo().setFechaAlta(DateUtil.getHoy());
		}
		Date date = getFechaAltaAfip().getDate();
		if(date!=null){
			if(getLegajo().getHistorialVigencias().isEmpty()){
				VigenciaEmpleado vigencia = new VigenciaEmpleado();
				vigencia.setFechaAlta( new java.sql.Date(date.getTime()));
				getLegajo().getHistorialVigencias().add(vigencia);
			}else{
				VigenciaEmpleado ultima = getLegajo().getHistorialVigencias().get(getLegajo().getHistorialVigencias().size()-1);
				if(ultima.getFechaBaja()==null){
					ultima.setFechaAlta(new java.sql.Date(date.getTime()));
					getLegajo().getHistorialVigencias().set(getLegajo().getHistorialVigencias().indexOf(ultima), ultima);
				}else{
					if(CLJOptionPane.showQuestionMessage(this, "Va a dar de alta nuevamente al empleado, esta seguro de continuar?", "Advertencia")==CLJOptionPane.YES_OPTION){
						VigenciaEmpleado vigencia = new VigenciaEmpleado();
						vigencia.setFechaAlta( new java.sql.Date(date.getTime()));
						getLegajo().getHistorialVigencias().add(vigencia);
						getLegajo().setDadoDeBaja(false);
					}else{
						return;
					}
				}
			}
		}
		getLegajo().setCategoria((Categoria) getCmbCategorias().getSelectedItem());
		getLegajo().setNroLegajo(getTxtNumeroLegajo().getValueWithNull());
		getLegajo().setNroTarjeta(getTxtNroTarjeta().getValueWithNull());
		getLegajo().setObservaciones(getTxtObservaciones().getText());
		getLegajo().setOtrosComentarios(getTxtOtrasObservaciones().getText());
		getLegajo().setPuesto((Puesto) getCmbPuestos().getSelectedItem());
		String valorHora = getTxtPrecioHora().getText().replaceAll("\\.", "").replaceAll(",", ".");
		getLegajo().setValorHora(new BigDecimal(Double.valueOf(valorHora)));
		String sueldoEstimado = getTxtSueldoEstimadoQuincenal().getText().replaceAll("\\.", "").replaceAll(",", ".");
		String sueldoEstimadoMens = getTxtSueldoEstimadoMensual().getText().replaceAll("\\.", "").replaceAll(",", ".");
		getLegajo().setSueldoEstimadoQuincenal(new BigDecimal(Double.valueOf(sueldoEstimado)));
		getLegajo().setSueldoEstimadoMensual(new BigDecimal(sueldoEstimadoMens));
		Contrato contratoElegido = (Contrato) getCmbContratos().getSelectedItem();
		getContratoEmpleado().setContrato(contratoElegido);
		getContratoEmpleado().setFechaDesde(new java.sql.Date(getPanelFechaAltaContrato().getDate().getTime()));
		getContratoEmpleado().setCantidadDias(getTxtDuracionContrato().getValue());
		getLegajo().setAfiliadoASindicato(getChkAfiliadoASindicato().isSelected());
		if (contratoElegido.getTipoContrato() == ETipoContrato.EFECTIVO) {
			getLegajo().setBancoDePago((Banco) getCmbBancoPago().getSelectedItem());
			getLegajo().setNroCuentaBanco(getTxtCtaBanco().getText());
		} else {
			getLegajo().setBancoDePago(null);
			getLegajo().setNroCuentaBanco(null);
		}

	}

	private boolean validar() {
		if (getTxtNumeroLegajo().getValueWithNull() == null) {
			CLJOptionPane.showErrorMessage(this, "Debe ingresar un número de legajo", "Error");
			getTxtNroTarjeta().requestFocus();
			return false;
		}
		if (getCmbSindicatos().getSelectedIndex() == -1) {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un sindicato", "Error");
			getCmbSindicatos().requestFocus();
			return false;
		}
		if (getCmbCategorias().getSelectedIndex() == -1) {
			CLJOptionPane.showErrorMessage(this, "Debe elegir una categoria", "Error");
			getCmbCategorias().requestFocus();
			return false;
		}
		if (getLegajo().getHorario().size() == 0) {
			CLJOptionPane.showErrorMessage(this, "Debe elegir el horario a cumplir", "Error");
			return false;
		}
		if (getCmbContratos().getSelectedIndex() == -1) {
			CLJOptionPane.showErrorMessage(this, "Debe elegir el tipo de contrato", "Error");
			getCmbContratos().requestFocus();
			return false;
		}
		if (getPanelFechaAltaContrato().getDate() == null) {
			CLJOptionPane.showErrorMessage(this, "Debe elegir la fecha de alta del contrato", "Error");
			getPanelFechaAltaContrato().requestFocus();
			return false;
		}
		
		if ((getLegajo().getNroLegajo() == null || !getLegajo().getNroLegajo().equals(getTxtNumeroLegajo().getValue()))
				&& GTLPersonalBeanFactory.getInstance().getBean2(EmpleadoFacadeRemote.class).getEmpleadoByNumeroLegajo(getTxtNumeroLegajo().getValue()) != null) {
			CLJOptionPane.showErrorMessage(this, "Ya existe un legajo con el número ingresado", "Error");
			getTxtNumeroLegajo().requestFocus();
			return false;
		}
		boolean okHorario = verificarRestriccionesDeHorarios(GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin());
		if (!okHorario) {
			return false;
		}

		Contrato contratoSeleccionado = (Contrato) getCmbContratos().getSelectedItem();
		if(contratoSeleccionado.getTipoContrato() != ETipoContrato.EFECTIVO){
			if (getTxtDuracionContrato().getValueWithNull() == null) {
				CLJOptionPane.showErrorMessage(this, "Debe elegir la duración del contrato", "Error");
				getTxtDuracionContrato().requestFocus();
				return false;
			}
			if (getTxtDuracionContrato().getValue() > MAX_DURACION_CONTRATO) {
				CLJOptionPane.showErrorMessage(this, "La duración del contrato de ser " + MAX_DURACION_CONTRATO + " días como máximo.", "Error");
				getTxtDuracionContrato().requestFocus();
				return false;
			}
		}
		if (getContratoEmpleado().getContrato() == null) {// creacion de nuevo legajo
			if (contratoSeleccionado.getTipoContrato() == ETipoContrato.EFECTIVO) {
				CLJOptionPane.showErrorMessage(this, "No puede cargarse a un trabajador como efectivo sin antes estar contratado.", "Error");
				return false;
			}
		} else {
			if (getContratoEmpleado().getContrato().getTipoContrato() == ETipoContrato.EFECTIVO && contratoSeleccionado.getTipoContrato() != ETipoContrato.EFECTIVO) {
				CLJOptionPane.showErrorMessage(this, "Un empleado efectivo no puede volver a tener un contrato.", "Error");
				return false;
			}
		}
		if(contratoSeleccionado.getTipoContrato() == ETipoContrato.EFECTIVO && getTxtCtaBanco().getText().length()<12){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar un número de cuenta correcto", "Error");
			getTxtCtaBanco().requestFocus();
			return false;
		}
		
		if(!getLegajo().getHistorialVigencias().isEmpty()){
			List<VigenciaEmpleado> historialVigencias = getLegajo().getHistorialVigencias();
			VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size()-1);
			if(ultima!=null){
				Date date = getFechaAltaAfip().getDate();
				java.sql.Date fechaSql = new java.sql.Date(date.getTime());
				if(ultima.getFechaBaja()!=null){
					if(!fechaSql.after(ultima.getFechaBaja())){
						CLJOptionPane.showErrorMessage(this, "La nueva fecha de alta es anterior a la fecha de última baja: " +DateUtil.dateToString(ultima.getFechaBaja()), "Error");
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean verificarRestriccionesDeHorarios(Boolean isAdmin) {
		boolean trabajaMasHorasDeLasPermitidas = false;
		boolean trabajaDespuesDeSabado13Hs = false;
		boolean trabajaDe6A18 = true;

		boolean ret = true;

		for (HorarioDia hd : getLegajo().getHorario()) {
			if (!trabajaMasHorasDeLasPermitidas) {
				trabajaMasHorasDeLasPermitidas = hd.getRangoHorario().getCantidadDeHoras() > CANT_MAX_HORAS_POR_DIA;
			}
			if (!trabajaDespuesDeSabado13Hs) {
				trabajaDespuesDeSabado13Hs = hd.getRangoDias().getDiaHasta().getNroDia() == 6 && hd.getRangoHorario().getHoraHasta() > 13 || hd.getRangoDias().getDiaHasta().getNroDia() == 7;
			}
			if (trabajaDe6A18) {
				trabajaDe6A18 = hd.getRangoHorario().getHoraDesde() >= 6 && hd.getRangoHorario().getHoraDesde() <= 18 && hd.getRangoHorario().getHoraHasta() >= 6
						&& hd.getRangoHorario().getHoraHasta() <= 18;
			}
		}

		if (!trabajaDe6A18 || trabajaDespuesDeSabado13Hs || trabajaMasHorasDeLasPermitidas) {
			String mensaje = "Se han encontrado los siguientes problemas: \n";
			if (!trabajaDe6A18) {
				mensaje += "\t- El empleado no trabaja de 6 a 18 hs en uno de los rangos de horarios seleccionados.\n";
			}
			if (trabajaDespuesDeSabado13Hs) {
				mensaje += "\t- El empleado trabaja después del sábado a las 13 hs en uno de los rangos de horarios seleccionados.\n";
			}
			if (trabajaMasHorasDeLasPermitidas) {
				mensaje += "\t- El empleado trabaja mas de "+CANT_MAX_HORAS_POR_DIA+" hs en uno de los rangos de horarios seleccionados.\n";
			}
			mensaje += (isAdmin ? "\n\nDesea continuar?" : "");

			if (isAdmin) {
				if (CLJOptionPane.showQuestionMessage(this, mensaje, "Advertencia") == CLJOptionPane.NO_OPTION) {
					ret = false;
				}
			} else {
				CLJOptionPane.showErrorMessage(this, mensaje, "Error");
				ret = false;
			}
		}

		return ret;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}
			});
		}
		return btnCancelar;
	}

	public ContratoEmpleado getContratoEmpleado() {
		return contratoEmpleado;
	}

	public void setContratoEmpleado(ContratoEmpleado contratoEmpleado) {
		this.contratoEmpleado = contratoEmpleado;
	}

	private PanelDatePicker getPanelFechaAltaContrato() {
		if (fechaAltaContrato == null) {
			fechaAltaContrato = new PanelDatePicker();
			fechaAltaContrato.clearFecha();
			fechaAltaContrato.setCaption("Fecha de contrato: ");
		}
		return fechaAltaContrato;
	}

	private CLJNumericTextField getTxtDuracionContrato() {
		if (txtDuracionContrato == null) {
			txtDuracionContrato = new CLJNumericTextField();
			txtDuracionContrato.setColumns(10);
			txtDuracionContrato.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {
					calcularFechaDeFinDeContrato();
				}
			});
		}
		return txtDuracionContrato;
	}

	private JCheckBox getChkAfiliadoASindicato() {
		if (chkAfiliadoASindicato == null) {
			chkAfiliadoASindicato = new JCheckBox("Afiliado a sindicato");
		}
		return chkAfiliadoASindicato;
	}

	private JFormattedTextField getTxtCtaBanco() {
		if (txtCtaBanco == null) {
			try {
				txtCtaBanco = new JFormattedTextField(new MaskFormatter("###-######/#"));
				txtCtaBanco.setPreferredSize(new Dimension(120, 20));
				txtCtaBanco.addFocusListener(new FocusAdapter() {

					@Override
					public void focusLost(FocusEvent e) {
						try {
							txtCtaBanco.commitEdit();
						} catch (ParseException e1) {
						}
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return txtCtaBanco;
	}

	private JComboBox getCmbBancoPago() {
		if (cmbBancoPago == null) {
			cmbBancoPago = new JComboBox();
			GuiUtil.llenarCombo(cmbBancoPago, GTLBeanFactory.getInstance().getBean2(BancoFacadeRemote.class).getAllOrderByName(), true);
			Banco bancoDefault = GTLBeanFactory.getInstance().getBean2(ParametrosGeneralesFacadeRemote.class).getParametrosGenerales().getBancoDefault();
			if (bancoDefault != null) {
				cmbBancoPago.setSelectedItem(bancoDefault);
			}
		}
		return cmbBancoPago;
	}

	private CLJTextField getTxtFechaFinContrato() {
		if (txtFechaFinContrato == null) {
			txtFechaFinContrato = new CLJTextField();
			txtFechaFinContrato.setEditable(false);
			txtFechaFinContrato.setPreferredSize(new Dimension(120, 20));
		}
		return txtFechaFinContrato;
	}

	private void calcularFechaDeFinDeContrato() {
		Contrato contrato = (Contrato) getCmbContratos().getSelectedItem();
		Date fechaAlta = getPanelFechaAltaContrato().getDate();
		if (contrato != null && contrato.getTipoContrato() == ETipoContrato.PLAZO_FIJO && fechaAlta != null) {
			java.sql.Date fechaFinal = DateUtil.sumarDias(fechaAlta, getTxtDuracionContrato().getValue());
			getTxtFechaFinContrato().setText(DateUtil.dateToString(fechaFinal, DateUtil.WEEK_DAY_SHORT_DATE));
		} else {
			getTxtFechaFinContrato().setText("");
		}
	}

	private DiaFacadeRemote getDiaFacade() {
		if(diaFacade == null) {
			this.diaFacade = GTLPersonalBeanFactory.getInstance().getBean2(DiaFacadeRemote.class);			
		}
		return diaFacade;
	}

	private HorarioDia getHorarioDiaPorDefault() {
		HorarioDia hd = new HorarioDia();
		List<Dia> allDias = getDiaFacade().getAllDias();
		RangoDias rd = new RangoDias();
		for(Dia d : allDias) {
			if(d.getNroDia() == 1) {//lunes
				rd.setDiaDesde(d);
			}
			if(d.getNroDia() == 5) {//viernes
				rd.setDiaHasta(d);
			}
		}
		hd.setRangoDias(rd);
		RangoHorario rh = new RangoHorario();
		rh.setHoraDesde(8);
		rh.setMinutosDesde(0);
		rh.setHoraHasta(16);
		rh.setMinutosHasta(0);
		hd.setRangoHorario(rh);
		return hd;
	}

}