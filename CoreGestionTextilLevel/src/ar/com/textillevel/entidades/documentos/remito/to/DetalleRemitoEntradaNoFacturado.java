package ar.com.textillevel.entidades.documentos.remito.to;

import java.io.Serializable;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;

public class DetalleRemitoEntradaNoFacturado implements Serializable {

	private static final long serialVersionUID = -8760178816960042960L;

	private Integer id;
	private Integer nroRemito;
	private String cliente;
	private String fecha;
	private String listaODTs;
	private String listaProductos;

	public DetalleRemitoEntradaNoFacturado(RemitoEntrada re) {
		this.id = re.getId();
		this.nroRemito = re.getNroRemito();
		this.cliente = re.getCliente().getRazonSocial();
		this.fecha = DateUtil.dateToString(re.getFechaEmision());
		this.listaProductos = FluentIterable.from(re.getProductoArticuloList()).join(Joiner.on(","));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(Integer nroRemito) {
		this.nroRemito = nroRemito;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getListaODTs() {
		return listaODTs;
	}

	public void setListaODTs(String listaODTs) {
		this.listaODTs = listaODTs;
	}

	public String getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(String listaProductos) {
		this.listaProductos = listaProductos;
	}
}
