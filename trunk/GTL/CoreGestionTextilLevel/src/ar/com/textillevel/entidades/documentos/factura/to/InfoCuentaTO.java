package ar.com.textillevel.entidades.documentos.factura.to;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InfoCuentaTO implements Serializable {

	private static final long serialVersionUID = 7823382729938676684L;

	private Map<Integer, Set<Integer>> infoFacturaReciboMap;
	private Map<Integer, Set<Integer>> infoNotDebReciboMap;

	public InfoCuentaTO() {
		this.infoFacturaReciboMap = new HashMap<Integer, Set<Integer>>();
		this.infoNotDebReciboMap = new HashMap<Integer, Set<Integer>>();
	}

	public void addInfoFactura(Integer idFactura, Integer idRecibo) {
		Set<Integer> idReciboFacturaSet = infoFacturaReciboMap.get(idFactura);
		if(idReciboFacturaSet == null) {
			idReciboFacturaSet = new HashSet<Integer>();
			infoFacturaReciboMap.put(idFactura, idReciboFacturaSet);
		}
		idReciboFacturaSet.add(idRecibo);
	}

	public void addInfoNotDeb(Integer idNotDeb, Integer idRecibo) {
		Set<Integer> idReciboNotDebSet = infoNotDebReciboMap.get(idNotDeb);
		if(idReciboNotDebSet == null) {
			idReciboNotDebSet = new HashSet<Integer>();
			infoNotDebReciboMap.put(idNotDeb, idReciboNotDebSet);
		}
		idReciboNotDebSet.add(idRecibo);
	}

	public Set<Integer> getInfoFacturaReciboSet(Integer idFactura) {
		return infoFacturaReciboMap.get(idFactura);
	}

	public Set<Integer> getInfoNotDebReciboSet(Integer idNotDeb) {
		return infoNotDebReciboMap.get(idNotDeb);
	}

}