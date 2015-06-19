/**
 * FEDetalleRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente.requests;

@SuppressWarnings({"rawtypes","serial","unused"})
public class FEDetalleRequest  implements java.io.Serializable {
    private int tipo_doc;

    private long nro_doc;

    private int tipo_cbte;

    private int punto_vta;

    private long cbt_desde;

    private long cbt_hasta;

    private double imp_total;

    private double imp_tot_conc;

    private double imp_neto;

    private double impto_liq;

    private double impto_liq_rni;

    private double imp_op_ex;

    private java.lang.String fecha_cbte;

    private java.lang.String fecha_serv_desde;

    private java.lang.String fecha_serv_hasta;

    private java.lang.String fecha_venc_pago;

    public FEDetalleRequest() {
    }

    public FEDetalleRequest(
           int tipo_doc,
           long nro_doc,
           int tipo_cbte,
           int punto_vta,
           long cbt_desde,
           long cbt_hasta,
           double imp_total,
           double imp_tot_conc,
           double imp_neto,
           double impto_liq,
           double impto_liq_rni,
           double imp_op_ex,
           java.lang.String fecha_cbte,
           java.lang.String fecha_serv_desde,
           java.lang.String fecha_serv_hasta,
           java.lang.String fecha_venc_pago) {
           this.tipo_doc = tipo_doc;
           this.nro_doc = nro_doc;
           this.tipo_cbte = tipo_cbte;
           this.punto_vta = punto_vta;
           this.cbt_desde = cbt_desde;
           this.cbt_hasta = cbt_hasta;
           this.imp_total = imp_total;
           this.imp_tot_conc = imp_tot_conc;
           this.imp_neto = imp_neto;
           this.impto_liq = impto_liq;
           this.impto_liq_rni = impto_liq_rni;
           this.imp_op_ex = imp_op_ex;
           this.fecha_cbte = fecha_cbte;
           this.fecha_serv_desde = fecha_serv_desde;
           this.fecha_serv_hasta = fecha_serv_hasta;
           this.fecha_venc_pago = fecha_venc_pago;
    }


    /**
     * Gets the tipo_doc value for this FEDetalleRequest.
     * 
     * @return tipo_doc
     */
    public int getTipo_doc() {
        return tipo_doc;
    }


    /**
     * Sets the tipo_doc value for this FEDetalleRequest.
     * 
     * @param tipo_doc
     */
    public void setTipo_doc(int tipo_doc) {
        this.tipo_doc = tipo_doc;
    }


    /**
     * Gets the nro_doc value for this FEDetalleRequest.
     * 
     * @return nro_doc
     */
    public long getNro_doc() {
        return nro_doc;
    }


    /**
     * Sets the nro_doc value for this FEDetalleRequest.
     * 
     * @param nro_doc
     */
    public void setNro_doc(long nro_doc) {
        this.nro_doc = nro_doc;
    }


    /**
     * Gets the tipo_cbte value for this FEDetalleRequest.
     * 
     * @return tipo_cbte
     */
    public int getTipo_cbte() {
        return tipo_cbte;
    }


    /**
     * Sets the tipo_cbte value for this FEDetalleRequest.
     * 
     * @param tipo_cbte
     */
    public void setTipo_cbte(int tipo_cbte) {
        this.tipo_cbte = tipo_cbte;
    }


    /**
     * Gets the punto_vta value for this FEDetalleRequest.
     * 
     * @return punto_vta
     */
    public int getPunto_vta() {
        return punto_vta;
    }


    /**
     * Sets the punto_vta value for this FEDetalleRequest.
     * 
     * @param punto_vta
     */
    public void setPunto_vta(int punto_vta) {
        this.punto_vta = punto_vta;
    }


    /**
     * Gets the cbt_desde value for this FEDetalleRequest.
     * 
     * @return cbt_desde
     */
    public long getCbt_desde() {
        return cbt_desde;
    }


    /**
     * Sets the cbt_desde value for this FEDetalleRequest.
     * 
     * @param cbt_desde
     */
    public void setCbt_desde(long cbt_desde) {
        this.cbt_desde = cbt_desde;
    }


    /**
     * Gets the cbt_hasta value for this FEDetalleRequest.
     * 
     * @return cbt_hasta
     */
    public long getCbt_hasta() {
        return cbt_hasta;
    }


    /**
     * Sets the cbt_hasta value for this FEDetalleRequest.
     * 
     * @param cbt_hasta
     */
    public void setCbt_hasta(long cbt_hasta) {
        this.cbt_hasta = cbt_hasta;
    }


    /**
     * Gets the imp_total value for this FEDetalleRequest.
     * 
     * @return imp_total
     */
    public double getImp_total() {
        return imp_total;
    }


    /**
     * Sets the imp_total value for this FEDetalleRequest.
     * 
     * @param imp_total
     */
    public void setImp_total(double imp_total) {
        this.imp_total = imp_total;
    }


    /**
     * Gets the imp_tot_conc value for this FEDetalleRequest.
     * 
     * @return imp_tot_conc
     */
    public double getImp_tot_conc() {
        return imp_tot_conc;
    }


    /**
     * Sets the imp_tot_conc value for this FEDetalleRequest.
     * 
     * @param imp_tot_conc
     */
    public void setImp_tot_conc(double imp_tot_conc) {
        this.imp_tot_conc = imp_tot_conc;
    }


    /**
     * Gets the imp_neto value for this FEDetalleRequest.
     * 
     * @return imp_neto
     */
    public double getImp_neto() {
        return imp_neto;
    }


    /**
     * Sets the imp_neto value for this FEDetalleRequest.
     * 
     * @param imp_neto
     */
    public void setImp_neto(double imp_neto) {
        this.imp_neto = imp_neto;
    }


    /**
     * Gets the impto_liq value for this FEDetalleRequest.
     * 
     * @return impto_liq
     */
    public double getImpto_liq() {
        return impto_liq;
    }


    /**
     * Sets the impto_liq value for this FEDetalleRequest.
     * 
     * @param impto_liq
     */
    public void setImpto_liq(double impto_liq) {
        this.impto_liq = impto_liq;
    }


    /**
     * Gets the impto_liq_rni value for this FEDetalleRequest.
     * 
     * @return impto_liq_rni
     */
    public double getImpto_liq_rni() {
        return impto_liq_rni;
    }


    /**
     * Sets the impto_liq_rni value for this FEDetalleRequest.
     * 
     * @param impto_liq_rni
     */
    public void setImpto_liq_rni(double impto_liq_rni) {
        this.impto_liq_rni = impto_liq_rni;
    }


    /**
     * Gets the imp_op_ex value for this FEDetalleRequest.
     * 
     * @return imp_op_ex
     */
    public double getImp_op_ex() {
        return imp_op_ex;
    }


    /**
     * Sets the imp_op_ex value for this FEDetalleRequest.
     * 
     * @param imp_op_ex
     */
    public void setImp_op_ex(double imp_op_ex) {
        this.imp_op_ex = imp_op_ex;
    }


    /**
     * Gets the fecha_cbte value for this FEDetalleRequest.
     * 
     * @return fecha_cbte
     */
    public java.lang.String getFecha_cbte() {
        return fecha_cbte;
    }


    /**
     * Sets the fecha_cbte value for this FEDetalleRequest.
     * 
     * @param fecha_cbte
     */
    public void setFecha_cbte(java.lang.String fecha_cbte) {
        this.fecha_cbte = fecha_cbte;
    }


    /**
     * Gets the fecha_serv_desde value for this FEDetalleRequest.
     * 
     * @return fecha_serv_desde
     */
    public java.lang.String getFecha_serv_desde() {
        return fecha_serv_desde;
    }


    /**
     * Sets the fecha_serv_desde value for this FEDetalleRequest.
     * 
     * @param fecha_serv_desde
     */
    public void setFecha_serv_desde(java.lang.String fecha_serv_desde) {
        this.fecha_serv_desde = fecha_serv_desde;
    }


    /**
     * Gets the fecha_serv_hasta value for this FEDetalleRequest.
     * 
     * @return fecha_serv_hasta
     */
    public java.lang.String getFecha_serv_hasta() {
        return fecha_serv_hasta;
    }


    /**
     * Sets the fecha_serv_hasta value for this FEDetalleRequest.
     * 
     * @param fecha_serv_hasta
     */
    public void setFecha_serv_hasta(java.lang.String fecha_serv_hasta) {
        this.fecha_serv_hasta = fecha_serv_hasta;
    }


    /**
     * Gets the fecha_venc_pago value for this FEDetalleRequest.
     * 
     * @return fecha_venc_pago
     */
    public java.lang.String getFecha_venc_pago() {
        return fecha_venc_pago;
    }


    /**
     * Sets the fecha_venc_pago value for this FEDetalleRequest.
     * 
     * @param fecha_venc_pago
     */
    public void setFecha_venc_pago(java.lang.String fecha_venc_pago) {
        this.fecha_venc_pago = fecha_venc_pago;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FEDetalleRequest)) return false;
        FEDetalleRequest other = (FEDetalleRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.tipo_doc == other.getTipo_doc() &&
            this.nro_doc == other.getNro_doc() &&
            this.tipo_cbte == other.getTipo_cbte() &&
            this.punto_vta == other.getPunto_vta() &&
            this.cbt_desde == other.getCbt_desde() &&
            this.cbt_hasta == other.getCbt_hasta() &&
            this.imp_total == other.getImp_total() &&
            this.imp_tot_conc == other.getImp_tot_conc() &&
            this.imp_neto == other.getImp_neto() &&
            this.impto_liq == other.getImpto_liq() &&
            this.impto_liq_rni == other.getImpto_liq_rni() &&
            this.imp_op_ex == other.getImp_op_ex() &&
            ((this.fecha_cbte==null && other.getFecha_cbte()==null) || 
             (this.fecha_cbte!=null &&
              this.fecha_cbte.equals(other.getFecha_cbte()))) &&
            ((this.fecha_serv_desde==null && other.getFecha_serv_desde()==null) || 
             (this.fecha_serv_desde!=null &&
              this.fecha_serv_desde.equals(other.getFecha_serv_desde()))) &&
            ((this.fecha_serv_hasta==null && other.getFecha_serv_hasta()==null) || 
             (this.fecha_serv_hasta!=null &&
              this.fecha_serv_hasta.equals(other.getFecha_serv_hasta()))) &&
            ((this.fecha_venc_pago==null && other.getFecha_venc_pago()==null) || 
             (this.fecha_venc_pago!=null &&
              this.fecha_venc_pago.equals(other.getFecha_venc_pago())));
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
        _hashCode += getTipo_doc();
        _hashCode += new Long(getNro_doc()).hashCode();
        _hashCode += getTipo_cbte();
        _hashCode += getPunto_vta();
        _hashCode += new Long(getCbt_desde()).hashCode();
        _hashCode += new Long(getCbt_hasta()).hashCode();
        _hashCode += new Double(getImp_total()).hashCode();
        _hashCode += new Double(getImp_tot_conc()).hashCode();
        _hashCode += new Double(getImp_neto()).hashCode();
        _hashCode += new Double(getImpto_liq()).hashCode();
        _hashCode += new Double(getImpto_liq_rni()).hashCode();
        _hashCode += new Double(getImp_op_ex()).hashCode();
        if (getFecha_cbte() != null) {
            _hashCode += getFecha_cbte().hashCode();
        }
        if (getFecha_serv_desde() != null) {
            _hashCode += getFecha_serv_desde().hashCode();
        }
        if (getFecha_serv_hasta() != null) {
            _hashCode += getFecha_serv_hasta().hashCode();
        }
        if (getFecha_venc_pago() != null) {
            _hashCode += getFecha_venc_pago().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FEDetalleRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FEDetalleRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo_doc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "tipo_doc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nro_doc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "nro_doc"));
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
        elemField.setFieldName("cbt_desde");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "cbt_desde"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cbt_hasta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "cbt_hasta"));
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
        elemField.setFieldName("imp_tot_conc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "imp_tot_conc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imp_neto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "imp_neto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("impto_liq");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "impto_liq"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("impto_liq_rni");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "impto_liq_rni"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imp_op_ex");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "imp_op_ex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_cbte");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "fecha_cbte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_serv_desde");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "fecha_serv_desde"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_serv_hasta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "fecha_serv_hasta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_venc_pago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "fecha_venc_pago"));
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
