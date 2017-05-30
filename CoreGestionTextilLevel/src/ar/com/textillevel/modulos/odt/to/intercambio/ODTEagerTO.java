package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;

public class ODTEagerTO implements Serializable {

	private static final long serialVersionUID = -6055897474831182051L;

	private Integer id;
	private RemitoEntradaTO remito;
	private List<PiezaODTTO> piezas;
	private SecuenciaODTTO secuenciaDeTrabajo;
	private Integer idProductoArticulo; // ProductoArticulo esta federado
	private String codigo;
	private Long timestampFechaODT;
	private Integer idEstadoODT;
	private Byte idAvance;
	private Integer idMaquinaActual; // Maquina esta federada
	private Short ordenEnMaquina;
	private Integer idMaquinaPrincipal; // Maquina esta federada
	private Long longPorComenzarUltSector;
	private Long longEnProcesoUltSector;
	private Long longFinalizadoUltSector;
	private Integer idProducto; //de producto parcial
	private Integer idArticulo; //de producto parcial
	
	private List<TransicionODTTO> transiciones;

	public ODTEagerTO() {

	}

	public ODTEagerTO(OrdenDeTrabajo odt, List<TransicionODT> transiciones) {
		this.id = odt.getId();
		this.codigo = odt.getCodigo();
		this.timestampFechaODT = odt.getFechaODT().getTime();
		this.longPorComenzarUltSector = odt.getFechaPorComenzarUltSector() == null ? null : odt.getFechaPorComenzarUltSector().getTime();
		this.longEnProcesoUltSector = odt.getFechaEnProcesoUltSector() == null ? null : odt.getFechaEnProcesoUltSector().getTime();
		this.longFinalizadoUltSector = odt.getFechaFinalizadoUltSector() == null ? null : odt.getFechaFinalizadoUltSector().getTime();
		this.idEstadoODT = odt.getEstado().getId();
		if (odt.getProductoArticulo() != null) {
			this.idProductoArticulo = odt.getProductoArticulo().getId();
		}
		if(odt.getProductoParcial() != null && odt.getProductoParcial().getProducto() != null) {
			this.idProducto = odt.getProductoParcial().getProducto().getId();
		}
		if(odt.getProductoParcial() != null && odt.getProductoParcial().getArticulo() != null) {
			this.idArticulo = odt.getProductoParcial().getArticulo().getId();
		}
		if (odt.getAvance() != null) {
			this.idAvance = odt.getAvance().getId();
		}
		if (odt.getMaquinaActual() != null) {
			this.idMaquinaActual = odt.getMaquinaActual().getId();
		}
		if (odt.getOrdenEnMaquina() != null) {
			this.ordenEnMaquina = odt.getOrdenEnMaquina();
		}
		if (odt.getMaquinaPrincipal() != null) {
			this.idMaquinaPrincipal = odt.getMaquinaPrincipal().getId();
		}
		if (odt.getRemito() != null) {
			this.remito = new RemitoEntradaTO(odt.getRemito());
		}
		if (odt.getSecuenciaDeTrabajo() != null) {
			this.secuenciaDeTrabajo = new SecuenciaODTTO(odt.getSecuenciaDeTrabajo());
		}
		if (odt.getPiezas() != null && !odt.getPiezas().isEmpty()) {
			this.piezas = new ArrayList<PiezaODTTO>();
			for (PiezaODT po : odt.getPiezas()) {
				this.piezas.add(new PiezaODTTO(po));
			}
		}
		if(!transiciones.isEmpty()) {
			this.transiciones = new ArrayList<TransicionODTTO>();
			for(TransicionODT tODT : transiciones) {
				getTransiciones().add(new TransicionODTTO(tODT));
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RemitoEntradaTO getRemito() {
		return remito;
	}

	public void setRemito(RemitoEntradaTO remito) {
		this.remito = remito;
	}

	public List<PiezaODTTO> getPiezas() {
		return piezas;
	}

	public void setPiezas(List<PiezaODTTO> piezas) {
		this.piezas = piezas;
	}

	public SecuenciaODTTO getSecuenciaDeTrabajo() {
		return secuenciaDeTrabajo;
	}

	public void setSecuenciaDeTrabajo(SecuenciaODTTO secuenciaDeTrabajo) {
		this.secuenciaDeTrabajo = secuenciaDeTrabajo;
	}

	public Integer getIdProductoArticulo() {
		return idProductoArticulo;
	}

	public void setIdProductoArticulo(Integer idProductoArticulo) {
		this.idProductoArticulo = idProductoArticulo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Long getTimestampFechaODT() {
		return timestampFechaODT;
	}

	public void setTimestampFechaODT(Long timestampFechaODT) {
		this.timestampFechaODT = timestampFechaODT;
	}

	public Integer getIdEstadoODT() {
		return idEstadoODT;
	}

	public void setIdEstadoODT(Integer idEstadoODT) {
		this.idEstadoODT = idEstadoODT;
	}

	public Byte getIdAvance() {
		return idAvance;
	}

	public void setIdAvance(Byte idAvance) {
		this.idAvance = idAvance;
	}

	public Integer getIdMaquinaActual() {
		return idMaquinaActual;
	}

	public void setIdMaquinaActual(Integer idMaquinaActual) {
		this.idMaquinaActual = idMaquinaActual;
	}

	public Short getOrdenEnMaquina() {
		return ordenEnMaquina;
	}

	public void setOrdenEnMaquina(Short ordenEnMaquina) {
		this.ordenEnMaquina = ordenEnMaquina;
	}

	public Integer getIdMaquinaPrincipal() {
		return idMaquinaPrincipal;
	}

	public void setIdMaquinaPrincipal(Integer idMaquinaPrincipal) {
		this.idMaquinaPrincipal = idMaquinaPrincipal;
	}

	public Long getLongPorComenzarUltSector() {
		return longPorComenzarUltSector;
	}

	public void setLongPorComenzarUltSector(Long longPorComenzarUltSector) {
		this.longPorComenzarUltSector = longPorComenzarUltSector;
	}

	public Long getLongEnProcesoUltSector() {
		return longEnProcesoUltSector;
	}

	public void setLongEnProcesoUltSector(Long longEnProcesoUltSector) {
		this.longEnProcesoUltSector = longEnProcesoUltSector;
	}

	public Long getLongFinalizadoUltSector() {
		return longFinalizadoUltSector;
	}

	public void setLongFinalizadoUltSector(Long longFinalizadoUltSector) {
		this.longFinalizadoUltSector = longFinalizadoUltSector;
	}

	public List<TransicionODTTO> getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(List<TransicionODTTO> transiciones) {
		this.transiciones = transiciones;
	}

	public Integer getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}

	public Integer getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(Integer idArticulo) {
		this.idArticulo = idArticulo;
	}

}