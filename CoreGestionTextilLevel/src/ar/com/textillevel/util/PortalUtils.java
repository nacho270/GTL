package ar.com.textillevel.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.textillevel.entidades.portal.UsuarioSistema;

public class PortalUtils {

	private static final String DEFAULT_ENC_KEY = "GtLSeeeiiileeeem26";
	
	public static String getHash(String mensaje, String algoritmo) {
		try {
			MessageDigest m = MessageDigest.getInstance(algoritmo);
			m.reset();
			m.update(mensaje.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashtext = bigInt.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (Exception e) {
			return null;
		}
	}

	public static String generarToken(UsuarioSistema usuario) {
		return generarToken(usuario.getId(),usuario.getUsrName(),usuario.getPassword(),System.currentTimeMillis());
	}

	private static String generarToken(Integer usrId, String userName, String pass, Long timestamp){
		return PortalUtils.encriptar( usrId + "," + userName + "," + PortalUtils.hashB64(pass)+","+timestamp);
	}
	
	public static String encriptar(String data) {
		DesEncrypter encrypter = new DesEncrypter(System.getProperty("textillevel.security.session_enc",DEFAULT_ENC_KEY));
		String encrypted = encrypter.encrypt(data);
		return encrypted;
	}

	public static String desencriptar(String data) {
		DesEncrypter encrypter = new DesEncrypter(System.getProperty("textillevel.security.session_enc",DEFAULT_ENC_KEY));
		data = data.replaceAll(" ", "+");
		return encrypter.decrypt(data);
	}

	public static String hashB64(String pwd) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			byte[] res = sha1.digest(pwd.getBytes("UTF-8"));
			return new sun.misc.BASE64Encoder().encode(res);
		} catch (NoSuchAlgorithmException e) {
			throw new FWRuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new FWRuntimeException(e);
		}
	}

}
