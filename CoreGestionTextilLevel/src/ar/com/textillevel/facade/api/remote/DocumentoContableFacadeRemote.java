package ar.com.textillevel.facade.api.remote;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.modulos.fe.cliente.responses.CbteTipoResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.DocTipoResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.FECompConsultaResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.IvaTipoResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.MonedaResponse;
import ar.com.textillevel.modulos.fe.to.EstadoServidorAFIP;

@Remote
public interface DocumentoContableFacadeRemote {

	public Integer getProximoNroDocumentoContable(EPosicionIVA posIva, ETipoDocumento tipoDoc);

	public List<DocumentoContableCliente> getDocumentosContablesSinCAE();

	public <D extends DocumentoContableCliente> D autorizarDocumentoContableAFIP(D docContable) throws ValidacionExceptionSinRollback, ValidacionException;

	public EstadoServidorAFIP getEstadoServidorAFIP(int nroSucursal) throws ValidacionException;

	public void checkImpresionDocumentoContable(DocumentoContableCliente documento) throws ValidacionException;

	public Long getCuitEmpresa();

	/* METODOS DE AFIP PARA DIALOGO DE EJECUCION */
	public DocTipoResponse getTiposDoc() throws RemoteException;
	public CbteTipoResponse getTiposComprobante() throws RemoteException;
	public MonedaResponse getTiposMoneda() throws RemoteException;
	public IvaTipoResponse getTiposIVA() throws RemoteException;
	public FECompConsultaResponse consultarDatosDocumentoIngresado(int idTipoComprobanteAFIP, int nroComprobante) throws RemoteException;

}
