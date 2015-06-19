/**
 * FERequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente.requests;

@SuppressWarnings({"rawtypes","serial","unused"})
public class FERequest  implements java.io.Serializable {
    private ar.com.textillevel.modulos.fe.cliente.requests.FECabeceraRequest fecr;

    private ar.com.textillevel.modulos.fe.cliente.requests.FEDetalleRequest[] fedr;

    public FERequest() {
    }

    public FERequest(
    	   ar.com.textillevel.modulos.fe.cliente.requests.FECabeceraRequest fecr,
    	   ar.com.textillevel.modulos.fe.cliente.requests.FEDetalleRequest[] fedr) {
           this.fecr = fecr;
           this.fedr = fedr;
    }


    /**
     * Gets the fecr value for this FERequest.
     * 
     * @return fecr
     */
    public ar.com.textillevel.modulos.fe.cliente.requests.FECabeceraRequest getFecr() {
        return fecr;
    }


    /**
     * Sets the fecr value for this FERequest.
     * 
     * @param fecr
     */
    public void setFecr(ar.com.textillevel.modulos.fe.cliente.requests.FECabeceraRequest fecr) {
        this.fecr = fecr;
    }


    /**
     * Gets the fedr value for this FERequest.
     * 
     * @return fedr
     */
    public ar.com.textillevel.modulos.fe.cliente.requests.FEDetalleRequest[] getFedr() {
        return fedr;
    }


    /**
     * Sets the fedr value for this FERequest.
     * 
     * @param fedr
     */
    public void setFedr(ar.com.textillevel.modulos.fe.cliente.requests.FEDetalleRequest[] fedr) {
        this.fedr = fedr;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FERequest)) return false;
        FERequest other = (FERequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fecr==null && other.getFecr()==null) || 
             (this.fecr!=null &&
              this.fecr.equals(other.getFecr()))) &&
            ((this.fedr==null && other.getFedr()==null) || 
             (this.fedr!=null &&
              java.util.Arrays.equals(this.fedr, other.getFedr())));
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
        if (getFecr() != null) {
            _hashCode += getFecr().hashCode();
        }
        if (getFedr() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFedr());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFedr(), i);
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
        new org.apache.axis.description.TypeDesc(FERequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FERequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "Fecr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FECabeceraRequest"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fedr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "Fedr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FEDetalleRequest"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FEDetalleRequest"));
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
