package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;

@Entity
@DiscriminatorValue(value = "IPP")
public class InstruccionProcedimientoPasadas extends InstruccionProcedimiento {

	private static final long serialVersionUID = 3852136236943380971L;

	private Integer cantidadPasadas;
	private Float temperatura;
	private Float velocidad;
	private List<QuimicoCantidad> quimicos;
	private List<MateriaPrimaCantidadExplotada<Quimico>> quimicosExplotados;
	private AccionProcedimiento accion;

	public InstruccionProcedimientoPasadas() {
		quimicos = new ArrayList<QuimicoCantidad>();
		quimicosExplotados = new ArrayList<MateriaPrimaCantidadExplotada<Quimico>>();
	}

	@Column(name="A_CANT_PASADAS",nullable=true)
	public Integer getCantidadPasadas() {
		return cantidadPasadas;
	}

	public void setCantidadPasadas(Integer cantidadPasadas) {
		this.cantidadPasadas = cantidadPasadas;
	}

	@Column(name="A_TEMPERATURA",nullable=true)
	public Float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Float temperatura) {
		this.temperatura = temperatura;
	}

	@Column(name="A_VELOCIDAD",nullable=true)
	public Float getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(Float velocidad) {
		this.velocidad = velocidad;
	}

	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_INSTRUCCIONP_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<QuimicoCantidad> getQuimicos() {
		return quimicos;
	}

	public void setQuimicos(List<QuimicoCantidad> quimicos) {
		this.quimicos = quimicos;
	}

	@ManyToOne
	@JoinColumn(name="F_ACCION_P_ID")
	public AccionProcedimiento getAccion() {
		return accion;
	}
	
	public void setAccion(AccionProcedimiento accion) {
		this.accion = accion;
	}
	
	@Override
	@Transient
	public ETipoInstruccionProcedimiento getTipo() {
		return ETipoInstruccionProcedimiento.PASADA;
	}

	@Override
	@Transient
	public void accept(IInstruccionProcedimientoVisitor visitor) {
		visitor.visit(this);
	}

	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_MAT_PRIM_EXP_P_ID",nullable=true)
	public List<MateriaPrimaCantidadExplotada<Quimico>> getQuimicosExplotados() {
		return quimicosExplotados;
	}

	public void setQuimicosExplotados(List<MateriaPrimaCantidadExplotada<Quimico>> quimicosExplotados) {
		this.quimicosExplotados = quimicosExplotados;
	}
}
