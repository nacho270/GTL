package ar.com.textillevel.gui.modulos.personal.modulos.legajos.cabecera;

import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.enums.ETipoBusquedaEmpleados;

public class ModeloCabeceraLegajos {

	private ETipoBusquedaEmpleados modoBusqueda;
	private ETipoContrato tipoContrato;
	private Integer nroLegajo;
	private Sindicato sindicato;
	private String nombreOApellido;

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

	public ETipoBusquedaEmpleados getModoBusqueda() {
		return modoBusqueda;
	}

	public void setModoBusqueda(ETipoBusquedaEmpleados modoBusqueda) {
		this.modoBusqueda = modoBusqueda;
	}

	public String getNombreOApellido() {
		return nombreOApellido;
	}

	public void setNombreOApellido(String nombreOApellido) {
		this.nombreOApellido = nombreOApellido;
	}

	
	public ETipoContrato getTipoContrato() {
		return tipoContrato;
	}

	
	public void setTipoContrato(ETipoContrato tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
}
