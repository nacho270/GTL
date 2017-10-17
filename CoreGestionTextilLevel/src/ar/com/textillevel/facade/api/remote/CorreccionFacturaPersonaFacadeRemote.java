package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.pagopersona.CorreccionFacturaPersona;
import ar.com.textillevel.entidades.documentos.pagopersona.NotaDebitoPersona;
import ar.com.textillevel.entidades.gente.Persona;

@Remote
public interface CorreccionFacturaPersonaFacadeRemote {

	public void confirmarND(NotaDebitoPersona notaDebitoPersona, String usrName);

	public void eliminarCorreccion(CorreccionFacturaPersona correccionFacturaPersona, String usrName);

	public NotaDebitoPersona getNDPersonaByIdEager(Integer idND);

	public boolean existeNroNDParaPersona(Integer value, Persona persona);

	public CorreccionFacturaPersona guardarCorreccionYGenerarMovimiento(CorreccionFacturaPersona correccion, String usuario, String obsMovimiento);

}
