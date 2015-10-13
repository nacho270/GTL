package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;
import ar.com.textillevel.entidades.gente.Persona;

@Local
public interface FacturaPersonaDAOLocal extends DAOLocal<FacturaPersona, Integer>{
	public boolean existeNroFacturaParaPersona(Integer nroFactura, Persona persona);
}
