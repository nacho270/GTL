package ar.com.fwcommon.util.xstream;

import java.io.Reader;

import com.thoughtworks.xstream.XStream;
/**
 * Clase útil para el sencillo manejo del paquete XStream para la serialización de
 * objetos a XML y viceversa (ver http://xstream.codehaus.org/).
 */
public class XStreamWrapper {

	private XStream xs;

	/**
	 * Método constructor.
	 */
	public XStreamWrapper() {
		xs = new XStream();
	}

	public XStreamWrapper(boolean noReferences) {
		xs = new XStream();
		if (noReferences)
			xs.setMode(XStream.NO_REFERENCES) ;
	}

	/**
	 * Serializa el objeto a XML.
	 * @param obj El objeto a serializar.
	 * @return Un String con el XML generado.
	 */
	public String serialize(Object obj) {
		return xs.toXML(obj);
	}

	/**
	 * Deserializa el XML a un objeto.
	 * @param xml El String con el XML.
	 * @return El objeto generado a partir del XML.
	 */
	public Object deserialize(String xml) {
		return xs.fromXML(xml);
	}

    /**
     * Deserializa el XML a un objeto.
     * @param xml El Reader con el archivo XML.
     * @return El objeto generado a partir del XML.
     */
    public Object deserialize(Reader xml) {
        return xs.fromXML(xml);
    }

	/**
	 * Mapea una clase con un alias o elemento XML.
	 * @param alias El alias de la clase.
	 * @param _class La clase a la que se le asigna el alias.
	 */
	public void alias(String alias, Class _class) {
		xs.alias(alias, _class);
	}

}