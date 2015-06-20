package ar.com.textillevel.modulos.fe.connector;

public class DatosRespuestaAFIP {

	private String resultado;
	private String motivo;
	private String reproceso;
	private String cae;

	public DatosRespuestaAFIP(String resultado, String motivo, String reproceso, String cae) {
		this.resultado = reproceso;
		this.motivo = motivo;
		this.reproceso = reproceso;
		this.cae = cae;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
}
