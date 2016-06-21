package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

@SuppressWarnings("rawtypes")
@Local
public interface MateriaPrimaCantidadDAOLocal extends DAOLocal<MateriaPrimaCantidad, Integer> {

}