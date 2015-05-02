package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.dao.api.AntiFichadaDAOLocal;
import ar.com.textillevel.modulos.personal.dao.api.ConfiguracionVacacionesDAOLocal;
import ar.com.textillevel.modulos.personal.dao.api.RegistroVacacionesDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.EMotivoAntifichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;
import ar.com.textillevel.modulos.personal.facade.api.local.EmpleadoFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.remote.VacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.AntiFichadaFactory;
import ar.com.textillevel.modulos.personal.utils.VacacionesHelper;

@Stateless
public class VacacionesFacade implements VacacionesFacadeRemote {

	@EJB
	private EmpleadoFacadeLocal empleadoFacade;

	@EJB
	private AntiFichadaDAOLocal antiFichadaDao;

	@EJB
	private ConfiguracionVacacionesDAOLocal confVacacionesDao;
	
	@EJB
	private RegistroVacacionesDAOLocal registroDao;
	
	public void grabarVacaciones(Empleado empleado, RegistroVacacionesLegajo registro) {
		registro.setLegajo(empleado.getLegajo());
		empleado = empleadoFacade.save(empleado);
		if(registro.getId()!=null){
			int indice = empleado.getLegajo().getHistorialVacaciones().indexOf(registro);
			registro = empleado.getLegajo().getHistorialVacaciones().get(indice);
		}else{
			registro = empleado.getLegajo().getHistorialVacaciones().get(empleado.getLegajo().getHistorialVacaciones().size()-1);
		}
		AntiFichada antiFichadaVacaciones = AntiFichadaFactory.createAntiFichadaVigencia(EMotivoAntifichada.VACACIONES, registro.getFechaDesde(), registro.getFechaHasta(), empleado.getLegajo(), true);
		antiFichadaVacaciones.setRegistroVacaciones(registro);
		antiFichadaDao.save(antiFichadaVacaciones);
	}

	public void borrarPeriodoVacaciones(Empleado empleado, RegistroVacacionesLegajo registroVacacionesLegajo) {
		antiFichadaDao.borrarAntiFichadaVacaciones(registroVacacionesLegajo);
		actualizarPeriodos(empleado,registroVacacionesLegajo.getFechaDesde());
	}

	private void actualizarPeriodos(Empleado empleado, Date fechaDesde) {
		List<RegistroVacacionesLegajo> historialVacaciones = empleado.getLegajo().getHistorialVacaciones();
		List<VigenciaEmpleado> historialVigencias = empleado.getLegajo().getHistorialVigencias();
		VigenciaEmpleado ultimaVigencia = historialVigencias.get(historialVigencias.size()-1);
		for(RegistroVacacionesLegajo reg : historialVacaciones){
			Integer anio = DateUtil.getAnio(reg.getFechaDesde());
			ConfiguracionVacaciones confAnio = confVacacionesDao.getConfiguracionVacaciones(DateUtil.stringToDate("31/12/"+anio, DateUtil.SHORT_DATE));
			if(reg.getFechaDesde().after(fechaDesde)){
				reg.setDiasRemanentes(VacacionesHelper.getDiasDisponiblesParaAnio(historialVacaciones, anio, confAnio,ultimaVigencia.getFechaAlta()));
			}
		}
		empleadoFacade.save(empleado);		
	}

	public void modificarPeriodoVacaciones(Empleado empleado, RegistroVacacionesLegajo reg) {
		antiFichadaDao.borrarAntiFichadaVacaciones(reg);
		AntiFichada antiFichadaVacaciones = AntiFichadaFactory.createAntiFichadaVigencia(EMotivoAntifichada.VACACIONES, reg.getFechaDesde(), reg.getFechaHasta(), empleado.getLegajo(), true);
		antiFichadaVacaciones.setRegistroVacaciones(reg);
		antiFichadaDao.save(antiFichadaVacaciones);
		actualizarPeriodos(empleado,reg.getFechaDesde());
	}

	public List<RegistroVacacionesLegajo> getAllRegistrosVacacionesByFecha(Date fechaDesde, Date fechaHasta) {
		return registroDao.getAllRegistrosVacacionesByFecha(fechaDesde,fechaHasta);
	}
}
