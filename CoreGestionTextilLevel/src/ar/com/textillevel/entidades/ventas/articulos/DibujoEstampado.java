package ar.com.textillevel.entidades.ventas.articulos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.swing.ImageIcon;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import ar.com.fwcommon.util.ImageUtil;
import ar.com.textillevel.entidades.gente.Cliente;

@Entity
@Table(name="T_DIBUJO")
public class DibujoEstampado implements Serializable, Comparable<DibujoEstampado>{
	
	private static final long serialVersionUID = -2130053260324494270L;
	
	private Integer id;
	private String nombre;
	private Integer nroDibujo;
	private BigDecimal anchoCilindro;
	private Cliente cliente;
	private List<VarianteEstampado> variantes;
	private byte[] bytesFotos;
	private Integer idEstado;

	public DibujoEstampado() {
		this.idEstado = EEstadoDibujo.EN_STOCK.getId();
		this.variantes = new ArrayList<VarianteEstampado>();
	}
	
	@Id
	@Column(name="P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="A_NOMBRE", nullable=false)
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="F_VARIANTE_P_ID")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<VarianteEstampado> getVariantes() {
		return variantes;
	}

	public void setVariantes(List<VarianteEstampado> variantes) {
		this.variantes = variantes;
	}
	
	@Column(name = "A_IMAGEN", nullable = true)
	@Type(type="org.hibernate.type.PrimitiveByteArrayBlobType")
	private byte[] getBytesFotos() {
		return bytesFotos;
	}

	private void setBytesFotos(byte[] bytesFotos) {
		this.bytesFotos = bytesFotos;
	}
	
	@Transient
	public ImageIcon getImagenEstampado() {
		if(getBytesFotos()!=null){
			return new ImageIcon(getBytesFotos());
		}
		return null;
	}
	
	public void setImagenEstampado(ImageIcon imagen) {
		if(imagen != null){
			try{
				setBytesFotos(ImageUtil.toByteArray(imagen.getImage()));
			}catch (Exception e) {
				
			}
		}
	}
	
	@Column(name="A_NRO_DIBUJO",nullable=true)
	public Integer getNroDibujo() {
		return nroDibujo;
	}
	
	public void setNroDibujo(Integer nroDibujo) {
		this.nroDibujo = nroDibujo;
	}

	@Column(name="A_ANCHO_CILINDRO",nullable=false)
	public BigDecimal getAnchoCilindro() {
		return anchoCilindro;
	}

	public void setAnchoCilindro(BigDecimal anchoCilindro) {
		this.anchoCilindro = anchoCilindro;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "F_CLIENTE_P_ID", nullable = true)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Column(name = "A_ID_ESTADO", nullable = false)
	private Integer getIdEstado() {
		return idEstado;
	}

	private void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}
	
	@Transient
	public EEstadoDibujo getEstado(){
		return EEstadoDibujo.getById(getIdEstado());
	}
	
	public void setEstado(EEstadoDibujo estado){
		setIdEstado(estado.getId());
	}

	@Override
	public String toString() {
		return getNroDibujo() + " - " + getNombre();
	}

	@Transient
	public Integer getCantidadColores() {
		if(getNroDibujo() != null) {
			return Integer.valueOf(String.valueOf(getNroDibujo().toString().charAt(0)));
		}
		return null;
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
		final DibujoEstampado other = (DibujoEstampado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public int compareTo(DibujoEstampado o) {
		return getNombre().compareToIgnoreCase(o.getNombre());
	}
}