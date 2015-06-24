package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.entidades.enums.EPosicionIVA;

@Remote
public interface DocumentoContableFacadeRemote {

	public Integer getProximoNroDocumentoContable(EPosicionIVA posIva);
	public List<DocumentoContableCliente> getDocumentosContablesSinCAE();
	public void autorizarMultiplesDocumentosAFIP(List<DocumentoContableCliente> documentos) throws ValidacionExceptionSinRollback, ValidacionException;
}
