package ar.com.textillevel.modulos.odt.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.InstruccionProcedimientoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoPasadas;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTexto;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTipoProducto;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;

@Stateless
public class InstruccionProcedimientoDAO extends GenericDAO<InstruccionProcedimiento, Integer> implements InstruccionProcedimientoDAOLocal {

	@SuppressWarnings("unchecked")
	public List<InstruccionProcedimiento> getInstruccionesBySectorAndTipo(ESectorMaquina sectorMaquina, ETipoInstruccionProcedimiento tipoInstruccion) {
		String nombreClase = null;
		if(tipoInstruccion == ETipoInstruccionProcedimiento.PASADA) {
			nombreClase = InstruccionProcedimientoPasadas.class.getCanonicalName();
		} else if(tipoInstruccion == ETipoInstruccionProcedimiento.TEXTO) {
			nombreClase = InstruccionProcedimientoTexto.class.getCanonicalName();
		} else {
			nombreClase = InstruccionProcedimientoTipoProducto.class.getCanonicalName();
		}
		String hql = " SELECT i FROM " + nombreClase + " i WHERE i.idTipoSector = :idSector AND i.procedimiento IS NULL ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSector", sectorMaquina.getId());
		List<InstruccionProcedimiento> resultList = q.getResultList();
		for(InstruccionProcedimiento i : resultList) {
			doEager(i);
		}
		return resultList;
	}

	private void doEager(InstruccionProcedimiento i) {
		if(i instanceof InstruccionProcedimientoPasadas) {
			((InstruccionProcedimientoPasadas)i).getQuimicos().size();
			((InstruccionProcedimientoPasadas)i).getQuimicosExplotados().size();
		}
	}

	public boolean existsInstruccion(InstruccionProcedimiento instruccion) {
		if(instruccion.getTipo() == ETipoInstruccionProcedimiento.PASADA) {
			return false;
		} else if(instruccion.getTipo() == ETipoInstruccionProcedimiento.TEXTO) {
			String hql = " SELECT i FROM InstruccionProcedimientoTexto i where (:id IS NULL OR i.id != :id) AND i.especificacion=:especificacion AND i.idTipoSector = :idSector";
			Query q = getEntityManager().createQuery(hql);
			q.setParameter("id", instruccion.getId());
			q.setParameter("especificacion", ((InstruccionProcedimientoTexto)instruccion).getEspecificacion());
			q.setParameter("idSector", instruccion.getSectorMaquina().getId());
			return !q.getResultList().isEmpty();
		} else {
			String hql = " SELECT i FROM InstruccionProcedimientoTipoProducto i where (:id IS NULL OR i.id != :id) AND i.tipoArticulo=:tipoArticulo AND i.idTipoSector = :idSector AND i.idTipoProducto = :idTipoProducto";
			Query q = getEntityManager().createQuery(hql);
			q.setParameter("id", instruccion.getId());
			q.setParameter("idTipoProducto", ((InstruccionProcedimientoTipoProducto)instruccion).getTipoProducto().getId());
			q.setParameter("tipoArticulo", ((InstruccionProcedimientoTipoProducto)instruccion).getTipoArticulo());
			q.setParameter("idSector", instruccion.getSectorMaquina().getId());
			return !q.getResultList().isEmpty();
		}
	}

}