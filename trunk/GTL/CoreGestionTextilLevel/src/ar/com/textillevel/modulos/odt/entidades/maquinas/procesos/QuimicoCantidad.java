package ar.com.textillevel.modulos.odt.entidades.maquinas.procesos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

@Entity
@Table(name = "T_QUIMICO_CANTIDAD")
public class QuimicoCantidad extends MateriaPrimaCantidad<Quimico> implements Serializable {

	private static final long serialVersionUID = -2171043015260068630L;

}
