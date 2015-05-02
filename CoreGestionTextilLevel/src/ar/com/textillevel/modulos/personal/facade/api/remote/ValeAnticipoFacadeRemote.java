package ar.com.textillevel.modulos.personal.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

@Remote
public interface ValeAnticipoFacadeRemote {

	public ValeAnticipo save(ValeAnticipo vale,Empleado empleado);

	public List<ValeAnticipo> getValesByLegajo(Integer idLegajo);
	
	public List<ValeAnticipo> buscarVales(Date fechaDesde, Date fechaHasta, String apellidoEmpleado, EEstadoValeAnticipo estado);

	public ValeAnticipo acutalizarVale(ValeAnticipo vale);

	public List<ValeAnticipo> getValesEnEstado(Integer idLegajo, EEstadoValeAnticipo estadoVale);

}
