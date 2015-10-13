package ar.com.fwcommon.componentes.error.validaciones;

import java.io.Serializable;

public abstract class InfoValidacionAbstract implements Serializable
{
	private int codigo;
	private String mensaje;
	public  InfoValidacionAbstract(int codigo, String mensaje) {
		this.codigo = codigo;
		this.mensaje = mensaje;
	}
	
	public int getCodigo()
	{
		return codigo;
	}
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}
	public String getMensaje()
	{
		return mensaje;
	}
	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}
}
