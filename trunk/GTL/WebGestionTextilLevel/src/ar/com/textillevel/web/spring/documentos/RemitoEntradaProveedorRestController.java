package ar.com.textillevel.web.spring.documentos;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.to.RemitoEntradaProvMobileTO;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping(value="/remitoprovE",method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
public class RemitoEntradaProveedorRestController {

	@RequestMapping
	public @ResponseBody RemitoEntradaProvMobileTO buscarRemito(@RequestParam(value="idRE") Integer idRE) {
		RemitoEntradaProveedor remitoEntrada = BeanHelper.getRemitoEntradaProveedorFacadeLocal().getByIdEager(idRE);
		return new RemitoEntradaProvMobileTO(remitoEntrada);
	}

	@RequestMapping(value="/byNro")
	public @ResponseBody RemitoEntradaProvMobileTO buscarRemitoByNro(@RequestParam(value="nroRE") Integer nroRE) {
		//TODO:
		return null;
	}

}
