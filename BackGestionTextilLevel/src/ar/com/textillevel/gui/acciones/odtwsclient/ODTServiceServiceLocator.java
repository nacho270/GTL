/**
 * ODTServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"unchecked", "serial", "rawtypes"})
public class ODTServiceServiceLocator extends org.apache.axis.client.Service implements ar.com.textillevel.gui.acciones.odtwsclient.ODTServiceService {

    public ODTServiceServiceLocator() {
    }


    public ODTServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ODTServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ODTServicePort
    private java.lang.String ODTServicePort_address = "http://dev-gtl:8080/GTL-gtlback-server/ODTService";

    public java.lang.String getODTServicePortAddress() {
        return ODTServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ODTServicePortWSDDServiceName = "ODTServicePort";

    public java.lang.String getODTServicePortWSDDServiceName() {
        return ODTServicePortWSDDServiceName;
    }

    public void setODTServicePortWSDDServiceName(java.lang.String name) {
        ODTServicePortWSDDServiceName = name;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.ODTService getODTServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ODTServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getODTServicePort(endpoint);
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.ODTService getODTServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ar.com.textillevel.gui.acciones.odtwsclient.ODTServiceBindingStub _stub = new ar.com.textillevel.gui.acciones.odtwsclient.ODTServiceBindingStub(portAddress, this);
            _stub.setPortName(getODTServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setODTServicePortEndpointAddress(java.lang.String address) {
        ODTServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ar.com.textillevel.gui.acciones.odtwsclient.ODTService.class.isAssignableFrom(serviceEndpointInterface)) {
                ar.com.textillevel.gui.acciones.odtwsclient.ODTServiceBindingStub _stub = new ar.com.textillevel.gui.acciones.odtwsclient.ODTServiceBindingStub(new java.net.URL(ODTServicePort_address), this);
                _stub.setPortName(getODTServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ODTServicePort".equals(inputPortName)) {
            return getODTServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "ODTServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "ODTServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ODTServicePort".equals(portName)) {
            setODTServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
