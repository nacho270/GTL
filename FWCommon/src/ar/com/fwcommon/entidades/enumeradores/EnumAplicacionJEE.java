package ar.com.fwcommon.entidades.enumeradores;

import java.net.URL;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.error.FWException;

/**
 * Para el BeanFactoryLocalAbstract necesitamos una clase que nos retorne la base JNDI para el lookup.
 * Para Materiales e Insertos web que nos retorne el id del Portal de la aplicación.
 * Con esta clase mantenemos el hardcodeo en un sólo lugar ...
 */
public enum EnumAplicacionJEE {

	;

	private Integer idAplicacion;
	private String serverJarName;
	private String earName;

	/**
	 * 
	 * @param idAplicacion id de la aplicación en el Portal
	 * @param serverJarName nombre del jar utilizado en el deploy
	 * @param earName nombre del .ear (nombre a utilizar como prefijo en los lookups)
	 */
	private EnumAplicacionJEE(Integer idAplicacion, String serverJarName, String earName) {
		this.idAplicacion = idAplicacion;
		this.serverJarName = serverJarName;
		this.earName = earName;
	}

	public static String getBaseJndiName() throws FWException {
		EnumAplicacionJEE enumAplicacionJEE  = getEnumAplicacionJEE() ;
		if (enumAplicacionJEE.getEarName() == null) {
			throw new FWException (BossError.ERR_APLICACION, "No se pudo determinar el nombre del ear de la aplicacion que se está ejecutando", new String[] {"Indicar el nombre del .ear de la aplicación en " + getCanonicalName() + "."}) ;
		}
		return enumAplicacionJEE.getEarName() + "/";
	}

	public static Integer getIdAplicacionPortal() throws FWException {
		EnumAplicacionJEE enumAplicacionJEE  = getEnumAplicacionJEE() ;
		if (enumAplicacionJEE.getIdAplicacion() == null) {
			throw new FWException (BossError.ERR_APLICACION, "No se pudo determinar el nombre del ear de la aplicacion que se está ejecutando", new String[] {"Indicar el id de la aplicación en " + getCanonicalName() + "."}) ;
		}
		return enumAplicacionJEE.getIdAplicacion() ;
	}

	private static EnumAplicacionJEE getEnumAplicacionJEE() throws FWException {
		URL url = EnumAplicacionJEE.class.getClassLoader().getResource(getResourceLocation());
		String serverJar = url.toString() ;
		for (EnumAplicacionJEE enumAplicacionJEE : values()) {
			if(serverJar.indexOf(enumAplicacionJEE.getServerJarName() + "!") != -1) {
				return enumAplicacionJEE ;
			}
		}
		throw new FWException (BossError.ERR_APLICACION, "No se pudo determinar la aplicacion que se está ejecutando (" + serverJar + ")", new String[] {"Verificar la información de la aplicación en " + getCanonicalName() + "."}) ;		
	}

	private Integer getIdAplicacion() {
		return idAplicacion;
	}

	private String getServerJarName() {
		return serverJarName;
	}

	private String getEarName() {
		return earName;
	}

	private static String getResourceLocation() {
		return getCanonicalName().replace('.', '/') + ".class";
	}
	private static String getCanonicalName() {
		return EnumAplicacionJEE.class.getCanonicalName() ;
	}

}
