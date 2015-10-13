package ar.com.fwcommon.boss;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.entidades.Aplicacion;

public class BossAplicacion {

	private static Aplicacion aplicacion;
	private static Integer idAplicacion;

	/**
	 * Devuelve la aplicación.
	 * @return aplicacion La entidad <b>Aplicación</b>.
	 */
	public static Aplicacion getAplicacion() {
		return aplicacion;
	}

	/**
	 * Setea la aplicación.
	 * @param aplicacion La entidad <b>Aplicación</b>.
	 */
	public static void setAplicacion(Aplicacion aplicacion) {
		BossAplicacion.aplicacion = aplicacion;
	}

	/**
	 * Setea la aplicación.
	 * @param idAplicacion El nro. de id de la aplicación.
	 */
	public static void setAplicacion(int idAplicacion) {
		//Tomar los datos de la aplicacion del Portal
		BossAplicacion.idAplicacion = idAplicacion;
	}

	/**
	 * Poblamos la aplicación.
	 * @throws FWException
	 */
	public static void poblarAplicacion() throws FWException {
		Aplicacion aplicacion = BossLoginPortal.getInstance().getDatosAplicacion(idAplicacion);
		setAplicacion(aplicacion);
	}

	/**
	 * Devuelve el id de la aplicación.
	 * @return idAplicacion El nro. de id de la aplicación.
	 */
	public static Integer getIdAplicacion() {
		return idAplicacion;
	}

}