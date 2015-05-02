package ar.com.textillevel.entidades.config;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.enums.ETipoFactura;

@Entity
@Table(name = "T_PARAM_GENERALES")
public class ParametrosGenerales implements Serializable {

	private static final long serialVersionUID = 5779214735126983871L;

	private Integer id;
	private Integer nroComienzoRemito;
	private Integer nroComienzoFactura;
	private Integer nroComienzoRecibo;
	private BigDecimal porcentajeIVAInscripto;
	private BigDecimal porcentajeIVANoInscripto;
	private BigDecimal porcentajeSeguro;
	private BigDecimal porcentajeToleranciaMermaNegativa;
	private BigDecimal precioPorTubo;
	private NumeracionCheque numeracionCheque;
	private Integer nroSucursal;
	private Integer diasAvisoVencimientoDeCheque;
	private Integer diasVenceCheque;
	private Integer nroComienzoODT;
	private Integer nroIGJ;
	private Integer nroComienzoFacturaB;
	private Integer nroComienzoOrdenDePago;
	private Integer nroComienzoOrdenDeDeposito;
	private Banco bancoDefault;
	private BigDecimal umbralDeuda;
	private Integer nroComienzoOrdenDePagoPersona;
	private ConfiguracionNumeracionFactura configuracionFacturaA;
	private ConfiguracionNumeracionFactura configuracionFacturaB;
	
	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_NRO_COMIENZO_REMITO", nullable = false)
	public Integer getNroComienzoRemito() {
		return nroComienzoRemito;
	}

	public void setNroComienzoRemito(Integer nroComienzoRemito) {
		this.nroComienzoRemito = nroComienzoRemito;
	}

	@Column(name = "A_NRO_COMIENZO_FACTURA", nullable = false)
	public Integer getNroComienzoFactura() {
		return nroComienzoFactura;
	}

	public void setNroComienzoFactura(Integer nroComienzoFactura) {
		this.nroComienzoFactura = nroComienzoFactura;
	}

	@Column(name = "A_PORC_IVA_INSC", nullable = false)
	public BigDecimal getPorcentajeIVAInscripto() {
		return porcentajeIVAInscripto;
	}

	public void setPorcentajeIVAInscripto(BigDecimal porcentajeIVAInscripto) {
		this.porcentajeIVAInscripto = porcentajeIVAInscripto;
	}

	@Column(name = "A_PORC_IVA_NO_INSC", nullable = false)
	public BigDecimal getPorcentajeIVANoInscripto() {
		return porcentajeIVANoInscripto;
	}

	public void setPorcentajeIVANoInscripto(BigDecimal porcentajeIVANoInscripto) {
		this.porcentajeIVANoInscripto = porcentajeIVANoInscripto;
	}

	@Column(name = "A_PORC_SEGURO", nullable = false)
	public BigDecimal getPorcentajeSeguro() {
		return porcentajeSeguro;
	}

	public void setPorcentajeSeguro(BigDecimal porcentajeSeguro) {
		this.porcentajeSeguro = porcentajeSeguro;
	}

	@Column(name = "A_PORC_MERMA", nullable = false)
	public BigDecimal getPorcentajeToleranciaMermaNegativa() {
		return porcentajeToleranciaMermaNegativa;
	}

	public void setPorcentajeToleranciaMermaNegativa(BigDecimal porcentajeToleranciaMermaNegativa) {
		this.porcentajeToleranciaMermaNegativa = porcentajeToleranciaMermaNegativa;
	}

	@Column(name = "A_PRECIO_TUBO", nullable = false)
	public BigDecimal getPrecioPorTubo() {
		return precioPorTubo;
	}

	public void setPrecioPorTubo(BigDecimal precioPorTubo) {
		this.precioPorTubo = precioPorTubo;
	}

	@Embedded
	public NumeracionCheque getNumeracionCheque() {
		return numeracionCheque;
	}

	public void setNumeracionCheque(NumeracionCheque numeracionCheque) {
		this.numeracionCheque = numeracionCheque;
	}

	@Column(name = "A_NRO_SUCURSAL", nullable = false)
	public Integer getNroSucursal() {
		return nroSucursal;
	}

	public void setNroSucursal(Integer nroSucursal) {
		this.nroSucursal = nroSucursal;
	}

	@Column(name = "A_NRO_COMIENZO_RECIBO", nullable = false)
	public Integer getNroComienzoRecibo() {
		return nroComienzoRecibo;
	}

	public void setNroComienzoRecibo(Integer nroComienzoRecibo) {
		this.nroComienzoRecibo = nroComienzoRecibo;
	}

	@Column(name = "A_DIAS_AVISO_VENC_CHEQ", nullable = false)
	public Integer getDiasAvisoVencimientoDeCheque() {
		return diasAvisoVencimientoDeCheque;
	}

	@Column(name = "A_DIAS_VENC_CHEQ", nullable = false)
	public Integer getDiasVenceCheque() {
		return diasVenceCheque;
	}

	public void setDiasVenceCheque(Integer diasVenceCheque) {
		this.diasVenceCheque = diasVenceCheque;
	}

	public void setDiasAvisoVencimientoDeCheque(Integer diasAvisoVencimientoDeCheque) {
		this.diasAvisoVencimientoDeCheque = diasAvisoVencimientoDeCheque;
	}

	@Column(name = "A_NRO_COM_ODT", nullable = false)
	public Integer getNroComienzoODT() {
		return nroComienzoODT;
	}

	public void setNroComienzoODT(Integer nroComienzoODT) {
		this.nroComienzoODT = nroComienzoODT;
	}

	@Column(name = "A_NRO_COMIENZO_FACTURA_B", nullable = false)
	public Integer getNroComienzoFacturaB() {
		return nroComienzoFacturaB;
	}

	public void setNroComienzoFacturaB(Integer nroComienzoFacturaB) {
		this.nroComienzoFacturaB = nroComienzoFacturaB;
	}
	
	@Column(name = "A_NRO_IGJ", nullable = false)
	public Integer getNroIGJ() {
		return nroIGJ;
	}
	
	public void setNroIGJ(Integer nroIGJ) {
		this.nroIGJ = nroIGJ;
	}
	
	@Column(name = "A_NRO_COMIENZO_ODP", nullable = false)
	public Integer getNroComienzoOrdenDePago() {
		return nroComienzoOrdenDePago;
	}
	
	public void setNroComienzoOrdenDePago(Integer nroComienzoOrdenDePago) {
		this.nroComienzoOrdenDePago = nroComienzoOrdenDePago;
	}

	@Column(name="A_NRO_COMIENZO_ODD",nullable=false)
	public Integer getNroComienzoOrdenDeDeposito() {
		return nroComienzoOrdenDeDeposito;
	}

	public void setNroComienzoOrdenDeDeposito(Integer nroComienzoOrdenDeDeposito) {
		this.nroComienzoOrdenDeDeposito = nroComienzoOrdenDeDeposito;
	}
	
	@ManyToOne
	@JoinColumn(name="F_BANCO_P_ID",nullable=false)
	public Banco getBancoDefault() {
		return bancoDefault;
	}

	public void setBancoDefault(Banco bancoDefault) {
		this.bancoDefault = bancoDefault;
	}

	@Column(name="A_UMBRAL_DEUDA", nullable=false)
	public BigDecimal getUmbralDeuda() {
		return umbralDeuda;
	}

	public void setUmbralDeuda(BigDecimal umbralDeuda) {
		this.umbralDeuda = umbralDeuda;
	}
	
	@Column(name="A_NRO_COMIENZO_ODP_PERS",nullable=false)
	public Integer getNroComienzoOrdenDePagoPersona() {
		return nroComienzoOrdenDePagoPersona;
	}
	
	public void setNroComienzoOrdenDePagoPersona(Integer nroComienzoOrdenDePagoPersona) {
		this.nroComienzoOrdenDePagoPersona = nroComienzoOrdenDePagoPersona;
	}
	
	@ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_NUMERACION_FACT_A_P_ID",nullable=true)
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public ConfiguracionNumeracionFactura getConfiguracionFacturaA() {
		return configuracionFacturaA;
	}

	public void setConfiguracionFacturaA(ConfiguracionNumeracionFactura configuracionFacturaA) {
		this.configuracionFacturaA = configuracionFacturaA;
	}

	@ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="F_NUMERACION_FACT_B_P_ID",nullable=true)
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public ConfiguracionNumeracionFactura getConfiguracionFacturaB() {
		return configuracionFacturaB;
	}
	
	public void setConfiguracionFacturaB(ConfiguracionNumeracionFactura configuracionFacturaB) {
		this.configuracionFacturaB = configuracionFacturaB;
	}
	
	@Transient
	public ConfiguracionNumeracionFactura getConfiguracionFacturaByTipoFactura(ETipoFactura tipo){
		switch(tipo){
			case A:{
				return configuracionFacturaA;
			}
			case B:{
				return configuracionFacturaB;
			}
			default:{
				throw new RuntimeException("No hay configuracion de numeracion para facturas de tipo " + tipo.getDescripcion());
			}
		}
	}
	
	@Transient
	public Boolean hayAlgunParametroVacio(boolean isB) {
		return nroComienzoRemito == null || nroComienzoFactura == null || nroComienzoRecibo == null || porcentajeIVAInscripto == null || porcentajeIVANoInscripto == null || porcentajeSeguro == null
				|| porcentajeToleranciaMermaNegativa == null || precioPorTubo == null || numeracionCheque == null || nroSucursal == null || diasAvisoVencimientoDeCheque == null
				|| diasVenceCheque == null || nroComienzoODT == null || nroComienzoFacturaB == null || nroIGJ == null || nroComienzoOrdenDePago == null || nroComienzoOrdenDeDeposito == null
				|| bancoDefault == null || umbralDeuda == null || nroComienzoOrdenDePagoPersona == null || (!isB && configuracionFacturaA == null) || (isB && configuracionFacturaB == null);
	}
}