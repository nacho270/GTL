/**
 * OdtEagerTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class OdtEagerTO  implements java.io.Serializable {
    private java.lang.String codigo;

    private java.lang.Integer id;

    private java.lang.Byte idAvance;

    private java.lang.Integer idEstadoODT;

    private java.lang.Integer idMaquinaActual;

    private java.lang.Integer idMaquinaPrincipal;

    private java.lang.Integer idProductoArticulo;

    private java.lang.Short ordenEnMaquina;

    private ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO[] piezas;

    private ar.com.textillevel.gui.acciones.odtwsclient.RemitoEntradaTO remito;

    private ar.com.textillevel.gui.acciones.odtwsclient.SecuenciaODTTO secuenciaDeTrabajo;

    private java.lang.Long timestampFechaODT;

    public OdtEagerTO() {
    }

    public OdtEagerTO(
           java.lang.String codigo,
           java.lang.Integer id,
           java.lang.Byte idAvance,
           java.lang.Integer idEstadoODT,
           java.lang.Integer idMaquinaActual,
           java.lang.Integer idMaquinaPrincipal,
           java.lang.Integer idProductoArticulo,
           java.lang.Short ordenEnMaquina,
           ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO[] piezas,
           ar.com.textillevel.gui.acciones.odtwsclient.RemitoEntradaTO remito,
           ar.com.textillevel.gui.acciones.odtwsclient.SecuenciaODTTO secuenciaDeTrabajo,
           java.lang.Long timestampFechaODT) {
           this.codigo = codigo;
           this.id = id;
           this.idAvance = idAvance;
           this.idEstadoODT = idEstadoODT;
           this.idMaquinaActual = idMaquinaActual;
           this.idMaquinaPrincipal = idMaquinaPrincipal;
           this.idProductoArticulo = idProductoArticulo;
           this.ordenEnMaquina = ordenEnMaquina;
           this.piezas = piezas;
           this.remito = remito;
           this.secuenciaDeTrabajo = secuenciaDeTrabajo;
           this.timestampFechaODT = timestampFechaODT;
    }


    /**
     * Gets the codigo value for this OdtEagerTO.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this OdtEagerTO.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the id value for this OdtEagerTO.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this OdtEagerTO.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the idAvance value for this OdtEagerTO.
     * 
     * @return idAvance
     */
    public java.lang.Byte getIdAvance() {
        return idAvance;
    }


    /**
     * Sets the idAvance value for this OdtEagerTO.
     * 
     * @param idAvance
     */
    public void setIdAvance(java.lang.Byte idAvance) {
        this.idAvance = idAvance;
    }


    /**
     * Gets the idEstadoODT value for this OdtEagerTO.
     * 
     * @return idEstadoODT
     */
    public java.lang.Integer getIdEstadoODT() {
        return idEstadoODT;
    }


    /**
     * Sets the idEstadoODT value for this OdtEagerTO.
     * 
     * @param idEstadoODT
     */
    public void setIdEstadoODT(java.lang.Integer idEstadoODT) {
        this.idEstadoODT = idEstadoODT;
    }


    /**
     * Gets the idMaquinaActual value for this OdtEagerTO.
     * 
     * @return idMaquinaActual
     */
    public java.lang.Integer getIdMaquinaActual() {
        return idMaquinaActual;
    }


    /**
     * Sets the idMaquinaActual value for this OdtEagerTO.
     * 
     * @param idMaquinaActual
     */
    public void setIdMaquinaActual(java.lang.Integer idMaquinaActual) {
        this.idMaquinaActual = idMaquinaActual;
    }


    /**
     * Gets the idMaquinaPrincipal value for this OdtEagerTO.
     * 
     * @return idMaquinaPrincipal
     */
    public java.lang.Integer getIdMaquinaPrincipal() {
        return idMaquinaPrincipal;
    }


    /**
     * Sets the idMaquinaPrincipal value for this OdtEagerTO.
     * 
     * @param idMaquinaPrincipal
     */
    public void setIdMaquinaPrincipal(java.lang.Integer idMaquinaPrincipal) {
        this.idMaquinaPrincipal = idMaquinaPrincipal;
    }


    /**
     * Gets the idProductoArticulo value for this OdtEagerTO.
     * 
     * @return idProductoArticulo
     */
    public java.lang.Integer getIdProductoArticulo() {
        return idProductoArticulo;
    }


    /**
     * Sets the idProductoArticulo value for this OdtEagerTO.
     * 
     * @param idProductoArticulo
     */
    public void setIdProductoArticulo(java.lang.Integer idProductoArticulo) {
        this.idProductoArticulo = idProductoArticulo;
    }


    /**
     * Gets the ordenEnMaquina value for this OdtEagerTO.
     * 
     * @return ordenEnMaquina
     */
    public java.lang.Short getOrdenEnMaquina() {
        return ordenEnMaquina;
    }


    /**
     * Sets the ordenEnMaquina value for this OdtEagerTO.
     * 
     * @param ordenEnMaquina
     */
    public void setOrdenEnMaquina(java.lang.Short ordenEnMaquina) {
        this.ordenEnMaquina = ordenEnMaquina;
    }


    /**
     * Gets the piezas value for this OdtEagerTO.
     * 
     * @return piezas
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO[] getPiezas() {
        return piezas;
    }


    /**
     * Sets the piezas value for this OdtEagerTO.
     * 
     * @param piezas
     */
    public void setPiezas(ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO[] piezas) {
        this.piezas = piezas;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO getPiezas(int i) {
        return this.piezas[i];
    }

    public void setPiezas(int i, ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO _value) {
        this.piezas[i] = _value;
    }


    /**
     * Gets the remito value for this OdtEagerTO.
     * 
     * @return remito
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.RemitoEntradaTO getRemito() {
        return remito;
    }


    /**
     * Sets the remito value for this OdtEagerTO.
     * 
     * @param remito
     */
    public void setRemito(ar.com.textillevel.gui.acciones.odtwsclient.RemitoEntradaTO remito) {
        this.remito = remito;
    }


    /**
     * Gets the secuenciaDeTrabajo value for this OdtEagerTO.
     * 
     * @return secuenciaDeTrabajo
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.SecuenciaODTTO getSecuenciaDeTrabajo() {
        return secuenciaDeTrabajo;
    }


    /**
     * Sets the secuenciaDeTrabajo value for this OdtEagerTO.
     * 
     * @param secuenciaDeTrabajo
     */
    public void setSecuenciaDeTrabajo(ar.com.textillevel.gui.acciones.odtwsclient.SecuenciaODTTO secuenciaDeTrabajo) {
        this.secuenciaDeTrabajo = secuenciaDeTrabajo;
    }


    /**
     * Gets the timestampFechaODT value for this OdtEagerTO.
     * 
     * @return timestampFechaODT
     */
    public java.lang.Long getTimestampFechaODT() {
        return timestampFechaODT;
    }


    /**
     * Sets the timestampFechaODT value for this OdtEagerTO.
     * 
     * @param timestampFechaODT
     */
    public void setTimestampFechaODT(java.lang.Long timestampFechaODT) {
        this.timestampFechaODT = timestampFechaODT;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OdtEagerTO)) return false;
        OdtEagerTO other = (OdtEagerTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.idAvance==null && other.getIdAvance()==null) || 
             (this.idAvance!=null &&
              this.idAvance.equals(other.getIdAvance()))) &&
            ((this.idEstadoODT==null && other.getIdEstadoODT()==null) || 
             (this.idEstadoODT!=null &&
              this.idEstadoODT.equals(other.getIdEstadoODT()))) &&
            ((this.idMaquinaActual==null && other.getIdMaquinaActual()==null) || 
             (this.idMaquinaActual!=null &&
              this.idMaquinaActual.equals(other.getIdMaquinaActual()))) &&
            ((this.idMaquinaPrincipal==null && other.getIdMaquinaPrincipal()==null) || 
             (this.idMaquinaPrincipal!=null &&
              this.idMaquinaPrincipal.equals(other.getIdMaquinaPrincipal()))) &&
            ((this.idProductoArticulo==null && other.getIdProductoArticulo()==null) || 
             (this.idProductoArticulo!=null &&
              this.idProductoArticulo.equals(other.getIdProductoArticulo()))) &&
            ((this.ordenEnMaquina==null && other.getOrdenEnMaquina()==null) || 
             (this.ordenEnMaquina!=null &&
              this.ordenEnMaquina.equals(other.getOrdenEnMaquina()))) &&
            ((this.piezas==null && other.getPiezas()==null) || 
             (this.piezas!=null &&
              java.util.Arrays.equals(this.piezas, other.getPiezas()))) &&
            ((this.remito==null && other.getRemito()==null) || 
             (this.remito!=null &&
              this.remito.equals(other.getRemito()))) &&
            ((this.secuenciaDeTrabajo==null && other.getSecuenciaDeTrabajo()==null) || 
             (this.secuenciaDeTrabajo!=null &&
              this.secuenciaDeTrabajo.equals(other.getSecuenciaDeTrabajo()))) &&
            ((this.timestampFechaODT==null && other.getTimestampFechaODT()==null) || 
             (this.timestampFechaODT!=null &&
              this.timestampFechaODT.equals(other.getTimestampFechaODT())));
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
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIdAvance() != null) {
            _hashCode += getIdAvance().hashCode();
        }
        if (getIdEstadoODT() != null) {
            _hashCode += getIdEstadoODT().hashCode();
        }
        if (getIdMaquinaActual() != null) {
            _hashCode += getIdMaquinaActual().hashCode();
        }
        if (getIdMaquinaPrincipal() != null) {
            _hashCode += getIdMaquinaPrincipal().hashCode();
        }
        if (getIdProductoArticulo() != null) {
            _hashCode += getIdProductoArticulo().hashCode();
        }
        if (getOrdenEnMaquina() != null) {
            _hashCode += getOrdenEnMaquina().hashCode();
        }
        if (getPiezas() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPiezas());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPiezas(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRemito() != null) {
            _hashCode += getRemito().hashCode();
        }
        if (getSecuenciaDeTrabajo() != null) {
            _hashCode += getSecuenciaDeTrabajo().hashCode();
        }
        if (getTimestampFechaODT() != null) {
            _hashCode += getTimestampFechaODT().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OdtEagerTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "odtEagerTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo"));
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
        elemField.setFieldName("idAvance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAvance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "byte"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idEstadoODT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idEstadoODT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idMaquinaActual");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idMaquinaActual"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idMaquinaPrincipal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idMaquinaPrincipal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProductoArticulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idProductoArticulo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenEnMaquina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ordenEnMaquina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piezas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piezas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "piezaODTTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("remito");
        elemField.setXmlName(new javax.xml.namespace.QName("", "remito"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "remitoEntradaTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("secuenciaDeTrabajo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "secuenciaDeTrabajo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "secuenciaODTTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timestampFechaODT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "timestampFechaODT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
