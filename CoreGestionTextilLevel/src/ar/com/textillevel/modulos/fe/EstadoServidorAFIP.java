package ar.com.textillevel.modulos.fe;

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
		this.authServer = new EstadoAFIPWrapper("Servidor de autenticaci�n", respuestaAfip.getAuthServer());
		this.pruebaAutenticacion = new EstadoAFIPWrapper("Prueba de autenticaci�n", pruebaAutorizacion?"ok":"error");
		this.ultimaFacturaAutorizada = ultimaFacturaAutorizada;
		this.ultimaNCAutorizada = ultimaNCAutorizada;
		this.ultimaNDAutorizada = ultimaNDAutorizada;
	}

	public class EstadoAFIPWrapper implements Serializable {

		private static final long serialVersionUID = 2246424736021379959L;
		
		private String nombre;
		private String valor;

		public EstadoAFIPWrapper(String nombre, String valor) {
			this.nombre = nombre;
			this.valor = valor;
		}

		public String getNombre() {
			return nombre;
		}

		public String getValor() {
			return valor;
		}
		
		public boolean isOK() {
			return getValor().equalsIgnoreCase("ok");
		}
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
