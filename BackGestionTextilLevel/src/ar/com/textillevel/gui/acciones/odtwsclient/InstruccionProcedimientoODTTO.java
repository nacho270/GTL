/**
 * InstruccionProcedimientoODTTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class InstruccionProcedimientoODTTO  implements java.io.Serializable {
    private java.lang.Integer accionProcedimientoId;

    private java.lang.Integer cantidadPasadas;

    private java.lang.String especificacion;

    private ar.com.textillevel.gui.acciones.odtwsclient.FormulaClienteExplotadaTO formula;

    private java.lang.Integer idTipoArticulo;

    private java.lang.Integer idTipoProducto;

    private java.lang.Byte idTipoSector;

    private ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] mpCantidadExplotadas;

    private java.lang.String observaciones;

    private java.lang.Float temperatura;

    private java.lang.String tipo;

    private java.lang.Float velocidad;

    public InstruccionProcedimientoODTTO() {
    }

    public InstruccionProcedimientoODTTO(
           java.lang.Integer accionProcedimientoId,
           java.lang.Integer cantidadPasadas,
           java.lang.String especificacion,
           ar.com.textillevel.gui.acciones.odtwsclient.FormulaClienteExplotadaTO formula,
           java.lang.Integer idTipoArticulo,
           java.lang.Integer idTipoProducto,
           java.lang.Byte idTipoSector,
           ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] mpCantidadExplotadas,
           java.lang.String observaciones,
           java.lang.Float temperatura,
           java.lang.String tipo,
           java.lang.Float velocidad) {
           this.accionProcedimientoId = accionProcedimientoId;
           this.cantidadPasadas = cantidadPasadas;
           this.especificacion = especificacion;
           this.formula = formula;
           this.idTipoArticulo = idTipoArticulo;
           this.idTipoProducto = idTipoProducto;
           this.idTipoSector = idTipoSector;
           this.mpCantidadExplotadas = mpCantidadExplotadas;
           this.observaciones = observaciones;
           this.temperatura = temperatura;
           this.tipo = tipo;
           this.velocidad = velocidad;
    }


    /**
     * Gets the accionProcedimientoId value for this InstruccionProcedimientoODTTO.
     * 
     * @return accionProcedimientoId
     */
    public java.lang.Integer getAccionProcedimientoId() {
        return accionProcedimientoId;
    }


    /**
     * Sets the accionProcedimientoId value for this InstruccionProcedimientoODTTO.
     * 
     * @param accionProcedimientoId
     */
    public void setAccionProcedimientoId(java.lang.Integer accionProcedimientoId) {
        this.accionProcedimientoId = accionProcedimientoId;
    }


    /**
     * Gets the cantidadPasadas value for this InstruccionProcedimientoODTTO.
     * 
     * @return cantidadPasadas
     */
    public java.lang.Integer getCantidadPasadas() {
        return cantidadPasadas;
    }


    /**
     * Sets the cantidadPasadas value for this InstruccionProcedimientoODTTO.
     * 
     * @param cantidadPasadas
     */
    public void setCantidadPasadas(java.lang.Integer cantidadPasadas) {
        this.cantidadPasadas = cantidadPasadas;
    }


    /**
     * Gets the especificacion value for this InstruccionProcedimientoODTTO.
     * 
     * @return especificacion
     */
    public java.lang.String getEspecificacion() {
        return especificacion;
    }


    /**
     * Sets the especificacion value for this InstruccionProcedimientoODTTO.
     * 
     * @param especificacion
     */
    public void setEspecificacion(java.lang.String especificacion) {
        this.especificacion = especificacion;
    }


    /**
     * Gets the formula value for this InstruccionProcedimientoODTTO.
     * 
     * @return formula
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.FormulaClienteExplotadaTO getFormula() {
        return formula;
    }


    /**
     * Sets the formula value for this InstruccionProcedimientoODTTO.
     * 
     * @param formula
     */
    public void setFormula(ar.com.textillevel.gui.acciones.odtwsclient.FormulaClienteExplotadaTO formula) {
        this.formula = formula;
    }


    /**
     * Gets the idTipoArticulo value for this InstruccionProcedimientoODTTO.
     * 
     * @return idTipoArticulo
     */
    public java.lang.Integer getIdTipoArticulo() {
        return idTipoArticulo;
    }


    /**
     * Sets the idTipoArticulo value for this InstruccionProcedimientoODTTO.
     * 
     * @param idTipoArticulo
     */
    public void setIdTipoArticulo(java.lang.Integer idTipoArticulo) {
        this.idTipoArticulo = idTipoArticulo;
    }


    /**
     * Gets the idTipoProducto value for this InstruccionProcedimientoODTTO.
     * 
     * @return idTipoProducto
     */
    public java.lang.Integer getIdTipoProducto() {
        return idTipoProducto;
    }


    /**
     * Sets the idTipoProducto value for this InstruccionProcedimientoODTTO.
     * 
     * @param idTipoProducto
     */
    public void setIdTipoProducto(java.lang.Integer idTipoProducto) {
        this.idTipoProducto = idTipoProducto;
    }


    /**
     * Gets the idTipoSector value for this InstruccionProcedimientoODTTO.
     * 
     * @return idTipoSector
     */
    public java.lang.Byte getIdTipoSector() {
        return idTipoSector;
    }


    /**
     * Sets the idTipoSector value for this InstruccionProcedimientoODTTO.
     * 
     * @param idTipoSector
     */
    public void setIdTipoSector(java.lang.Byte idTipoSector) {
        this.idTipoSector = idTipoSector;
    }


    /**
     * Gets the mpCantidadExplotadas value for this InstruccionProcedimientoODTTO.
     * 
     * @return mpCantidadExplotadas
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] getMpCantidadExplotadas() {
        return mpCantidadExplotadas;
    }


    /**
     * Sets the mpCantidadExplotadas value for this InstruccionProcedimientoODTTO.
     * 
     * @param mpCantidadExplotadas
     */
    public void setMpCantidadExplotadas(ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] mpCantidadExplotadas) {
        this.mpCantidadExplotadas = mpCantidadExplotadas;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO getMpCantidadExplotadas(int i) {
        return this.mpCantidadExplotadas[i];
    }

    public void setMpCantidadExplotadas(int i, ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO _value) {
        this.mpCantidadExplotadas[i] = _value;
    }


    /**
     * Gets the observaciones value for this InstruccionProcedimientoODTTO.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this InstruccionProcedimientoODTTO.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }


    /**
     * Gets the temperatura value for this InstruccionProcedimientoODTTO.
     * 
     * @return temperatura
     */
    public java.lang.Float getTemperatura() {
        return temperatura;
    }


    /**
     * Sets the temperatura value for this InstruccionProcedimientoODTTO.
     * 
     * @param temperatura
     */
    public void setTemperatura(java.lang.Float temperatura) {
        this.temperatura = temperatura;
    }


    /**
     * Gets the tipo value for this InstruccionProcedimientoODTTO.
     * 
     * @return tipo
     */
    public java.lang.String getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this InstruccionProcedimientoODTTO.
     * 
     * @param tipo
     */
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the velocidad value for this InstruccionProcedimientoODTTO.
     * 
     * @return velocidad
     */
    public java.lang.Float getVelocidad() {
        return velocidad;
    }


    /**
     * Sets the velocidad value for this InstruccionProcedimientoODTTO.
     * 
     * @param velocidad
     */
    public void setVelocidad(java.lang.Float velocidad) {
        this.velocidad = velocidad;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InstruccionProcedimientoODTTO)) return false;
        InstruccionProcedimientoODTTO other = (InstruccionProcedimientoODTTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.accionProcedimientoId==null && other.getAccionProcedimientoId()==null) || 
             (this.accionProcedimientoId!=null &&
              this.accionProcedimientoId.equals(other.getAccionProcedimientoId()))) &&
            ((this.cantidadPasadas==null && other.getCantidadPasadas()==null) || 
             (this.cantidadPasadas!=null &&
              this.cantidadPasadas.equals(other.getCantidadPasadas()))) &&
            ((this.especificacion==null && other.getEspecificacion()==null) || 
             (this.especificacion!=null &&
              this.especificacion.equals(other.getEspecificacion()))) &&
            ((this.formula==null && other.getFormula()==null) || 
             (this.formula!=null &&
              this.formula.equals(other.getFormula()))) &&
            ((this.idTipoArticulo==null && other.getIdTipoArticulo()==null) || 
             (this.idTipoArticulo!=null &&
              this.idTipoArticulo.equals(other.getIdTipoArticulo()))) &&
            ((this.idTipoProducto==null && other.getIdTipoProducto()==null) || 
             (this.idTipoProducto!=null &&
              this.idTipoProducto.equals(other.getIdTipoProducto()))) &&
            ((this.idTipoSector==null && other.getIdTipoSector()==null) || 
             (this.idTipoSector!=null &&
              this.idTipoSector.equals(other.getIdTipoSector()))) &&
            ((this.mpCantidadExplotadas==null && other.getMpCantidadExplotadas()==null) || 
             (this.mpCantidadExplotadas!=null &&
              java.util.Arrays.equals(this.mpCantidadExplotadas, other.getMpCantidadExplotadas()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones()))) &&
            ((this.temperatura==null && other.getTemperatura()==null) || 
             (this.temperatura!=null &&
              this.temperatura.equals(other.getTemperatura()))) &&
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo()))) &&
            ((this.velocidad==null && other.getVelocidad()==null) || 
             (this.velocidad!=null &&
              this.velocidad.equals(other.getVelocidad())));
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
        if (getAccionProcedimientoId() != null) {
            _hashCode += getAccionProcedimientoId().hashCode();
        }
        if (getCantidadPasadas() != null) {
            _hashCode += getCantidadPasadas().hashCode();
        }
        if (getEspecificacion() != null) {
            _hashCode += getEspecificacion().hashCode();
        }
        if (getFormula() != null) {
            _hashCode += getFormula().hashCode();
        }
        if (getIdTipoArticulo() != null) {
            _hashCode += getIdTipoArticulo().hashCode();
        }
        if (getIdTipoProducto() != null) {
            _hashCode += getIdTipoProducto().hashCode();
        }
        if (getIdTipoSector() != null) {
            _hashCode += getIdTipoSector().hashCode();
        }
        if (getMpCantidadExplotadas() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMpCantidadExplotadas());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMpCantidadExplotadas(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        if (getTemperatura() != null) {
            _hashCode += getTemperatura().hashCode();
        }
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        if (getVelocidad() != null) {
            _hashCode += getVelocidad().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InstruccionProcedimientoODTTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "instruccionProcedimientoODTTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accionProcedimientoId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "accionProcedimientoId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cantidadPasadas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cantidadPasadas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("especificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "especificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formula");
        elemField.setXmlName(new javax.xml.namespace.QName("", "formula"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "formulaClienteExplotadaTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoArticulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTipoArticulo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoProducto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTipoProducto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoSector");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTipoSector"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "byte"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mpCantidadExplotadas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mpCantidadExplotadas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "materiaPrimaCantidadExplotadaTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("temperatura");
        elemField.setXmlName(new javax.xml.namespace.QName("", "temperatura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("velocidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "velocidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
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
