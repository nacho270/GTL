package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.CalculadorTotalesVisitor;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.ReciboSueldoDatosHandler;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.cabecera.ModeloCabeceraReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.exception.InvalidStateReciboSueldoException;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.impresion.ImprimirReciboSueldoHandler;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.util.AnotacionesHelper;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoDeduccion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoHaber;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoNoRemunerativo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoRetencion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EQuincena;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.ETipoItemReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.visitor.IItemReciboSueldoVisitor;
import ar.com.textillevel.modulos.personal.facade.api.remote.ReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;
import ar.com.textillevel.modulos.personal.utils.ReciboSueldoHelper;

public class JDialogReciboSueldo extends JDialog {

	private static final long serialVersionUID = 8783205750870810991L;

	private Frame owner;
	private ReciboSueldo reciboSueldo;

	private JPanel panDatosEncabezado;
	private JTextField txtPeriodoAbonado;
	private JTextField txtLegajo;
	private JTextField txtNombre;
	private JTextField txtCUIL;
	private JTextField txtValorHora;
	private JButton btnActualizar;
	
	private PanTablaItemsReciboSueldo panTablaItemsReciboSueldo;
	
	private JPanel panDatosTotalNeto;
	private JTextField txtImporteNeto;
	
	private JPanel panAnotaciones;
	private JTextField txtAnotaciones;

	
	private JPanel panelBotones;
	private JButton btnGrabar;
	private JButton btnCancelar;
	private JButton btnVerificar;
	private JButton btnImprimir;
	
	private ReciboSueldoFacadeRemote reciboSueldoFacade;

	public JDialogReciboSueldo(Frame owner, ReciboSueldo reciboSueldo) {
		super(owner);
		setModal(true);
		this.owner = owner;
		this.reciboSueldo = reciboSueldo;
		setTitle("Recibo de Sueldo");
		construct();
		setSize(800, 630);
		handleSeleccionControles();
		setDatos();
	}

	private void handleSeleccionControles() {
		boolean niVerificadoNiImpreso = reciboSueldo.getEstado() != EEstadoReciboSueldo.IMPRESO && reciboSueldo.getEstado() != EEstadoReciboSueldo.VERIFICADO;
		getBtnGrabar().setEnabled(niVerificadoNiImpreso);
		getBtnVerificar().setEnabled(reciboSueldo.getEstado() == EEstadoReciboSueldo.CREADO);
		getBtnImprimir().setEnabled(reciboSueldo.getEstado() == EEstadoReciboSueldo.IMPRESO || reciboSueldo.getEstado() == EEstadoReciboSueldo.VERIFICADO);
		getBtnActualizar().setEnabled(niVerificadoNiImpreso);
		getTxtValorHora().setEditable(niVerificadoNiImpreso);
	}

	private void setDatos() {
		getTxtPeriodoAbonado().setText(ReciboSueldoHelper.getInstance().calcularPeriodoRS(reciboSueldo));
		getTxtLegajo().setText(reciboSueldo.getLegajo().getNroLegajo().toString());
		getTxtNombre().setText(reciboSueldo.getLegajo().getEmpleado().getApellido() + ", " + reciboSueldo.getLegajo().getEmpleado().getNombre());
		getTxtCUIL().setText(reciboSueldo.getLegajo().getEmpleado().getDocumentacion().getCuit());
		getTxtValorHora().setText(GenericUtils.getDecimalFormat().format(reciboSueldo.getValorHora().doubleValue()));
		llenarDatosTabla();
	}

	private void llenarDatosTabla() {
		llenarTablaItemsReciboSueldo();
		llenarAnotaciones();
		llenarImporteNeto();
	}

	private void llenarAnotaciones() {
		getTxtAnotaciones().setText(AnotacionesHelper.getInstance().getSingleTextWithAllAnotaciones(reciboSueldo));
	}

	private void llenarImporteNeto() {
		double neto = reciboSueldo.getNeto().doubleValue();
		String netoFormatted = GenericUtils.getDecimalFormat().format(neto);
		String netoLetras = GenericUtils.convertirNumeroATexto(neto);
		getTxtImporteNeto().setText("$ " + netoFormatted + " 	Son pesos " + netoLetras);
	}

	private void llenarTablaItemsReciboSueldo() {
		getPanTablaItemsReciboSueldo().agregarElementos(reciboSueldo.getItems());
		getPanTablaItemsReciboSueldo().agregarFilaTotal(reciboSueldo);
	}

	private void construct() {
		setLayout(new GridBagLayout());
		add(getPanDatosEncabezado(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0.2, 0));
		add(getPanTablaItemsReciboSueldo(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0.8, 0.8));
		add(getPanAnotaciones(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 0.1));
		add(getPanDatosTotalNeto(), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 0, 0.1));
		add(getPanelBotones(), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
	}

	private JPanel getPanDatosEncabezado() {
		if(panDatosEncabezado == null){
			panDatosEncabezado = new JPanel();
			panDatosEncabezado.setLayout(new GridBagLayout());
			panDatosEncabezado.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosEncabezado.add(getTxtNombre(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panDatosEncabezado.add(new JLabel("Período Abonado: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosEncabezado.add(getTxtPeriodoAbonado(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panDatosEncabezado.add(new JLabel("Número de Legajo: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosEncabezado.add(getTxtLegajo(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosEncabezado.add(new JLabel("CUIL: "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosEncabezado.add(getTxtCUIL(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosEncabezado.add(new JLabel("Valor de la Hora: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosEncabezado.add(getTxtValorHora(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosEncabezado.add(getBtnActualizar(), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosEncabezado.setBorder(BorderFactory.createTitledBorder("Datos básicos"));
		}
		return panDatosEncabezado;
	}

	private JTextField getTxtCUIL() {
		if(txtCUIL == null) {
			txtCUIL = new JTextField();
			txtCUIL.setEditable(false);
		}
		return txtCUIL;
	}

	private JTextField getTxtValorHora() {
		if(txtValorHora == null) {
			txtValorHora = new JTextField();
		}
		return txtValorHora;
	}
	
	private JTextField getTxtNombre() {
		if(txtNombre == null) {
			txtNombre = new JTextField();
			txtNombre.setEditable(false);
		}
		return txtNombre;
	}

	private JTextField getTxtLegajo() {
		if(txtLegajo == null) {
			txtLegajo = new JTextField();
			txtLegajo.setEditable(false);
		}
		return txtLegajo;
	}

	private JTextField getTxtPeriodoAbonado() {
		if(txtPeriodoAbonado == null) {
			txtPeriodoAbonado = new JTextField();
			txtPeriodoAbonado.setEditable(false);
		}
		return txtPeriodoAbonado;
	}

	private PanTablaItemsReciboSueldo getPanTablaItemsReciboSueldo() {
		if(panTablaItemsReciboSueldo == null) {
			panTablaItemsReciboSueldo = new PanTablaItemsReciboSueldo();
		}
		return panTablaItemsReciboSueldo;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnGrabar());
			panelBotones.add(getBtnVerificar());
			panelBotones.add(getBtnImprimir());
			panelBotones.add(getBtnCancelar());
		}
		return panelBotones;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}

			});
		}
		return btnCancelar;
	}

	private JButton getBtnGrabar() {
		if (btnGrabar == null) {
			btnGrabar = new JButton("Grabar");
			btnGrabar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					boolean check = checkValorHoraIngresado();
					if(check) {
						reciboSueldo.setEstado(EEstadoReciboSueldo.CREADO);
						reciboSueldo = getReciboSueldoFacade().guardarRecibo(reciboSueldo);
						CLJOptionPane.showInformationMessage(owner, "El recibo se ha guardado con éxito", "Información");
						getBtnVerificar().setEnabled(true);
					}
				}

			});

		}
		return btnGrabar;
	}

	private boolean checkValorHoraIngresado() {
		boolean valorHoraDistintoYDecideContinuar = false;
		BigDecimal valorHora = getMontoInTextField(getTxtValorHora());
		if(valorHora == null || valorHora.compareTo(reciboSueldo.getValorHora()) != 0) {
			valorHoraDistintoYDecideContinuar = CLJOptionPane.showQuestionMessage(owner, StringW.wordWrap("El valor de hora ingresado '" + valorHora + "' es diferente al utilizado para calcular los items. ¿Desea continuar?") , "Información") == CLJOptionPane.YES_OPTION;
			if(!valorHoraDistintoYDecideContinuar) {
				return false;
			}
		}
		return true;
	}
	
	private JButton getBtnVerificar() {
		if (btnVerificar == null) {
			btnVerificar = new JButton("Verificar");
			btnVerificar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					boolean check = checkValorHoraIngresado();
					if(check) {
						reciboSueldo.setEstado(EEstadoReciboSueldo.VERIFICADO);
						reciboSueldo = getReciboSueldoFacade().guardarRecibo(reciboSueldo);
						CLJOptionPane.showInformationMessage(owner, "El recibo se ha verificado con éxito", "Información");
						getBtnGrabar().setEnabled(false);
						getBtnVerificar().setEnabled(false);
						getBtnImprimir().setEnabled(true);
					}
				}

			});

		}
		return btnVerificar;
	}

	private JButton getBtnImprimir() {
		if (btnImprimir == null) {
			btnImprimir = new JButton("Imprimir");
			btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					reciboSueldo = getReciboSueldoFacade().getByIdEager(reciboSueldo.getId());
					ImprimirReciboSueldoHandler handler = new ImprimirReciboSueldoHandler(reciboSueldo, owner);
					if(handler.imprimir() && reciboSueldo.getEstado() == EEstadoReciboSueldo.VERIFICADO) {
						reciboSueldo.setEstado(EEstadoReciboSueldo.IMPRESO);
						getReciboSueldoFacade().guardarRecibo(reciboSueldo);
					} 
					dispose();
				}

			});

		}
		return btnImprimir;
	}

	private class PanTablaItemsReciboSueldo extends PanelTabla<ItemReciboSueldo> {

		private static final long serialVersionUID = 854148236242980928L;

		public static final int CANT_COLS = 9;
		public static final int COL_CONCEPTO = 0;
		public static final int COL_CODIGO = 1;
		public static final int COL_UNIDADES = 2;
		public static final int COL_HABERES = 3;
		public static final int COL_RETENCIONES = 4;
		public static final int COL_ASIGNACIONES = 5;
		public static final int COL_DEDUCCIONES = 6;
		public static final int COL_NO_REMUNERATIVO = 7;
		public static final int COL_OBJ = 8;

		public PanTablaItemsReciboSueldo() {
			getBotonAgregar().setVisible(false);
		}

		public void agregarFilaTotal(ReciboSueldo reciboSueldo) {
			Object[] row = new Object[CANT_COLS];
			row[COL_CONCEPTO] = "TOTALES BRUTO";
			row[COL_HABERES] = GenericUtils.getDecimalFormat().format(reciboSueldo.getBruto());
			row[COL_RETENCIONES] = GenericUtils.getDecimalFormat().format(reciboSueldo.getTotalRetenciones());
			row[COL_NO_REMUNERATIVO] = GenericUtils.getDecimalFormat().format(reciboSueldo.getTotalNoRemunerativo());
			row[COL_DEDUCCIONES] = GenericUtils.getDecimalFormat().format(reciboSueldo.getTotalDeducciones());
			getTabla().addRow(row);
			getTabla().setBackgroundRow(getTabla().getRowCount() - 1, Color.GRAY);
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_CONCEPTO, "Descripción de los conceptos", 150, 150, true);
			tabla.setStringColumn(COL_CODIGO, "Código", 50, 50, true);
			tabla.setStringColumn(COL_UNIDADES, "Unidades", 70, 70, true);
			tabla.setStringColumn(COL_HABERES, "Haberes", 70, 70, true);
			tabla.setStringColumn(COL_RETENCIONES, "Retenciones", 80, 80, true);
			tabla.setStringColumn(COL_ASIGNACIONES, "Asignaciones", 80, 80, true);
			tabla.setStringColumn(COL_DEDUCCIONES, "Deducciones", 90, 90, true);
			tabla.setStringColumn(COL_NO_REMUNERATIVO, "No Remunerativo", 90, 90, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			return tabla;
		}

		@Override
		protected void agregarElemento(ItemReciboSueldo elemento) {
			IItemReciboSueldoVisitor visitor = new AgregarItemReciboSueldoVisitor(getTabla());
			elemento.aceptarVisitor(visitor);
		}

		@Override
		protected ItemReciboSueldo getElemento(int fila) {
			return (ItemReciboSueldo)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarQuitar() {
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow != -1) {
				reciboSueldo.getItems().remove(selectedRow);
				CalculadorTotalesVisitor visitor = new CalculadorTotalesVisitor(reciboSueldo);
				for(ItemReciboSueldo irs : reciboSueldo.getItems()) {
					irs.aceptarVisitor(visitor);
				}
				getTabla().removeAllRows();
				llenarDatosTabla();
			}
			return true;
		}

		@Override
		protected void filaTablaSeleccionada() {
			int selectedRow = getTabla().getSelectedRow();
			if(selectedRow != -1) {
				ItemReciboSueldo item = getElemento(selectedRow);
				if(item==null){
					getBotonEliminar().setEnabled(false);
					return;
				}
				boolean niVerificadoNiImpreso = reciboSueldo.getEstado() != EEstadoReciboSueldo.VERIFICADO && reciboSueldo.getEstado() != EEstadoReciboSueldo.IMPRESO; 
				getBotonEliminar().setEnabled(niVerificadoNiImpreso && (item.getTipoItemReciboSueldo()==ETipoItemReciboSueldo.HORAS_EXTRAS_AL_50 || item.getTipoItemReciboSueldo()==ETipoItemReciboSueldo.HORAS_EXTRAS_AL_100));
			}
		}

		private class AgregarItemReciboSueldoVisitor implements IItemReciboSueldoVisitor {
			
			private CLJTable tabla;

			private AgregarItemReciboSueldoVisitor(CLJTable tabla) {
				this.tabla = tabla;
			}

			public void visit(ItemReciboSueldoHaber irsh) {
				Object[] row = new Object[PanTablaItemsReciboSueldo.CANT_COLS];
				row[PanTablaItemsReciboSueldo.COL_CONCEPTO] = irsh.getDescripcion();
				row[PanTablaItemsReciboSueldo.COL_CODIGO] = irsh.getCodigo();
				row[PanTablaItemsReciboSueldo.COL_UNIDADES] = irsh.getUnidades();
				row[PanTablaItemsReciboSueldo.COL_HABERES] = GenericUtils.getDecimalFormat().format(irsh.getMonto());
				row[PanTablaItemsReciboSueldo.COL_OBJ] = irsh;
				tabla.addRow(row);
			}

			public void visit(ItemReciboSueldoNoRemunerativo irsnr) {
				Object[] row = new Object[PanTablaItemsReciboSueldo.CANT_COLS];
				row[PanTablaItemsReciboSueldo.COL_CONCEPTO] = irsnr.getDescripcion();
				row[PanTablaItemsReciboSueldo.COL_CODIGO] = irsnr.getCodigo();
				row[PanTablaItemsReciboSueldo.COL_UNIDADES] = irsnr.getUnidades();
				row[PanTablaItemsReciboSueldo.COL_NO_REMUNERATIVO] = GenericUtils.getDecimalFormat().format(irsnr.getMonto());
				row[PanTablaItemsReciboSueldo.COL_OBJ] = irsnr;
				tabla.addRow(row);
			}

			public void visit(ItemReciboSueldoRetencion irsr) {
				Object[] row = new Object[PanTablaItemsReciboSueldo.CANT_COLS];
				row[PanTablaItemsReciboSueldo.COL_CONCEPTO] = irsr.getDescripcion();
				row[PanTablaItemsReciboSueldo.COL_CODIGO] = irsr.getCodigo();
				row[PanTablaItemsReciboSueldo.COL_UNIDADES] = irsr.getUnidades();
				row[PanTablaItemsReciboSueldo.COL_RETENCIONES] = GenericUtils.getDecimalFormat().format(irsr.getMonto());
				row[PanTablaItemsReciboSueldo.COL_OBJ] = irsr;
				tabla.addRow(row);
			}

			public void visit(ItemReciboSueldoDeduccion irsd) {
				Object[] row = new Object[PanTablaItemsReciboSueldo.CANT_COLS];
				row[PanTablaItemsReciboSueldo.COL_CONCEPTO] = irsd.getDescripcion();
				row[PanTablaItemsReciboSueldo.COL_CODIGO] = irsd.getCodigo();
				row[PanTablaItemsReciboSueldo.COL_UNIDADES] = irsd.getUnidades();
				row[PanTablaItemsReciboSueldo.COL_DEDUCCIONES] = GenericUtils.getDecimalFormat().format(irsd.getMonto());
				row[PanTablaItemsReciboSueldo.COL_OBJ] = irsd;
				tabla.addRow(row);
			}

		}

	}

	private JPanel getPanDatosTotalNeto() {
		if(panDatosTotalNeto == null) {
			panDatosTotalNeto = new JPanel();
			panDatosTotalNeto.setLayout(new GridBagLayout());
			panDatosTotalNeto.add(new JLabel("Total Neto:"), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDatosTotalNeto.add(getTxtImporteNeto(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 0));			
		}
		return panDatosTotalNeto;
	}

	private JTextField getTxtImporteNeto() {
		if(txtImporteNeto == null) {
			txtImporteNeto = new JTextField();
			txtImporteNeto.setEditable(false);
		}
		return txtImporteNeto;
	}

	private JPanel getPanAnotaciones() {
		if(panAnotaciones == null) {
			panAnotaciones = new JPanel();
			panAnotaciones.setLayout(new GridBagLayout());
			panAnotaciones.add(new JLabel("Anotaciones:"), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panAnotaciones.add(getTxtAnotaciones(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 1, 1, 1, 0));			
		}
		return panAnotaciones;
	}

	private JTextField getTxtAnotaciones() {
		if(txtAnotaciones == null) {
			txtAnotaciones = new JTextField();
			txtAnotaciones.setEditable(false);
		}
		return txtAnotaciones;
	}

	private ReciboSueldoFacadeRemote getReciboSueldoFacade() {
		if(reciboSueldoFacade == null) {
			reciboSueldoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ReciboSueldoFacadeRemote.class);
		}
		return reciboSueldoFacade;
	}

	private JButton getBtnActualizar() {
		if(btnActualizar == null) {
			btnActualizar = new JButton("Actualizar");
			btnActualizar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					BigDecimal valorHora = getMontoInTextField(getTxtValorHora());
					if(valorHora == null) {
						CLJOptionPane.showErrorMessage(JDialogReciboSueldo.this, "Debe ingresar un valor hora válido", "Error");
						return;
					} else {
						//obtengo los vales antes de resetear los datos del RS
						List<ValeAnticipo> vales = extractVales();
						//reseteo los datos de la tabla y del RS
						getPanTablaItemsReciboSueldo().getTabla().removeAllRows();
						reciboSueldo.reset();
						//vuelvo a llenar el RS y la interfaz con el nuevo valor hora
						try {
							ModeloCabeceraReciboSueldo modelo = new ModeloCabeceraReciboSueldo();
							modelo.setAnio(reciboSueldo.getAnio());
							modelo.setMes(reciboSueldo.getMes());
							modelo.setQuincena(reciboSueldo.getQuincena() == null ? null : EQuincena.getById(reciboSueldo.getQuincena().getId()));
							new ReciboSueldoDatosHandler(reciboSueldo, modelo, valorHora, vales);
							llenarDatosTabla();
						} catch (InvalidStateReciboSueldoException e1) {
							CLJOptionPane.showErrorMessage(JDialogReciboSueldo.this, StringW.wordWrap(e1.getMessage()), "Error");
							return;
						}
					}
				}
				
			});
		}
		return btnActualizar;
	}

	private List<ValeAnticipo> extractVales() {
		List<ValeAnticipo> vales = new ArrayList<ValeAnticipo>();
		for(ItemReciboSueldo irs : reciboSueldo.getItems()) {
			if(irs instanceof ItemReciboSueldoDeduccion) {
				ItemReciboSueldoDeduccion itemDeducc = (ItemReciboSueldoDeduccion) irs;
				vales.addAll(itemDeducc.getVales());
			}
		}
		return vales;
	}

	private BigDecimal getMontoInTextField(JTextField txt) {
		String strValue = txt.getText().trim();
		if (StringUtil.isNullOrEmpty(strValue) || !GenericUtils.esNumerico(strValue)) {
			return null;
		} else {
			return new BigDecimal(strValue.replace(',', '.'));
		}
	}

}