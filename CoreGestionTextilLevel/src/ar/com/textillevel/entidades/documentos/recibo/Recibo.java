package ar.com.textillevel.entidades.documentos.recibo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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

import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPago;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoRecibo;
import ar.com.textillevel.entidades.enums.EEstadoRecibo;
import ar.com.textillevel.entidades.gente.Cliente;

@Entity
@Table(name = "T_RECIBO")
public class Recibo implements Serializable {

	private static final long serialVersionUID = -7296566151618293249L;

	private Integer id;
	private Cliente cliente;
	private List<PagoRecibo> pagoReciboList;
	private List<FormaPago> pagos;
	private BigDecimal monto;
	private String txtCantidadPesos;
	private Timestamp fechaEmision;
	private Integer nroRecibo;
	private Integer idEstadoRecibo;
	private Date fecha;
	private String observaciones;
	private String usuarioConfirmacion;		//EL USUARIO QUE ACEPTA UN DOCUMENTO, LO PONGO ACA PARA QUE SEA FACIL TRAERLO EN LA TABLA DE MOVIMIENTOS
	
	public Recibo() {
		this.pagoReciboList = new ArrayList<PagoRecibo>();
		this.pagos = new ArrayList<FormaPago>();
	}
	
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
	@JoinColumn(name = "F_CLIENTE_P_ID",nullable=false)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="F_RECIBO_P_ID")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<PagoRecibo> getPagoReciboList() {
		return pagoReciboList;
	}

	public void setPagoReciboList(List<PagoRecibo> pagoReciboList) {
		this.pagoReciboList = pagoReciboList;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "F_RECIBO_P_ID")
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<FormaPago> getPagos() {
		return pagos;
	}

	public void setPagos(List<FormaPago> pagos) {
		this.pagos = pagos;
	}

	@Column(name = "A_MONTO", nullable = false)
	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "A_TXT_CANT_PESOS", nullable = false)
	public String getTxtCantidadPesos() {
		return txtCantidadPesos;
	}

	public void setTxtCantidadPesos(String txtCantidadPesos) {
		this.txtCantidadPesos = txtCantidadPesos;
	}
	
	@Column(name = "A_FECHA_EMISION", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Timestamp getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Timestamp fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	@Column(name = "A_OBSERVACIONES", nullable = true)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	@Column(name="A_NRO_RECIBO", nullable=false)
	public Integer getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(Integer nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	@Column(name="A_ID_ESTADO_RECIBO", columnDefinition="INTEGER DEFAULT 1")
	private Integer getIdEstadoRecibo() {
		return idEstadoRecibo;
	}

	private void setIdEstadoRecibo(Integer idEstadoRecibo) {
		this.idEstadoRecibo = idEstadoRecibo;
	}

	@Column(name = "A_FECHA", nullable=false)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Transient
	public EEstadoRecibo getEstadoRecibo(){
		if(getIdEstadoRecibo() == null){
			return null;
		}
		return EEstadoRecibo.getById(getIdEstadoRecibo());
	}
	
	public void setEstadoRecibo(EEstadoRecibo estado){
		if(estado == null){
			setIdEstadoRecibo(null);
		}
		setIdEstadoRecibo(estado.getId());
	}
	
	@Column(name="A_USR_CONFIRMACION")	
	public String getUsuarioConfirmacion() {
		return usuarioConfirmacion;
	}
	
	public void setUsuarioConfirmacion(String usuarioConfirmacion) {
		this.usuarioConfirmacion = usuarioConfirmacion;
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
		Recibo other = (Recibo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}