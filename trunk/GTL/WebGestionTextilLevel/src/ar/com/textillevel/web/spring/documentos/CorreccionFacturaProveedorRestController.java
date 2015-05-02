package ar.com.textillevel.web.spring.documentos;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.to.FacturaProvMobTO;
import ar.com.textillevel.web.spring.exception.GTLException;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value="/correccfacturaprov",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
public class CorreccionFacturaProveedorRestController {

	@RequestMapping
	public @ResponseBody FacturaProvMobTO buscarCorreccion(@RequestParam(value="idCorreccion") Integer idCorreccion) {
		CorreccionFacturaProveedor correccionFactura = BeanHelper.getCorreccionFacturaProveedorFacadeLocal().getCorreccionFacturaByIdEager(idCorreccion);
		if(correccionFactura == null){
			throw new GTLException(777, "No se encontró la corrección factura");
		}
		return new FacturaProvMobTO(correccionFactura);
	}

	@RequestMapping(value="/byNro")
	public @ResponseBody FacturaProvMobTO buscarCorreccionFacturaPorNumero(@RequestParam(value="nroCorreccion") Integer nroCorreccion) throws GTLException {
		//TODO
		return null;
	}

}

