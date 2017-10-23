package ar.com.textillevel.util;

import java.util.Collection;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;

public class CambioEstadoDibujoHelper {

	private static CambioEstadoDibujoHelper instance = new CambioEstadoDibujoHelper();
	private Multimap<EEstadoDibujo, EEstadoDibujo> mapTransicionesPosibles;

	private CambioEstadoDibujoHelper() {
		initMap();
	}

	public static CambioEstadoDibujoHelper getInstance() {
		return instance;
	}
	
	private Multimap<EEstadoDibujo, EEstadoDibujo> getMapTransicionesPosibles() {
		if(this.mapTransicionesPosibles == null) {
			this.mapTransicionesPosibles = ArrayListMultimap.create();
		}
		return this.mapTransicionesPosibles;
	}

	private void initMap() {
		getMapTransicionesPosibles().put(EEstadoDibujo.EN_STOCK, EEstadoDibujo.GRABADO);
		getMapTransicionesPosibles().put(EEstadoDibujo.EN_STOCK,EEstadoDibujo.ROTO);
		
		getMapTransicionesPosibles().put(EEstadoDibujo.ROTO,EEstadoDibujo.GRABADO);

		getMapTransicionesPosibles().put(EEstadoDibujo.GRABADO, EEstadoDibujo.EN_STOCK);
	}

	public boolean puedeCambiarEstado(EEstadoDibujo estado) {
		return !getMapTransicionesPosibles().get(estado).isEmpty();
	}

	public Collection<EEstadoDibujo> getEstadosPosibles(EEstadoDibujo estado) {
		return getMapTransicionesPosibles().get(estado);
	}

	public boolean cambioEstadoValido(EEstadoDibujo estadoFrom, EEstadoDibujo estadoUntil) {
		return getEstadosPosibles(estadoFrom).contains(estadoUntil);
	}

}