package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.antiguedad.ConfiguracionAntiguedad;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;

@Remote
public interface AntiguedadFacadeRemote {

	public List<ConfiguracionAntiguedad> getAllConfiguraciones();

	public void remove(ConfiguracionAntiguedad configuracionAntiguedadActual);

	public ConfiguracionAntiguedad save(ConfiguracionAntiguedad configuracionAntiguedadActual);

	public Date getConfiguracionParaFechaAnterior(Sindicato selectedItem, Date date);

	public List<ConfiguracionAntiguedad> getAllConfiguracionesBySindicato(Sindicato sindicatoSeleccionado);

	public ConfiguracionAntiguedad getConfiguracionVigenteParaFecha(Sindicato sindicato, Date fecha);

}
