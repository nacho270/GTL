package ar.com.textillevel.modulos.odt.dao.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;

@Local
public interface OrdenDeTrabajoDAOLocal extends DAOLocal<OrdenDeTrabajo, Integer> {

	/**
	 * Devuelve una lista de ODTs (para el cliente) que no están asociadas a los remitos. 
	 * @param idCliente
	 * @return lista de ODTs (para el cliente) que no están asociadas a los remitos.
	 */
	public List<OrdenDeTrabajo> getOdtNoAsociadasByClient(Integer idCliente);

	public String getUltimoCodigoODT();

	public List<OrdenDeTrabajo> getOdtEagerByRemitoList(Integer idRemito);

	/**
	 * Devuelve una lista de ODTs asociadas al remito de entrada cuyo id es <code>idRemitoEntrada</code>
	 * @param idRemitoEntrada
	 * @return Una lista de ODTs asociadas al remito de entrada cuyo id es <code>idRemitoEntrada</code>
	 */
	public List<OrdenDeTrabajo> getODTAsociadas(Integer idRemitoEntrada);

	public OrdenDeTrabajo getByIdEager(Integer idODT);

	public List<OrdenDeTrabajo> getByIdsEager(List<Integer> ids);
	
	public List<OrdenDeTrabajo> getOrdenesDeTrabajo(EEstadoODT estado, Date fechaDesde, Date fechaHasta, Cliente cliente);

	public List<OrdenDeTrabajo> getAllEnProceso(Date fechaDesde, Date fechaHasta, Cliente cliente);

	public List<OrdenDeTrabajo> getAllEnProcesoByTipoMaquina(Date fechaDesde, Date fechaHasta, Cliente cliente, Integer idTipoMaquina);

	public Short getUltimoOrdenMaquina(Maquina maquina);

	public OrdenDeTrabajo getByMaquinaYOrden(Integer maquinaActual, Short ordenEnMaquina);
	
	public void updateODT(OrdenDeTrabajo odt);

	public void borrarSecuencia(OrdenDeTrabajo ordenDeTrabajo);

	public void flush();
	
	public OrdenDeTrabajo getODTEagerByCodigo(String codigo);

}
