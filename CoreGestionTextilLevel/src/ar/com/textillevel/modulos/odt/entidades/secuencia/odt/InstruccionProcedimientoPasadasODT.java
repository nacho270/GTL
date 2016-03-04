package ar.com.textillevel.modulos.odt.entidades.secuencia.odt;

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

import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.AccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimientoPasadas;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.QuimicoCantidad;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.util.GeneradorDescripcionMateriaPrimaUtil;

@Entity
@DiscriminatorValue(value = "IPPODT")
public class InstruccionProcedimientoPasadasODT extends InstruccionProcedimientoODT {

	private static final long serialVersionUID = 3852136236943380971L;

	private Integer cantidadPasadas;
	private Float temperatura;
	private Float velocidad;
	private List<MateriaPrimaCantidadExplotada<Quimico>> quimicosExplotados;
	private AccionProcedimiento accion;

	public InstruccionProcedimientoPasadasODT() {
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

	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_MAT_PRIM_EXP_P_ID",nullable=true)
	public List<MateriaPrimaCantidadExplotada<Quimico>> getQuimicosExplotados() {
		return quimicosExplotados;
	}

	public void setQuimicosExplotados(List<MateriaPrimaCantidadExplotada<Quimico>> quimicosExplotados) {
		this.quimicosExplotados = quimicosExplotados;
	}

	@Override
	public void accept(IInstruccionProcedimientoODTVisitor visitor) {
		visitor.visit(this);
	}

	@Transient
	public String getDescrSimple() {
		String descrQuimicos = "";
		if (!getQuimicosExplotados().isEmpty()) {
			descrQuimicos += "con ";
			if (getQuimicosExplotados().size() == 1) {
				MateriaPrimaCantidad<Quimico> quimicoCantidad = getQuimicosExplotados().get(0).getMateriaPrimaCantidadDesencadenante();
				descrQuimicos += quimicoCantidad.getCantidad() + " " + quimicoCantidad.getUnidad() + " de " + quimicoCantidad.getMateriaPrima().getDescripcion();
			} else {
				for (int i = 0; i < getQuimicosExplotados().size(); i++) {
					MateriaPrimaCantidad<Quimico> quimicoCantidad = getQuimicosExplotados().get(i).getMateriaPrimaCantidadDesencadenante();
					if (i != 0 && i == getQuimicosExplotados().size() - 1) {
						descrQuimicos = descrQuimicos.substring(0, descrQuimicos.length() - 2);
						descrQuimicos += " y ";
						descrQuimicos += quimicoCantidad.getCantidad() + " " + quimicoCantidad.getUnidad() + " de " + quimicoCantidad.getMateriaPrima().getDescripcion();
					} else {
						descrQuimicos += quimicoCantidad.getCantidad() + " " + quimicoCantidad.getUnidad() + " de " + quimicoCantidad.getMateriaPrima().getDescripcion() + ", ";
					}
				}
			}
		}
		return getCantidadPasadas() + " vuelta(s)/pasada(s) a " + getTemperatura() + "ºC " + " y " + getVelocidad() + " M/S " + descrQuimicos;
	}

	@Transient
	public String getDescrDetallada() {
		String descrQuimicos = "";
		if (!getQuimicosExplotados().isEmpty()) {
			descrQuimicos += "con ";
			descrQuimicos += GeneradorDescripcionMateriaPrimaUtil.generarDescripcion(getQuimicosExplotados(), null);
		}
		descrQuimicos = getCantidadPasadas() + " vuelta(s)/pasada(s) a " + getTemperatura() + " ºC " + ". Velocidad: " + getVelocidad() + " M/S " + descrQuimicos;
		return descrQuimicos;
	}

	@Override
	@Transient
	public InstruccionProcedimiento toInstruccionProcedimiento() {
		InstruccionProcedimientoPasadas ipp = new InstruccionProcedimientoPasadas();
		ipp.setAccion(getAccion());
		ipp.setCantidadPasadas(getCantidadPasadas());
		ipp.setObservaciones(getObservaciones());
		ipp.setSectorMaquina(getSectorMaquina());
		ipp.setTemperatura(getTemperatura());
		ipp.setVelocidad(getVelocidad());
		for (MateriaPrimaCantidadExplotada<Quimico> qc : getQuimicosExplotados()) {
			QuimicoCantidad quimicoCantidad = new QuimicoCantidad();
			quimicoCantidad.setCantidad(qc.getCantidadExplotada());
			quimicoCantidad.setMateriaPrima(qc.getMateriaPrimaCantidadDesencadenante().getMateriaPrima());
			quimicoCantidad.setUnidad(qc.getMateriaPrimaCantidadDesencadenante().getUnidad());
			ipp.getQuimicos().add(quimicoCantidad);
		}
		return ipp;
	}

}