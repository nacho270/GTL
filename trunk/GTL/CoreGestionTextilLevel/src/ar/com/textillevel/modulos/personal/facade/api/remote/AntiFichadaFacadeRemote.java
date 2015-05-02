package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.EMotivoAntifichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;

@Remote
public interface AntiFichadaFacadeRemote {
	public void crearAntiFichadas(EMotivoAntifichada tipo, Date fechaDesde, Date fechaHasta,LegajoEmpleado legajo, Boolean justificada);
}
