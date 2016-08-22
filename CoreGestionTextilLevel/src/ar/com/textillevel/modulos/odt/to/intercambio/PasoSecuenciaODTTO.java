package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;

import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;

public class PasoSecuenciaODTTO implements Serializable {

	private static final long serialVersionUID = -3280719971982306176L;

	private Integer id;
	private Integer idSector; // TipoMaquina esta federado
	private Integer idProceso; // ProcesoTipoMaquina esta federado
	private String observaciones;
	private ProcedimientoODTTO subProceso; // ProcedimientoODT NO esta federado

	public PasoSecuenciaODTTO() {
	}

	public PasoSecuenciaODTTO(PasoSecuenciaODT p) {
		this.id = p.getId();
		this.idSector = p.getSector().getId();
		this.idProceso = p.getProceso().getId();
		this.observaciones = p.getObservaciones();
		this.subProceso = new ProcedimientoODTTO(p.getSubProceso());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdSector() {
		return idSector;
	}

	public void setIdSector(Integer idSector) {
		this.idSector = idSector;
	}

	public Integer getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(Integer idProceso) {
		this.idProceso = idProceso;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public ProcedimientoODTTO getSubProceso() {
		return subProceso;
	}

	public void setSubProceso(ProcedimientoODTTO subProceso) {
		this.subProceso = subProceso;
	}

}
