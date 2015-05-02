package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.cheque.Cheque;

@Local
public interface ChequeFacadeLocal {
	public Cheque grabarCheque(Cheque cheque, String usuario) throws CLException;
	public Cheque getChequebyId(Integer idCheque);
	public Cheque getChequeByNumeracion(String letraCheque, Integer nroCheque);
}
