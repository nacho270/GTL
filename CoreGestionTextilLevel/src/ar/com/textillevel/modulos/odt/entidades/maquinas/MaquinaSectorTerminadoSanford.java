package ar.com.textillevel.modulos.odt.entidades.maquinas;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="MSTS")
public class MaquinaSectorTerminadoSanford extends MaquinaSectorTerminado{

	private static final long serialVersionUID = 2611356938508586798L;

}
