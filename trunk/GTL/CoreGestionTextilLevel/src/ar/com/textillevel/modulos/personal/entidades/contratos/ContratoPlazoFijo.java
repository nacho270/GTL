package ar.com.textillevel.modulos.personal.entidades.contratos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="PLAZOFIJO")
public class ContratoPlazoFijo extends Contrato{

	private static final long serialVersionUID = 3886055261544969383L;
}
