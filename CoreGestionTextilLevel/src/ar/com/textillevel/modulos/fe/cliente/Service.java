/**
 * Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente;

public interface Service extends javax.xml.rpc.Service {

/**
 * AFIP Web Service de Facturacion Electronica - Version 1
 */
    public java.lang.String getServiceSoapAddress();

    public ar.com.textillevel.modulos.fe.cliente.ServiceSoap getServiceSoap() throws javax.xml.rpc.ServiceException;

    public ar.com.textillevel.modulos.fe.cliente.ServiceSoap getServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
