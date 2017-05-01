package main.servicios.alertas;

public interface AccionNotificacion<T> {

	void ejecutar(T param);
}
