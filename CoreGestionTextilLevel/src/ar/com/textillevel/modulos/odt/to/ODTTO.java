package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;
import java.math.BigDecimal;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;

public class ODTTO implements Serializable {

	private static final long serialVersionUID = -8293434233580197226L;

	private Integer id;
	private String codigo;
	private Integer idRemito;
	private String nombreCliente;
	private Integer nroCliente;
	private String producto;
	private Short ordenEnMaquina;
	private Integer maquinaActual;
	private BigDecimal totalMetros;
	private BigDecimal totalKilos;
	private EAvanceODT avance;
	private ETipoProducto tipoProducto;
	private TipoMaquina tipoMaquina;
	
	public ODTTO() {

	}

	public ODTTO(Integer id, String codigo, Integer idRemito, Cliente cliente, ProductoArticulo productoArticulo, Short ordenEnMaquina, Maquina maquinaActual, BigDecimal totalKilos, Byte avance, BigDecimal totalMetros) {
		this.id = id;
		this.codigo = codigo;
		this.idRemito = idRemito;
		this.nombreCliente = cliente.getDescripcionResumida();
		this.nroCliente = cliente.getNroCliente();
		this.producto = productoArticulo.toString();
		this.ordenEnMaquina = ordenEnMaquina;
		this.maquinaActual = maquinaActual == null ? null : maquinaActual.getId();
		this.totalMetros = totalMetros;
		this.totalKilos = totalKilos;
		this.avance = avance == null ? null : EAvanceODT.getById(avance.byteValue());
		this.tipoProducto = productoArticulo.getTipo();
		this.tipoMaquina = maquinaActual == null ? null : maquinaActual.getTipoMaquina();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getIdRemito() {
		return idRemito;
	}

	public void setIdRemito(Integer idRemito) {
		this.idRemito = idRemito;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public Short getOrdenEnMaquina() {
		return ordenEnMaquina;
	}

	public void setOrdenEnMaquina(Short ordenEnMaquina) {
		this.ordenEnMaquina = ordenEnMaquina;
	}

	public Integer getMaquinaActual() {
		return maquinaActual;
	}

	public void setMaquinaActual(Integer maquinaActual) {
		this.maquinaActual = maquinaActual;
	}

	public Integer getNroCliente() {
		return nroCliente;
	}

	public BigDecimal getTotalMetros() {
		return totalMetros;
	}

	public BigDecimal getTotalKilos() {
		return totalKilos;
	}

	public EAvanceODT getAvance() {
		return avance;
	}

	public ETipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public TipoMaquina getTipoMaquina() {
		return tipoMaquina;
	}

}