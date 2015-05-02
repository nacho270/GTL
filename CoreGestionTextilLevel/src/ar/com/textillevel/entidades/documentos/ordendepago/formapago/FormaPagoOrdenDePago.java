package ar.com.textillevel.entidades.documentos.ordendepago.formapago;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="T_FORMA_PAGO_ORDEN_DE_PAGO")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TIPO",discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(value="FP")
public abstract class FormaPagoOrdenDePago implements Serializable {

	private static final long serialVersionUID = 8655022679930802595L;

	private Integer id;
	private Timestamp fechaEmision;

	@Transient
	public abstract BigDecimal getImporte();

	@Transient
	public abstract String getDescripcion();

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_FECHA_EMISION", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Timestamp getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Timestamp fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public abstract void accept(IFormaPagoVisitor visitor);
	
}
