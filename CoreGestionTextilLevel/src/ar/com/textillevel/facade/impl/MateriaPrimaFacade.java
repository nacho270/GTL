package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.DiscriminatorValue;

import ar.com.textillevel.dao.api.local.MateriaPrimaCantidadDAOLocal;
import ar.com.textillevel.dao.api.local.MateriaPrimaDAOLocal;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

@Stateless
public class MateriaPrimaFacade implements MateriaPrimaFacadeRemote {

	@EJB
	private MateriaPrimaDAOLocal materiaPrimaDAOLocal;
	
	@EJB
	private MateriaPrimaCantidadDAOLocal materiaPrimaCantidadDAOLocal;

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
			MateriaPrima padre = getPadre(materiaPrima);
			if(!padre.equals(materiaPrima)) {
				padre.getMpHijas().addAll(materiaPrima.getMpHijas());
				materiaPrima.getMpHijas().clear();
				materiaPrimaDAOLocal.save(padre);
			}
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

	private MateriaPrima getPadre(MateriaPrima mp) {
		if(mp.getIdPadre() == null || mp.getIdPadre().equals(mp.getId())) {
			return mp;
		} else {
			MateriaPrima padre = materiaPrimaDAOLocal.getById(mp.getIdPadre());
			return getPadre(padre);
		}
	}

	public boolean existeMateriaPrima(String nombre, Integer idAExcluir) {
		return materiaPrimaDAOLocal.existeMateriaPrima(nombre,idAExcluir);
	}

	public List<Anilina> getAllAnilinasByTipoArticulo(TipoArticulo tipoArticulo) {
		return materiaPrimaDAOLocal.getAllAnilinasByTipoArticulo(tipoArticulo);
	}

	public List<Anilina> getOtrasAnilinasByMismoColorIndex(Anilina anilina) {
		return materiaPrimaDAOLocal.getOtrasAnilinasByMismoColorIndex(anilina);
	}
	
	public <T extends MateriaPrima> List<T> getAllByClase(Class<T> clazz) {
		return materiaPrimaDAOLocal.getAllByClase(clazz);
	}

	public MateriaPrima getByIdEager(Integer id) {
		return materiaPrimaDAOLocal.getByIdEager(id);
	}

	@SuppressWarnings("unchecked")
	public <T extends Formulable> MateriaPrimaCantidad<T> getMateriaPrimaCantidadById(Integer idMP) {
		return materiaPrimaCantidadDAOLocal.getById(idMP);
	}

}