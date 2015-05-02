package main;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

@SuppressWarnings({"unchecked","rawtypes"})
public class TestJasperLista {

	public TestJasperLista() throws JRException {
		FacturaTO facturaTO = getFacturaTO();
		Map parameters = new HashMap();
		// parameters.put("ITEMS", facturaTO.getItems());
		parameters.put("MONTO", facturaTO.getMonto());
		net.sf.jasperreports.engine.JasperReport jasperCompilado = null;
		jasperCompilado = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/ar/com/textillevel/reportes/testlista.jasper"));
		// List<String> singletonList = Collections.singletonList("nacho capo");
		// parameters.put("FECHA", new java.util.Date());
		// InputStream is =
		// this.getClass().getResourceAsStream("/main/testfactura.jrxml");
		// JasperReport report = JasperCompileManager.compileReport(is);
		System.out.println("lleno");
		JasperPrint print = JasperFillManager.fillReport(jasperCompilado, parameters, new JRBeanCollectionDataSource(facturaTO.getItems()));
		System.out.println("fin lleno. Exporto");
		// Exporta el informe a PDF
		JasperExportManager.exportReportToPdfFile(print, "test.pdf");
		System.out.println("fin exporto");
		// Para visualizar el pdf directamente desde java
		JasperViewer.viewReport(print, false);

	}

	private FacturaTO getFacturaTO() {
		FacturaTO facto = new FacturaTO();
		facto.setMonto(new BigDecimal(200.5f));
		ItemFacturaTO it1 = new ItemFacturaTO();
		it1.setDescripcion("item 1");
		it1.setPrecioUnitario(new BigDecimal(100f));
		facto.getItems().add(it1);
		it1 = new ItemFacturaTO();
		it1.setDescripcion("item 10000");
		it1.setPrecioUnitario(new BigDecimal(500f));
		facto.getItems().add(it1);
		return facto;
	}

	public static void main(String[] args) {
		try {
			new TestJasperLista();
			System.exit(1);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public class FacturaTO implements Serializable {

		private static final long serialVersionUID = 4909109560343000452L;

		private BigDecimal monto;
		private List<ItemFacturaTO> items;

		public FacturaTO() {
			items = new ArrayList<ItemFacturaTO>();
		}

		public BigDecimal getMonto() {
			return monto;
		}

		public void setMonto(BigDecimal monto) {
			this.monto = monto;
		}

		public List<ItemFacturaTO> getItems() {
			return items;
		}

		public void setItems(List<ItemFacturaTO> items) {
			this.items = items;
		}
	}

	public class ItemFacturaTO implements Serializable {

		private static final long serialVersionUID = -3290822577974290984L;

		private BigDecimal precioUnitario;
		private String descripcion;

		public BigDecimal getPrecioUnitario() {
			return precioUnitario;
		}

		public void setPrecioUnitario(BigDecimal precioUnitario) {
			this.precioUnitario = precioUnitario;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
	}
}
