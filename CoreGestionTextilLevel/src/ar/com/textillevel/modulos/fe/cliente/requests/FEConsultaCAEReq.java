/**
 * FEConsultaCAEReq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente.requests;

@SuppressWarnings({"rawtypes","serial","unused"})
public class FEConsultaCAEReq  implements java.io.Serializable {
    private long cuit_emisor;

    private int tipo_cbte;

    private int punto_vta;

    private long cbt_nro;

    private double imp_total;

    private java.lang.String cae;

    private java.lang.String fecha_cbte;

    public FEConsultaCAEReq() {
    }

    public FEConsultaCAEReq(
           long cuit_emisor,
           int tipo_cbte,
           int punto_vta,
           long cbt_nro,
           double imp_total,
           java.lang.String cae,
           java.lang.String fecha_cbte) {
           this.cuit_emisor = cuit_emisor;
           this.tipo_cbte = tipo_cbte;
           this.punto_vta = punto_vta;
           this.cbt_nro = cbt_nro;
           this.imp_total = imp_total;
           this.cae = cae;
           this.fecha_cbte = fecha_cbte;
    }


    /**
     * Gets the cuit_emisor value for this FEConsultaCAEReq.
     * 
     * @return cuit_emisor
     */
    public long getCuit_emisor() {
        return cuit_emisor;
    }


    /**
     * Sets the cuit_emisor value for this FEConsultaCAEReq.
     * 
     * @param cuit_emisor
     */
    public void setCuit_emisor(long cuit_emisor) {
        this.cuit_emisor = cuit_emisor;
    }


    /**
     * Gets the tipo_cbte value for this FEConsultaCAEReq.
     * 
     * @return tipo_cbte
     */
    public int getTipo_cbte() {
        return tipo_cbte;
    }


    /**
     * Sets the tipo_cbte value for this FEConsultaCAEReq.
     * 
     * @param tipo_cbte
     */
    public void setTipo_cbte(int tipo_cbte) {
        this.tipo_cbte = tipo_cbte;
    }


    /**
     * Gets the punto_vta value for this FEConsultaCAEReq.
     * 
     * @return punto_vta
     */
    public int getPunto_vta() {
        return punto_vta;
    }


    /**
     * Sets the punto_vta value for this FEConsultaCAEReq.
     * 
     * @param punto_vta
     */
    public void setPunto_vta(int punto_vta) {
        this.punto_vta = punto_vta;
    }


    /**
     * Gets the cbt_nro value for this FEConsultaCAEReq.
     * 
     * @return cbt_nro
     */
    public long getCbt_nro() {
        return cbt_nro;
    }


    /**
     * Sets the cbt_nro value for this FEConsultaCAEReq.
     * 
     * @param cbt_nro
     */
    public void setCbt_nro(long cbt_nro) {
        this.cbt_nro = cbt_nro;
    }


    /**
     * Gets the imp_total value for this FEConsultaCAEReq.
     * 
     * @return imp_total
     */
    public double getImp_total() {
        return imp_total;
    }


    /**
     * Sets the imp_total value for this FEConsultaCAEReq.
     * 
     * @param imp_total
     */
    public void setImp_total(double imp_total) {
        this.imp_total = imp_total;
    }


    /**
     * Gets the cae value for this FEConsultaCAEReq.
     * 
     * @return cae
     */
    public java.lang.String getCae() {
        return cae;
    }


    /**
     * Sets the cae value for this FEConsultaCAEReq.
     * 
     * @param cae
     */
    public void setCae(java.lang.String cae) {
        this.cae = cae;
    }


    /**
     * Gets the fecha_cbte value for this FEConsultaCAEReq.
     * 
     * @return fecha_cbte
     */
    public java.lang.String getFecha_cbte() {
        return fecha_cbte;
    }


    /**
     * Sets the fecha_cbte value for this FEConsultaCAEReq.
     * 
     * @param fecha_cbte
     */
    public void setFecha_cbte(java.lang.String fecha_cbte) {
        this.fecha_cbte = fecha_cbte;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FEConsultaCAEReq)) return false;
        FEConsultaCAEReq other = (FEConsultaCAEReq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.cuit_emisor == other.getCuit_emisor() &&
            this.tipo_cbte == other.getTipo_cbte() &&
            this.punto_vta == other.getPunto_vta() &&
            this.cbt_nro == other.getCbt_nro() &&
            this.imp_total == other.getImp_total() &&
            ((this.cae==null && other.getCae()==null) || 
             (this.cae!=null &&
              this.cae.equals(other.getCae()))) &&
            ((this.fecha_cbte==null && other.getFecha_cbte()==null) || 
             (this.fecha_cbte!=null &&
              this.fecha_cbte.equals(other.getFecha_cbte())));
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
        _hashCode += new Long(getCuit_emisor()).hashCode();
        _hashCode += getTipo_cbte();
        _hashCode += getPunto_vta();
        _hashCode += new Long(getCbt_nro()).hashCode();
        _hashCode += new Double(getImp_total()).hashCode();
        if (getCae() != null) {
            _hashCode += getCae().hashCode();
        }
        if (getFecha_cbte() != null) {
            _hashCode += getFecha_cbte().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FEConsultaCAEReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FEConsultaCAEReq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cuit_emisor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "cuit_emisor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo_cbte");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "tipo_cbte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("punto_vta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "punto_vta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cbt_nro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "cbt_nro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imp_total");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "imp_total"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cae");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "cae"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_cbte");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "fecha_cbte"));
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
