package ar.com.textillevel.gui.acciones.entregreingdocwsclient;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class TerminalServiceServiceLocator extends org.apache.axis.client.Service
                implements ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceService {

    public TerminalServiceServiceLocator() {
    }

    public TerminalServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TerminalServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName)
                    throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TerminalServicePort
    private java.lang.String TerminalServicePort_address = "http://dev-gtl:8080/GTL-gtlback-server/TerminalService";

    @Override
    public java.lang.String getTerminalServicePortAddress() {
        return TerminalServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TerminalServicePortWSDDServiceName = "TerminalServicePort";

    public java.lang.String getTerminalServicePortWSDDServiceName() {
        return TerminalServicePortWSDDServiceName;
    }

    public void setTerminalServicePortWSDDServiceName(java.lang.String name) {
        TerminalServicePortWSDDServiceName = name;
    }

    @Override
    public ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalService getTerminalServicePort()
                    throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TerminalServicePort_address);
        } catch (final java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTerminalServicePort(endpoint);
    }

    @Override
    public ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalService getTerminalServicePort(java.net.URL portAddress)
                    throws javax.xml.rpc.ServiceException {
        try {
            final ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceBindingStub _stub = new ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceBindingStub(
                            portAddress, this);
            _stub.setPortName(getTerminalServicePortWSDDServiceName());
            _stub.setTimeout(10000); // 10 segundos
            return _stub;
        } catch (final org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTerminalServicePortEndpointAddress(java.lang.String address) {
        TerminalServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    @Override
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalService.class
                            .isAssignableFrom(serviceEndpointInterface)) {
                final ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceBindingStub _stub = new ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceBindingStub(
                                new java.net.URL(TerminalServicePort_address), this);
                _stub.setPortName(getTerminalServicePortWSDDServiceName());
                return _stub;
            }
        } catch (final java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
                        + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    @Override
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
                    throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        final java.lang.String inputPortName = portName.getLocalPart();
        if ("TerminalServicePort".equals(inputPortName)) {
            return getTerminalServicePort();
        } else {
            final java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    @Override
    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.terminal.webservices.textillevel.com.ar/",
                        "TerminalServiceService");
    }

    private java.util.HashSet ports = null;

    @Override
    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.terminal.webservices.textillevel.com.ar/",
                            "TerminalServicePort"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address)
                    throws javax.xml.rpc.ServiceException {

        if ("TerminalServicePort".equals(portName)) {
            setTerminalServicePortEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address)
                    throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}