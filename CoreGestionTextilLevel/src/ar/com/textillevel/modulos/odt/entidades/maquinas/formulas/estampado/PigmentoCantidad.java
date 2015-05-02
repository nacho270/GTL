package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.estampado;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

@Entity
@Table(name = "T_PIGMENTO_CANTIDAD")
public class PigmentoCantidad extends MateriaPrimaCantidad<Pigmento> implements Serializable {

	private static final long serialVersionUID = -4192149920395059944L;

}
