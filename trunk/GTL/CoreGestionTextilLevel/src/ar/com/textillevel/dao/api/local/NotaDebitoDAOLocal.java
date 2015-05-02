package ar.com.textillevel.dao.api.local;

import java.util.List;
import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;

@Local
public interface NotaDebitoDAOLocal extends DAOLocal<NotaDebito, Integer> {

	public List<NotaDebito> getNotaDebitoPendientePagarList(Integer idCliente);

	public List<NotaDebito> getAllByIdCliente(Integer idCliente);

}
