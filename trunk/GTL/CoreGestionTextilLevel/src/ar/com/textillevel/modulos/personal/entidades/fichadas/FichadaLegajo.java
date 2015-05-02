package ar.com.textillevel.modulos.personal.entidades.fichadas;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.IndexColumn;

import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.EFormaIngresoFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.ETipoFichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;

@Entity
@Table(name = "T_PERS_FICHADAS")
public class FichadaLegajo implements Serializable, Cloneable {

	private static final long serialVersionUID = -8338441476588669621L;

	private Integer id;
	private LegajoEmpleado legajo;
	private Timestamp horario;
	private Integer idTipoFichada;
	private Integer idFormaIngresoFichada;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "F_LEGAJO_P_ID")
	@IndexColumn(name = "F_LEGAJO_P_ID")
	public LegajoEmpleado getLegajo() {
		return legajo;
	}

	public void setLegajo(LegajoEmpleado legajo) {
		this.legajo = legajo;
	}

	@Column(name = "A_HORARIO", nullable = false)
	public Timestamp getHorario() {
		return horario;
	}

	public void setHorario(Timestamp horario) {
		this.horario = horario;
	}

	@Column(name = "A_ID_TIPO_FICHADA", nullable = false)
	private Integer getIdTipoFichada() {
		return idTipoFichada;
	}

	private void setIdTipoFichada(Integer idTipoFichada) {
		this.idTipoFichada = idTipoFichada;
	}

	@Transient
	public ETipoFichada getTipoFichada() {
		return ETipoFichada.getById(getIdTipoFichada());
	}

	public void setTipoFichada(ETipoFichada tipoFichada) {
		setIdTipoFichada(tipoFichada.getId());
	}

	@Column(name = "A_ID_FORMA_INGRESO_FICHADA", nullable = false)
	private Integer getIdFormaIngresoFichada() {
		return idFormaIngresoFichada;
	}

	private void setIdFormaIngresoFichada(Integer idFormaIngresoFichada) {
		this.idFormaIngresoFichada = idFormaIngresoFichada;
	}

	@Transient
	public EFormaIngresoFichada getFormaIngresoFichada() {
		return EFormaIngresoFichada.getById(getIdFormaIngresoFichada());
	}

	public void setFormaIngresoFichada(EFormaIngresoFichada formaIngreso) {
		setIdFormaIngresoFichada(formaIngreso.getId());
	}

	@Transient
	@Override
	public FichadaLegajo clone() {
		FichadaLegajo copia = null;
		try {
			copia = (FichadaLegajo) super.clone();
		} catch (CloneNotSupportedException e) { }
		return copia;
	}

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
		FichadaLegajo other = (FichadaLegajo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
