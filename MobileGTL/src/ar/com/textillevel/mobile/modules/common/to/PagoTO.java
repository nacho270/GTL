package ar.com.textillevel.mobile.modules.common.to;

public class PagoTO {

	private String fecha;
	private String concepto;
	private String importePagado;
	private Integer idDocumento;
	private Integer idTipoDocumento;

	public PagoTO() {
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(String importePagado) {
		this.importePagado = importePagado;
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
