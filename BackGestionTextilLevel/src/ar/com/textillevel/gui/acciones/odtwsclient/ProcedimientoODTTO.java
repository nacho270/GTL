/**
 * ProcedimientoODTTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class ProcedimientoODTTO  implements java.io.Serializable {
    private java.lang.Integer idTipoArticulo;

    private java.lang.String nombre;

    private ar.com.textillevel.gui.acciones.odtwsclient.InstruccionProcedimientoODTTO[] pasos;

    public ProcedimientoODTTO() {
    }

    public ProcedimientoODTTO(
           java.lang.Integer idTipoArticulo,
           java.lang.String nombre,
           ar.com.textillevel.gui.acciones.odtwsclient.InstruccionProcedimientoODTTO[] pasos) {
           this.idTipoArticulo = idTipoArticulo;
           this.nombre = nombre;
           this.pasos = pasos;
    }


    /**
     * Gets the idTipoArticulo value for this ProcedimientoODTTO.
     * 
     * @return idTipoArticulo
     */
    public java.lang.Integer getIdTipoArticulo() {
        return idTipoArticulo;
    }


    /**
     * Sets the idTipoArticulo value for this ProcedimientoODTTO.
     * 
     * @param idTipoArticulo
     */
    public void setIdTipoArticulo(java.lang.Integer idTipoArticulo) {
        this.idTipoArticulo = idTipoArticulo;
    }


    /**
     * Gets the nombre value for this ProcedimientoODTTO.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this ProcedimientoODTTO.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the pasos value for this ProcedimientoODTTO.
     * 
     * @return pasos
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.InstruccionProcedimientoODTTO[] getPasos() {
        return pasos;
    }


    /**
     * Sets the pasos value for this ProcedimientoODTTO.
     * 
     * @param pasos
     */
    public void setPasos(ar.com.textillevel.gui.acciones.odtwsclient.InstruccionProcedimientoODTTO[] pasos) {
        this.pasos = pasos;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.InstruccionProcedimientoODTTO getPasos(int i) {
        return this.pasos[i];
    }

    public void setPasos(int i, ar.com.textillevel.gui.acciones.odtwsclient.InstruccionProcedimientoODTTO _value) {
        this.pasos[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProcedimientoODTTO)) return false;
        ProcedimientoODTTO other = (ProcedimientoODTTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idTipoArticulo==null && other.getIdTipoArticulo()==null) || 
             (this.idTipoArticulo!=null &&
              this.idTipoArticulo.equals(other.getIdTipoArticulo()))) &&
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
        if (getIdTipoArticulo() != null) {
            _hashCode += getIdTipoArticulo().hashCode();
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
        new org.apache.axis.description.TypeDesc(ProcedimientoODTTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "procedimientoODTTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoArticulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTipoArticulo"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "instruccionProcedimientoODTTO"));
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
