package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.ProcedimientoODT;

public class ProcedimientoODTTO implements Serializable {

	private static final long serialVersionUID = -6300279453051435411L;

	private Integer idTipoArticulo;
	private String nombre;
	private List<InstruccionProcedimientoODTTO> pasos;
	
	public ProcedimientoODTTO() {
		
	}
	
	public ProcedimientoODTTO(ProcedimientoODT procODT) {
		this.nombre = procODT.getNombre();
		this.idTipoArticulo = procODT.getTipoArticulo().getId();
		setPasos(new ArrayList<InstruccionProcedimientoODTTO>());
		for(InstruccionProcedimientoODT paso : procODT.getPasos()) {
			getPasos().add(new InstruccionProcedimientoODTTO(paso));
		}
	}

	public List<InstruccionProcedimientoODTTO> getPasos() {
		return pasos;
	}

	public void setPasos(List<InstruccionProcedimientoODTTO> pasos) {
		this.pasos = pasos;
	}

	public Integer getIdTipoArticulo() {
		return idTipoArticulo;
	}

	public void setIdTipoArticulo(Integer idTipoArticulo) {
		this.idTipoArticulo = idTipoArticulo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}