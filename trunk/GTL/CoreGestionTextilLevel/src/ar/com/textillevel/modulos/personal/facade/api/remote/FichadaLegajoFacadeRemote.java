package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.fichadas.FichadaLegajo;
import ar.com.textillevel.modulos.personal.entidades.fichadas.to.FichadaLegajoTO;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;

@Remote
public interface FichadaLegajoFacadeRemote {
	public List<FichadaLegajoTO> getAllByLegajoYFecha(LegajoEmpleado legajo, Date fechaDesde, Date fechaHasta);
	public FichadaLegajo save(FichadaLegajo fichada);
	public void remove(FichadaLegajo fichada);
}
