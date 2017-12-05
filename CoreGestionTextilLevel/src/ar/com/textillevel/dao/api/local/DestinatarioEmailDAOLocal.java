package ar.com.textillevel.dao.api.local;

import java.util.List;
import javax.ejb.Local;
import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.DestinatarioEmail;
import ar.com.textillevel.entidades.enums.ETipoBusquedaAgenda;

@Local
public interface DestinatarioEmailDAOLocal extends DAOLocal<DestinatarioEmail, String> {

	public DestinatarioEmail getByParams(String lowerCase, Integer idEntidad, ETipoBusquedaAgenda tipoEntidad);

	public List<DestinatarioEmail> getAllByEntidad(Integer idEntidad, ETipoBusquedaAgenda tipoEntidad);

}
