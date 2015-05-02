package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.pagopersona.FacturaPersona;
import ar.com.textillevel.entidades.gente.Persona;

@Remote
public interface FacturaPersonaFacadeRemote {
	public FacturaPersona guardarFactura(FacturaPersona factura, String usuario);
	public boolean existeNroFacturaParaPersona(Integer nroFactura, Persona persona);
	public FacturaPersona getById(Integer idFactura);
	public void eliminarFactura(FacturaPersona factura, String usuario);
	public FacturaPersona editarFactura(FacturaPersona factura, String usuario);
	public void confirmarFactura(FacturaPersona factura, String usrName);
}
