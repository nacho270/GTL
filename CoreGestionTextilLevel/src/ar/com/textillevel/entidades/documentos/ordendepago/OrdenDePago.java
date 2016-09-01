package ar.com.textillevel.entidades.documentos.ordendepago;

import java.io.Serializable;
import java.math.BigDecimal;
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

import ar.com.textillevel.entidades.documentos.ordendepago.formapago.FormaPagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePago;
import ar.com.textillevel.entidades.enums.EEstadoOrdenDePago;
import ar.com.textillevel.entidades.gente.Proveedor;

@Entity
@Table(name = "T_ORDEN_PAGO")
public class OrdenDePago implements Serializable {

	private static final long serialVersionUID = 3958766118227847391L;

	private Integer id;
	private List<FormaPagoOrdenDePago> formasDePago;
	private Proveedor proveedor;
	private BigDecimal monto;
	private String txtCantidadPesos;
	private Timestamp fechaEmision;
	private Integer nroOrden;
	private Integer idEstadoOrden;
	private String usuarioConfirmacion;
	private List<PagoOrdenDePago> pagos;
	private Integer nroReciboProveedor;
	private String usuarioCreador;
	private Boolean entregado;
	private Timestamp fechaHoraEntregada;
	
	public OrdenDePago(){
		formasDePago = new ArrayList<FormaPagoOrdenDePago>();
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

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "F_ORDEN_PAGO_P_ID")
	public List<FormaPagoOrdenDePago> getFormasDePago() {
		return formasDePago;
	}

	public void setFormasDePago(List<FormaPagoOrdenDePago> pagos) {
		this.formasDePago = pagos;
	}
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "F_ORDEN_PAGO_P_ID")
	public List<PagoOrdenDePago> getPagos() {
		return pagos;
	}

	public void setPagos(List<PagoOrdenDePago> pagos) {
		this.pagos = pagos;
	}

	@ManyToOne
	@JoinColumn(name = "F_PROV_P_ID",nullable=false)
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
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

	@Column(name="A_NRO_ORDEN", nullable=false)
	public Integer getNroOrden() {
		return nroOrden;
	}

	public void setNroOrden(Integer nroOrden) {
		this.nroOrden = nroOrden;
	}

	@Column(name="A_ID_ESTADO_ORDEN", columnDefinition="INTEGER DEFAULT 1")
	private Integer getIdEstadoOrden() {
		return idEstadoOrden;
	}

	private void setIdEstadoOrden(Integer idEstadoOrden) {
		this.idEstadoOrden = idEstadoOrden;
	}
	
	@Transient
	public EEstadoOrdenDePago getEstadoOrden(){
		if(getIdEstadoOrden() == null){
			return null;
		}
		return EEstadoOrdenDePago.getById(getIdEstadoOrden());
	}
	
	public void setEstadoOrden(EEstadoOrdenDePago estado){
		if(estado == null){
			setIdEstadoOrden(null);
		}
		setIdEstadoOrden(estado.getId());
	}
	
	@Column(name="A_USR_CONFIRMACION")	
	public String getUsuarioConfirmacion() {
		return usuarioConfirmacion;
	}

	public void setUsuarioConfirmacion(String usuarioConfirmacion) {
		this.usuarioConfirmacion = usuarioConfirmacion;
	}
	
	@Column(name="A_NRO_RECIBO_PROV",nullable=true)
	public Integer getNroReciboProveedor() {
		return nroReciboProveedor;
	}

	public void setNroReciboProveedor(Integer nroReciboProveedor) {
		this.nroReciboProveedor = nroReciboProveedor;
	}

	@Column(name="A_USUARIO_CREADOR", nullable=false)
	public String getUsuarioCreador() {
		return usuarioCreador;
	}

	public void setUsuarioCreador(String usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}

	@Column(name = "A_ENTREGADA", nullable = true)
	public Boolean getEntregado() {
		return entregado;
	}

	public void setEntregado(Boolean entregado) {
		this.entregado = entregado;
	}
	
	@Column(name = "A_FECHA_HORA_ENTREGADA", nullable = true)
	public Timestamp getFechaHoraEntregada() {
		return fechaHoraEntregada;
	}

	public void setFechaHoraEntregada(Timestamp fechaHoraEntregada) {
		this.fechaHoraEntregada = fechaHoraEntregada;
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
		OrdenDePago other = (OrdenDePago) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
