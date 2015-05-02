package ar.com.textillevel.modulos.odt.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.TipoMaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;

@Stateless
@SuppressWarnings("unchecked")
public class TipoMaquinaDAO extends GenericDAO<TipoMaquina, Integer> implements TipoMaquinaDAOLocal{

	public boolean existsByNombre(TipoMaquina tipoMaquina) {
		String hql = "FROM TipoMaquina t where (:id IS NULL OR t.id != :id) AND t.nombre=:nombre";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", tipoMaquina.getId());
		q.setParameter("nombre", tipoMaquina.getNombre());
		return !q.getResultList().isEmpty();
	}

	public boolean existsByOrden(TipoMaquina tipoMaquina) {
		String hql = "FROM TipoMaquina t where (:id IS NULL OR t.id != :id) AND t.orden=:orden";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", tipoMaquina.getId());
		q.setParameter("orden", tipoMaquina.getOrden());
		return !q.getResultList().isEmpty();
	}

	public List<TipoMaquina> getAllByIdTipo(Integer idTipoMaquina) {
		return getEntityManager().createQuery(" SELECT t FROM TipoMaquina t WHERE t.id = :idTipoMaquina ORDER BY t.nombre ASC").setParameter("idTipoMaquina", idTipoMaquina).getResultList();
	}

	public TipoMaquina getTipoMaquinaConOrdenMayor() {
		List<TipoMaquina> allTiposMaquina = getEntityManager().createQuery(" SELECT t FROM TipoMaquina t ORDER BY t.orden DESC").getResultList();
		if(allTiposMaquina.isEmpty()) {
			return null;
		} else {
			return allTiposMaquina.get(0);
		}
	}

//	public TipoMaquina getByIdEager(Integer idTipoMaquina) {
//		return getById(idTipoMaquina);
//	}
	
//	public TipoMaquina getByIdSuperEager(Integer idTipoMaquina) {
//		TipoMaquina tipoMaquina = getById(idTipoMaquina);
//		tipoMaquina.getProcesos().size();
//		for(ProcesoTipoMaquina p : tipoMaquina.getProcesos()){
//			if(p.getProcedimientos() !=null){
//				p.getProcedimientos().size();
//				for(ProcedimientoTipoArticulo procedimiento: p.getProcedimientos()){
//					procedimiento.getTipoArticulo().getNombre();
//					procedimiento.getTipoArticulo().getTiposArticuloComponentes().size();
//					if(procedimiento.getPasos() != null){
//						procedimiento.getPasos().size();
//						for(InstruccionProcedimiento instruccion : procedimiento.getPasos()){
//							if(instruccion instanceof InstruccionProcedimientoPasadas && ((InstruccionProcedimientoPasadas)instruccion).getQuimicos()!=null){
//								((InstruccionProcedimientoPasadas)instruccion).getQuimicos().size();
//							}
//						}
//					}
//				}
//			}
//		}
//		return tipoMaquina;
//	}

}
