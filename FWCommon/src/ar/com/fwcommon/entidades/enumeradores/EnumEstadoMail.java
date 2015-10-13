package ar.com.fwcommon.entidades.enumeradores;

public class EnumEstadoMail {

    public static final int NO_ENVIADO = 0;
    public static final int ENVIADO = 1;
    public static final int FALLO_ENVIO = 2;
	public static String getDescripcion(int estado) {
		if (estado == NO_ENVIADO)
			return "No enviado";
		else if (estado == ENVIADO)
			return "Enviado";
		else if (estado == FALLO_ENVIO)
			return "Fallo en el envio";
		else
			return "Estado desconocido" ;
	}

}