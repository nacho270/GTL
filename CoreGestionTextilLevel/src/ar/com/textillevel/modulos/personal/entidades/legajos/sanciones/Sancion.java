package ar.com.textillevel.modulos.personal.entidades.legajos.sanciones;

import java.io.Serializable;
import java.sql.Date;

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

import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.visitor.ISancionVisitor;

@Entity
@Table(name = "T_PERS_SANCION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class Sancion implements Serializable {

	private static final long serialVersionUID = 5403321566768125127L;

	private Integer id;
	private Date fechaSancion;
	private LegajoEmpleado legajo;
	private String motivo;
	private String observaciones;
	private CartaDocumento cartaDocumento;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_FECHA", nullable=false)
	public Date getFechaSancion() {
		return fechaSancion;
	}

	public void setFechaSancion(Date fechaSancion) {
		this.fechaSancion = fechaSancion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_LEGAJO_P_ID", nullable=false)
	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	@Column(name = "A_MOTIVO", nullable=false,length=700)
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Column(name = "A_OBSERVACIONES", nullable=true,length=700)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_CARTA_DOC_P_ID", insertable=false, updatable=false)
	public CartaDocumento getCartaDocumento() {
		return cartaDocumento;
	}

	public void setCartaDocumento(CartaDocumento cartaDocumento) {
		this.cartaDocumento = cartaDocumento;
	}

	@Transient
	public abstract String getResumen();

	@Transient
	public abstract ETipoSancion getTipoSancion();

	@Transient
	public abstract void accept(ISancionVisitor visitor);
	
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
		Sancion other = (Sancion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}