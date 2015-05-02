package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.EmpresaSegurosDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.commons.EmpresaSeguros;

@Stateless
public class EmpresaSegurosDAO extends GenericDAO<EmpresaSeguros, Integer> implements EmpresaSegurosDAOLocal{

}
