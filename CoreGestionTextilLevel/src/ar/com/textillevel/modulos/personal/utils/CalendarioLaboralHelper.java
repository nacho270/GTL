package ar.com.textillevel.modulos.personal.utils;

import java.math.BigDecimal;
import java.sql.Date;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.ConfigFormaPagoSindicato;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.RangoDiasFeriado;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.TotalHorasPagoDia;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

public class CalendarioLaboralHelper {

	public static Date calcularPrimerFechaHabil(CalendarioAnualFeriado calendario, Date fechaReferencia, int offset) {
		int offsetRecorrido = 1;
		Date fechaActual = new Date(fechaReferencia.getTime());
		int signum = offset >= 0 ? 1 : -1;
		while(offsetRecorrido < Math.abs(offset)) {
			fechaActual = new Date(signum*DateUtil.ONE_DAY + fechaActual.getTime());
			if(!isDomingoOrFeriado(calendario, fechaActual)) {
				offsetRecorrido ++;
			}
		}
		return fechaActual;
	}

	public static Integer getCantidadDiasHabiles(CalendarioAnualFeriado calendario, Date fechaDesde, Date fechaHasta) {
		Integer diasHabiles = 0;
		Date fechaActual = new Date(fechaDesde.getTime());
		while(!fechaActual.after(fechaHasta)) {
			if(!isDomingoOrFeriado(calendario, fechaActual)) {
				diasHabiles ++;
			}
			fechaActual = new Date(DateUtil.ONE_DAY + fechaActual.getTime());
		}
		return diasHabiles;
	}

	private static boolean isDomingoOrFeriado(CalendarioAnualFeriado calendario, Date fecha) {
		int diaSemana = DateUtil.getDiaSemana(fecha);
		if(diaSemana == 1) {//Es domingo
			return true;
		}
		if(calendario != null) {
			return calendario.getRangoEnFecha(fecha) != null;
		}
		return false;
	}

	//TODO: Mejorar esto. Los métodos son casi iguales!!!!
	public static BigDecimal getCantidadHorasFeriadoBySindicato(CalendarioAnualFeriado calendario, Date dia, Sindicato sindicato) {
		RangoDiasFeriado feriado = calendario.getRangoEnFecha(dia);
		if(feriado != null) { //El día tiene que estar configurado como feriado
			if(feriado.getConfigsFormasPagoSindicatos().isEmpty()) { //uso la configuración default
				for(ConfigFormaPagoSindicato c : calendario.getConfigsFormasPagoSindicatos()) {
					if(c.getSindicato().equals(sindicato)) {
						TotalHorasPagoDia defaultTotalHoras = null; 
						for(TotalHorasPagoDia t : c.getTotalHorasPagoPorDias()) {
							if(t.getDia() == null) {
								defaultTotalHoras = t;
							} else if(DateUtil.getDiaSemana(dia) == t.getDia().getNroDia()) {
								return new BigDecimal(t.getTotalHoras());
							}
						}
						if(defaultTotalHoras != null) {
							return new BigDecimal(defaultTotalHoras.getTotalHoras());
						}
					}
				}
			} else {
				for(ConfigFormaPagoSindicato c : feriado.getConfigsFormasPagoSindicatos()) {
					if(c.getSindicato().equals(sindicato)) {
						return new BigDecimal(c.getTotalHorasPagoPorDias().get(0).getTotalHoras());
					}
				}
			}
		}
		return null;
	}

	public static boolean discriminaEnRS(CalendarioAnualFeriado calendario, Date dia, Sindicato sindicato) {
		RangoDiasFeriado feriado = calendario.getRangoEnFecha(dia);
		if(feriado != null) { //El día tiene que estar configurado como feriado
			if(feriado.getConfigsFormasPagoSindicatos().isEmpty()) { //uso la configuración default
				for(ConfigFormaPagoSindicato c : calendario.getConfigsFormasPagoSindicatos()) {
					if(c.getSindicato().equals(sindicato)) {
						TotalHorasPagoDia defaultTotalHoras = null; 
						for(TotalHorasPagoDia t : c.getTotalHorasPagoPorDias()) {
							if(t.getDia() == null) {
								defaultTotalHoras = t;
							} else if(DateUtil.getDiaSemana(dia) == t.getDia().getNroDia()) {
								return t.isDiscriminaEnRS();
							}
						}
						if(defaultTotalHoras != null) {
							return defaultTotalHoras.isDiscriminaEnRS();
						}
					}
				}
			} else {
				for(ConfigFormaPagoSindicato c : feriado.getConfigsFormasPagoSindicatos()) {
					if(c.getSindicato().equals(sindicato)) {
						return c.getTotalHorasPagoPorDias().get(0).isDiscriminaEnRS();
					}
				}
			}
		}
		return false;
	}
	
}