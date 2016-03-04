package ar.com.textillevel.modulos.odt.entidades.secuencia.odt;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoTexto;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;

@Entity
@DiscriminatorValue(value = "IPPTODT")
public class InstruccionProcedimientoTextoODT extends InstruccionProcedimientoODT {

	private static final long serialVersionUID = 4633562184716501059L;

	private String especificacion;
	
	@Column(name="A_ESPECIFICACION")
	public String getEspecificacion() {
		return especificacion;
	}
	
	public void setEspecificacion(String especificacion) {
		this.especificacion = especificacion;
	}
	
	@Override
	@Transient
	public ETipoInstruccionProcedimiento getTipo() {
		return ETipoInstruccionProcedimiento.TEXTO;
	}

	@Override
	public void accept(IInstruccionProcedimientoODTVisitor visitor) {
		visitor.visit(this);
	}

	@Transient
	public String getDescrSimple() {
		return getEspecificacion();
	}

	@Transient
	public String getDescrDetallada() {
		return getEspecificacion();
	}

	@Override
	@Transient
	public InstruccionProcedimiento toInstruccionProcedimiento() {
		InstruccionProcedimientoTexto ipt = new InstruccionProcedimientoTexto();
		ipt.setEspecificacion(getEspecificacion());
		ipt.setObservaciones(getObservaciones());
		ipt.setSectorMaquina(getSectorMaquina());
		return ipt;
	}

}
