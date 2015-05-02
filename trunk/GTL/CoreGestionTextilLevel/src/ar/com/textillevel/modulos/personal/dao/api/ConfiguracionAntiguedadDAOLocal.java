package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.antiguedad.ConfiguracionAntiguedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

@Local
public interface ConfiguracionAntiguedadDAOLocal extends DAOLocal<ConfiguracionAntiguedad, Integer> {

	public Date getConfiguracionParaFechaAnterior(Sindicato sindicato, Date fechaDesde);

	public List<ConfiguracionAntiguedad> getAllConfiguracionesBySindicato(Sindicato sindicatoSeleccionado);

	public ConfiguracionAntiguedad getConfiguracionVigenteParaFecha(Sindicato sindicato, Date fecha);

}
