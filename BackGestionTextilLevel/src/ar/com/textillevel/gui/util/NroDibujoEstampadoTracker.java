package ar.com.textillevel.gui.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NroDibujoEstampadoTracker {

	private Map<Integer, Set<Integer>> trackMapNrosPedidos;

	public NroDibujoEstampadoTracker() {
		this.trackMapNrosPedidos = new HashMap<Integer, Set<Integer>>();
	}

	public void clear() {
		this.trackMapNrosPedidos.clear();
	}

	public boolean putNro(Integer nroDibujo) {
		Integer nroComienzo = getNroComienzoDibujo(nroDibujo);
		Set<Integer> setExistentes = trackMapNrosPedidos.get(nroComienzo);
		boolean alreadyExists = setExistentes != null && setExistentes.contains(nroDibujo);
		if(setExistentes == null) {
			setExistentes = new HashSet<Integer>();
			trackMapNrosPedidos.put(nroComienzo, setExistentes);
		}
		setExistentes.add(nroDibujo);
		return alreadyExists;
	}

	public Integer getCantidadByNro(Integer nroDibujo) {
		Set<Integer> setExistentes = trackMapNrosPedidos.get(nroDibujo);
		return setExistentes == null ? 0 : setExistentes.size();
	}

	public boolean removeNro(Integer nroDibujo) {
		Integer nroComienzo = getNroComienzoDibujo(nroDibujo);
		Set<Integer> setExistentes = trackMapNrosPedidos.get(nroComienzo);
		return setExistentes != null && setExistentes.remove(nroDibujo); 
	}

	private int getNroComienzoDibujo(Integer nroDibujo) {
		return (nroDibujo - nroDibujo % 1000)/1000;
	}

}