package ar.com.textillevel.webservices.odt.impl;

import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ar.com.textillevel.webservices.odt.api.remote.RegistrarAvaceODTServiceRemote;

@Stateless
@WebService
public class RegistrarAvaceODTService implements RegistrarAvaceODTServiceRemote{

	private static final Logger logger = Logger.getLogger(RegistrarAvaceODTService.class);
	
	//URL: http://localhost:8080/GTL-gtlback-server/RegistrarAvaceODTService?wsdl
	
	/*
	 Poner estos jars en jboss-salem/lib/endorsed
	 	jaxb-api.jar
		jaxb-impl.jar
		jboss-jaxrpc.jar
		jboss-jaxws.jar
		jboss-saaj.jar
		serializer.jar
		xalan.jar
		xercesImpl.jar
	 */
	
	public void recibir(String codigoBarras) {
		logger.info("Registrando avance");
	}
}
