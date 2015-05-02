package ar.com.textillevel.modulos.odt.entidades.secuencia.odt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.ProcedimientoAbstract;

@Entity
@Table(name = "T_PROCEDIMIENTO_ODT")
public class ProcedimientoODT extends ProcedimientoAbstract implements Serializable {

	private static final long serialVersionUID = 5323182578948508912L;
	
	private List<InstruccionProcedimiento> pasos;
	
	public ProcedimientoODT() {
		pasos = new ArrayList<InstruccionProcedimiento>();
	}

	public ProcedimientoODT(ProcedimientoTipoArticulo subProceso) {
		this.setNombre(subProceso.getNombre());
		this.setPasos(new ArrayList<InstruccionProcedimiento>(subProceso.getPasos()));
		this.setTipoArticulo(subProceso.getTipoArticulo());
	}

//	@ManyToMany(fetch = FetchType.EAGER,cascade={CascadeType.ALL})
//	@JoinTable(name = "T_PROCEDIMIENTO_ODT_INSTRUCCION_ASOC", 
//		joinColumns = { 
//			@JoinColumn(name = "F_PROCEDIMIENTO_P_ID") 
//		}, 
//		inverseJoinColumns = { 
//			@JoinColumn(name = "F_INSTRUCCION_P_ID") 
//		}
//	)
	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinColumn(name="F_PROC_ODT_P_ID",nullable=true)
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<InstruccionProcedimiento> getPasos() {
		return pasos;
	}
	
	public void setPasos(List<InstruccionProcedimiento> pasos) {
		this.pasos = pasos;
	}
}
