package ar.com.fwcommon.componentes.error.xml;
import java.util.List;

public class Cuerpo {

	private String usuario ;
	private List<Propiedad> propiedades ;
	private String tipoError ;
	private String mensajeError ;
	private String llamadaParametrizada ;
	private String causa ;
	private List<Tip> tips ;

	public String getCausa() {
		return causa;
	}
	public void setCausa(String causa) {
		this.causa = causa;
	}
	public String getLlamadaParametrizada() {
		return llamadaParametrizada;
	}
	public void setLlamadaParametrizada(String llamadaParametrizada) {
		this.llamadaParametrizada = llamadaParametrizada;
	}
	public String getMensajeError() {
		return mensajeError;
	}
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public List<Propiedad> getPropiedades() {
		return propiedades;
	}
	public void setPropiedades(List<Propiedad> propiedades) {
		this.propiedades = propiedades;
	}
	public String getTipoError() {
		return tipoError;
	}
	public void setTipoError(String tipoError) {
		this.tipoError = tipoError;
	}
	public List<Tip> getTips() {
		return tips;
	}
	public void setTips(List<Tip> tips) {
		this.tips = tips;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPropiedad (String llave) {
		for (Propiedad propiedad : getPropiedades()) {
			if (propiedad.getLlave().equals(llave))
				return propiedad.getValor();
		}
		return null ;
	}

}
