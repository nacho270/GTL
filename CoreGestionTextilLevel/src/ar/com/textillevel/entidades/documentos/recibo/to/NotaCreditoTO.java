package ar.com.textillevel.entidades.documentos.recibo.to;

public class NotaCreditoTO {

	public String fecha;
	public String descrNC;
	public String importe;
	private Integer idDocumento;
	private Integer idTipoDocumento;

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getDescrNC() {
		return descrNC;
	}

	public void setDescrNC(String descrNC) {
		this.descrNC = descrNC;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

}
