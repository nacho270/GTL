package ar.com.textillevel.facade.impl;

import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ar.com.textillevel.dao.api.local.DestinatarioEmailDAOLocal;
import ar.com.textillevel.entidades.documentos.DestinatarioEmail;
import ar.com.textillevel.facade.api.remote.DestinatarioEmailFacadeRemote;

@Stateless
public class DestinatarioEmailFacade implements DestinatarioEmailFacadeRemote {

	@EJB
	private DestinatarioEmailDAOLocal deDAOLocal;

	@Override
	public List<DestinatarioEmail> getAll() {
		return deDAOLocal.getAllOrderBy("email");
	}

	@Override
	public void persistContactsSiNoExisten(Set<String> contacts) {
		for(String c : contacts) {
			DestinatarioEmail de = deDAOLocal.getById(c.trim().toLowerCase());
			if(de == null) {//persist!
				DestinatarioEmail deNew = new DestinatarioEmail();
				deNew.setEmail(c);
				deDAOLocal.save(deNew);
			}
		}
	}

}
