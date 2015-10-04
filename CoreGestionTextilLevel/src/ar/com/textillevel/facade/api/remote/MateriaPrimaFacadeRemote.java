package ar.com.textillevel.facade.api.remote;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;

@Remote
public interface MateriaPrimaFacadeRemote {

	public abstract List<MateriaPrima> getAllOrderByName(boolean incluirRepetidos);
	
	public abstract List<MateriaPrima> getAllOrderByTipos(boolean incluirRepetidos, ETipoMateriaPrima... tipos);
	
	public abstract MateriaPrima save(MateriaPrima materiaPrima);

	public abstract void remove(MateriaPrima materiaPrima);

	public Anilina getAnilinaByColorIndex(Integer colorIndex);

	public abstract boolean existeAnilina(TipoAnilina tipoAnilina, Integer colorIndex, BigDecimal concentracion, Integer idAExcluir);

	public abstract boolean existeMateriaPrima(String nombre, Integer idAExcluir);

	public abstract List<Anilina> getAllAnilinasByTipoArticulo(TipoArticulo tipoArticulo);
	
	public abstract <T extends MateriaPrima> List<T> getAllByClase(Class<T> clazz);

	public abstract MateriaPrima getByIdEager(Integer id);

}
