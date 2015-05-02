package ar.com.textillevel.entidades.to.remitosalida;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;

public class RemitoSalidaConBajaStockTO implements Serializable {

	private static final long serialVersionUID = -7950248478458101593L;

	private RemitoSalida remitoSalida;
	private List<PiezaRemitoSalidaTO> piezasRemitoSalidaTO;
	private List<RemitoEntrada> remitoEntradaModificadosList;
	private String userName;
	
	public RemitoSalidaConBajaStockTO() {
		this.piezasRemitoSalidaTO = new ArrayList<PiezaRemitoSalidaTO>();
		this.remitoEntradaModificadosList = new ArrayList<RemitoEntrada>();
	}

	public RemitoSalida getRemitoSalida() {
		return remitoSalida;
	}

	public void setRemitoSalida(RemitoSalida remitoSalida) {
		this.remitoSalida = remitoSalida;
	}

	public List<PiezaRemitoSalidaTO> getPiezasRemitoSalidaTO() {
		return piezasRemitoSalidaTO;
	}

	public void setPiezasRemitoSalidaTO(List<PiezaRemitoSalidaTO> piezasRemitoSalidaTO) {
		this.piezasRemitoSalidaTO = piezasRemitoSalidaTO;
	}

	public List<RemitoEntrada> getRemitoEntradaModificadosList() {
		return remitoEntradaModificadosList;
	}

	public void setRemitoEntradaModificadosList(List<RemitoEntrada> remitoEntradaModificadosList) {
		this.remitoEntradaModificadosList = remitoEntradaModificadosList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
