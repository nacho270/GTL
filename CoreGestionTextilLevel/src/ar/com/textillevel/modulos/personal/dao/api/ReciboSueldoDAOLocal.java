package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.fwcommon.entidades.Mes;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EQuincena;

@Local
public interface ReciboSueldoDAOLocal extends DAOLocal<ReciboSueldo, Integer> {

	public ReciboSueldo getReciboSueldo(LegajoEmpleado legajo, Integer anio, Mes mes, EQuincena quincena);

	public ReciboSueldo getByIdEager(Integer idReciboSueldo);

	public ReciboSueldo getReciboSueldoByTextoOrden(Integer idLegajo, String textoOrden);

}
