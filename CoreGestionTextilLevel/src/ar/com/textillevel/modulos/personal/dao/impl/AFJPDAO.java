package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.AFJPDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.commons.AFJP;

@Stateless
public class AFJPDAO extends GenericDAO<AFJP, Integer> implements AFJPDAOLocal{

}
