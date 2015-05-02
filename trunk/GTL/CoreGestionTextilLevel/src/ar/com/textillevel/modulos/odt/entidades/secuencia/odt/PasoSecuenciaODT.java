package ar.com.textillevel.modulos.odt.entidades.secuencia.odt;

import javax.persistence.Entity;
import javax.persistence.Table;

import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.PasoSecuenciaAbstract;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.PasoSecuencia;

@Entity
@Table(name="T_PASO_SECEUNCIA_ODT")
public class PasoSecuenciaODT extends PasoSecuenciaAbstract<ProcedimientoODT>{

	private static final long serialVersionUID = -4072802828436933989L;

	public PasoSecuenciaAbstract<ProcedimientoODT> clonar() {
		PasoSecuenciaODT pasoClonado = new PasoSecuenciaODT();
		pasoClonado.setProceso(this.getProceso());
		pasoClonado.setSector(this.getSector());
		pasoClonado.setSubProceso(this.getSubProceso());
		pasoClonado.setObservaciones(this.getObservaciones());
		return pasoClonado;
	}
	
	public PasoSecuencia toPasoSecuencia() {
		PasoSecuencia pasoClonado = new PasoSecuencia();
		pasoClonado.setProceso(this.getProceso());
		pasoClonado.setSector(this.getSector());
		pasoClonado.setSubProceso(new ProcedimientoTipoArticulo(this.getSubProceso()));
		pasoClonado.setObservaciones(this.getObservaciones());
		return pasoClonado;
	}
}
