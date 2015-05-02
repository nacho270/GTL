package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EstadoGeneralODTsTO implements Serializable{

	private static final long serialVersionUID = 6023929237143702947L;

	private List<ODTTO> odtsDisponibles;
	private List<EstadoActualTipoMaquinaTO> estadoMaquinas;

	public EstadoGeneralODTsTO() {
		odtsDisponibles = new ArrayList<ODTTO>();
		estadoMaquinas = new ArrayList<EstadoActualTipoMaquinaTO>();
	}

	public List<EstadoActualTipoMaquinaTO> getEstadoMaquinas() {
		return estadoMaquinas;
	}

	public void setEstadoMaquinas(List<EstadoActualTipoMaquinaTO> estadoMaquinas) {
		this.estadoMaquinas = estadoMaquinas;
	}

	public List<ODTTO> getOdtsDisponibles() {
		return odtsDisponibles;
	}

	public void setOdtsDisponibles(List<ODTTO> odtsDisponibles) {
		this.odtsDisponibles = odtsDisponibles;
	}
}
