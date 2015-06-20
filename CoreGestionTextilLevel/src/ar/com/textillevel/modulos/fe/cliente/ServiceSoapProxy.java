package ar.com.textillevel.modulos.fe.cliente;

public class ServiceSoapProxy implements ar.com.textillevel.modulos.fe.cliente.ServiceSoap {
  private String _endpoint = null;
  private ar.com.textillevel.modulos.fe.cliente.ServiceSoap serviceSoap = null;
  
  public ServiceSoapProxy() {
    _initServiceSoapProxy();
  }
  
  public ServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initServiceSoapProxy();
  }
  
  private void _initServiceSoapProxy() {
    try {
      serviceSoap = (new ar.com.textillevel.modulos.fe.cliente.ServiceLocator()).getServiceSoap();
      if (serviceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)serviceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)serviceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (serviceSoap != null)
      ((javax.xml.rpc.Stub)serviceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ar.com.textillevel.modulos.fe.cliente.ServiceSoap getServiceSoap() {
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap;
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FECAEResponse FECAESolicitar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, ar.com.textillevel.modulos.fe.cliente.requests.FECAERequest feCAEReq) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FECAESolicitar(auth, feCAEReq);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FERegXReqResponse FECompTotXRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FECompTotXRequest(auth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.DummyResponse FEDummy() throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEDummy();
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FERecuperaLastCbteResponse FECompUltimoAutorizado(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, int ptoVta, int cbteTipo) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FECompUltimoAutorizado(auth, ptoVta, cbteTipo);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FECompConsultaResponse FECompConsultar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, ar.com.textillevel.modulos.fe.cliente.requests.FECompConsultaReq feCompConsReq) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FECompConsultar(auth, feCompConsReq);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FECAEAResponse FECAEARegInformativo(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, ar.com.textillevel.modulos.fe.cliente.requests.FECAEARequest feCAEARegInfReq) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FECAEARegInformativo(auth, feCAEARegInfReq);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FECAEAGetResponse FECAEASolicitar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, int periodo, short orden) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FECAEASolicitar(auth, periodo, orden);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FECAEASinMovConsResponse FECAEASinMovimientoConsultar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, java.lang.String CAEA, int ptoVta) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FECAEASinMovimientoConsultar(auth, CAEA, ptoVta);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FECAEASinMovResponse FECAEASinMovimientoInformar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, int ptoVta, java.lang.String CAEA) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FECAEASinMovimientoInformar(auth, ptoVta, CAEA);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FECAEAGetResponse FECAEAConsultar(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, int periodo, short orden) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FECAEAConsultar(auth, periodo, orden);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FECotizacionResponse FEParamGetCotizacion(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth, java.lang.String monId) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetCotizacion(auth, monId);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FETributoResponse FEParamGetTiposTributos(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetTiposTributos(auth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.MonedaResponse FEParamGetTiposMonedas(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetTiposMonedas(auth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.IvaTipoResponse FEParamGetTiposIva(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetTiposIva(auth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.OpcionalTipoResponse FEParamGetTiposOpcional(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetTiposOpcional(auth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.ConceptoTipoResponse FEParamGetTiposConcepto(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetTiposConcepto(auth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FEPtoVentaResponse FEParamGetPtosVenta(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetPtosVenta(auth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.CbteTipoResponse FEParamGetTiposCbte(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetTiposCbte(auth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.DocTipoResponse FEParamGetTiposDoc(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetTiposDoc(auth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FEPaisResponse FEParamGetTiposPaises(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest auth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEParamGetTiposPaises(auth);
  }
  
  
}