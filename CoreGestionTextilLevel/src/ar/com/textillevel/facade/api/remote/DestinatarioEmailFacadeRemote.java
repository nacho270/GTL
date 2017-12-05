package ar.com.textillevel.facade.api.remote;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import ar.com.textillevel.entidades.documentos.DestinatarioEmail;
import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;

@Remote
public interface DestinatarioEmailFacadeRemote {

	public List<DestinatarioEmail> getAllByEntidad(Integer idEntidad, ETipoBusquedaAgenda tipoEntidad);

	public void persistContactsSiNoExisten(Set<String> contacts, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad);

}
