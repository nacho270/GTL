package ar.com.fwcommon.componentes;

import java.util.EventObject;

public class InvalidTextFormatEvent extends EventObject {
	private static final long serialVersionUID = -7320552654117505232L;

	public InvalidTextFormatEvent(Object source) {
		super(source);
	}
}
