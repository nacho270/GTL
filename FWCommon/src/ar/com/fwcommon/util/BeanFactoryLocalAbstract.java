package ar.com.fwcommon.util;

import ar.com.fwcommon.componentes.error.FWException;

public class BeanFactoryLocalAbstract extends BeanFactoryAbstract {

	public BeanFactoryLocalAbstract(String appName) {
		setApplicationName(appName);
		System.out.println("Asumiendo " + getApplicationName() + " para los lookups locales.");
	}

	protected void addJndiName(Class<?> interfaceLocal) throws FWException {
		getJndiNames().put(interfaceLocal, getApplicationName() + getBeanName(interfaceLocal));
	}

	@Override
	protected String getBeanName(Class<?> clazz) throws FWException {
		if(!clazz.getName().endsWith("Local")) {
			throw new FWException("Una interfaz local debe terminar en Local");
		}
		String beanName = clazz.getName().replaceFirst("Local$", "/local");
		if(beanName.lastIndexOf('.') != -1) {
			beanName = beanName.substring(beanName.lastIndexOf('.') + 1);
		}
		return beanName;
	}

}