/**
 * FEUltNroResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente.responses;

@SuppressWarnings({"rawtypes","serial","unused"})
public class FEUltNroResponse  implements java.io.Serializable {
    private ar.com.textillevel.modulos.fe.cliente.responses.UltNroResponse nro;

    private ar.com.textillevel.modulos.fe.cliente.VError RError;

    public FEUltNroResponse() {
    }

    public FEUltNroResponse(
           ar.com.textillevel.modulos.fe.cliente.responses.UltNroResponse nro,
           ar.com.textillevel.modulos.fe.cliente.VError RError) {
           this.nro = nro;
           this.RError = RError;
    }


    /**
     * Gets the nro value for this FEUltNroResponse.
     * 
     * @return nro
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.UltNroResponse getNro() {
        return nro;
    }


    /**
     * Sets the nro value for this FEUltNroResponse.
     * 
     * @param nro
     */
    public void setNro(ar.com.textillevel.modulos.fe.cliente.responses.UltNroResponse nro) {
        this.nro = nro;
    }


    /**
     * Gets the RError value for this FEUltNroResponse.
     * 
     * @return RError
     */
    public ar.com.textillevel.modulos.fe.cliente.VError getRError() {
        return RError;
    }


    /**
     * Sets the RError value for this FEUltNroResponse.
     * 
     * @param RError
     */
    public void setRError(ar.com.textillevel.modulos.fe.cliente.VError RError) {
        this.RError = RError;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FEUltNroResponse)) return false;
        FEUltNroResponse other = (FEUltNroResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nro==null && other.getNro()==null) || 
             (this.nro!=null &&
              this.nro.equals(other.getNro()))) &&
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
        if (getNro() != null) {
            _hashCode += getNro().hashCode();
        }
        if (getRError() != null) {
            _hashCode += getRError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FEUltNroResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FEUltNroResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "nro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "UltNroResponse"));
        elemField.setMinOccurs(0);
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
