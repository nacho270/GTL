/**
 * SecuenciaODTTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

public class SecuenciaODTTO  implements java.io.Serializable {
    private java.lang.Integer id;

    private java.lang.Integer idCliente;

    private java.lang.Integer idTipoProducto;

    private java.lang.String nombre;

    private java.lang.Integer[] pasos;

    public SecuenciaODTTO() {
    }

    public SecuenciaODTTO(
           java.lang.Integer id,
           java.lang.Integer idCliente,
           java.lang.Integer idTipoProducto,
           java.lang.String nombre,
           java.lang.Integer[] pasos) {
           this.id = id;
           this.idCliente = idCliente;
           this.idTipoProducto = idTipoProducto;
           this.nombre = nombre;
           this.pasos = pasos;
    }


    /**
     * Gets the id value for this SecuenciaODTTO.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this SecuenciaODTTO.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the idCliente value for this SecuenciaODTTO.
     * 
     * @return idCliente
     */
    public java.lang.Integer getIdCliente() {
        return idCliente;
    }


    /**
     * Sets the idCliente value for this SecuenciaODTTO.
     * 
     * @param idCliente
     */
    public void setIdCliente(java.lang.Integer idCliente) {
        this.idCliente = idCliente;
    }


    /**
     * Gets the idTipoProducto value for this SecuenciaODTTO.
     * 
     * @return idTipoProducto
     */
    public java.lang.Integer getIdTipoProducto() {
        return idTipoProducto;
    }


    /**
     * Sets the idTipoProducto value for this SecuenciaODTTO.
     * 
     * @param idTipoProducto
     */
    public void setIdTipoProducto(java.lang.Integer idTipoProducto) {
        this.idTipoProducto = idTipoProducto;
    }


    /**
     * Gets the nombre value for this SecuenciaODTTO.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this SecuenciaODTTO.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the pasos value for this SecuenciaODTTO.
     * 
     * @return pasos
     */
    public java.lang.Integer[] getPasos() {
        return pasos;
    }


    /**
     * Sets the pasos value for this SecuenciaODTTO.
     * 
     * @param pasos
     */
    public void setPasos(java.lang.Integer[] pasos) {
        this.pasos = pasos;
    }

    public java.lang.Integer getPasos(int i) {
        return this.pasos[i];
    }

    public void setPasos(int i, java.lang.Integer _value) {
        this.pasos[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SecuenciaODTTO)) return false;
        SecuenciaODTTO other = (SecuenciaODTTO) obj;
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
            ((this.idCliente==null && other.getIdCliente()==null) || 
             (this.idCliente!=null &&
              this.idCliente.equals(other.getIdCliente()))) &&
            ((this.idTipoProducto==null && other.getIdTipoProducto()==null) || 
             (this.idTipoProducto!=null &&
              this.idTipoProducto.equals(other.getIdTipoProducto()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.pasos==null && other.getPasos()==null) || 
             (this.pasos!=null &&
              java.util.Arrays.equals(this.pasos, other.getPasos())));
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
        if (getIdCliente() != null) {
            _hashCode += getIdCliente().hashCode();
        }
        if (getIdTipoProducto() != null) {
            _hashCode += getIdTipoProducto().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getPasos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPasos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPasos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SecuenciaODTTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "secuenciaODTTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoProducto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTipoProducto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pasos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pasos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
