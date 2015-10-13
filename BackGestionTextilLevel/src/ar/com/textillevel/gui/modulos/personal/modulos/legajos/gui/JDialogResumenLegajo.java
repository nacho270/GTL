package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import main.GTLGlobalCache;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.boss.BossEstilos;
import ar.com.fwcommon.componentes.FWCheckBoxListDialog;
import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.wfsanciones.WorkflowEstadoSancionVisitor;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.wfvaleatencion.WorkflowEstadoValeAtencionVisitor;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.valeanticipo.ImpresionValeAnticipoHandler;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.dialogs.JDialogPasswordInput;
import ar.com.textillevel.modulos.personal.entidades.legajos.AccionHistorica;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.Domicilio;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.AccionSancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Apercibimiento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.CartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.EEStadoCartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.ETipoCartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.ETipoSancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf.EstadoSancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.wf.SancionEstadoFactory;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.AccionValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.EEStadoValeEnfermedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ETipoVale;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAccidente;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeEnfermedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValePreocupacional;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf.EstadoValeAtencion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.wf.ValeAtencionEstadoFactory;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.PeriodoVacaciones;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SancionFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.VacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAnticipoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAtencionFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;
import ar.com.textillevel.modulos.personal.utils.VacacionesHelper;
import ar.com.textillevel.util.GTLBeanFactory;

public class JDialogResumenLegajo extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final String ICONO_CAMBIO_ESTADO = "ar/com/textillevel/imagenes/b_cambio_estado.png";
	private static final String ICONO_CAMBIO_ESTADO_DES = "ar/com/textillevel/imagenes/b_cambio_estado_des.png";

	private JTabbedPane panelDetalles;
	private JPanel panelBotones;
	private JPanel panelTabResumen;
	private PanTablaSancion panTablaSancion;
	private PanTablaValeAtencion panTablaValeAtencion;
	private PanTablaValeAnticipo panTablaValeAnticipo;
	private PanelResumenVacaciones panTablaVacaciones;
	
	private JButton btnCerrar;

	private JTextField txtNombreEmpleado;
	private JTextField txtNroDeLegajo;
	private JTextField txtDireccion;
	private JTextField txtSanciones;
	private JTextField txtTelefono;
	private JTextField txtDocumento;
	private JLabel lblDocumento;
	
	private FWJNumericTextField txtSumaDiasRemanentes;
	
	private JLabel lblFotoEmpleado;
	private JComboBox cmbTipoSancion;

	private final Empleado empleado;

	private SancionFacadeRemote sancionFacade;
	private ValeAtencionFacadeRemote valeAtencionFacade;
	private ValeAnticipoFacadeRemote valeAnticipoFacade;
	private ConfiguracionVacacionesFacadeRemote configuracionFacade;
	
	private final Frame owner;

	private static final int INDICE_VALOR_SANCION_TODOS_EN_COMBO = 0;

	public JDialogResumenLegajo(Frame owner, Empleado empleado) {
		super(owner);
		this.owner = owner;
		this.empleado = empleado;
		construct();
		setDatos();
	}

	private void setDatos() {
		LegajoEmpleado legajo = empleado.getLegajo();
		getTxtNombreEmpleado().setText(empleado.getNombre() + " " + empleado.getApellido());
		getTxtNroLegajo().setText(legajo == null ? null : legajo.getNroLegajo().toString());
		List<Sancion> sanciones = null;
		if(legajo != null) {
			sanciones = getSancionFacade().getSanciones(legajo);
			getPanTablaSancion().agregarElementos(sanciones);
			getPanTablaValeAtencion().agregarElementos(getValeAtencionFacade().getValesAtencion(legajo));
			getPanTablaValeAnticipo().agregarElementos(getValeAnticipoFacade().getValesEnEstado(legajo.getId(), EEstadoValeAnticipo.A_DESCONTAR));
		}
		Domicilio ultimoDomicilio = empleado.getUltimoDomicilio();
		if(ultimoDomicilio != null) {
			getTxtDireccion().setText(ultimoDomicilio.toString());
			getTxtTelefono().setText(ultimoDomicilio.getTelefono());
		}
		ImageIcon foto = empleado.getFoto();
		if(foto != null) {
			getLblFotoEmpleado().setIcon(foto);
		}
		if(empleado.getDocumentacion().getNroDocumento() != null) {
			getTxtDocumento().setText(empleado.getDocumentacion().getNroDocumento().toString());
		} else if(empleado.getDocumentacion().getNroCedula() != null) {
			getLblDocumento().setText("CI: ");
			getTxtDocumento().setText(empleado.getDocumentacion().getNroCedula().toString());
		}
		
		if(sanciones != null) {
			getTxtSanciones().setText(String.valueOf(sanciones.size()));
		}
		
		refreshTableVacaciones();
	}

	private void refreshTableVacaciones() {
		getPanTablaVacaciones().limpiar();
		LegajoEmpleado legajo = empleado.getLegajo();
		if(legajo!=null){
			Integer sumaDiasRemanentes = 0;
			for(RegistroVacacionesLegajo reg : legajo.getHistorialVacaciones()){
				getPanTablaVacaciones().agregarElemento(reg);
			}
			
			Integer diasCorrespondientes = 0;
			
			VigenciaEmpleado vigenciaEmpleado = legajo.getHistorialVigencias().get(legajo.getHistorialVigencias().size()-1);
			Date fechaAlta = vigenciaEmpleado.getFechaAlta();
			ConfiguracionVacaciones confFecha = getConfiguracionFacade().getConfiguracionVacaciones(new java.sql.Date(fechaAlta.getTime()));
			if(confFecha!=null){
				GregorianCalendar gc = DateUtil.getGregorianCalendar();
				gc.set(GregorianCalendar.DAY_OF_MONTH, DateUtil.getDia(fechaAlta));
				gc.set(GregorianCalendar.MONTH, DateUtil.getMes(fechaAlta));
				int anioIngreso = DateUtil.getAnio(fechaAlta);
				gc.set(GregorianCalendar.YEAR, anioIngreso);
				
				while(gc.get(GregorianCalendar.YEAR) <= DateUtil.getAnio(DateUtil.getHoy())){
					java.sql.Date fechaSQL = new java.sql.Date(gc.getTime().getTime());
					Integer mesesMinimos = confFecha.getMesesMinimosParaEntrar();
					int difMeses = Math.abs(DateUtil.getDaysBetween(fechaAlta, DateUtil.stringToDate("31/12/"+DateUtil.getAnio(fechaSQL)))/30);
					if(difMeses>=mesesMinimos){
						PeriodoVacaciones periodoCorrespondienteParaElAnio = VacacionesHelper.getPeriodoCorrespondiente(confFecha, fechaAlta, fechaSQL);
						if(periodoCorrespondienteParaElAnio!=null){
							diasCorrespondientes += periodoCorrespondienteParaElAnio.getCantidadDias();
						}
					}
					anioIngreso++;
					gc.set(GregorianCalendar.YEAR, anioIngreso);
				}
				
				Map<Integer, List<RegistroVacacionesLegajo>> vacacionesPorAnio = VacacionesHelper.agruparPorAnio(legajo.getHistorialVacaciones());
				for(Integer anio : vacacionesPorAnio.keySet()){
					List<RegistroVacacionesLegajo> registros = vacacionesPorAnio.get(anio);
					Collections.sort(registros, new Comparator<RegistroVacacionesLegajo>() {
						public int compare(RegistroVacacionesLegajo o1, RegistroVacacionesLegajo o2) {
							return o1.getFechaDesde().compareTo(o2.getFechaDesde())*-1;
						}
					});
					sumaDiasRemanentes+=registros.get(0).getDiasConfiguracion() - registros.get(0).getDiasRemanentes();
				}
				getTxtSumaDiasRemanentes().setValue(diasCorrespondientes.longValue() - sumaDiasRemanentes.longValue());
			}else{
				FWJOptionPane.showWarningMessage(this, "Atención: No se ha cargado la configuración de vacaciones", "Advertencia");
			}
		}
	}

	private void construct() {
		setUpComponentes();
		setUpScreen();
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				cerrar();
			}
		});
		this.add(getPanelDetalles(), BorderLayout.CENTER);
		this.add(getPanelBotones(), BorderLayout.SOUTH);
	}

	private void cerrar() {
		dispose();
	}

	private void setUpScreen() {
		this.setTitle("Resumen de Legajo");
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setSize(new Dimension(600, 500));
		this.setResizable(false);
		GuiUtil.centrar(this);
		this.setModal(true);
	}
	
	private JTabbedPane getPanelDetalles() {
		if (panelDetalles == null) {
			panelDetalles = new JTabbedPane();
			panelDetalles.addTab("Resumen", getPanelTabResumen());
			panelDetalles.addTab("Sanciones", getPanValeAtencion());
			panelDetalles.addTab("Vales de Atención", getPanTablaValeAtencion());
			panelDetalles.addTab("Vales de Anticipo", getPanTablaValeAnticipo());
			panelDetalles.addTab("Historial vacaciones",getPanelTabVacaciones());
		}
		return panelDetalles;
	}

	private JPanel getPanelTabResumen() {
		if(panelTabResumen == null){
			panelTabResumen = new JPanel();
			panelTabResumen.setLayout(new GridBagLayout());
			panelTabResumen.add(new JLabel("Legajo: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabResumen.add(getTxtNroLegajo(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabResumen.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panelTabResumen.add(getTxtNombreEmpleado(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabResumen.add(new JLabel("Dirección: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabResumen.add(getTxtDireccion(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0.5, 0));
			panelTabResumen.add(new JLabel("Teléfono: "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabResumen.add(getTxtTelefono(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabResumen.add(new JLabel("Sanciones: "), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabResumen.add(getTxtSanciones(), GenericUtils.createGridBagConstraints(1, 2, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabResumen.add(getLblDocumento(), GenericUtils.createGridBagConstraints(0, 3, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panelTabResumen.add(getTxtDocumento(), GenericUtils.createGridBagConstraints(1, 3, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panelTabResumen.add(getLblFotoEmpleado(), GenericUtils.createGridBagConstraints(0, 4, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 4, 1, 0, 0));
		}
		return panelTabResumen;
	}

	private PanTablaSancion getPanTablaSancion() {
		if(panTablaSancion == null) {
			panTablaSancion = new PanTablaSancion();
		}
		return panTablaSancion;
	}

	private JPanel getPanValeAtencion() {
		JPanel panValeAtencion = new JPanel();
		panValeAtencion.setLayout(new GridBagLayout());
		panValeAtencion.add(new JLabel("Tipo de Sanción: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panValeAtencion.add(getCmbTipoSancion(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
		panValeAtencion.add(getPanTablaSancion(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 1));
		return panValeAtencion;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private JComboBox getCmbTipoSancion() {
		if(cmbTipoSancion == null) {
			cmbTipoSancion = new JComboBox();
			List optionList = new ArrayList();
			optionList.add("TODOS");
			optionList.add(ETipoSancion.APERCIBIMIENTO);
			optionList.add(ETipoSancion.CARTA_DOCUMENTO);
			GuiUtil.llenarCombo(cmbTipoSancion, optionList, true);
			cmbTipoSancion.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						getPanTablaSancion().limpiar();
						int selectedIndex = getCmbTipoSancion().getSelectedIndex();
						ETipoSancion tipoSancionSelected = selectedIndex == INDICE_VALOR_SANCION_TODOS_EN_COMBO ? null : (ETipoSancion)getCmbTipoSancion().getSelectedItem();
						List<Sancion> sanciones = getSancionFacade().getSanciones(empleado.getLegajo());
						for(Sancion s : sanciones) {
							if(tipoSancionSelected == null || s.getTipoSancion() == tipoSancionSelected) {
								getPanTablaSancion().agregarElemento(s);
							}
						}
					}
				}

			});
		}
		return cmbTipoSancion;
	}

	private PanTablaValeAtencion getPanTablaValeAtencion() {
		if(panTablaValeAtencion == null) {
			panTablaValeAtencion = new PanTablaValeAtencion();
		}
		return panTablaValeAtencion;
	}
	
	private PanTablaValeAnticipo getPanTablaValeAnticipo() {
		if(panTablaValeAnticipo == null) {
			panTablaValeAnticipo = new PanTablaValeAnticipo();
		}
		return panTablaValeAnticipo;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
			panelBotones.add(getBtnCerrar());
		}
		return panelBotones;
	}

	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Cerrar");
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					cerrar();
				}

			});
		}
		return btnCerrar;
	}

	private JTextField getTxtNombreEmpleado() {
		if(txtNombreEmpleado == null) {
			txtNombreEmpleado = new JTextField();
			txtNombreEmpleado.setEditable(false);
		}
		return txtNombreEmpleado; 
	}

	private JTextField getTxtDireccion() {
		if(txtDireccion == null) {
			txtDireccion = new JTextField();
			txtDireccion.setEditable(false);
		}
		return txtDireccion;
	}

	private JTextField getTxtSanciones() {
		if(txtSanciones == null) {
			txtSanciones = new JTextField();
			txtSanciones.setEditable(false);
		}
		return txtSanciones;
	}

	
	private JTextField getTxtTelefono() {
		if(txtTelefono == null) {
			txtTelefono = new JTextField();
			txtTelefono.setEditable(false);
		}
		return txtTelefono;
	}
	
	private JTextField getTxtDocumento() {
		if(txtDocumento == null) {
			txtDocumento = new JTextField();
			txtDocumento.setEditable(false);
		}
		return txtDocumento;
	}

	private JLabel getLblDocumento() {
		if(lblDocumento == null) {
			lblDocumento = new JLabel("DNI: ");
		}
		return lblDocumento;
	}

	private JLabel getLblFotoEmpleado() {
		if (lblFotoEmpleado == null) {
			lblFotoEmpleado = new JLabel();
			lblFotoEmpleado.setBorder(BorderFactory.createLineBorder(Color.RED.darker().darker()));
		}
		return lblFotoEmpleado;
	}

	private JTextField getTxtNroLegajo() {
		if(txtNroDeLegajo == null) {
			txtNroDeLegajo = new JTextField();
			txtNroDeLegajo.setEditable(false);
		}
		return txtNroDeLegajo; 
	}

	private SancionFacadeRemote getSancionFacade() {
		if(sancionFacade == null) {
			sancionFacade = GTLPersonalBeanFactory.getInstance().getBean2(SancionFacadeRemote.class);
		}
		return sancionFacade;
	}

	private ValeAtencionFacadeRemote getValeAtencionFacade() {
		if(valeAtencionFacade == null) {
			valeAtencionFacade = GTLPersonalBeanFactory.getInstance().getBean2(ValeAtencionFacadeRemote.class);
		}
		return valeAtencionFacade;
	}

	private ValeAnticipoFacadeRemote getValeAnticipoFacade() {
		if(valeAnticipoFacade == null) {
			valeAnticipoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ValeAnticipoFacadeRemote.class);
		}
		return valeAnticipoFacade;
	}
	
	private class PanTablaSancion extends PanelTabla<Sancion> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 4;
		private static final int COL_FECHA = 0;
		private static final int COL_TIPO_SANCION = 1;
		private static final int COL_DETALLE_SANCION = 2;
		private static final int COL_OBJ = 3;

		private JButton btnCambioEstado;

		public PanTablaSancion() {
			agregarBoton(getBtnCambioEstado());
		}

		private void refreshAndReselectItem() {
			int rowSelected = getTabla().getSelectedRow();
			getTabla().removeAllRows();
			agregarElementos(getSancionFacade().getSanciones(empleado.getLegajo()));
			getTabla().setRowSelectionInterval(rowSelected, rowSelected);
		}

		private JButton getBtnCambioEstado() {
			if(btnCambioEstado == null) {
				btnCambioEstado = BossEstilos.createButton(ICONO_CAMBIO_ESTADO, ICONO_CAMBIO_ESTADO_DES);
				btnCambioEstado.setEnabled(false);

				btnCambioEstado.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						Sancion elemento = getElemento(getTabla().getSelectedRow());
						CartaDocumento cd = (CartaDocumento)elemento; //No está habilitada para apercibimientos
						EstadoSancion estadoSancion = SancionEstadoFactory.getInstance().getEstadoSancion(cd.getEstadoCD());
						if(estadoSancion.getEstadosSiguientes().size() == 1) {
							if(FWJOptionPane.showQuestionMessage(JDialogResumenLegajo.this, StringW.wordWrap("¿Está seguro que desea realizar la acción?"), "Confirmación") == FWJOptionPane.YES_OPTION) {
								EstadoSancion nextEstadoSancion = estadoSancion.getEstadosSiguientes().iterator().next();
								WorkflowEstadoSancionVisitor visitor = new WorkflowEstadoSancionVisitor(owner, cd, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName()); 
								nextEstadoSancion.accept(visitor);
								refreshAndReselectItem();
							}
						} else {
							String[] correcs = new String[estadoSancion.getEstadosSiguientes().size()];
							int i = 0;
							for(EstadoSancion es : estadoSancion.getEstadosSiguientes()){
								correcs[i] = es.getEnumEstado().getDescrAccion();
								i++;
							}
							Object opcion = JOptionPane.showInputDialog(JDialogResumenLegajo.this, "Seleccione la operación a realizar:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, correcs,correcs[0]);
							if(opcion != null) {
								if(FWJOptionPane.showQuestionMessage(JDialogResumenLegajo.this, StringW.wordWrap("¿Está seguro que desea realizar la acción?"), "Confirmación") == FWJOptionPane.YES_OPTION) {
									EEStadoCartaDocumento enumEstadoCD = EEStadoCartaDocumento.getByDescripcion((String)opcion);
									EstadoSancion nextEstadoSancion = SancionEstadoFactory.getInstance().getEstadoSancion(enumEstadoCD);
									WorkflowEstadoSancionVisitor visitor = new WorkflowEstadoSancionVisitor(owner, cd, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName()); 
									nextEstadoSancion.accept(visitor);
									refreshAndReselectItem();
								}
							}
						}
					}

				});
			}
			return btnCambioEstado;
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setDateColumn(COL_FECHA, "FECHA");
			tabla.setStringColumn(COL_TIPO_SANCION, "SANCION", 100, 100, true);
			tabla.setMultilineColumn(COL_DETALLE_SANCION, "DETALLE", 330, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setReorderingAllowed(false);

			tabla.addMouseListener(new MouseAdapter() {

				@Override
				@SuppressWarnings({ "rawtypes" })
				public void mouseClicked(MouseEvent e) {
					int sr = getTabla().getSelectedRow();
					if(e.getClickCount() == 2 && sr != -1) {
						Sancion elemento = getElemento(sr);
						List<AccionSancion> historialSanciones = getSancionFacade().getHistoria(elemento);
						List<AccionHistorica> historial = new ArrayList<AccionHistorica>();
						for(AccionSancion as : historialSanciones) {
							historial.add(as);
						}
						JDialogVerHistorialAcciones dialogoHistorial = new JDialogVerHistorialAcciones(owner, empleado, historial);
						dialogoHistorial.addAccionHistoricaEventListener(new AccionHistoricaEventListener() {
							public void accionHistoricaPersisted(AccionHistoricaEvent evt) {
								getSancionFacade().updateAccion(evt.getAh());
							}
						});
						dialogoHistorial.setVisible(true);
					}
				}

			});
			
			return tabla;
		}

		@Override
		protected void filaTablaSeleccionada() {
			if(getTabla().getSelectedRow() == -1) {
				getBtnCambioEstado().setEnabled(false);
				return;
			}
			Sancion elemento = getElemento(getTabla().getSelectedRow());
			if(elemento instanceof Apercibimiento) {
				getBtnCambioEstado().setEnabled(false);
			} else {
				CartaDocumento cd = (CartaDocumento) elemento;
				if(cd.getTipoCD() ==ETipoCartaDocumento.AVISO_JUSTIF_FALTA) {
					getBtnCambioEstado().setEnabled(!SancionEstadoFactory.getInstance().getEstadoSancion(cd.getEstadoCD()).getEstadosSiguientes().isEmpty());
				} else {
					List<EEStadoCartaDocumento> estados = new ArrayList<EEStadoCartaDocumento>();
					estados.add(EEStadoCartaDocumento.JUSTIFICADA);
					estados.add(EEStadoCartaDocumento.RECIBIDA);
					getBtnCambioEstado().setEnabled(!estados.contains(cd.getEstadoCD()));
				}
			}
		}

		@Override
		protected void agregarElemento(Sancion elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA] = elemento.getFechaSancion();
			row[COL_TIPO_SANCION] = elemento.getTipoSancion().toString();
			row[COL_DETALLE_SANCION] = elemento.getResumen();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected Sancion getElemento(int fila) {
			return (Sancion)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean validarAgregar() {
			String[] tipoSanciones= new String[ETipoSancion.values().length];
			for(int i = 0 ; i<ETipoSancion.values().length;i++){
				tipoSanciones[i] = ETipoSancion.values()[i].getDescripcion();
			}
			Object tipoSancion = JOptionPane.showInputDialog(JDialogResumenLegajo.this, "Seleccione el tipo de sanción:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, tipoSanciones,tipoSanciones[0]);
			if(tipoSancion!=null){
				ETipoSancion tipoCorrecion = ETipoSancion.getByDescripcion((String)tipoSancion);
				if(tipoCorrecion == ETipoSancion.APERCIBIMIENTO) {
					Apercibimiento a = new Apercibimiento();
					a.setLegajo(empleado.getLegajo());
					JDialogApercibimiento dialogo = new JDialogApercibimiento(owner, a); 
					dialogo.setVisible(true);
					if(dialogo.isAcepto()) {
						agregarElemento(dialogo.getApercibimiento());
					}
				} else {
					String[] tiposCD= new String[ETipoCartaDocumento.values().length];
					for(int i = 0 ; i<ETipoCartaDocumento.values().length;i++){
						tiposCD[i] = ETipoCartaDocumento.values()[i].getDescripcion();
					}
					Object opcionCD = JOptionPane.showInputDialog(JDialogResumenLegajo.this, "Seleccione el tipo de carta documento:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, tiposCD,tiposCD[0]);
					if(opcionCD != null) {
						ETipoCartaDocumento tipoCD = ETipoCartaDocumento.getByDescripcion((String)opcionCD);
						CartaDocumento cd = new CartaDocumento();
						cd.setLegajo(empleado.getLegajo());
						cd.setTipoCD(tipoCD);
						List<Sancion> sancionesNoAsociadas = getSancionFacade().getSancionesNoAsociadas(empleado.getLegajo(), cd.getTipoCD());
						if(!sancionesNoAsociadas.isEmpty()) {
							FWCheckBoxListDialog lista = new FWCheckBoxListDialog(owner);
							lista.setValores(sancionesNoAsociadas, true);
							lista.setVisible(true);
							cd.getSancionesAsociadas().addAll(lista.getValoresSeleccionados());
						}
						JDialogCartaDocumento dialogo = new JDialogCartaDocumento(owner, cd);
						dialogo.setVisible(true);
						if(dialogo.isAcepto()) {
							agregarElemento(dialogo.getCartaDocumento());
						}
					}
				}
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			if(FWJOptionPane.showQuestionMessage(JDialogResumenLegajo.this, StringW.wordWrap("¿Está seguro que desea eliminar la sanción junto con toda su historia?"), "Confirmación") == FWJOptionPane.YES_OPTION) {
				if(esAdminOrIngresoPasswordDeAdmin()) {
					try {
						getSancionFacade().eliminarSancion(getElemento(getTabla().getSelectedRow()));
						return true;
					} catch (ValidacionException e) {
						FWJOptionPane.showErrorMessage(JDialogResumenLegajo.this, StringW.wordWrap(e.getMensajeError()), "Error");
					}
				}
				
			}
			return false;
		}
		
	}

	private class PanTablaValeAtencion extends PanelTabla<ValeAtencion> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 4;
		private static final int COL_FECHA = 0;
		private static final int COL_TIPO_VALE_ATENCION = 1;
		private static final int COL_DETALLE_VALE_ATENCION = 2;
		private static final int COL_OBJ = 3;

		private JButton btnCambioEstado;

		public PanTablaValeAtencion() {
			agregarBoton(getBtnCambioEstado());
		}

		private JButton getBtnCambioEstado() {
			if(btnCambioEstado == null) {
				btnCambioEstado = BossEstilos.createButton(ICONO_CAMBIO_ESTADO, ICONO_CAMBIO_ESTADO_DES);
				btnCambioEstado.setEnabled(false);

				btnCambioEstado.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						ValeAtencion elemento = getElemento(getTabla().getSelectedRow());
						EstadoValeAtencion estadoValeAtencion = ValeAtencionEstadoFactory.getInstance().getEstadoValeAtencion(elemento.getEstadoValeAtencion());
						if(estadoValeAtencion.getEstadosSiguientes().size() == 1) {
							if(FWJOptionPane.showQuestionMessage(JDialogResumenLegajo.this, StringW.wordWrap("¿Está seguro que desea realizar la acción?"), "Confirmación") == FWJOptionPane.YES_OPTION) {
								EstadoValeAtencion nextEstadoValeAtencion = estadoValeAtencion.getEstadosSiguientes().iterator().next();
								WorkflowEstadoValeAtencionVisitor visitor = new WorkflowEstadoValeAtencionVisitor(owner, elemento, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName()); 
								nextEstadoValeAtencion.accept(visitor);
								refreshAndReselectItem();
							}
						} else {
							String[] correcs = new String[estadoValeAtencion.getEstadosSiguientes().size()];
							int i = 0;
							for(EstadoValeAtencion eva : estadoValeAtencion.getEstadosSiguientes()){
								correcs[i] = eva.getEnumEstado().getDescrAccion();
								i++;
							}
							Object opcion = JOptionPane.showInputDialog(JDialogResumenLegajo.this, "Seleccione la operación a realizar:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, correcs,correcs[0]);
							if(opcion != null) {
								if(FWJOptionPane.showQuestionMessage(JDialogResumenLegajo.this, StringW.wordWrap("¿Está seguro que desea realizar la acción?"), "Confirmación") == FWJOptionPane.YES_OPTION) {
									EEStadoValeEnfermedad enumEstadoVA = EEStadoValeEnfermedad.getByDescripcion((String)opcion);
									EstadoValeAtencion nextEstadoValeAtencion = ValeAtencionEstadoFactory.getInstance().getEstadoValeAtencion(enumEstadoVA);
									WorkflowEstadoValeAtencionVisitor visitor = new WorkflowEstadoValeAtencionVisitor(owner, elemento, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName()); 
									nextEstadoValeAtencion.accept(visitor);
									refreshAndReselectItem();
								}
							}
						}
					}

				});
			}
			return btnCambioEstado;
		}

		private void refreshAndReselectItem() {
			int rowSelected = getTabla().getSelectedRow();
			getTabla().removeAllRows();
			agregarElementos(getValeAtencionFacade().getValesAtencion(empleado.getLegajo()));
			getTabla().setRowSelectionInterval(rowSelected, rowSelected);
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setDateColumn(COL_FECHA, "FECHA");
			tabla.setStringColumn(COL_TIPO_VALE_ATENCION, "VALE", 100, 100, true);
			tabla.setMultilineColumn(COL_DETALLE_VALE_ATENCION, "DETALLE", 350, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setReorderingAllowed(false);
			
			tabla.addMouseListener(new MouseAdapter() {

				@Override
				@SuppressWarnings({ "rawtypes" })
				public void mouseClicked(MouseEvent e) {
					int sr = getTabla().getSelectedRow();
					if(e.getClickCount() == 2 && sr != -1) {
						ValeAtencion elemento = getElemento(sr);
						List<AccionValeAtencion> historialSanciones = getValeAtencionFacade().getHistoria(elemento);
						List<AccionHistorica> historial = new ArrayList<AccionHistorica>();
						for(AccionValeAtencion as : historialSanciones) {
							historial.add(as);
						}
						JDialogVerHistorialAcciones dialogoHistorial = new JDialogVerHistorialAcciones(owner, empleado, historial);
						dialogoHistorial.addAccionHistoricaEventListener(new AccionHistoricaEventListener() {
							public void accionHistoricaPersisted(AccionHistoricaEvent evt) {
								getValeAtencionFacade().updateAccion(evt.getAh());
							}
						});
						dialogoHistorial.setVisible(true);
					}
				}

			});

			return tabla;
		}

		@Override
		protected void agregarElemento(ValeAtencion elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA] = elemento.getFechaVale();
			row[COL_TIPO_VALE_ATENCION] = elemento.getTipoVale().toString();
			row[COL_DETALLE_VALE_ATENCION] = elemento.getResumen();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected ValeAtencion getElemento(int fila) {
			return (ValeAtencion)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void filaTablaSeleccionada() {
			if(getTabla().getSelectedRow() == -1) {
				getBtnCambioEstado().setEnabled(false);
				return;
			}
			ValeAtencion elemento = getElemento(getTabla().getSelectedRow());
			if(elemento instanceof ValePreocupacional) {
				getBtnCambioEstado().setEnabled(false);
			} else {
				List<EEStadoValeEnfermedad> estados = new ArrayList<EEStadoValeEnfermedad>();
				estados.add(EEStadoValeEnfermedad.ABIERTO);
				estados.add(EEStadoValeEnfermedad.JUSTIFICADO_Y_CONTROL);
				getBtnCambioEstado().setEnabled(estados.contains(elemento.getEstadoValeAtencion()));
			}
		}

		@Override
		public boolean validarAgregar() {
			String[] correcs= new String[ETipoVale.values().length];
			for(int i = 0 ; i<ETipoVale.values().length;i++){
				correcs[i] = ETipoVale.values()[i].getDescripcion();
			}
			Object opcion = JOptionPane.showInputDialog(JDialogResumenLegajo.this, "Seleccione el tipo de vale:", "Lista de opciones", JOptionPane.INFORMATION_MESSAGE, null, correcs,correcs[0]);
			if(opcion!=null){
				ETipoVale tipoCorrecion = ETipoVale.getByDescripcion((String)opcion);
				if(tipoCorrecion == ETipoVale.ENFERMEDAD) {
					ValeEnfermedad ve = new ValeEnfermedad();
					ve.setLegajo(empleado.getLegajo());
					JDialogValeEnfermedad dialogo = new JDialogValeEnfermedad(owner, empleado.getLegajo(), ve); 
					dialogo.setVisible(true);
					if(dialogo.isAcepto()) {
						agregarElemento(dialogo.getValeEnfermedad());
					}
				}
				if(tipoCorrecion == ETipoVale.ACCIDENTE) {
					ValeAccidente va = new ValeAccidente();
					va.setLegajo(empleado.getLegajo());
					JDialogValeAccidente dialogo = new JDialogValeAccidente(owner, empleado.getLegajo(), va); 
					dialogo.setVisible(true);
					if(dialogo.isAcepto()) {
						agregarElemento(dialogo.getValeAccidente());
					}
				}
				if(tipoCorrecion == ETipoVale.PREOCUPACIONAL) {
					ValePreocupacional cd = new ValePreocupacional();
					cd.setLegajo(empleado.getLegajo());
					JDialogValePreocupacional dialogo = new JDialogValePreocupacional(owner, cd);
					dialogo.setVisible(true);
					if(dialogo.isAcepto()) {
						agregarElemento(dialogo.getValePreocupacional());
					}
				}
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			if(FWJOptionPane.showQuestionMessage(JDialogResumenLegajo.this, StringW.wordWrap("¿Está seguro que desea eliminar el vale de atención junto con toda su historia?"), "Confirmación") == FWJOptionPane.YES_OPTION) {
				if(esAdminOrIngresoPasswordDeAdmin()) {
					try {
						getValeAtencionFacade().eliminarValeAtencion(getElemento(getTabla().getSelectedRow()));
						return true;
					} catch (ValidacionException e) {
						FWJOptionPane.showErrorMessage(JDialogResumenLegajo.this, StringW.wordWrap(e.getMensajeError()), "Error");
					}
				}
			}
			return false;
		}

	}

	private class PanTablaValeAnticipo extends PanelTabla<ValeAnticipo> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 6;
		private static final int COL_NRO = 0;
		private static final int COL_FECHA = 1;
		private static final int COL_ESTADO = 2;
		private static final int COL_MONTO = 3;
		private static final int COL_CONCEPTO = 4;
		private static final int COL_OBJ = 5;

		private JButton btnMarcarComoUtilizado;
		private JButton btnImprimir;
		
		public PanTablaValeAnticipo() {
			getBotonEliminar().setVisible(false);
			agregarBoton(getBtnMarcarComoUtilizado());
			agregarBoton(getBtnImprimir());
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_NRO, "NUMERO", 60, 60, true);
			tabla.setDateColumn(COL_FECHA, "FECHA");
			tabla.setStringColumn(COL_ESTADO, "ESTADO", 80, 80, true);
			tabla.setMultilineColumn(COL_MONTO, "MONTO", 50, true);
			tabla.setStringColumn(COL_CONCEPTO, "CONCEPTO", 250, 250, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			tabla.setReorderingAllowed(false);
			tabla.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if(getTabla().getSelectedRow()>-1){
						ValeAnticipo vale = getElemento(getTabla().getSelectedRow());
						getBtnMarcarComoUtilizado().setEnabled(vale.getEstadoVale() == EEstadoValeAnticipo.A_DESCONTAR);
						getBtnImprimir().setEnabled(true);
					}else {
						getBtnMarcarComoUtilizado().setEnabled(false);
						getBtnImprimir().setEnabled(false);
					}
				}
			});
			return tabla;
		}

		@Override
		protected void agregarElemento(ValeAnticipo elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_NRO] = elemento.getNroVale().toString();
			row[COL_FECHA] = elemento.getFecha();
			row[COL_MONTO] = GenericUtils.getDecimalFormat().format(elemento.getMonto().doubleValue());
			row[COL_CONCEPTO] = elemento.getConcepto();
			row[COL_ESTADO] = elemento.getEstadoVale().getDescripcion();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected ValeAnticipo getElemento(int fila) {
			return (ValeAnticipo)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarValeAnticipo dialog = new JDialogAgregarValeAnticipo(owner, empleado);
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				ValeAnticipo valeAnticipo = dialog.getValeAnticipo();
				valeAnticipo = getValeAnticipoFacade().save(valeAnticipo,empleado);
				FWJOptionPane.showInformationMessage(owner, "El vale se ha creado satisfactoriamente", "Información");
				if(FWJOptionPane.showQuestionMessage(owner, "Desea imprimir el vale?", "Pregunta")==FWJOptionPane.YES_OPTION){
					ImpresionValeAnticipoHandler handler = new ImpresionValeAnticipoHandler(valeAnticipo,owner);
					handler.imprimir();
				}
				agregarElemento(valeAnticipo);
			}
			return false;
		}

		public JButton getBtnMarcarComoUtilizado() {
			if(btnMarcarComoUtilizado == null){
				btnMarcarComoUtilizado = BossEstilos.createButton("ar/com/textillevel/imagenes/b_verificar_stock.png", "ar/com/textillevel/imagenes/b_verificar_stock_des.png");
				btnMarcarComoUtilizado.setEnabled(false);
				btnMarcarComoUtilizado.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ValeAnticipo vale = getElemento(getTabla().getSelectedRow());
						vale.setEstadoValeAnticipo(EEstadoValeAnticipo.DESCONTADO);
						getValeAnticipoFacade().acutalizarVale(vale);
						limpiar();
						LegajoEmpleado legajo = empleado.getLegajo();
						agregarElementos(getValeAnticipoFacade().getValesByLegajo(legajo.getId()));
						btnMarcarComoUtilizado.setEnabled(false);
					}
				});
			}
			return btnMarcarComoUtilizado;
		}

		
		public JButton getBtnImprimir() {
			if(btnImprimir == null){
				btnImprimir = BossEstilos.createButton("ar/com/textillevel/imagenes/b_imprimir_moderno.png","ar/com/textillevel/imagenes/b_imprimir_moderno_des.png");
				btnImprimir.setEnabled(false);
				btnImprimir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ValeAnticipo vale = getElemento(getTabla().getSelectedRow());
						ImpresionValeAnticipoHandler handler = new ImpresionValeAnticipoHandler(vale,JDialogResumenLegajo.this);
						handler.imprimir();
					}
				});
			}
			return btnImprimir;
		}
	}

	private boolean esAdminOrIngresoPasswordDeAdmin() {
		boolean esAdminOrIngresoPasswordDeAdmin = GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin();
		if(!esAdminOrIngresoPasswordDeAdmin) {
			JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(JDialogResumenLegajo.this.owner,"Eliminar");
			if (jDialogPasswordInput.isAcepto()) {
				String pass = new String(jDialogPasswordInput.getPassword());
				UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
				esAdminOrIngresoPasswordDeAdmin = usrAdmin !=null;
			}
		}
		return esAdminOrIngresoPasswordDeAdmin;
	}
	
	private class PanelResumenVacaciones extends PanelTabla<RegistroVacacionesLegajo>{

		private static final long serialVersionUID = 5801174129222649844L;

		private static final int CANT_COLS = 5;
		private static final int COL_FECHA_DESDE = 0;
		private static final int COL_FECHA_HASTA = 1;
		private static final int COL_DIAS_TOMADOS = 2;
		private static final int COL_DIAS_REMANENTES = 3;
		private static final int COL_OBJ = 4;
		
		public PanelResumenVacaciones() {
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setDateColumn(COL_FECHA_DESDE, "Desde", 100, true);
			tabla.setDateColumn(COL_FECHA_HASTA, "Hasta", 100, true);
			tabla.setIntColumn(COL_DIAS_TOMADOS, "Días tomados", 100, true);
			tabla.setIntColumn(COL_DIAS_REMANENTES, "Días sobrantes", 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(RegistroVacacionesLegajo elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA_DESDE] = elemento.getFechaDesde();
			row[COL_FECHA_HASTA] = elemento.getFechaHasta();
			row[COL_DIAS_TOMADOS] = elemento.getTotalDiasTomados();
			row[COL_DIAS_REMANENTES] = elemento.getDiasRemanentes();
			getTabla().addRow(row);
		}

		@Override
		protected RegistroVacacionesLegajo getElemento(int fila) {
			return (RegistroVacacionesLegajo)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			int fila = getTabla().getSelectedRow();
			if(fila >-1){
				ConfiguracionVacaciones conf = getConfiguracionFacade().getConfiguracionVacaciones(DateUtil.getHoy());
				if(conf == null){
					FWJOptionPane.showErrorMessage(JDialogResumenLegajo.this, "No existe una configuración de vacaciones vigente para la fecha actual", "Error");
					return;
				}
				LegajoEmpleado legajo = empleado.getLegajo();
				List<RegistroVacacionesLegajo> historialVacaciones = legajo.getHistorialVacaciones();
				List<VigenciaEmpleado> historialVigencias = legajo.getHistorialVigencias();
				VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size()-1);
				RegistroVacacionesLegajo reg = historialVacaciones.get(fila);
				if(reg.getFechaDesde().after(DateUtil.getHoy())){
					PeriodoVacaciones periodoCorrespondiente = VacacionesHelper.getPeriodoCorrespondiente(conf, ultima.getFechaAlta());
					JDialogAgregarModificarPeriodoVacaciones dialog = new JDialogAgregarModificarPeriodoVacaciones(JDialogResumenLegajo.this, legajo, periodoCorrespondiente, getElemento(fila));
					dialog.setVisible(true);
					if(dialog.isAcepto()){
						historialVacaciones.set(fila, dialog.getRegistroActual());
						Collections.sort(historialVacaciones, new Comparator<RegistroVacacionesLegajo>() {
							public int compare(RegistroVacacionesLegajo o1, RegistroVacacionesLegajo o2) {
								return o1.getFechaDesde().compareTo(o2.getFechaDesde());
							}
						});
						empleado.setLegajo(legajo);
						GTLPersonalBeanFactory.getInstance().getBean2(VacacionesFacadeRemote.class).modificarPeriodoVacaciones(empleado,reg);
						refreshTableVacaciones();
					}
				}else{
					FWJOptionPane.showErrorMessage(JDialogResumenLegajo.this, "No se pueden modificar los registros de vacaciones pasados","Error");
				}
			}
		}
		
		@Override
		public boolean validarQuitar() {
			int fila = getTabla().getSelectedRow();
			if(fila >-1){
				LegajoEmpleado legajo = empleado.getLegajo();
				List<RegistroVacacionesLegajo> historialVacaciones = legajo.getHistorialVacaciones();
				RegistroVacacionesLegajo reg = getElemento(fila);
				if(reg.getFechaDesde().after(DateUtil.getHoy())){
					historialVacaciones.remove(fila);
					Collections.sort(historialVacaciones, new Comparator<RegistroVacacionesLegajo>() {
						public int compare(RegistroVacacionesLegajo o1, RegistroVacacionesLegajo o2) {
							return o1.getFechaDesde().compareTo(o2.getFechaDesde());
						}
					});
					empleado.setLegajo(legajo);
					GTLPersonalBeanFactory.getInstance().getBean2(VacacionesFacadeRemote.class).borrarPeriodoVacaciones(empleado,reg);
					refreshTableVacaciones();
				}else{
					FWJOptionPane.showErrorMessage(JDialogResumenLegajo.this, "No se pueden borrar los registros de vacaciones pasados","Error");
				}
			}
			return false;
		}
		
		@Override
		public boolean validarAgregar() {
			ConfiguracionVacaciones conf = getConfiguracionFacade().getConfiguracionVacaciones(DateUtil.getHoy());
			if(conf == null){
				FWJOptionPane.showErrorMessage(JDialogResumenLegajo.this, "No existe una configuración de vacaciones vigente para la fecha actual", "Error");
				return false;
			}
			LegajoEmpleado legajo = empleado.getLegajo();
			List<VigenciaEmpleado> historialVigencias = legajo.getHistorialVigencias();
			VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size()-1);
			PeriodoVacaciones periodoCorrespondiente = VacacionesHelper.getPeriodoCorrespondiente(conf, ultima.getFechaAlta());
			if(periodoCorrespondiente == null){
				FWJOptionPane.showWarningMessage(JDialogResumenLegajo.this, "El empleado tiene menos de " + conf.getMesesMinimosParaEntrar() + " meses de antigüedad.\nSe calcula 1 día de vacaciones por cada 20 trabajados.", "Advertencia");
				//TODO: CALCULAR 1 DIA POR CADA 20 TRABAJADOS
			}
			List<RegistroVacacionesLegajo> historialVacaciones = legajo.getHistorialVacaciones();
			JDialogAgregarModificarPeriodoVacaciones dialogo = new JDialogAgregarModificarPeriodoVacaciones(JDialogResumenLegajo.this, legajo,periodoCorrespondiente);
			dialogo.setVisible(true);
			if (dialogo.isAcepto()) {
				RegistroVacacionesLegajo registroActual = dialogo.getRegistroActual();
				historialVacaciones.add(registroActual);
				Collections.sort(historialVacaciones, new Comparator<RegistroVacacionesLegajo>() {
					public int compare(RegistroVacacionesLegajo o1, RegistroVacacionesLegajo o2) {
						return o1.getFechaDesde().compareTo(o2.getFechaDesde());
					}
				});
				empleado.setLegajo(legajo);
				GTLPersonalBeanFactory.getInstance().getBean2(VacacionesFacadeRemote.class).grabarVacaciones(empleado, registroActual);
				refreshTableVacaciones();
			}
			return false;
		}
	}

	private JPanel getPanelTabVacaciones(){
		JPanel panel = new JPanel();
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelSur.add(new JLabel("Total días acumulados: "));
		panelSur.add(getTxtSumaDiasRemanentes());
		
		panel.add(getPanTablaVacaciones(),BorderLayout.CENTER);
		panel.add(panelSur,BorderLayout.SOUTH);
		
		return panel;
	}

	private FWJNumericTextField getTxtSumaDiasRemanentes() {
		if(txtSumaDiasRemanentes == null){
			txtSumaDiasRemanentes = new FWJNumericTextField();
			txtSumaDiasRemanentes.setPreferredSize(new Dimension(120, 20));
			txtSumaDiasRemanentes.setEditable(false);
		}
		return txtSumaDiasRemanentes;
	}

	
	private PanelResumenVacaciones getPanTablaVacaciones() {
		if(panTablaVacaciones == null){
			panTablaVacaciones = new PanelResumenVacaciones();
			panTablaVacaciones.setPreferredSize(new Dimension(500, 300));
		}
		return panTablaVacaciones;
	}
	
	private ConfiguracionVacacionesFacadeRemote getConfiguracionFacade() {
		if (configuracionFacade == null) {
			configuracionFacade = GTLPersonalBeanFactory.getInstance().getBean2(ConfiguracionVacacionesFacadeRemote.class);
		}
		return configuracionFacade;
	}
}