package ar.com.textillevel.gui.acciones.impresionremito;

import java.awt.Frame;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.gui.modulos.odt.util.ODTDatosMostradoHelper;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.JasperHelper;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ImpresionRemitoEntradaFichaHandler {

	private static final String ARCHIVO_JASPER = "/ar/com/textillevel/reportes/remito_entrada_ficha.jasper";
	private static final String ARCHIVO_JASPER_REVERSO = "/ar/com/textillevel/reportes/remito_entrada_ficha_reverso.jasper";

	private final OrdenDeTrabajo odt;
	private Frame frameOwner;

	public ImpresionRemitoEntradaFichaHandler(OrdenDeTrabajo odt, Frame frameOwner) {
		this.frameOwner = frameOwner;
		this.odt = odt;
	}

	public void imprimir() {
		FWJOptionPane.showInformationMessage(frameOwner, "Se imprimirá la primer cara de la ODT: " + odt.getCodigo(), "Información");
		imprimirPrimerCara();
		FWJOptionPane.showInformationMessage(frameOwner, "Se imprimirá la segunda cara de la ODT: " + odt.getCodigo(), "Información");
		imprimirSegundaCara();
	}

	private void imprimirPrimerCara() {
		JasperReport reporte = JasperHelper.loadReporte(ARCHIVO_JASPER);
		ODTTO odtto = new ODTTO(this.odt);
		try {
			JasperPrint jasperPrint = JasperHelper.fillReport(reporte, odtto.getMapaParametros(), Collections.singletonList(odtto));
			JasperHelper.imprimirReporte(jasperPrint, true, true, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void imprimirSegundaCara() {
		JasperReport reporte = JasperHelper.loadReporte(ARCHIVO_JASPER_REVERSO);
		ODTTOReverso odtto = new ODTTOReverso(this.odt);
		try {
			JasperPrint jasperPrint = JasperHelper.fillReport(reporte, odtto.getMapaParametros(), Collections.singletonList(odtto));
			JasperHelper.imprimirReporte(jasperPrint, true, true, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class ODTTO implements Serializable {

		private static final long serialVersionUID = -2142756333577045834L;

		private final String fecha;
		private final String cliente;
		private final String piezas;
		private final String nombreTipoArticulo;
		private final String anchoArticulo;
		private final String totalMetros;
		private final String tarima;
		private final String totalKilos;
		private final String codigoBarras;
		private final String control;
		private final String remito;
		private final String observaciones;


		public ODTTO(final OrdenDeTrabajo odt) {
			ODTDatosMostradoHelper odtDatosHelper = new ODTDatosMostradoHelper(odt);
			this.fecha = DateUtil.dateToString(odt.getRemito().getFechaEmision());
			this.cliente = odtDatosHelper.getDescCliente();
			this.piezas = odt.getPiezas().size() + "";
			this.nombreTipoArticulo = odtDatosHelper.getDescTipoArticulo();
			this.anchoArticulo = odtDatosHelper.getDescAnchoArticulo();
			this.totalMetros = odt.getTotalMetrosEntrada().toString();
			this.tarima = odtDatosHelper.getDescTarima();
			this.totalKilos = odt.getRemito().getPesoTotal().toString();
			this.codigoBarras = odt.getCodigo();
			this.control = odt.getRemito().getControl() == null ? "" : odt.getRemito().getControl();
			this.remito = odt.getRemito().getNroRemito() + "";
			this.observaciones = calcularObs(odt.getRemito().getPiezas());
		}

		private String calcularObs(List<PiezaRemito> piezasRemito) {
			StringBuilder sb = new StringBuilder("");
			for(PiezaRemito pr : piezasRemito) {
				if(!StringUtil.isNullOrEmpty(pr.getObservaciones())) {
					sb.append(pr.getObservaciones() + " ");
				}
			}
			return sb.toString().length() == 0 ? "" : sb.toString().substring(0, 30);
		}

		public Map<String, Object> getMapaParametros() throws IOException {
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("CLIENTE", this.cliente);
			mapa.put("FECHA", this.fecha);
			mapa.put("PIEZAS", this.piezas);
			mapa.put("TART_NOMBRE", this.nombreTipoArticulo);
			mapa.put("ART_ANCHO", this.anchoArticulo);
			mapa.put("TOTAL_METROS", this.totalMetros);
			mapa.put("TARIMA", StringUtil.isNullOrEmpty(this.tarima) ? " --- " : this.tarima);
			mapa.put("TOTAL_KILOS", this.totalKilos);
			mapa.put("CONTROL", this.control);
			mapa.put("REMITO", this.remito);
			mapa.put("IMAGEN", GenericUtils.createBarCode(this.codigoBarras));		
			mapa.put("OBS", this.observaciones);
			mapa.put("con", new JRBeanCollectionDataSource(Collections.singletonList(this)));
			return mapa;
		}

	}
	
	public static class ODTTOReverso implements Serializable {

		private static final int CANT_PIEZAS_COLUMNA = 12;
		
		private static final long serialVersionUID = -2142756333577045834L;

		private List<PiezaResumenTO> piezas1;
		private List<PiezaResumenTO> piezas2;
		private List<PiezaResumenTO> piezas3;
		private List<PiezaResumenTO> piezas4;
		private List<PiezaResumenTO> piezas5;

		public ODTTOReverso(final OrdenDeTrabajo odt) {
			piezas1 = new ArrayList<PiezaResumenTO>();
			piezas2 = new ArrayList<PiezaResumenTO>();
			piezas3 = new ArrayList<PiezaResumenTO>();
			piezas4 = new ArrayList<PiezaResumenTO>();
			piezas5 = new ArrayList<PiezaResumenTO>();
			for (int i=1; i <= odt.getPiezas().size(); i++) {
				PiezaResumenTO p = new PiezaResumenTO();
				p.setNroPieza(new Short(i+""));
				p.setMetros(odt.getPiezas().get(i-1).getPiezaRemito().getMetros().toString());
				getPiezasDS(i).add(p);
			}
		}

		private List<PiezaResumenTO> getPiezasDS(int nroPieza) {
			if(nroPieza <= CANT_PIEZAS_COLUMNA) {
				return piezas1;
			} else if(nroPieza > CANT_PIEZAS_COLUMNA && nroPieza <= 2*CANT_PIEZAS_COLUMNA) {
				return piezas2;
			} else if(nroPieza > 2*CANT_PIEZAS_COLUMNA && nroPieza <= 3*CANT_PIEZAS_COLUMNA) {
				return piezas3;
			} else if(nroPieza > 3*CANT_PIEZAS_COLUMNA && nroPieza <= 4*CANT_PIEZAS_COLUMNA){
				return piezas4;
			} else {
				return piezas5;
			}
		}

		public Map<String, Object> getMapaParametros() throws IOException {
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("piezasDS1", new JRBeanCollectionDataSource(piezas1));
			mapa.put("piezasDS2", new JRBeanCollectionDataSource(piezas2));
			mapa.put("piezasDS3", new JRBeanCollectionDataSource(piezas3));
			mapa.put("piezasDS4", new JRBeanCollectionDataSource(piezas4));
			mapa.put("piezasDS5", new JRBeanCollectionDataSource(piezas5));
			mapa.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");
			mapa.put("con", new JRBeanCollectionDataSource(Collections.singletonList(this)));			
			return mapa;	
		}

		public JRDataSource getPiezasDS() {
			return new JRBeanCollectionDataSource(piezas1);
		}

	}

	public static class PiezaResumenTO {
		
		private Short nroPieza;
		private String metros;

		public PiezaResumenTO() {
			
		}
		
		public Short getNroPieza() {
			return nroPieza;
		}

		public void setNroPieza(Short nroPieza) {
			this.nroPieza = nroPieza;
		}

		public String getMetros() {
			return metros;
		}

		public void setMetros(String metros) {
			this.metros = metros;
		}
		
	}

}
