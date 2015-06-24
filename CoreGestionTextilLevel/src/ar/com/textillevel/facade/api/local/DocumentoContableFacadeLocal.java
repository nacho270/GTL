package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.entidades.enums.EPosicionIVA;

@Local
public interface DocumentoContableFacadeLocal {

	public Integer getProximoNroDocumentoContable(EPosicionIVA posIva) ;

	public <D extends DocumentoContableCliente> D autorizarDocumentoContableAFIP(D docContable) throws ValidacionExceptionSinRollback, ValidacionException;

	public void checkAutorizacionAFIP(DocumentoContableCliente docContable) throws ValidacionException;

}
