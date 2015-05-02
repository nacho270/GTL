package ar.com.textillevel.mobile.modules.cuenta.common;

public interface MovimientoRenderer {

	public int getTextColorDebe();
	public int getTextColorHaber();
	public int getTextColorSaldoParcial(Float saldoParcial);
	public String getStrMontoHaber(Float monto);
	public String getStrMontoDebe(Float monto);

}
