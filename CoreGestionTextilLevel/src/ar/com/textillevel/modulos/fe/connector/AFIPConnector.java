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
import ar.com.textillevel.modulos.fe.cliente.requests.FECAERequest;
import ar.com.textillevel.modulos.fe.cliente.responses.DocTipoResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.DummyResponse;
import ar.com.textillevel.modulos.fe.cliente.responses.FECAEResponse;

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
		FECAERequest request = null;
		FECAEResponse response = servicios.FECAESolicitar(new FEAuthRequest(authData.getToken(), authData.getHash(), authData.getCuitEmpresa()), request);
		String resultado = response.getFeCabResp().getResultado();
		String reproceso = response.getFeCabResp().getReproceso();
		String cae = response.getFeDetResp()[0].getCAE();
		return new DatosRespuestaAFIP(resultado, reproceso, cae);
	}
	
	public DocTipoResponse getTiposDoc() throws RemoteException{
		AuthAFIPData authData = ConfiguracionAFIPHolder.getInstance().getAuthData();
		return servicios.FEParamGetTiposDoc(new FEAuthRequest(authData.getToken(), authData.getHash(), authData.getCuitEmpresa()));
	}
	
	public DummyResponse funciona() throws RemoteException{
		return servicios.FEDummy();
	}
}
