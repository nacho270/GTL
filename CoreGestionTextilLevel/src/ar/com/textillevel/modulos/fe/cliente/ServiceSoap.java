/**
 * ServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente;

public interface ServiceSoap extends java.rmi.Remote {

    /**
     * Solicitud de Código de Autorización Electrónico (CAE)
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FECAEResponse FECAESolicitar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, ar.com.textillevel.modulos.fe.cliente.requests.FECAERequest feCAEReq) throws java.rmi.RemoteException;

    /**
     * Retorna la cantidad maxima de registros que puede tener una
     * invocacion al metodo FECAESolicitar / FECAEARegInformativo
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FERegXReqResponse FECompTotXRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;

    /**
     * Metodo dummy para verificacion de funcionamiento
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.DummyResponse FEDummy() throws java.rmi.RemoteException;

    /**
     * Retorna el ultimo comprobante autorizado para el tipo de comprobante
     * / cuit / punto de venta ingresado / Tipo de Emisión
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FERecuperaLastCbteResponse FECompUltimoAutorizado(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, int ptoVta, int cbteTipo) throws java.rmi.RemoteException;

    /**
     * Consulta Comprobante emitido y su código.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FECompConsultaResponse FECompConsultar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, ar.com.textillevel.modulos.fe.cliente.requests.FECompConsultaReq feCompConsReq) throws java.rmi.RemoteException;

    /**
     * Rendición de comprobantes asociados a un CAEA.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FECAEAResponse FECAEARegInformativo(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, ar.com.textillevel.modulos.fe.cliente.requests.FECAEARequest feCAEARegInfReq) throws java.rmi.RemoteException;

    /**
     * Solicitud de Código de Autorización Electrónico Anticipado
     * (CAEA)
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FECAEAGetResponse FECAEASolicitar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, int periodo, short orden) throws java.rmi.RemoteException;

    /**
     * Consulta CAEA informado como sin movimientos.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FECAEASinMovConsResponse FECAEASinMovimientoConsultar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, java.lang.String CAEA, int ptoVta) throws java.rmi.RemoteException;

    /**
     * Informa CAEA sin movimientos.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FECAEASinMovResponse FECAEASinMovimientoInformar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, int ptoVta, java.lang.String CAEA) throws java.rmi.RemoteException;

    /**
     * Consultar CAEA emitidos.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FECAEAGetResponse FECAEAConsultar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, int periodo, short orden) throws java.rmi.RemoteException;

    /**
     * Recupera la cotizacion de la moneda consultada y su  fecha
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FECotizacionResponse FEParamGetCotizacion(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, java.lang.String monId) throws java.rmi.RemoteException;

    /**
     * Recupera el listado de los diferente tributos que pueden ser
     * utilizados  en el servicio de autorizacion
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FETributoResponse FEParamGetTiposTributos(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;

    /**
     * Recupera el listado de monedas utilizables en servicio de autorización
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.MonedaResponse FEParamGetTiposMonedas(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;

    /**
     * Recupera el listado  de Tipos de Iva utilizables en servicio
     * de autorización.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.IvaTipoResponse FEParamGetTiposIva(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;

    /**
     * Recupera el listado de identificadores para los campos Opcionales
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.OpcionalTipoResponse FEParamGetTiposOpcional(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;

    /**
     * Recupera el listado  de identificadores para el campo Concepto.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.ConceptoTipoResponse FEParamGetTiposConcepto(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;

    /**
     * Recupera el listado de puntos de venta registrados y su estado
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FEPtoVentaResponse FEParamGetPtosVenta(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;

    /**
     * Recupera el listado  de Tipos de Comprobantes utilizables en
     * servicio de autorización.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.CbteTipoResponse FEParamGetTiposCbte(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;

    /**
     * Recupera el listado  de Tipos de Documentos utilizables en
     * servicio de autorización.
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.DocTipoResponse FEParamGetTiposDoc(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;

    /**
     * Recupera el listado de los diferente paises que pueden ser
     * utilizados  en el servicio de autorizacion
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FEPaisResponse FEParamGetTiposPaises(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException;
}
