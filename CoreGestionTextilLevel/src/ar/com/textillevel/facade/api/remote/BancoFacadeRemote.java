package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.cheque.Banco;

@Remote
public interface BancoFacadeRemote {
	public Banco save(Banco banco);
	public void remove(Banco banco);
	public List<Banco> getAllOrderByName();
	public Boolean existeBanco(Integer codigoBanco);
	public Banco getBancoByCodigo(Integer codigoBanco);
}
