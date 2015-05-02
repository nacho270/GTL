package ar.com.textillevel.web.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ar.com.textillevel.facade.api.local.ChequeFacadeLocal;
import ar.com.textillevel.facade.api.local.ClienteFacadeLocal;
import ar.com.textillevel.facade.api.local.CorreccionFacadeLocal;
import ar.com.textillevel.facade.api.local.CorreccionFacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.FacturaFacadeLocal;
import ar.com.textillevel.facade.api.local.FacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.OrdenDePagoFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.local.ProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.ReciboFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoEntradaFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoEntradaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoSalidaFacadeLocal;
import ar.com.textillevel.facade.api.local.UsuarioSistemaFacadeLocal;
import ar.com.textillevel.modulos.odt.facade.api.local.OrdenDeTrabajoFacadeLocal;

public class BeanHelper {
	
	private static InitialContext initialContext;
	
	public static ParametrosGeneralesFacadeLocal getParametrosGeneralesFacade(){
		return lookup(ParametrosGeneralesFacadeLocal.class);
	}
	
	public static UsuarioSistemaFacadeLocal getUsuarioSistemaFacade(){
		return lookup(UsuarioSistemaFacadeLocal.class);
	}
	
	public static CuentaFacadeLocal getCuentaFacade(){
		return lookup(CuentaFacadeLocal.class);
	}

	public static ClienteFacadeLocal getClienteFacade(){
		return lookup(ClienteFacadeLocal.class);
	}
	
	public static ProveedorFacadeLocal getProveedorFacade(){
		return lookup(ProveedorFacadeLocal.class);
	}

	public static FacturaFacadeLocal getFacturaFacade(){
		return lookup(FacturaFacadeLocal.class);
	}

	public static CorreccionFacadeLocal getCorreccionFacade(){
		return lookup(CorreccionFacadeLocal.class);
	}

	public static ReciboFacadeLocal getReciboFacade(){
		return lookup(ReciboFacadeLocal.class);
	}
	
	public static OrdenDePagoFacadeLocal getOrdenDePagoFacade(){
		return lookup(OrdenDePagoFacadeLocal.class);
	}
	
	public static ChequeFacadeLocal geChequeFacade(){
		return lookup(ChequeFacadeLocal.class);
	}
	
	public static RemitoEntradaFacadeLocal geRemitoEntradaFacade(){
		return lookup(RemitoEntradaFacadeLocal.class);
	}
	
	public static OrdenDeTrabajoFacadeLocal getODTFacadeLocal(){
		return lookup(OrdenDeTrabajoFacadeLocal.class);
	}
	
	public static RemitoSalidaFacadeLocal getRemitoSalidaFacadeLocal(){
		return lookup(RemitoSalidaFacadeLocal.class);
	}
	
	public static FacturaProveedorFacadeLocal getFacturaProveedorFacadeLocal(){
		return lookup(FacturaProveedorFacadeLocal.class);
	}

	public static CorreccionFacturaProveedorFacadeLocal getCorreccionFacturaProveedorFacadeLocal(){
		return lookup(CorreccionFacturaProveedorFacadeLocal.class);
	}
	
	public static RemitoEntradaProveedorFacadeLocal getRemitoEntradaProveedorFacadeLocal() {
		return lookup(RemitoEntradaProveedorFacadeLocal.class);
	}

	@SuppressWarnings("unchecked")
	private static <T> T lookup(Class<?> bean){
		try {
			String name = bean.getSimpleName().replaceFirst("Local$", "/local") ;
			return (T) getInitialContext().lookup("GTL/"+name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error accediendo a " + bean, e);
		}
	}
	
	private static InitialContext getInitialContext()  {
		if (initialContext == null) {
			try {
				initialContext = new InitialContext();
			} catch (NamingException e) {
				throw new RuntimeException("No se pudo crear un contexto inicial para acceder al Servidor de aplicaciones", e);
			}
		}
		return initialContext;
	}

}
