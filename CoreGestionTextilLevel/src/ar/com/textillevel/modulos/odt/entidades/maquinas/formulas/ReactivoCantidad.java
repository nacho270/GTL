package ar.com.textillevel.modulos.odt.entidades.maquinas.formulas;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import ar.com.textillevel.entidades.ventas.materiaprima.Reactivo;

@Entity
@Table(name = "T_REACTIVO_CANTIDAD")
public class ReactivoCantidad extends MateriaPrimaCantidad<Reactivo> implements Serializable {

	private static final long serialVersionUID = -1755193977403618937L;

}
