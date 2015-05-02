package ar.com.textillevel.modulos.alertas.entidades;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_ALERTA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class Alerta implements Serializable {

	private static final long serialVersionUID = 7957325007605623214L;

	private Integer id;
	private TipoAlerta tipoAlerta;
	private Timestamp fechaMinimaParaMostrar;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="F_TIPO_ALERTA_P_ID")
	public TipoAlerta getTipoAlerta() {
		return tipoAlerta;
	}

	public void setTipoAlerta(TipoAlerta tipoAlerta) {
		this.tipoAlerta = tipoAlerta;
	}

	@Column(name="A_FECHA_MINIMA",nullable=true)
	public Timestamp getFechaMinimaParaMostrar() {
		return fechaMinimaParaMostrar;
	}

	public void setFechaMinimaParaMostrar(Timestamp fechaMinimaParaMostrar) {
		this.fechaMinimaParaMostrar = fechaMinimaParaMostrar;
	}

	public abstract void accept(IVisitorAlerta visitor);
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaMinimaParaMostrar == null) ? 0 : fechaMinimaParaMostrar.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((tipoAlerta == null) ? 0 : tipoAlerta.hashCode());
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
		Alerta other = (Alerta) obj;
		if (fechaMinimaParaMostrar == null) {
			if (other.fechaMinimaParaMostrar != null)
				return false;
		} else if (!fechaMinimaParaMostrar.equals(other.fechaMinimaParaMostrar))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tipoAlerta == null) {
			if (other.tipoAlerta != null)
				return false;
		} else if (!tipoAlerta.equals(other.tipoAlerta))
			return false;
		return true;
	}
}
