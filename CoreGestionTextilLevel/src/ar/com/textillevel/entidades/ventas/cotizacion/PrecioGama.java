package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.entidades.ventas.articulos.GamaColor;
import ar.com.textillevel.entidades.ventas.articulos.GamaColorCliente;

@Entity
@Table(name = "T_PRECIO_GAMA")
public class PrecioGama implements Serializable{

	private static final long serialVersionUID = -4042531226341019113L;
	
	private Integer id;
	private GamaColorCliente gamaCliente; // nullable.Si no esta, se lee la default
	private GamaColor gamaDefault; // no nullable. Se lee de aca si la otra es null
	private Float precio;

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
	@JoinColumn(name = "F_GAMA_CLIENTE_P_ID", nullable = true)
	public GamaColorCliente getGamaCliente() {
		return gamaCliente;
	}

	public void setGamaCliente(GamaColorCliente gamaCliente) {
		this.gamaCliente = gamaCliente;
	}

	@ManyToOne
	@JoinColumn(name = "F_GAMA_DEFAULT_P_ID", nullable = true)
	public GamaColor getGamaDefault() {
		return gamaDefault;
	}

	public void setGamaDefault(GamaColor gamaDefault) {
		this.gamaDefault = gamaDefault;
	}

	@Column(name = "A_PRECIO", nullable = false)
	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}
}
