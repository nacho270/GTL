package ar.com.textillevel.entidades.cuenta;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.com.textillevel.entidades.gente.Persona;

@Entity
@DiscriminatorValue(value = "CPERS")
public class CuentaPersona extends Cuenta{

	private static final long serialVersionUID = 6181843657383833408L;
	
	private Persona persona;

	@ManyToOne
	@JoinColumn(name="F_PERSONA_P_ID", nullable=true)
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
}
