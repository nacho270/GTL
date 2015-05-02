package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cheque.Banco;

@Local
public interface BancoDAOLocal extends DAOLocal<Banco, Integer>{
	Banco getBancoByCodigo(Integer codigoBanco);
}
