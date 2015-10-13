package ar.com.fwcommon.templates.modulo.model.accionesmouse;

import ar.com.fwcommon.componentes.error.FWException;



public interface IBuilderAccionAdicional<T> {
	
	public AccionesAdicionales<T> construirAccionAdicional(int idModel) throws FWException;
}
