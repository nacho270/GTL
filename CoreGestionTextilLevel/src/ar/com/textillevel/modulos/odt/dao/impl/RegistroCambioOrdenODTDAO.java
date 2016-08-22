package ar.com.textillevel.modulos.odt.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.RegistroCambioOrdenODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.workflow.RegistroCambioOrdenODT;

@Stateless
public class RegistroCambioOrdenODTDAO extends GenericDAO<RegistroCambioOrdenODT, Integer> implements RegistroCambioOrdenODTDAOLocal{

}
