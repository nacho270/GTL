package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.modulos.fe.EstadoServidorAFIP;

@Remote
public interface DocumentoContableFacadeRemote {

	public Integer getProximoNroDocumentoContable(EPosicionIVA posIva);
	public List<DocumentoContableCliente> getDocumentosContablesSinCAE();
	public <D extends DocumentoContableCliente> D autorizarDocumentoContableAFIP(D docContable) throws ValidacionExceptionSinRollback, ValidacionException;
	public EstadoServidorAFIP getEstadoServidorAFIP() throws ValidacionException;
	public void checkImpresionDocumentoContable(DocumentoContableCliente documento) throws ValidacionException;
	
}
