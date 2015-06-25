package ar.com.textillevel.modulos.fe;

import java.io.Serializable;

import ar.com.textillevel.modulos.fe.cliente.responses.DummyResponse;

public class EstadoServidorAFIP implements Serializable {
	
	private static final long serialVersionUID = -59089109232039231L;

	private EstadoAFIPWrapper appServer;
	private EstadoAFIPWrapper dbServer;
	private EstadoAFIPWrapper authServer;
	

	public EstadoServidorAFIP(DummyResponse respuestaAfip) {
		this.appServer = new EstadoAFIPWrapper("Servidor de negocio", respuestaAfip.getAppServer());
		this.dbServer = new EstadoAFIPWrapper("Servidor de base de datos", respuestaAfip.getDbServer());
		this.authServer = new EstadoAFIPWrapper("Servidor de autenticación", respuestaAfip.getAuthServer());
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
}
