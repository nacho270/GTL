package ar.com.textillevel.web.spring.cuenta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.cuenta.to.CuentaTO;

@Controller
@RequestMapping("/cuenta")
public class CuentaRestController {
	
	private volatile Map<ETipoCuenta, AbstractCuentaRestController<?, ?, ?>> implMap;
	
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CuentaTO buscarMovimientos(
										@RequestParam(value="tipoCuenta") Integer tipoCuenta, 
										@RequestParam(value="cuentaABuscar") String cuenta,
										@RequestParam(value="cantidadMovimientos") Integer cantidad) {
		return getImplMap().get(ETipoCuenta.getById(tipoCuenta)).getCuenta(cuenta, cantidad);
	}
	
	private Map<ETipoCuenta, AbstractCuentaRestController<?, ?, ?>> getImplMap() {
		if(implMap == null){
			synchronized (CuentaRestController.class) {
				if(implMap == null){
					implMap = new ConcurrentHashMap<ETipoCuenta, AbstractCuentaRestController<?,?,?>>();
					implMap.put(ETipoCuenta.CLIENTE, new CuentaRestControllerCliente());
					implMap.put(ETipoCuenta.PROVEEDOR, new CuentaRestControllerProveedor());
				}
			}
		}
		return implMap;
	}
}
