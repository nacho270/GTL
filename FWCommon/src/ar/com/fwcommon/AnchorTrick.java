package ar.com.fwcommon;
/**
 * Truco del Ancla.
 * Clase útil para la carga de recursos (archivos) a través de Java Web Start.
 * Ej.: Carga de una imágen
 * 
 * ClassLoader cl = new AnchorTrick().getClass().getClassLoader();
 * ImageIcon imagen;
 * try {
 * 		imagen = new ImageIcon(cl.getResource("C:/icono.png"));
 * } catch(Exception e) {
 * 		e.printStackTrace();
 * }
 * 
 * @version 1.0
 */
public class AnchorTrick {

	public AnchorTrick() {
	}

}