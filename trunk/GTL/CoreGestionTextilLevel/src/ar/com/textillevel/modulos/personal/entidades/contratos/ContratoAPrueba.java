package ar.com.textillevel.modulos.personal.entidades.contratos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="APRUEBA")
public class ContratoAPrueba extends Contrato{

	private static final long serialVersionUID = 2638011948340686520L;
}
