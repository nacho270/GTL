package ar.com.textillevel.modulos.personal.dao.api;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

@Local
public interface ValeAnticipoDAOLocal extends DAOLocal<ValeAnticipo, Integer> {

	public List<ValeAnticipo> getValesByLegajo(Integer idLegajo);

	public List<ValeAnticipo> buscarVales(Date fechaDesde, Date fechaHasta, String apellidoEmpleado, EEstadoValeAnticipo estado);

	public List<ValeAnticipo> getValesEnEstado(Integer idLegajo, EEstadoValeAnticipo estadoVale);

}
