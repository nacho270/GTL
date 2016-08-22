package ar.com.textillevel.webservices.odt.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.modulos.odt.to.intercambio.ODTEagerTO;
import ar.com.textillevel.modulos.odt.to.intercambio.RemitoEntradaTO;

@Remote
public interface ODTServiceRemote {
	public Boolean recibirRemitoEntrada(RemitoEntradaTO remitoEntrada, String usuarioSistema);
	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente);
	public List<ODTEagerTO> getByIdsEager(List<Integer> ids);
}
