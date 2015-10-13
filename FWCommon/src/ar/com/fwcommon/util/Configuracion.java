package ar.com.fwcommon.util;

public class Configuracion {

	private static void setProperty(String key, String value) {
		if(value != null) {
			System.setProperty(key, value);
		}
	}

	//Login
	public static String getLoginEndpoint() {
		return System.getProperty("ar.com.login_endpoint");
	}

	public static void setLoginEndpoint(String loginEndpoint) {
		setProperty("ar.com.login_endpoint", loginEndpoint);
	}

	public static String getLoginNombre() {
		return System.getProperty("ar.com.login_nombre");
	}

	public static void setLoginNombre(String loginNombre) {
		setProperty("ar.com.login_nombre", loginNombre);
	}

	//Publicidad
	public static String getPublicidadEndpoint() {
		return System.getProperty("ar.com.publicidad_endpoint");
	}

	public static void setPublicidadEndpoint(String publicidadEndpoint) {
		setProperty("ar.com.publicidad_endpoint", publicidadEndpoint);
	}

	public static String getPublicidadNombre() {
		return System.getProperty("ar.com.publicidad_nombre");
	}

	public static void setPublicidadNombre(String publicidadNombre) {
		setProperty("ar.com.publicidad_nombre", publicidadNombre);
	}

	//Mail
	public static String getMailEndpoint() {
		return System.getProperty("ar.com.mail_endpoint");
	}

	public static void setMailEndpoint(String mailEndpoint) {
		setProperty("ar.com.mail_endpoint", mailEndpoint);
	}

	public static String getMailNombre() {
		return System.getProperty("ar.com.mail_nombre");
	}

	public static void setMailNombre(String mailNombre) {
		setProperty("ar.com.mail_nombre", mailNombre);
	}

	//Transferencia
	public static String getTransferenciaEndpoint() {
		return System.getProperty("ar.com.transferencia_endpoint");
	}

	public static void setTransferenciaEndpoint(String transferenciaEndpoint) {
		setProperty("ar.com.transferencia_endpoint", transferenciaEndpoint);
	}

	public static String getTransferenciaNombre() {
		return System.getProperty("ar.com.transferencia_nombre");
	}

	public static void setTransferenciaNombre(String transferenciaNombre) {
		setProperty("ar.com.transferencia_nombre", transferenciaNombre);
	}

	//FTP
	public static String getFtpServer() {
		return System.getProperty("ar.com.ftp_server");
	}

	public static void setFtpServer(String ftpServer) {
		setProperty("ar.com.ftp_server", ftpServer);
	}

	public static String getFtpUsuario() {
		return System.getProperty("ar.com.ftp_usuario");
	}

	public static void setFtpUsuario(String ftpUsuario) {
		setProperty("ar.com.ftp_usuario", ftpUsuario);
	}

	public static String getFtpPassword() {
		return System.getProperty("ar.com.ftp_password");
	}

	public static void setFtpPassword(String ftpPassword) {
		setProperty("ar.com.ftp_password", ftpPassword);
	}

	//Clasificados
	public static String getClasificadosEndpoint() {
		return System.getProperty("ar.com.clasificados_endpoint");
	}

	public static void setClasificadosEndpoint(String clasificadosEndpoint) {
		setProperty("ar.com.clasificados_endpoint", clasificadosEndpoint);
	}

	public static String getClasificadosNombre() {
		return System.getProperty("ar.com.clasificados_nombre");
	}

	public static void setClasificadosNombre(String clasificadosNombre) {
		setProperty("ar.com.clasificados_nombre", clasificadosNombre);
	}

	//General
	public static String getRaizAlmacenamiento() {
		return System.getProperty("ar.com.raizalmacenamiento");
	}

	public static void setRaizAlmacenamiento(String raizAlmacenamiento) {
		setProperty("ar.com.raizalmacenamiento", raizAlmacenamiento);
	}

	public static String getRutaPdfIn() {
		return System.getProperty("ar.com.rutapdfin");
	}

	public static void setRutaPdfIn(String rutaPdfIn) {
		setProperty("ar.com.rutapdfin", rutaPdfIn);
	}

	public static String getRutaPdfOut() {
		return System.getProperty("ar.com.rutapdfout");
	}

	public static void setRutaPdfOut(String rutaPdfOut) {
		setProperty("ar.com.rutapdfout", rutaPdfOut);
	}

	public static String getRutaTempAutomata() {
		return System.getProperty("ar.com.rutatempautomata");
	}

	public static void setRutaTempAutomata(String rutaTempAutomata) {
		setProperty("ar.com.rutatempautomata", rutaTempAutomata);
	}

	public static String getRutaTempLocal() {
		return System.getProperty("ar.com.rutatemplocal");
	}

	public static void setRutaTempLocal(String rutaTempLocal) {
		setProperty("ar.com.rutatemplocal", rutaTempLocal);
	}

	public static String getDireccionRespuesta() {
		return System.getProperty("ar.com.direccionrespuesta");
	}

	public static void setDireccionRespuesta(String direccionRespuesta) {
		setProperty("ar.com.direccionrespuesta", direccionRespuesta);
	}

	public static boolean isNotificarResponsablesMedio() {
		return Boolean.valueOf(System.getProperty("ar.com.notificarresponsablesmedio"));
	}

	public static void setNotificarResponsablesMedio(boolean notificarResponsablesMedio) {
		setProperty("ar.com.notificarresponsablesmedio", String.valueOf(notificarResponsablesMedio));
	}

	public static boolean isNotificarResponsablesSap() {
		return Boolean.valueOf(System.getProperty("ar.com.notificarresponsablessap"));
	}

	public static void setNotificarResponsablesSap(String notificarResponsablesSap) {
		setProperty("ar.com.notificarresponsablessap", String.valueOf(notificarResponsablesSap));
	}

	//Red1
	public static String getRed1Endpoint() {
		return System.getProperty("ar.com.red1_endpoint");
	}

	public static void setRed1Endpoint(String red1Endpoint) {
		setProperty("ar.com.red1_endpoint", red1Endpoint);
	}

	public static String getRed1Nombre() {
		return System.getProperty("ar.com.red1_nombre");
	}

	public static void setRed1Nombre(String red1Nombre) {
		setProperty("ar.com.red1_nombre", red1Nombre);
	}

	public static String getMailNotiError() {
		return System.getProperty("ar.com.direccionNoficacionErrores");
	}

	public static void setMailNotiError(String mailNotiError) {
		setProperty("ar.com.direccionNoficacionErrores", mailNotiError);
	}

}