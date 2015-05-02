package ar.com.textillevel.entidades.cheque;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.entidades.gente.InfoDireccion;

@Entity
@Table(name = "T_BANCO")
public class Banco implements Serializable {
	
	private static final long serialVersionUID = 6327958364158880705L;

	private Integer id;
	private String nombre;
	private InfoDireccion direccion;
	private String telefono;
	private Integer codigoBanco;
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_NOMBRE")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="F_INFO_DIRECCION_P_ID")
	public InfoDireccion getDireccion() {
		return direccion;
	}

	public void setDireccion(InfoDireccion direccion) {
		this.direccion = direccion;
	}
	
	@Column(name="A_TELEFONO", nullable=true, length=20)
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	@Column(name="A_COD_BANCO", nullable=false)
	public Integer getCodigoBanco() {
		return codigoBanco;
	}
	
	public void setCodigoBanco(Integer codigoBanco) {
		this.codigoBanco = codigoBanco;
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
		Banco other = (Banco) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nombre.toString();
	}
}
