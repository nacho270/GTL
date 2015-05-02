package ar.com.textillevel.entidades.documentos.ordendedeposito;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.com.textillevel.entidades.cheque.Banco;

@Entity
@Table(name = "T_ORDEN_DEPOSITO")
public class OrdenDeDeposito implements Serializable {

	private static final long serialVersionUID = 1000073968339563728L;

	private Integer id;
	private Integer nroOrden;
	private Date fecha;
	private List<DepositoCheque> depositos;
	private String totalLetras;
	private BigDecimal montoTotal;
	private Banco banco;
	private String personaResponsable;
	
	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_NRO_ORDEN",nullable=false)
	public Integer getNroOrden() {
		return nroOrden;
	}

	public void setNroOrden(Integer nroOrden) {
		this.nroOrden = nroOrden;
	}

	@Column(name = "A_FECHA", nullable = false)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "F_ORDEN_DEPOSITO_P_ID", nullable = false)
	public List<DepositoCheque> getDepositos() {
		return depositos;
	}

	public void setDepositos(List<DepositoCheque> depositos) {
		this.depositos = depositos;
	}

	@Column(name = "A_TOTAL_LETRAS", nullable = false)
	public String getTotalLetras() {
		return totalLetras;
	}

	public void setTotalLetras(String totalLetras) {
		this.totalLetras = totalLetras;
	}

	@Column(name = "A_MONTO_TOTAL", nullable = false)
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	@ManyToOne
	@JoinColumn(name="F_BANCO_P_ID",nullable=false)
	public Banco getBanco() {
		return banco;
	}
	
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
	@Column(name="A_PERSONA_RESPONSABLE", nullable=false)
	public String getPersonaResponsable() {
		return personaResponsable;
	}

	public void setPersonaResponsable(String personaResponsable) {
		this.personaResponsable = personaResponsable;
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
		OrdenDeDeposito other = (OrdenDeDeposito) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
