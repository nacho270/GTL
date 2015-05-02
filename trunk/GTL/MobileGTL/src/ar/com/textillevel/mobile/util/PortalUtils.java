package ar.com.textillevel.mobile.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class PortalUtils {
	public static String getHash(String mensaje, String algoritmo) {
		try{
			MessageDigest m = MessageDigest.getInstance(algoritmo);
			m.reset();
			m.update(mensaje.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			}
			return hashtext;
		}catch(Exception e){
			return null;
		}
	}
	
	public static void main(String[] args){
		System.out.println(getHash("admin", "MD5"));
	}
}
