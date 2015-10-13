package ar.com.fwcommon.util;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.FWRuntimeException;


public class BeanFactoryRemoteAbstract extends BeanFactoryAbstract {

	protected BeanFactoryRemoteAbstract() throws FWException {
		this(null);
	}
	protected BeanFactoryRemoteAbstract(String defaultAppName) throws FWException {
		setApplicationName( System.getProperty("applicationName") );
		if (getApplicationName() == null) {
			if (StringUtil.isNullOrEmpty(defaultAppName))
				throw new FWException("No se pudo determinar a que aplicación acceder") ;
			else
				setApplicationName(defaultAppName);
		}
		if (!getApplicationName().endsWith("/")) {
			setApplicationName( getApplicationName() + "/" );
		}
	}	
	
	

	protected void addJndiName(Class<?> interfaceRemota) throws FWException {
		String beanName = getBeanName(interfaceRemota);
		getJndiNames().put(interfaceRemota, getApplicationName() + beanName);
	}

	protected String getBeanName(Class<?> interfaceRemota) throws FWException {
		if (!interfaceRemota.getName().endsWith("Remote")) {
			throw new FWException ("Una interfaz remota debe terminar en Remote") ;
		}
		String beanName = interfaceRemota.getName().replaceFirst("Remote$", "/remote") ;
		if (beanName.lastIndexOf('.') != -1) {
			beanName = beanName.substring(beanName.lastIndexOf('.') + 1) ;
		}
		return beanName;
	}

	/**
	 * Similar a {@link #getUnregisteredBean(Class)}, pero puede recibir el  contexto jndi.
	 * @param <T>
	 * @param clazz
	 * @param jndiCtx
	 * @return
	 * @throws FWRuntimeException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getUnregisteredBean(Class<T> clazz, InitialContext jndiCtx) throws FWRuntimeException  {
		try {
			String bn = getApplicationName() + getBeanName(clazz);
			if (jndiCtx==null)
				jndiCtx = getInitialContext();
			return (T) jndiCtx.lookup(bn);
		} catch(Exception e) {
			try {
				logger.warn("JNDI erroneo?:" + getApplicationName() + getBeanName(clazz));
			} catch(FWException e1) {}
			throw new FWRuntimeException("Error accediendo vía JNDI al bean " , e);
		}
	}
	
	
	/**
	 * 
	 * Busca y creo un nuevo Bean, pero no registrado. 
	 * Util para evitar empaquetar en la aplicación Beans que son solo de test.
	 * Ej: TestMainDineroMail es un bean de test, no debería estar presente en el deploy final.
	 * 
	 * <B>EVITAR SU USO EN OTROS CASOS</B>
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws FWRuntimeException
	 */
	public <T> T getUnregisteredBean(Class<T> clazz) throws FWRuntimeException  {
		return getUnregisteredBean(clazz, null);
	}
	
	private final static Logger logger = Logger.getLogger(BeanFactoryRemoteAbstract.class);
	
}
