/**
 * TransicionODTTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class TransicionODTTO  implements java.io.Serializable {
    private ar.com.textillevel.gui.acciones.odtwsclient.CambioAvanceTO[] cambiosAvance;

    private java.lang.Long fechaHoraRegistro;

    private java.lang.Integer idMaquina;

    private java.lang.Integer idTipoMaquina;

    private java.lang.Integer idUsuarioSistema;

    public TransicionODTTO() {
    }

    public TransicionODTTO(
           ar.com.textillevel.gui.acciones.odtwsclient.CambioAvanceTO[] cambiosAvance,
           java.lang.Long fechaHoraRegistro,
           java.lang.Integer idMaquina,
           java.lang.Integer idTipoMaquina,
           java.lang.Integer idUsuarioSistema) {
           this.cambiosAvance = cambiosAvance;
           this.fechaHoraRegistro = fechaHoraRegistro;
           this.idMaquina = idMaquina;
           this.idTipoMaquina = idTipoMaquina;
           this.idUsuarioSistema = idUsuarioSistema;
    }


    /**
     * Gets the cambiosAvance value for this TransicionODTTO.
     * 
     * @return cambiosAvance
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.CambioAvanceTO[] getCambiosAvance() {
        return cambiosAvance;
    }


    /**
     * Sets the cambiosAvance value for this TransicionODTTO.
     * 
     * @param cambiosAvance
     */
    public void setCambiosAvance(ar.com.textillevel.gui.acciones.odtwsclient.CambioAvanceTO[] cambiosAvance) {
        this.cambiosAvance = cambiosAvance;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.CambioAvanceTO getCambiosAvance(int i) {
        return this.cambiosAvance[i];
    }

    public void setCambiosAvance(int i, ar.com.textillevel.gui.acciones.odtwsclient.CambioAvanceTO _value) {
        this.cambiosAvance[i] = _value;
    }


    /**
     * Gets the fechaHoraRegistro value for this TransicionODTTO.
     * 
     * @return fechaHoraRegistro
     */
    public java.lang.Long getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }


    /**
     * Sets the fechaHoraRegistro value for this TransicionODTTO.
     * 
     * @param fechaHoraRegistro
     */
    public void setFechaHoraRegistro(java.lang.Long fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }


    /**
     * Gets the idMaquina value for this TransicionODTTO.
     * 
     * @return idMaquina
     */
    public java.lang.Integer getIdMaquina() {
        return idMaquina;
    }


    /**
     * Sets the idMaquina value for this TransicionODTTO.
     * 
     * @param idMaquina
     */
    public void setIdMaquina(java.lang.Integer idMaquina) {
        this.idMaquina = idMaquina;
    }


    /**
     * Gets the idTipoMaquina value for this TransicionODTTO.
     * 
     * @return idTipoMaquina
     */
    public java.lang.Integer getIdTipoMaquina() {
        return idTipoMaquina;
    }


    /**
     * Sets the idTipoMaquina value for this TransicionODTTO.
     * 
     * @param idTipoMaquina
     */
    public void setIdTipoMaquina(java.lang.Integer idTipoMaquina) {
        this.idTipoMaquina = idTipoMaquina;
    }


    /**
     * Gets the idUsuarioSistema value for this TransicionODTTO.
     * 
     * @return idUsuarioSistema
     */
    public java.lang.Integer getIdUsuarioSistema() {
        return idUsuarioSistema;
    }


    /**
     * Sets the idUsuarioSistema value for this TransicionODTTO.
     * 
     * @param idUsuarioSistema
     */
    public void setIdUsuarioSistema(java.lang.Integer idUsuarioSistema) {
        this.idUsuarioSistema = idUsuarioSistema;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransicionODTTO)) return false;
        TransicionODTTO other = (TransicionODTTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cambiosAvance==null && other.getCambiosAvance()==null) || 
             (this.cambiosAvance!=null &&
              java.util.Arrays.equals(this.cambiosAvance, other.getCambiosAvance()))) &&
            ((this.fechaHoraRegistro==null && other.getFechaHoraRegistro()==null) || 
             (this.fechaHoraRegistro!=null &&
              this.fechaHoraRegistro.equals(other.getFechaHoraRegistro()))) &&
            ((this.idMaquina==null && other.getIdMaquina()==null) || 
             (this.idMaquina!=null &&
              this.idMaquina.equals(other.getIdMaquina()))) &&
            ((this.idTipoMaquina==null && other.getIdTipoMaquina()==null) || 
             (this.idTipoMaquina!=null &&
              this.idTipoMaquina.equals(other.getIdTipoMaquina()))) &&
            ((this.idUsuarioSistema==null && other.getIdUsuarioSistema()==null) || 
             (this.idUsuarioSistema!=null &&
              this.idUsuarioSistema.equals(other.getIdUsuarioSistema())));
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
        if (getCambiosAvance() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCambiosAvance());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCambiosAvance(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getFechaHoraRegistro() != null) {
            _hashCode += getFechaHoraRegistro().hashCode();
        }
        if (getIdMaquina() != null) {
            _hashCode += getIdMaquina().hashCode();
        }
        if (getIdTipoMaquina() != null) {
            _hashCode += getIdTipoMaquina().hashCode();
        }
        if (getIdUsuarioSistema() != null) {
            _hashCode += getIdUsuarioSistema().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransicionODTTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "transicionODTTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cambiosAvance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cambiosAvance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "cambioAvanceTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaHoraRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaHoraRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idMaquina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idMaquina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoMaquina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTipoMaquina"));
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
