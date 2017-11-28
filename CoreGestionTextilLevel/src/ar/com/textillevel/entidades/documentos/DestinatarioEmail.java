package ar.com.textillevel.entidades.documentos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_DESTINATARIO_EMAIL")
public class DestinatarioEmail implements Serializable {

	private static final long serialVersionUID = 1L;

	public String email;

	@Id
	@Column(name = "A_EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return getEmail();
	}

}
