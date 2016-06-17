package ar.com.textillevel.modulos.odt.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

@Local
public interface MaquinaDAOLocal extends DAOLocal<Maquina, Integer> {

	public List<Maquina> getAllByTipo(TipoMaquina tipoMaquina);

	public List<Maquina> getAllByIdTipoMaquina(Integer idTipoMaquina);
	
	public List<Maquina> getAllSorted();

	public boolean existsNombreByTipoMaquina(Maquina maquina);

	public Maquina getByIdEager(Integer id);

	public List<Maquina> getAllBySector(ESectorMaquina sector);

}
