/**
 * ODTService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

public interface ODTService extends java.rmi.Remote {
    public java.lang.Boolean borrarRemitoDeEntrada(java.lang.Integer arg0) throws java.rmi.RemoteException;
    public ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO[] getByIdsEager(int[] arg0) throws java.rmi.RemoteException;
    public ar.com.textillevel.gui.acciones.odtwsclient.DetallePiezaRemitoEntradaSinSalida[] getInfoPiezasEntradaSinSalidaByClient(java.lang.Integer arg0) throws java.rmi.RemoteException;
    public java.lang.Boolean recibirRemitoEntrada(ar.com.textillevel.gui.acciones.odtwsclient.RemitoEntradaTO arg0) throws java.rmi.RemoteException;
}
