package ar.com.textillevel.gui.util.controles.intellisense;

import java.util.EventListener;

public interface TeclaArribaAbajoEventListener extends EventListener {
	public void onTeclaArriba(TeclaArribaAbajoEventData data);
	public void onTeclaAbajo(TeclaArribaAbajoEventData data);
}
