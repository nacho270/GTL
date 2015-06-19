/**
 * ServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente;


public interface ServiceSoap extends java.rmi.Remote {

    /**
     * Retorna la cantidad maxima de registros de detalle que puede
     * tener una invocacion al FEAutorizarRequest.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FERecuperaQTYResponse FERecuperaQTYRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth) throws java.rmi.RemoteException;

    /**
     * Metodo dummy para verificacion basica de funcionamiento.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.DummyResponse FEDummy() throws java.rmi.RemoteException;

    /**
     * Retorna el ultimo comprobante autorizado para el  tipo de comprobante
     * /cuit / punto de venta ingresado.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FERecuperaLastCMPResponse FERecuperaLastCMPRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth, ar.com.textillevel.modulos.fe.cliente.FELastCMPtype argTCMP) throws java.rmi.RemoteException;

    /**
     * Retorna el ultimo número de Request.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FEUltNroResponse FEUltNroRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth) throws java.rmi.RemoteException;

    /**
     * Dado un lote de comprobantes retorna el mismo autorizado con
     * el CAE otorgado.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FEResponse FEAutRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth, ar.com.textillevel.modulos.fe.cliente.requests.FERequest fer) throws java.rmi.RemoteException;

    /**
     * Consulta el CAE.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FEConsultaCAEResponse FEConsultaCAERequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth, ar.com.textillevel.modulos.fe.cliente.requests.FEConsultaCAEReq argCAERequest) throws java.rmi.RemoteException;
}
