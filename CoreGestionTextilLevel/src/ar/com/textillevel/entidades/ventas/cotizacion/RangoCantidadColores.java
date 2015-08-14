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
@Table(name = "T_RANGO_CANTIDAD_COLORES")
public class RangoCantidadColores implements Serializable {

	private static final long serialVersionUID = -1466816699070964522L;

	private Integer id;
	private Integer minimo;
	private Integer maximo;
	private List<RangoCoberturaEstampado> rangos;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_MINIMO", nullable = false)
	public Integer getMinimo() {
		return minimo;
	}

	public void setMinimo(Integer minimo) {
		this.minimo = minimo;
	}

	@Column(name = "A_MAXIMO", nullable = false)
	public Integer getMaximo() {
		return maximo;
	}

	public void setMaximo(Integer maximo) {
		this.maximo = maximo;
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_RANGO_CANT_COLORES_P_ID")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<RangoCoberturaEstampado> getRangos() {
		return rangos;
	}

	public void setRangos(List<RangoCoberturaEstampado> rangos) {
		this.rangos = rangos;
	}
}
