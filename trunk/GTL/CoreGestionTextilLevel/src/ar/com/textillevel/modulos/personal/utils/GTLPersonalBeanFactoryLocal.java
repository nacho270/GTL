package ar.com.textillevel.modulos.personal.utils;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.CLRuntimeException;
import ar.clarin.fwjava.util.BeanFactoryLocalAbstract;
import ar.com.textillevel.modulos.personal.facade.api.local.EmpleadoFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.local.FichadaLegajoFacadeLocal;

public class GTLPersonalBeanFactoryLocal extends BeanFactoryLocalAbstract {

	private static GTLPersonalBeanFactoryLocal instance;

	protected GTLPersonalBeanFactoryLocal() throws CLException {
		super("GTL/");
		addJndiName(FichadaLegajoFacadeLocal.class);
		addJndiName(EmpleadoFacadeLocal.class);
	}

	public static GTLPersonalBeanFactoryLocal getInstance() throws CLException {
		if (instance == null) {
			try {
				instance = new GTLPersonalBeanFactoryLocal();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public static GTLPersonalBeanFactoryLocal getInstance2() throws CLRuntimeException {
		try {
			return getInstance();
		} catch (CLException e) {
			throw new CLRuntimeException(e);
		}
	}
}
