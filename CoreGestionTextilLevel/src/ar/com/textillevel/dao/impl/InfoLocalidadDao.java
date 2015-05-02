package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.InfoLocalidadDAOLocal;
import ar.com.textillevel.entidades.gente.InfoLocalidad;

@Stateless
public class InfoLocalidadDao extends GenericDAO<InfoLocalidad, Integer> implements InfoLocalidadDAOLocal{

}
