package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.BancoDAOLocal;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.facade.api.remote.BancoFacadeRemote;

@Stateless
public class BancoFacade implements BancoFacadeRemote{

	@EJB
	private BancoDAOLocal bancoDao;

	public List<Banco> getAllOrderByName() {
		return bancoDao.getAllBancos();
	}

	public void remove(Banco banco) {
		bancoDao.removeById(banco.getId());
	}

	public Banco save(Banco banco) {
		return bancoDao.save(banco);
	}

	public Boolean existeBanco(Integer codigoBanco) {
		return this.getBancoByCodigo(codigoBanco)!=null;
	}

	public Banco getBancoByCodigo(Integer codigoBanco) {
		return bancoDao.getBancoByCodigo(codigoBanco);
	}
}
