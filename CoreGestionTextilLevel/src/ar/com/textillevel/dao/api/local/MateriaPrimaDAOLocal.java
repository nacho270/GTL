package ar.com.textillevel.dao.api.local;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;

@Local
public interface MateriaPrimaDAOLocal extends DAOLocal<MateriaPrima, Integer> {

	public abstract List<MateriaPrima> getAllOrderByName(boolean incluirRepetidos);

	public abstract Anilina getAnilinaByColorIndex(Integer colorIndex);

	public boolean existeAnilina(TipoAnilina tipoAnilina, Integer colorIndex, BigDecimal concentracion, Integer idAExcluir);
	
	public abstract boolean existeMateriaPrima(String nombre, Integer idAExcluir);

	public abstract List<Anilina> getAllAnilinasByTipoArticulo(TipoArticulo tipoArticulo);

	public <T extends MateriaPrima> List<T> getAllByClase(Class<T> clazz);

	public abstract MateriaPrima getByIdEager(Integer id);

}