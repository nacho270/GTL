package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.textillevel.entidades.cheque.Cheque;

@Local
public interface ChequeFacadeLocal {
	public Cheque grabarCheque(Cheque cheque, String usuario) throws FWException;
	public Cheque getChequebyId(Integer idCheque);
	public Cheque getChequeByNumeracion(String letraCheque, Integer nroCheque);
}
