package ar.com.textillevel.modulos.odt.entidades.secuencia.fw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;

@MappedSuperclass
public abstract class ProcedimientoAbstract implements Serializable{

	private static final long serialVersionUID = -1361441075632777305L;
	
	private Integer id;
	private TipoArticulo tipoArticulo;
	private String nombre;

	public ProcedimientoAbstract() {
		
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_TIPO_ARTICULO_P_ID", nullable = false)
	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

	@Column(name = "A_NOMBRE", nullable = false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return nombre.toUpperCase();
	}
}
