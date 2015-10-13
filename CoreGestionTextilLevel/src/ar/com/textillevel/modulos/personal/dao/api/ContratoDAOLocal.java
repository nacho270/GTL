package ar.com.textillevel.modulos.personal.dao.api;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.modulos.personal.entidades.contratos.Contrato;

@Local
public interface ContratoDAOLocal extends DAOLocal<Contrato, Integer>{

}
