package ar.com.textillevel.entidades.documentos.remito;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPrecioMateriaPrima;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Entity
@Table(name="T_REMITO_ENTRADA_DIBUJO")
public class RemitoEntradaDibujo implements Serializable {

	private static final long serialVersionUID = 3990757092484629852L;

	private Integer id;
	private Timestamp fechaIngreso;
	private Cliente cliente;
	private Factura factura;
	private List<ItemDibujoRemitoEntrada> items;

	public RemitoEntradaDibujo() {
		this.items = new ArrayList<ItemDibujoRemitoEntrada>();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "P_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="A_FECHA_INGRESO", nullable=false)
	public Timestamp getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Timestamp fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	@ManyToOne
	@JoinColumn(name="F_CLIENTE_P_ID", nullable=false)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@ManyToOne
	@JoinColumn(name="F_FACTURA_P_ID", nullable=true)
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_REMITO_ENT_DIBUJO_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ItemDibujoRemitoEntrada> getItems() {
		return items;
	}

	public void setItems(List<ItemDibujoRemitoEntrada> items) {
		this.items = items;
	}

	@Transient
	public Integer calcCantCilindros() {
		Integer res = 0;
		for(DibujoEstampado d : getDibujos()) {
			res += d.getCantidadColores();
		}
		return res;
	}

	@Transient
	public List<DibujoEstampado> getDibujos() {
		List<DibujoEstampado> result = new ArrayList<DibujoEstampado>();
		for(ItemDibujoRemitoEntrada it : getItems()) {
			result.add(it.getDibujo());
		}
		return result;
	}

	@Transient
	public void addItem(ItemFacturaPrecioMateriaPrima it, DibujoEstampado de) {
		ItemDibujoRemitoEntrada item = new ItemDibujoRemitoEntrada();
		item.setDibujo(de);
		item.setPrecio(it.getPrecioUnitario().multiply(new BigDecimal(de.getCantidadColores())));
		getItems().add(item);
	}

	@Transient
	public void addItem(double precio, DibujoEstampado de) {
		ItemDibujoRemitoEntrada item = new ItemDibujoRemitoEntrada();
		item.setDibujo(de);
		item.setPrecio(new BigDecimal(precio));
		getItems().add(item);
	}

	@Transient
	public ItemDibujoRemitoEntrada getItem(DibujoEstampado dibujo) {
		for(ItemDibujoRemitoEntrada it : getItems()) {
			if(it.getDibujo().equals(dibujo)) {
				return it;
			}
		}
		return null;
	}

	@Transient
	public List<DibujoEstampado> getDibujosPersited() {
		List<DibujoEstampado> result = new ArrayList<DibujoEstampado>();
		for(ItemDibujoRemitoEntrada it : getItems()) {
			if(it.getDibujo() != null && it.getDibujo().getId() != null) {
				result.add(it.getDibujo());
			}
		}
		return result;
	}

}