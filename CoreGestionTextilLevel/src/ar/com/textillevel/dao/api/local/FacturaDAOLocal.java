package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Cliente;

@Local
public interface FacturaDAOLocal extends DAOLocal<Factura, Integer>{

	public Integer getLastNumeroFactura(ETipoFactura tipoFactura);

	public Factura getByNroFacturaConCorrecciones(Integer nroFactura);
	
	public Factura getByNroFacturaConItems(Integer nroFactura);

	public List<Factura> getFacturaImpagaListByClient(Integer idCliente);
	
	public Factura getByIdEager(Integer id);

	public List<Timestamp> getFacturaAnteriorYPosterior(Integer nroFactura, ETipoFactura tipoFactura);

	public boolean facturaYaTieneRecibo(Factura factura);
	
	public List<Factura> getFacturasEntreFechas(Date fechaDesde, Date fechaHasta, ETipoFactura tipoFactura, Cliente cl);

	public boolean esLaUltimaFactura(Factura factura);

	public List<Factura> getFacturaByRemitoSalida(Integer idRemitoSalida);

	public List<Factura> getAllByIdClienteList(Integer idCliente);

	public List<Factura> getAllFacturasByCliente(Integer idCliente);

	public Integer getUltimoNumeroFacturaImpreso(ETipoFactura tipoFactura);

}
