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
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FERecuperaQTYResponse FERecuperaQTYRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FERecuperaQTYRequest(argAuth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.DummyResponse FEDummy() throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEDummy();
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FERecuperaLastCMPResponse FERecuperaLastCMPRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth, ar.com.textillevel.modulos.fe.cliente.FELastCMPtype argTCMP) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FERecuperaLastCMPRequest(argAuth, argTCMP);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FEUltNroResponse FEUltNroRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEUltNroRequest(argAuth);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FEResponse FEAutRequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth, ar.com.textillevel.modulos.fe.cliente.requests.FERequest fer) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEAutRequest(argAuth, fer);
  }
  
  public ar.com.textillevel.modulos.fe.cliente.responses.FEConsultaCAEResponse FEConsultaCAERequest(ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest argAuth, ar.com.textillevel.modulos.fe.cliente.requests.FEConsultaCAEReq argCAERequest) throws java.rmi.RemoteException{
    if (serviceSoap == null)
      _initServiceSoapProxy();
    return serviceSoap.FEConsultaCAERequest(argAuth, argCAERequest);
  }
  
  
}