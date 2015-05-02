package ar.com.textillevel.modulos.odt.entidades.secuencia.odt;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.SecuenciaAbstract;

@Entity
@Table(name="T_SECUENCIA_ODT")
public class SecuenciaODT extends SecuenciaAbstract<ProcedimientoODT,PasoSecuenciaODT> {

	private static final long serialVersionUID = 4204640940361972102L;

	private OrdenDeTrabajo odt;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="F_ODT_P_ID")
	public OrdenDeTrabajo getOdt() {
		return odt;
	}

	public void setOdt(OrdenDeTrabajo odt) {
		this.odt = odt;
	}

	public SecuenciaAbstract<ProcedimientoODT,PasoSecuenciaODT> clonar() {
		SecuenciaODT secClonada = new SecuenciaODT();
		secClonada.setCliente(this.getCliente());
		secClonada.setNombre(this.getNombre());
		secClonada.setTipoProducto(getTipoProducto());
		//NO HARIA FALTA CLONAR LA ODT
		for(PasoSecuenciaODT p : this.getPasos()){
			secClonada.getPasos().add((PasoSecuenciaODT)p.clonar());
		}
		return secClonada;
	}
}
