package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;

@Local
public interface TipoAnilinaDAOLocal extends DAOLocal<TipoAnilina, Integer>{

	public TipoAnilina getByIdEager(Integer id);

}
