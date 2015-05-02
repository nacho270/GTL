package ar.com.textillevel.mobile.util;

import java.util.Properties;

import android.content.Context;

public class AppConfig {

	private static final AppConfig instance = new AppConfig();
	private static final String CONF_FILE = "app.conf";
	
	private static final String API_URL = "/gtl/spring/";
	private static final String QUERY_BY_NRO = "/byNro";
	
	
	public final static String API_URL_LOGIN = API_URL + "login";
	public final static String API_URL_CUENTA = API_URL + "cuenta";
	
	public final static String API_URL_FACTURA = API_URL + "factura";
	public final static String API_URL_FACTURA_BY_NRO = API_URL_FACTURA + QUERY_BY_NRO;
	
	public final static String API_URL_RECIBO = API_URL + "recibo";
	public final static String API_URL_RECIBO_BY_NRO = API_URL_RECIBO + QUERY_BY_NRO;
	
	public final static String API_URL_ODP = API_URL + "odp";
	public final static String API_URL_ODP_BY_NRO = API_URL_ODP + QUERY_BY_NRO;
	
	
	public final static String API_URL_CHEQUE = API_URL + "cheque";
	public final static String API_URL_CHEQUE_BY_RO = API_URL_CHEQUE + QUERY_BY_NRO;

	public final static String API_URL_RE = API_URL + "remitoE";
	//TODO: BY NUMERO

	public final static String API_URL_CORRECCION_CLIENTE = API_URL + "correccioncliente";
	//TODO: BY NUMERO

	public final static String API_URL_RS = API_URL + "remitoS";
	//TODO: BY NUMERO
	
	public final static String API_URL_FACTURA_PROV = API_URL + "facturaprov";
	//TODO: BY NUMERO
	
	public final static String API_URL_CORRECCION_FACTURA_PROV = API_URL + "correccfacturaprov";
	//TODO: BY NUMERO

	public final static String API_URL_RE_PROV = API_URL + "remitoprovE";
	//TODO: BY NUMERO
	
	private String serverIp;
	
	private AppConfig(){}
	
	public void initConfig(Context context){
        Properties p = new AssetsPropertyReader(context).getProperties(CONF_FILE);
        this.serverIp = p.getProperty("server");
	}
	
	public static AppConfig getInstance(){
		return instance;
	}
	
	public String getServer(){
		return serverIp;
	}
}
