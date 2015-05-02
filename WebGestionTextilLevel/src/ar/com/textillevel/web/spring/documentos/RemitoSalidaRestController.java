package ar.com.textillevel.web.spring.documentos;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ar.com.textillevel.entidades.documentos.remito.to.RemitoSalidaMobileTO;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value="/remitoS",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
public class RemitoSalidaRestController {

	@RequestMapping
	public @ResponseBody RemitoSalidaMobileTO buscarRemitoSalida(@RequestParam(value="idRS") Integer idRS) {
		return new RemitoSalidaMobileTO(BeanHelper.getRemitoSalidaFacadeLocal().getByIdConPiezasYProductos(idRS));
	}

	@RequestMapping(value="/byNro")
	public @ResponseBody RemitoSalidaMobileTO buscarRemitoSalidaByNro(@RequestParam(value="nroRS") Integer nroRS) {
		//TODO:
		return null;
	}

}
