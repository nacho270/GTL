package ar.com.textillevel.facade.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ar.com.textillevel.dao.api.local.NotaDebitoDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.facade.api.remote.NotaDebitoFacadeRemote;

@Stateless
public class NotaDebitoFacade implements NotaDebitoFacadeRemote {

	@EJB
	private NotaDebitoDAOLocal notaDebitoDAO;

	public List<NotaDebito> getNotaDebitoPendientePagarList(Integer idCliente) {
		return notaDebitoDAO.getNotaDebitoPendientePagarList(idCliente);
	}

}
