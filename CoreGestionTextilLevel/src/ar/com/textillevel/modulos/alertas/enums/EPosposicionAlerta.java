package ar.com.textillevel.modulos.alertas.enums;

import java.sql.Timestamp;

import ar.com.fwcommon.util.DateUtil;

public enum EPosposicionAlerta {
	
	NADA("No realizar acción", new CalculadorPosposicion() {
		public Timestamp calcularPosposicion(Timestamp fechaAlerta) {
			return fechaAlerta;
		}
	}), 
	NUNCA("No volver a recordar", new CalculadorPosposicion() {
		public Timestamp calcularPosposicion(Timestamp fechaAlerta) {
			return null;
		}
	}),
	MINUTOS_10("10 Minutos",new CalculadorMinutos(10)),
	MINUTOS_30("10 Minutos",new CalculadorMinutos(30)),
	
	HORAS_1("10 Minutos",new CalculadorHoras(1)),
	HORAS_2("10 Minutos",new CalculadorHoras(2)),
	HORAS_3("10 Minutos",new CalculadorHoras(3)),
	HORAS_4("10 Minutos",new CalculadorHoras(4)),
	HORAS_5("10 Minutos",new CalculadorHoras(5)),
	HORAS_6("10 Minutos",new CalculadorHoras(6)),
	HORAS_7("10 Minutos",new CalculadorHoras(7)),
	HORAS_8("10 Minutos",new CalculadorHoras(8)),
	
	UN_DIA("Mañana", new CalculadorDias(1)),
	DOS_DIAS("Pasado mañana", new CalculadorDias(2)), 
	UNA_SEMANA("Una semana",  new CalculadorDias(7)), 
	UN_MES("Un mes",  new CalculadorDias(30));

	private EPosposicionAlerta(String descripcion, CalculadorPosposicion calculador) {
		this.descripcion = descripcion;
		this.calculador = calculador;
	}

	private String descripcion;
	private CalculadorPosposicion calculador;

	public String getDescripcion() {
		return descripcion;
	}

	@Override
	public String toString() {
		return descripcion;
	}

	public Timestamp getFechaPosposicion(Timestamp fechaAlerta) {
		return getCalculador().calcularPosposicion(fechaAlerta);
	}

	private CalculadorPosposicion getCalculador() {
		return calculador;
	}
	
	private static interface CalculadorPosposicion{
		public abstract Timestamp calcularPosposicion(Timestamp fechaAlerta);
	}

	private static class CalculadorMinutos implements CalculadorPosposicion {

		private final int minutos;
		
		public CalculadorMinutos(int minutos){
			this.minutos = minutos;
		}
		
		public Timestamp calcularPosposicion(Timestamp fechaAlerta) {
			return DateUtil.sumarMinutos(fechaAlerta==null?DateUtil.getAhora():fechaAlerta,minutos);
		}
	}
	
	private static class CalculadorDias implements CalculadorPosposicion {

		private final int dias;
		
		public CalculadorDias(int dias){
			this.dias = dias;
		}
		
		public Timestamp calcularPosposicion(Timestamp fechaAlerta) {
			return DateUtil.sumarDias(fechaAlerta==null?DateUtil.getAhora():fechaAlerta,dias);
		}
	}
	
	private static class CalculadorHoras implements CalculadorPosposicion {
		
		private final int horas;
		
		public CalculadorHoras(int horas){
			this.horas = horas;
		}

		public Timestamp calcularPosposicion(Timestamp fechaAlerta) {
			return DateUtil.sumarHoras(fechaAlerta==null?DateUtil.getAhora():fechaAlerta,horas);
		}
	}
}