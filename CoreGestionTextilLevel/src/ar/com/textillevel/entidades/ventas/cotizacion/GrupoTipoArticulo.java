package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;

@MappedSuperclass
public class GrupoTipoArticulo implements Serializable{

	private static final long serialVersionUID = 2918848319937718773L;

	private Integer id;
	private TipoArticulo tipoArticulo;
	
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
	@JoinColumn(name = "F_TIPO_ARTICULO_P_ID", nullable = false)
	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

}
