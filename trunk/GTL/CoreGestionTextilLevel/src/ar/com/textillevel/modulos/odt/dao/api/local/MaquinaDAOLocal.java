package ar.com.textillevel.modulos.odt.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;

@Local
public interface MaquinaDAOLocal extends DAOLocal<Maquina, Integer> {

	public List<Maquina> getAllByTipo(TipoMaquina tipoMaquina);

	public List<Maquina> getAllByIdTipoMaquina(Integer idTipoMaquina);
	
	public List<Maquina> getAllSorted();

	public boolean existsNombreByTipoMaquina(Maquina maquina);

	public Maquina getByIdEager(Integer id);

}
