package ar.com.fwcommon.componentes;

import java.util.EventListener;

/**
 * Clase que define que el formato que se está ingresando no es válido
 * 
 * 
 */
public interface InvalidTextFormatListener extends EventListener {
	public void invalidTextFormat(InvalidTextFormatEvent e);
}
