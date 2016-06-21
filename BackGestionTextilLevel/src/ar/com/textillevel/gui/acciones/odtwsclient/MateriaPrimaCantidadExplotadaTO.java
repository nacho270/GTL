/**
 * MateriaPrimaCantidadExplotadaTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "rawtypes", "unused"})
public class MateriaPrimaCantidadExplotadaTO  implements java.io.Serializable {
    private java.lang.Float cantidadExplotada;

    private java.lang.Integer idMateriaPrimaCantidad;

    private java.lang.Integer idTipoArticulo;

    public MateriaPrimaCantidadExplotadaTO() {
    }

    public MateriaPrimaCantidadExplotadaTO(
           java.lang.Float cantidadExplotada,
           java.lang.Integer idMateriaPrimaCantidad,
           java.lang.Integer idTipoArticulo) {
           this.cantidadExplotada = cantidadExplotada;
           this.idMateriaPrimaCantidad = idMateriaPrimaCantidad;
           this.idTipoArticulo = idTipoArticulo;
    }


    /**
     * Gets the cantidadExplotada value for this MateriaPrimaCantidadExplotadaTO.
     * 
     * @return cantidadExplotada
     */
    public java.lang.Float getCantidadExplotada() {
        return cantidadExplotada;
    }


    /**
     * Sets the cantidadExplotada value for this MateriaPrimaCantidadExplotadaTO.
     * 
     * @param cantidadExplotada
     */
    public void setCantidadExplotada(java.lang.Float cantidadExplotada) {
        this.cantidadExplotada = cantidadExplotada;
    }


    /**
     * Gets the idMateriaPrimaCantidad value for this MateriaPrimaCantidadExplotadaTO.
     * 
     * @return idMateriaPrimaCantidad
     */
    public java.lang.Integer getIdMateriaPrimaCantidad() {
        return idMateriaPrimaCantidad;
    }


    /**
     * Sets the idMateriaPrimaCantidad value for this MateriaPrimaCantidadExplotadaTO.
     * 
     * @param idMateriaPrimaCantidad
     */
    public void setIdMateriaPrimaCantidad(java.lang.Integer idMateriaPrimaCantidad) {
        this.idMateriaPrimaCantidad = idMateriaPrimaCantidad;
    }


    /**
     * Gets the idTipoArticulo value for this MateriaPrimaCantidadExplotadaTO.
     * 
     * @return idTipoArticulo
     */
    public java.lang.Integer getIdTipoArticulo() {
        return idTipoArticulo;
    }


    /**
     * Sets the idTipoArticulo value for this MateriaPrimaCantidadExplotadaTO.
     * 
     * @param idTipoArticulo
     */
    public void setIdTipoArticulo(java.lang.Integer idTipoArticulo) {
        this.idTipoArticulo = idTipoArticulo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MateriaPrimaCantidadExplotadaTO)) return false;
        MateriaPrimaCantidadExplotadaTO other = (MateriaPrimaCantidadExplotadaTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cantidadExplotada==null && other.getCantidadExplotada()==null) || 
             (this.cantidadExplotada!=null &&
              this.cantidadExplotada.equals(other.getCantidadExplotada()))) &&
            ((this.idMateriaPrimaCantidad==null && other.getIdMateriaPrimaCantidad()==null) || 
             (this.idMateriaPrimaCantidad!=null &&
              this.idMateriaPrimaCantidad.equals(other.getIdMateriaPrimaCantidad()))) &&
            ((this.idTipoArticulo==null && other.getIdTipoArticulo()==null) || 
             (this.idTipoArticulo!=null &&
              this.idTipoArticulo.equals(other.getIdTipoArticulo())));
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
        if (getCantidadExplotada() != null) {
            _hashCode += getCantidadExplotada().hashCode();
        }
        if (getIdMateriaPrimaCantidad() != null) {
            _hashCode += getIdMateriaPrimaCantidad().hashCode();
        }
        if (getIdTipoArticulo() != null) {
            _hashCode += getIdTipoArticulo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MateriaPrimaCantidadExplotadaTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "materiaPrimaCantidadExplotadaTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cantidadExplotada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cantidadExplotada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idMateriaPrimaCantidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idMateriaPrimaCantidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoArticulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTipoArticulo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
