package main.statusbar;

public class ConfiguracionComponenteStatusBar {

	private boolean repetible;
	private long tiempoRepeticion;

	public ConfiguracionComponenteStatusBar(boolean repetible, long tiempoRepeticion) {
		this.repetible = repetible;
		this.tiempoRepeticion = tiempoRepeticion;
	}

	public boolean isRepetible() {
		return repetible;
	}

	public void setRepetible(boolean repetible) {
		this.repetible = repetible;
	}

	public long getTiempoRepeticion() {
		return tiempoRepeticion;
	}

	public void setTiempoRepeticion(long tiempoRepeticion) {
		this.tiempoRepeticion = tiempoRepeticion;
	}
}
