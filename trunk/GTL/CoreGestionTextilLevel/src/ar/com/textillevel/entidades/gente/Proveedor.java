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
@Table(name="T_PROVEEDORES")
public class Proveedor implements Serializable, IAgendable {
	
	private static final long serialVersionUID = -5632214176913913474L;
	
	private Integer id;
	private String nombreCorto;
	private String razonSocial;
	private String cuit;
	private InfoDireccion direccionFiscal;
	private InfoDireccion direccionReal;
	private String telefono;
	private String celular;
	private String observaciones;
	private String contacto;
	private String email;
	private String skype;
	private String fax;
	private Rubro rubro;
	private String sitioWeb;
	private String nroIngresosBrutos;
	private Integer idPosicionIva;
	private CondicionDeVenta condicionVenta;
	private Provincia provincia;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="P_ID")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="A_NOMBRE_CORTO")
	public String getNombreCorto() {
		return nombreCorto;
	}
	
	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}
	
	@Column(name="A_RAZON_SOCIAL", nullable=false)
	public String getRazonSocial() {
		return razonSocial;
	}
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	@Column(name="A_CUIT", nullable=false)
	public String getCuit() {
		return cuit;
	}
	
	public void setCuit(String cuit) {
		this.cuit = cuit;
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
	
	@Column(name="A_TELEFONO")
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	@Column(name="A_CELULAR")
	public String getCelular() {
		return celular;
	}
	
	public void setCelular(String celular) {
		this.celular = celular;
	}
	
	@Column(name="A_OBSERVACIONES",length=1000)
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	@Override
	@Transient
	public String toString() {
		return razonSocial;
	}

	@Column(name="A_CONTACTO")
	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
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
	@JoinColumn(name="F_RUBRO_P_ID", nullable=true)
	public Rubro getRubro() {
		return rubro;
	}

	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}
	
	@Column(name="A_SITIO_WEB")
	public String getSitioWeb() {
		return sitioWeb;
	}
	
	public void setSitioWeb(String sitioWeb) {
		this.sitioWeb = sitioWeb;
	}

	@Column(name="A_NRO_ING_BRUTOS")
	public String getNroIngresosBrutos() {
		return nroIngresosBrutos;
	}

	public void setNroIngresosBrutos(String nroIngresosBrutos) {
		this.nroIngresosBrutos = nroIngresosBrutos;
	}
	
	@Column(name="A_ID_POSICION_IVA")
	private Integer getIdPosicionIva() {
		return idPosicionIva;
	}

	private void setIdPosicionIva(Integer idPosicionIva) {
		this.idPosicionIva = idPosicionIva;
	}

	@ManyToOne
	@JoinColumn(name="F_COND_VENTA_P_ID", nullable=false)
	public CondicionDeVenta getCondicionVenta() {
		return condicionVenta;
	}

	public void setCondicionVenta(CondicionDeVenta condicionVenta) {
		this.condicionVenta = condicionVenta;
	}

	@ManyToOne
	@JoinColumn(name="F_PROVINCIA_P_ID",nullable=false)
	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
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
	
	@Transient
	public String getDireccion() {
		return this.getDireccionReal()!=null? this.getDireccionReal().getDireccion():"";
	}
	
	@Transient
	public Integer getCodigoPostal() {
		return this.getDireccionReal()!=null && this.getDireccionReal().getLocalidad() != null?this.getDireccionReal().getLocalidad().getCodigoArea():null;
	}
	
	@Transient
	public String getLocalidad() {
		return  this.getDireccionReal()!=null && this.getDireccionReal().getLocalidad()!= null?this.getDireccionReal().getLocalidad().getNombreLocalidad():"";
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
		final Proveedor other = (Proveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public String getIdentificadorInterno() {
		return getNombreCorto();
	}

	@Transient
	public String getCondicionDeVenta() {
		CondicionDeVenta cv = getCondicionVenta();
		return cv==null?"":cv.getNombre();
	}
}
