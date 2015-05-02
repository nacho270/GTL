package ar.com.textillevel.modulos.personal.facade.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;

@Local
public interface AntiFichadaFacadeLocal {
	public List<AntiFichada> getAntifichadasParaFecha(Date fecha, LegajoEmpleado legajo);
}
