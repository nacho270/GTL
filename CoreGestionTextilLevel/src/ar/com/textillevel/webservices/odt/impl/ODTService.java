package ar.com.textillevel.webservices.odt.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.local.OrdenDeTrabajoFacadeLocal;
import ar.com.textillevel.modulos.odt.to.intercambio.ODTEagerTO;
import ar.com.textillevel.webservices.odt.api.remote.ODTServiceRemote;

@Stateless
@WebService
public class ODTService implements ODTServiceRemote {

	private static final Logger logger = Logger.getLogger(ODTService.class);
	
	@EJB
	private OrdenDeTrabajoFacadeLocal odtFacade;
	
	//URL: http://localhost:8080/GTL-gtlback-server/ODTService?wsdl
	
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

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) {
		return odtFacade.getInfoPiezasEntradaCompletoSinSalidaByClient(idCliente);
	}

	public List<ODTEagerTO> getByIdsEager(List<Integer> ids) {
		List<ODTEagerTO> lista = new ArrayList<ODTEagerTO>();
		for(OrdenDeTrabajo odt : odtFacade.getByIdsEager(ids)) {
			lista.add(new ODTEagerTO(odt));
		}
		return lista;
	}

}
