package ar.com.textillevel.entidades.documentos.remito.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ar.clarin.fwjava.componentes.error.CLRuntimeException;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.util.ODTCodigoHelper;
import ar.com.textillevel.util.Utils;

public class RemitoEntradaMobileTO implements Serializable {

	private static final long serialVersionUID = 5262895614447579804L;

	private CuentaOwnerTO owner;
	private String nroRemito;
	private Integer idDocumento;
	private Integer idTipoDocumento;
	private String fecha;
	private String pesoTotal;
	private String anchoCrudo;
	private String anchoFinal;
	private String totalMetros;
	private String productos;
	private String tarima;
	private Boolean enPalet;
	private List<PiezaRemitoMobileTO> piezas;

	public RemitoEntradaMobileTO(RemitoEntrada remito, List<OrdenDeTrabajo> odtList) {
		if(remito.getArticuloStock() == null || remito.getCliente() == null){ // remito normal o 01
			this.owner = new CuentaOwnerTO(remito.getCliente());
		}else if(remito.getCliente() == null){ //remito compra tela 
			this.owner = new CuentaOwnerTO(remito.getProveedor());
		}
		
		this.nroRemito = String.valueOf(remito.getNroRemito());
		this.idDocumento = remito.getId();
		this.idTipoDocumento = ETipoDocumento.REMITO_ENTRADA.getId();
		this.fecha = DateUtil.dateToString(remito.getFechaEmision());
		this.pesoTotal = Utils.getDecimalFormat().format(remito.getPesoTotal().doubleValue());
		this.anchoCrudo = Utils.getDecimalFormat().format(remito.getAnchoCrudo().doubleValue());
		this.anchoFinal = Utils.getDecimalFormat().format(remito.getAnchoFinal().doubleValue());
		this.totalMetros = Utils.getDecimalFormat().format(remito.getTotalMetros().doubleValue());
		this.productos = StringUtil.getCadena(remito.getProductoList(), ", ");
		if(remito.getTarima()!=null){
			this.tarima = String.valueOf(remito.getTarima().getNumero());
		}else{
			this.tarima = " - ";
		}
		this.enPalet = remito.getEnPalet();
		cargarPiezas(remito,odtList);
	}

	private void cargarPiezas(RemitoEntrada remito, List<OrdenDeTrabajo> odtList) {
		this.piezas = new ArrayList<PiezaRemitoMobileTO>();
		unificarInstanciasEnODTs(remito, odtList);
		Map<PiezaRemito, OrdenDeTrabajo> odtPiezaMap = new HashMap<PiezaRemito, OrdenDeTrabajo>();
		for(OrdenDeTrabajo odt : odtList) {
			for(PiezaODT piezaODT : odt.getPiezas()) {
				odtPiezaMap.put(piezaODT.getPiezaRemito(), odt);
			}
		}
		for(PiezaRemito p : remito.getPiezas()){
			PiezaRemitoMobileTO pm = new PiezaRemitoMobileTO();
			pm.setMetros(Utils.getDecimalFormat().format(p.getMetros().doubleValue()));
			pm.setNumero(String.valueOf(p.getOrdenPieza()));
			pm.setObservaciones(p.getObservaciones());
			for(Entry<PiezaRemito, OrdenDeTrabajo> entry : odtPiezaMap.entrySet()) {
				if(entry.getKey().getId().equals(p.getId())) {
					pm.setOdt(ODTCodigoHelper.getInstance().formatCodigo(entry.getValue().getCodigo()));
				}
			}
			this.piezas.add(pm);
		}
	}

	private void unificarInstanciasEnODTs(RemitoEntrada remito, List<OrdenDeTrabajo> odtList) {
		for(OrdenDeTrabajo odt : odtList) {
			for(PiezaODT podt : odt.getPiezas()) {
				PiezaRemito pr = getPiezaRemito(remito.getPiezas(), podt.getPiezaRemito());
				if(pr == null) {
					throw new CLRuntimeException("Estado inconsistente!!!");
				}
				podt.setPiezaRemito(pr);
			}
		}
	}
	
	private PiezaRemito getPiezaRemito(List<PiezaRemito> piezas, PiezaRemito piezaRemito) {
		for(PiezaRemito pr : piezas) {
			if(pr.equals(piezaRemito)) {
				return pr;
			}
		}
		return null;
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

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
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

	public String getAnchoCrudo() {
		return anchoCrudo;
	}

	public void setAnchoCrudo(String anchoCrudo) {
		this.anchoCrudo = anchoCrudo;
	}

	public String getAnchoFinal() {
		return anchoFinal;
	}

	public void setAnchoFinal(String anchoFinal) {
		this.anchoFinal = anchoFinal;
	}

	public String getProductos() {
		return productos;
	}

	public void setProductos(String productos) {
		this.productos = productos;
	}

	public String getTarima() {
		return tarima;
	}

	public void setTarima(String tarima) {
		this.tarima = tarima;
	}

	public Boolean getEnPalet() {
		return enPalet;
	}

	public void setEnPalet(Boolean enPalet) {
		this.enPalet = enPalet;
	}

	public String getTotalMetros() {
		return totalMetros;
	}

	public void setTotalMetros(String totalMetros) {
		this.totalMetros = totalMetros;
	}

	public List<PiezaRemitoMobileTO> getPiezas() {
		return piezas;
	}

	public void setPiezas(List<PiezaRemitoMobileTO> piezas) {
		this.piezas = piezas;
	}
}
