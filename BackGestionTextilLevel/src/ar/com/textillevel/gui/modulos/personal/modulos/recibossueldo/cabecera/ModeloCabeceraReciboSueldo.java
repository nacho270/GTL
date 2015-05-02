package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.cabecera;

import ar.clarin.fwjava.entidades.Mes;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EQuincena;

public class ModeloCabeceraReciboSueldo {

	private Integer nroLegajo;
	private Sindicato sindicato;
	private String nombreOApellido;
	private EQuincena quincena;
	private Integer anio;
	private Mes mes;

	public Integer getNroLegajo() {
		return nroLegajo;
	}

	public void setNroLegajo(Integer nroLegajo) {
		this.nroLegajo = nroLegajo;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public String getNombreOApellido() {
		return nombreOApellido;
	}

	public void setNombreOApellido(String nombreOApellido) {
		this.nombreOApellido = nombreOApellido;
	}

	public EQuincena getQuincena() {
		return quincena;
	}

	public void setQuincena(EQuincena quincena) {
		this.quincena = quincena;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Mes getMes() {
		return mes;
	}

	public void setMes(Mes mes) {
		this.mes = mes;
	}

}