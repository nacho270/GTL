package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;
import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.MateriaPrimaCantidadDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

@Stateless
@SuppressWarnings({"rawtypes"})
public class MateriaPrimaCantidadDAO extends GenericDAO<MateriaPrimaCantidad, Integer> implements MateriaPrimaCantidadDAOLocal {

}