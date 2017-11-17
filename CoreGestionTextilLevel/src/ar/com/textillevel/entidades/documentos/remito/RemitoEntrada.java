package ar.com.textillevel.entidades.documentos.remito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.factura.CondicionDeVenta;
import ar.com.textillevel.entidades.documentos.remito.enums.ELugarTarima;
import ar.com.textillevel.entidades.documentos.remito.enums.ESituacionODTRE;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Entity
@DiscriminatorValue(value = "ENT")
public class RemitoEntrada extends Remito {

	private static final long serialVersionUID = 6207878696402570129L;

	private CondicionDeVenta condicionDeVenta;
	private List<ProductoArticulo> productoArticuloList;
	private Articulo articuloStock; //los que tienen seteado un articulo y proveedor=PMP=null significan que son remitos de entrada 01
	private Proveedor proveedor; //los que tienen proveedor significan que fueron una compra de tela a ese proveedor
	private PrecioMateriaPrima precioMatPrima; //los que tienen PMP significan que fueron una compra de tela a ese proveedor
	private BigDecimal anchoCrudo;
	private BigDecimal anchoFinal;
	private Tarima tarima;
	private Integer idLugarTarima;
	private Boolean enPalet;
	private String control;
	private String observacionesODT;
	private String articuloCliente;
	private Integer idSituacionODT;

	public RemitoEntrada() {
		this.productoArticuloList = new ArrayList<ProductoArticulo>();
	}

	@ManyToOne
	@JoinColumn(name = "F_COND_VENTA_P_ID")
	public CondicionDeVenta getCondicionDeVenta() {
		return condicionDeVenta;
	}

	public void setCondicionDeVenta(CondicionDeVenta condicionDeVenta) {
		this.condicionDeVenta = condicionDeVenta;
	}

	@ManyToMany
	@JoinTable(name = "T_REMITO_ENTRADA_PRODUCTO_ART_ASOC", 
			joinColumns = { @JoinColumn(name = "F_REMITO_ENTRADA_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_PRODUCTO_ARTICULO_P_ID") })
	public List<ProductoArticulo> getProductoArticuloList() {
		return productoArticuloList;
	}

	public void setProductoArticuloList(List<ProductoArticulo> productoArticuloList) {
		this.productoArticuloList = productoArticuloList;
	}

	@ManyToOne
	@JoinColumn(name = "F_ARTICULO_STOCK_P_ID", nullable=true)
	public Articulo getArticuloStock() {
		return articuloStock;
	}

	public void setArticuloStock(Articulo articuloStock) {
		this.articuloStock = articuloStock;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "F_PROVEEDOR_P_ID", nullable=true)
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	@ManyToOne
	@JoinColumn(name = "F_PMP_P_ID", nullable=true)
	public PrecioMateriaPrima getPrecioMatPrima() {
		return precioMatPrima;
	}

	public void setPrecioMatPrima(PrecioMateriaPrima precioMatPrima) {
		this.precioMatPrima = precioMatPrima;
	}

	@ManyToOne
	@JoinColumn(name = "F_TARIMA_P_ID", nullable=true)
	public Tarima getTarima() {
		return tarima;
	}

	public void setTarima(Tarima tarima) {
		this.tarima = tarima;
	}

	@Column(name="A_ANCHO_CRUDO")
	public BigDecimal getAnchoCrudo() {
		return anchoCrudo;
	}

	public void setAnchoCrudo(BigDecimal anchoCrudo) {
		this.anchoCrudo = anchoCrudo;
	}

	@Column(name="A_ANCHO_FINAL")
	public BigDecimal getAnchoFinal() {
		return anchoFinal;
	}

	public void setAnchoFinal(BigDecimal anchoFinal) {
		this.anchoFinal = anchoFinal;
	}

	@Column(name="A_EN_PALET")
	public Boolean getEnPalet() {
		return enPalet;
	}

	public void setEnPalet(Boolean enPalet) {
		this.enPalet = enPalet;
	}

	@Column(name="A_CONTROL")
	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}
	
	@Column(name="A_OBSERVACIONES_ODT")
	public String getObservacionesODT() {
		return observacionesODT;
	}

	public void setObservacionesODT(String observacionesODT) {
		this.observacionesODT = observacionesODT;
	}

	@Column(name="A_ARTICULO_CLIENTE")
	public String getArticuloCliente() {
		return articuloCliente;
	}

	public void setArticuloCliente(String articuloCliente) {
		this.articuloCliente = articuloCliente;
	}
	
	@Transient
	public void recalcularOrdenes() {
		int ordenPieza = 1;
		for (PiezaRemito pe : getPiezas()) {
			pe.setOrdenPieza(ordenPieza);
			ordenPieza++;
		}
	}

	@Transient
	public BigDecimal getTotalMetros() {
		BigDecimal totalMetros = new BigDecimal(0);
		for (PiezaRemito pieza : getPiezas()) {
			totalMetros = totalMetros.add(pieza.getMetros());
		}
		return totalMetros;
	}

	@Transient
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Nro.: " + getNroRemito()).append(" - Fecha: " + DateUtil.dateToString(getFechaEmision())).append(" - Productos : " + StringUtil.getCadena(getProductoArticuloList(), ", "));
		return sb.toString();
	}

	@Transient
	public PiezaRemito getPiezaByOrden(Integer ordenPieza) {
		for(PiezaRemito pr : getPiezas()) {
			if(pr.getOrdenPieza().equals(ordenPieza)) {
				return pr;
			}
		}
		return null;
	}

	@Transient
	public float getGramaje() {
		return getPesoTotal().floatValue() / getTotalMetros().floatValue();		
	}

	@Transient
	public boolean isRemito01() {
		return getArticuloStock() != null && getProveedor() == null && getPrecioMatPrima() == null;
	}

	@Transient
	public ESituacionODTRE getSituacionODT() {
		if(getIdSituacionODT() == null) {
			return ESituacionODTRE.CON_ODT;
		}
		return ESituacionODTRE.getById(getIdSituacionODT());
	}

	@Column(name = "A_ID_SITUACION_ODT", nullable=true)
	private Integer getIdSituacionODT() {
		return idSituacionODT;
	}

	private void setIdSituacionODT(Integer idSituacionODT) {
		this.idSituacionODT = idSituacionODT;
	}

	public void setSituacion(ESituacionODTRE situacion) {
		if (situacion == null) {
			this.setIdSituacionODT(null);
		}
		setIdSituacionODT(situacion.getId());
	}

	@Column(name = "A_ID_LUGAR_TARIMA", nullable=true)
	private Integer getIdLugarTarima() {
		return idLugarTarima;
	}

	private void setIdLugarTarima(Integer idLugarTarima) {
		this.idLugarTarima = idLugarTarima;
	}

	@Transient
	public ELugarTarima getLugarTarima() {
		if(getIdLugarTarima() == null) {
			return null;
		}
		return ELugarTarima.getById(getIdLugarTarima());
	}

	public void setLugarTarima(ELugarTarima lugarTarima) {
		if (lugarTarima == null) {
			setIdLugarTarima(null);
		}
		setIdLugarTarima(lugarTarima.getId());
	}

}