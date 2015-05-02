package ar.com.textillevel.modulos.odt.dao.api.local;

import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TerminacionFraccionado;

@Local
public interface TerminacionFraccionadoDAOLocal extends DAOLocal<TerminacionFraccionado, Integer> {

	public boolean existsTerminacionByNombre(TerminacionFraccionado terminacion);

}
