package ar.com.textillevel.facade.api.remote;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ivaventas.IVAVentasTO;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Remote
public interface FacturaFacadeRemote {

	public Factura guardarFacturaYGenerarMovimiento(Factura factura, List<DibujoEstampado> dibujos, String usuario) throws ValidacionException, ValidacionExceptionSinRollback;
	public Integer getLastNumeroFactura(ETipoFactura tipoFactura, ETipoDocumento tipoDoc);
	public Factura getByNroFactura(Integer nroFactura);
	public Factura getByNroFacturaConItems(Integer nroFactura, Integer nroSucursal);
	public List<Factura> getFacturaImpagaListByClient(Integer idCliente);
	public Factura getByIdEager(Integer id);
	public Factura actualizarFactura(Factura factura) throws FWException;
	public List<Timestamp> getFechasFacturasAnteriorYPosterior(Integer nroFactura, ETipoFactura tipoFactura, ETipoDocumento tipoDoc);
	public void anularFactura(Factura factura, boolean anularRemitoSalida, String usuario) throws ValidacionException, FWException;
	public void cambiarEstadoFactura(Factura factura, EEstadoFactura estadoNuevo, String usuario);
	public IVAVentasTO calcularIVAVentas(Date fechaDesde, Date fechaHasta, ETipoFactura tipoFactura, Cliente cliente);
	public void eliminarFactura(Factura factura, String usrName) throws ValidacionException, FWException;
	public Factura editarFactura(Factura factura, List<DibujoEstampado> dibujos, String usuario) throws ValidacionException;
	public List<Factura> getAllFacturasByCliente(Integer idCliente);
	public Integer getUltimoNumeroFacturaImpreso(ETipoFactura tipoFactura);
	public void pruebaAutorizar();
	public Factura getByIdEagerRemitosEntrada(Integer idFactura);

}
