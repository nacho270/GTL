package ar.com.textillevel.facade.api.remote;

import java.util.List;
import javax.ejb.Remote;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;

@Remote
public interface NotaDebitoFacadeRemote {

	public List<NotaDebito> getNotaDebitoPendientePagarList(Integer idCliente);

}
