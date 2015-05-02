package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;
import ar.com.textillevel.entidades.gente.IAgendable;
import ar.com.textillevel.entidades.gente.Rubro;

@Remote
public interface AgendaFacadeRemote {
	public List<IAgendable> buscar(String criterio,ETipoBusquedaAgenda tipoBusqueda, Rubro rubroPersona) throws CLException;
}
