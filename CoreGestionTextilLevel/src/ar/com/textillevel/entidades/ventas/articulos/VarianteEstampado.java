package ar.com.textillevel.entidades.ventas.articulos;

import java.io.Serializable;
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
import javax.swing.ImageIcon;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import ar.clarin.fwjava.util.ImageUtil;

@Entity
@Table(name="T_VARIANTE")
public class VarianteEstampado implements Serializable {

	private static final long serialVersionUID = -4656428049501062392L;

	private Integer id;
	private String nombre;
	private Color colorFondo;
	private List<ColorCilindro> colores;
	private byte[] bytesFotos;
	private byte[] bytesFotosOriginal;
	private Float porcentajeCobertura;
	private GamaColor gama;

	public VarianteEstampado() {
		this.colores = new ArrayList<ColorCilindro>();
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
	@JoinColumn(name="F_VARIANTE_P_ID", nullable=false)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ColorCilindro> getColores() {
		return colores;
	}

	public void setColores(List<ColorCilindro> colores) {
		this.colores = colores;
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
		}else{
			setBytesFotos(null);
		}
	}

	@Column(name = "A_IMAGEN_ORIG", nullable = true)
	@Type(type="org.hibernate.type.PrimitiveByteArrayBlobType")
	private byte[] getBytesFotosOriginal() {
		return bytesFotosOriginal;
	}

	private void setBytesFotosOriginal(byte[] bytesFotosOriginal) {
		this.bytesFotosOriginal = bytesFotosOriginal;
	}

	@Transient
	public ImageIcon getImagenEstampadoOriginal() {
		if(getBytesFotosOriginal()!=null){
			return new ImageIcon(getBytesFotosOriginal());
		}
		return null;
	}

	public void setImagenEstampadoOriginal(ImageIcon imagen) {
		if(imagen != null){
			try{
				setBytesFotosOriginal(ImageUtil.toByteArray(imagen.getImage()));
			}catch (Exception e) {
				
			}
		}else{
			setBytesFotosOriginal(null);
		}
	}

	@ManyToOne
	@JoinColumn(name="F_COLOR_FONDO_P_ID")
	public Color getColorFondo() {
		return colorFondo;
	}

	public void setColorFondo(Color colorFondo) {
		this.colorFondo = colorFondo;
	}

	@Column(name = "A_PORC_COBERTURA")
	public Float getPorcentajeCobertura() {
		return porcentajeCobertura;
	}

	public void setPorcentajeCobertura(Float porcentajeCobertura) {
		this.porcentajeCobertura = porcentajeCobertura;
	}

	@ManyToOne
	@JoinColumn(name="F_GAMA_P_ID")
	public GamaColor getGama() {
		return gama;
	}

	public void setGama(GamaColor gama) {
		this.gama = gama;
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
		final VarianteEstampado other = (VarianteEstampado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	@Transient
	public String toString(){
		return nombre;
	}

	@Transient
	public boolean fondoSeEstampaEnCilindro() {
		for(ColorCilindro cc : getColores()) {
			if(cc.getColor() == null) {
				return true;
			}
		}
		return false;
	}

}