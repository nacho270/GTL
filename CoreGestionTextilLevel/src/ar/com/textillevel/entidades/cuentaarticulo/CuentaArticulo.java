package ar.com.textillevel.entidades.cuentaarticulo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Entity
@Table(name = "T_CUENTA_ARTICULO")
public class CuentaArticulo implements Serializable {

	private static final long serialVersionUID = -7576478478255553763L;

	private Integer id;
	private BigDecimal cantidad;
	private Integer idTipoUnidad;
	private Timestamp fechaCreacion;
	private Articulo articulo;
	private Cliente cliente;

	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_CANTIDAD", nullable=false)
	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	@Column(name="A_TIPO_UNIDAD", nullable=false)
	private Integer getIdTipoUnidad() {
		return idTipoUnidad;
	}

	private void setIdTipoUnidad(Integer idTipoUnidad) {
		this.idTipoUnidad = idTipoUnidad;
	}

	public void setTipoUnidad(EUnidad tipoUnidad) {
		if (tipoUnidad == null) {
			this.setIdTipoUnidad(null);
		}
		setIdTipoUnidad(tipoUnidad.getId());
	}

	@Transient
	public EUnidad getTipoUnidad() {
		if (getIdTipoUnidad() == null) {
			return null;
		}
		return EUnidad.getById(getIdTipoUnidad());
	}

	
	@Column(name = "A_FECHA_CREACION", nullable=false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@ManyToOne
	@JoinColumn(name="F_ARTICULO_P_ID", nullable=false)
	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	@ManyToOne
	@JoinColumn(name="F_CLIENTE_P_ID", nullable=false)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
		CuentaArticulo other = (CuentaArticulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}