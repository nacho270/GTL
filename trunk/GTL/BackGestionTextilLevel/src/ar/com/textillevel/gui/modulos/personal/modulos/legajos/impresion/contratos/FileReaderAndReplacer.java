package ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos;

public class FileReaderAndReplacer {

	private String contenidoArchivo;

	public FileReaderAndReplacer(String path) throws Exception {
		setContenidoArchivo(new FileReaderWrapper(path).obtenerContenido());
	}

	public String getContenidoArchivo() {
		return contenidoArchivo;
	}

	public void setContenidoArchivo(String contenidoArchivo) {
		this.contenidoArchivo = contenidoArchivo;
	}

	public void reemplazarComodin(EComodinesContrato comodin, String texto) {
		setContenidoArchivo(getContenidoArchivo().replaceAll(comodin.getComodin(), texto));
	}
	
	public static void main(String[] args){
		try {
			FileReaderAndReplacer fileReaderAndReplacer = new FileReaderAndReplacer("ar/com/textillevel/reportes/contratos/aprueba/primero.txt");
			fileReaderAndReplacer.reemplazarComodin(EComodinesContrato.NOMBRE_EMPLEADO, "nachooo");
			System.out.println(fileReaderAndReplacer.getContenidoArchivo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
