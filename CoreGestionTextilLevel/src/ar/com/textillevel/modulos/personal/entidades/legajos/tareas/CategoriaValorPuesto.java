package ar.com.textillevel.modulos.personal.entidades.legajos.tareas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "T_PERS_CATEG_VAL_PUESTO")
public class CategoriaValorPuesto implements Serializable {

	private static final long serialVersionUID = -967919796063930896L;

	private Integer id;
	private Categoria categoria;
	private BigDecimal valorHoraDefault;
	private List<ValorPuesto> valoresPuesto;

	public CategoriaValorPuesto() {
		this.valoresPuesto = new ArrayList<ValorPuesto>();
		this.valorHoraDefault = new BigDecimal(0);
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="F_CATEGORIA_P_ID",nullable=false)
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Column(name = "A_VALOR_HORA_DEFAULT", nullable=false)
	public BigDecimal getValorHoraDefault() {
		return valorHoraDefault;
	}

	public void setValorHoraDefault(BigDecimal valorHoraDefault) {
		this.valorHoraDefault = valorHoraDefault;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_CAT_VALOR_PUESTO_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ValorPuesto> getValoresPuesto() {
		return valoresPuesto;
	}

	public void setValoresPuesto(List<ValorPuesto> valoresPuesto) {
		this.valoresPuesto = valoresPuesto;
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
		CategoriaValorPuesto other = (CategoriaValorPuesto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public BigDecimal getValorHoraParaPuesto(Puesto puesto) {
		for(ValorPuesto vp : getValoresPuesto()) {
			if(vp.getPuesto().equals(puesto)) {
				return vp.getValorHora();
			}
		}
		return getValorHoraDefault();
	}

}