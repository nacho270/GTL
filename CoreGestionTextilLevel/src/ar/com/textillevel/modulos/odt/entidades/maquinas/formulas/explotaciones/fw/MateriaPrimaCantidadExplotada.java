package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

@Entity
@Table(name="T_MAP_PRIMA_EXPLOTADA")
public class MateriaPrimaCantidadExplotada<T extends Formulable> implements Serializable {

	private static final long serialVersionUID = 3881650717842551211L;

	private Integer id;
	private MateriaPrimaCantidad<T> materiaPrimaCantidadDesencadenante;
	private TipoArticulo tipoArticulo;
	private Float cantidadExplotada;

	public MateriaPrimaCantidadExplotada() {

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

	@ManyToOne(fetch=FetchType.EAGER,targetEntity=MateriaPrimaCantidad.class)
	@JoinColumn(name="F_MAT_PRIMA_CANT_P_ID")
	public MateriaPrimaCantidad<T> getMateriaPrimaCantidadDesencadenante() {
		return materiaPrimaCantidadDesencadenante;
	}

	public void setMateriaPrimaCantidadDesencadenante(MateriaPrimaCantidad<T> materiaPrimaCantidadDesencadenante) {
		this.materiaPrimaCantidadDesencadenante = materiaPrimaCantidadDesencadenante;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_TIPO_ARTICULO_P_ID")
	public TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

	@Column(name="A_CANTIDAD_EXPLOTADA")
	public Float getCantidadExplotada() {
		return cantidadExplotada;
	}

	public void setCantidadExplotada(Float cantidadExplotada) {
		this.cantidadExplotada = cantidadExplotada;
	}
}
