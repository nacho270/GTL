package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Color;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class FormulaCliente implements Serializable {

	private static final long serialVersionUID = 6171393380691150327L;

	private Integer id;
	private String nombre;
	private Cliente cliente;
	private Color color;
	private Integer nroFormula;
	private String codigoFormula;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_NOMBRE", nullable=true)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_CLIENTE_P_ID")
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@ManyToOne
	@JoinColumn(name="F_COLOR_P_ID", nullable=true)
	@Fetch(FetchMode.JOIN)
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Column(name = "A_CODIGO_FORMULA", nullable=false)
	public String getCodigoFormula() {
		return codigoFormula;
	}

	public void setCodigoFormula(String codigoFormula) {
		this.codigoFormula = codigoFormula;
	}

	@Column(name = "A_NRO_FORMULA", nullable=false)
	public Integer getNroFormula() {
		return nroFormula;
	}

	public void setNroFormula(Integer nroFormula) {
		this.nroFormula = nroFormula;
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
		FormulaCliente other = (FormulaCliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
			else 
				return obj == this;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getCodigoFormula() + (getNombre() != null? " - " + getNombre() : "");
	}

	@Transient
	public abstract void accept(IFormulaClienteVisitor visitor);

}