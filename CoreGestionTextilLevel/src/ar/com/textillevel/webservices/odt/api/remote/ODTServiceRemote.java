package ar.com.textillevel.webservices.odt.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.modulos.odt.to.intercambio.ODTEagerTO;

@Remote
public interface ODTServiceRemote {
	public void recibir(String codigoBarras);
	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente);
	public List<ODTEagerTO> getByIdsEager(List<Integer> ids);
}
