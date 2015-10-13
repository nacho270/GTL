package ar.com.fwcommon.util.jasper;

import java.util.Collection;
import java.util.Map;
/**
 * Clase útil para la configuración de JasperWrapper.
 * ar.com.fwcommon.util.jasper.JasperWrapper
 */
public class JasperWrapperProperties {

    private String xmlReport;
    private Map parameters;
    private Collection data;

    /**
     * Devuelve el reporte en formato XML.
     * @return xmlReport
     */
    public String getXmlReport() {
        return xmlReport;
    }

    /**
     * Setea el reporte en formato XML.
     * @param xmlReport
     */
    public void setXmlReport(String xmlReport) {
        this.xmlReport = xmlReport;
    }

    /**
     * Devuelve los parámetros.
     * @return parameters
     */
    public Map getParameters() {
        return parameters;
    }

    /**
     * Setea los parámetros.
     * @param parameters
     */
    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    /**
     * Devuelve los datos.
     * @return
     */
    public Collection getData() {
        return data;
    }

    /**
     * Setea los datos. 
     * @param data
     */
    public void setData(Collection data) {
        this.data = data;
    }

}