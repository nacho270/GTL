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
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoPasadas;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.ProcedimientoAbstract;

@Entity
@Table(name = "T_PROCEDIMIENTO_ODT")
public class ProcedimientoODT extends ProcedimientoAbstract implements Serializable {

	private static final long serialVersionUID = 5323182578948508912L;
	
	private List<InstruccionProcedimientoODT> pasos;
	
	public ProcedimientoODT() {
		pasos = new ArrayList<InstruccionProcedimientoODT>();
	}

	public ProcedimientoODT(ProcedimientoTipoArticulo subProceso) {
		this.setNombre(subProceso.getNombre());
		this.setPasos(convertToPasosODT(subProceso.getPasos()));
		this.setTipoArticulo(subProceso.getTipoArticulo());
	}

	@OneToMany(fetch=FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinColumn(name="F_PROC_ODT_P_ID",nullable=true)
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<InstruccionProcedimientoODT> getPasos() {
		return pasos;
	}
	
	public void setPasos(List<InstruccionProcedimientoODT> pasos) {
		this.pasos = pasos;
	}

	private List<InstruccionProcedimientoODT> convertToPasosODT(List<InstruccionProcedimiento> pasos) {
		List<InstruccionProcedimientoODT> pasosTmp = new ArrayList<InstruccionProcedimientoODT>();
		for(InstruccionProcedimiento ip : pasos) {
			if(ip instanceof InstruccionProcedimientoPasadas) {
				InstruccionProcedimientoPasadasODT ippodt = new InstruccionProcedimientoPasadasODT();
				ippodt.setAccion(((InstruccionProcedimientoPasadas) ip).getAccion());
				ippodt.setCantidadPasadas(((InstruccionProcedimientoPasadas) ip).getCantidadPasadas());
				//TODO:
				
			}
		}
		return pasosTmp;
	}

}