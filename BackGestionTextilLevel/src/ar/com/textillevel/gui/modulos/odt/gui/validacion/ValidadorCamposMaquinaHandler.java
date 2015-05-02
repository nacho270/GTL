package ar.com.textillevel.gui.modulos.odt.gui.validacion;

import java.util.ArrayList;
import java.util.List;

public class ValidadorCamposMaquinaHandler {

	private List<Validacion> validaciones;

	public ValidadorCamposMaquinaHandler() {
		this.validaciones  = new ArrayList<Validacion>();
	}

	private List<Validacion> getValidaciones() {
		return validaciones;
	}

	public void addValidacion(Validacion validacion) {
		getValidaciones().add(validacion);
	}

	public boolean validar() {
		for(Validacion v : getValidaciones()) {
			if(!v.validate()) {
				return false;
			}
		}
		return true;
	}

}
