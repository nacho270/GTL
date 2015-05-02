package ar.com.textillevel.entidades.cuentaarticulo.movimientos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.entidades.cuentaarticulo.CuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.visitor.IMovimientoCuentaArticuloVisitor;
import ar.com.textillevel.entidades.portal.UsuarioSistema;

@Entity
@Table(name = "T_MOVIMIENTO_CTA_ART")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class MovimientoCuentaArticulo implements Serializable {

	private static final long serialVersionUID = 7255996588456260441L;

	private Integer id;
	private CuentaArticulo cuenta;
	private String descripcionResumen;
	private BigDecimal cantidad;
	private Timestamp fechaHora;
	private UsuarioSistema usuarioConfirmacion;
	private String observaciones;
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="F_CUENTA_P_ID", nullable=false)
	public CuentaArticulo getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaArticulo cuenta) {
		this.cuenta = cuenta;
	}

	@Column(name="A_DESCRIPCION", nullable=false)
	public String getDescripcionResumen() {
		return descripcionResumen;
	}

	public void setDescripcionResumen(String descripcionResumen) {
		this.descripcionResumen = descripcionResumen;
	}

	@Column(name="A_CANTIDAD", nullable=false)
	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal monto) {
		this.cantidad = monto;
	}

	@Column(name = "A_FECHA_HORA", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	@ManyToOne
	@JoinColumn(name="F_USUARIO_P_ID",nullable=true)
	public UsuarioSistema getUsuarioConfirmacion() {
		return usuarioConfirmacion;
	}

	public void setUsuarioConfirmacion(UsuarioSistema usuarioConfirmacion) {
		this.usuarioConfirmacion = usuarioConfirmacion;
	}

	@Column(name="A_OBSERVACIONES",nullable=true)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public abstract void aceptarVisitor(IMovimientoCuentaArticuloVisitor visitor);

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
		MovimientoCuentaArticulo other = (MovimientoCuentaArticulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}