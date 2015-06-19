package ar.com.textillevel.modulos.fe;

public class ConfiguracionFacturaElectronica {

	private boolean habilitada;
	private String endpointAutenticacion;
	private String endpointNegocio;
	private String servicio;
	private String destinoServicio;
	private String keyStore;
	private String keyStoreSigner;
	private String keyStorePass;
	private Long duracion;

	public boolean isHabilitada() {
		return habilitada;
	}

	public void setHabilitada(boolean habilitada) {
		this.habilitada = habilitada;
	}

	public String getEndpointAutenticacion() {
		return endpointAutenticacion;
	}

	public void setEndpointAutenticacion(String endpointAutenticacion) {
		this.endpointAutenticacion = endpointAutenticacion;
	}

	public String getEndpointNegocio() {
		return endpointNegocio;
	}

	public void setEndpointNegocio(String endpointNegocio) {
		this.endpointNegocio = endpointNegocio;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getDestinoServicio() {
		return destinoServicio;
	}

	public void setDestinoServicio(String destinoServicio) {
		this.destinoServicio = destinoServicio;
	}

	public String getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	public String getKeyStoreSigner() {
		return keyStoreSigner;
	}

	public void setKeyStoreSigner(String keyStoreSigner) {
		this.keyStoreSigner = keyStoreSigner;
	}

	public String getKeyStorePass() {
		return keyStorePass;
	}

	public void setKeyStorePass(String keyStorePass) {
		this.keyStorePass = keyStorePass;
	}

	public Long getDuracion() {
		return duracion;
	}

	public void setDuracion(Long duracion) {
		this.duracion = duracion;
	}
}
