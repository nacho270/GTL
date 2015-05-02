package ar.com.textillevel.gui.util;

public enum ESkin {
	ROJO ("ar/com/textillevel/skin/sgpthemepack_rojo.zip"),
	AZUL ("ar/com/textillevel/skin/sgpthemepack_azul.zip");
	
	private ESkin(String path){
		this.path = path;
	}
	
	private String path;

	
	public String getPath() {
		return path;
	}

	
	public void setPath(String path) {
		this.path = path;
	}
}
