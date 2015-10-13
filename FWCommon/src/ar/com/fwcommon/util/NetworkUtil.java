package ar.com.fwcommon.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.error.FWException;

public class NetworkUtil {

	private static InetAddress getLocalHost() throws FWException {
		try {
			return InetAddress.getLocalHost();
		} catch(UnknownHostException e) {
			throw new FWException(BossError.ERR_CONEXION, "No se pudo obtener la dirección del equipo.", "No se pudo obtener la dirección del equipo.", e,
					new String[] { "Verifique la configuración de red del equipo." });
		}
	}

	/**
	 * 
	 * @return el nombre del equipo en el cual se está ejecutando la aplicación.
	 * @throws FWException
	 */
	public static String getNombreLocalHost() throws FWException {
		return getLocalHost().getHostName();
	}

	/**
	 * 
	 * @return el ip del equipo en el cual se está ejecutando la aplicación.
	 * @throws FWException
	 */
	public static String getIpLocalHost() throws FWException {
		return getLocalHost().getHostAddress();
	}

	/**
	 * @param host el nombre del host.
	 * @return el ip de host.
	 * @throws FWException
	 */
	public static InetAddress getIpEquipo(String host) throws FWException {
		try {
			return InetAddress.getByName(host);
		} catch(UnknownHostException e) {
			throw new FWException(BossError.ERR_CONEXION, "No se pudo obtener la dirección del equipo (" + host + ").", "No se pudo obtener la dirección del equipo (" + host + ").",e,
					new String[] { "Verifique el nombre del equipo.", "Verifique el DNS." });
		}
	}

}