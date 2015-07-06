package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Cliente;

@Local
public interface FacturaDAOLocal extends DAOLocal<Factura, Integer> {

	public Integer getLastNumeroFactura(ETipoFactura tipoFactura, ETipoDocumento tipoDoc, Integer nroSucursal);

	public Factura getByNroFacturaConCorrecciones(Integer nroFactura, Integer nroSucursal);
	
	public Factura getByNroFacturaConItems(Integer nroFactura, Integer nroSucursal);

	public List<Factura> getFacturaImpagaListByClient(Integer idCliente, Integer nroSucursal);
	
	public Factura getByIdEager(Integer id);

	public List<Timestamp> getFacturaAnteriorYPosterior(Integer nroFactura, ETipoFactura tipoFactura, ETipoDocumento tipoDoc, Integer nroSucursal);

	public boolean facturaYaTieneRecibo(Factura factura);
	
	public List<Factura> getFacturasEntreFechas(Date fechaDesde, Date fechaHasta, ETipoFactura tipoFactura, Cliente cl, Integer nroSucursal);

	public boolean esLaUltimaFactura(Factura factura, Integer nroSucursal);

	public List<Factura> getFacturaByRemitoSalida(Integer idRemitoSalida);

	public List<Factura> getAllByIdClienteList(Integer idCliente, Integer nroSucursal);

	public List<Factura> getAllFacturasByCliente(Integer idCliente, Integer nroSucursal);

	public Integer getUltimoNumeroFacturaImpreso(ETipoFactura tipoFactura, Integer nroSucursal);

}
