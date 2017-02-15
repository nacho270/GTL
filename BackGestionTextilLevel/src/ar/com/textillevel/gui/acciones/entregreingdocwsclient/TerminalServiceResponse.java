package ar.com.textillevel.gui.acciones.entregreingdocwsclient;

@SuppressWarnings({ "rawtypes", "unused", "serial" })
public class TerminalServiceResponse implements java.io.Serializable {
    private int codigoError;

    private boolean error;

    private java.lang.String mensajeError;

    public TerminalServiceResponse() {
    }

    public TerminalServiceResponse(int codigoError, boolean error, java.lang.String mensajeError) {
        this.codigoError = codigoError;
        this.error = error;
        this.mensajeError = mensajeError;
    }

    /**
     * Gets the codigoError value for this TerminalServiceResponse.
     *
     * @return codigoError
     */
    public int getCodigoError() {
        return codigoError;
    }

    /**
     * Sets the codigoError value for this TerminalServiceResponse.
     *
     * @param codigoError
     */
    public void setCodigoError(int codigoError) {
        this.codigoError = codigoError;
    }

    /**
     * Gets the error value for this TerminalServiceResponse.
     *
     * @return error
     */
    public boolean isError() {
        return error;
    }

    /**
     * Sets the error value for this TerminalServiceResponse.
     *
     * @param error
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * Gets the mensajeError value for this TerminalServiceResponse.
     *
     * @return mensajeError
     */
    public java.lang.String getMensajeError() {
        return mensajeError;
    }

    /**
     * Sets the mensajeError value for this TerminalServiceResponse.
     *
     * @param mensajeError
     */
    public void setMensajeError(java.lang.String mensajeError) {
        this.mensajeError = mensajeError;
    }

    private java.lang.Object __equalsCalc = null;

    @Override
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TerminalServiceResponse)) {
            return false;
        }
        final TerminalServiceResponse other = (TerminalServiceResponse) obj;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && codigoError == other.getCodigoError() && error == other.isError()
                        && ((mensajeError == null && other.getMensajeError() == null)
                                        || (mensajeError != null && mensajeError.equals(other.getMensajeError())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    @Override
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getCodigoError();
        _hashCode += (isError() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMensajeError() != null) {
            _hashCode += getMensajeError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
                    TerminalServiceResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.terminal.webservices.textillevel.com.ar/",
                        "terminalServiceResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("error");
        elemField.setXmlName(new javax.xml.namespace.QName("", "error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensajeError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mensajeError"));
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
    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
                    java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
                    java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }
	
}
