package ar.com.textillevel.modulos.personal.dao.impl;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ContratoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.contratos.Contrato;

@Stateless
public class ContratoDAO extends GenericDAO<Contrato, Integer> implements ContratoDAOLocal{

}
