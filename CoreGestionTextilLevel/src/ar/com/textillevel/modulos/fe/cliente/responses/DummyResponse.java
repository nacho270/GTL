/**
 * DummyResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente.responses;

@SuppressWarnings({"rawtypes","serial","unused"})
public class DummyResponse  implements java.io.Serializable {
    private java.lang.String appserver;

    private java.lang.String dbserver;

    private java.lang.String authserver;

    public DummyResponse() {
    }

    public DummyResponse(
           java.lang.String appserver,
           java.lang.String dbserver,
           java.lang.String authserver) {
           this.appserver = appserver;
           this.dbserver = dbserver;
           this.authserver = authserver;
    }


    /**
     * Gets the appserver value for this DummyResponse.
     * 
     * @return appserver
     */
    public java.lang.String getAppserver() {
        return appserver;
    }


    /**
     * Sets the appserver value for this DummyResponse.
     * 
     * @param appserver
     */
    public void setAppserver(java.lang.String appserver) {
        this.appserver = appserver;
    }


    /**
     * Gets the dbserver value for this DummyResponse.
     * 
     * @return dbserver
     */
    public java.lang.String getDbserver() {
        return dbserver;
    }


    /**
     * Sets the dbserver value for this DummyResponse.
     * 
     * @param dbserver
     */
    public void setDbserver(java.lang.String dbserver) {
        this.dbserver = dbserver;
    }


    /**
     * Gets the authserver value for this DummyResponse.
     * 
     * @return authserver
     */
    public java.lang.String getAuthserver() {
        return authserver;
    }


    /**
     * Sets the authserver value for this DummyResponse.
     * 
     * @param authserver
     */
    public void setAuthserver(java.lang.String authserver) {
        this.authserver = authserver;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DummyResponse)) return false;
        DummyResponse other = (DummyResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.appserver==null && other.getAppserver()==null) || 
             (this.appserver!=null &&
              this.appserver.equals(other.getAppserver()))) &&
            ((this.dbserver==null && other.getDbserver()==null) || 
             (this.dbserver!=null &&
              this.dbserver.equals(other.getDbserver()))) &&
            ((this.authserver==null && other.getAuthserver()==null) || 
             (this.authserver!=null &&
              this.authserver.equals(other.getAuthserver())));
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
        if (getAppserver() != null) {
            _hashCode += getAppserver().hashCode();
        }
        if (getDbserver() != null) {
            _hashCode += getDbserver().hashCode();
        }
        if (getAuthserver() != null) {
            _hashCode += getAuthserver().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DummyResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "DummyResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appserver");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "appserver"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dbserver");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "dbserver"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authserver");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "authserver"));
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
