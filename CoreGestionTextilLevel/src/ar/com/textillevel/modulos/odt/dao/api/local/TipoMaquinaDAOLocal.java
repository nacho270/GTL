package ar.com.textillevel.modulos.odt.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;

@Local
public interface TipoMaquinaDAOLocal extends DAOLocal<TipoMaquina, Integer>{

	public boolean existsByNombre(TipoMaquina tipoMaquina);

	public boolean existsByOrden(TipoMaquina tipoMaquina);

	public List<TipoMaquina> getAllByIdTipo(Integer idTipoMaquina);

	public TipoMaquina getTipoMaquinaConOrdenMayor();

//	public TipoMaquina getByIdEager(Integer idTipoMaquina);
//	public TipoMaquina getByIdSuperEager(Integer idTipoMaquina);

}
