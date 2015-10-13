package ar.com.fwcommon.util;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.FWRuntimeException;

public abstract class BeanFactoryAbstract {

	private static InitialContext initialContext ;
	private Map<Class<?>, String> jndiNames = new HashMap<Class<?>, String>() ;
	private Map<Class<?>, Object> beanCache = new HashMap<Class<?>, Object>() ;

	private String applicationName ;
	
	protected String getApplicationName(){
		return applicationName;
	}

	protected void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> clazz) throws FWException  {
		if (beanCache.get(clazz) == null) {
			beanCache.put(clazz, getNewBean(clazz)) ;
		}
		return (T) beanCache.get(clazz) ;
	}
	
	/**
	 * Llama a getBean, pero envuelve la excepcion CLException en CLRuntimeException. 
	 * (Generalmente un error de este tipo es irreparable y se debe manejar mostrando un error que impide seguir usando la aplicacion )
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws FWRuntimeException
	 */
	public <T> T getBean2(Class<T> clazz) throws FWRuntimeException  {
		try {
			return getBean(clazz);
		} catch(FWException e) {
			e.printStackTrace();
			throw new FWRuntimeException(e);
		}
	}	

	@SuppressWarnings("unchecked")
	public <T> T getNewBean(Class<T> clazz) throws FWException  {
		String jndiName = jndiNames.get(clazz) ;
		if (jndiName == null) {
			throw new FWException("No está definido el nombre JNDI para la clase " + clazz) ;
		}
		try {
			return (T) getInitialContext().lookup(jndiName);
		} catch(Exception e) {
			throw new FWException("Error accediendo vía JNDI al bean " + jndiName, e);
		}
	}

	public synchronized InitialContext getInitialContext() throws FWException {
		if (initialContext == null) {
			try {
				initialContext = new InitialContext();
			} catch (NamingException e) {
				e.printStackTrace();
				throw new FWException("No se pudo crear un contexto inicial para acceder al Servidor de aplicaciones", e) ;
			}
		}
		return initialContext ;
	}

	protected Map<Class<?>, String> getJndiNames() {
		return jndiNames ;
	}

	protected abstract String getBeanName(Class<?> clazz) throws FWException;

	protected void addJndiNameWithOutAppName(Class<?> clazz) throws FWException {
		String beanName = getBeanName(clazz);
		getJndiNames().put(clazz, beanName);
	}

}