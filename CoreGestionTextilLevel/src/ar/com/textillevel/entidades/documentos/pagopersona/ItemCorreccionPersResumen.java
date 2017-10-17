package ar.com.textillevel.entidades.documentos.pagopersona;

import java.math.BigDecimal;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "ICRPERS")
public class ItemCorreccionPersResumen extends ItemCorreccionFacturaPersona {

	private static final long serialVersionUID = -6504256170207638579L;

	public ItemCorreccionPersResumen() {
		super();
		setFactorConversionMoneda(new BigDecimal(1));
		setImporte(new BigDecimal(0));
	}

}