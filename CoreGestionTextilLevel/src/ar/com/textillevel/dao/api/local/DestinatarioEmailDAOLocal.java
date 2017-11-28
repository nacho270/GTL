package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;
import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.DestinatarioEmail;

@Local
public interface DestinatarioEmailDAOLocal extends DAOLocal<DestinatarioEmail, String> {
	
}
