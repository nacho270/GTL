package ar.com.textillevel.web.spring.documentos;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.to.FacturaMobTO;
import ar.com.textillevel.web.spring.exception.GTLException;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value="/factura",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
public class FacturaRestController {
	
	@RequestMapping
	public @ResponseBody FacturaMobTO buscarFactura(@RequestParam(value="idFactura") Integer idFactura) {
		Factura factura = BeanHelper.getFacturaFacade().getByIdEager(idFactura);
		if(factura == null){
			throw new GTLException(777, "No se encontró la factura");
		}
		return new FacturaMobTO(factura, BeanHelper.getParametrosGeneralesFacade().getParametrosGenerales());
	}

	@RequestMapping(value="/byNro")
	public @ResponseBody FacturaMobTO buscarFacturaPorNumero(@RequestParam(value="nroFactura") Integer nroFactura) throws GTLException {
		Factura factura = BeanHelper.getFacturaFacade().getByNroFacturaConItems(nroFactura);
		if(factura == null){
			throw new GTLException(777, "No se encontró la factura");
		}
		return new FacturaMobTO(factura, BeanHelper.getParametrosGeneralesFacade().getParametrosGenerales());
	}
}

