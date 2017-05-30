package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;

public class RemitoEntradaTO implements Serializable {

	private static final long serialVersionUID = 5600580554949238695L;

	// De Remito
	private Integer id;
	private Long dateFechaEmision;
	private Integer idCliente; // Cliente esta federado
	private List<PiezaRemitoTO> piezas;
	private BigDecimal pesoTotal;
	private Integer nroRemito;

	// De RemitoEntrada
	private Integer idCondicionDeVenta; // CondicionDeVenta esta federada
	private List<Integer> productoArticuloIdsList; // ProductoArticulo esta
													// federado
	private Integer idArticuloStock; // Articulo esta federado
	private Integer idProveedor; // Proveedor esta federado
	private Integer idPrecioMatPrima; // PrecioMateriaPrima esta federado
	private BigDecimal anchoCrudo;
	private BigDecimal anchoFinal;
	private Integer idTarima; // Tarima esta federada
	private Boolean enPalet;
	private String control;
	private String observacionesODT;
	private String articuloCliente;
	private Integer idSituacionODT;
	

	// Replica de ODTTO
	private List<ODTEagerTO> odts;

	public RemitoEntradaTO() {

	}

	public RemitoEntradaTO(RemitoEntrada re) {
		this.id = re.getId();
		this.dateFechaEmision = re.getFechaEmision().getTime();
		this.pesoTotal = re.getPesoTotal();
		this.nroRemito = re.getNroRemito();
		this.anchoCrudo = re.getAnchoCrudo();
		this.anchoFinal = re.getAnchoFinal();
		this.enPalet = re.getEnPalet();
		this.control = re.getControl();
		this.observacionesODT = re.getObservacionesODT();
		this.articuloCliente = re.getArticuloCliente();
		this.idSituacionODT = re.getSituacionODT().getId();

		if (re.getCliente() != null) {
			this.idCliente = re.getCliente().getId();
		}
		if (re.getProveedor() != null) {
			this.idProveedor = re.getProveedor().getId();
		}
		if (re.getCondicionDeVenta() != null) {
			this.idCondicionDeVenta = re.getCondicionDeVenta().getId();
		}
		if (re.getArticuloStock() != null) {
			this.idArticuloStock = re.getArticuloStock().getId();
		}
		if (re.getPrecioMatPrima() != null) {
			this.idPrecioMatPrima = re.getPrecioMatPrima().getId();
		}
		if (re.getTarima() != null) {
			this.idTarima = re.getId();
		}
		if (re.getPiezas() != null && !re.getPiezas().isEmpty()) {
			this.piezas = new ArrayList<PiezaRemitoTO>();
			for (PiezaRemito pr : re.getPiezas()) {
				this.piezas.add(new PiezaRemitoTO(pr));
			}
		}
		if (re.getProductoArticuloList() != null && !re.getProductoArticuloList().isEmpty()) {
			this.productoArticuloIdsList = new ArrayList<Integer>();
			for (ProductoArticulo pa : re.getProductoArticuloList()) {
				this.productoArticuloIdsList.add(pa.getId());
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getDateFechaEmision() {
		return dateFechaEmision;
	}

	public void setDateFechaEmision(Long dateFechaEmision) {
		this.dateFechaEmision = dateFechaEmision;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public List<PiezaRemitoTO> getPiezas() {
		return piezas;
	}

	public void setPiezas(List<PiezaRemitoTO> piezas) {
		this.piezas = piezas;
	}

	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public Integer getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(Integer nroRemito) {
		this.nroRemito = nroRemito;
	}

	public Integer getIdCondicionDeVenta() {
		return idCondicionDeVenta;
	}

	public void setIdCondicionDeVenta(Integer idCondicionDeVenta) {
		this.idCondicionDeVenta = idCondicionDeVenta;
	}

	public List<Integer> getProductoArticuloIdsList() {
		return productoArticuloIdsList;
	}

	public void setProductoArticuloIdsList(List<Integer> productoArticuloIdsList) {
		this.productoArticuloIdsList = productoArticuloIdsList;
	}

	public Integer getIdArticuloStock() {
		return idArticuloStock;
	}

	public void setIdArticuloStock(Integer idArticuloStock) {
		this.idArticuloStock = idArticuloStock;
	}

	public Integer getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Integer idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Integer getIdPrecioMatPrima() {
		return idPrecioMatPrima;
	}

	public void setIdPrecioMatPrima(Integer idPrecioMatPrima) {
		this.idPrecioMatPrima = idPrecioMatPrima;
	}

	public BigDecimal getAnchoCrudo() {
		return anchoCrudo;
	}

	public void setAnchoCrudo(BigDecimal anchoCrudo) {
		this.anchoCrudo = anchoCrudo;
	}

	public BigDecimal getAnchoFinal() {
		return anchoFinal;
	}

	public void setAnchoFinal(BigDecimal anchoFinal) {
		this.anchoFinal = anchoFinal;
	}

	public Integer getIdTarima() {
		return idTarima;
	}

	public void setIdTarima(Integer idTarima) {
		this.idTarima = idTarima;
	}

	public Boolean getEnPalet() {
		return enPalet;
	}

	public void setEnPalet(Boolean enPalet) {
		this.enPalet = enPalet;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}
	
	public List<ODTEagerTO> getOdts() {
		return odts;
	}

	public void setOdts(List<ODTEagerTO> odts) {
		this.odts = odts;
	}

	public String getObservacionesODT() {
		return observacionesODT;
	}

	public void setObservacionesODT(String observacionesODT) {
		this.observacionesODT = observacionesODT;
	}

	public String getArticuloCliente() {
		return articuloCliente;
	}

	public void setArticuloCliente(String articuloCliente) {
		this.articuloCliente = articuloCliente;
	}

	public Integer getIdSituacionODT() {
		return idSituacionODT;
	}

	public void setIdSituacionODT(Integer idSituacionODT) {
		this.idSituacionODT = idSituacionODT;
	}

}
