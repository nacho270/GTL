package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.DiscriminatorValue;

import ar.com.textillevel.dao.api.local.MateriaPrimaDAOLocal;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;

@Stateless
public class MateriaPrimaFacade implements MateriaPrimaFacadeRemote {

	@EJB
	private MateriaPrimaDAOLocal materiaPrimaDAOLocal;

	public List<MateriaPrima> getAllOrderByName(boolean incluirRepetidos) {
		return materiaPrimaDAOLocal.getAllOrderByName(incluirRepetidos);
	}

	public void remove(MateriaPrima materiaPrima) {
		materiaPrimaDAOLocal.removeById(materiaPrima.getId());
	}

	public MateriaPrima save(MateriaPrima materiaPrima) {
		if(materiaPrima.getId() != null) {
			String tipo = materiaPrima.getClass().getAnnotation(DiscriminatorValue.class).value();
			materiaPrimaDAOLocal.updateTipoManualmente(materiaPrima.getId(), tipo);
		}
		if (!materiaPrima.getMpHijas().isEmpty()) {
			for (MateriaPrima mpHija : materiaPrima.getMpHijas()) {
				materiaPrimaDAOLocal.save(mpHija);
			}
		}
		MateriaPrima mp = materiaPrimaDAOLocal.save(materiaPrima);
		return mp;
	}

	public Anilina getAnilinaByColorIndex(Integer colorIndex) {
		return materiaPrimaDAOLocal.getAnilinaByColorIndex(colorIndex);
	}

	public boolean existeAnilina(TipoAnilina tipoAnilina, Integer colorIndex, BigDecimal concentracion, Integer idAExcluir) {
		return materiaPrimaDAOLocal.existeAnilina(tipoAnilina,colorIndex,concentracion, idAExcluir);
	}

	public List<MateriaPrima> getAllOrderByTipos(boolean incluirRepetidos, final ETipoMateriaPrima... tipos) {
		List<MateriaPrima> all = getAllOrderByName(incluirRepetidos);
		List<MateriaPrima> filtradas = new ArrayList<MateriaPrima>();
		for(MateriaPrima mp : all){
			for(ETipoMateriaPrima etmp : tipos){
				if(mp.getTipo() == etmp){
					mp.getMpHijas().size();
					filtradas.add(mp);
				}
			}
		}
		return filtradas;
	}

	public boolean existeMateriaPrima(String nombre, Integer idAExcluir) {
		return materiaPrimaDAOLocal.existeMateriaPrima(nombre,idAExcluir);
	}

	public List<Anilina> getAllAnilinasByTipoArticulo(TipoArticulo tipoArticulo) {
		return materiaPrimaDAOLocal.getAllAnilinasByTipoArticulo(tipoArticulo);
	}

	public <T extends MateriaPrima> List<T> getAllByClase(Class<T> clazz) {
		return materiaPrimaDAOLocal.getAllByClase(clazz);
	}

	public MateriaPrima getByIdEager(Integer id) {
		return materiaPrimaDAOLocal.getByIdEager(id);
	}

}