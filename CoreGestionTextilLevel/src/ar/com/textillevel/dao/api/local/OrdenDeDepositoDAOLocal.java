package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;

@Local
public interface OrdenDeDepositoDAOLocal extends DAOLocal<OrdenDeDeposito, Integer>{

	public Integer getLastNroOrden();

	public OrdenDeDeposito getOrdenByNro(Integer nroOrden);

}
