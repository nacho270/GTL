package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

@Local
public interface RemitoSalidaDAOLocal extends DAOLocal<RemitoSalida, Integer> {

	public Integer getLastNroRemito();
	public RemitoSalida getByIdConPiezasYProductos(Integer id);
	public RemitoSalida getByNroRemitoConPiezasYProductos(Integer nroRemito);
	
	/**
	 * Devuelve el remito de salida con piezas y productos aunque est� anulado.
	 * @param nroRemito
	 * @return
	 */
	public RemitoSalida getByNroRemitoConPiezasYProductosAnulado(Integer nroRemito);
	public Integer getUltimoNumeroFactura(EPosicionIVA posIva);
	public List<RemitoSalida> getRemitosByClienteYFecha(Date fechaDesde, Date fechaHasta, Cliente cliente);
	
	/**
	 * Devuelve los remitos de salidas asociados a una odt
	 * @param odts
	 * @return los remitos de salidas asociados a una odt
	 */
	public List<RemitoSalida> getRemitosByODT(OrdenDeTrabajo odt);
	public List<RemitoSalida> getRemitoSalidaByFechasAndCliente(Date fechaDesde, Date fechaHasta, Integer idCliente);
	public List<RemitoSalida> getRemitoSalidaByFechasAndProveedor(Date fechaDesde, Date fechaHasta, Integer idProveedor);
	public List<RemitoSalida> getRemitosConNumerosDeFacturaMenorA(Integer nroDesde, Date fechaDesde);
	public List<RemitoSalida> getRemitosSalidaSinFacturaPorCliente(Cliente cliente);
	public void borrarAsociacionNotaCredito(NotaCreditoProveedor ncp);
	public List<RemitoSalida> getRemitosByNroRemitoConPiezasYProductos(Integer nroRemito);

}
