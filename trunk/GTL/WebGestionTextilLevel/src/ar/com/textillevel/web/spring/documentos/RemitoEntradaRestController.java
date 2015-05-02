package ar.com.textillevel.web.spring.documentos;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.documentos.remito.to.RemitoEntradaMobileTO;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value="/remitoE",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
public class RemitoEntradaRestController {

	@RequestMapping
	public @ResponseBody RemitoEntradaMobileTO buscarRemito(@RequestParam(value="idRE") Integer idRE) {
		List<OrdenDeTrabajo> odtList = BeanHelper.getODTFacadeLocal().getOdtEagerByRemitoList(idRE);
		return new RemitoEntradaMobileTO(BeanHelper.geRemitoEntradaFacade().getByIdEager(idRE),odtList);
	}
	
	@RequestMapping(value="/byNro")
	public @ResponseBody RemitoEntradaMobileTO buscarRemitoByNro(@RequestParam(value="nroRE") Integer nroRE) {
//		List<OrdenDeTrabajo> odtList = BeanHelper.getODTFacadeLocal().getOdtEagerByRemitoList(nroRE);
//		return new RemitoEntradaMobileTO(BeanHelper.geRemitoEntradaFacade().getByIdEager(nroRE),odtList);
		
		//TODO: PUEDE HABER VARIOS REMITOS CON EL MISMO NUMERO, REQUIERE NUMERO DE CLIENTE... VER SI LO QUIERE SALEM
		
		return null;
	}
}
