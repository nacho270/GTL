package ar.com.textillevel.modulos.odt.entidades.maquinas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;


@Entity
@Table(name="T_MAQUINA")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING,name="DISC")
public abstract class Maquina implements Serializable {

	private static final long serialVersionUID = 8625141654793255028L;

	private Integer id;
	private String nombre;
	private TipoMaquina tipoMaquina;
	private Float potencia;
	private Float anchoMin;
	private Float anchoMax;
	private List<TipoArticulo> tipoArticulos;

	protected Maquina() {
		this.tipoArticulos = new ArrayList<TipoArticulo>();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_NOMBRE",nullable=false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToOne
	@JoinColumn(name="F_TIPO_MAQUINA_P_ID",nullable=false)
	public TipoMaquina getTipoMaquina() {
		return tipoMaquina;
	}

	public void setTipoMaquina(TipoMaquina tipoMaquina) {
		this.tipoMaquina = tipoMaquina;
	}

	@Column(name="A_POTENCIA")
	public Float getPotencia() {
		return potencia;
	}

	public void setPotencia(Float potencia) {
		this.potencia = potencia;
	}

	@Column(name="A_ANCHO_MIN")
	public Float getAnchoMin() {
		return anchoMin;
	}

	public void setAnchoMin(Float anchoMin) {
		this.anchoMin = anchoMin;
	}

	@Column(name="A_ANCHO_MAX")
	public Float getAnchoMax() {
		return anchoMax;
	}

	public void setAnchoMax(Float anchoMax) {
		this.anchoMax = anchoMax;
	}

	@ManyToMany
	@JoinTable(name = "T_MAQUINA_TIPO_ARTICULO_ASOC", 
			joinColumns = { @JoinColumn(name = "F_MAQUINA_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_TIPO_ARTICULO_P_ID") })
	public List<TipoArticulo> getTipoArticulos() {
		return tipoArticulos;
	}

	public void setTipoArticulos(List<TipoArticulo> tipoArticulos) {
		this.tipoArticulos = tipoArticulos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Maquina other = (Maquina) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return nombre;
	}

	@Transient
	public ESectorMaquina getSector() {
		return getTipoMaquina().getSectorMaquina();
	}

}
