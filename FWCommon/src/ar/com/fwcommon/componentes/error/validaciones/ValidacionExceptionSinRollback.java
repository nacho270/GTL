package ar.com.fwcommon.componentes.error.validaciones;

import java.util.Arrays;
import java.util.List;

import javax.ejb.ApplicationException;


@ApplicationException(rollback=false)
public class ValidacionExceptionSinRollback extends Exception {

	private static final long serialVersionUID = 7130278304609471561L;
	
	private Integer codigoError;
	private String mensajeError;
	
	public ValidacionExceptionSinRollback(Integer codigoError, String mensajeError) {
		super();
		this.codigoError = codigoError;
		this.mensajeError = mensajeError;
	}

	public ValidacionExceptionSinRollback(InfoValidacionAbstract enumException) {
		super();
		this.codigoError = enumException.getCodigo();
		this.mensajeError = enumException.getMensaje();
	}	
	public ValidacionExceptionSinRollback(InfoValidacionAbstract enumException, List<String> valoresParametros) {
		super();
		this.codigoError = enumException.getCodigo();
		this.mensajeError = parser(enumException.getMensaje(), valoresParametros);
	}

	public ValidacionExceptionSinRollback(InfoValidacionAbstract enumException, String[] valoresParametros) {
		super();
		this.codigoError = enumException.getCodigo();
		this.mensajeError = parser(enumException.getMensaje(), Arrays.asList(valoresParametros));
	}	

	public String getMensajeError() {
		return mensajeError;
	}

	public Integer getCodigoError() {
		return codigoError;
	}
	
	public String getMessage (){
		return this.mensajeError;
	}

	private String parser(String mensaje, List<String> valores){
		if(valores!=null){
			for (int i = 0; i < valores.size(); i++) {
				mensaje = mensaje.replaceAll("\\{"+i+"\\}", valores.get(i));
			}
		}
		return mensaje;
	}
}
