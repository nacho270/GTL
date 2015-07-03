package ar.com.textillevel.entidades.cuenta.to;

import java.util.HashMap;
import java.util.Map;

public enum ETipoIVAAFIP {
	IVA_0(0d, 3),
	IVA_10_5(10.5d, 4),
	IVA_21(21d, 5),
	IVA_27(27d, 6),
	IVA_5(5d, 8),
	IVA_2_5(2.5d, 9);

	private ETipoIVAAFIP(double porcentajeIVA, int idAFIP) {
		this.porcentajeIVA = porcentajeIVA;
		this.idAFIP = idAFIP;
	}

	private double porcentajeIVA;
	private int idAFIP;

	public double getPorcentajeIVA() {
		return porcentajeIVA;
	}

	public int getIdAFIP() {
		return idAFIP;
	}
	
	private static Map<Double, ETipoIVAAFIP> keyMap;

	public static ETipoIVAAFIP getByPorcentaje(Double porcentaje) {
		if (porcentaje == null)
			return null;
		return getKeyMap().get(porcentaje);
	}


	private static Map<Double, ETipoIVAAFIP> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<Double, ETipoIVAAFIP>();
			ETipoIVAAFIP[] valores = values();
			for (int i = 0; i < valores.length; i++) {
				keyMap.put(valores[i].porcentajeIVA, valores[i]);
			}
		}
		return keyMap;
	}

	@Override
	public String toString() {
		return getPorcentajeIVA() + " => ID: " + getIdAFIP();
	}
}
