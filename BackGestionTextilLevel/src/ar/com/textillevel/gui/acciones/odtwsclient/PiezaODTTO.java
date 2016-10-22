/**
 * PiezaODTTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class PiezaODTTO  implements java.io.Serializable {
    private java.lang.String codigoOdt;

    private java.lang.Integer id;

    private java.math.BigDecimal metros;

    private java.math.BigDecimal metrosStockInicial;

    private java.lang.Integer nroPiezaStockInicial;

    private java.lang.Integer orden;

    private java.lang.Integer ordenSubpieza;

    private ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO piezaRemito;

    private ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO[] piezasSalida;

    public PiezaODTTO() {
    }

    public PiezaODTTO(
           java.lang.String codigoOdt,
           java.lang.Integer id,
           java.math.BigDecimal metros,
           java.math.BigDecimal metrosStockInicial,
           java.lang.Integer nroPiezaStockInicial,
           java.lang.Integer orden,
           java.lang.Integer ordenSubpieza,
           ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO piezaRemito,
           ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO[] piezasSalida) {
           this.codigoOdt = codigoOdt;
           this.id = id;
           this.metros = metros;
           this.metrosStockInicial = metrosStockInicial;
           this.nroPiezaStockInicial = nroPiezaStockInicial;
           this.orden = orden;
           this.ordenSubpieza = ordenSubpieza;
           this.piezaRemito = piezaRemito;
           this.piezasSalida = piezasSalida;
    }


    /**
     * Gets the codigoOdt value for this PiezaODTTO.
     * 
     * @return codigoOdt
     */
    public java.lang.String getCodigoOdt() {
        return codigoOdt;
    }


    /**
     * Sets the codigoOdt value for this PiezaODTTO.
     * 
     * @param codigoOdt
     */
    public void setCodigoOdt(java.lang.String codigoOdt) {
        this.codigoOdt = codigoOdt;
    }


    /**
     * Gets the id value for this PiezaODTTO.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this PiezaODTTO.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the metros value for this PiezaODTTO.
     * 
     * @return metros
     */
    public java.math.BigDecimal getMetros() {
        return metros;
    }


    /**
     * Sets the metros value for this PiezaODTTO.
     * 
     * @param metros
     */
    public void setMetros(java.math.BigDecimal metros) {
        this.metros = metros;
    }


    /**
     * Gets the metrosStockInicial value for this PiezaODTTO.
     * 
     * @return metrosStockInicial
     */
    public java.math.BigDecimal getMetrosStockInicial() {
        return metrosStockInicial;
    }


    /**
     * Sets the metrosStockInicial value for this PiezaODTTO.
     * 
     * @param metrosStockInicial
     */
    public void setMetrosStockInicial(java.math.BigDecimal metrosStockInicial) {
        this.metrosStockInicial = metrosStockInicial;
    }


    /**
     * Gets the nroPiezaStockInicial value for this PiezaODTTO.
     * 
     * @return nroPiezaStockInicial
     */
    public java.lang.Integer getNroPiezaStockInicial() {
        return nroPiezaStockInicial;
    }


    /**
     * Sets the nroPiezaStockInicial value for this PiezaODTTO.
     * 
     * @param nroPiezaStockInicial
     */
    public void setNroPiezaStockInicial(java.lang.Integer nroPiezaStockInicial) {
        this.nroPiezaStockInicial = nroPiezaStockInicial;
    }


    /**
     * Gets the orden value for this PiezaODTTO.
     * 
     * @return orden
     */
    public java.lang.Integer getOrden() {
        return orden;
    }


    /**
     * Sets the orden value for this PiezaODTTO.
     * 
     * @param orden
     */
    public void setOrden(java.lang.Integer orden) {
        this.orden = orden;
    }


    /**
     * Gets the ordenSubpieza value for this PiezaODTTO.
     * 
     * @return ordenSubpieza
     */
    public java.lang.Integer getOrdenSubpieza() {
        return ordenSubpieza;
    }


    /**
     * Sets the ordenSubpieza value for this PiezaODTTO.
     * 
     * @param ordenSubpieza
     */
    public void setOrdenSubpieza(java.lang.Integer ordenSubpieza) {
        this.ordenSubpieza = ordenSubpieza;
    }


    /**
     * Gets the piezaRemito value for this PiezaODTTO.
     * 
     * @return piezaRemito
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO getPiezaRemito() {
        return piezaRemito;
    }


    /**
     * Sets the piezaRemito value for this PiezaODTTO.
     * 
     * @param piezaRemito
     */
    public void setPiezaRemito(ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO piezaRemito) {
        this.piezaRemito = piezaRemito;
    }


    /**
     * Gets the piezasSalida value for this PiezaODTTO.
     * 
     * @return piezasSalida
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO[] getPiezasSalida() {
        return piezasSalida;
    }


    /**
     * Sets the piezasSalida value for this PiezaODTTO.
     * 
     * @param piezasSalida
     */
    public void setPiezasSalida(ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO[] piezasSalida) {
        this.piezasSalida = piezasSalida;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO getPiezasSalida(int i) {
        return this.piezasSalida[i];
    }

    public void setPiezasSalida(int i, ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO _value) {
        this.piezasSalida[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PiezaODTTO)) return false;
        PiezaODTTO other = (PiezaODTTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoOdt==null && other.getCodigoOdt()==null) || 
             (this.codigoOdt!=null &&
              this.codigoOdt.equals(other.getCodigoOdt()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.metros==null && other.getMetros()==null) || 
             (this.metros!=null &&
              this.metros.equals(other.getMetros()))) &&
            ((this.metrosStockInicial==null && other.getMetrosStockInicial()==null) || 
             (this.metrosStockInicial!=null &&
              this.metrosStockInicial.equals(other.getMetrosStockInicial()))) &&
            ((this.nroPiezaStockInicial==null && other.getNroPiezaStockInicial()==null) || 
             (this.nroPiezaStockInicial!=null &&
              this.nroPiezaStockInicial.equals(other.getNroPiezaStockInicial()))) &&
            ((this.orden==null && other.getOrden()==null) || 
             (this.orden!=null &&
              this.orden.equals(other.getOrden()))) &&
            ((this.ordenSubpieza==null && other.getOrdenSubpieza()==null) || 
             (this.ordenSubpieza!=null &&
              this.ordenSubpieza.equals(other.getOrdenSubpieza()))) &&
            ((this.piezaRemito==null && other.getPiezaRemito()==null) || 
             (this.piezaRemito!=null &&
              this.piezaRemito.equals(other.getPiezaRemito()))) &&
            ((this.piezasSalida==null && other.getPiezasSalida()==null) || 
             (this.piezasSalida!=null &&
              java.util.Arrays.equals(this.piezasSalida, other.getPiezasSalida())));
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
        if (getCodigoOdt() != null) {
            _hashCode += getCodigoOdt().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getMetros() != null) {
            _hashCode += getMetros().hashCode();
        }
        if (getMetrosStockInicial() != null) {
            _hashCode += getMetrosStockInicial().hashCode();
        }
        if (getNroPiezaStockInicial() != null) {
            _hashCode += getNroPiezaStockInicial().hashCode();
        }
        if (getOrden() != null) {
            _hashCode += getOrden().hashCode();
        }
        if (getOrdenSubpieza() != null) {
            _hashCode += getOrdenSubpieza().hashCode();
        }
        if (getPiezaRemito() != null) {
            _hashCode += getPiezaRemito().hashCode();
        }
        if (getPiezasSalida() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPiezasSalida());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPiezasSalida(), i);
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
        new org.apache.axis.description.TypeDesc(PiezaODTTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "piezaODTTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoOdt");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoOdt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metros");
        elemField.setXmlName(new javax.xml.namespace.QName("", "metros"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metrosStockInicial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "metrosStockInicial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nroPiezaStockInicial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nroPiezaStockInicial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orden");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orden"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenSubpieza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ordenSubpieza"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piezaRemito");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piezaRemito"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "piezaRemitoTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piezasSalida");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piezasSalida"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "piezaRemitoTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
