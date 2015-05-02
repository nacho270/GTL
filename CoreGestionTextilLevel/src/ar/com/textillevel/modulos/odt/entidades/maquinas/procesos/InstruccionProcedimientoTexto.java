package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;

@Entity
@DiscriminatorValue(value = "IPPT")
public class InstruccionProcedimientoTexto extends InstruccionProcedimiento{

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
	@Transient
	public void accept(IInstruccionProcedimientoVisitor visitor) {
		visitor.visit(this);		
	}
}
