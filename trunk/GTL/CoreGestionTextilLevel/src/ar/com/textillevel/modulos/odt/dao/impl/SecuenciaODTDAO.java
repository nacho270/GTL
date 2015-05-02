package ar.com.textillevel.modulos.odt.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.SecuenciaODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.SecuenciaODT;

@Stateless
public class SecuenciaODTDAO extends GenericDAO<SecuenciaODT, Integer> implements SecuenciaODTDAOLocal {

}
