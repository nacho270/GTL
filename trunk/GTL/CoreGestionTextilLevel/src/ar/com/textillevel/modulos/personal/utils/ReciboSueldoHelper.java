package ar.com.textillevel.modulos.personal.utils;

import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EQuincena;

public class ReciboSueldoHelper {

	private static final int TOTAL_MESES = 12;
	private static final int TOTAL_QUINCENAS = 2;

	private static ReciboSueldoHelper instance = new ReciboSueldoHelper();

	private ReciboSueldoHelper() {
	}

	public static ReciboSueldoHelper getInstance() {
		return instance;
	}

	public String calcularPeriodoRS(ReciboSueldo reciboSueldo) {
		String anio = reciboSueldo.getAnio().toString();
		String mes = reciboSueldo.getMes().getNombre();
		String quincena = "";
		if(reciboSueldo.getQuincena() != null) {
			quincena = reciboSueldo.getQuincena().equals(EQuincena.PRIMERA.getId()) ? "PRIMER QUINCENA" : "SEGUNDA QUINCENA";
		}
		return quincena + " " + mes + " de " + anio;
	}

	public String calcularTextoOrdenRS(ReciboSueldo reciboSueldo) {
		StringBuilder textoOrden = new StringBuilder();
		textoOrden.append(reciboSueldo.getAnio().toString())
				  .append(reciboSueldo.getMes().getNroMes())
				  .append(reciboSueldo.getQuincena() == null ? "" : reciboSueldo.getQuincena().toString());
		return textoOrden.toString();
	}

	public String getTextoOrdenSiguiente(ReciboSueldo reciboSueldo) {
		StringBuilder textoOrden = new StringBuilder();
		int anio = reciboSueldo.getMes().getNroMes() == TOTAL_MESES ? reciboSueldo.getAnio() + 1 : reciboSueldo.getAnio();
		int mes = reciboSueldo.getMes().getNroMes() == TOTAL_MESES ? 1 : reciboSueldo.getMes().getNroMes() + 1;
		int quincena = -1;
		if(reciboSueldo.getQuincena() != null) {
			quincena = reciboSueldo.getQuincena().getId() == TOTAL_QUINCENAS ? 1 : TOTAL_QUINCENAS;
		}
		textoOrden.append(anio)
				  .append(mes)
				  .append(quincena == -1 ? "" : quincena);
		return textoOrden.toString();
	}

	public String getTextoOrdenAnterior(ReciboSueldo reciboSueldo) {
		StringBuilder textoOrden = new StringBuilder();
		int anio = reciboSueldo.getMes().getNroMes() == 1 ? reciboSueldo.getAnio() - 1 : reciboSueldo.getAnio();
		int mes = reciboSueldo.getMes().getNroMes() == 1 ? TOTAL_MESES : reciboSueldo.getMes().getNroMes() - 1;
		int quincena = -1;
		if(reciboSueldo.getQuincena() != null) {
			quincena = reciboSueldo.getQuincena().getId() == 1 ? TOTAL_QUINCENAS : 1;
		}
		textoOrden.append(anio)
				  .append(mes)
				  .append(quincena == -1 ? "" : quincena);
		return textoOrden.toString();
	}

}