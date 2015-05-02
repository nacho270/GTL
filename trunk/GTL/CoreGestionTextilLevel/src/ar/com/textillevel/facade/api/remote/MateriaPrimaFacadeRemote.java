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

	public abstract List<MateriaPrima> getAllOrderByName();
	
	public abstract List<MateriaPrima> getAllOrderByTipos(ETipoMateriaPrima... tipos);
	
	public abstract MateriaPrima save(MateriaPrima materiaPrima);

	public abstract void remove(MateriaPrima materiaPrima);

	public MateriaPrima getByIdEager(Integer idMateriaPrima);
	
	public Anilina getAnilinaByColorIndex(Integer colorIndex);

	public abstract boolean existeAnilina(TipoAnilina tipoAnilina, Integer colorIndex, BigDecimal concentracion);

	public abstract boolean existeMateriaPrima(String nombre, Integer idAExcluir);

	public abstract List<Anilina> getAllAnilinasByTipoArticulo(TipoArticulo tipoArticulo);
	
	public abstract <T extends MateriaPrima> List<T> getAllByClase(Class<T> clazz);

}
