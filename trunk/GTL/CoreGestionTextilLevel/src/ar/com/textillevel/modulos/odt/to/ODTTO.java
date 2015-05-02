package ar.com.textillevel.modulos.odt.to;

import java.io.Serializable;

import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class ODTTO implements Serializable {

	private static final long serialVersionUID = -8293434233580197226L;

	private Integer id;
	private String codigo;
	private Integer idRemito;
	private String nombreCliente;
	private String producto;
	private Short ordenEnMaquina;
	private Integer maquinaActual;
	
	public ODTTO() {

	}

	public ODTTO(OrdenDeTrabajo odt) {
		this.id = odt.getId();
		this.codigo = odt.getCodigo();
		this.idRemito = odt.getRemito().getId();
		this.nombreCliente = odt.getRemito().getCliente().getDescripcionResumida();
		this.producto = odt.getProducto().toString();
		this.ordenEnMaquina = odt.getOrdenEnMaquina();
		this.maquinaActual = odt.getMaquinaActual()!=null?odt.getMaquinaActual().getId():null;
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

}
