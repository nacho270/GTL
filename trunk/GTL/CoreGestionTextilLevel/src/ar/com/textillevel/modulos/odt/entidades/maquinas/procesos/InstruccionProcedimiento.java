package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.ProcedimientoODT;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;


@Entity
@Table(name = "T_INSTRUCCION_PROCEDIMIENTO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING,name="DISC")
public abstract class InstruccionProcedimiento implements Serializable {

	private static final long serialVersionUID = 7241841347552170556L;

	private Integer id;
	private Byte idTipoSector;
	private String observaciones;
	private ProcedimientoODT procedimiento;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_ID_SECTOR",nullable=false)
	private Byte getIdTipoSector() {
		return idTipoSector;
	}

	private void setIdTipoSector(Byte idTipoSector) {
		this.idTipoSector = idTipoSector;
	}

	@Column(name="A_OBSERVACIONES",nullable=true)
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_PROC_ODT_P_ID",nullable=true,insertable=false, updatable=false)
	public ProcedimientoODT getProcedimiento() {
		return procedimiento;
	}
	
	public void setProcedimiento(ProcedimientoODT procedimiento) {
		this.procedimiento = procedimiento;
	}

	@Transient
	public ESectorMaquina getSectorMaquina(){
		return ESectorMaquina.getById(getIdTipoSector());
	}
	
	public void setSectorMaquina(ESectorMaquina sector){
		setIdTipoSector(sector.getId());
	}

	@Transient
	public abstract ETipoInstruccionProcedimiento getTipo();

	@Transient
	public abstract void accept(IInstruccionProcedimientoVisitor visitor);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstruccionProcedimiento other = (InstruccionProcedimiento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}