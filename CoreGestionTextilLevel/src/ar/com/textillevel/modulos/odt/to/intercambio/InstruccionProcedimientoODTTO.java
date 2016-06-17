package ar.com.textillevel.modulos.odt.to.intercambio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoPasadasODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoTextoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoTipoProductoODT;

public class InstruccionProcedimientoODTTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tipo; // pasada, texto, tipo producto
	private Byte idTipoSector;
	private String observaciones;

	// pasada
	private Integer cantidadPasadas;
	private Float temperatura;
	private Float velocidad;
	private List<MateriaPrimaCantidadExplotadaTO> mpCantidadExplotadas; 
	private Integer accionProcedimientoId;

	// texto
	private String especificacion;

	// tipo producto
	private Integer idTipoArticulo;
	private Integer idTipoProducto;
	private FormulaClienteExplotadaTO formula;

	public InstruccionProcedimientoODTTO() {
	}

	public InstruccionProcedimientoODTTO(InstruccionProcedimientoODT instODT) {
		this.idTipoSector = instODT.getSectorMaquina().getId();
		this.observaciones = instODT.getObservaciones();
		if (instODT instanceof InstruccionProcedimientoPasadasODT) {
			InstruccionProcedimientoPasadasODT instODTP = (InstruccionProcedimientoPasadasODT) instODT;
			this.tipo = "IPPODT";
			this.cantidadPasadas = instODTP.getCantidadPasadas();
			this.temperatura = instODTP.getTemperatura();
			this.velocidad = instODTP.getVelocidad();
			this.accionProcedimientoId = instODTP.getAccion().getId();
			setMpCantidadExplotadas(new ArrayList<MateriaPrimaCantidadExplotadaTO>());
			for(MateriaPrimaCantidadExplotada<Quimico> mp : instODTP.getQuimicosExplotados()) {
				getMpCantidadExplotadas().add(new MateriaPrimaCantidadExplotadaTO(mp));
			}
		} else if (instODT instanceof InstruccionProcedimientoTextoODT) {
			InstruccionProcedimientoTextoODT instODTT = (InstruccionProcedimientoTextoODT) instODT;
			this.tipo = "IPPTODT";
			this.especificacion = instODTT.getEspecificacion();

		} else {
			InstruccionProcedimientoTipoProductoODT instODTTP = (InstruccionProcedimientoTipoProductoODT) instODT;
			this.tipo = "IPTPODT";
			this.idTipoArticulo = instODTTP.getTipoArticulo().getId();
			this.idTipoProducto = instODTTP.getTipoProducto().getId();
			this.formula = new FormulaClienteExplotadaTO(instODTTP.getFormula());
		}
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Byte getIdTipoSector() {
		return idTipoSector;
	}

	public void setIdTipoSector(Byte idTipoSector) {
		this.idTipoSector = idTipoSector;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Integer getCantidadPasadas() {
		return cantidadPasadas;
	}

	public void setCantidadPasadas(Integer cantidadPasadas) {
		this.cantidadPasadas = cantidadPasadas;
	}

	public Float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Float temperatura) {
		this.temperatura = temperatura;
	}

	public Float getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(Float velocidad) {
		this.velocidad = velocidad;
	}

	public List<MateriaPrimaCantidadExplotadaTO> getMpCantidadExplotadas() {
		return mpCantidadExplotadas;
	}

	public void setMpCantidadExplotadas(
			List<MateriaPrimaCantidadExplotadaTO> mpCantidadExplotadas) {
		this.mpCantidadExplotadas = mpCantidadExplotadas;
	}

	public Integer getAccionProcedimientoId() {
		return accionProcedimientoId;
	}

	public void setAccionProcedimientoId(Integer accionProcedimientoId) {
		this.accionProcedimientoId = accionProcedimientoId;
	}

	public String getEspecificacion() {
		return especificacion;
	}

	public void setEspecificacion(String especificacion) {
		this.especificacion = especificacion;
	}

	public Integer getIdTipoArticulo() {
		return idTipoArticulo;
	}

	public void setIdTipoArticulo(Integer idTipoArticulo) {
		this.idTipoArticulo = idTipoArticulo;
	}

	public Integer getIdTipoProducto() {
		return idTipoProducto;
	}

	public void setIdTipoProducto(Integer idTipoProducto) {
		this.idTipoProducto = idTipoProducto;
	}

	public FormulaClienteExplotadaTO getFormula() {
		return formula;
	}

	public void setFormula(FormulaClienteExplotadaTO formula) {
		this.formula = formula;
	}

}