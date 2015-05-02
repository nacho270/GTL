package ar.com.textillevel.modulos.personal.entidades.contratos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="EFECTIVO")
public class ContratoEfectivo extends Contrato{

	private static final long serialVersionUID = 2766288378973539794L;
}
