package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;

public class EstadoActualMaquinaTO implements Serializable {

	private static final long serialVersionUID = 8693274504789334431L;

	private MaquinaTO maquina;
	private Map<EAvanceODT, List<ODTTO>> odtsPorEstado;

	public EstadoActualMaquinaTO() {
		odtsPorEstado = new LinkedHashMap<EAvanceODT, List<ODTTO>>();
		initMap();
	}

	private void initMap() {
		for (EAvanceODT e : EAvanceODT.values()) {
			if (e != EAvanceODT.EN_PROCESO) {
				odtsPorEstado.put(e, new ArrayList<ODTTO>());
			}
		}
	}

	public EstadoActualMaquinaTO(Maquina maquina) {
		this.maquina = new MaquinaTO(maquina);
		odtsPorEstado = new LinkedHashMap<EAvanceODT, List<ODTTO>>();
		initMap();
	}

	public Map<EAvanceODT, List<ODTTO>> getOdtsPorEstado() {
		return odtsPorEstado;
	}

	public void setOdtsPorEstado(Map<EAvanceODT, List<ODTTO>> odtsPorEstado) {
		this.odtsPorEstado = odtsPorEstado;
	}

	public MaquinaTO getMaquina() {
		return maquina;
	}

	public void setMaquina(MaquinaTO maquina) {
		this.maquina = maquina;
	}

}
