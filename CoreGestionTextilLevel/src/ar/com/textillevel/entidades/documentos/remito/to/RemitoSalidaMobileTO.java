package ar.com.textillevel.entidades.documentos.remito.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.documentos.to.DocumentoRelMobTO;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.util.Utils;

public class RemitoSalidaMobileTO implements Serializable {

	private static final long serialVersionUID = -235914064511919664L;

	private CuentaOwnerTO owner;
	private String nroRemito;
	private String fecha;
	private String pesoTotal;
	private String totalMetros;
	private String productos;
	private String odts;
	private List<PiezaRemitoMobileTO> piezas;
	private List<DocumentoRelMobTO> remitosEntrada;

	public RemitoSalidaMobileTO(RemitoSalida rs) {
		if(rs.getCliente() != null) {
			this.owner = new CuentaOwnerTO(rs.getCliente());
		}else if(rs.getProveedor() != null){ 
			this.owner = new CuentaOwnerTO(rs.getProveedor());
		}
		this.odts = calcularODTs(rs.getOdts());
		this.nroRemito = rs.getNroRemito().toString();
		this.fecha = DateUtil.dateToString(rs.getFechaEmision());
		this.pesoTotal = Utils.getDecimalFormat().format(rs.getPesoTotal().doubleValue());
		this.productos = StringUtil.getCadena(rs.getProductoList(), ", ");
		this.piezas = calcularPiezas(rs.getPiezas());
		this.totalMetros = Utils.getDecimalFormat().format(rs.getTotalMetros().doubleValue());
		this.remitosEntrada = calcularRemitos(rs.getOdts());
	}

	private List<DocumentoRelMobTO> calcularRemitos(List<OrdenDeTrabajo> odts) {
		List<DocumentoRelMobTO> remitosList = new ArrayList<DocumentoRelMobTO>();
		for(OrdenDeTrabajo odt : odts) {
			RemitoEntrada remitoEntrada = odt.getRemito();
			if(remitoEntrada != null) {
				remitosList.add(new DocumentoRelMobTO(remitoEntrada.getId(), ETipoDocumento.REMITO_ENTRADA.getId(), String.valueOf(remitoEntrada.getNroRemito())));
			}
		}
		return remitosList;
	}

	private String calcularODTs(List<OrdenDeTrabajo> odts) {
		List<String> odtsStr = new ArrayList<String>();
		for(OrdenDeTrabajo odt : odts) {
			odtsStr.add(odt.getCodigo());
		}
		return StringUtil.getCadena(odtsStr, ", ");
	}

	public CuentaOwnerTO getOwner() {
		return owner;
	}

	public void setOwner(CuentaOwnerTO owner) {
		this.owner = owner;
	}

	public String getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(String nroRemito) {
		this.nroRemito = nroRemito;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(String pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public String getTotalMetros() {
		return totalMetros;
	}

	public void setTotalMetros(String totalMetros) {
		this.totalMetros = totalMetros;
	}

	public String getProductos() {
		return productos;
	}

	public void setProductos(String productos) {
		this.productos = productos;
	}

	public String getOdts() {
		return odts;
	}

	public void setOdts(String odts) {
		this.odts = odts;
	}

	public List<PiezaRemitoMobileTO> getPiezas() {
		return piezas;
	}

	public List<DocumentoRelMobTO> getRemitosEntrada() {
		return remitosEntrada;
	}
	
	private List<PiezaRemitoMobileTO> calcularPiezas(List<PiezaRemito> piezas) {
		List<PiezaRemitoMobileTO> piezasTO = new ArrayList<PiezaRemitoMobileTO>();
		for(PiezaRemito pr : piezas) {
			PiezaRemitoMobileTO prto = new PiezaRemitoMobileTO();
			prto.setMetros(Utils.getDecimalFormat().format(pr.getMetros().doubleValue()));
			prto.setNumero(pr.getOrdenPieza().toString());
			prto.setObservaciones(pr.getObservaciones());
			if(pr.getPiezaEntrada() != null) {
				prto.setMetrosEntrada(Utils.getDecimalFormat().format(getSumMetros(pr).doubleValue()));
			}
			piezasTO.add(prto);
		}
		return piezasTO;
	}

	private BigDecimal getSumMetros(PiezaRemito elemento) {
		BigDecimal sum = new BigDecimal(0);
		for(PiezaODT podt : elemento.getPiezasPadreODT()) {
			sum = sum.add(podt.getPiezaRemito().getMetros());
		}
		return sum;
	}

}