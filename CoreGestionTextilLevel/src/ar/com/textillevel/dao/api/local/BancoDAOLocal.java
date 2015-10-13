package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cheque.Banco;

@Local
public interface BancoDAOLocal extends DAOLocal<Banco, Integer> {
	Banco getBancoByCodigo(Integer codigoBanco);
	public List<Banco> getAllBancos();
}
