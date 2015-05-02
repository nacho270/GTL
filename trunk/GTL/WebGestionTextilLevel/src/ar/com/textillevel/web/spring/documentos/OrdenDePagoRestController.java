package ar.com.textillevel.web.spring.documentos;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.to.OrdenDePagoMobileTO;
import ar.com.textillevel.web.spring.exception.GTLException;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value="/odp",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
public class OrdenDePagoRestController {
	
	@RequestMapping
	public @ResponseBody OrdenDePagoMobileTO buscarODP(@RequestParam(value="idODP") Integer idODP) {
		return new OrdenDePagoMobileTO(BeanHelper.getOrdenDePagoFacade().getByIdEager(idODP));
	}
	
	@RequestMapping(value="/byNro")
	public @ResponseBody OrdenDePagoMobileTO buscarODPByNro(@RequestParam(value="nroODP") Integer nroODP) {
		OrdenDePago odp = BeanHelper.getOrdenDePagoFacade().getOrdenDePagoByNroOrdenEager(nroODP);
		if(odp == null){
			throw new GTLException(777,"No se encontró la orden de pago");
		}
		return new OrdenDePagoMobileTO(odp);
	}
}
