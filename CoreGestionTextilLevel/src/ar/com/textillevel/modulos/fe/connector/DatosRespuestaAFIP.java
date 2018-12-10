
package ar.com.textillevel.modulos.fe.connector;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.modulos.fe.cliente.dto.Err;
import ar.com.textillevel.modulos.fe.cliente.dto.Obs;
import ar.com.textillevel.modulos.fe.cliente.responses.FECAEResponse;

public class DatosRespuestaAFIP {

	private String resultado;
	private String reproceso;
	private String cae;
	private String observaciones;
	private String fechaVencimiento;

	public DatosRespuestaAFIP(FECAEResponse fecaeResponse) {
		this.resultado = fecaeResponse.getFeCabResp().getResultado();
		this.reproceso = fecaeResponse.getFeCabResp().getReproceso();
		this.cae = fecaeResponse.getFeDetResp()[0].getCAE();
		this.observaciones = buildTextoError(fecaeResponse.getFeDetResp()[0].getObservaciones(), fecaeResponse.getErrors());
		this.fechaVencimiento = fecaeResponse.getFeDetResp()[0].getCAEFchVto();
	}

	private String buildTextoError(Obs[] observaciones, Err[] errors) {
		List<String> msgList = new ArrayList<String>();
		if(observaciones != null) {
			for(Obs o : observaciones) {
				msgList.add(o.toString());
			}
		}
		if(errors != null) {
			for(Err e : errors) {
				msgList.add(e.toString());
			}
		}
		if(msgList.isEmpty()) {
			return "";
		} else {
			return StringUtil.getCadena(msgList, "\n");
		}
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getReproceso() {
		return reproceso;
	}

	public void setReproceso(String reproceso) {
		this.reproceso = reproceso;
	}

	public String getCae() {
		return cae;
	}

	public void setCae(String cae) {
		this.cae = cae;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public boolean isAutorizada() {
		return getResultado().equalsIgnoreCase("A");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Resultado: " + resultado).append("\n")
		  .append("Reproceso: " + reproceso).append("\n")
		  .append("CAE: " + cae).append("\n")
		  .append("Observaciones: " + observaciones)
		  .append("Vencimiento: " + fechaVencimiento);
		return sb.toString();
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

}