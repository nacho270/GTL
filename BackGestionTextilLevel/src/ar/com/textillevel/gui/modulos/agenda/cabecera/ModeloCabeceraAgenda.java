package ar.com.textillevel.gui.modulos.agenda.cabecera;

import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;
import ar.com.textillevel.entidades.gente.Rubro;

public class ModeloCabeceraAgenda {
	private String criterioBusqueda;
	private ETipoBusquedaAgenda tipoBusqueda;
	private Rubro rubroPersona;
	
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	public ETipoBusquedaAgenda getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(ETipoBusquedaAgenda tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public Rubro getRubroPersona() {
		return rubroPersona;
	}

	public void setRubroPersona(Rubro rubroPersona) {
		this.rubroPersona = rubroPersona;
	}
}
