package ar.com.textillevel.gui.acciones.entregreingdocwsclient;

public class TerminalServiceProxy implements ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalService {
    private String _endpoint = null;
    private ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalService terminalService = null;

    public TerminalServiceProxy() {
        _initTerminalServiceProxy();
    }

    public TerminalServiceProxy(String endpoint) {
        _endpoint = endpoint;
        _initTerminalServiceProxy();
    }

    private void _initTerminalServiceProxy() {
        try {
            terminalService = (new ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceServiceLocator())
                            .getTerminalServicePort();
            if (terminalService != null) {
                if (_endpoint != null) {
                    ((javax.xml.rpc.Stub) terminalService)._setProperty("javax.xml.rpc.service.endpoint.address",
                                    _endpoint);
                } else {
                    _endpoint = (String) ((javax.xml.rpc.Stub) terminalService)
                                    ._getProperty("javax.xml.rpc.service.endpoint.address");
                }
            }

        } catch (final javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (terminalService != null) {
            ((javax.xml.rpc.Stub) terminalService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        }

    }

    public ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalService getTerminalService() {
        if (terminalService == null) {
            _initTerminalServiceProxy();
        }
        return terminalService;
    }

    @Override
    public ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceResponse marcarEntregado(java.lang.String arg0,
                    java.lang.String arg1) throws java.rmi.RemoteException {
        if (terminalService == null) {
            _initTerminalServiceProxy();
        }
        return terminalService.marcarEntregado(arg0, arg1);
    }

    @Override
    public ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceResponse reingresar(java.lang.String arg0,
                    java.lang.String arg1) throws java.rmi.RemoteException {
        if (terminalService == null) {
            _initTerminalServiceProxy();
        }
        return terminalService.reingresar(arg0, arg1);
    }

}
