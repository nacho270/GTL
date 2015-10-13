package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.GamaColorDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.GamaColor;

@Stateless
public class GamaColorDAO extends GenericDAO<GamaColor, Integer> implements GamaColorDAOLocal{

}
