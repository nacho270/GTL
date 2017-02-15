package ar.com.textillevel.gui.acciones.entregreingdocwsclient;

public interface TerminalServiceService extends javax.xml.rpc.Service {
    public java.lang.String getTerminalServicePortAddress();

    public ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalService getTerminalServicePort()
                    throws javax.xml.rpc.ServiceException;

    public ar.com.textillevel.gui.acciones.entregreingdocwsclient.TerminalService getTerminalServicePort(java.net.URL portAddress)
                    throws javax.xml.rpc.ServiceException;
}