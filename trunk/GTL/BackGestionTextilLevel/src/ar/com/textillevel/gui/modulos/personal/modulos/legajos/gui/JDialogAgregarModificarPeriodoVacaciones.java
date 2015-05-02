package ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.util.AntiguedadHelper;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.PeriodoVacaciones;
import ar.com.textillevel.modulos.personal.facade.api.remote.CalendarioAnualFeriadoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;
import ar.com.textillevel.modulos.personal.utils.VacacionesHelper;

public class JDialogAgregarModificarPeriodoVacaciones extends JDialog {

	private static final long serialVersionUID = 7733491307144474800L;

	private PanelDatePicker panelFechaDesde;
	private PanelDatePicker panelFechaHasta;
	private JComboBox cmbAnioCorrespondiente;
	private CLJNumericTextField txtTotalDias;
	private CLJNumericTextField txtTotalDiasRemanentes;
	private CLJTextField txtPeriodoCorrespondiente;
	private CLJTextField txtAntiguedad;
	private JButton btnAceptar;
	private JButton btnSalir;
	private JLabel lblNotas;
	
	private JPanel panelNorte;
	private JPanel panelCentro;
	private JPanel panelSur;

	private LegajoEmpleado legajo;
	private PeriodoVacaciones periodoCorrespondiente;
	private RegistroVacacionesLegajo registroActual;

	private CalendarioAnualFeriadoFacadeRemote calendarioFacade;
	
	private Map<Integer, List<RegistroVacacionesLegajo>> mapaAniosVacaciones;
	private Integer _anioComboElegido;
	private boolean acepto;

	public JDialogAgregarModificarPeriodoVacaciones(Frame frame, LegajoEmpleado legajo, PeriodoVacaciones periodoCorrespondiente) {
		super(frame);
		setRegistroActual(new RegistroVacacionesLegajo());
		setLegajo(legajo);
		setPeriodoCorrespondiente(periodoCorrespondiente);
		setUpComponentes();
		setUpScreen();
	}
	
	public JDialogAgregarModificarPeriodoVacaciones(Dialog frame, LegajoEmpleado legajo, PeriodoVacaciones periodoCorrespondiente) {
		super(frame);
		setRegistroActual(new RegistroVacacionesLegajo());
		setLegajo(legajo);
		setPeriodoCorrespondiente(periodoCorrespondiente);
		setUpComponentes();
		setUpScreen();
	}

	public JDialogAgregarModificarPeriodoVacaciones(Dialog frame, LegajoEmpleado legajo, PeriodoVacaciones periodoCorrespondiente, RegistroVacacionesLegajo registro){
		super(frame);
		setRegistroActual(registro);
		setLegajo(legajo);
		setPeriodoCorrespondiente(periodoCorrespondiente);
		setUpComponentes();
		setUpScreen();
	}
	
	private void setUpScreen() {
		setTitle("Agregar/modificar periodo vacacional");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(650, 190));
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
		add(getPanelCentro(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	public JPanel getPanelNorte() {
		if (panelNorte == null) {
			panelNorte = new JPanel(new GridBagLayout());
			java.sql.Date anioAl31_12 = DateUtil.getFecha(DateUtil.getAnio(DateUtil.getHoy()), 12, 31);
			panelNorte.add(new JLabel("Antig�edad al " + DateUtil.dateToString(anioAl31_12) + ": "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(getTxtAntiguedad(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

			panelNorte.add(new JLabel("Dias correspondientes: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelNorte.add(getTxtPeriodoCorrespondiente(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

			panelNorte.add(new JSeparator(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 4, 1, 1, 0));
		}
		return panelNorte;
	}

	public JPanel getPanelCentro() {
		if (panelCentro == null) {
			panelCentro = new JPanel(new GridBagLayout());

			panelCentro.add(new JLabel("Fecha desde: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getPanelFechaDesde(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

			panelCentro.add(new JLabel("Fecha hasta: "), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getPanelFechaHasta(), GenericUtils.createGridBagConstraints(3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

			panelCentro.add(new JLabel("Corresponden a: "), GenericUtils.createGridBagConstraints(4, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getCmbAnioCorrespondiente(), GenericUtils.createGridBagConstraints(5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

			
			panelCentro.add(new JLabel("Total d�as: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtTotalDias(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));

			panelCentro.add(new JLabel("D�as remanentes: "), GenericUtils.createGridBagConstraints(2, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtTotalDiasRemanentes(), GenericUtils.createGridBagConstraints(3, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			
			panelCentro.add(getLblNotas(), GenericUtils.createGridBagConstraints(4, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		}
		return panelCentro;
	}

	public JPanel getPanelSur() {
		if (panelSur == null) {
			panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnAceptar());
			panelSur.add(getBtnSalir());
		}
		return panelSur;
	}

	public RegistroVacacionesLegajo getRegistroActual() {
		return registroActual;
	}

	public void setRegistroActual(RegistroVacacionesLegajo registroActual) {
		this.registroActual = registroActual;
	}

	public PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker() {

				private static final long serialVersionUID = -1441694992762420106L;

				@Override
				public void accionBotonCalendarioAdicional() {
					calcularDias();
				}
			};
			panelFechaDesde.clearFecha();
		}
		return panelFechaDesde;
	}

	public PanelDatePicker getPanelFechaHasta() {
		if (panelFechaHasta == null) {
			panelFechaHasta = new PanelDatePicker() {

				private static final long serialVersionUID = -645497111948818562L;

				@Override
				public void accionBotonCalendarioAdicional() {
					calcularDias();
				}
			};
			panelFechaHasta.clearFecha();
		}
		return panelFechaHasta;
	}

	private void calcularDias() {
		Date fechaDesde = getPanelFechaDesde().getDate();
		Date fechaHasta = getPanelFechaHasta().getDate();
		if (fechaDesde != null && fechaHasta != null) {
			java.sql.Date fechaDesdeSql = new java.sql.Date(fechaDesde.getTime());
			java.sql.Date fechaHastaSql = new java.sql.Date(fechaHasta.getTime());
			if(fechaDesdeSql.before(fechaHastaSql)){
				if(!seSolapanFechas(fechaDesdeSql,fechaHastaSql)){
					Integer cantidadDias = DateUtil.contarDias(fechaDesdeSql, fechaHastaSql);
					Integer diasDisponibles = getDiasDisponiblesAnioCombo();
					if(cantidadDias <= diasDisponibles){
						Integer cantidadDiasTomados = DateUtil.contarDias(fechaDesdeSql, fechaHastaSql);
						getTxtTotalDiasRemanentes().setValue(diasDisponibles.longValue());
						getTxtTotalDias().setValue(cantidadDiasTomados.longValue());
						getLblNotas().setText("Para el a�o " + getCmbAnioCorrespondiente().getSelectedItem() + " hay " + (diasDisponibles -cantidadDias) + " d�as");
					}else{
						CLJOptionPane.showErrorMessage(this, "El empleado solo puede tomarse " + diasDisponibles + " d�as", "Error");
					}
				}else{
					CLJOptionPane.showErrorMessage(this, "Las fechas elegidas se solapan con otro periodo elegido", "Error");
				}
			}else{
				CLJOptionPane.showErrorMessage(this, "La 'Fecha desde' debe ser anterior a la 'Fecha hasta'", "Error");
			}
		}
	}
	
	private boolean seSolapanFechas(java.sql.Date fechaDesdeSql, java.sql.Date fechaHastaSql) {
		for(RegistroVacacionesLegajo r : getLegajo().getHistorialVacaciones()){
			if(r.seSolapa(fechaDesdeSql, fechaHastaSql)){
				return true;
			}
		}
		return false;
	}

	private void capturarDatos(){
		java.sql.Date fechaDesdeSql = new java.sql.Date(getPanelFechaDesde().getDate().getTime());
		java.sql.Date fechaHastaSql = new java.sql.Date(getPanelFechaHasta().getDate().getTime());
		getRegistroActual().setDiasHabiles(getCalendarioFacade().getCantidadDiasHabilesBetweenFechas(fechaDesdeSql, fechaHastaSql));
		Integer cantidadDiasTomados = DateUtil.contarDias(fechaDesdeSql, fechaHastaSql);
		getRegistroActual().setTotalDiasTomados(cantidadDiasTomados);
		getRegistroActual().setDiasRemanentes(getDiasDisponiblesAnioCombo()-cantidadDiasTomados);
		getRegistroActual().setDiasConfiguracion(getPeriodoCorrespondiente().getCantidadDias());
		getRegistroActual().setFechaDesde(fechaDesdeSql);
		getRegistroActual().setFechaHasta(fechaHastaSql);
		getRegistroActual().setAnioCorrespondiente((Integer)getCmbAnioCorrespondiente().getSelectedItem());
	}

	private Integer getDiasDisponiblesAnioCombo(){
		Integer anio = (Integer)getCmbAnioCorrespondiente().getSelectedItem();
		ConfiguracionVacaciones conf = GTLPersonalBeanFactory.getInstance().getBean2(ConfiguracionVacacionesFacadeRemote.class).getConfiguracionVacaciones(DateUtil.stringToDate("31/12/"+anio, DateUtil.SHORT_DATE));
		List<VigenciaEmpleado> historialVigencias = getLegajo().getHistorialVigencias();
		VigenciaEmpleado ultimaVigencia = historialVigencias.get(historialVigencias.size()-1);
		return VacacionesHelper.getDiasDisponiblesParaAnio(getLegajo().getHistorialVacaciones(), anio, conf, ultimaVigencia.getFechaAlta());
	}
	
	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public CLJNumericTextField getTxtTotalDias() {
		if (txtTotalDias == null) {
			txtTotalDias = new CLJNumericTextField();
			txtTotalDias.setEditable(false);
		}
		return txtTotalDias;
	}

	public CLJNumericTextField getTxtTotalDiasRemanentes() {
		if (txtTotalDiasRemanentes == null) {
			txtTotalDiasRemanentes = new CLJNumericTextField();
			txtTotalDiasRemanentes.setEditable(false);
		}
		return txtTotalDiasRemanentes;
	}

	private void salir() {
		if (CLJOptionPane.showQuestionMessage(this, "Va a salir sin guardar cambios, desea continuar?", "Pregunta") == CLJOptionPane.YES_OPTION) {
			dispose();
		}
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(validar()){
						capturarDatos();
						setAcepto(true);
						dispose();
					}
				}
			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		Date fechaDesde = getPanelFechaDesde().getDate();
		Date fechaHasta = getPanelFechaHasta().getDate();
		if (fechaDesde == null) {
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la 'Fecha desde'", "Error");
			return false;
		}
		if(fechaHasta == null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la 'Fecha hasta'", "Error");
			return false;
		}
		java.sql.Date fechaDesdeSql = new java.sql.Date(getPanelFechaDesde().getDate().getTime());
		java.sql.Date fechaHastaSql = new java.sql.Date(getPanelFechaHasta().getDate().getTime());
		if(!fechaDesdeSql.before(fechaHastaSql)){
			CLJOptionPane.showErrorMessage(this, "La 'Fecha desde' debe ser anterior a la 'Fecha hasta'", "Error");
			return false;
		}
		if(seSolapanFechas(fechaDesdeSql, fechaHastaSql)){
			CLJOptionPane.showErrorMessage(this, "Las fechas elegidas se solapan con otro periodo elegido", "Error");
		}
		Integer cantidadDias = DateUtil.contarDias(fechaDesdeSql, fechaHastaSql);
		Integer diasDisponibles = getDiasDisponiblesAnioCombo();
		if(cantidadDias > diasDisponibles){
			CLJOptionPane.showErrorMessage(this, "El empleado solo puede tomarse " + getPeriodoCorrespondiente().getCantidadDias() + " d�as", "Error");
			return false;
		}
		
		for(RegistroVacacionesLegajo reg : getLegajo().getHistorialVacaciones()){
			if( (getRegistroActual().getId()!=null && !getRegistroActual().getId().equals(reg)) || getRegistroActual().getId() == null){
				if( getRegistroActual().getId()==null){
					getRegistroActual().setFechaDesde(fechaDesdeSql);
					getRegistroActual().setFechaHasta(fechaHastaSql);
				}
				if(getRegistroActual().seSolapa(reg.getFechaDesde(), reg.getFechaHasta())){
					CLJOptionPane.showErrorMessage(this, "El periodo se solpa con otro agregado previamente", "Error");
					return false;
				}
			}
		}
		return true;
	}
	
	public JButton getBtnSalir() {
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

	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	public PeriodoVacaciones getPeriodoCorrespondiente() {
		return periodoCorrespondiente;
	}

	public void setPeriodoCorrespondiente(PeriodoVacaciones periodoCorrespondiente) {
		this.periodoCorrespondiente = periodoCorrespondiente;
	}

	public CLJTextField getTxtPeriodoCorrespondiente() {
		if (txtPeriodoCorrespondiente == null) {
			txtPeriodoCorrespondiente = new CLJTextField();
			txtPeriodoCorrespondiente.setEditable(false);
			txtPeriodoCorrespondiente.setText(getPeriodoCorrespondiente().toString());
		}
		return txtPeriodoCorrespondiente;
	}

	public CLJTextField getTxtAntiguedad() {
		if(txtAntiguedad == null){
			txtAntiguedad = new CLJTextField();
			txtAntiguedad.setEditable(false);
			java.sql.Date anioAl31_12 = DateUtil.getFecha(DateUtil.getAnio(DateUtil.getHoy()), 12, 31);
			Integer antiguedadAnios = AntiguedadHelper.getInstance().calcularAntiguedad(anioAl31_12, getLegajo());
			if(antiguedadAnios == 0){ //no tiene un a�o de antiguedad
				int difMeses = Math.abs(DateUtil.getDaysBetween(DateUtil.getHoy(), anioAl31_12)/30);
				txtAntiguedad.setText(difMeses + " meses");
			}else{
				txtAntiguedad.setText(antiguedadAnios + " a�os");
			}
		}
		return txtAntiguedad;
	}

	public CalendarioAnualFeriadoFacadeRemote getCalendarioFacade() {
		if(calendarioFacade == null){
			calendarioFacade = GTLPersonalBeanFactory.getInstance().getBean2(CalendarioAnualFeriadoFacadeRemote.class);
		}
		return calendarioFacade;
	}
	
	public JComboBox getCmbAnioCorrespondiente() {
		if(cmbAnioCorrespondiente == null){
			cmbAnioCorrespondiente = new JComboBox();
			VigenciaEmpleado vigenciaEmpleado = getLegajo().getHistorialVigencias().get(getLegajo().getHistorialVigencias().size()-1);
			for(Integer anio = DateUtil.getAnio(vigenciaEmpleado.getFechaAlta());anio<DateUtil.getAnio(DateUtil.getHoy())+2;anio++){
				cmbAnioCorrespondiente.addItem(anio);
			}
			_anioComboElegido = (Integer)cmbAnioCorrespondiente.getSelectedItem();
			cmbAnioCorrespondiente.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						Integer anio = (Integer)getCmbAnioCorrespondiente().getSelectedItem();
						ConfiguracionVacaciones conf = GTLPersonalBeanFactory.getInstance().getBean2(ConfiguracionVacacionesFacadeRemote.class).getConfiguracionVacaciones(DateUtil.stringToDate("31/12/"+anio, DateUtil.SHORT_DATE));
						List<VigenciaEmpleado> historialVigencias = getLegajo().getHistorialVigencias();
						VigenciaEmpleado ultimaVigencia = historialVigencias.get(historialVigencias.size()-1);
						for(Integer an = DateUtil.getAnio(ultimaVigencia.getFechaAlta());an<anio;an++){
							if(getMapaAniosVacaciones().get(an)==null){
								CLJOptionPane.showErrorMessage(JDialogAgregarModificarPeriodoVacaciones.this, StringW.wordWrap("No pueden asignarse vacaciones para el " + anio + " debido a que aun no se han asignado para el " + an), "Error");
								GenericUtils.setValorComboSinListeners(getCmbAnioCorrespondiente(), _anioComboElegido);
								return;
							}else{
								List<RegistroVacacionesLegajo> list = getMapaAniosVacaciones().get(an);
								Collections.sort(list, new Comparator<RegistroVacacionesLegajo>() {
									public int compare(RegistroVacacionesLegajo o1, RegistroVacacionesLegajo o2) {
										return o1.getFechaDesde().compareTo(o2.getFechaDesde())*-1;
									}
								});
								if(list.get(0).getDiasRemanentes()>0){
									CLJOptionPane.showErrorMessage(JDialogAgregarModificarPeriodoVacaciones.this, StringW.wordWrap("Aun tiene d�as disponibles en el a�o " + an), "Error");
									GenericUtils.setValorComboSinListeners(getCmbAnioCorrespondiente(), _anioComboElegido);
									return;
								}
							}
						}
						Integer diasDisponiblesParaAnio = VacacionesHelper.getDiasDisponiblesParaAnio(getLegajo().getHistorialVacaciones(), anio,conf,ultimaVigencia.getFechaAlta());
						if(diasDisponiblesParaAnio==0){
							CLJOptionPane.showErrorMessage(JDialogAgregarModificarPeriodoVacaciones.this, "No quedan d�as disponibles para el a�o seleccionado", "Error");
							GenericUtils.setValorComboSinListeners(getCmbAnioCorrespondiente(), _anioComboElegido);
							return;
						}else{
							getLblNotas().setText("Para el a�o " + anio + " hay " + diasDisponiblesParaAnio + " d�as");
							_anioComboElegido = anio;
						}
					}
				}
			});
		}
		return cmbAnioCorrespondiente;
	}

	
	public Map<Integer, List<RegistroVacacionesLegajo>> getMapaAniosVacaciones() {
		if(mapaAniosVacaciones == null){
			mapaAniosVacaciones = VacacionesHelper.agruparPorAnio(getLegajo().getHistorialVacaciones());
		}
		return mapaAniosVacaciones;
	}

	public JLabel getLblNotas() {
		if(lblNotas == null){
			lblNotas = new JLabel();
			Integer anio = (Integer)getCmbAnioCorrespondiente().getSelectedItem();
			lblNotas.setText("Para el a�o " + anio + " hay " + getDiasDisponiblesAnioCombo() + " d�as");
		}
		return lblNotas;
	}
}
