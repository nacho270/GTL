package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.util.NumUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.modulos.personal.dao.api.EmpleadoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.legajos.to.DatosVencimientoContratoEmpleadoTO;
import ar.com.textillevel.modulos.personal.enums.ETipoBusquedaEmpleados;

@Stateless
@SuppressWarnings("unchecked")
public class EmpleadoDAO extends GenericDAO<Empleado, Integer> implements EmpleadoDAOLocal {

	public Empleado getByIdTotalmenteEager(Integer id) {
		Empleado em = getById(id);
		if(em.getArtConvenio()!=null){
			em.getArtConvenio().getBeneficiario1();
		}
		if(em.getArtObligatoria()!=null){
			em.getArtObligatoria().getBeneficiario1();
		}
		em.getDatosFamilia().size();
		em.getDomicilios().size();
		em.getHistorialEmpleos().size();
		em.getInformacionEstadoCivil().size();
		em.getNacionalidad().getNombre();
		em.getDocumentacion().getAfjp().getNombre();
		if(em.getSeguro()!=null){
			em.getSeguro().getFechaAlta();
		}
		return em;
	}

	public Integer getProximoNumeroLegajo() {
		String hql = " SELECT MAX(l.nroLegajo) FROM LegajoEmpleado l ";
		Query q = getEntityManager().createQuery(hql);
		Integer numero = NumUtil.toInteger(q.getSingleResult());
		return numero == null?1:numero+1;
	}

	public List<Empleado> buscarEmpleados(Integer nroLegajo, ETipoBusquedaEmpleados modoBusqueda, Sindicato sindicato, String nombreOApellido, Boolean incluirPrivados, ETipoContrato tipoDeContrato) {
		String hql = " SELECT e FROM Empleado e LEFT JOIN FETCH e.legajo LEFT JOIN FETCH e.contratoEmpleado WHERE 1 = 1 "+
					 (incluirPrivados?" " : " AND e.privado = 0 ")+
					 (nroLegajo!=null && nroLegajo>0?" AND e.legajo.nroLegajo = :nroLegajo ": " ")+
					 (modoBusqueda==ETipoBusquedaEmpleados.INACTIVOS?" AND e.legajo.dadoDeBaja = 1 ": modoBusqueda==ETipoBusquedaEmpleados.ACTIVOS?" AND e.legajo.dadoDeBaja = 0  ": " ")+
					 (sindicato!=null? " AND e.legajo.puesto.sindicato.id = :idSindicato ":" ")+
					 (nombreOApellido!=null? " AND (e.nombre LIKE :nombreOApellido OR e.apellido LIKE :nombreOApellido) ":" ")+
					 (tipoDeContrato!=null?  " AND e.contratoEmpleado.contrato.idTipoContrato = :idTipoContrato ":" ");
		Query q = getEntityManager().createQuery(hql);
		if(nroLegajo!=null && nroLegajo > 0){
			q.setParameter("nroLegajo", nroLegajo);
		}
		if(sindicato !=null){
			q.setParameter("idSindicato", sindicato.getId());
		}
		if(nombreOApellido!=null){
			q.setParameter("nombreOApellido", "%"+nombreOApellido+"%");
		}
		if(tipoDeContrato!=null){
			q.setParameter("idTipoContrato", tipoDeContrato.getId());
		}
		List<Empleado> empleados = q.getResultList();
		if(empleados!=null && !empleados.isEmpty()){
			for(Empleado e : empleados){
				e.getDomicilios().size();
				if(e.getLegajo()!=null){
					e.getLegajo().getHistorialVigencias().size();
					e.getLegajo().getHistorialVacaciones().size();
				}
			}
			return empleados;
		}
		return null;
	}

	public Empleado getEmpleadoByNumeroLegajo(Integer nroLegajo) {
		String hql = " SELECT e FROM Empleado e LEFT JOIN FETCH e.legajo  WHERE e.legajo.nroLegajo = :nroLegajo ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroLegajo", nroLegajo);
		List<Empleado> lista = q.getResultList();
		if(lista != null && !lista.isEmpty()){
			if(lista.size()==1){
				Empleado empleado = lista.get(0);
				empleado.getHistorialEmpleos().size();
				return empleado;
			}else{
				throw new RuntimeException("INCONSISTENCIA, HAY EMPLEADOS CON EL MISMO NUMERO DE LEGAJO");
			}
		}
		return null;
	}

	public List<Empleado> getAllOrderByName(Boolean incluirPrivados) {
		String hql = " SELECT e FROM Empleado e" +
					(incluirPrivados?" " : " WHERE e.privado = 0 ");
		Query q = getEntityManager().createQuery(hql);
		return q.getResultList();
	}

	public List<DatosVencimientoContratoEmpleadoTO> getEmpleadosConContratosQueVencenEnODespuesDeFecha(Date fecha, ETipoContrato tipoContrato) {
		//fecha >= fecha de vencimiento (fecha de contrato + duracion)
//		String hql = " SELECT e FROM Empleado e JOIN FETCH e.contratoEmpleado ce WHERE ce.fechaDesde <= :fecha ";// DATE_ADD(ce.fechaDesde, INTERVAL ce.cantidadDias DAY) <= :fecha ";
		String nativeQuery = " SELECT emp.A_NOMBRE,emp.A_APELLIDO,  DATE_ADD(ce.a_fecha_desde, INTERVAL ce.a_cantidad_dias DAY) " +
							 " FROM t_pers_empleado emp INNER JOIN t_pers_contrato_empleado ce ON ce.P_ID = emp.F_CONTR_EMP_P_ID " +
							 "		INNER JOIN T_PERS_CONTRATO c ON c.P_ID = ce.F_CONTRATO_P_ID "+
							 " WHERE DATE_ADD(ce.a_fecha_desde, INTERVAL ce.a_cantidad_dias DAY) <=  :fecha AND c.A_ID_TIPO_CONTRATO = :idTipoContrato ";
		Query q = getEntityManager().createNativeQuery(nativeQuery);
		q.setParameter("fecha", fecha);
		q.setParameter("idTipoContrato", tipoContrato.getId());
		List<Object[]> resultSet = q.getResultList();
		if(resultSet!=null && !resultSet.isEmpty()){
			List<DatosVencimientoContratoEmpleadoTO> ret = new ArrayList<DatosVencimientoContratoEmpleadoTO>();
			for(Object[] o : resultSet){
				ret.add(new DatosVencimientoContratoEmpleadoTO((String)o[0],(String)o[1],(Date)o[2]));
			}
			return ret;
		}
		return null;
	}

	public LegajoEmpleado getLegajoByNumero(Integer nroLegajo) {
		String hql = " SELECT l FROM LegajoEmpleado l WHERE l.nroLegajo = :nroLegajo ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroLegajo", nroLegajo);
		List<LegajoEmpleado> legs = q.getResultList();
		if(legs!=null && !legs.isEmpty()){
			return legs.get(0);
		}
		return null;
	}

	public List<Empleado> getAllActivosBySindicato(Sindicato sindicato, Integer nroLegajo, String nombreOrApellido) {
		String hql = "SELECT e " +
					 "FROM Empleado e " +
					 "JOIN FETCH e.legajo leg " +
					 "WHERE leg.puesto.sindicato.id = :idSindicato AND " +
					 "		e.privado = 0 AND " +
					 "		e.legajo.dadoDeBaja = 0 "+
					 (nroLegajo!=null && nroLegajo>0?" AND leg.nroLegajo = :nroLegajo ": " ")+
					 (sindicato!=null? " AND leg.puesto.sindicato.id = :idSindicato ":" ")+
					 (!StringUtil.isNullOrEmpty(nombreOrApellido)? " AND (e.nombre LIKE :nombreOApellido OR e.apellido LIKE :nombreOApellido) ":" ");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSindicato", sindicato.getId());
		if(nroLegajo!=null && nroLegajo > 0){
			q.setParameter("nroLegajo", nroLegajo);
		}
		if(nombreOrApellido!=null){
			q.setParameter("nombreOApellido", "%"+nombreOrApellido+"%");
		}
		List<Empleado> empleados = q.getResultList();
		for(Empleado e : empleados) {
			if(e.getLegajo() != null) {
				e.getLegajo().getHistorialVigencias().size();
			}
		}
		return empleados;
	}

	public List<Empleado> getAllOrderByApellido(String apellido, Boolean incluirPrivados) {
		String hql = " SELECT e FROM Empleado e " +
					 " JOIN FETCH e.legajo " +
					 " WHERE e.apellido LIKE :apellido "+
					 (incluirPrivados?" " : " AND e.privado = 0 ") +
					 " AND	e.legajo.dadoDeBaja = 0 ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("apellido","%"+ apellido+"%");
		return q.getResultList();
	}

}