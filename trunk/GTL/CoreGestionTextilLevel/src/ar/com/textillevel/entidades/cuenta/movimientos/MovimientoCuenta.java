package ar.com.textillevel.entidades.cuenta.movimientos;

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

import ar.com.textillevel.entidades.cuenta.Cuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.visitor.IFilaMovimientoVisitor;

@Entity
@Table(name = "T_MOVIMIENTO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class MovimientoCuenta implements Serializable {

	private static final long serialVersionUID = -1502127190652843873L;

	private Integer id;
	private Cuenta cuenta;
	private String descripcionResumen; // SE ARMA CON LA FACTURA Y REMITO O CON EL RECIBO
	private BigDecimal monto; // CALCULABLE DE LA FACTURA O RECIBO
	private Timestamp fechaHora;
	// private DetalleMovimiento detalle;
	private String observaciones;

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
	@JoinColumn(name = "F_CUENTA_P_ID", nullable = false)
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	@Column(name = "A_DESCRIPCION", nullable = false)
	public String getDescripcionResumen() {
		return descripcionResumen;
	}

	public void setDescripcionResumen(String descripcionResumen) {
		this.descripcionResumen = descripcionResumen;
	}

	@Column(name = "A_MONTO", nullable = false)
	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "A_FECHA_HORA", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	public abstract void aceptarVisitor(IFilaMovimientoVisitor visitor);

	@Column(name = "A_OBSERVACIONES", nullable = true)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
		MovimientoCuenta other = (MovimientoCuenta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}