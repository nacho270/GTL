package ar.com.textillevel.modulos.odt.entidades.secuencia.generica;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcedimientoTipoArticulo;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.SecuenciaAbstract;

@Entity
@Table(name="T_SECUENCIA_TIPO_PRODUCTO")
public class SecuenciaTipoProducto extends SecuenciaAbstract<ProcedimientoTipoArticulo,PasoSecuencia> implements Serializable {

	private static final long serialVersionUID = 4778120514386655552L;

	@Transient
	public SecuenciaTipoProducto clonar() {
		SecuenciaTipoProducto secClonada = new SecuenciaTipoProducto();
		secClonada.setCliente(this.getCliente());
		secClonada.setNombre(this.getNombre());
		secClonada.setTipoProducto(getTipoProducto());
		for(PasoSecuencia p : this.getPasos()){
			secClonada.getPasos().add(p.clonar());
		}
		return secClonada;
	}
}
