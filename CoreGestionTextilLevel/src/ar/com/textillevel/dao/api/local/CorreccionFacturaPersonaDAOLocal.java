package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.pagopersona.CorreccionFacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.NotaDebitoPersona;
import ar.com.textillevel.entidades.gente.Persona;

@Local
public interface CorreccionFacturaPersonaDAOLocal extends DAOLocal<CorreccionFacturaPersona, Integer> {

	public NotaDebitoPersona getNDByIdEager(Integer idND);

	public boolean existeNroNDParaPersona(Integer nroND, Persona persona);

}
