package ar.com.textillevel.modulos.personal.utils;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.util.BeanFactoryLocalAbstract;
import ar.com.textillevel.modulos.personal.facade.api.local.EmpleadoFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.local.FichadaLegajoFacadeLocal;

public class GTLPersonalBeanFactoryLocal extends BeanFactoryLocalAbstract {

	private static GTLPersonalBeanFactoryLocal instance;

	protected GTLPersonalBeanFactoryLocal() throws FWException {
		super("GTL/");
		addJndiName(FichadaLegajoFacadeLocal.class);
		addJndiName(EmpleadoFacadeLocal.class);
	}

	public static GTLPersonalBeanFactoryLocal getInstance() throws FWException {
		if (instance == null) {
			try {
				instance = new GTLPersonalBeanFactoryLocal();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public static GTLPersonalBeanFactoryLocal getInstance2() throws FWRuntimeException {
		try {
			return getInstance();
		} catch (FWException e) {
			throw new FWRuntimeException(e);
		}
	}
}
