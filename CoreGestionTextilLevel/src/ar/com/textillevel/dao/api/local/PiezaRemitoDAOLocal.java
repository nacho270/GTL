package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;

@Local
public interface PiezaRemitoDAOLocal extends DAOLocal<PiezaRemito, Integer>{

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente);
	
	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaCompletoSinSalidaByClient(Integer idCliente);

}
