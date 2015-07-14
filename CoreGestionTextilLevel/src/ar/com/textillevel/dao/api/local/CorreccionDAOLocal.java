package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.entidades.gente.Cliente;

@Local
public interface CorreccionDAOLocal extends DAOLocal<CorreccionFactura, Integer>{

	public CorreccionFactura getCorreccionByNumero(Integer idNumero, ETipoCorreccionFactura tipoCorreccion, Integer nroSucursal);
	public List<CorreccionFactura> getCorreccionesByFecha(Date fechaDesde, Date fechaHasta, Cliente cl, Integer nroSucursal);
	public List<NotaCredito> getNotaCreditoPendienteUsarList(Integer idCliente, Integer nroSucursal);
	public NotaCredito getNotaDeCreditoByFacturaRelacionada(Factura factura);
	public boolean notaDebitoSeUsaEnRecibo(NotaDebito nd);
	public boolean notaCreditoSeUsaEnRecibo(NotaCredito nc);
	public List<NotaCredito> getAllNotaCreditoList(Integer idCliente, Integer nroSucursal);
	public NotaDebito getNotaDebitoByCheque(Cheque cheque);
	public CorreccionFactura getCorreccionById(Integer idCorreccion);

}
