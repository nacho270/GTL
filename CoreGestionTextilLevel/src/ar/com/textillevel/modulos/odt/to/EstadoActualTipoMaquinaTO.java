package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;

public class EstadoActualTipoMaquinaTO implements Serializable {

	private static final long serialVersionUID = 8693274504789334431L;

	private TipoMaquinaTO tipoMaquina;
	private Map<EAvanceODT, List<ODTTO>> odtsPorEstado;

	public EstadoActualTipoMaquinaTO() {
		odtsPorEstado = new LinkedHashMap<EAvanceODT, List<ODTTO>>();
		initMap();
	}

	private void initMap() {
		for(EAvanceODT e : EAvanceODT.values()){
			if(e != EAvanceODT.EN_PROCESO){
				odtsPorEstado.put(e, new ArrayList<ODTTO>());
			}
		}
	}

	public EstadoActualTipoMaquinaTO(TipoMaquina tm) {
		this.tipoMaquina = new TipoMaquinaTO(tm);
		odtsPorEstado = new LinkedHashMap<EAvanceODT, List<ODTTO>>();
		initMap();
	}

	
	public Map<EAvanceODT, List<ODTTO>> getOdtsPorEstado() {
		return odtsPorEstado;
	}

	public void setOdtsPorEstado(Map<EAvanceODT, List<ODTTO>> odtsPorEstado) {
		this.odtsPorEstado = odtsPorEstado;
	}

	public TipoMaquinaTO getTipoMaquina() {
		return tipoMaquina;
	}

	public void setTipoMaquina(TipoMaquinaTO tipoMaquina) {
		this.tipoMaquina = tipoMaquina;
	}
}
