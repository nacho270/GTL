package ar.com.textillevel.modulos.fe;

public class TestAFIP {

	static {
		System.setProperty("textillevel.fe.habilitada", "si");
		System.setProperty("textillevel.fe.endpointAutenticacion", "https://wsaahomo.afip.gov.ar/ws/services/LoginCms");
		System.setProperty("textillevel.fe.endpointNegocio", "https://wswhomo.afip.gov.ar/wsfe/service.asmx");
		System.setProperty("textillevel.fe.servicio", "wsfe");
		System.setProperty("textillevel.fe.destinoServicio", "cn=wsaahomo,o=afip,c=ar,serialNumber=CUIT 33693450239");
		System.setProperty("textillevel.fe.keyStore", "/home/ignacio.cicero/dev/salem/afip/salem.p12");
		System.setProperty("textillevel.fe.keyStoreSigner", "Diego Salem");
		System.setProperty("textillevel.fe.keyStorePass", "soloio");
		System.setProperty("textillevel.fe.duracion", "3600000");
	}

	public static void main(String[] args) {
		AuthAFIPData authData = ConfiguracionAFIPHolder.getInstance().getAuthData();
		System.out.println("HASH: " + authData.getHash());
		System.out.println("TOKEN: " + authData.getToken());
	}
}
