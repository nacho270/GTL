package ar.com.textillevel.entidades.documentos.remito;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemRemitoSalidaProveedor;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

@Entity
@DiscriminatorValue(value="SAL")
public class RemitoSalida extends Remito implements Serializable {

	private static final long serialVersionUID = 1863941363490686977L;

	private BigDecimal porcentajeMerma;
	private Integer nroFactura;
	private Integer nroOrden;
	private List<OrdenDeTrabajo> odts;
	private List<ItemRemitoSalidaProveedor> items;
	private List<CorreccionFacturaProveedor> correccionesProvGeneradas; //Correcciones de proveedor generadas automaticamente al ingresar un remito de salida 
	private Proveedor proveedor;
	private Integer idTipoRemitoSalida;
	private Boolean anulado;
	private Factura factura;
	private Integer nroSucursal;
	
	public RemitoSalida() {
		this.odts = new ArrayList<OrdenDeTrabajo>();
		this.items = new ArrayList<ItemRemitoSalidaProveedor>();
		this.correccionesProvGeneradas = new ArrayList<CorreccionFacturaProveedor>();
		setPesoTotal(new BigDecimal(0));
	}

	@Column(name="A_PORCE_MERMA")
	public BigDecimal getPorcentajeMerma() {
		return porcentajeMerma;
	}
	
	public void setPorcentajeMerma(BigDecimal porcentajeMerma) {
		this.porcentajeMerma = porcentajeMerma;
	}

	@Column(name="A_NRO_FACTURA", nullable=true)
	public Integer getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(Integer nroFactura) {
		this.nroFactura = nroFactura;
	}

	@ManyToMany
	@JoinTable(name = "T_REMITO_SALIDA_ODT", 
			joinColumns = { @JoinColumn(name = "F_REMITO_SALIDA_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_ODT_P_ID") })
	public List<OrdenDeTrabajo> getOdts() {
		return odts;
	}

	public void setOdts(List<OrdenDeTrabajo> odts) {
		this.odts = odts;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_REMITO_SAL_PROV_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ItemRemitoSalidaProveedor> getItems() {
		return items;
	}

	public void setItems(List<ItemRemitoSalidaProveedor> items) {
		this.items = items;
	}

	@ManyToMany
	@JoinTable(name = "T_REM_SAL_CORREC_FACT_PROV_ASOC", 
			joinColumns = { @JoinColumn(name = "F_REMITO_SALIDA_P_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "F_CORRECC_P_ID") })
	public List<CorreccionFacturaProveedor> getCorreccionesProvGeneradas() {
		return correccionesProvGeneradas;
	}

	public void setCorreccionesProvGeneradas(List<CorreccionFacturaProveedor> correccionesProvGeneradas) {
		this.correccionesProvGeneradas = correccionesProvGeneradas;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_PROV_P_ID", nullable=true)
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	@Column(name = "A_ID_TIPO_REM_SALIDA", nullable = true)
	public Integer getIdTipoRemitoSalida() {
		return idTipoRemitoSalida;
	}

	public void setIdTipoRemitoSalida(Integer idTipoRemitoSalida) {
		this.idTipoRemitoSalida = idTipoRemitoSalida;
	}

	public void setTipoRemitoSalida(ETipoRemitoSalida tipoRemitoSalida) {
		if (tipoRemitoSalida == null) {
			this.setIdTipoRemitoSalida(null);
		}
		setIdTipoRemitoSalida(tipoRemitoSalida.getId());
	}

	@Transient
	public ETipoRemitoSalida getTipoRemitoSalida() {
		if (getIdTipoRemitoSalida() == null) {
			return null;
		}
		return ETipoRemitoSalida.getById(getIdTipoRemitoSalida());
	}

	@Column(name = "A_ANULADO", nullable = true)
	public Boolean getAnulado() {
		return anulado;
	}

	public void setAnulado(Boolean anulado) {
		this.anulado = anulado;
	}

	@Column(name = "A_NROORDEN", nullable = true)
	public Integer getNroOrden() {
		return nroOrden;
	}

	public void setNroOrden(Integer nroOrden) {
		this.nroOrden = nroOrden;
	}

	@Transient
	public List<ProductoArticulo> getProductoList() {
		Set<ProductoArticulo> productoSet = new HashSet<ProductoArticulo>();
		for(OrdenDeTrabajo odt : getOdts()) {
			productoSet.add(odt.getProductoArticulo());
		}
		return new ArrayList<ProductoArticulo>(productoSet);
	}

	@Override
	@Transient
	public void recalcularOrdenes() {
		Collections.sort(getPiezas(), new Comparator<PiezaRemito> () {
			public int compare(PiezaRemito o1, PiezaRemito o2) {
				return o1.getOrdenPieza().compareTo(o2.getOrdenPieza());
			}
		});
	}

	@Transient
	public Integer getCantidadPiezas() {
		return getPiezas().size();
	}

	@Override
	@Transient
	public BigDecimal getTotalMetros() {
		BigDecimal totalMetros = new BigDecimal(0);
		for(PiezaRemito pieza : getPiezas()) {
			totalMetros = totalMetros.add(pieza.getMetros());
		}
		return totalMetros;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_FACTURA_P_ID",insertable=false, updatable=false)
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	@Column(name = "A_NRO_SUCURSAL", nullable = true)
	public Integer getNroSucursal() {
		return nroSucursal;
	}

	public void setNroSucursal(Integer nroSucursal) {
		this.nroSucursal = nroSucursal;
	}

	@Override
	public String toString(){
		return "Remito Salida Nº " + getNroRemito() +". Fecha: " + DateUtil.dateToString(getFechaEmision());
	}

}