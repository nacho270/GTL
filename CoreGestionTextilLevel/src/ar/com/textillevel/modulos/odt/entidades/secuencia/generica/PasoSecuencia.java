package ar.com.textillevel.modulos.odt.entidades.secuencia.generica;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.PasoSecuenciaAbstract;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.ProcedimientoODT;

@Entity
@Table(name="T_PASO_SECEUNCIA")
public class PasoSecuencia extends PasoSecuenciaAbstract<ProcedimientoTipoArticulo> implements Serializable {

	private static final long serialVersionUID = -3130217208752941990L;

	@Transient
	public PasoSecuencia clonar() {
		PasoSecuencia pasoClonado = new PasoSecuencia();
		pasoClonado.setProceso(this.getProceso());
		pasoClonado.setSector(this.getSector());
		pasoClonado.setSubProceso(this.getSubProceso());
		pasoClonado.setObservaciones(this.getObservaciones());
		return pasoClonado;
	}

	public PasoSecuenciaODT toPasoODT() {
		PasoSecuenciaODT pasoClonado = new PasoSecuenciaODT();
		pasoClonado.setProceso(this.getProceso());
		pasoClonado.setSector(this.getSector());
		pasoClonado.setSubProceso(new ProcedimientoODT(this.getSubProceso()));
		pasoClonado.setObservaciones(this.getObservaciones());
		return pasoClonado;
	}
}