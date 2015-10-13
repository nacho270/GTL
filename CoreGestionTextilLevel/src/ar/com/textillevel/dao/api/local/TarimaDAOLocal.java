package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.remito.Tarima;

@Local
public interface TarimaDAOLocal extends DAOLocal<Tarima, Integer> {

	public List<Tarima> getAllSorted();

	public boolean existsTarima(Tarima tarima);
	
}
