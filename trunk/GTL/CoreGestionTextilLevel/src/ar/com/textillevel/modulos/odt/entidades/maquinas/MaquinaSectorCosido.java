package ar.com.textillevel.modulos.odt.entidades.maquinas;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="MSC")
public class MaquinaSectorCosido extends Maquina {

	private static final long serialVersionUID = -6490920786539290292L;

}
