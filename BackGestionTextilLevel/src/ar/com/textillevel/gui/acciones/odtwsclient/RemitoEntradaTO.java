/**
 * RemitoEntradaTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ar.com.textillevel.gui.acciones.odtwsclient;

@SuppressWarnings({"serial", "unused", "rawtypes"})
public class RemitoEntradaTO  implements java.io.Serializable {
    private java.math.BigDecimal anchoCrudo;

    private java.math.BigDecimal anchoFinal;

    private java.lang.Long dateFechaEmision;

    private java.lang.Boolean enPalet;

    private java.lang.Integer id;

    private java.lang.Integer idArticuloStock;

    private java.lang.Integer idCliente;

    private java.lang.Integer idCondicionDeVenta;

    private java.lang.Integer idPrecioMatPrima;

    private java.lang.Integer idProveedor;

    private java.lang.Integer idTarima;

    private java.lang.Integer nroRemito;

    private ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO[] odts;

    private java.math.BigDecimal pesoTotal;

    private ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO[] piezas;

    private java.lang.Integer[] productoArticuloIdsList;

    public RemitoEntradaTO() {
    }

    public RemitoEntradaTO(
           java.math.BigDecimal anchoCrudo,
           java.math.BigDecimal anchoFinal,
           java.lang.Long dateFechaEmision,
           java.lang.Boolean enPalet,
           java.lang.Integer id,
           java.lang.Integer idArticuloStock,
           java.lang.Integer idCliente,
           java.lang.Integer idCondicionDeVenta,
           java.lang.Integer idPrecioMatPrima,
           java.lang.Integer idProveedor,
           java.lang.Integer idTarima,
           java.lang.Integer nroRemito,
           ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO[] odts,
           java.math.BigDecimal pesoTotal,
           ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO[] piezas,
           java.lang.Integer[] productoArticuloIdsList) {
           this.anchoCrudo = anchoCrudo;
           this.anchoFinal = anchoFinal;
           this.dateFechaEmision = dateFechaEmision;
           this.enPalet = enPalet;
           this.id = id;
           this.idArticuloStock = idArticuloStock;
           this.idCliente = idCliente;
           this.idCondicionDeVenta = idCondicionDeVenta;
           this.idPrecioMatPrima = idPrecioMatPrima;
           this.idProveedor = idProveedor;
           this.idTarima = idTarima;
           this.nroRemito = nroRemito;
           this.odts = odts;
           this.pesoTotal = pesoTotal;
           this.piezas = piezas;
           this.productoArticuloIdsList = productoArticuloIdsList;
    }


    /**
     * Gets the anchoCrudo value for this RemitoEntradaTO.
     * 
     * @return anchoCrudo
     */
    public java.math.BigDecimal getAnchoCrudo() {
        return anchoCrudo;
    }


    /**
     * Sets the anchoCrudo value for this RemitoEntradaTO.
     * 
     * @param anchoCrudo
     */
    public void setAnchoCrudo(java.math.BigDecimal anchoCrudo) {
        this.anchoCrudo = anchoCrudo;
    }


    /**
     * Gets the anchoFinal value for this RemitoEntradaTO.
     * 
     * @return anchoFinal
     */
    public java.math.BigDecimal getAnchoFinal() {
        return anchoFinal;
    }


    /**
     * Sets the anchoFinal value for this RemitoEntradaTO.
     * 
     * @param anchoFinal
     */
    public void setAnchoFinal(java.math.BigDecimal anchoFinal) {
        this.anchoFinal = anchoFinal;
    }


    /**
     * Gets the dateFechaEmision value for this RemitoEntradaTO.
     * 
     * @return dateFechaEmision
     */
    public java.lang.Long getDateFechaEmision() {
        return dateFechaEmision;
    }


    /**
     * Sets the dateFechaEmision value for this RemitoEntradaTO.
     * 
     * @param dateFechaEmision
     */
    public void setDateFechaEmision(java.lang.Long dateFechaEmision) {
        this.dateFechaEmision = dateFechaEmision;
    }


    /**
     * Gets the enPalet value for this RemitoEntradaTO.
     * 
     * @return enPalet
     */
    public java.lang.Boolean getEnPalet() {
        return enPalet;
    }


    /**
     * Sets the enPalet value for this RemitoEntradaTO.
     * 
     * @param enPalet
     */
    public void setEnPalet(java.lang.Boolean enPalet) {
        this.enPalet = enPalet;
    }


    /**
     * Gets the id value for this RemitoEntradaTO.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this RemitoEntradaTO.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the idArticuloStock value for this RemitoEntradaTO.
     * 
     * @return idArticuloStock
     */
    public java.lang.Integer getIdArticuloStock() {
        return idArticuloStock;
    }


    /**
     * Sets the idArticuloStock value for this RemitoEntradaTO.
     * 
     * @param idArticuloStock
     */
    public void setIdArticuloStock(java.lang.Integer idArticuloStock) {
        this.idArticuloStock = idArticuloStock;
    }


    /**
     * Gets the idCliente value for this RemitoEntradaTO.
     * 
     * @return idCliente
     */
    public java.lang.Integer getIdCliente() {
        return idCliente;
    }


    /**
     * Sets the idCliente value for this RemitoEntradaTO.
     * 
     * @param idCliente
     */
    public void setIdCliente(java.lang.Integer idCliente) {
        this.idCliente = idCliente;
    }


    /**
     * Gets the idCondicionDeVenta value for this RemitoEntradaTO.
     * 
     * @return idCondicionDeVenta
     */
    public java.lang.Integer getIdCondicionDeVenta() {
        return idCondicionDeVenta;
    }


    /**
     * Sets the idCondicionDeVenta value for this RemitoEntradaTO.
     * 
     * @param idCondicionDeVenta
     */
    public void setIdCondicionDeVenta(java.lang.Integer idCondicionDeVenta) {
        this.idCondicionDeVenta = idCondicionDeVenta;
    }


    /**
     * Gets the idPrecioMatPrima value for this RemitoEntradaTO.
     * 
     * @return idPrecioMatPrima
     */
    public java.lang.Integer getIdPrecioMatPrima() {
        return idPrecioMatPrima;
    }


    /**
     * Sets the idPrecioMatPrima value for this RemitoEntradaTO.
     * 
     * @param idPrecioMatPrima
     */
    public void setIdPrecioMatPrima(java.lang.Integer idPrecioMatPrima) {
        this.idPrecioMatPrima = idPrecioMatPrima;
    }


    /**
     * Gets the idProveedor value for this RemitoEntradaTO.
     * 
     * @return idProveedor
     */
    public java.lang.Integer getIdProveedor() {
        return idProveedor;
    }


    /**
     * Sets the idProveedor value for this RemitoEntradaTO.
     * 
     * @param idProveedor
     */
    public void setIdProveedor(java.lang.Integer idProveedor) {
        this.idProveedor = idProveedor;
    }


    /**
     * Gets the idTarima value for this RemitoEntradaTO.
     * 
     * @return idTarima
     */
    public java.lang.Integer getIdTarima() {
        return idTarima;
    }


    /**
     * Sets the idTarima value for this RemitoEntradaTO.
     * 
     * @param idTarima
     */
    public void setIdTarima(java.lang.Integer idTarima) {
        this.idTarima = idTarima;
    }


    /**
     * Gets the nroRemito value for this RemitoEntradaTO.
     * 
     * @return nroRemito
     */
    public java.lang.Integer getNroRemito() {
        return nroRemito;
    }


    /**
     * Sets the nroRemito value for this RemitoEntradaTO.
     * 
     * @param nroRemito
     */
    public void setNroRemito(java.lang.Integer nroRemito) {
        this.nroRemito = nroRemito;
    }


    /**
     * Gets the odts value for this RemitoEntradaTO.
     * 
     * @return odts
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO[] getOdts() {
        return odts;
    }


    /**
     * Sets the odts value for this RemitoEntradaTO.
     * 
     * @param odts
     */
    public void setOdts(ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO[] odts) {
        this.odts = odts;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO getOdts(int i) {
        return this.odts[i];
    }

    public void setOdts(int i, ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO _value) {
        this.odts[i] = _value;
    }


    /**
     * Gets the pesoTotal value for this RemitoEntradaTO.
     * 
     * @return pesoTotal
     */
    public java.math.BigDecimal getPesoTotal() {
        return pesoTotal;
    }


    /**
     * Sets the pesoTotal value for this RemitoEntradaTO.
     * 
     * @param pesoTotal
     */
    public void setPesoTotal(java.math.BigDecimal pesoTotal) {
        this.pesoTotal = pesoTotal;
    }


    /**
     * Gets the piezas value for this RemitoEntradaTO.
     * 
     * @return piezas
     */
    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO[] getPiezas() {
        return piezas;
    }


    /**
     * Sets the piezas value for this RemitoEntradaTO.
     * 
     * @param piezas
     */
    public void setPiezas(ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO[] piezas) {
        this.piezas = piezas;
    }

    public ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO getPiezas(int i) {
        return this.piezas[i];
    }

    public void setPiezas(int i, ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO _value) {
        this.piezas[i] = _value;
    }


    /**
     * Gets the productoArticuloIdsList value for this RemitoEntradaTO.
     * 
     * @return productoArticuloIdsList
     */
    public java.lang.Integer[] getProductoArticuloIdsList() {
        return productoArticuloIdsList;
    }


    /**
     * Sets the productoArticuloIdsList value for this RemitoEntradaTO.
     * 
     * @param productoArticuloIdsList
     */
    public void setProductoArticuloIdsList(java.lang.Integer[] productoArticuloIdsList) {
        this.productoArticuloIdsList = productoArticuloIdsList;
    }

    public java.lang.Integer getProductoArticuloIdsList(int i) {
        return this.productoArticuloIdsList[i];
    }

    public void setProductoArticuloIdsList(int i, java.lang.Integer _value) {
        this.productoArticuloIdsList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RemitoEntradaTO)) return false;
        RemitoEntradaTO other = (RemitoEntradaTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anchoCrudo==null && other.getAnchoCrudo()==null) || 
             (this.anchoCrudo!=null &&
              this.anchoCrudo.equals(other.getAnchoCrudo()))) &&
            ((this.anchoFinal==null && other.getAnchoFinal()==null) || 
             (this.anchoFinal!=null &&
              this.anchoFinal.equals(other.getAnchoFinal()))) &&
            ((this.dateFechaEmision==null && other.getDateFechaEmision()==null) || 
             (this.dateFechaEmision!=null &&
              this.dateFechaEmision.equals(other.getDateFechaEmision()))) &&
            ((this.enPalet==null && other.getEnPalet()==null) || 
             (this.enPalet!=null &&
              this.enPalet.equals(other.getEnPalet()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.idArticuloStock==null && other.getIdArticuloStock()==null) || 
             (this.idArticuloStock!=null &&
              this.idArticuloStock.equals(other.getIdArticuloStock()))) &&
            ((this.idCliente==null && other.getIdCliente()==null) || 
             (this.idCliente!=null &&
              this.idCliente.equals(other.getIdCliente()))) &&
            ((this.idCondicionDeVenta==null && other.getIdCondicionDeVenta()==null) || 
             (this.idCondicionDeVenta!=null &&
              this.idCondicionDeVenta.equals(other.getIdCondicionDeVenta()))) &&
            ((this.idPrecioMatPrima==null && other.getIdPrecioMatPrima()==null) || 
             (this.idPrecioMatPrima!=null &&
              this.idPrecioMatPrima.equals(other.getIdPrecioMatPrima()))) &&
            ((this.idProveedor==null && other.getIdProveedor()==null) || 
             (this.idProveedor!=null &&
              this.idProveedor.equals(other.getIdProveedor()))) &&
            ((this.idTarima==null && other.getIdTarima()==null) || 
             (this.idTarima!=null &&
              this.idTarima.equals(other.getIdTarima()))) &&
            ((this.nroRemito==null && other.getNroRemito()==null) || 
             (this.nroRemito!=null &&
              this.nroRemito.equals(other.getNroRemito()))) &&
            ((this.odts==null && other.getOdts()==null) || 
             (this.odts!=null &&
              java.util.Arrays.equals(this.odts, other.getOdts()))) &&
            ((this.pesoTotal==null && other.getPesoTotal()==null) || 
             (this.pesoTotal!=null &&
              this.pesoTotal.equals(other.getPesoTotal()))) &&
            ((this.piezas==null && other.getPiezas()==null) || 
             (this.piezas!=null &&
              java.util.Arrays.equals(this.piezas, other.getPiezas()))) &&
            ((this.productoArticuloIdsList==null && other.getProductoArticuloIdsList()==null) || 
             (this.productoArticuloIdsList!=null &&
              java.util.Arrays.equals(this.productoArticuloIdsList, other.getProductoArticuloIdsList())));
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
        if (getAnchoCrudo() != null) {
            _hashCode += getAnchoCrudo().hashCode();
        }
        if (getAnchoFinal() != null) {
            _hashCode += getAnchoFinal().hashCode();
        }
        if (getDateFechaEmision() != null) {
            _hashCode += getDateFechaEmision().hashCode();
        }
        if (getEnPalet() != null) {
            _hashCode += getEnPalet().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIdArticuloStock() != null) {
            _hashCode += getIdArticuloStock().hashCode();
        }
        if (getIdCliente() != null) {
            _hashCode += getIdCliente().hashCode();
        }
        if (getIdCondicionDeVenta() != null) {
            _hashCode += getIdCondicionDeVenta().hashCode();
        }
        if (getIdPrecioMatPrima() != null) {
            _hashCode += getIdPrecioMatPrima().hashCode();
        }
        if (getIdProveedor() != null) {
            _hashCode += getIdProveedor().hashCode();
        }
        if (getIdTarima() != null) {
            _hashCode += getIdTarima().hashCode();
        }
        if (getNroRemito() != null) {
            _hashCode += getNroRemito().hashCode();
        }
        if (getOdts() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOdts());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOdts(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPesoTotal() != null) {
            _hashCode += getPesoTotal().hashCode();
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
        if (getProductoArticuloIdsList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProductoArticuloIdsList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProductoArticuloIdsList(), i);
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
        new org.apache.axis.description.TypeDesc(RemitoEntradaTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "remitoEntradaTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anchoCrudo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anchoCrudo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anchoFinal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anchoFinal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateFechaEmision");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateFechaEmision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enPalet");
        elemField.setXmlName(new javax.xml.namespace.QName("", "enPalet"));
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
        elemField.setFieldName("idArticuloStock");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idArticuloStock"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCondicionDeVenta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idCondicionDeVenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPrecioMatPrima");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idPrecioMatPrima"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProveedor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idProveedor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTarima");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTarima"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("odts");
        elemField.setXmlName(new javax.xml.namespace.QName("", "odts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "odtEagerTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pesoTotal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pesoTotal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piezas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piezas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.odt.webservices.textillevel.com.ar/", "piezaRemitoTO"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productoArticuloIdsList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "productoArticuloIdsList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
