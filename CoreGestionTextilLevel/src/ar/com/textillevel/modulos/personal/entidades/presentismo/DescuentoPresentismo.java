package ar.com.textillevel.modulos.personal.entidades.presentismo;

import java.io.Serializable;
import java.math.BigDecimal;

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

import ar.com.textillevel.modulos.personal.entidades.presentismo.visitor.IDescuentoPresentismoVisitor;

@Entity
@Table(name = "T_PERS_DESC_PRESENTISMO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "DESC")
public abstract class DescuentoPresentismo implements Serializable {

	private static final long serialVersionUID = 2843868525129192095L;

	private Integer id;
	private BigDecimal porcentajeDescuento;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_PORC_DESCUENTO", nullable = false)
	public BigDecimal getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}
	
	@Transient
	public abstract void visit(IDescuentoPresentismoVisitor visitor);
}
