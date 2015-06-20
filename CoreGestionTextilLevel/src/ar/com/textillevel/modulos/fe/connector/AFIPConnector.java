package ar.com.textillevel.modulos.fe.connector;

import java.net.URL;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import ar.com.textillevel.entidades.documentos.factura.DocumentoContableCliente;
import ar.com.textillevel.modulos.fe.AuthAFIPData;
import ar.com.textillevel.modulos.fe.ConfiguracionAFIPHolder;
import ar.com.textillevel.modulos.fe.cliente.ServiceLocator;
import ar.com.textillevel.modulos.fe.cliente.ServiceSoap;
import ar.com.textillevel.modulos.fe.cliente.requests.FEAuthRequest;
import ar.com.textillevel.modulos.fe.cliente.requests.FERequest;
import ar.com.textillevel.modulos.fe.cliente.responses.FEResponse;

public class AFIPConnector {

	private static final Logger logger = Logger.getLogger(AFIPConnector.class);
	
	private static ServiceSoap servicios;
	private static final AFIPConnector instance = new AFIPConnector();
	public static AFIPConnector getInstance() {
		return instance;
	}
	
	static {
		try{
			if(ConfiguracionAFIPHolder.getInstance().isHabilitado()) {
				ServiceLocator locator = new ServiceLocator();
				servicios = locator.getServiceSoap(new URL(ConfiguracionAFIPHolder.getInstance().getURLNegocio()));
				
			}
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	public DatosRespuestaAFIP autorizarDocumento(DocumentoContableCliente documento) throws RemoteException {
		AuthAFIPData authData = ConfiguracionAFIPHolder.getInstance().getAuthData();
		FERequest request = null;
		FEResponse response = servicios.FEAutRequest(new FEAuthRequest(authData.getToken(), authData.getHash(), authData.getCuitEmpresa()), request);
		String resultado = response.getFecResp().getResultado();
		String motivo = response.getFecResp().getMotivo();
		String reproceso = response.getFecResp().getReproceso();
		String cae = response.getFedResp()[0].getCae();
		return new DatosRespuestaAFIP(resultado, motivo, reproceso, cae);
	}
	
}
