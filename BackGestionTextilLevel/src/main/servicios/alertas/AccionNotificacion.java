package main.servicios.alertas;

public interface AccionNotificacion<T> {

	String getTitulo();
	void ejecutar(T param);
}
