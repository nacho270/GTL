package ar.com.textillevel.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;

public class CambioEstadoDibujoHelper {

	private static CambioEstadoDibujoHelper instance = new CambioEstadoDibujoHelper();
	private Map<EEstadoDibujo, List<EEstadoDibujo>> mapTransicionesPosibles;

	private CambioEstadoDibujoHelper() {
		initMap();
	}

	public static CambioEstadoDibujoHelper getInstance() {
		return instance;
	}
	
	private Map<EEstadoDibujo, List<EEstadoDibujo>> getMapTransicionesPosibles() {
		if(this.mapTransicionesPosibles == null) {
			this.mapTransicionesPosibles = new HashMap<EEstadoDibujo, List<EEstadoDibujo>>();
		}
		return this.mapTransicionesPosibles;
	}

	private void initMap() {
		for(EEstadoDibujo e : EEstadoDibujo.values()) {
			getMapTransicionesPosibles().put(e, new ArrayList<EEstadoDibujo>());
		}
		List<EEstadoDibujo> tmp = getMapTransicionesPosibles().get(EEstadoDibujo.EN_STOCK);
		tmp.add(EEstadoDibujo.GRABADO);
		tmp.add(EEstadoDibujo.ROTO);
		
		tmp = getMapTransicionesPosibles().get(EEstadoDibujo.ROTO);
		tmp.add(EEstadoDibujo.GRABADO);

		tmp = getMapTransicionesPosibles().get(EEstadoDibujo.GRABADO);
		tmp.add(EEstadoDibujo.EN_STOCK);
	}

	public boolean puedeCambiarEstado(EEstadoDibujo estado) {
		return !getMapTransicionesPosibles().get(estado).isEmpty();
	}

	public List<EEstadoDibujo> getEstadosPosibles(EEstadoDibujo estado) {
		return getMapTransicionesPosibles().get(estado);
	}

	public boolean cambioEstadoValido(EEstadoDibujo estadoFrom, EEstadoDibujo estadoUntil) {
		return getEstadosPosibles(estadoFrom).contains(estadoUntil);
	}

}