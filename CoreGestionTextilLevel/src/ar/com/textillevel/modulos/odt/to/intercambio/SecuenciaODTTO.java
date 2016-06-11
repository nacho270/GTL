package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.SecuenciaODT;

public class SecuenciaODTTO implements Serializable {

	private static final long serialVersionUID = 147202086606650651L;

	private Integer id;
	private String nombre;
	private List<PasoSecuenciaODTTO> pasosSecuencia;
	private Integer idCliente; // Cliente esta federado
	private Integer idTipoProducto;

	public SecuenciaODTTO() {

	}

	public SecuenciaODTTO(SecuenciaODT secuenciaDeTrabajo) {
		this.id = secuenciaDeTrabajo.getId();
		this.nombre = secuenciaDeTrabajo.getNombre();
		this.idCliente = secuenciaDeTrabajo.getCliente().getId();
		this.idTipoProducto = secuenciaDeTrabajo.getTipoProducto().getId();
		this.pasosSecuencia = new ArrayList<PasoSecuenciaODTTO>();
		for (PasoSecuenciaODT p : secuenciaDeTrabajo.getPasos()) {
			this.pasosSecuencia.add(new PasoSecuenciaODTTO(p));
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<PasoSecuenciaODTTO> getPasosSecuencia() {
		return pasosSecuencia;
	}

	public void setPasosSecuencia(List<PasoSecuenciaODTTO> pasosSecuencia) {
		this.pasosSecuencia = pasosSecuencia;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdTipoProducto() {
		return idTipoProducto;
	}

	public void setIdTipoProducto(Integer idTipoProducto) {
		this.idTipoProducto = idTipoProducto;
	}

}
