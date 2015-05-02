package ar.com.textillevel.modulos.odt.dao.impl;

import javax.ejb.Stateless;

import ar.com.textillevel.modulos.odt.dao.api.local.RegistroCambioOrdenODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.workflow.RegistroCambioOrdenODT;

import ar.clarin.fwjava.dao.impl.GenericDAO;

@Stateless
public class RegistroCambioOrdenODTDAO extends GenericDAO<RegistroCambioOrdenODT, Integer> implements RegistroCambioOrdenODTDAOLocal{

}
