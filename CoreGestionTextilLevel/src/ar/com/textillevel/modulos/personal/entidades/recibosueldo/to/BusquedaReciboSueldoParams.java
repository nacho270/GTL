package ar.com.textillevel.modulos.personal.entidades.recibosueldo.to;

import java.io.Serializable;
import ar.clarin.fwjava.entidades.Mes;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EQuincena;

public class BusquedaReciboSueldoParams implements Serializable {

	private static final long serialVersionUID = 4482026562708802191L;

	private Sindicato sindicato;
	private Integer anio;
	private Mes mes;
	private EQuincena quincena;
	private Integer nroLegajo;
	private String nombreOrApellido;

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
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

	public EQuincena getQuincena() {
		return quincena;
	}

	public void setQuincena(EQuincena quincena) {
		this.quincena = quincena;
	}

	public Integer getNroLegajo() {
		return nroLegajo;
	}

	public void setNroLegajo(Integer nroLegajo) {
		this.nroLegajo = nroLegajo;
	}

	public String getNombreOrApellido() {
		return nombreOrApellido;
	}

	public void setNombreOrApellido(String nombreOrApellido) {
		this.nombreOrApellido = nombreOrApellido;
	}

}
