package ar.com.textillevel.modulos.odt.dao.impl;

import javax.ejb.Stateless;
import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.CambioAvanceDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.workflow.CambioAvance;

@Stateless
public class CambioAvanceDAO extends GenericDAO<CambioAvance, Integer> implements CambioAvanceDAOLocal {

}
