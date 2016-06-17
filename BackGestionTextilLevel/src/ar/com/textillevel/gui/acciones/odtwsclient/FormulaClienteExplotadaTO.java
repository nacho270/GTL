/**
 * FormulaClienteExplotadaTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class FormulaClienteExplotadaTO  implements java.io.Serializable {
    private ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] anilinas;

    private java.lang.Integer idFormulaDesencadenante;

    private ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] pigmentos;

    private ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] quimicos;

    private java.lang.String tipo;

    public FormulaClienteExplotadaTO() {
    }

    public FormulaClienteExplotadaTO(
           ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] anilinas,
           java.lang.Integer idFormulaDesencadenante,
           ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] pigmentos,
           ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] quimicos,
           java.lang.String tipo) {
           this.anilinas = anilinas;
           this.idFormulaDesencadenante = idFormulaDesencadenante;
           this.pigmentos = pigmentos;
           this.quimicos = quimicos;
           this.tipo = tipo;
    }


    /**
     * Gets the anilinas value for this FormulaClienteExplotadaTO.
     * 
     * @return anilinas
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] getAnilinas() {
        return anilinas;
    }


    /**
     * Sets the anilinas value for this FormulaClienteExplotadaTO.
     * 
     * @param anilinas
     */
    public void setAnilinas(ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] anilinas) {
        this.anilinas = anilinas;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO getAnilinas(int i) {
        return this.anilinas[i];
    }

    public void setAnilinas(int i, ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO _value) {
        this.anilinas[i] = _value;
    }


    /**
     * Gets the idFormulaDesencadenante value for this FormulaClienteExplotadaTO.
     * 
     * @return idFormulaDesencadenante
     */
    public java.lang.Integer getIdFormulaDesencadenante() {
        return idFormulaDesencadenante;
    }


    /**
     * Sets the idFormulaDesencadenante value for this FormulaClienteExplotadaTO.
     * 
     * @param idFormulaDesencadenante
     */
    public void setIdFormulaDesencadenante(java.lang.Integer idFormulaDesencadenante) {
        this.idFormulaDesencadenante = idFormulaDesencadenante;
    }


    /**
     * Gets the pigmentos value for this FormulaClienteExplotadaTO.
     * 
     * @return pigmentos
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] getPigmentos() {
        return pigmentos;
    }


    /**
     * Sets the pigmentos value for this FormulaClienteExplotadaTO.
     * 
     * @param pigmentos
     */
    public void setPigmentos(ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] pigmentos) {
        this.pigmentos = pigmentos;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO getPigmentos(int i) {
        return this.pigmentos[i];
    }

    public void setPigmentos(int i, ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO _value) {
        this.pigmentos[i] = _value;
    }


    /**
     * Gets the quimicos value for this FormulaClienteExplotadaTO.
     * 
     * @return quimicos
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] getQuimicos() {
        return quimicos;
    }


    /**
     * Sets the quimicos value for this FormulaClienteExplotadaTO.
     * 
     * @param quimicos
     */
    public void setQuimicos(ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO[] quimicos) {
        this.quimicos = quimicos;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO getQuimicos(int i) {
        return this.quimicos[i];
    }

    public void setQuimicos(int i, ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO _value) {
        this.quimicos[i] = _value;
    }


    /**
     * Gets the tipo value for this FormulaClienteExplotadaTO.
     * 
     * @return tipo
     */
    public java.lang.String getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this FormulaClienteExplotadaTO.
     * 
     * @param tipo
     */
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FormulaClienteExplotadaTO)) return false;
        FormulaClienteExplotadaTO other = (FormulaClienteExplotadaTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anilinas==null && other.getAnilinas()==null) || 
             (this.anilinas!=null &&
              java.util.Arrays.equals(this.anilinas, other.getAnilinas()))) &&
            ((this.idFormulaDesencadenante==null && other.getIdFormulaDesencadenante()==null) || 
             (this.idFormulaDesencadenante!=null &&
              this.idFormulaDesencadenante.equals(other.getIdFormulaDesencadenante()))) &&
            ((this.pigmentos==null && other.getPigmentos()==null) || 
             (this.pigmentos!=null &&
              java.util.Arrays.equals(this.pigmentos, other.getPigmentos()))) &&
            ((this.quimicos==null && other.getQuimicos()==null) || 
             (this.quimicos!=null &&
              java.util.Arrays.equals(this.quimicos, other.getQuimicos()))) &&
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo())));
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
        if (getAnilinas() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnilinas());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnilinas(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdFormulaDesencadenante() != null) {
            _hashCode += getIdFormulaDesencadenante().hashCode();
        }
        if (getPigmentos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPigmentos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPigmentos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getQuimicos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getQuimicos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getQuimicos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FormulaClienteExplotadaTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "formulaClienteExplotadaTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anilinas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anilinas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "materiaPrimaCantidadExplotadaTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idFormulaDesencadenante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idFormulaDesencadenante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pigmentos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pigmentos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "materiaPrimaCantidadExplotadaTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quimicos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "quimicos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "materiaPrimaCantidadExplotadaTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo"));
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
