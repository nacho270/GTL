package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.entidades.Vigencia;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.facade.api.remote.QuincenaFacadeRemote;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.cabecera.ModeloCabeceraReciboSueldo;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.exception.InvalidStateReciboSueldoException;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.util.AnotacionesHelper;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.util.AntiguedadHelper;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.ConfiguracionAntiguedad;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.EEstadoDiaFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.to.FichadaLegajoTO;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ETipoCobro;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.presentismo.ConfiguracionPresentismo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoDeduccion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoHaber;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoNoRemunerativo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoRetencion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.Quincena;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.Asignacion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemPorcSueldoBruto;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemSimple;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldoHaber;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldoRetencion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ValorConceptoFecha;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.enums.ETipoConceptoReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EQuincena;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.ETipoItemReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.facade.api.remote.AntiguedadFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.AsignacionFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.CalendarioAnualFeriadoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConceptoReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionPresentismoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.FichadaLegajoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.ReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.CalendarioLaboralHelper;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;
import ar.com.textillevel.modulos.personal.utils.ReciboSueldoHelper;

public class ReciboSueldoDatosHandler {

	private static final int CANTIDAD_DIAS_QUINCENA = 15;

	private ReciboSueldo reciboSueldo;
	private ModeloCabeceraReciboSueldo modeloCabecera;
	private CalendarioAnualFeriado calendario;

	private FichadaLegajoFacadeRemote fichadaFacade;
	private ConceptoReciboSueldoFacadeRemote conceptosReciboSueldoFacade;
	private AntiguedadFacadeRemote antiguedadFacade;
	private ConfiguracionPresentismoFacadeRemote configuracionPresentismoFacade;
	private CalendarioAnualFeriadoFacadeRemote calendarioFacade;
	private AsignacionFacadeRemote asignacionFacade;
	private ReciboSueldoFacadeRemote reciboSueldoFacade;
	private QuincenaFacadeRemote quincenaFacade;

	public ReciboSueldoDatosHandler(ReciboSueldo reciboSueldo, ModeloCabeceraReciboSueldo modeloCabecera, BigDecimal valorHora, List<ValeAnticipo> valesAnticipo) throws InvalidStateReciboSueldoException {
		this.reciboSueldo = reciboSueldo;
		this.modeloCabecera = modeloCabecera;
		cargarDatosReciboSueldo(valorHora, valesAnticipo);
	}

	private void cargarDatosReciboSueldo(BigDecimal valorHora, List<ValeAnticipo> valesAnticipo) throws InvalidStateReciboSueldoException {
		cargarDatosBasicos();
		Vigencia rangoFechasRS = calcularRangoFechasRS();
		BigDecimal totalHaber = cargarHaberes(rangoFechasRS, valorHora);
		cargarDeducciones(valesAnticipo);
		cargarConceptosReciboSueldo(totalHaber);
		cargarAsignacionesNoRemunerativas(rangoFechasRS);
		cargarTotalesReciboSueldo();
		cargarAsignacionesNoRemunAsigSueldoBruto(rangoFechasRS);
		cargarTotalesReciboSueldo();
	}

	private Vigencia calcularRangoFechasRS() {
		Date fechaDesde = null;
		Date fechaHasta = null;
		//Obtengo la info de horas trabajadas
		if(reciboSueldo.getQuincena() == null) { //Es mensual
			fechaDesde = DateUtil.getFecha(modeloCabecera.getAnio(), modeloCabecera.getMes().getNroMes(), 1);
			fechaHasta = DateUtil.getUltimoDiaMes(fechaDesde);
		} else { //Es quincenal
			boolean primerQuincena = reciboSueldo.getQuincena().getId() == EQuincena.PRIMERA.getId();
			fechaDesde = primerQuincena ? DateUtil.getFecha(modeloCabecera.getAnio(), modeloCabecera.getMes().getNroMes(), 1) : DateUtil.getFecha(modeloCabecera.getAnio(), modeloCabecera.getMes().getNroMes(), CANTIDAD_DIAS_QUINCENA);
			fechaHasta = primerQuincena ? DateUtil.getFecha(modeloCabecera.getAnio(), modeloCabecera.getMes().getNroMes(), CANTIDAD_DIAS_QUINCENA) : DateUtil.getUltimoDiaMes(fechaDesde);
		}
		fechaDesde = DateUtil.redondearFecha(fechaDesde);
		fechaDesde = new Date(fechaDesde.getTime()+DateUtil.ONE_HOUR);
		
		fechaHasta = DateUtil.redondearFecha(fechaHasta);
		fechaHasta = new Date(fechaHasta.getTime()+DateUtil.ONE_HOUR);

		return new Vigencia(fechaDesde, fechaHasta);
	}

	private void cargarAsignacionesNoRemunerativas(Vigencia rangoFechasRS) {
		boolean esQuincenal = reciboSueldo.getQuincena() != null; 
		if(esQuincenal && reciboSueldo.getQuincena().getId() == EQuincena.PRIMERA.getId()) {
			return;
		} else {
			CalendarioAnualFeriado calendarioFeriados = getCalendario(DateUtil.getAnio(rangoFechasRS.getFechaHasta()));
			Date fechaDesde =  esQuincenal ? DateUtil.getFecha(reciboSueldo.getAnio(), reciboSueldo.getMes().getNroMes(), 1) : rangoFechasRS.getFechaDesde();
			Integer diasHabiles = CalendarioLaboralHelper.getCantidadDiasHabiles(calendarioFeriados, fechaDesde, rangoFechasRS.getFechaHasta());
			Integer diasTrabajados = 0;
			if(esQuincenal) {
				ReciboSueldo reciboSueldoAnterior = getReciboSueldoFacade().getReciboSueldoAnterior(reciboSueldo);
				diasTrabajados = reciboSueldoAnterior == null ? reciboSueldo.getDiasTrabajados() : reciboSueldo.getDiasTrabajados() + reciboSueldoAnterior.getDiasTrabajados();
			} else {
				diasTrabajados = reciboSueldo.getDiasTrabajados();
			}
			//TODO: si es un sindicato quincenal entonces diasTrabajados debe ser por hora en cambio si es mensual es así como está ahora
			double porcentajeDiasTrabajados = (diasTrabajados*1d / diasHabiles*1d);
			Sindicato sindicato = reciboSueldo.getLegajo().getCategoria().getSindicato();
			List<AsignacionNoRemSimple> asignaciones  = getAsignacionFacade().getAllNoRemSimpleByFechaAndIdSindicato(rangoFechasRS.getFechaHasta(), sindicato.getId());
			for(AsignacionNoRemSimple asigSimple : asignaciones) {
				if(aplicaSegunQuincena(sindicato, asigSimple, reciboSueldo.getQuincena())) {
					ItemReciboSueldoNoRemunerativo irsnr = new ItemReciboSueldoNoRemunerativo();
					irsnr.setAsignacionNoRem(asigSimple);
					irsnr.setMonto(asigSimple.getImporte().multiply(new BigDecimal(porcentajeDiasTrabajados)));
					irsnr.setTipoItemReciboSueldo(ETipoItemReciboSueldo.ASIGNACION_NO_REMUNERATIVA_SIMPLE);
					irsnr.setDescripcion(("No Rem. " + reciboSueldo.getMes().getNombre() + " " + reciboSueldo.getAnio()).toUpperCase());
					reciboSueldo.getItems().add(irsnr);
				}
			}
		}
	}

	private void cargarAsignacionesNoRemunAsigSueldoBruto(Vigencia rangoFechasRS) {
		boolean esQuincenal = reciboSueldo.getQuincena() != null; 
		if(esQuincenal && reciboSueldo.getQuincena().getId() == EQuincena.PRIMERA.getId()) {
			return;
		} else {
			Sindicato sindicato = reciboSueldo.getLegajo().getCategoria().getSindicato();
			BigDecimal totalBruto = reciboSueldo.getBruto();
			//Busco el item de antiguedad y se lo resto al total bruto
			for(ItemReciboSueldo irs : reciboSueldo.getItems()) {
				if(irs.getTipoItemReciboSueldo() == ETipoItemReciboSueldo.ANTIGUEDAD) {
					totalBruto = totalBruto.subtract(irs.getMonto());
				}
			}
			List<AsignacionNoRemPorcSueldoBruto> asignaciones = getAsignacionFacade().getAllNoRemPorcSueldoBrutoByFechaAndIdSindicato(rangoFechasRS.getFechaHasta(), sindicato.getId());
			for(AsignacionNoRemPorcSueldoBruto asigPorcSueldoBruto : asignaciones) {
				if(aplicaSegunQuincena(sindicato, asigPorcSueldoBruto, reciboSueldo.getQuincena())) {
					ItemReciboSueldoNoRemunerativo irsnr = new ItemReciboSueldoNoRemunerativo();
					irsnr.setAsignacionNoRem(asigPorcSueldoBruto);
					BigDecimal montoAsignacion = asigPorcSueldoBruto.getPorcentaje().multiply(totalBruto).divide(new BigDecimal(100));
					irsnr.setMonto(montoAsignacion);
					irsnr.setTipoItemReciboSueldo(ETipoItemReciboSueldo.ASIGNACION_NO_REMUNERATIVA_PORC_SUELDO_BRUTO);
					irsnr.setDescripcion(asigPorcSueldoBruto.getTextoVisualizacionRS());
					reciboSueldo.getItems().add(irsnr);
				}
			}
		}
	}

	private boolean aplicaSegunQuincena(Sindicato sindicato, Asignacion asig, Quincena quincena) {
		if(sindicato.getTipoCobro() == ETipoCobro.MENSUAL) {
			return true;
		} else {
			return quincena != null && (asig.getQuincena() == null || asig.getQuincena().equals(quincena));
		}
	}

	private void cargarDeducciones(List<ValeAnticipo> valesAnticipo) {
		BigDecimal monto = BigDecimal.ZERO;
		for(ValeAnticipo vale : valesAnticipo) {
			monto = monto.add(vale.getMonto());
			vale.setEstadoValeAnticipo(EEstadoValeAnticipo.DESCONTADO);
		}
		if(monto.compareTo(BigDecimal.ZERO) > 0) {
			ItemReciboSueldoDeduccion itemDeduccion = new ItemReciboSueldoDeduccion();
			itemDeduccion.getVales().addAll(valesAnticipo);
			itemDeduccion.setMonto(monto);
			itemDeduccion.setTipoItemReciboSueldo(ETipoItemReciboSueldo.VALE_ANTICIPO);
			itemDeduccion.setDescripcion(ETipoItemReciboSueldo.VALE_ANTICIPO.getNombre());
			reciboSueldo.getItems().add(itemDeduccion);
		}
	}

	private void cargarTotalesReciboSueldo() {
		CalculadorTotalesVisitor visitor = new CalculadorTotalesVisitor(reciboSueldo);
		for(ItemReciboSueldo irs : reciboSueldo.getItems()) {
			irs.aceptarVisitor(visitor);
		}
	}

	private void cargarDatosBasicos() {
		reciboSueldo.setAnio(modeloCabecera.getAnio());
		reciboSueldo.setMes(modeloCabecera.getMes());
		reciboSueldo.setQuincena(modeloCabecera.getQuincena() == null ? null : quincenaFacade.getById(modeloCabecera.getQuincena().getId()));
		reciboSueldo.setTextoOrden(ReciboSueldoHelper.getInstance().calcularTextoOrdenRS(reciboSueldo));
	}

	private BigDecimal cargarHaberes(Vigencia rangoFechasRS, BigDecimal valorHora) throws InvalidStateReciboSueldoException {
		boolean esQuincenal = reciboSueldo.getQuincena() != null;
		LegajoEmpleado legajo = reciboSueldo.getLegajo();
		List<FichadaLegajoTO> infoFichadas = getFichadaFacade().getAllByLegajoYFecha(legajo, rangoFechasRS.getFechaDesde(), rangoFechasRS.getFechaHasta());
		
		//Obtengo la configuracion del presentismo
		BigDecimal porcentajePresentismo = null;
		Sindicato sindicato = legajo.getCategoria().getSindicato();
		ConfiguracionPresentismo configPresentismoActual = getConfiguracionPresentismoFacade().getConfiguracionPresentismoByFechaYSindicato(rangoFechasRS.getFechaHasta(), sindicato);
		if(configPresentismoActual != null) {
			porcentajePresentismo = configPresentismoActual.getPorcentajeTotal();
		}

		Double totalHaber = 0d;
		Double totalHoras = 0d;
		Double totalHorasAl50 = 0d;
		Double totalHorasAl100 = 0d;
		Double totalParaElCalculoDelPresentismo = 0d;
		Integer diasTrabajados = 0;

		List<FichadaLegajoTO> feriados = new ArrayList<FichadaLegajoTO>();

		for(FichadaLegajoTO fichada : infoFichadas) {
			totalHoras += fichada.getHorasNormales();
			totalHorasAl50 += fichada.getHorasExtrasAl50();
			totalHorasAl100 += fichada.getHorasExtrasAl100();
			//calculo el porcentaje de presentismo
			if(porcentajePresentismo != null && porcentajePresentismo.compareTo(BigDecimal.ZERO) > 0) {
				if(fichada.getPorcentajeDescuentoAusentismo() != null) {
					porcentajePresentismo = porcentajePresentismo.subtract(fichada.getPorcentajeDescuentoAusentismo());
				}
			}
			if(fichada.getHorasNormales() > 0) {
				diasTrabajados ++;
			}
			//guardo los feriados en otra lista para procesarlos luego
			if(fichada.getEstadoDiaFichada() == EEstadoDiaFichada.FERIADO) {
				feriados.add(fichada);
			}
		}

		//Lleno los dias trabajados
		reciboSueldo.setDiasTrabajados(diasTrabajados);

		//Lleno el valor hora
		reciboSueldo.setValorHora(valorHora);

		//Lleno las anotaciones
		reciboSueldo.getAnotaciones().addAll(AnotacionesHelper.getInstance().calcularAnotaciones(infoFichadas));

		//proceso los feriados
		BigDecimal totalHorasFeriado = procesarFeriados(feriados, rangoFechasRS, valorHora);

		//Sumo al total de horas lo de los feriados
		totalHoras = totalHoras + totalHorasFeriado.doubleValue();

		//Creo el item recibo correspondiente al sueldo basico
		ItemReciboSueldoHaber irsh = crearItemReciboSueldoHaber(ETipoItemReciboSueldo.SUELDO_BASICO, esQuincenal ? "HORAS TRABAJADAS" : ETipoItemReciboSueldo.SUELDO_BASICO.getNombre(), new BigDecimal(totalHoras).multiply(valorHora), totalHoras.doubleValue());
		reciboSueldo.getItems().add(irsh);
		totalHaber += irsh.getMonto().doubleValue();
		totalParaElCalculoDelPresentismo += totalHaber;

		//Creo el item recibo correspondiente a las horas al 50%
		if(totalHorasAl50 > 0d) {
			irsh = crearItemReciboSueldoHaber(ETipoItemReciboSueldo.HORAS_EXTRAS_AL_50, ETipoItemReciboSueldo.HORAS_EXTRAS_AL_50.getNombre(), new BigDecimal(totalHorasAl50*1.5).multiply(valorHora), totalHorasAl50.doubleValue());
			reciboSueldo.getItems().add(irsh);
			totalHaber += irsh.getMonto().doubleValue();
			totalParaElCalculoDelPresentismo += totalHorasAl50;
		}

		//Creo el item recibo correspondiente a las horas al 100%
		if(totalHorasAl100 > 0d) {
			irsh = crearItemReciboSueldoHaber(ETipoItemReciboSueldo.HORAS_EXTRAS_AL_100, ETipoItemReciboSueldo.HORAS_EXTRAS_AL_100.getNombre(), new BigDecimal(totalHorasAl100*2).multiply(valorHora), totalHorasAl100.doubleValue());
			reciboSueldo.getItems().add(irsh);
			totalHaber += irsh.getMonto().doubleValue();
			totalParaElCalculoDelPresentismo += totalHorasAl100;
		}

		//Creo el item correspondiente al presentismo
		if(porcentajePresentismo != null && porcentajePresentismo.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal totalPremio = porcentajePresentismo.multiply(new BigDecimal(totalParaElCalculoDelPresentismo)).divide(new BigDecimal(100));
			irsh = crearItemReciboSueldoHaber(ETipoItemReciboSueldo.PREMIO_PRESENTISMO, ETipoItemReciboSueldo.PREMIO_PRESENTISMO.getNombre() + " " + porcentajePresentismo + "%", totalPremio, null);
			reciboSueldo.getItems().add(irsh);
			totalHaber += irsh.getMonto().doubleValue();
		}

		//Creo el item recibo correspondiente a la antiguedad
		Integer antiguedadAnios = AntiguedadHelper.getInstance().calcularAntiguedad(rangoFechasRS.getFechaHasta(), reciboSueldo);
		if(antiguedadAnios == null) {//se está queriendo hacer un recibo de sueldo anterior a la fecha de alta.
			throw new InvalidStateReciboSueldoException("No es posible realizar un recibo de sueldo para el período '" + ReciboSueldoHelper.getInstance().calcularPeriodoRS(reciboSueldo) + "'. Por favor, verifique que la fecha de alta sea posterior.");
		}
		ConfiguracionAntiguedad configAntiguedad = getAntiguedadFacade().getConfiguracionVigenteParaFecha(sindicato, rangoFechasRS.getFechaHasta());
		if(configAntiguedad != null) {
			BigDecimal valorAntiguedad = configAntiguedad.getImportePorPuestoYAnios(antiguedadAnios, legajo.getPuesto());
			if(valorAntiguedad != null) {
				irsh = crearItemReciboSueldoHaber(ETipoItemReciboSueldo.ANTIGUEDAD, "ANTIGÜEDAD " + antiguedadAnios + " AÑO(S)", valorAntiguedad, null);
				reciboSueldo.getItems().add(irsh);
				totalHaber += irsh.getMonto().doubleValue();
			}
		}

		return new BigDecimal(totalHaber);
	}

	private BigDecimal procesarFeriados(List<FichadaLegajoTO> feriados, Vigencia rangoFechasRS, BigDecimal valorHora) {
		BigDecimal totalHorasPorFeriados = BigDecimal.ZERO;
		Sindicato sindicato = reciboSueldo.getLegajo().getCategoria().getSindicato();
		CalendarioAnualFeriado calendario = getCalendario(DateUtil.getAnio(rangoFechasRS.getFechaHasta()));
		for(FichadaLegajoTO feriado : feriados) {
			BigDecimal totalHoras = CalendarioLaboralHelper.getCantidadHorasFeriadoBySindicato(calendario, feriado.getDia(), sindicato);
			if(CalendarioLaboralHelper.discriminaEnRS(calendario, feriado.getDia(), sindicato)) {
				ItemReciboSueldoHaber itemRSFeriado = crearItemReciboSueldoHaber(ETipoItemReciboSueldo.FERIADO, ETipoItemReciboSueldo.FERIADO.getNombre() +  " " + DateUtil.dateToString(feriado.getDia()), valorHora.multiply(totalHoras), totalHoras.doubleValue());
				reciboSueldo.getItems().add(itemRSFeriado);
			} else {
				totalHorasPorFeriados = totalHorasPorFeriados.add(totalHoras);
			}
		}
		return totalHorasPorFeriados;
	}

	private ItemReciboSueldoHaber crearItemReciboSueldoHaber(ETipoItemReciboSueldo tipo, String descripcion, BigDecimal monto, Double unidades) {
		ItemReciboSueldoHaber item = new ItemReciboSueldoHaber();
		item.setTipoItemReciboSueldo(tipo);
		item.setDescripcion(descripcion);
		item.setMonto(monto);
		item.setUnidades(unidades);
		return item;
	}

	private void cargarConceptosReciboSueldo(BigDecimal totalHaberes) {
		Date hoy = DateUtil.getHoy();
		List<ConceptoReciboSueldo> allBySindicato = getConceptosReciboSueldoFacade().getAllBySindicato(reciboSueldo.getLegajo().getCategoria().getSindicato());
		for(ConceptoReciboSueldo crs : allBySindicato) {
			ValorConceptoFecha valorConceptoVigente = crs.getValorConceptoVigente(hoy);
			if(valorConceptoVigente != null) {
				if(crs.getTipo() == ETipoConceptoReciboSueldo.HABER) {
					ItemReciboSueldoHaber irsh = new ItemReciboSueldoHaber();
					irsh.setDescripcion(crs.getNombre() + (valorConceptoVigente.getValorPorcentual() == null ? "" : " " + valorConceptoVigente.getValorPorcentual() + "%"));
					irsh.setMonto(calcularMonto(valorConceptoVigente, totalHaberes));
					irsh.setConceptoHaber((ConceptoReciboSueldoHaber)crs);
					irsh.setTipoItemReciboSueldo(ETipoItemReciboSueldo.CONCEPTO_HABER);
					reciboSueldo.getItems().add(irsh);
				} else {
					ItemReciboSueldoRetencion irsr = new ItemReciboSueldoRetencion();
					irsr.setDescripcion(crs.getNombre() + (valorConceptoVigente.getValorPorcentual() == null ? "" : " " + valorConceptoVigente.getValorPorcentual() + "%"));
					irsr.setMonto(calcularMonto(valorConceptoVigente, totalHaberes));
					irsr.setConceptoRetencion((ConceptoReciboSueldoRetencion)crs);
					irsr.setTipoItemReciboSueldo(ETipoItemReciboSueldo.CONCEPTO_DEBE);
					reciboSueldo.getItems().add(irsr);
				}
			}
		}
	}

	private BigDecimal calcularMonto(ValorConceptoFecha valorConceptoVigente, BigDecimal montoAOperar) {
		if(valorConceptoVigente.getValorNumerico() == null) {
			return valorConceptoVigente.getValorPorcentual().multiply(montoAOperar).divide(new BigDecimal(100));
		} else {
			return valorConceptoVigente.getValorNumerico();
		}
	}

	private FichadaLegajoFacadeRemote getFichadaFacade() {
		if(fichadaFacade == null) {
			fichadaFacade = GTLPersonalBeanFactory.getInstance().getBean2(FichadaLegajoFacadeRemote.class);
		}
		return fichadaFacade;
	}

	private ConceptoReciboSueldoFacadeRemote getConceptosReciboSueldoFacade() {
		if(conceptosReciboSueldoFacade == null) {
			conceptosReciboSueldoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ConceptoReciboSueldoFacadeRemote.class);
		}
		return conceptosReciboSueldoFacade;
	}

	private AntiguedadFacadeRemote getAntiguedadFacade() {
		if(antiguedadFacade == null) {
			antiguedadFacade = GTLPersonalBeanFactory.getInstance().getBean2(AntiguedadFacadeRemote.class);
		}
		return antiguedadFacade;
	}

	private ConfiguracionPresentismoFacadeRemote getConfiguracionPresentismoFacade() {
		if(configuracionPresentismoFacade == null) {
			configuracionPresentismoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ConfiguracionPresentismoFacadeRemote.class);
		}
		return configuracionPresentismoFacade;
	}

	private AsignacionFacadeRemote getAsignacionFacade() {
		if(asignacionFacade == null) {
			asignacionFacade = GTLPersonalBeanFactory.getInstance().getBean2(AsignacionFacadeRemote.class);
		}
		return asignacionFacade;
	}

	private CalendarioAnualFeriadoFacadeRemote getCalendarioFacade() {
		if(calendarioFacade == null) {
			calendarioFacade = GTLPersonalBeanFactory.getInstance().getBean2(CalendarioAnualFeriadoFacadeRemote.class);
		}
		return calendarioFacade;
	}

	private ReciboSueldoFacadeRemote getReciboSueldoFacade() {
		if(reciboSueldoFacade == null) {
			reciboSueldoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ReciboSueldoFacadeRemote.class);
		}
		return reciboSueldoFacade;
	}

	private CalendarioAnualFeriado getCalendario(Integer anio) {
		if(calendario == null) {
			calendario = getCalendarioFacade().getByIdEager(anio);
		}
		return calendario;
	}

}