package ar.com.textillevel.modulos.fe.to;

import java.io.Serializable;

import ar.com.textillevel.modulos.fe.cliente.responses.DummyResponse;

public class EstadoServidorAFIP implements Serializable {
	
	private static final long serialVersionUID = -59089109232039231L;

	private EstadoAFIPWrapper appServer;
	private EstadoAFIPWrapper dbServer;
	private EstadoAFIPWrapper authServer;
	private EstadoAFIPWrapper pruebaAutenticacion;
	private String ultimaFacturaAutorizada;
	private String ultimaNDAutorizada;
	private String ultimaNCAutorizada;

	public EstadoServidorAFIP(DummyResponse respuestaAfip, boolean pruebaAutorizacion, 
			String ultimaFacturaAutorizada,String ultimaNCAutorizada,String ultimaNDAutorizada) {
		this.appServer = new EstadoAFIPWrapper("Servidor de negocio", respuestaAfip.getAppServer());
		this.dbServer = new EstadoAFIPWrapper("Servidor de base de datos", respuestaAfip.getDbServer());
		this.authServer = new EstadoAFIPWrapper("Servidor de autenticación", respuestaAfip.getAuthServer());
		this.pruebaAutenticacion = new EstadoAFIPWrapper("Prueba de autenticación", pruebaAutorizacion?"ok":"error");
		this.ultimaFacturaAutorizada = ultimaFacturaAutorizada;
		this.ultimaNCAutorizada = ultimaNCAutorizada;
		this.ultimaNDAutorizada = ultimaNDAutorizada;
	}

	public EstadoAFIPWrapper getAppServer() {
		return appServer;
	}

	public EstadoAFIPWrapper getDbServer() {
		return dbServer;
	}

	public EstadoAFIPWrapper getAuthServer() {
		return authServer;
	}

	public EstadoAFIPWrapper getPruebaAutenticacion() {
		return pruebaAutenticacion;
	}

	public String getUltimaFacturaAutorizada() {
		return ultimaFacturaAutorizada;
	}

	public String getUltimaNDAutorizada() {
		return ultimaNDAutorizada;
	}

	public String getUltimaNCAutorizada() {
		return ultimaNCAutorizada;
	}
}
