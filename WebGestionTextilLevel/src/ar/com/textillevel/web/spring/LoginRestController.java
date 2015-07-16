package ar.com.textillevel.web.spring;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.entidades.portal.to.UsuarioSistemaTO;
import ar.com.textillevel.util.PortalUtils;
import ar.com.textillevel.web.spring.exception.GTLException;
import ar.com.textillevel.web.spring.session.SessionMap;
import ar.com.textillevel.web.util.BeanHelper;

@Controller
@RequestMapping("/login")
public class LoginRestController {

	@RequestMapping(method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody UsuarioSistemaTO login(@RequestParam(value="user") String user, 
											    @RequestParam(value="pass") String pass){
		if(user.equalsIgnoreCase("admin")) throw new GTLException(777, "Usuario o clave erroneos"); //prevengo el login de admin que es muy obvio
		UsuarioSistema usuario = BeanHelper.getUsuarioSistemaFacade().loginSinHashear(user, pass);
		if(usuario == null){
			throw new GTLException(777, "Usuario o clave erroneos");
		}
		String token = PortalUtils.generarToken(usuario);
		SessionMap.getInstance().newSession(token);
		return new UsuarioSistemaTO(token, user);
	}
}
