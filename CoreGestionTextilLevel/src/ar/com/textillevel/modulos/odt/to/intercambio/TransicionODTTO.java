package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.modulos.odt.entidades.workflow.CambioAvance;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;

public class TransicionODTTO implements Serializable {

	private static final long serialVersionUID = -8686032151062787593L;

	private Integer idMaquina;
	private Integer idTipoMaquina;
	private Long fechaHoraRegistro;
	private List<CambioAvanceTO> cambiosAvance;
	private Integer idUsuarioSistema;
	private Integer idTerminal;

	public TransicionODTTO() {
	}
	
	public TransicionODTTO(TransicionODT tODT) {
		this.idMaquina = tODT.getMaquina() == null ? null : tODT.getMaquina().getId();
		this.idTipoMaquina = tODT.getTipoMaquina() == null ? null : tODT.getTipoMaquina().getId();
		this.fechaHoraRegistro = tODT.getFechaHoraRegistro().getTime();
		this.idUsuarioSistema = tODT.getUsuarioSistema() == null ? null : tODT.getUsuarioSistema().getId();
		this.cambiosAvance = new ArrayList<CambioAvanceTO>();
		this.idTerminal = tODT.getTerminal() == null ? null : tODT.getTerminal().getId(); 
		for(CambioAvance ca : tODT.getCambiosAvance()) {
			getCambiosAvance().add(new CambioAvanceTO(ca));
		}
	}

	public Integer getIdMaquina() {
		return idMaquina;
	}

	public void setIdMaquina(Integer idMaquina) {
		this.idMaquina = idMaquina;
	}

	public Integer getIdTipoMaquina() {
		return idTipoMaquina;
	}

	public void setIdTipoMaquina(Integer idTipoMaquina) {
		this.idTipoMaquina = idTipoMaquina;
	}

	public Long getFechaHoraRegistro() {
		return fechaHoraRegistro;
	}

	public void setFechaHoraRegistro(Long fechaHoraRegistro) {
		this.fechaHoraRegistro = fechaHoraRegistro;
	}

	public Integer getIdUsuarioSistema() {
		return idUsuarioSistema;
	}

	public void setIdUsuarioSistema(Integer idUsuarioSistema) {
		this.idUsuarioSistema = idUsuarioSistema;
	}

	public List<CambioAvanceTO> getCambiosAvance() {
		return cambiosAvance;
	}

	public void setCambiosAvance(List<CambioAvanceTO> cambiosAvance) {
		this.cambiosAvance = cambiosAvance;
	}

	public Integer getIdTerminal() {
		return idTerminal;
	}

	public void setIdTerminal(Integer idTerminal) {
		this.idTerminal = idTerminal;
	}

}