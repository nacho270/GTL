package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.ProcedimientoAbstract;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.ProcedimientoODT;

@Entity
@Table(name = "T_PROCEDIMIENTO_TIPO_ARTICULO")
public class ProcedimientoTipoArticulo extends ProcedimientoAbstract implements Serializable {

	private static final long serialVersionUID = 5323182578948508912L;

	private List<InstruccionProcedimiento> pasos;

	public ProcedimientoTipoArticulo() {
		pasos = new ArrayList<InstruccionProcedimiento>();
	}
	
	public ProcedimientoTipoArticulo(ProcedimientoODT procODT){
		this.setNombre(procODT.getNombre());
		this.setPasos(toInstruccionesFromInstruccionesODT(procODT.getPasos()));
		this.setTipoArticulo(procODT.getTipoArticulo());
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "T_PROCEDIMIENTO_INSTRUCCION_ASOC", 
		joinColumns = { 
			@JoinColumn(name = "F_PROCEDIMIENTO_P_ID") 
		}, 
		inverseJoinColumns = { 
			@JoinColumn(name = "F_INSTRUCCION_P_ID")
		}
	)
	public List<InstruccionProcedimiento> getPasos() {
		return pasos;
	}

	
	public void setPasos(List<InstruccionProcedimiento> pasos) {
		this.pasos = pasos;
	}

	private List<InstruccionProcedimiento> toInstruccionesFromInstruccionesODT(List<InstruccionProcedimientoODT> instruccionesODT) {
		List<InstruccionProcedimiento> instruccionList = new ArrayList<InstruccionProcedimiento>();
		for(InstruccionProcedimientoODT iodt : instruccionesODT) {
			instruccionList.add(iodt.toInstruccionProcedimiento());
		}
		return instruccionList;
	}

}