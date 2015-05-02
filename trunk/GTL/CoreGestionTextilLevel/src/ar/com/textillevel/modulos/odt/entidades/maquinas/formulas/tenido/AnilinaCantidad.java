package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.tenido;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

@Entity
@Table(name = "T_ANILINA_CANTIDAD")
public class AnilinaCantidad extends MateriaPrimaCantidad<Anilina> implements Serializable {

	private static final long serialVersionUID = 6339322870186346444L;

}
