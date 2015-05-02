package ar.com.textillevel.web.spring.documentos;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.to.ReciboMobileTO;
import ar.com.textillevel.web.spring.exception.GTLException;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value="/recibo",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
public class ReciboRestController {
	
	@RequestMapping
	public @ResponseBody ReciboMobileTO buscarRecibo(@RequestParam(value="idRecibo") Integer idRecibo) {
		return new ReciboMobileTO(BeanHelper.getReciboFacade().getByIdEager(idRecibo));
	}
	
	@RequestMapping(value="/byNro")
	public @ResponseBody ReciboMobileTO buscarReciboByNro(@RequestParam(value="nroRecibo") Integer nroRecibo) {
		Recibo recibo = BeanHelper.getReciboFacade().getByNroReciboEager(nroRecibo);
		if(recibo == null){
			throw new GTLException(777,"No se encontró el recibo");
		}
		return new ReciboMobileTO(recibo);
	}
}
