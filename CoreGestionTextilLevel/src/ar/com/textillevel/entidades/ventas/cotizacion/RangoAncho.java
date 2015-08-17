package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "T_RANGO_ANCHO_ARTICULO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
public abstract class RangoAncho implements Serializable{

	private static final long serialVersionUID = 1181170583796051214L;
	
	private Integer id;
	private Float anchoMinimo; // nulleable, si el otro no es null
	private Float anchoMaximo; // nulleable, si el otro no es null
	private Float anchoExacto; // nulleable, si el otro no es null
	
	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_ANCHO_MINIMO", nullable = false)
	public Float getAnchoMinimo() {
		return anchoMinimo;
	}

	public void setAnchoMinimo(Float anchoMinimo) {
		this.anchoMinimo = anchoMinimo;
	}

	@Column(name = "A_ANCHO_MAXIMO")
	public Float getAnchoMaximo() {
		return anchoMaximo;
	}

	public void setAnchoMaximo(Float anchoMaximo) {
		this.anchoMaximo = anchoMaximo;
	}

	@Column(name = "A_ANCHO_MAXIMO_EXACTO")
	public Float getAnchoExacto() {
		return anchoExacto;
	}

	public void setAnchoExacto(Float anchoExacto) {
		this.anchoExacto = anchoExacto;
	}
}
