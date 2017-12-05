package ar.com.textillevel.facade.impl;

import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ar.com.textillevel.dao.api.local.DestinatarioEmailDAOLocal;
import ar.com.textillevel.entidades.documentos.DestinatarioEmail;
import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;
import ar.com.textillevel.facade.api.remote.DestinatarioEmailFacadeRemote;

@Stateless
public class DestinatarioEmailFacade implements DestinatarioEmailFacadeRemote {

	@EJB
	private DestinatarioEmailDAOLocal deDAOLocal;

	@Override
	public List<DestinatarioEmail> getAllByEntidad(Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) {
		return deDAOLocal.getAllByEntidad(idEntidad, tipoEntidad);
	}

	@Override
	public void persistContactsSiNoExisten(Set<String> contacts, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad) {
		for(String c : contacts) {
			DestinatarioEmail de = deDAOLocal.getByParams(c.trim().toLowerCase(), idEntidad, tipoEntidad);
			if(de == null) {//persist!
				DestinatarioEmail deNew = new DestinatarioEmail();
				deNew.setEmail(c);
				deNew.setIdEntidad(idEntidad);
				deNew.setTipoEntidad(tipoEntidad);
				deDAOLocal.save(deNew);
			}
		}
	}

}
