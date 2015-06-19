/**
 * FERecuperaQTYResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente.responses;

@SuppressWarnings({"rawtypes","serial","unused"})
public class FERecuperaQTYResponse  implements java.io.Serializable {
    private ar.com.textillevel.modulos.fe.cliente.FERecuperaQTY qty;

    private ar.com.textillevel.modulos.fe.cliente.VError RError;

    public FERecuperaQTYResponse() {
    }

    public FERecuperaQTYResponse(
           ar.com.textillevel.modulos.fe.cliente.FERecuperaQTY qty,
           ar.com.textillevel.modulos.fe.cliente.VError RError) {
           this.qty = qty;
           this.RError = RError;
    }


    /**
     * Gets the qty value for this FERecuperaQTYResponse.
     * 
     * @return qty
     */
    public ar.com.textillevel.modulos.fe.cliente.FERecuperaQTY getQty() {
        return qty;
    }


    /**
     * Sets the qty value for this FERecuperaQTYResponse.
     * 
     * @param qty
     */
    public void setQty(ar.com.textillevel.modulos.fe.cliente.FERecuperaQTY qty) {
        this.qty = qty;
    }


    /**
     * Gets the RError value for this FERecuperaQTYResponse.
     * 
     * @return RError
     */
    public ar.com.textillevel.modulos.fe.cliente.VError getRError() {
        return RError;
    }


    /**
     * Sets the RError value for this FERecuperaQTYResponse.
     * 
     * @param RError
     */
    public void setRError(ar.com.textillevel.modulos.fe.cliente.VError RError) {
        this.RError = RError;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FERecuperaQTYResponse)) return false;
        FERecuperaQTYResponse other = (FERecuperaQTYResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.qty==null && other.getQty()==null) || 
             (this.qty!=null &&
              this.qty.equals(other.getQty()))) &&
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
        if (getQty() != null) {
            _hashCode += getQty().hashCode();
        }
        if (getRError() != null) {
            _hashCode += getRError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FERecuperaQTYResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FERecuperaQTYResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qty");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "qty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FERecuperaQTY"));
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
