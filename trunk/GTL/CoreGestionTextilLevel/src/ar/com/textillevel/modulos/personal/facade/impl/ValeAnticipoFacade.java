package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.modulos.personal.dao.api.EmpleadoDAOLocal;
import ar.com.textillevel.modulos.personal.dao.api.ValeAnticipoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAnticipoFacadeRemote;

@Stateless
public class ValeAnticipoFacade implements ValeAnticipoFacadeRemote {

	@EJB
	private ValeAnticipoDAOLocal valeDao;
	
	@EJB
	private EmpleadoDAOLocal empleadoDao;

	public ValeAnticipo save(ValeAnticipo vale, Empleado empleado) {
		empleado = empleadoDao.save(empleado);
		vale.setLegajo(empleado.getLegajo());
		return valeDao.save(vale);
	}

	public List<ValeAnticipo> getValesByLegajo(Integer idLegajo) {
		return valeDao.getValesByLegajo(idLegajo);
	}

	public List<ValeAnticipo> buscarVales(Date fechaDesde, Date fechaHasta, String apellidoEmpleado,  EEstadoValeAnticipo estado) {
		return valeDao.buscarVales(fechaDesde,fechaHasta,apellidoEmpleado,estado);
	}

	public ValeAnticipo acutalizarVale(ValeAnticipo vale) {
		return valeDao.save(vale);
	}

	public List<ValeAnticipo> getValesEnEstado(Integer idLegajo, EEstadoValeAnticipo estadoVale) {
		return valeDao.getValesEnEstado(idLegajo, estadoVale);
	}
}
