package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "T_DEFINICION_PRECIO_LISTA")
public class DefinicionPrecio implements Serializable {

	private static final long serialVersionUID = 1261085515831398091L;

	private Integer id;
	private List<RangoAncho> rangos;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "F_DEFINICION_PRECIO_P_ID")
	@org.hibernate.annotations.Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public List<RangoAncho> getRangos() {
		return rangos;
	}

	public void setRangos(List<RangoAncho> rangos) {
		this.rangos = rangos;
	}
}
