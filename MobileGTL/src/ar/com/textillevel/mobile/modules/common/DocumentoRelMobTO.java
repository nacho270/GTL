package ar.com.textillevel.mobile.modules.common;

public class DocumentoRelMobTO {

	private Integer idDocumento;
	private Integer idTipoDocumento;
	private String textoMostrar;

	public DocumentoRelMobTO(Integer idDocumento, Integer idTipoDocumento,String textoMostrar) {
		this.idDocumento = idDocumento;
		this.idTipoDocumento = idTipoDocumento;
		this.textoMostrar = textoMostrar;
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

	public String getTextoMostrar() {
		return textoMostrar;
	}

	public void setTextoMostrar(String textoMostrar) {
		this.textoMostrar = textoMostrar;
	}

}
