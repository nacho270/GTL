package main;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.facade.api.remote.CorrectorCuentasClientesFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class MainCorrectorCuenta {

	public static void main(String[] args) {
		System.getProperties().setProperty("applicationName", "GTL");
		System.getProperties().setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		System.getProperties().setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		System.getProperties().setProperty("cltimezone","GMT-3");
		if(System.getProperty("java.naming.provider.url")==null){
			System.getProperties().setProperty("java.naming.provider.url", "localhost:1099");
		}
		if(System.getProperty("textillevel.chat.server.ip")==null){
			System.getProperties().setProperty("textillevel.chat.server.ip", "127.0.0.1");
		}
		if(System.getProperty("textillevel.chat.server.port")==null){
			System.getProperties().setProperty("textillevel.chat.server.port", "7777");
		}
		CorrectorCuentasClientesFacadeRemote correctorFacade = GTLBeanFactory.getInstance().getBean2(CorrectorCuentasClientesFacadeRemote.class);
//		ClienteFacadeRemote clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
//		List<Cliente> allOrderByName = clienteFacade.getAllOrderByName();
		try {
//			for(Cliente cl : allOrderByName){
//				System.out.println("CORRIGIENDO CUENTA DEL CLIENTE: " + cl.getNroCliente() + " " + cl.toString());
				correctorFacade.corregirCuenta(4, "admin");
//			}
			System.out.println("FIN DEL PROCESO");
		} catch (ValidacionException e) {
			System.out.println(e.getMensajeError());
		}
	}

}
