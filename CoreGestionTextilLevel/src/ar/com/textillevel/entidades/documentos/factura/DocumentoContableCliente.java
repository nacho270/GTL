package ar.com.textillevel.entidades.documentos.factura;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.EEstadoImpresionDocumento;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Cliente;

@Entity
@Table(name = "T_FACTURA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class DocumentoContableCliente implements Serializable {

	private static final long serialVersionUID = -5909799153834276469L;

	public static final int LONG_OBS_AFIP = 712;

	private Integer id;
	private BigDecimal montoTotal;
	private Integer nroFactura;
	private Timestamp fechaEmision;
	private BigDecimal montoSubtotal;
	private Integer idEstado;
	private Integer idEstadoImpresion;
	private Cliente cliente;
	private Integer idTipoFactura;
	private BigDecimal porcentajeIVAInscripto;
	private String caeAFIP;
	private String observacionesAFIP;
	private Integer nroSucursal;
	private String fechaVencimientoCaeAFIP;

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_NRO_FACTURA", nullable = true)
	public Integer getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(Integer nroFactura) {
		this.nroFactura = nroFactura;
	}

	@Column(name = "A_FECHA_EMISION", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Timestamp getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Timestamp fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	@Column(name = "A_MONTO_TOTAL", nullable = false)
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	@Column(name = "A_MONTO_SUBTOTAL", nullable = false)
	public BigDecimal getMontoSubtotal() {
		return montoSubtotal;
	}

	public void setMontoSubtotal(BigDecimal montoSubtotal) {
		this.montoSubtotal = montoSubtotal;
	}
	
	@Column(name = "A_ID_ESTADO_FACTURA", nullable = true)
	public Integer getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public void setEstadoFactura(EEstadoFactura estado) {
		if (estado == null) {
			this.setIdEstado(null);
		}
		setIdEstado(estado.getId());
	}

	@Transient
	public EEstadoFactura getEstadoFactura() {
		if (getIdEstado() == null) {
			return null;
		}
		return EEstadoFactura.getById(getIdEstado());
	}

	@Column(name="A_ESTADO_IMPRESION", nullable=false)
	protected Integer getIdEstadoImpresion() {
		return idEstadoImpresion;
	}
	
	protected void setIdEstadoImpresion(Integer idEstadoImpresion) {
		this.idEstadoImpresion = idEstadoImpresion;
	}
	
	@Transient
	public EEstadoImpresionDocumento getEstadoImpresion(){
		return EEstadoImpresionDocumento.getById(getIdEstadoImpresion());
	}
	
	public void setEstadoImpresion(EEstadoImpresionDocumento estadoImpresion){
		setIdEstadoImpresion(estadoImpresion.getId());
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_CLIENTE_P_ID", nullable = false)
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Column(name = "A_ID_TIPO_FACTURA", nullable = false)
	public Integer getIdTipoFactura() {
		return idTipoFactura;
	}

	public void setIdTipoFactura(Integer idTipoFactura) {
		this.idTipoFactura = idTipoFactura;
	}

	public void setTipoFactura(ETipoFactura tipoFactura) {
		if (tipoFactura == null) {
			this.setIdTipoFactura(null);
		}
		setIdTipoFactura(tipoFactura.getId());
	}

	@Transient
	public ETipoFactura getTipoFactura() {
		if (getIdTipoFactura() == null) {
			return null;
		}
		return ETipoFactura.getById(getIdTipoFactura());
	}

	@Column(name = "A_PRCT_IVA_INSCR", nullable = true)
	public BigDecimal getPorcentajeIVAInscripto() {
		return porcentajeIVAInscripto;
	}

	public void setPorcentajeIVAInscripto(BigDecimal porcentajeIVAInscripto) {
		this.porcentajeIVAInscripto = porcentajeIVAInscripto;
	}

	@Column(name = "A_CAE_AFIP", nullable = true, length=50)
	public String getCaeAFIP() {
		return caeAFIP;
	}

	public void setCaeAFIP(String caeAFIP) {
		this.caeAFIP = caeAFIP;
	}

	@Column(name = "A_FECHA_VTO_CAE_AFIP", nullable = true, length=8)
	public String getFechaVencimientoCaeAFIP() {
		return fechaVencimientoCaeAFIP;
	}

	public void setFechaVencimientoCaeAFIP(String fechaVencimientoCaeAFIP) {
		this.fechaVencimientoCaeAFIP = fechaVencimientoCaeAFIP;
	}
	
	@Column(name = "A_OBS_AFIP", nullable = true, length=LONG_OBS_AFIP)
	public String getObservacionesAFIP() {
		return observacionesAFIP;
	}

	public void setObservacionesAFIP(String observacionesAFIP) {
		this.observacionesAFIP = observacionesAFIP;
	}
	
	@Column(name = "A_NRO_SUCURSAL", nullable = false)
	public Integer getNroSucursal() {
		return nroSucursal;
	}

	public void setNroSucursal(Integer nroSucursal) {
		this.nroSucursal = nroSucursal;
	}
	
	@Transient
	public abstract ETipoDocumento getTipoDocumento();

	@Transient
	public abstract List<DocumentoContableCliente> getDocsContableRelacionados();

	@Transient
	public abstract double getTotalIVA();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id== null ? 0 : id.hashCode());
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
		final DocumentoContableCliente other = (DocumentoContableCliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public String convertirFechaVencimientoAFIP() {
		String fechaVTO = getFechaVencimientoCaeAFIP();
		if(StringUtil.isNullOrEmpty(fechaVTO)) {
			return "";
		}
		return fechaVTO.substring(6, 8) + "/" + fechaVTO.substring(4, 6) + "/" + fechaVTO.substring(0, 4);
	}

	public String crearCodigoDeBarrasAFIP(String cuitEmpresa) {
		StringBuilder sb = new StringBuilder();
		sb.append(cuitEmpresa)
			.append(StringUtil.fillLeftWithZeros(""+getTipoDocumento().getId(),2))
			.append(StringUtil.fillLeftWithZeros(""+getNroSucursal(),4))
			.append(getCaeAFIP())
			.append(getFechaVencimientoCaeAFIP());
		sb.append(String.valueOf(calcularDigitoVerificador(sb.toString())));
		return sb.toString();
	}

	private int calcularDigitoVerificador(String codigoParcial) {
		int sumaPares = 0;
		int sumaImpares = 0;
		for(int i = 0; i<codigoParcial.length();i++) {
			if(i%2!=0){ //ESTO ESTA BIEN, LA AFIP HABLA DE POSICIONES PARES EMPEZANDO DE 1
				sumaPares += Integer.valueOf(String.valueOf(codigoParcial.charAt(i)));
			}else{
				sumaImpares += Integer.valueOf(String.valueOf(codigoParcial.charAt(i)));
			}
		}
		sumaImpares *= 3;
		int total = sumaImpares + sumaPares;
		int enteroMasCercano = total + (10 - total%10);
		return enteroMasCercano - total;
	}
}