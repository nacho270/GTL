package ar.com.textillevel.modulos.odt.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.modulos.odt.dao.api.local.OrdenDeTrabajoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.DoEagerFormulaExplotadaVisitor;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaEstampadoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaTenidoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoPasadas;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTipoProducto;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoPasadasODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoTipoProductoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.SecuenciaODT;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;

@Stateless
@SuppressWarnings("unchecked")
public class OrdenDeTrabajoDAO extends GenericDAO<OrdenDeTrabajo, Integer> implements OrdenDeTrabajoDAOLocal {

	public List<OrdenDeTrabajo> getOdtNoAsociadasByClient(Integer idCliente) {
//		Query query = getEntityManager().createQuery(
//						"SELECT DISTINCT odt " +
//						"FROM OrdenDeTrabajo odt " +
//						"JOIN FETCH odt.piezas podt " +
//						"WHERE odt.remito.cliente.id = :idCliente AND " +
//						"NOT EXISTS (" +
//						"SELECT 1 " +
//						"FROM PiezaRemito pr " +
//						"WHERE podt IN ELEMENTS(pr.piezasPadreODT))"
//						);
		Query query = getEntityManager().createQuery(
				"SELECT odt " +
				"FROM OrdenDeTrabajo odt " +
				"WHERE odt.remito.cliente.id = :idCliente AND " +
				"NOT EXISTS (" +
				"SELECT 1 " +
				"FROM RemitoSalida rs " +
				"WHERE odt IN ELEMENTS(rs.odts) AND rs.cliente.id = :idCliente)"
				);
		query.setParameter("idCliente", idCliente);
		List<OrdenDeTrabajo> resultList = query.getResultList();

		for(OrdenDeTrabajo odt : resultList) {
			for(PiezaODT podt2 : odt.getPiezas()) {
				podt2.getPiezasSalida().size();
			}
		}
		return resultList;
	}

	public String getUltimoCodigoODT() {
		Query query = getEntityManager().createQuery(" SELECT odt.codigo " +
													 " FROM OrdenDeTrabajo odt " +
				 									 " ORDER BY CAST(substring(odt.codigo, 1, 4) AS integer) DESC, " +
				 									 "		    CAST(substring(odt.codigo, 5, 2) AS integer) DESC, " +
				 									 "		    CAST(substring(odt.codigo, 7, length(odt.codigo)+6) AS integer) DESC");
		query.setMaxResults(1);
		List<String> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	public List<OrdenDeTrabajo> getOdtEagerByRemitoList(Integer idRemito) {
		Query query = getEntityManager().createQuery("SELECT odt " +
													 "FROM OrdenDeTrabajo odt " +
													 "WHERE odt.remito.id = :idRemito ");
		query.setParameter("idRemito", idRemito);
		List<OrdenDeTrabajo> resultList = query.getResultList();
		for(OrdenDeTrabajo odt : resultList) {
			odt = getByIdEager(odt.getId());
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see ar.com.textillevel.dao.api.local.OrdenDeTrabajoDAOLocal#getODTAsociadas(java.lang.Integer)
	 */
	public List<OrdenDeTrabajo> getODTAsociadas(Integer idRemitoEntrada) {
		Query query = getEntityManager().createQuery("FROM OrdenDeTrabajo odt " +
													 "WHERE odt.remito.id = :idRemitoEntrada");
		query.setParameter("idRemitoEntrada", idRemitoEntrada);
		List<OrdenDeTrabajo> resultList = query.getResultList();
		return resultList;
	}

	public OrdenDeTrabajo getByIdEager(Integer idODT) {
		OrdenDeTrabajo odt = getById(idODT);
		if(odt.getMaquinaActual() != null) {
			odt.getMaquinaActual().getTipoMaquina().getSectorMaquina();
		}
		for(PiezaODT pieza : odt.getPiezas()) {
			pieza.getPiezasSalida().size();
		}
		if(odt.getRemito() != null) {
			odt.getRemito().getPiezas().size();
		}
		if(odt.getSecuenciaDeTrabajo()!=null){
			odt.getSecuenciaDeTrabajo().getPasos().size();
			for(PasoSecuenciaODT ps : odt.getSecuenciaDeTrabajo().getPasos()){
				ps.getSubProceso().getPasos().size();

				doEagerIntruccionesODT(ps.getSubProceso().getPasos());

				ps.getProceso().getInstrucciones().size();

				doEagerIntrucciones(ps.getProceso().getInstrucciones());
				
				ps.getProceso().getNombre();
				ps.getSector().getNombre();
				ps.getSubProceso().getNombre();
				ps.getSubProceso().getTipoArticulo().getNombre();
			}
		}
		
		if(odt.getProductoArticulo() != null){
			Articulo articulo = odt.getProductoArticulo().getArticulo();
			if(articulo!=null && articulo.getTipoArticulo() != null){
				articulo.getTipoArticulo().getNombre();
				if(articulo.getTipoArticulo().getTiposArticuloComponentes()!=null){
					articulo.getTipoArticulo().getTiposArticuloComponentes().size();
				}
			}
			if(odt.getRemito()!=null){
				odt.getRemito().getProductoArticuloList().size();
				for(ProductoArticulo p : odt.getRemito().getProductoArticuloList()){
					if(p.getTipo() == ETipoProducto.ESTAMPADO){
						if(p.getVariante()!=null && p.getVariante().getColores()!=null){
							p.getVariante().getColores().size();
						}
					}else if(p.getTipo() == ETipoProducto.TENIDO){
						if(p.getGamaColor()!= null && p.getGamaColor().getColores() != null){
							p.getGamaColor().getColores().size();
						}
					}
				}
			}
		}
		return odt;
	}

	public List<OrdenDeTrabajo> getByIdsEager(List<Integer> ids) {
		List<OrdenDeTrabajo> lista = new ArrayList<OrdenDeTrabajo>();
		for (Integer id : ids) {
			lista.add(getByIdEager(id));
		}
		return lista;
	}

	private void doEagerIntruccionesODT(List<InstruccionProcedimientoODT> instrucciones) {
		for(InstruccionProcedimientoODT i : instrucciones){
			if(i instanceof InstruccionProcedimientoPasadasODT){
				((InstruccionProcedimientoPasadasODT)i).getQuimicosExplotados().size();
			}else if(i instanceof InstruccionProcedimientoTipoProductoODT){
				InstruccionProcedimientoTipoProductoODT itp = (InstruccionProcedimientoTipoProductoODT)i;
				if(itp.getFormula()!=null){
					DoEagerFormulaExplotadaVisitor visitor = new DoEagerFormulaExplotadaVisitor(); 
					itp.getFormula().accept(visitor);
				}
				itp.getTipoArticulo().getNombre();
				itp.getTipoArticulo().getTiposArticuloComponentes().size();
			}
		}		
	}

	private void doEagerIntrucciones(List<InstruccionProcedimiento> instrucciones){
		for(InstruccionProcedimiento i : instrucciones){
			if(i instanceof InstruccionProcedimientoPasadas){
				((InstruccionProcedimientoPasadas)i).getQuimicos().size();
			}else if(i instanceof InstruccionProcedimientoTipoProducto){
				InstruccionProcedimientoTipoProducto itp = (InstruccionProcedimientoTipoProducto)i;
				itp.getTipoArticulo().getNombre();
				itp.getTipoArticulo().getTiposArticuloComponentes().size();
			}
		}		
	}	
	
	public List<OrdenDeTrabajo> getOrdenesDeTrabajo(EEstadoODT estado, Date fechaDesde, Date fechaHasta, Cliente cliente) {
		String hql = " SELECT odt FROM OrdenDeTrabajo odt WHERE 1=1 "+
					 (estado!=null?" AND odt.idEstadoODT = :idEstadoODT ":" ")+
					 (fechaDesde!=null?" AND odt.fechaODT >= :fechaDesde  ":" ")+
					 (fechaHasta!=null?" AND odt.fechaODT <= :fechaHasta  ":" ") +
					 (cliente!=null?" AND odt.remito.cliente.id = :idCliente ":" ");
		Query q = getEntityManager().createQuery(hql);
		if(estado!=null){
			q.setParameter("idEstadoODT", estado.getId());
		}
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(cliente!=null){
			q.setParameter("idCliente", cliente.getId());
		}
		return q.getResultList();
	}

	public List<OrdenDeTrabajo> getAllEnProceso(Date fechaDesde, Date fechaHasta, Cliente cliente) {
		String hql = " SELECT odt FROM OrdenDeTrabajo odt WHERE odt.maquinaActual IS NOT NULL "+
					 (fechaDesde!=null? " AND odt.fechaODT >= :fechaDesde ":" ")+
					 (fechaHasta!=null? " AND odt.fechaODT <= :fechaHasta" : " ")+
					 (cliente!=null?" AND odt.remito.cliente.id = :idCliente ":" ");
		Query q = getEntityManager().createQuery(hql);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(cliente!=null){
			q.setParameter("idCliente", cliente.getId());
		}
		return q.getResultList();
	}

	public List<OrdenDeTrabajo> getAllEnProcesoByTipoMaquina(Date fechaDesde, Date fechaHasta, Cliente cliente, Integer idTipoMaquina) {
		String hql = " SELECT odt FROM OrdenDeTrabajo odt WHERE odt.maquinaActual.tipoMaquina.id = :idTipoMaquina  "+
					 (fechaDesde!=null? " AND odt.fechaODT >= :fechaDesde ":" ")+
					 (fechaHasta!=null? " AND odt.fechaODT <= :fechaHasta" : " ")+
					 (cliente!=null?" AND odt.remito.cliente.id = :idCliente ":" ");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idTipoMaquina", idTipoMaquina);
		if (fechaDesde != null) {
			q.setParameter("fechaDesde", fechaDesde);
		}
		if (fechaHasta != null) {
			q.setParameter("fechaHasta", fechaHasta);
		}
		if (cliente != null) {
			q.setParameter("idCliente", cliente.getId());
		}
		return q.getResultList();
	}

	public Short getUltimoOrdenMaquina(Maquina maquina) {
		String hql = " SELECT MAX(odt.ordenEnMaquina) FROM OrdenDeTrabajo odt WHERE odt.maquinaActual.id = :idMaquina ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idMaquina", maquina.getId());
		Integer ret = NumUtil.toInteger(q.getSingleResult());
		return ret==null?0:ret.shortValue();
	}

	public OrdenDeTrabajo getByMaquinaYOrden(Integer idMaquina, Short ordenEnMaquina) {
		String hql = " SELECT odt FROM OrdenDeTrabajo odt WHERE odt.maquinaActual.id = :idMaquina AND odt.ordenEnMaquina = :ordenEnMaquina ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("ordenEnMaquina", ordenEnMaquina);
		q.setParameter("idMaquina", idMaquina);
		List<OrdenDeTrabajo> odts = q.getResultList();
		if(odts == null || odts.isEmpty() || odts.size()>1){
			throw new RuntimeException("Inconstencia en los ordenes de las odt");
		}
		return odts.get(0);
	}
	
	public void updateODT(OrdenDeTrabajo odt){
		getHibernateSession().update(odt);
	}

	public void borrarSecuencia(OrdenDeTrabajo ordenDeTrabajo) {
		Integer idSecuencia = ordenDeTrabajo.getSecuenciaDeTrabajo().getId();
		SecuenciaODT sec = getEntityManager().find(SecuenciaODT.class, idSecuencia);
		Set<Integer> idsPasos = new HashSet<Integer>();
		Set<Integer> idsProcesos = new HashSet<Integer>();
		Set<Integer> idsInstrucciones = new HashSet<Integer>();
		Set<Integer> idsMatPrimEx = new HashSet<Integer>();
		Set<Integer> idsFormEx = new HashSet<Integer>();
		Set<Integer> idsMPEAnilina = new HashSet<Integer>();
		Set<Integer> idsMPEQuimico = new HashSet<Integer>();
		Set<Integer> idsMPEPigmento = new HashSet<Integer>();
		
		
		for(PasoSecuenciaODT ps : sec.getPasos()){
			idsPasos.add(ps.getId());
			idsProcesos.add(ps.getSubProceso().getId());
			for(InstruccionProcedimientoODT ip : ps.getSubProceso().getPasos()){
				idsInstrucciones.add(ip.getId());
				if(ip instanceof InstruccionProcedimientoTipoProductoODT){
					InstruccionProcedimientoTipoProductoODT ipta = (InstruccionProcedimientoTipoProductoODT)ip;
					if(ipta.getFormula()!=null){
						if(ipta.getFormula() instanceof FormulaEstampadoClienteExplotada){
							FormulaEstampadoClienteExplotada fee = (FormulaEstampadoClienteExplotada)ipta.getFormula();
							for(MateriaPrimaCantidadExplotada<Pigmento> mpc : fee.getPigmentos()){
								idsMPEPigmento.add(mpc.getId());
							}
							for(MateriaPrimaCantidadExplotada<Quimico> mpc : fee.getQuimicos()){
								idsMPEQuimico.add(mpc.getId());
							}
						}else if(ipta.getFormula() instanceof FormulaTenidoClienteExplotada){
							FormulaTenidoClienteExplotada fee = (FormulaTenidoClienteExplotada)ipta.getFormula();
							for(MateriaPrimaCantidadExplotada<Anilina> mpc : fee.getMateriasPrimas()){
								idsMPEAnilina.add(mpc.getId());
							}
						}
						idsFormEx.add(ipta.getFormula().getId());
					}
				}else if(ip instanceof InstruccionProcedimientoPasadasODT){
					InstruccionProcedimientoPasadasODT ipp = (InstruccionProcedimientoPasadasODT)ip;
					if(ipp.getQuimicosExplotados()!=null && !ipp.getQuimicosExplotados().isEmpty()){
						for(MateriaPrimaCantidadExplotada<Quimico> mp : ipp.getQuimicosExplotados()){
							idsMatPrimEx.add(mp.getId());
						}
					}
				}
			}
		}
		
		/*
		 * delete from t_paso_seceuncia_odt;
update t_orden_de_trabajo set f_secuencia_p_id = null;
delete from t_secuencia_odt;
delete from t_map_prima_explotada;
delete from t_instruccion_procedimiento where f_proc_odt_p_id is not null;
delete from t_procedimiento_odt;
		 */
		
		
		getEntityManager().createNativeQuery("delete from t_paso_seceuncia_odt where f_secuencia_p_id = :idSecuencia ").setParameter("idSecuencia", idSecuencia).executeUpdate();
		getEntityManager().flush();
		ordenDeTrabajo.setSecuenciaDeTrabajo(null);
		ordenDeTrabajo = save(ordenDeTrabajo);
		getEntityManager().flush();
		getEntityManager().createQuery(" DELETE FROM SecuenciaODT s WHERE s.id = :idSecuencia ").setParameter("idSecuencia", idSecuencia).executeUpdate();
		getEntityManager().flush();
		if(!idsMatPrimEx.isEmpty()){
			getEntityManager().createNativeQuery("delete from t_map_prima_explotada where p_id in (:idsMpExp)").setParameter("idsMpExp", idsMatPrimEx).executeUpdate();
			getEntityManager().flush();
		}
		if(!idsMPEPigmento.isEmpty()){
			getEntityManager().createNativeQuery("delete from t_map_prima_explotada where p_id in (:idsMPEPigmento)").setParameter("idsMPEPigmento", idsMPEPigmento).executeUpdate();
			getEntityManager().flush();
		}
		if(!idsMPEQuimico.isEmpty()){
			getEntityManager().createNativeQuery("delete from t_map_prima_explotada where p_id in (:idsMPEQuimico)").setParameter("idsMPEQuimico", idsMPEQuimico).executeUpdate();
			getEntityManager().flush();
		}
		
		if(!idsMatPrimEx.isEmpty()){
			getEntityManager().createNativeQuery("delete from t_map_prima_explotada where p_id in (:idsMPEAnilina)").setParameter("idsMPEAnilina", idsMPEAnilina).executeUpdate();
			getEntityManager().flush();
		}
		
		if(!idsFormEx.isEmpty()){
			getEntityManager().createNativeQuery("delete from T_FORMULA_TENIDO_EXPLOTADA where p_id in (:idsFormEx)").setParameter("idsFormEx", idsFormEx).executeUpdate();
			getEntityManager().flush();
		}
		if(!idsFormEx.isEmpty()){
			getEntityManager().createNativeQuery("delete from T_FORMULA_ESTAMPADO_EXPLOTADA where p_id in (:idsFormEx)").setParameter("idsFormEx", idsFormEx).executeUpdate();
			getEntityManager().flush();
		}
		getEntityManager().createNativeQuery("delete from t_instruccion_procedimiento where p_id in (:idsInstrucciones)").setParameter("idsInstrucciones", idsInstrucciones).executeUpdate();
		getEntityManager().flush();
		getEntityManager().createNativeQuery("delete from t_procedimiento_odt where p_id in (:idsProcesos)").setParameter("idsProcesos", idsProcesos).executeUpdate();
		getEntityManager().flush();
	}

	public void flush() {
		getEntityManager().flush();
	}

	@Override
	public OrdenDeTrabajo getODTEagerByCodigo(String codigo) {
		Query query = getEntityManager().createQuery(" SELECT odt " +
													 " FROM OrdenDeTrabajo odt " +
													 " WHERE odt.codigo = :codigo");
		query.setParameter("codigo", codigo);
		List<OrdenDeTrabajo> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else if(resultList.size() > 1) {
			throw new IllegalArgumentException("Existe más de una ODT con código " + codigo);
		} else {
			OrdenDeTrabajo odt = resultList.get(0);
			getByIdEager(odt.getId());
			return odt;
		}
	}
}