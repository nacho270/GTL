package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.ConfiguracionAntiguedadDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.ConfiguracionAntiguedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.AntiguedadFacadeRemote;

@Stateless
public class AntiguedadFacade implements AntiguedadFacadeRemote{

	@EJB
	private ConfiguracionAntiguedadDAOLocal configuracionAntiguedadDao;
	
	public List<ConfiguracionAntiguedad> getAllConfiguraciones() {
		return configuracionAntiguedadDao.getAll();
	}

	public void remove(ConfiguracionAntiguedad configuracionAntiguedadActual) {
		configuracionAntiguedadDao.removeById(configuracionAntiguedadActual.getId());
	}

	public ConfiguracionAntiguedad save(ConfiguracionAntiguedad configuracionAntiguedadActual) {
		return configuracionAntiguedadDao.save(configuracionAntiguedadActual);
	}

	public Date getConfiguracionParaFechaAnterior(Sindicato sindicato, Date fechaDesde) {
		return configuracionAntiguedadDao.getConfiguracionParaFechaAnterior(sindicato,fechaDesde);
	}

	public List<ConfiguracionAntiguedad> getAllConfiguracionesBySindicato(Sindicato sindicatoSeleccionado) {
		List<ConfiguracionAntiguedad> confsPorSindicato = configuracionAntiguedadDao.getAllConfiguracionesBySindicato(sindicatoSeleccionado);
		for(ConfiguracionAntiguedad cf : confsPorSindicato){
			cf.getAntiguedades().size();
		}
		return confsPorSindicato;
	}

	public ConfiguracionAntiguedad getConfiguracionVigenteParaFecha(Sindicato sindicato, Date fecha) {
		return configuracionAntiguedadDao.getConfiguracionVigenteParaFecha(sindicato, fecha);
	}

}
