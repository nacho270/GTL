package ar.com.textillevel.facade.api.remote;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import ar.com.textillevel.entidades.documentos.DestinatarioEmail;

@Remote
public interface DestinatarioEmailFacadeRemote {

	public List<DestinatarioEmail> getAll();

	public void persistContactsSiNoExisten(Set<String> contacts);

}
