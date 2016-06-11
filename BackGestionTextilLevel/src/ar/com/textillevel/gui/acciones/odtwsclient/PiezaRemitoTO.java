/**
 * PiezaRemitoTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class PiezaRemitoTO  implements java.io.Serializable {
    private java.lang.Boolean enSalida;

    private java.lang.Integer id;

    private java.lang.Integer idPmpDescuentoStock;

    private java.math.BigDecimal metros;

    private java.lang.String observaciones;

    private java.lang.Integer ordenPieza;

    private java.lang.String ordenPiezaCalculado;

    private ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO piezaEntrada;

    private java.lang.Boolean piezaSinODT;

    private ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO[] piezasPadreODT;

    public PiezaRemitoTO() {
    }

    public PiezaRemitoTO(
           java.lang.Boolean enSalida,
           java.lang.Integer id,
           java.lang.Integer idPmpDescuentoStock,
           java.math.BigDecimal metros,
           java.lang.String observaciones,
           java.lang.Integer ordenPieza,
           java.lang.String ordenPiezaCalculado,
           ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO piezaEntrada,
           java.lang.Boolean piezaSinODT,
           ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO[] piezasPadreODT) {
           this.enSalida = enSalida;
           this.id = id;
           this.idPmpDescuentoStock = idPmpDescuentoStock;
           this.metros = metros;
           this.observaciones = observaciones;
           this.ordenPieza = ordenPieza;
           this.ordenPiezaCalculado = ordenPiezaCalculado;
           this.piezaEntrada = piezaEntrada;
           this.piezaSinODT = piezaSinODT;
           this.piezasPadreODT = piezasPadreODT;
    }


    /**
     * Gets the enSalida value for this PiezaRemitoTO.
     * 
     * @return enSalida
     */
    public java.lang.Boolean getEnSalida() {
        return enSalida;
    }


    /**
     * Sets the enSalida value for this PiezaRemitoTO.
     * 
     * @param enSalida
     */
    public void setEnSalida(java.lang.Boolean enSalida) {
        this.enSalida = enSalida;
    }


    /**
     * Gets the id value for this PiezaRemitoTO.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this PiezaRemitoTO.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the idPmpDescuentoStock value for this PiezaRemitoTO.
     * 
     * @return idPmpDescuentoStock
     */
    public java.lang.Integer getIdPmpDescuentoStock() {
        return idPmpDescuentoStock;
    }


    /**
     * Sets the idPmpDescuentoStock value for this PiezaRemitoTO.
     * 
     * @param idPmpDescuentoStock
     */
    public void setIdPmpDescuentoStock(java.lang.Integer idPmpDescuentoStock) {
        this.idPmpDescuentoStock = idPmpDescuentoStock;
    }


    /**
     * Gets the metros value for this PiezaRemitoTO.
     * 
     * @return metros
     */
    public java.math.BigDecimal getMetros() {
        return metros;
    }


    /**
     * Sets the metros value for this PiezaRemitoTO.
     * 
     * @param metros
     */
    public void setMetros(java.math.BigDecimal metros) {
        this.metros = metros;
    }


    /**
     * Gets the observaciones value for this PiezaRemitoTO.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this PiezaRemitoTO.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }


    /**
     * Gets the ordenPieza value for this PiezaRemitoTO.
     * 
     * @return ordenPieza
     */
    public java.lang.Integer getOrdenPieza() {
        return ordenPieza;
    }


    /**
     * Sets the ordenPieza value for this PiezaRemitoTO.
     * 
     * @param ordenPieza
     */
    public void setOrdenPieza(java.lang.Integer ordenPieza) {
        this.ordenPieza = ordenPieza;
    }


    /**
     * Gets the ordenPiezaCalculado value for this PiezaRemitoTO.
     * 
     * @return ordenPiezaCalculado
     */
    public java.lang.String getOrdenPiezaCalculado() {
        return ordenPiezaCalculado;
    }


    /**
     * Sets the ordenPiezaCalculado value for this PiezaRemitoTO.
     * 
     * @param ordenPiezaCalculado
     */
    public void setOrdenPiezaCalculado(java.lang.String ordenPiezaCalculado) {
        this.ordenPiezaCalculado = ordenPiezaCalculado;
    }


    /**
     * Gets the piezaEntrada value for this PiezaRemitoTO.
     * 
     * @return piezaEntrada
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO getPiezaEntrada() {
        return piezaEntrada;
    }


    /**
     * Sets the piezaEntrada value for this PiezaRemitoTO.
     * 
     * @param piezaEntrada
     */
    public void setPiezaEntrada(ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO piezaEntrada) {
        this.piezaEntrada = piezaEntrada;
    }


    /**
     * Gets the piezaSinODT value for this PiezaRemitoTO.
     * 
     * @return piezaSinODT
     */
    public java.lang.Boolean getPiezaSinODT() {
        return piezaSinODT;
    }


    /**
     * Sets the piezaSinODT value for this PiezaRemitoTO.
     * 
     * @param piezaSinODT
     */
    public void setPiezaSinODT(java.lang.Boolean piezaSinODT) {
        this.piezaSinODT = piezaSinODT;
    }


    /**
     * Gets the piezasPadreODT value for this PiezaRemitoTO.
     * 
     * @return piezasPadreODT
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO[] getPiezasPadreODT() {
        return piezasPadreODT;
    }


    /**
     * Sets the piezasPadreODT value for this PiezaRemitoTO.
     * 
     * @param piezasPadreODT
     */
    public void setPiezasPadreODT(ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO[] piezasPadreODT) {
        this.piezasPadreODT = piezasPadreODT;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO getPiezasPadreODT(int i) {
        return this.piezasPadreODT[i];
    }

    public void setPiezasPadreODT(int i, ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO _value) {
        this.piezasPadreODT[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PiezaRemitoTO)) return false;
        PiezaRemitoTO other = (PiezaRemitoTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.enSalida==null && other.getEnSalida()==null) || 
             (this.enSalida!=null &&
              this.enSalida.equals(other.getEnSalida()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.idPmpDescuentoStock==null && other.getIdPmpDescuentoStock()==null) || 
             (this.idPmpDescuentoStock!=null &&
              this.idPmpDescuentoStock.equals(other.getIdPmpDescuentoStock()))) &&
            ((this.metros==null && other.getMetros()==null) || 
             (this.metros!=null &&
              this.metros.equals(other.getMetros()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones()))) &&
            ((this.ordenPieza==null && other.getOrdenPieza()==null) || 
             (this.ordenPieza!=null &&
              this.ordenPieza.equals(other.getOrdenPieza()))) &&
            ((this.ordenPiezaCalculado==null && other.getOrdenPiezaCalculado()==null) || 
             (this.ordenPiezaCalculado!=null &&
              this.ordenPiezaCalculado.equals(other.getOrdenPiezaCalculado()))) &&
            ((this.piezaEntrada==null && other.getPiezaEntrada()==null) || 
             (this.piezaEntrada!=null &&
              this.piezaEntrada.equals(other.getPiezaEntrada()))) &&
            ((this.piezaSinODT==null && other.getPiezaSinODT()==null) || 
             (this.piezaSinODT!=null &&
              this.piezaSinODT.equals(other.getPiezaSinODT()))) &&
            ((this.piezasPadreODT==null && other.getPiezasPadreODT()==null) || 
             (this.piezasPadreODT!=null &&
              java.util.Arrays.equals(this.piezasPadreODT, other.getPiezasPadreODT())));
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
        if (getEnSalida() != null) {
            _hashCode += getEnSalida().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIdPmpDescuentoStock() != null) {
            _hashCode += getIdPmpDescuentoStock().hashCode();
        }
        if (getMetros() != null) {
            _hashCode += getMetros().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        if (getOrdenPieza() != null) {
            _hashCode += getOrdenPieza().hashCode();
        }
        if (getOrdenPiezaCalculado() != null) {
            _hashCode += getOrdenPiezaCalculado().hashCode();
        }
        if (getPiezaEntrada() != null) {
            _hashCode += getPiezaEntrada().hashCode();
        }
        if (getPiezaSinODT() != null) {
            _hashCode += getPiezaSinODT().hashCode();
        }
        if (getPiezasPadreODT() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPiezasPadreODT());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPiezasPadreODT(), i);
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
        new org.apache.axis.description.TypeDesc(PiezaRemitoTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "piezaRemitoTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enSalida");
        elemField.setXmlName(new javax.xml.namespace.QName("", "enSalida"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("idPmpDescuentoStock");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idPmpDescuentoStock"));
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
        elemField.setFieldName("observaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenPieza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ordenPieza"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenPiezaCalculado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ordenPiezaCalculado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piezaEntrada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piezaEntrada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "piezaRemitoTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piezaSinODT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piezaSinODT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piezasPadreODT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piezasPadreODT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "piezaODTTO"));
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
