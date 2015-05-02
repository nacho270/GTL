package ar.com.textillevel.modulos.personal.facade.impl;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.dao.api.FichadaLegajoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.RangoDiasFeriado;
import ar.com.textillevel.modulos.personal.entidades.fichadas.FichadaLegajo;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaParcial;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.ETipoAntiFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.EEstadoDiaFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.ETipoFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.to.FichadaLegajoTO;
import ar.com.textillevel.modulos.personal.entidades.fichadas.to.GrupoHoraEntradaSalidaTO;
import ar.com.textillevel.modulos.personal.entidades.legajos.HorarioDia;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.presentismo.ConfiguracionPresentismo;
import ar.com.textillevel.modulos.personal.facade.api.local.AntiFichadaFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.local.ConfiguracionPresentismoFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.local.FichadaLegajoFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.local.ParametrosGeneralesPersonalFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.remote.CalendarioAnualFeriadoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.FichadaLegajoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.PresentismoHelper;

@Stateless
public class FichadaLegajoFacade implements FichadaLegajoFacadeRemote, FichadaLegajoFacadeLocal {

	@EJB
	private FichadaLegajoDAOLocal fichadaDao;

	@EJB
	private CalendarioAnualFeriadoFacadeRemote calendarioFacade;

	@EJB
	private AntiFichadaFacadeLocal antiFichadasFacade;

	@EJB
	private ParametrosGeneralesPersonalFacadeLocal parametrosGralesFacade;
	
	@EJB
	private ConfiguracionPresentismoFacadeLocal confPresentismoFacade;

	/*
	 * PARA VOLVER A LA FORMA ANTERIOR DE CALCULO DE PRESENTISMO, VER LA VERSION 1897 DEL 8/10/11 15:45
	 */
	
	public List<FichadaLegajoTO> getAllByLegajoYFecha(LegajoEmpleado legajo, Date fechaDesde, Date fechaHasta) {
		List<FichadaLegajo> fichadas = fichadaDao.getAllByLegajoYFecha(legajo.getId(), fechaDesde, fechaHasta);
		if (fichadas != null && !fichadas.isEmpty()) {
			CalendarioAnualFeriado calendarioDesde = fechaDesde != null ? calendarioFacade.getByIdEager(DateUtil.getAnio(fechaDesde)) : null;
			CalendarioAnualFeriado calendarioHasta = fechaHasta != null ? calendarioFacade.getByIdEager(DateUtil.getAnio(fechaHasta)) : null;
			List<HorarioDia> horario = legajo.getHorario();
			Map<ContenedorFechasKey, List<FichadaLegajo>> fichadasAgrupadas = agruparFichadasPorDia(fichadas);
			Map<ContenedorFechasKey, FichadaLegajoTO> fichadasCompletadas = armarDias(fichadasAgrupadas, horario, calendarioDesde, calendarioDesde, legajo);
			List<FichadaLegajoTO> fichadasRet = completarDiasFaltantes(fichadasCompletadas, fechaDesde, fechaHasta, horario, calendarioDesde, calendarioHasta, legajo);
			calcularDescuentosPresentismo(fichadasRet,legajo.getCategoria().getSindicato());
			return fichadasRet;
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("incomplete-switch")
	private void calcularDescuentosPresentismo(List<FichadaLegajoTO> fichadasRet, Sindicato sindicato) {
		ContadorFaltas contador = new ContadorFaltas();
		for(FichadaLegajoTO fichada : fichadasRet){
			ConfiguracionPresentismo conf = confPresentismoFacade.getConfiguracionPresentismoByFechaYSindicato(fichada.getDia(),sindicato);
			PresentismoHelper pHelper = new PresentismoHelper(conf);
			switch(fichada.getEstadoDiaFichada()){
				case RETIRO_TEMPRANO:
				case FALTA:{
					contador.aumentar(1);
					fichada.setPorcentajeDescuentoAusentismo(pHelper.getDescuentoPorCantidadDeFaltas(contador.getContador()));
					break;
				}
				case TARDE_TEMPRANO:
				case TEMPRANO_TARDE:
				case TARDE:{
					fichada.setPorcentajeDescuentoAusentismo(pHelper.getDescuentoPorMinutosTarde(fichada.getMinutosParaPresentismo(), contador));
					break;
				}
			}
		}
	}

	private Map<ContenedorFechasKey, List<FichadaLegajo>> agruparFichadasPorDia(List<FichadaLegajo> fichadas) {
		Map<ContenedorFechasKey, List<FichadaLegajo>> mapRet = new LinkedHashMap<ContenedorFechasKey, List<FichadaLegajo>>();
		for (FichadaLegajo fichada : fichadas) {
			Date fechaRedondeada = DateUtil.redondearFecha(fichada.getHorario());
			ContenedorFechasKey contenedorTemp = new ContenedorFechasKey(fechaRedondeada, DateUtil.dateToString(fechaRedondeada));
			if (!mapRet.containsKey(contenedorTemp)) {
				mapRet.put(contenedorTemp, new ArrayList<FichadaLegajo>());
			}
			mapRet.get(contenedorTemp).add(fichada);
		}
		return mapRet;
	}

	/**
	 * @param fichadasAgrupadas
	 * @param calendarioHasta para chequear si vino en un feriado
	 * @param calendarioDesde idem
	 * @param horario para ver si vino tarde o temprano
	 * @param legajo
	 * @return Un mapa con la fecha y las fichadas de esa fecha completadas con el estado {@link EEstadoDiaFichada}
	 */
	private Map<ContenedorFechasKey, FichadaLegajoTO> armarDias(Map<ContenedorFechasKey, List<FichadaLegajo>> fichadasAgrupadas, List<HorarioDia> horario, CalendarioAnualFeriado calendarioDesde,
			CalendarioAnualFeriado calendarioHasta, LegajoEmpleado legajo) {
		Map<ContenedorFechasKey, FichadaLegajoTO> fichadasRet = new LinkedHashMap<ContenedorFechasKey, FichadaLegajoTO>();
		for (ContenedorFechasKey contenedor : fichadasAgrupadas.keySet()) {
			FichadaLegajoTO fichadaTO = new FichadaLegajoTO();
			for (FichadaLegajo fl : fichadasAgrupadas.get(contenedor)) {// lleno la informacion de las 2 entradas y las 2 salidas
				setearHorarios(fichadaTO, fl);
				fichadaTO.getFichadasComprendidas().add(fl);
			}
			if (!fichadaTO.esConsistente()) {
				fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.INCONSISTENTE);
			}
			// una vez llenos los horarios, ahora tengo que analizarlos para ver
			// si llego tarde o temprano y ver las horas extra
			procesarHorarioFichada(fichadaTO, horario, legajo);
			fichadasRet.put(contenedor, fichadaTO);
		}
		return fichadasRet;
	}

	/**
	 * Criterios: 
	 * 		-Llegada tarde: 
	 * 			1- Un minuto mas tarde => tarde 
	 * 			2- Un minuto mas temprano => normal 
	 * 		-Salida temprano: 
	 * 			1- Un minuto mas temprano => temprano 
	 * 			2- Un minuto mas tarde => normal 
	 * 		-Horas extra: Se calculan con una tolerancia. 
	 * 			Ej: Horario de entrada: 8:00. Tolerancia = 30 minutos.
	 * 				Llega 7:30 => Normal 
	 * 				Llega 7:29 => 1 minuto extra 
	 * 			Ej2: Horario salida: 18:00. Tolerancia = 30 minutos. 
	 * 				Sale: 18:01 => Normal 
	 * 				Sale: 18:31 => 1 minuto extra
	 * 
	 * @param fichadaTO
	 * @param horario
	 * @param legajo
	 * @param contador 
	 */
	private void procesarHorarioFichada(FichadaLegajoTO fichadaTO, List<HorarioDia> horario, LegajoEmpleado legajo) {
		int diaSemana = DateUtil.getDiaSemana(fichadaTO.getDia());
		Integer toleranciaHorasExtra = parametrosGralesFacade.getParametrosGenerales().getToleranciaParaHorasExtra();
		for (HorarioDia hd : horario) {
			if (estaDentroDeLosDiasDeTrabajo(fichadaTO.getDia(), horario)) {
				if ((hd.getRangoDias().getDiaDesde().getNroDia() + 1) <= diaSemana && 
					(hd.getRangoDias().getDiaHasta().getNroDia() + 1) >= diaSemana && 
					fichadaTO.getEstadoDiaFichada() != EEstadoDiaFichada.INCONSISTENTE) {
					List<AntiFichada> antifichadasParaFecha = antiFichadasFacade.getAntifichadasParaFecha(fichadaTO.getDia(), legajo);
					if (!antifichadasParaFecha.isEmpty()) {// hay antifichadas, ahora hay que analizarlas
						for (AntiFichada af : antifichadasParaFecha) {//TODO: METER PRESENTISMO, VER CUANDO SE PIERDE Y CUANDO NO
							if (af.getTipoAntiFichada() == ETipoAntiFichada.PARCIAL) {
								AntiFichadaParcial afp = (AntiFichadaParcial) af;
								if (fichadaTO.getEstadoDiaFichada() == EEstadoDiaFichada.NORMAL) {
									fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.getByMotivoAntiFichada(afp.getMotivoAntiFichada()));
								} else {
									if (fichadaTO.getEstadoDiaFichada() == EEstadoDiaFichada.TARDE && !afp.getEntrada()) { // llego tarde y la antifichada es de salida, entonces es tarde y temprano
										fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.TARDE_TEMPRANO);
									} else if (fichadaTO.getEstadoDiaFichada() == EEstadoDiaFichada.RETIRO_TEMPRANO && afp.getEntrada()) { // se retiro antes y la antifichada es de entrada, entonces es
										fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.TEMPRANO_TARDE);
									}
								}
							} else {
								// no deberia haber anti fichadas de vigencia
								// para este dia
							}
						}
						fichadaTO.setJustificada(true);
						// TODO: COMO LLENO LA CAUSA SI HAY VARIAS ANTIFICHADAS?
						// TODO: LLENAR LAS HORAS TRABAJADAS
					} else {
						int horaEntradaFichada = fichadaTO.getHoraEntrada();// DateUtil.getHoras(fichadaTO.getHoraEntrada1());
						int horaEntradaHorario = hd.getRangoHorario().getHoraDesde();

						int horaSalidaFichada = fichadaTO.getHoraSalida(); // fichadaTO.getHoraSalida2()==null?DateUtil.getHoras(fichadaTO.getHoraSalida1()):DateUtil.getHoras(fichadaTO.getHoraSalida2());
						int horaSalidaHorario = hd.getRangoHorario().getHoraHasta();

						int minutosEntradaFichada = fichadaTO.getMinutosEntrada();// DateUtil.getMinutos(fichadaTO.getHoraEntrada1());
						int minutosEntradaHorario = hd.getRangoHorario().getMinutosDesde();

						int minutosSalidaFichada = fichadaTO.getMinutosSalida();// fichadaTO.getHoraSalida2()==null?DateUtil.getMinutos(fichadaTO.getHoraSalida1()):DateUtil.getMinutos(fichadaTO.getHoraSalida2());
						int minutosSalidaHorario = hd.getRangoHorario().getMinutosHasta();

						boolean seFueTemprano = false;
						boolean llegoTarde = false;

						boolean seFueTarde = false;
						boolean llegoTemprano = false;

						Double horasTrabajadas = redondearHoras(getTotalHorasTrabajadas(fichadaTO));
						fichadaTO.setHorasNormales(horasTrabajadas);

						// veo si se fue temprano
						if (horaSalidaFichada < horaSalidaHorario) {
							fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.RETIRO_TEMPRANO);
							fichadaTO.setMinutosParaPresentismo(60-minutosSalidaFichada);
							seFueTemprano = true;
						} else if (horaSalidaFichada == horaSalidaHorario) {
							if (minutosSalidaFichada < minutosSalidaHorario) {
								fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.RETIRO_TEMPRANO);
								fichadaTO.setMinutosParaPresentismo(minutosSalidaHorario-minutosSalidaFichada);
							} else if ((minutosSalidaHorario + toleranciaHorasExtra) < minutosSalidaFichada) {
								// horas extra por trabajar despues de hora
								if(minutosSalidaFichada>=55){
									minutosSalidaFichada = 60;
								}else if(minutosSalidaFichada>=25&&minutosSalidaFichada<=54){
									minutosSalidaFichada = 30;
								}else{
									minutosSalidaFichada =0;
								}
								Double horasExtra = (minutosSalidaFichada - minutosSalidaHorario) / 60d;
								setHorasExtra(fichadaTO, diaSemana, horaSalidaFichada, horasExtra,toleranciaHorasExtra);
								fichadaTO.setHorasNormales(fichadaTO.getHorasNormales()-horasExtra);
								seFueTarde = true;
							}
						} else {// hora salida fichado > hora salida -> Se fue tarde -> horas extra
							Integer minutosExtra = minutosSalidaFichada - minutosSalidaHorario;
							Double horasExtra = horaSalidaFichada - horaSalidaHorario + minutosExtra / 60d;
							setHorasExtra(fichadaTO, diaSemana, horaSalidaFichada, horasExtra,toleranciaHorasExtra);
							fichadaTO.setHorasNormales(fichadaTO.getHorasNormales()-horasExtra);
							seFueTarde = true;
						}

						// veo si llego tarde
						if (horaEntradaFichada > horaEntradaHorario) {
							fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.TARDE);
							if(seFueTemprano){
								fichadaTO.setMinutosParaPresentismo(fichadaTO.getMinutosParaPresentismo()+60-minutosEntradaHorario);
							}else{
								fichadaTO.setMinutosParaPresentismo(60-minutosEntradaHorario);
							}
							llegoTarde = true;
						} else if (horaEntradaFichada == horaEntradaHorario) {
							if (minutosEntradaFichada > minutosEntradaHorario) {
								fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.TARDE);
								if(seFueTemprano){
									fichadaTO.setMinutosParaPresentismo(fichadaTO.getMinutosParaPresentismo()+minutosEntradaFichada-minutosEntradaHorario);
								}else{
									fichadaTO.setMinutosParaPresentismo(minutosEntradaFichada-minutosEntradaHorario);
								}
								llegoTarde = true;
							} else {
								if ((minutosEntradaFichada + toleranciaHorasExtra) < minutosEntradaHorario) {
									// horas extra por llegar antes
									if(minutosEntradaHorario>=55){
										minutosEntradaHorario = 60;
									}else if(minutosEntradaHorario>=25&&minutosSalidaFichada<=54){
										minutosEntradaHorario = 30;
									}else{
										minutosEntradaHorario =0;
									}
									Double horasExtra = (minutosEntradaHorario - minutosEntradaFichada) / 60d;
									setHorasExtra(fichadaTO, diaSemana, horaSalidaFichada, horasExtra, toleranciaHorasExtra);
									fichadaTO.setHorasNormales(fichadaTO.getHorasNormales()-horasExtra);
									llegoTemprano = true;
								}
							}
						} else {// horaEntradaFichada < horaEntradaHorario -> horas extra por llegar antes
							Integer minutosExtra = minutosEntradaHorario - minutosEntradaFichada;
							Double horasExtra = horaEntradaHorario - horaEntradaFichada + minutosExtra / 60d;
							setHorasExtra(fichadaTO, diaSemana, horaSalidaFichada, horasExtra,toleranciaHorasExtra);
							fichadaTO.setHorasNormales(fichadaTO.getHorasNormales()-horasExtra);
							llegoTemprano = true;
						}
						if (llegoTarde && seFueTemprano) {
							fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.TARDE_TEMPRANO);
						}
						if (llegoTemprano && seFueTarde) {
							fichadaTO.setEstadoDiaFichada(EEstadoDiaFichada.TEMPRANO_TARDE);
						}
						fichadaTO.setHorasNormales(redondearHoras(fichadaTO.getHorasNormales()));
					}
				}
			} else { // VINO CUANDO NO LE CORRESPONDIA VENIR
				if (diaSemana == 7) { // SABADO -> SON AL 50% EN ESTE CASO
					Double horasTrabajadas = getTotalHorasTrabajadas(fichadaTO);
					fichadaTO.setHorasExtrasAl50(fichadaTO.getHorasExtrasAl50() + horasTrabajadas);
				}
			}
		}
	}

	private Double getTotalHorasTrabajadas(FichadaLegajoTO fichadaTO) {
		long milisTrabajadosTotal = 0;
		for (GrupoHoraEntradaSalidaTO g : fichadaTO.getGruposEntradaSalida()) {
			milisTrabajadosTotal += g.getDiferenciaHorario();
		}
		Double horasTrabajadas = milisTrabajadosTotal / 1000d / 3600;
		return horasTrabajadas;
	}

	private void setHorasExtra(FichadaLegajoTO fichadaTO, int diaSemana, int horaSalidaFichada, Double horasExtra, Integer toleranciaHorasExtra) {
		horasExtra = redondearHoras(horasExtra);
		if (diaSemana == 7) { // SABADO
			if (horaSalidaFichada >= 13+toleranciaHorasExtra) { // despues de las 13
				fichadaTO.setHorasExtrasAl100(fichadaTO.getHorasExtrasAl100() + horasExtra);
			}
		} else if (diaSemana == 1) {// DOMINGO
			fichadaTO.setHorasExtrasAl100(fichadaTO.getHorasExtrasAl100() + horasExtra);
		} else {// OTRO DIA DE LA SEMANA
			fichadaTO.setHorasExtrasAl50(fichadaTO.getHorasExtrasAl50() + horasExtra);
		}
	}

	private Double redondearHoras(Double horasExtra) {
		Double copiaHaciaArriba = Math.ceil(horasExtra);
		Double diferencia = copiaHaciaArriba - horasExtra;
		return diferencia == 0.5 ? horasExtra : diferencia < 0.5 ? copiaHaciaArriba : Math.floor(horasExtra);
	}

	private void setearHorarios(FichadaLegajoTO fichadaTO, FichadaLegajo fl) {
		if (fl.getTipoFichada() == ETipoFichada.ENTRADA) {
			if (fichadaTO.getGruposEntradaSalida().isEmpty()) {
				GrupoHoraEntradaSalidaTO grupoNuevo = new GrupoHoraEntradaSalidaTO();
				grupoNuevo.setHoraEntrada(fl.getHorario());
				fichadaTO.getGruposEntradaSalida().add(grupoNuevo);
			} else {
				boolean encontre = false;
				for (GrupoHoraEntradaSalidaTO g : fichadaTO.getGruposEntradaSalida()) {
					if (g.getHoraEntrada() == null) {
						g.setHoraEntrada(fl.getHorario());
						encontre = true;
						break;
					}
				}
				if (!encontre) {
					GrupoHoraEntradaSalidaTO grupoNuevo = new GrupoHoraEntradaSalidaTO();
					grupoNuevo.setHoraEntrada(fl.getHorario());
					fichadaTO.getGruposEntradaSalida().add(grupoNuevo);
				}
			}
			fichadaTO.setDia(DateUtil.redondearFecha(fl.getHorario()));
		} else {
			if (fichadaTO.getGruposEntradaSalida().isEmpty()) {
				GrupoHoraEntradaSalidaTO grupoNuevo = new GrupoHoraEntradaSalidaTO();
				grupoNuevo.setHoraSalida(fl.getHorario());
				fichadaTO.getGruposEntradaSalida().add(grupoNuevo);
			} else {
				boolean encontre = false;
				for (GrupoHoraEntradaSalidaTO g : fichadaTO.getGruposEntradaSalida()) {
					if (g.getHoraSalida() == null) {
						g.setHoraSalida(fl.getHorario());
						encontre = true;
						break;
					}
				}
				if (!encontre) {
					GrupoHoraEntradaSalidaTO grupoNuevo = new GrupoHoraEntradaSalidaTO();
					grupoNuevo.setHoraEntrada(fl.getHorario());
					fichadaTO.getGruposEntradaSalida().add(grupoNuevo);
				}
			}
			fichadaTO.setDia(DateUtil.redondearFecha(fl.getHorario()));
		}
	}

	private List<FichadaLegajoTO> completarDiasFaltantes(Map<ContenedorFechasKey, FichadaLegajoTO> fichadasCompletadas, Date fechaDesde, Date fechaHasta, List<HorarioDia> horario,
			CalendarioAnualFeriado calendarioDesde, CalendarioAnualFeriado calendarioHasta, LegajoEmpleado legajo) {
		List<FichadaLegajoTO> listaRet = new ArrayList<FichadaLegajoTO>();
		Date desde = fechaDesde == null ? getFechaMasLejana(fichadasCompletadas) : fechaDesde;
		Date hasta = fechaHasta == null ? getMasCercana(fichadasCompletadas) : fechaHasta;
		Date indiceFechaActual = desde;
		Date hoy = DateUtil.getHoy();
		ContenedorFechasKey contenedorTemp = new ContenedorFechasKey(indiceFechaActual, DateUtil.dateToString(indiceFechaActual));
		while (indiceFechaActual.compareTo(hasta) <= 0 && indiceFechaActual.compareTo(hoy) <= 0) {
			if (fichadasCompletadas.get(contenedorTemp) == null) {// TODO en esa fecha no hay fichadas => pierde presentismo!! ver como es el tema
				FichadaLegajoTO fNueva = new FichadaLegajoTO();
				// EN ESTA PARTE ANALIZO LAS CAUSAS POR LAS QUE NO VINO
				analizarCausasFalta(horario, calendarioDesde, calendarioHasta, legajo, indiceFechaActual, fNueva);
				fNueva.setDia(indiceFechaActual);
				listaRet.add(fNueva);
			} else {
				listaRet.add(fichadasCompletadas.get(contenedorTemp));
			}
			indiceFechaActual = DateUtil.sumarDias(indiceFechaActual, 1);
			contenedorTemp = new ContenedorFechasKey(indiceFechaActual, DateUtil.dateToString(indiceFechaActual));
		}

		CollectionUtils.forAllDo(listaRet, new Closure() {

			public void execute(Object arg0) {
				FichadaLegajoTO fichada = (FichadaLegajoTO) arg0;
				fichada.setStrDia(armarTextoDia(fichada));
			}

			private String armarTextoDia(FichadaLegajoTO fichada) {
				return DateUtil.DIAS_SEMANA[DateUtil.getDiaSemana(fichada.getDia()) - 1] + " " + DateUtil.getDia(fichada.getDia()) + " de " + DateUtil.MESES[DateUtil.getMes(fichada.getDia())];
			}
		});
		return listaRet;
	}

	private void analizarCausasFalta(List<HorarioDia> horario, CalendarioAnualFeriado calendarioDesde, CalendarioAnualFeriado calendarioHasta, LegajoEmpleado legajo, Date fechaActual,
			FichadaLegajoTO fNueva) {
		
		if (estaDentroDeLosDiasDeTrabajo(fechaActual, horario)) { // me fijo si en esa fecha le correspondia laburar. Si no le corresponde, no pasa nada
			if (!esFeriado(fechaActual, calendarioDesde, calendarioHasta)) {// no hay fichadas y le correspondia, veo si era feriado

				//TODO: VER LICENCIA POR ENFERMEDAD Y DEMAS, IMPLEMENTAR
				// if(!estaDeLicencia(legajo)){ //veo si esta con licencia
				// fNueva.setEstadoDiaFichada(EEstadoDiaFichada.FALTA); //no vino esta dentro de su horario, no era feriado y no estaba de licencia-> falto
				// }else{
				// fNueva.setEstadoDiaFichada(EEstadoDiaFichada.LICENCIA);
				// }

				// ya pase los casos simples de verificar, ahora hay que ver las faltas con las antifichadas
				List<AntiFichada> antifichadasParaFecha = antiFichadasFacade.getAntifichadasParaFecha(fechaActual, legajo);
				if (!antifichadasParaFecha.isEmpty()) {// hay antifichadas, ahora hay que analizarlas
					for (AntiFichada af : antifichadasParaFecha) { // deberia haber una sola, porque deberian ser todas de vigencia, nunca parciales
						if (af.getTipoAntiFichada() == ETipoAntiFichada.VIGENCIA) {
							fNueva.setEstadoDiaFichada(EEstadoDiaFichada.getByMotivoAntiFichada(af.getMotivoAntiFichada()));
						} else {
							// TODO: NO VA A PASAR, EL EMPLEADO NO VINO, POR
							// ENDE, NO DEBERIA TENER ANTIFICHADAS PARCIALES
						}
					}
				} else {
					fNueva.setEstadoDiaFichada(EEstadoDiaFichada.FALTA);
				}
			} else {
				fNueva.setEstadoDiaFichada(EEstadoDiaFichada.FERIADO);
			}
		} else {
			if (esFeriado(fechaActual, calendarioDesde, calendarioHasta)) {// FERIADO
				fNueva.setEstadoDiaFichada(EEstadoDiaFichada.FERIADO);
			} else if(DateUtil.getDiaSemana(fechaActual) == 1) { // DOMINGO
				fNueva.setEstadoDiaFichada(EEstadoDiaFichada.DOMINGO);
			}
		}
	}

	private boolean esFeriado(Date indiceFechaActual, CalendarioAnualFeriado calendarioDesde, CalendarioAnualFeriado calendarioHasta) {
		boolean feriado = false;
		if (calendarioDesde != null) {
			feriado = chequearFechaDentroDelCalendarioDeFeriados(indiceFechaActual, calendarioDesde, feriado);
		}
		if (calendarioHasta != null) {
			feriado = chequearFechaDentroDelCalendarioDeFeriados(indiceFechaActual, calendarioHasta, feriado);
		}
		return feriado;
	}

	private boolean chequearFechaDentroDelCalendarioDeFeriados(Date indiceFechaActual, CalendarioAnualFeriado calendarioDesde, boolean feriado) {
		List<RangoDiasFeriado> feriados = calendarioDesde.getFeriados();
		for (RangoDiasFeriado rdf : feriados) {
			if (DateUtil.isBetween(rdf.getDesde(), rdf.getHasta(), indiceFechaActual) || indiceFechaActual.equals(rdf.getDesde()) || indiceFechaActual.equals(rdf.getHasta())) {
				feriado |= true;
			}
		}
		return feriado;
	}

	private boolean estaDentroDeLosDiasDeTrabajo(Date indiceFechaActual, List<HorarioDia> horario) {
		int diaSemana = DateUtil.getDiaSemana(indiceFechaActual);
		for (HorarioDia hd : horario) {
			if ((hd.getRangoDias().getDiaDesde().getNroDia() + 1) <= diaSemana && (hd.getRangoDias().getDiaHasta().getNroDia() + 1) >= diaSemana) {
				return true;
			}
		}
		return false;
	}

	private Date getMasCercana(Map<ContenedorFechasKey, FichadaLegajoTO> fichadasCompletadas) {
		Date fechaRet = null;
		for (ContenedorFechasKey contenedor : fichadasCompletadas.keySet()) {
			if (fechaRet == null) {
				fechaRet = contenedor.getFecha();
				continue;
			}
			if (contenedor.getFecha().compareTo(fechaRet) > 0) {
				fechaRet = contenedor.getFecha();
			}
		}
		return fechaRet;
	}

	private Date getFechaMasLejana(Map<ContenedorFechasKey, FichadaLegajoTO> fichadasCompletadas) {
		Date fechaRet = null;
		for (ContenedorFechasKey contenedor : fichadasCompletadas.keySet()) {
			if (fechaRet == null) {
				fechaRet = contenedor.getFecha();
				continue;
			}
			if (contenedor.getFecha().compareTo(fechaRet) < 0) {
				fechaRet = contenedor.getFecha();
			}
		}
		return fechaRet;
	}

	public FichadaLegajo save(FichadaLegajo fichada) {
		return fichadaDao.save(fichada);
	}

	public void remove(FichadaLegajo fichada) {
		fichadaDao.removeById(fichada.getId());
	}

	public Timestamp getFechaHoraUltimaFichada() {
		return fichadaDao.getFechaHoraUltimaFichada();
	}

	public class ContenedorFechasKey implements Serializable {

		private static final long serialVersionUID = -3547612046263579788L;

		private Date fecha;
		private String fechaStr;

		public ContenedorFechasKey(Date fecha, String fechaStr) {
			setFecha(fecha);
			setFechaStr(fechaStr);
		}

		public Date getFecha() {
			return fecha;
		}

		public void setFecha(Date fecha) {
			this.fecha = fecha;
		}

		public String getFechaStr() {
			return fechaStr;
		}

		public void setFechaStr(String fechaStr) {
			this.fechaStr = fechaStr;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((fechaStr == null) ? 0 : fechaStr.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ContenedorFechasKey other = (ContenedorFechasKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (fechaStr == null) {
				if (other.fechaStr != null)
					return false;
			} else if (!fechaStr.equals(other.fechaStr))
				return false;
			return true;
		}

		private FichadaLegajoFacade getOuterType() {
			return FichadaLegajoFacade.this;
		}
	}
	
	public class ContadorFaltas {

		private Integer contador;

		public ContadorFaltas() {
			contador = 0;
		}

		public Integer getContador() {
			return contador;
		}

		public void setContador(Integer contador) {
			this.contador = contador;
		}
		
		public void aumentar(Integer cantidad){
			setContador(getContador()+1);
		}

		@Override
		public String toString() {
			return ""+contador;
		}
	}
}
