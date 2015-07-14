package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;

@Remote
public interface CorreccionFacadeRemote {

	public CorreccionFactura guardarCorreccionYGenerarMovimiento(CorreccionFactura correccion, String usuario) throws ValidacionException, ValidacionExceptionSinRollback;
	public CorreccionFactura getCorreccionByNumero(Integer idNumero, ETipoCorreccionFactura tipoCorreccion, Integer nroSucursal);
	public CorreccionFactura actualizarCorreccion(CorreccionFactura correccion) throws CLException;
	public List<NotaCredito> getNotaCreditoPendienteUsarList(Integer idCliente);
	public void cambiarVerificacionCorreccion(CorreccionFactura correccion, boolean verificada, String usrName);
	public NotaCredito getNotaDeCreditoByFacturaRelacionada(Factura factura);
	public void eliminarCorreccion(CorreccionFactura correccion, String usrName) throws CLException, ValidacionException;
	public CorreccionFactura editarCorreccion(CorreccionFactura correccion, String usuario) throws ValidacionException;
	public void anularCorreccion(CorreccionFactura correccion, String usrName) throws CLException, ValidacionException;
	public CorreccionFactura getCorreccionById(Integer idCorreccion);

}
