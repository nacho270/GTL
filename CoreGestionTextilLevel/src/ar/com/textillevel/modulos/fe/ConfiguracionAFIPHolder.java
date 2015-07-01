package ar.com.textillevel.modulos.fe;

import org.apache.log4j.Logger;

import ar.com.textillevel.util.Utils;

public class ConfiguracionAFIPHolder {

	private static final Logger logger = Logger.getLogger(ConfiguracionAFIPHolder.class);

	private static volatile ConfiguracionAFIPHolder instance;
	private ConfiguracionFacturaElectronica configFE;
	private AuthAFIPData authAFIPData;
	private volatile long lastAuthRequestMillis = -1l;

	public static ConfiguracionAFIPHolder getInstance() {
		if (instance == null) {
			synchronized (ConfiguracionAFIPHolder.class) {
				if (instance == null) {
					instance = new ConfiguracionAFIPHolder();
				}
			}
		}
		return instance;
	}

	private ConfiguracionAFIPHolder() {
		loadConfigFE();
		if (configFE.isHabilitada()) {
			loadAuthAFIP();
		}
	}

	private void loadConfigFE() {
		configFE = new ConfiguracionFacturaElectronica();
		boolean habilitada = Utils.esAfirmativo(System.getProperty("textillevel.fe.habilitada"));
		configFE.setHabilitada(habilitada);
		if(habilitada) {
			configFE.setDestinoServicio(System.getProperty("textillevel.fe.destinoServicio"));
			configFE.setDuracion(Long.valueOf(System.getProperty("textillevel.fe.duracion")));
			configFE.setEndpointAutenticacion(System.getProperty("textillevel.fe.endpointAutenticacion"));
			configFE.setEndpointNegocio(System.getProperty("textillevel.fe.endpointNegocio"));
			configFE.setKeyStore(System.getProperty("textillevel.fe.keyStore"));
			configFE.setKeyStorePass(System.getProperty("textillevel.fe.keyStorePass"));
			configFE.setKeyStoreSigner(System.getProperty("textillevel.fe.keyStoreSigner"));
			configFE.setServicio(System.getProperty("textillevel.fe.servicio"));
		}
	}

	private void loadAuthAFIP() {
		try {
			authAFIPData = ClienteAutenticacionAFIP.crearAutorizacion(configFE);
			lastAuthRequestMillis = System.currentTimeMillis();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	public AuthAFIPData getAuthData() {
		if (lastAuthRequestMillis != -1) {
			long currentTime = System.currentTimeMillis();
			synchronized (ConfiguracionAFIPHolder.class) {
				if (currentTime - lastAuthRequestMillis >= configFE.getDuracion().longValue()) {
					logger.info("Renovando autorización AFIP...");
					loadAuthAFIP();
				}
			}
			return authAFIPData;
		}
		return null;
	}
	
	public boolean isHabilitado() {
		return configFE != null && configFE.isHabilitada();
	}
	
	public String getURLNegocio(){
		return configFE.getEndpointNegocio();
	}
}
