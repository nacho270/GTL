package ar.com.textillevel.excepciones;

import java.io.Serializable;

import ar.clarin.fwjava.componentes.error.validaciones.InfoValidacionAbstract;

public class InfoValidacionGTL extends InfoValidacionAbstract implements Serializable{
	
	private static final long serialVersionUID = 6691608665171945096L;

	public  InfoValidacionGTL(int codigo, String mensaje) {
		super(codigo,mensaje);
	}
}