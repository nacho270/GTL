package ar.com.textillevel.web.spring.documentos;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.documentos.to.CorreccionFacturaMobTO;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value="/correccioncliente",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
public class CorreccionFacturaRestController {

	@RequestMapping
	public @ResponseBody CorreccionFacturaMobTO buscarFactura(@RequestParam(value="idCorreccion") Integer idCorreccion) {
		return BeanHelper.getCorreccionFacade().getCorreccionMobById(idCorreccion);
	}

}

