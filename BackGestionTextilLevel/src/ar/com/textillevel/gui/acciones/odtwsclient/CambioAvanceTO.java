/**
 * CambioAvanceTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class CambioAvanceTO  implements java.io.Serializable {
    private java.lang.Long fechaHora;

    private java.lang.Byte idAvance;

    private java.lang.Integer idTerminal;

    private java.lang.Integer idUsuarioSistema;

    private java.lang.String observaciones;

    public CambioAvanceTO() {
    }

    public CambioAvanceTO(
           java.lang.Long fechaHora,
           java.lang.Byte idAvance,
           java.lang.Integer idTerminal,
           java.lang.Integer idUsuarioSistema,
           java.lang.String observaciones) {
           this.fechaHora = fechaHora;
           this.idAvance = idAvance;
           this.idTerminal = idTerminal;
           this.idUsuarioSistema = idUsuarioSistema;
           this.observaciones = observaciones;
    }


    /**
     * Gets the fechaHora value for this CambioAvanceTO.
     * 
     * @return fechaHora
     */
    public java.lang.Long getFechaHora() {
        return fechaHora;
    }


    /**
     * Sets the fechaHora value for this CambioAvanceTO.
     * 
     * @param fechaHora
     */
    public void setFechaHora(java.lang.Long fechaHora) {
        this.fechaHora = fechaHora;
    }


    /**
     * Gets the idAvance value for this CambioAvanceTO.
     * 
     * @return idAvance
     */
    public java.lang.Byte getIdAvance() {
        return idAvance;
    }


    /**
     * Sets the idAvance value for this CambioAvanceTO.
     * 
     * @param idAvance
     */
    public void setIdAvance(java.lang.Byte idAvance) {
        this.idAvance = idAvance;
    }


    /**
     * Gets the idTerminal value for this CambioAvanceTO.
     * 
     * @return idTerminal
     */
    public java.lang.Integer getIdTerminal() {
        return idTerminal;
    }


    /**
     * Sets the idTerminal value for this CambioAvanceTO.
     * 
     * @param idTerminal
     */
    public void setIdTerminal(java.lang.Integer idTerminal) {
        this.idTerminal = idTerminal;
    }


    /**
     * Gets the idUsuarioSistema value for this CambioAvanceTO.
     * 
     * @return idUsuarioSistema
     */
    public java.lang.Integer getIdUsuarioSistema() {
        return idUsuarioSistema;
    }


    /**
     * Sets the idUsuarioSistema value for this CambioAvanceTO.
     * 
     * @param idUsuarioSistema
     */
    public void setIdUsuarioSistema(java.lang.Integer idUsuarioSistema) {
        this.idUsuarioSistema = idUsuarioSistema;
    }


    /**
     * Gets the observaciones value for this CambioAvanceTO.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this CambioAvanceTO.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CambioAvanceTO)) return false;
        CambioAvanceTO other = (CambioAvanceTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fechaHora==null && other.getFechaHora()==null) || 
             (this.fechaHora!=null &&
              this.fechaHora.equals(other.getFechaHora()))) &&
            ((this.idAvance==null && other.getIdAvance()==null) || 
             (this.idAvance!=null &&
              this.idAvance.equals(other.getIdAvance()))) &&
            ((this.idTerminal==null && other.getIdTerminal()==null) || 
             (this.idTerminal!=null &&
              this.idTerminal.equals(other.getIdTerminal()))) &&
            ((this.idUsuarioSistema==null && other.getIdUsuarioSistema()==null) || 
             (this.idUsuarioSistema!=null &&
              this.idUsuarioSistema.equals(other.getIdUsuarioSistema()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getFechaHora() != null) {
            _hashCode += getFechaHora().hashCode();
        }
        if (getIdAvance() != null) {
            _hashCode += getIdAvance().hashCode();
        }
        if (getIdTerminal() != null) {
            _hashCode += getIdTerminal().hashCode();
        }
        if (getIdUsuarioSistema() != null) {
            _hashCode += getIdUsuarioSistema().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CambioAvanceTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "cambioAvanceTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaHora");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaHora"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAvance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAvance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "byte"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTerminal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTerminal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUsuarioSistema");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idUsuarioSistema"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
