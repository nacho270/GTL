package main.servicios.alertas;

public interface AccionNotificacion<T> {

	String getTitulo();
	boolean ejecutar(T param);
}
