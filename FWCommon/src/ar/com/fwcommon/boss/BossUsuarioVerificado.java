package ar.com.fwcommon.boss;

import java.util.HashMap;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.security.auth.callback.UsernamePasswordHandler;

import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.entidades.Usuario;

public class BossUsuarioVerificado {

	private static Usuario usuarioVerificado;
	private static LoginContext loginContext;

	public static Usuario getUsuarioVerificado() {
		return usuarioVerificado;
	}

	public static void setUsuarioVerificado(Usuario usuarioVerificado) {
		BossUsuarioVerificado.usuarioVerificado = usuarioVerificado;
	}
	
	public static void loginJAASClienteJBoss () {
		//System.setProperty("java.security.auth.login.config",
        //"d:/auth.conf");

		final HashMap<String, String> options = new HashMap<String,String>();//("multi-threaded",false)
		options.put("multi-threaded","false");
		options.put("password-stacking",null);
		
		Configuration configuration = new javax.security.auth.login.Configuration()
	      {
	         private AppConfigurationEntry[] aces = { new AppConfigurationEntry( 
	        		 "org.jboss.security.ClientLoginModule", 
	                  LoginModuleControlFlag.REQUIRED,options 
	               ) };
	         @Override
	         public AppConfigurationEntry[] getAppConfigurationEntry(String name)
	         {
	            return "algo".equals(name) ? aces : null;
	         }
	         @Override
	         public void refresh() {}
	      };
		javax.security.auth.login.Configuration.setConfiguration(configuration);
		
		UsernamePasswordHandler handler = null;
	    handler = new UsernamePasswordHandler(usuarioVerificado.getNombre(), usuarioVerificado.getPassword());
		try {
			loginContext = new LoginContext("algo",handler);
			loginContext.login();
		} catch (LoginException e) {
			throw new FWRuntimeException("JAAS login Exception",e);
		}
	}

}