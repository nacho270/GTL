package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.boss.BossEstilos;
import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTablaSinBotones;
import ar.clarin.fwjava.componentes.VerticalFlowLayout;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.ImageUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.EEstadoDiaFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.to.FichadaLegajoTO;
import ar.com.textillevel.modulos.personal.entidades.fichadas.to.GrupoHoraEntradaSalidaTO;
import ar.com.textillevel.modulos.personal.entidades.legajos.HorarioDia;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;
import ar.com.textillevel.modulos.personal.facade.api.remote.FichadaLegajoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogVerInfoFichadas extends JDialog {

	private static final long serialVersionUID = 8368991417010076287L;

	private static final int CANT_MAX_PANEL_LINEA = 6;

	private static final String ICONO_BOTON_MODIF = "ar/clarin/fwjava/imagenes/b_modificar_fila.png";
	private static final String ICONO_BOTON_MODIF_DES = "ar/clarin/fwjava/imagenes/b_modificar_fila_des.png";
	public int CANT_COLS;
	public int COL_COLOR;
	public int COL_FECHA;
	public int COL_HORAS_NORMALES;
	public int COL_HORAS_EXTRA_AL_50;
	public int COL_HORAS_EXTRA_AL_100;
	public int COL_FALTA_JUSTIFICADA;
	public int COL_CAUSA_FALTA;
	public int COL_OBJ;
	public int inicioColumnasDinamicas;
	private JScrollPane jsp;

	private PanelTablaHorarios panelTablaHorarios;
	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private CLJTextField txtNroLegajo;
	private CLJTextField txtNombreYApellido;
	private CLJTextField txtSindicato;
	private JButton btnBuscar;
	private JLabel lblLoading;

	private JButton btnAceptar;

	private JPanel panelTablaFichadas;
	private JButton btnModificar;
	private CLJTable tablaFichadas;

	private LegajoEmpleado legajo;

	private FichadaLegajoFacadeRemote fichadasFacade;

	private Map<EEstadoDiaFichada, PanelReferenciaYCantidad> paneles;

	public JDialogVerInfoFichadas(Frame padre, LegajoEmpleado legajo) {
		super(padre);
		setLegajo(legajo);
		setUpComponentes();
		setUpScreen();
		loadData();
		resetContadores();
	}

	private void loadData() {
		getTxtNroLegajo().setText(String.valueOf(getLegajo().getNroLegajo()));
		getTxtNombreYApellido().setText(getLegajo().getEmpleado().toString());
		getTxtSindicato().setText(getLegajo().getPuesto().getSindicato().getNombre());
		for (HorarioDia hd : getLegajo().getHorario()) {
			getPanelTablaHorarios().agregarElemento(hd);
		}
	}

	private void setUpScreen() {
		setTitle("Información de fichadas");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setSize(new Dimension(900, 675));
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		add(getPanelNorte(), BorderLayout.NORTH);
		add(getPanelTablaFichadas(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	public JPanel getPanelNorte() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Legajo Nº:"), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNroLegajo(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(new JLabel("Nombre y apellido: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtNombreYApellido(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(new JLabel("Sindicato:"), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getTxtSindicato(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getPanelTablaHorarios(), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 0, 0));

		panel.add(getPanelFechaDesde(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 0, 0));
		panel.add(getPanelFechaHasta(), GenericUtils.createGridBagConstraints(2, 2, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 0, 0));
		panel.add(getBtnBuscar(), GenericUtils.createGridBagConstraints(4, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));

		panel.add(getLblLoading(), GenericUtils.createGridBagConstraints(5, 2, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		return panel;
	}

	public JPanel getPanelSur() {
		// JPanel panel = new JPanel(new GridLayout((int)Math.ceil(EEstadoDiaFichada.values().length/CANT_MAX_PANEL_LINEA), CANT_MAX_PANEL_LINEA));
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Referencias"));
		int indiceY = 0;
		int indiceX = 0;
		for (EEstadoDiaFichada e : getPaneles().keySet()) {
			panel.add(getPaneles().get(e), GenericUtils.createGridBagConstraints(indiceX++, indiceY, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1, 1, 1));
			// panel.add(p);
			if (indiceX == CANT_MAX_PANEL_LINEA) {
				indiceX = 0;
				indiceY++;
			}
		}
		panel.add(getBtnAceptar(), GenericUtils.createGridBagConstraints((int) Math.floor(CANT_MAX_PANEL_LINEA / 2) - 1, ++indiceY, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 0, 0));

		return panel;
	}

	private class PanelReferenciaYCantidad extends JPanel {

		private static final long serialVersionUID = 2984759166362339902L;

		private CLJNumericTextField txtCantidad;
		private Integer cantidad;

		public PanelReferenciaYCantidad(EEstadoDiaFichada estadoDia) {
			setCantidad(0);
			setLayout(new GridBagLayout());
			add(new JLabel(estadoDia.getDescripcion()), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			JLabel label = null;
			if (estadoDia.getColor() != null) {
				label = new JLabel("      ");
				label.setBackground(estadoDia.getColor());
				label.setOpaque(true);
			} else {
				label = new JLabel();
				label.setIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/img_error.png"));
			}
			label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			add(label, GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			add(getTxtCantidad(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 0, 0));
		}

		public CLJNumericTextField getTxtCantidad() {
			if (txtCantidad == null) {
				txtCantidad = new CLJNumericTextField();
				txtCantidad.setEditable(false);
				txtCantidad.setHorizontalAlignment(CLJNumericTextField.CENTER);
				txtCantidad.setPreferredSize(new Dimension(120, 20));
			}
			return txtCantidad;
		}

		public void refreshTextBox() {
			getTxtCantidad().setValue(getCantidad().longValue());
		}

		public Integer getCantidad() {
			return cantidad;
		}

		public void setCantidad(Integer cantidad) {
			this.cantidad = cantidad;
		}

		public void resetContador() {
			setCantidad(0);
		}
	}

	private class PanelTablaHorarios extends PanelTablaSinBotones<HorarioDia> {

		private static final long serialVersionUID = -7340499308062112114L;

		private static final int CANT_COLS = 3;
		private static final int COL_RANGO_DIAS = 0;
		private static final int COL_RANGO_HORAS = 1;
		private static final int COL_OBJ = 2;

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_RANGO_DIAS, "Días", 150, 150, true);
			tabla.setStringColumn(COL_RANGO_HORAS, "Horario", 150, 150, true);
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
	}

	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	public JPanel getPanelTablaFichadas() {
		if (panelTablaFichadas == null) {
			panelTablaFichadas = new JPanel(new GridBagLayout());

			JPanel panelBotones = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5));
			panelBotones.add(getBtnModificar());

			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 5, 5);
			constraints.gridheight = 2;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weighty = 1;
			constraints.weightx = 1;
			constraints.gridy = 0;
			constraints.gridx = 0;
			panelTablaFichadas.add(getJsp(), constraints);

			constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 5, 0, 5);
			constraints.anchor = GridBagConstraints.NORTH;
			constraints.gridy = 0;
			constraints.gridx = 1;
			panelTablaFichadas.add(panelBotones, constraints);
		}
		return panelTablaFichadas;
	}

	public PanelTablaHorarios getPanelTablaHorarios() {
		if (panelTablaHorarios == null) {
			panelTablaHorarios = new PanelTablaHorarios();
			panelTablaHorarios.setPreferredSize(new Dimension(330, 100));
		}
		return panelTablaHorarios;
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

	public CLJTextField getTxtNroLegajo() {
		if (txtNroLegajo == null) {
			txtNroLegajo = new CLJTextField();
			txtNroLegajo.setPreferredSize(new Dimension(40, 20));
			txtNroLegajo.setEditable(false);
			txtNroLegajo.setHorizontalAlignment(CLJTextField.CENTER);
		}
		return txtNroLegajo;
	}

	public CLJTextField getTxtNombreYApellido() {
		if (txtNombreYApellido == null) {
			txtNombreYApellido = new CLJTextField();
			txtNombreYApellido.setPreferredSize(new Dimension(200, 20));
			txtNombreYApellido.setEditable(false);
		}
		return txtNombreYApellido;
	}

	public CLJTextField getTxtSindicato() {
		if (txtSindicato == null) {
			txtSindicato = new CLJTextField();
			txtSindicato.setPreferredSize(new Dimension(160, 20));
			txtSindicato.setEditable(false);
		}
		return txtSindicato;
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	public JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new ThreadLoading().start();
				}
			});
		}
		return btnBuscar;
	}

	private class ThreadLoading extends Thread {

		@Override
		public void run() {
			Date fechaDesde = getPanelFechaDesde().getDate();
			Date fechaHasta = getPanelFechaHasta().getDate();
			if (fechaDesde != null && fechaHasta != null) {
				if (fechaDesde.after(fechaHasta)) {
					CLJOptionPane.showErrorMessage(JDialogVerInfoFichadas.this, "La 'fecha desde' debe ser igual o anterior a la 'fecha hasta'", "Error");
					return;
				}
			}
			List<VigenciaEmpleado> historialVigencias = getLegajo().getHistorialVigencias();
			VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size() - 1);
			if (fechaDesde.before(ultima.getFechaAlta())) {
				CLJOptionPane.showErrorMessage(JDialogVerInfoFichadas.this, "La 'fecha desde' debe ser igual o posterior a la fecha de alta del empleado: " + DateUtil.dateToString(ultima.getFechaAlta()), "Error");
				return;
			}
			java.sql.Date fechaDesdeSql = fechaDesde != null ? new java.sql.Date(fechaDesde.getTime()) : null;
			java.sql.Date fechaHastaSql = fechaHasta != null ? new java.sql.Date(fechaHasta.getTime()) : null;
			
			fechaDesdeSql = DateUtil.redondearFecha(fechaDesdeSql);
			fechaDesdeSql = new java.sql.Date(fechaDesdeSql.getTime()+DateUtil.ONE_HOUR);
			
			fechaHastaSql = DateUtil.redondearFecha(fechaHastaSql);
			fechaHastaSql = new java.sql.Date(fechaHastaSql.getTime()+DateUtil.ONE_HOUR);

			habilitarControles(false);
			getBtnModificar().setEnabled(false);
			resetContadores();
			getTablaFichadas().removeAllRows();
			try {
				List<FichadaLegajoTO> fichadas = getFichadasFacade().getAllByLegajoYFecha(getLegajo(), fechaDesdeSql, fechaHastaSql);
				if (fichadas != null && !fichadas.isEmpty()) {
					armarNuevaTabla(fichadas);
					llenarTabla(fichadas, fechaDesdeSql, fechaHastaSql);
				}
			} catch (Exception e) {
				e.printStackTrace();
				BossError.gestionarError(new CLException(e.getMessage()));
			} finally {
				habilitarControles(true);
			}
		}

		private void habilitarControles(boolean estado) {
			getLblLoading().setVisible(!estado);
			getBtnBuscar().setEnabled(estado);
			getBtnAceptar().setEnabled(estado);
		}

		private void armarNuevaTabla(List<FichadaLegajoTO> fichadas) {
			int maximaCantidadDeGrupos = getMaximaCantidadDeGrupos(fichadas);
			int columnasParaGrupos = 2 * maximaCantidadDeGrupos;
			CANT_COLS = 8 + columnasParaGrupos; // 8 es la cantidad de columnas fijas + 2 por cada entrada-salida

			tablaFichadas = new CLJTable(0, CANT_COLS);

			COL_COLOR = 0;
			COL_FECHA = 1;
			int indice = 1 + columnasParaGrupos;
			int indice2 = 1 + columnasParaGrupos;
			COL_HORAS_NORMALES = ++indice;
			COL_HORAS_EXTRA_AL_50 = ++indice;
			COL_HORAS_EXTRA_AL_100 = ++indice;
			COL_FALTA_JUSTIFICADA = ++indice;
			COL_CAUSA_FALTA = ++indice;
			COL_OBJ = ++indice;
			tablaFichadas.setStringColumn(COL_COLOR, "Ref.", 30, 30, true);
			tablaFichadas.setStringColumn(COL_FECHA, "Fecha", 130, 130, true);
			for (int i = 2; i <= indice2; i++) {
				tablaFichadas.setTimeColumn(i, i % 2 == 0 ? "Entrada" : "Salida", 70, true);
			}
			tablaFichadas.setFloatColumn(COL_HORAS_NORMALES, "Horas normales", 90, true);
			tablaFichadas.setFloatColumn(COL_HORAS_EXTRA_AL_50, "Horas extra al 50%", 105, true);
			tablaFichadas.setFloatColumn(COL_HORAS_EXTRA_AL_100, "Horas extra al 100%", 110, true);
			tablaFichadas.setCheckColumn(COL_FALTA_JUSTIFICADA, "Justificada", 80, true);
			tablaFichadas.setStringColumn(COL_CAUSA_FALTA, "Causa", 200, 200, true);
			tablaFichadas.setStringColumn(COL_OBJ, "", 0);
			inicioColumnasDinamicas = 2;
			tablaFichadas.getColumnModel().getColumn(COL_COLOR).setCellRenderer(new ImageCellRenderer());
			tablaFichadas.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if (getTablaFichadas().getSelectedRow() > -1) {
						FichadaLegajoTO fichada = (FichadaLegajoTO) getTablaFichadas().getValueAt(getTablaFichadas().getSelectedRow(), COL_OBJ);
						if (fichada.getEstadoDiaFichada() == EEstadoDiaFichada.INCONSISTENTE) {
							getBtnModificar().setEnabled(true);
						} else {
							getBtnModificar().setEnabled(false);
						}
					} else {
						getBtnModificar().setEnabled(false);
					}
				}
			});
		}
	}

	private int getMaximaCantidadDeGrupos(List<FichadaLegajoTO> fichadas) {
		int max = 0;
		for (FichadaLegajoTO f : fichadas) {
			if (f.getGruposEntradaSalida().size() > max) {
				max = f.getGruposEntradaSalida().size();
			}
		}
		return max;
	}

	private class ImageCellRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = -6857889981026983340L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (value != null) {
				setText("      ");
				setBackground((Color) value);
				setOpaque(true);
				return this;
			} else {
				setIcon(ImageUtil.loadIcon("ar/com/textillevel/imagenes/img_error.png"));
				setHorizontalAlignment(JLabel.CENTER);
				return this;
			}
		}
	}

	private void llenarTabla(List<FichadaLegajoTO> fichadas, java.sql.Date fechaDesdeSql, java.sql.Date fechaHastaSql) {
		for (FichadaLegajoTO f : fichadas) {
			getTablaFichadas().addRow(crearRow(f));
			PanelReferenciaYCantidad panelReferenciaYCantidad = getPaneles().get(f.getEstadoDiaFichada());
			panelReferenciaYCantidad.setCantidad(panelReferenciaYCantidad.getCantidad() + 1);
		}
		getTablaFichadas().invalidate();
		getTablaFichadas().repaint();
		getTablaFichadas().updateUI();
		getJsp().setViewportView(getTablaFichadas());
		getJsp().invalidate();
		getJsp().repaint();
		getJsp().updateUI();
		actualizarTextosContadores();
	}

	private Object[] crearRow(FichadaLegajoTO elemento) {
		Object[] row = new Object[CANT_COLS];
		row[COL_FECHA] = elemento.getStrDia();
		row[COL_COLOR] = elemento.getEstadoDiaFichada().getColor();
		int copiaInicioColumnas = inicioColumnasDinamicas;
		for (GrupoHoraEntradaSalidaTO g : elemento.getGruposEntradaSalida()) {
			row[copiaInicioColumnas++] = g.getHoraEntrada();
			row[copiaInicioColumnas++] = g.getHoraSalida();
		}
		row[COL_HORAS_NORMALES] = elemento.getHorasNormales();
		row[COL_HORAS_EXTRA_AL_50] = elemento.getHorasExtrasAl50();
		row[COL_HORAS_EXTRA_AL_100] = elemento.getHorasExtrasAl100();
		row[COL_FALTA_JUSTIFICADA] = elemento.getJustificada();
		row[COL_CAUSA_FALTA] = elemento.getCausa();
		row[COL_OBJ] = elemento;
		return row;
	}

	private void resetContadores() {
		for (EEstadoDiaFichada e : getPaneles().keySet()) {
			getPaneles().get(e).resetContador();
		}
	}

	private void actualizarTextosContadores() {
		for (EEstadoDiaFichada e : getPaneles().keySet()) {
			getPaneles().get(e).refreshTextBox();
		}
	}

	public JLabel getLblLoading() {
		if (lblLoading == null) {
			lblLoading = new JLabel(ImageUtil.loadIcon("ar/com/textillevel/imagenes/loading_rojo.gif"));
			lblLoading.setVisible(false);
		}
		return lblLoading;
	}

	public FichadaLegajoFacadeRemote getFichadasFacade() {
		if (fichadasFacade == null) {
			fichadasFacade = GTLPersonalBeanFactory.getInstance().getBean2(FichadaLegajoFacadeRemote.class);
		}
		return fichadasFacade;
	}

	public Map<EEstadoDiaFichada, PanelReferenciaYCantidad> getPaneles() {
		if (paneles == null) {
			paneles = new LinkedHashMap<EEstadoDiaFichada, JDialogVerInfoFichadas.PanelReferenciaYCantidad>();
			SortedMap<String, EEstadoDiaFichada> map = new TreeMap<String, EEstadoDiaFichada>();
			for (EEstadoDiaFichada l : EEstadoDiaFichada.values()) {
			    map.put(l.getDescripcion(), l);
			}
			for (EEstadoDiaFichada estado : map.values()) {
				paneles.put(estado, new PanelReferenciaYCantidad(estado));
			}
		}
		return paneles;
	}

	public JButton getBtnModificar() {
		if (btnModificar == null) {
			btnModificar = BossEstilos.createButton(ICONO_BOTON_MODIF, ICONO_BOTON_MODIF_DES);
			btnModificar.setToolTipText("Modificar fila");
			btnModificar.setEnabled(false);
			btnModificar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					FichadaLegajoTO fichada = (FichadaLegajoTO) getTablaFichadas().getValueAt(getTablaFichadas().getSelectedRow(), COL_OBJ);
					JDialogEditarFichadas dialog = new JDialogEditarFichadas(JDialogVerInfoFichadas.this, fichada, getLegajo().getEmpleado().toString());
					dialog.setVisible(true);
					if (dialog.isAcepto()) {
						getBtnBuscar().doClick();
					}
				}
			});
			btnModificar.setEnabled(false);
		}
		return btnModificar;
	}

	public CLJTable getTablaFichadas() {
		if (tablaFichadas == null) {
			tablaFichadas = new CLJTable(0, 0);
			tablaFichadas.setAllowHidingColumns(false);
			tablaFichadas.setAllowSorting(false);
			tablaFichadas.setReorderingAllowed(false);
			tablaFichadas.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if (getTablaFichadas().getSelectedRow() > -1) {
						FichadaLegajoTO fichada = (FichadaLegajoTO) getTablaFichadas().getValueAt(getTablaFichadas().getSelectedRow(), COL_OBJ);
						if (fichada.getEstadoDiaFichada() == EEstadoDiaFichada.INCONSISTENTE) {
							getBtnModificar().setEnabled(true);
						} else {
							getBtnModificar().setEnabled(false);
						}
					} else {
						getBtnModificar().setEnabled(false);
					}
				}
			});
		}
		return tablaFichadas;
	}

	public JScrollPane getJsp() {
		if (jsp == null) {
			jsp = new JScrollPane(getTablaFichadas(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setSize(new Dimension(300, 300));
			jsp.setPreferredSize(new Dimension(300, 300));
		}
		return jsp;
	}
}
