package ar.com.textillevel.web.spring.documentos;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.to.CorreccionFacturaMobTO;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value="/correccioncliente",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
public class CorreccionFacturaRestController {

	@RequestMapping
	public @ResponseBody CorreccionFacturaMobTO buscarCorreccion(@RequestParam(value="idCorreccion") Integer idCorreccion) {
		return BeanHelper.getCorreccionFacade().getCorreccionMobById(idCorreccion);
	}

	@RequestMapping(value="/byNro")
	public @ResponseBody CorreccionFacturaMobTO buscarCorreccionPorNumero(@RequestParam(value="nroCorreccion") Integer nroCorreccion, @RequestParam(value="idTipoCorreccion") Integer idTipoCorreccion, @RequestParam(value="nroSucursal") Integer nroSucursal) {
		ETipoCorreccionFactura tipoCorreccion = idTipoCorreccion == ETipoDocumento.NOTA_DEBITO.getId() ? ETipoCorreccionFactura.NOTA_DEBITO : ETipoCorreccionFactura.NOTA_CREDITO;
		return BeanHelper.getCorreccionFacade().getCorreccionMobByNumero(nroCorreccion, tipoCorreccion, nroSucursal);
	}

}