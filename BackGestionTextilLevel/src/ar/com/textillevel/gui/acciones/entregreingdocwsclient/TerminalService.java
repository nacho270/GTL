package ar.com.textillevel.gui.acciones.entregreingdocwsclient;

public interface TerminalService extends java.rmi.Remote {
	
    public ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceResponse marcarEntregado(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalServiceResponse reingresar(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;

}