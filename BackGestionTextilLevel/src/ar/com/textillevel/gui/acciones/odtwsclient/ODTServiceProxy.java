package ar.com.textillevel.gui.acciones.odtwsclient;

public class ODTServiceProxy implements ar.com.textillevel.gui.acciones.odtwsclient.ODTService {
  private String _endpoint = null;
  private ar.com.textillevel.gui.acciones.odtwsclient.ODTService oDTService = null;
  
  public ODTServiceProxy() {
    _initODTServiceProxy();
  }
  
  public ODTServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initODTServiceProxy();
  }
  
  private void _initODTServiceProxy() {
    try {
      oDTService = (new ar.com.textillevel.gui.acciones.odtwsclient.ODTServiceServiceLocator()).getODTServicePort();
      if (oDTService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)oDTService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)oDTService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (oDTService != null)
      ((javax.xml.rpc.Stub)oDTService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ar.com.textillevel.gui.acciones.odtwsclient.ODTService getODTService() {
    if (oDTService == null)
      _initODTServiceProxy();
    return oDTService;
  }
  
  public ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO[] getByIdsEager(int[] arg0) throws java.rmi.RemoteException{
    if (oDTService == null)
      _initODTServiceProxy();
    return oDTService.getByIdsEager(arg0);
  }
  
  public ar.com.textillevel.gui.acciones.odtwsclient.DetallePiezaRemitoEntradaSinSalida[] getInfoPiezasEntradaSinSalidaByClient(java.lang.Integer arg0) throws java.rmi.RemoteException{
    if (oDTService == null)
      _initODTServiceProxy();
    return oDTService.getInfoPiezasEntradaSinSalidaByClient(arg0);
  }
  
  public void recibir(java.lang.String arg0) throws java.rmi.RemoteException{
    if (oDTService == null)
      _initODTServiceProxy();
    oDTService.recibir(arg0);
  }
  
  
}