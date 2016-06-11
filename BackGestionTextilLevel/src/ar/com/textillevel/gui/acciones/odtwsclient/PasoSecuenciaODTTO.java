/**
 * PasoSecuenciaODTTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class PasoSecuenciaODTTO  implements java.io.Serializable {
    private java.lang.Integer id;

    private java.lang.Integer idProcedimiento;

    private java.lang.Integer idProceso;

    private java.lang.Integer idSector;

    private java.lang.String observaciones;

    public PasoSecuenciaODTTO() {
    }

    public PasoSecuenciaODTTO(
           java.lang.Integer id,
           java.lang.Integer idProcedimiento,
           java.lang.Integer idProceso,
           java.lang.Integer idSector,
           java.lang.String observaciones) {
           this.id = id;
           this.idProcedimiento = idProcedimiento;
           this.idProceso = idProceso;
           this.idSector = idSector;
           this.observaciones = observaciones;
    }


    /**
     * Gets the id value for this PasoSecuenciaODTTO.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this PasoSecuenciaODTTO.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the idProcedimiento value for this PasoSecuenciaODTTO.
     * 
     * @return idProcedimiento
     */
    public java.lang.Integer getIdProcedimiento() {
        return idProcedimiento;
    }


    /**
     * Sets the idProcedimiento value for this PasoSecuenciaODTTO.
     * 
     * @param idProcedimiento
     */
    public void setIdProcedimiento(java.lang.Integer idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }


    /**
     * Gets the idProceso value for this PasoSecuenciaODTTO.
     * 
     * @return idProceso
     */
    public java.lang.Integer getIdProceso() {
        return idProceso;
    }


    /**
     * Sets the idProceso value for this PasoSecuenciaODTTO.
     * 
     * @param idProceso
     */
    public void setIdProceso(java.lang.Integer idProceso) {
        this.idProceso = idProceso;
    }


    /**
     * Gets the idSector value for this PasoSecuenciaODTTO.
     * 
     * @return idSector
     */
    public java.lang.Integer getIdSector() {
        return idSector;
    }


    /**
     * Sets the idSector value for this PasoSecuenciaODTTO.
     * 
     * @param idSector
     */
    public void setIdSector(java.lang.Integer idSector) {
        this.idSector = idSector;
    }


    /**
     * Gets the observaciones value for this PasoSecuenciaODTTO.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this PasoSecuenciaODTTO.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PasoSecuenciaODTTO)) return false;
        PasoSecuenciaODTTO other = (PasoSecuenciaODTTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.idProcedimiento==null && other.getIdProcedimiento()==null) || 
             (this.idProcedimiento!=null &&
              this.idProcedimiento.equals(other.getIdProcedimiento()))) &&
            ((this.idProceso==null && other.getIdProceso()==null) || 
             (this.idProceso!=null &&
              this.idProceso.equals(other.getIdProceso()))) &&
            ((this.idSector==null && other.getIdSector()==null) || 
             (this.idSector!=null &&
              this.idSector.equals(other.getIdSector()))) &&
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIdProcedimiento() != null) {
            _hashCode += getIdProcedimiento().hashCode();
        }
        if (getIdProceso() != null) {
            _hashCode += getIdProceso().hashCode();
        }
        if (getIdSector() != null) {
            _hashCode += getIdSector().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PasoSecuenciaODTTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "pasoSecuenciaODTTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProcedimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idProcedimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProceso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idProceso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSector");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idSector"));
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
