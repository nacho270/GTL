package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.pagopersona.CorreccionFacturaPersona;

@Local
public interface CorreccionFacturaPersonaFacadeLocal {

	public CorreccionFacturaPersona guardarCorreccionYGenerarMovimiento(CorreccionFacturaPersona correccion, String usuario, String obsMovimiento);

	/*
	public CorreccionFacturaProveedor actualizarCorreccion(CorreccionFacturaProveedor correccion);
	public void borrarCorreccionFacturaProveedor(CorreccionFacturaProveedor correFacturaProveedor, String usuario) throws ValidacionException;
	public CorreccionFacturaProveedor obtenerNotaDeDebitoByCheque(Cheque c);
	public CorreccionFacturaProveedor getCorreccionFacturaByIdEager(Integer id);
	*/

}
