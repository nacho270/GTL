/**
 * FECabeceraResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.modulos.fe.cliente.responses;

@SuppressWarnings({"rawtypes","serial","unused"})
public class FECabeceraResponse  implements java.io.Serializable {
    private long id;

    private long cuit;

    private java.lang.String fecha_cae;

    private int cantidadreg;

    private java.lang.String resultado;

    private java.lang.String motivo;

    private java.lang.String reproceso;

    private int presta_serv;

    public FECabeceraResponse() {
    }

    public FECabeceraResponse(
           long id,
           long cuit,
           java.lang.String fecha_cae,
           int cantidadreg,
           java.lang.String resultado,
           java.lang.String motivo,
           java.lang.String reproceso,
           int presta_serv) {
           this.id = id;
           this.cuit = cuit;
           this.fecha_cae = fecha_cae;
           this.cantidadreg = cantidadreg;
           this.resultado = resultado;
           this.motivo = motivo;
           this.reproceso = reproceso;
           this.presta_serv = presta_serv;
    }


    /**
     * Gets the id value for this FECabeceraResponse.
     * 
     * @return id
     */
    public long getId() {
        return id;
    }


    /**
     * Sets the id value for this FECabeceraResponse.
     * 
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * Gets the cuit value for this FECabeceraResponse.
     * 
     * @return cuit
     */
    public long getCuit() {
        return cuit;
    }


    /**
     * Sets the cuit value for this FECabeceraResponse.
     * 
     * @param cuit
     */
    public void setCuit(long cuit) {
        this.cuit = cuit;
    }


    /**
     * Gets the fecha_cae value for this FECabeceraResponse.
     * 
     * @return fecha_cae
     */
    public java.lang.String getFecha_cae() {
        return fecha_cae;
    }


    /**
     * Sets the fecha_cae value for this FECabeceraResponse.
     * 
     * @param fecha_cae
     */
    public void setFecha_cae(java.lang.String fecha_cae) {
        this.fecha_cae = fecha_cae;
    }


    /**
     * Gets the cantidadreg value for this FECabeceraResponse.
     * 
     * @return cantidadreg
     */
    public int getCantidadreg() {
        return cantidadreg;
    }


    /**
     * Sets the cantidadreg value for this FECabeceraResponse.
     * 
     * @param cantidadreg
     */
    public void setCantidadreg(int cantidadreg) {
        this.cantidadreg = cantidadreg;
    }


    /**
     * Gets the resultado value for this FECabeceraResponse.
     * 
     * @return resultado
     */
    public java.lang.String getResultado() {
        return resultado;
    }


    /**
     * Sets the resultado value for this FECabeceraResponse.
     * 
     * @param resultado
     */
    public void setResultado(java.lang.String resultado) {
        this.resultado = resultado;
    }


    /**
     * Gets the motivo value for this FECabeceraResponse.
     * 
     * @return motivo
     */
    public java.lang.String getMotivo() {
        return motivo;
    }


    /**
     * Sets the motivo value for this FECabeceraResponse.
     * 
     * @param motivo
     */
    public void setMotivo(java.lang.String motivo) {
        this.motivo = motivo;
    }


    /**
     * Gets the reproceso value for this FECabeceraResponse.
     * 
     * @return reproceso
     */
    public java.lang.String getReproceso() {
        return reproceso;
    }


    /**
     * Sets the reproceso value for this FECabeceraResponse.
     * 
     * @param reproceso
     */
    public void setReproceso(java.lang.String reproceso) {
        this.reproceso = reproceso;
    }


    /**
     * Gets the presta_serv value for this FECabeceraResponse.
     * 
     * @return presta_serv
     */
    public int getPresta_serv() {
        return presta_serv;
    }


    /**
     * Sets the presta_serv value for this FECabeceraResponse.
     * 
     * @param presta_serv
     */
    public void setPresta_serv(int presta_serv) {
        this.presta_serv = presta_serv;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FECabeceraResponse)) return false;
        FECabeceraResponse other = (FECabeceraResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.cuit == other.getCuit() &&
            ((this.fecha_cae==null && other.getFecha_cae()==null) || 
             (this.fecha_cae!=null &&
              this.fecha_cae.equals(other.getFecha_cae()))) &&
            this.cantidadreg == other.getCantidadreg() &&
            ((this.resultado==null && other.getResultado()==null) || 
             (this.resultado!=null &&
              this.resultado.equals(other.getResultado()))) &&
            ((this.motivo==null && other.getMotivo()==null) || 
             (this.motivo!=null &&
              this.motivo.equals(other.getMotivo()))) &&
            ((this.reproceso==null && other.getReproceso()==null) || 
             (this.reproceso!=null &&
              this.reproceso.equals(other.getReproceso()))) &&
            this.presta_serv == other.getPresta_serv();
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
        _hashCode += new Long(getId()).hashCode();
        _hashCode += new Long(getCuit()).hashCode();
        if (getFecha_cae() != null) {
            _hashCode += getFecha_cae().hashCode();
        }
        _hashCode += getCantidadreg();
        if (getResultado() != null) {
            _hashCode += getResultado().hashCode();
        }
        if (getMotivo() != null) {
            _hashCode += getMotivo().hashCode();
        }
        if (getReproceso() != null) {
            _hashCode += getReproceso().hashCode();
        }
        _hashCode += getPresta_serv();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FECabeceraResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "FECabeceraResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cuit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "cuit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_cae");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "fecha_cae"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cantidadreg");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "cantidadreg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "resultado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "motivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reproceso");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "reproceso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presta_serv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ar.gov.afip.dif.facturaelectronica/", "presta_serv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
