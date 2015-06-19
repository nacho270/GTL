/**
 * FERecuperaLastCMPResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente.responses;

@SuppressWarnings({"rawtypes","serial","unused"})
public class FERecuperaLastCMPResponse  implements java.io.Serializable {
    private int cbte_nro;

    private ar.com.textillevel.modulos.fe.cliente.VError RError;

    public FERecuperaLastCMPResponse() {
    }

    public FERecuperaLastCMPResponse(
           int cbte_nro,
           ar.com.textillevel.modulos.fe.cliente.VError RError) {
           this.cbte_nro = cbte_nro;
           this.RError = RError;
    }


    /**
     * Gets the cbte_nro value for this FERecuperaLastCMPResponse.
     * 
     * @return cbte_nro
     */
    public int getCbte_nro() {
        return cbte_nro;
    }


    /**
     * Sets the cbte_nro value for this FERecuperaLastCMPResponse.
     * 
     * @param cbte_nro
     */
    public void setCbte_nro(int cbte_nro) {
        this.cbte_nro = cbte_nro;
    }


    /**
     * Gets the RError value for this FERecuperaLastCMPResponse.
     * 
     * @return RError
     */
    public ar.com.textillevel.modulos.fe.cliente.VError getRError() {
        return RError;
    }


    /**
     * Sets the RError value for this FERecuperaLastCMPResponse.
     * 
     * @param RError
     */
    public void setRError(ar.com.textillevel.modulos.fe.cliente.VError RError) {
        this.RError = RError;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FERecuperaLastCMPResponse)) return false;
        FERecuperaLastCMPResponse other = (FERecuperaLastCMPResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.cbte_nro == other.getCbte_nro() &&
            ((this.RError==null && other.getRError()==null) || 
             (this.RError!=null &&
              this.RError.equals(other.getRError())));
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
        _hashCode += getCbte_nro();
        if (getRError() != null) {
            _hashCode += getRError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FERecuperaLastCMPResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FERecuperaLastCMPResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cbte_nro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "cbte_nro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RError");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "RError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "vError"));
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
