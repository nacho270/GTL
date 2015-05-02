package ar.com.textillevel.entidades.gente;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.enums.EPosicionIVA;

@Entity
@Table(name="T_CLIENTE")
public class Cliente implements Serializable, IAgendable {

	private static final long serialVersionUID = -8500484189228300886L;
	
	private Integer id;
	private Integer nroCliente;
	private String razonSocial;
	private InfoDireccion direccionFiscal;
	private InfoDireccion direccionReal;
	private String telefono;
	private String celular;
	private String cuit;
	private String localidad;
	private String contacto;
	private String email;
	private String skype;
	private String observaciones;
	private String fax;
	private Integer idPosicionIva;
	private CondicionDeVenta condicionVenta;

	public Cliente() {
		this.nroCliente = null;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_NROCLIENTE", nullable=true)
	public Integer getNroCliente() {
		return nroCliente;
	}

	public void setNroCliente(Integer nroCliente) {
		this.nroCliente = nroCliente;
	}

	@Column(name="A_RAZON_SOCIAL", nullable=false, length=50)
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	@Column(name="A_TELEFONO", nullable=true, length=20)
	public String getTelefono() {
		return telefono;
	}

	@Column(name="A_CELULAR",nullable=true, length=20)
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@Column(name="A_CUIT", nullable=true, length=50)
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	@Column(name="A_LOCALIDAD", nullable=true, length=100)
	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	@Column(name="A_CONTACTO", nullable=true, length=50)
	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	@Column(name="A_OBSERVACIONES", nullable=true, length=1000)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="F_INFO_DIR_FISC_P_ID")
	public InfoDireccion getDireccionFiscal() {
		return direccionFiscal;
	}

	public void setDireccionFiscal(InfoDireccion direccionFiscal) {
		this.direccionFiscal = direccionFiscal;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="F_INFO_DIR_REAL_P_ID")
	public InfoDireccion getDireccionReal() {
		return direccionReal;
	}

	public void setDireccionReal(InfoDireccion direccionReal) {
		this.direccionReal = direccionReal;
	}

	@Column(name="A_EMAIL", nullable=true, length=100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="A_SKYPE", nullable=true, length=50)
	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	@Column(name="A_FAX")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@ManyToOne
	@JoinColumn(name="F_COND_VENTA_P_ID", nullable=false)
	public CondicionDeVenta getCondicionVenta() {
		return condicionVenta;
	}
	
	public void setCondicionVenta(CondicionDeVenta condicionVenta) {
		this.condicionVenta = condicionVenta;
	}

	@Column(name="A_ID_POSICION_IVA")
	private Integer getIdPosicionIva() {
		return idPosicionIva;
	}

	private void setIdPosicionIva(Integer idPosicionIva) {
		this.idPosicionIva = idPosicionIva;
	}

	@Transient
	public EPosicionIVA getPosicionIva() {
		if(getIdPosicionIva() == null) {
			return null;
		}
		return EPosicionIVA.getById(getIdPosicionIva());
	}

	@Transient
	public void setPosicionIva(EPosicionIVA posicionIVA) {
		if(posicionIVA == null) {
			return;
		}
		setIdPosicionIva(posicionIVA.getId());
	}

	@Override
	@Transient
	public String toString() {
		return getRazonSocial();
	}

	@Transient
	public String getDireccion() {
		if(getDireccionReal() != null) {
			return getDireccionReal().getDireccion();
		}
		return null;
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
		final Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public Integer getCodigoPostal() {
		if(getDireccionReal() != null && getDireccionReal().getLocalidad() != null) {
			return getDireccionReal().getLocalidad().getCodigoPostal();
		}
		return null;
	}

	@Transient
	public String getDescripcionResumida() {
		String iniciales = "";
		String[] split = getRazonSocial().split(" ");
		for(String s : split){
			iniciales += s.toUpperCase().charAt(0);
		}
		return getNroCliente() + " " + iniciales;
	}

	@Transient
	public String getIdentificadorInterno() {
		return String.valueOf(getNroCliente());
	}

	@Transient
	public String getCondicionDeVenta() {
		CondicionDeVenta cv = getCondicionVenta();
		return cv==null?"":cv.getNombre();
	}

	@Transient
	public String getNroIngresosBrutos() {
		return "";
	}
}