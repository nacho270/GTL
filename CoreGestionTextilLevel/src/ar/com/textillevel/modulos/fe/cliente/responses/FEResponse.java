/**
 * FEResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente.responses;

@SuppressWarnings({"rawtypes","serial","unused"})
public class FEResponse  implements java.io.Serializable {
    private ar.com.textillevel.modulos.fe.cliente.responses.FECabeceraResponse fecResp;

    private ar.com.textillevel.modulos.fe.cliente.responses.FEDetalleResponse[] fedResp;

    private ar.com.textillevel.modulos.fe.cliente.VError RError;

    public FEResponse() {
    }

    public FEResponse(
           ar.com.textillevel.modulos.fe.cliente.responses.FECabeceraResponse fecResp,
           ar.com.textillevel.modulos.fe.cliente.responses.FEDetalleResponse[] fedResp,
           ar.com.textillevel.modulos.fe.cliente.VError RError) {
           this.fecResp = fecResp;
           this.fedResp = fedResp;
           this.RError = RError;
    }


    /**
     * Gets the fecResp value for this FEResponse.
     * 
     * @return fecResp
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FECabeceraResponse getFecResp() {
        return fecResp;
    }


    /**
     * Sets the fecResp value for this FEResponse.
     * 
     * @param fecResp
     */
    public void setFecResp(ar.com.textillevel.modulos.fe.cliente.responses.FECabeceraResponse fecResp) {
        this.fecResp = fecResp;
    }


    /**
     * Gets the fedResp value for this FEResponse.
     * 
     * @return fedResp
     */
    public ar.com.textillevel.modulos.fe.cliente.responses.FEDetalleResponse[] getFedResp() {
        return fedResp;
    }


    /**
     * Sets the fedResp value for this FEResponse.
     * 
     * @param fedResp
     */
    public void setFedResp(ar.com.textillevel.modulos.fe.cliente.responses.FEDetalleResponse[] fedResp) {
        this.fedResp = fedResp;
    }


    /**
     * Gets the RError value for this FEResponse.
     * 
     * @return RError
     */
    public ar.com.textillevel.modulos.fe.cliente.VError getRError() {
        return RError;
    }


    /**
     * Sets the RError value for this FEResponse.
     * 
     * @param RError
     */
    public void setRError(ar.com.textillevel.modulos.fe.cliente.VError RError) {
        this.RError = RError;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FEResponse)) return false;
        FEResponse other = (FEResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fecResp==null && other.getFecResp()==null) || 
             (this.fecResp!=null &&
              this.fecResp.equals(other.getFecResp()))) &&
            ((this.fedResp==null && other.getFedResp()==null) || 
             (this.fedResp!=null &&
              java.util.Arrays.equals(this.fedResp, other.getFedResp()))) &&
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
        if (getFecResp() != null) {
            _hashCode += getFecResp().hashCode();
        }
        if (getFedResp() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFedResp());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFedResp(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRError() != null) {
            _hashCode += getRError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FEResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FEResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecResp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FecResp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FECabeceraResponse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fedResp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FedResp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FEDetalleResponse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FEDetalleResponse"));
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
