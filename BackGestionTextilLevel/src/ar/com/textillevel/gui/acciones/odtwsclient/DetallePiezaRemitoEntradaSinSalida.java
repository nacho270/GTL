/**
 * DetallePiezaRemitoEntradaSinSalida.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "rawtypes", "unused"})
public class DetallePiezaRemitoEntradaSinSalida  implements java.io.Serializable {
    private java.lang.Integer cantPiezas;

    private java.lang.String codigoODT;

    private java.lang.Integer idODT;

    private double metrosTotales;

    private java.lang.Integer nroRemito;

    private java.lang.String producto;

    public DetallePiezaRemitoEntradaSinSalida() {
    }

    public DetallePiezaRemitoEntradaSinSalida(
           java.lang.Integer cantPiezas,
           java.lang.String codigoODT,
           java.lang.Integer idODT,
           double metrosTotales,
           java.lang.Integer nroRemito,
           java.lang.String producto) {
           this.cantPiezas = cantPiezas;
           this.codigoODT = codigoODT;
           this.idODT = idODT;
           this.metrosTotales = metrosTotales;
           this.nroRemito = nroRemito;
           this.producto = producto;
    }


    /**
     * Gets the cantPiezas value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @return cantPiezas
     */
    public java.lang.Integer getCantPiezas() {
        return cantPiezas;
    }


    /**
     * Sets the cantPiezas value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @param cantPiezas
     */
    public void setCantPiezas(java.lang.Integer cantPiezas) {
        this.cantPiezas = cantPiezas;
    }


    /**
     * Gets the codigoODT value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @return codigoODT
     */
    public java.lang.String getCodigoODT() {
        return codigoODT;
    }


    /**
     * Sets the codigoODT value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @param codigoODT
     */
    public void setCodigoODT(java.lang.String codigoODT) {
        this.codigoODT = codigoODT;
    }


    /**
     * Gets the idODT value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @return idODT
     */
    public java.lang.Integer getIdODT() {
        return idODT;
    }


    /**
     * Sets the idODT value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @param idODT
     */
    public void setIdODT(java.lang.Integer idODT) {
        this.idODT = idODT;
    }


    /**
     * Gets the metrosTotales value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @return metrosTotales
     */
    public double getMetrosTotales() {
        return metrosTotales;
    }


    /**
     * Sets the metrosTotales value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @param metrosTotales
     */
    public void setMetrosTotales(double metrosTotales) {
        this.metrosTotales = metrosTotales;
    }


    /**
     * Gets the nroRemito value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @return nroRemito
     */
    public java.lang.Integer getNroRemito() {
        return nroRemito;
    }


    /**
     * Sets the nroRemito value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @param nroRemito
     */
    public void setNroRemito(java.lang.Integer nroRemito) {
        this.nroRemito = nroRemito;
    }


    /**
     * Gets the producto value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @return producto
     */
    public java.lang.String getProducto() {
        return producto;
    }


    /**
     * Sets the producto value for this DetallePiezaRemitoEntradaSinSalida.
     * 
     * @param producto
     */
    public void setProducto(java.lang.String producto) {
        this.producto = producto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetallePiezaRemitoEntradaSinSalida)) return false;
        DetallePiezaRemitoEntradaSinSalida other = (DetallePiezaRemitoEntradaSinSalida) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cantPiezas==null && other.getCantPiezas()==null) || 
             (this.cantPiezas!=null &&
              this.cantPiezas.equals(other.getCantPiezas()))) &&
            ((this.codigoODT==null && other.getCodigoODT()==null) || 
             (this.codigoODT!=null &&
              this.codigoODT.equals(other.getCodigoODT()))) &&
            ((this.idODT==null && other.getIdODT()==null) || 
             (this.idODT!=null &&
              this.idODT.equals(other.getIdODT()))) &&
            this.metrosTotales == other.getMetrosTotales() &&
            ((this.nroRemito==null && other.getNroRemito()==null) || 
             (this.nroRemito!=null &&
              this.nroRemito.equals(other.getNroRemito()))) &&
            ((this.producto==null && other.getProducto()==null) || 
             (this.producto!=null &&
              this.producto.equals(other.getProducto())));
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
        if (getCantPiezas() != null) {
            _hashCode += getCantPiezas().hashCode();
        }
        if (getCodigoODT() != null) {
            _hashCode += getCodigoODT().hashCode();
        }
        if (getIdODT() != null) {
            _hashCode += getIdODT().hashCode();
        }
        _hashCode += new Double(getMetrosTotales()).hashCode();
        if (getNroRemito() != null) {
            _hashCode += getNroRemito().hashCode();
        }
        if (getProducto() != null) {
            _hashCode += getProducto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetallePiezaRemitoEntradaSinSalida.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "detallePiezaRemitoEntradaSinSalida"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cantPiezas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cantPiezas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoODT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoODT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idODT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idODT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metrosTotales");
        elemField.setXmlName(new javax.xml.namespace.QName("", "metrosTotales"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nroRemito");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nroRemito"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("producto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "producto"));
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
