package ar.com.fwcommon.boss;

public class ElementoListado {

	Object campo1;
	Object campo2;
	
	public ElementoListado(Object campo1) {
		this.campo1 = campo1;
	}
	
	public ElementoListado(Object campo1, Object campo2) {
		this.campo1 = campo1;
		this.campo2 = campo2;
	}
	
	public Object getCampo1() {
		return campo1;
	}
	
	public void setCampo1(Object campo1) {
		this.campo1 = campo1;
	}
	
	public Object getCampo2() {
		return campo2;
	}
	
	public void setCampo2(Object campo2) {
		this.campo2 = campo2;
	}

}
