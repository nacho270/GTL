package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaParcial;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaVigencia;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;

@Local
public interface AntiFichadaDAOLocal extends DAOLocal<AntiFichada, Integer> {
	
	public List<AntiFichadaParcial> getAntiFichadasParcialesPorFechaYEmpleado(Date fecha, LegajoEmpleado legajo);
	public List<AntiFichadaVigencia> getAntiFichadasDeVigenciaPorFechaYEmpleado(Date fecha, LegajoEmpleado legajo);
	public void borrarAntifichadasValeAtencion(ValeAtencion valeAtencion);
	public void borrarAntifichadasSancion(Sancion sancion);
	public void borrarAntiFichadaVacaciones(RegistroVacacionesLegajo registroVacacionesLegajo);
}
