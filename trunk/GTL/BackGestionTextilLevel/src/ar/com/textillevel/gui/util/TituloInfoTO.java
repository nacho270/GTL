package ar.com.textillevel.gui.util;

public class TituloInfoTO {
	private String titulo;
	private String informacion;
	
	public TituloInfoTO(String titulo, String informacion) {
		super();
		this.titulo = titulo;
		this.informacion = informacion;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getInformacion() {
		return informacion;
	}
	
	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}
}
