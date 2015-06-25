package ar.com.textillevel.entidades.documentos.factura;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFactura;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.EEstadoImpresionDocumento;

@Entity
@DiscriminatorValue(value = "FC")
public class Factura extends DocumentoContableCliente implements Serializable {

	private static final long serialVersionUID = -541804970454200801L;

	private List<RemitoSalida> remitos;
	private List<ItemFactura> items;
	private BigDecimal porcentajeIVANoInscripto;
	private BigDecimal montoFaltantePorPagar;
	private CondicionDeVenta condicionDeVenta;
	private BigDecimal montoImpuestos;
	private String usuarioConfirmacion;		//EL USUARIO QUE ACEPTA UN DOCUMENTO, LO PONGO ACA PARA QUE SEA FACIL TRAERLO EN LA TABLA DE MOVIMIENTOS
	
	public Factura() {
		setIdEstado(EEstadoFactura.IMPAGA.getId());
		setIdEstadoImpresion(EEstadoImpresionDocumento.PENDIENTE.getId());
	}

	public Factura(Timestamp fechaEmision) {
		setIdEstado(EEstadoFactura.IMPAGA.getId());
		setFechaEmision(fechaEmision);
		setIdEstadoImpresion(EEstadoImpresionDocumento.PENDIENTE.getId());
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "F_FACTURA_P_ID")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ItemFactura> getItems() {
		return items;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "F_FACTURA_P_ID", nullable = true)
	public  List<RemitoSalida> getRemitos() {
		return remitos;
	}

	public void setRemitos( List<RemitoSalida> remitos) {
		this.remitos = remitos;
	}

	@Column(name = "A_PRCT_IVA_NO_INSCR", nullable = true)
	public BigDecimal getPorcentajeIVANoInscripto() {
		return porcentajeIVANoInscripto;
	}

	public void setPorcentajeIVANoInscripto(BigDecimal porcentajeIVANoInscripto) {
		this.porcentajeIVANoInscripto = porcentajeIVANoInscripto;
	}

	@Column(name = "A_MONTO_FALTANTE", nullable = true)
	public BigDecimal getMontoFaltantePorPagar() {
		return montoFaltantePorPagar;
	}

	public void setMontoFaltantePorPagar(BigDecimal montoFaltantePorPagar) {
		this.montoFaltantePorPagar = montoFaltantePorPagar;
	}

	@ManyToOne
	@JoinColumn(name = "F_COND_VENTA_P_ID", nullable = true)
	@Fetch(FetchMode.JOIN)
	public CondicionDeVenta getCondicionDeVenta() {
		return condicionDeVenta;
	}

	public void setCondicionDeVenta(CondicionDeVenta condicionDeVenta) {
		this.condicionDeVenta = condicionDeVenta;
	}

	@Column(name = "A_MONTO_IMPUESTOS")
	public BigDecimal getMontoImpuestos() {
		return montoImpuestos;
	}

	public void setMontoImpuestos(BigDecimal montoImpuestos) {
		this.montoImpuestos = montoImpuestos;
	}

	@Transient
	@Override
	public String toString(){
		return "Factura - " + getNroFactura();
	}

	@Column(name="A_USR_CONFIRMACION")	
	public String getUsuarioConfirmacion() {
		return usuarioConfirmacion;
	}
	
	public void setUsuarioConfirmacion(String usuarioConfirmacion) {
		this.usuarioConfirmacion = usuarioConfirmacion;
	}
	
	@Transient
	public String getNrosRemito() {
		List<String> lista = new ArrayList<String>();
		for(RemitoSalida r : remitos){
			lista.add(String.valueOf(r.getNroRemito()));
		}
		return StringUtil.getCadena(lista, " / ");
	}

	@Override
	@Transient
	public ETipoDocumento getTipoDocumento() {
		return ETipoDocumento.FACTURA;
	}

	@Override
	@Transient
	public List<DocumentoContableCliente> getDocsContableRelacionados() {
		return Collections.emptyList();
	}

}