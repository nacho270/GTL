package ar.com.textillevel.entidades.ventas.materiaprima;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.textillevel.entidades.enums.ETipoMoneda;

@Entity
@Table(name = "T_PRECIO_MATERIA_PRIMA")
public class PrecioMateriaPrima implements Serializable {

	private static final long serialVersionUID = 4509546919629264210L;

	private Integer id;
	private MateriaPrima materiaPrima;
	private BigDecimal precio;
	private Timestamp fechaUltModif;
	private String alias;
	private Integer idTipoMoneda;
	private String siglas;
	private boolean actualizarHorario;
	private ListaDePreciosProveedor preciosProveedor;
	private BigDecimal stockActual;
	private BigDecimal stockInicial;
	private BigDecimal stockInicialDisponible;
	private BigDecimal puntoDePedido;
	private Integer version;
	
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
	@JoinColumn(name = "F_MATERIA_PRIMA_P_ID")
	@Fetch(FetchMode.JOIN)
	public MateriaPrima getMateriaPrima() {
		return materiaPrima;
	}

	public void setMateriaPrima(MateriaPrima materiaPrima) {
		this.materiaPrima = materiaPrima;
	}

	@Column(name = "A_PRECIO", nullable = false)
	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	@Column(name = "A_FECHA_ULT_MODIF", nullable = false)
	public Timestamp getFechaUltModif() {
		return fechaUltModif;
	}

	public void setFechaUltModif(Timestamp fechaUltModif) {
		this.fechaUltModif = fechaUltModif;
	}

	@Column(name = "A_ALIAS", nullable = true)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "A_TIPO_MONEDA_ID", nullable = true)
	private Integer getIdTipoMoneda() {
		return idTipoMoneda;
	}

	private void setIdTipoMoneda(Integer idTipoMoneda) {
		this.idTipoMoneda = idTipoMoneda;
	}

	@Column(name = "A_SIGLAS", nullable = true)
	public String getSiglas() {
		return siglas;
	}

	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}

	@Transient
	public ETipoMoneda getTipoMoneda() {
		if (getIdTipoMoneda() == null) {
			return ETipoMoneda.PESOS;
		} else {
			return ETipoMoneda.getById(getIdTipoMoneda());
		}
	}

	public void setTipoMoneda(ETipoMoneda tipoMoneda) {
		setIdTipoMoneda(tipoMoneda.getId());
	}

	@ManyToOne
	@JoinColumn(name="F_LISTA_DE_PRECIOS_P_ID", insertable=false, updatable=false)
	public ListaDePreciosProveedor getPreciosProveedor() {
		return preciosProveedor;
	}

	public void setPreciosProveedor(ListaDePreciosProveedor preciosProveedor) {
		this.preciosProveedor = preciosProveedor;
	}
	
	@Column(name="A_STOCK_ACTUAL", nullable=false)
	public BigDecimal getStockActual() {
		return stockActual;
	}

	public void setStockActual(BigDecimal stockActual) {
		this.stockActual = stockActual;
	}

	@Column(name="A_PUNTO_PEDIDO", nullable=false)
	public BigDecimal getPuntoDePedido() {
		return puntoDePedido;
	}

	public void setPuntoDePedido(BigDecimal puntoDePedido) {
		this.puntoDePedido = puntoDePedido;
	}

	@Version
	@Column(name="A_VERSION")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name="A_STOCK_INICIAL", nullable=false)
	public BigDecimal getStockInicial() {
		return stockInicial;
	}

	public void setStockInicial(BigDecimal stockInicial) {
		this.stockInicial = stockInicial;
	}

	@Column(name="A_STOCK_INICIAL_DISP", nullable=true)
	public BigDecimal getStockInicialDisponible() {
		return stockInicialDisponible;
	}

	public void setStockInicialDisponible(BigDecimal stockInicialDisponible) {
		this.stockInicialDisponible = stockInicialDisponible;
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
		final PrecioMateriaPrima other = (PrecioMateriaPrima) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		} else {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return getMateriaPrima().getDescripcion() + " - " + getAlias();
	}

	@Transient
	public boolean isActualizarHorario() {
		return actualizarHorario;
	}

	public void setActualizarHorario(boolean actualizarHorario) {
		this.actualizarHorario = actualizarHorario;
	}

}