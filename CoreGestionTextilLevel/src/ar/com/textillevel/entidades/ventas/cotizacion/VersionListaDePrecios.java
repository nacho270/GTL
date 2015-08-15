package ar.com.textillevel.entidades.ventas.cotizacion;

import java.io.Serializable;
import java.sql.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "T_VERSION_LISTA_PRECIO")
public class VersionListaDePrecios implements Serializable{

	private static final long serialVersionUID = 8639306062580166848L;
	
	private Integer id;
	private Date inicioValidez;
	private List<DefinicionPrecio> precios;

	
	public VersionListaDePrecios() {
		this.precios = new ArrayList<DefinicionPrecio>();
	}

	public VersionListaDePrecios(Date inicioValidez) {
		this();
		this.inicioValidez = inicioValidez;
	}

	@Id
	@Column(name = "P_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "A_INICIO_VALIDEZ", nullable = false)
	public Date getInicioValidez() {
		return inicioValidez;
	}

	public void setInicioValidez(Date inicioValidez) {
		this.inicioValidez = inicioValidez;
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinColumn(name = "F_VERSION_LISTA_PRECIO_P_ID")
	@org.hibernate.annotations.Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<DefinicionPrecio> getPrecios() {
		return precios;
	}

	public void setPrecios(List<DefinicionPrecio> precios) {
		this.precios = precios;
	}
}
