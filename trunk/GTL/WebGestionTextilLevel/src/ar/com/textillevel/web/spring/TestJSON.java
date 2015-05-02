package ar.com.textillevel.web.spring;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;

@Controller
@RequestMapping("/test.json")
public class TestJSON {
	
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CondicionDeVenta test(){
		CondicionDeVenta c = new CondicionDeVenta();
		c.setId(52);
		c.setNombre("Condicion");
		return c;
	}
	
}
