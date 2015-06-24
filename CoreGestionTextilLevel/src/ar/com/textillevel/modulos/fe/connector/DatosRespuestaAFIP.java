package ar.com.textillevel.modulos.fe.connector;

import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.modulos.fe.cliente.responses.FECAEResponse;

public class DatosRespuestaAFIP {

	private String resultado;
	private String reproceso;
	private String cae;
	private String observaciones;

	public DatosRespuestaAFIP(FECAEResponse fecaeResponse) {
		this.resultado = fecaeResponse.getFeCabResp().getResultado();
		this.reproceso = fecaeResponse.getFeCabResp().getReproceso();
		this.cae = fecaeResponse.getFeDetResp()[0].getCAE();
		this.observaciones = StringUtil.getCadena(fecaeResponse.getFeDetResp()[0].getObservaciones(), "\n");
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
		  .append("Observaciones: " + observaciones);
		return sb.toString();
	}


}