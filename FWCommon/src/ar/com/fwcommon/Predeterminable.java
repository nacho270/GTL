package ar.com.fwcommon;

public interface Predeterminable {

	/**
	 * Método que permite setear el estado de <b>predeterminado</b>.
	 * @param predeterminado
	 */
	void setPredeterminado(boolean predeterminado);

	/**
	 * @return <b>true</b> si el estado es <b>predeterminado</b>.
	 */
	boolean isPredeterminado();

}