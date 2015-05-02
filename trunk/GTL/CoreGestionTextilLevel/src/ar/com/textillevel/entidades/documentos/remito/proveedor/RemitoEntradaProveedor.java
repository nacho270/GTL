package ar.com.textillevel.entidades.documentos.remito.proveedor;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.gente.Proveedor;

@Entity
@Table(name = "T_REMITO_ENTRADA_PROV")
public class RemitoEntradaProveedor implements Serializable {

	private static final long serialVersionUID = -5588649377365224924L;

	private Integer id;
	private Date fechaEmision;
	private Proveedor proveedor;
	private String nroRemito;
	private List<PiezaRemitoEntradaProveedor> piezas;
	private RemitoEntrada remitoEntrada; //Cuando el R.E.P. se genero a partir de uno entrada de cliente (compra de tela)

	public RemitoEntradaProveedor(){
		piezas = new ArrayList<PiezaRemitoEntradaProveedor>();
	}
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_FECHA_EMISION", nullable=false)
	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_PROV_P_ID", nullable=false)
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	@Column(name="A_NRO_REMITO_STR", nullable=false)
	public String getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(String nroRemito) {
		this.nroRemito = nroRemito;
	}

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="F_REMITO_PROV_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<PiezaRemitoEntradaProveedor> getPiezas() {
		return piezas;
	}

	public void setPiezas(List<PiezaRemitoEntradaProveedor> piezas) {
		this.piezas = piezas;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_REM_ENT_CLIENTE_P_ID")
	public RemitoEntrada getRemitoEntrada() {
		return remitoEntrada;
	}

	public void setRemitoEntrada(RemitoEntrada remitoEntrada) {
		this.remitoEntrada = remitoEntrada;
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
		RemitoEntradaProveedor other = (RemitoEntradaProveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	@Transient
	public String toString() {
		return "Nº: " + getNroRemito() + " - Fecha: " + getFechaEmision();
	}

}