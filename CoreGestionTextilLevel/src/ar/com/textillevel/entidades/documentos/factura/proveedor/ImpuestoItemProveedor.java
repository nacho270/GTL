package ar.com.textillevel.entidades.documentos.factura.proveedor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.gente.Provincia;

@Entity
@Table(name = "T_IMPUESTO_ITEM_PROVEEDOR")
public class ImpuestoItemProveedor implements Serializable {

	private static final long serialVersionUID = 6680476689054579696L;

	private Integer id;
	private double porcDescuento;
	private String nombre;
	private Integer idTipoImpuesto;
	private Boolean aplicaEnProvincia;
	private Provincia provincia;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_PORC_DESC", nullable=false)
	public double getPorcDescuento() {
		return porcDescuento;
	}

	public void setPorcDescuento(double porcDescuento) {
		this.porcDescuento = porcDescuento;
	}

	@Column(name = "A_NOMBRE", nullable=false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "A_ID_TIPO_IMPUESTO", nullable=false)
	private Integer getIdTipoImpuesto() {
		return idTipoImpuesto;
	}

	private void setIdTipoImpuesto(Integer idTipoImpuesto) {
		this.idTipoImpuesto = idTipoImpuesto;
	}

	@Column(name = "A_APLICA_EN_PCIA", nullable=false)
	public Boolean getAplicaEnProvincia() {
		return aplicaEnProvincia;
	}

	public void setAplicaEnProvincia(Boolean aplicaEnProvincia) {
		this.aplicaEnProvincia = aplicaEnProvincia;
	}

	@Transient
	public ETipoImpuesto getTipoImpuesto(){
		if(getIdTipoImpuesto() == null){
			return null;
		}
		return ETipoImpuesto.getById(getIdTipoImpuesto());
	}

	public void setTipoImpuesto(ETipoImpuesto tipoImpuesto){
		if(tipoImpuesto == null){
			setIdTipoImpuesto(null);
		}
		setIdTipoImpuesto(tipoImpuesto.getId());
	}
	
	@ManyToOne
	@JoinColumn(name="F_PROVINCIA_P_ID",nullable=true)
	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}
	
	@Override
	public String toString() {
		if(id == -1) {
			return getNombre();
		} else {
			return getNombre() + " " + getPorcDescuento() + "%" + (getProvincia() !=null?" ("+ getProvincia().getNombre()+")":"");
		}
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
		final ImpuestoItemProveedor other = (ImpuestoItemProveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}