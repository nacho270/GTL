package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;

public class MateriaPrimaCantidadExplotadaTO implements Serializable {

	private static final long serialVersionUID = -6931178289454251482L;

	private Integer idMateriaPrimaCantidad; // está federada
	private Integer idTipoArticulo;
	private Float cantidadExplotada;
	
	public MateriaPrimaCantidadExplotadaTO() {
		
	}

	public MateriaPrimaCantidadExplotadaTO(MateriaPrimaCantidadExplotada<?> mpc) {
		this.idMateriaPrimaCantidad = mpc.getMateriaPrimaCantidadDesencadenante().getId();
		this.idTipoArticulo = mpc.getTipoArticulo() == null ? null : mpc.getTipoArticulo().getId();
		this.cantidadExplotada = mpc.getCantidadExplotada();
	}

	public Integer getIdMateriaPrimaCantidad() {
		return idMateriaPrimaCantidad;
	}

	public void setIdMateriaPrimaCantidad(Integer idMateriaPrimaCantidad) {
		this.idMateriaPrimaCantidad = idMateriaPrimaCantidad;
	}

	public Integer getIdTipoArticulo() {
		return idTipoArticulo;
	}

	public void setIdTipoArticulo(Integer idTipoArticulo) {
		this.idTipoArticulo = idTipoArticulo;
	}

	public Float getCantidadExplotada() {
		return cantidadExplotada;
	}

	public void setCantidadExplotada(Float cantidadExplotada) {
		this.cantidadExplotada = cantidadExplotada;
	}

}
