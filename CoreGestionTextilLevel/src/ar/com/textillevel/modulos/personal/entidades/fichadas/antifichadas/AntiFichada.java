package ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.EMotivoAntifichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.enums.ETipoAntiFichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;

@Entity
@Table(name = "T_PERS_ANTI_FICHADAS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class AntiFichada implements Serializable {

	private static final long serialVersionUID = 113898289328464432L;

	private Integer id;
	private LegajoEmpleado legajo;
	private String descripcion;
	private Integer idMotivoAntiFichada;
	private Boolean justificada;
	private Sancion sancion;
	private ValeAtencion valeAtencion;
	private RegistroVacacionesLegajo registroVacaciones;

	public AntiFichada() {
		this.justificada = false;
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_LEGAJO_P_ID", nullable = false)
	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	@Column(name = "A_DESCRIPCION", nullable = true)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "A_JUSTIFICADA", nullable = false)
	public Boolean getJustificada() {
		return justificada;
	}

	public void setJustificada(Boolean justificada) {
		this.justificada = justificada;
	}

	private Integer getIdMotivoAntiFichada() {
		return idMotivoAntiFichada;
	}

	private void setIdMotivoAntiFichada(Integer idMotivoAntiFichada) {
		this.idMotivoAntiFichada = idMotivoAntiFichada;
	}

	@Transient
	public EMotivoAntifichada getMotivoAntiFichada() {
		return EMotivoAntifichada.getById(getIdMotivoAntiFichada());
	}

	public void setMotivoAntifichada(EMotivoAntifichada tipo) {
		if (tipo == null) {
			setIdMotivoAntiFichada(null);
			return;
		}
		setIdMotivoAntiFichada(tipo.getId());
	}

	@ManyToOne
	@JoinColumn(name = "F_SANCION_P_ID", nullable=true)
	public Sancion getSancion() {
		return sancion;
	}

	public void setSancion(Sancion sancion) {
		this.sancion = sancion;
	}

	@ManyToOne
	@JoinColumn(name = "F_VALE_ATENCION_P_ID", nullable=true)
	public ValeAtencion getValeAtencion() {
		return valeAtencion;
	}

	public void setValeAtencion(ValeAtencion valeAtencion) {
		this.valeAtencion = valeAtencion;
	}

	@Transient
	public abstract ETipoAntiFichada getTipoAntiFichada();

	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "F_REG_VAC_P_ID", nullable=true)
	public RegistroVacacionesLegajo getRegistroVacaciones() {
		return registroVacaciones;
	}
	
	public void setRegistroVacaciones(RegistroVacacionesLegajo registroVacaciones) {
		this.registroVacaciones = registroVacaciones;
	}

}