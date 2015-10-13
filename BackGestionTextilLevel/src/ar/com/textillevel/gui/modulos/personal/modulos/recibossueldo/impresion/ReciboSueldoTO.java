package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.impresion;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.util.AnotacionesHelper;
import ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.util.AntiguedadHelper;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EQuincena;
import ar.com.textillevel.modulos.personal.utils.ReciboSueldoHelper;

public class ReciboSueldoTO {

	private static final int CANTIDAD_DIAS_QUINCENA = 15;

	private String periodo;
	private String legajo;
	private String cuil;
	private String categoria;
	private String fechaIngreso;
	private String nombre;
	private String dni;
	private String obraSocial;
	private String sindicato;
	private String antiguedad;
	private String totalHaberes;
	private String totalRetenciones;
	private String totalNoRemun;
	private String totalDeducciones;
	private String neto;
	private String fechaUltDeposito;
	private String fechaPago;

	private List<ItemReciboSueldoTO> items;

	public ReciboSueldoTO() {
	}

	public ReciboSueldoTO(ReciboSueldo reciboSueldo, Date fechaUltDeposito, Date fechaPago) {
		Empleado empleado = reciboSueldo.getLegajo().getEmpleado();
		setPeriodo(ReciboSueldoHelper.getInstance().calcularPeriodoRS(reciboSueldo));
		setLegajo(reciboSueldo.getLegajo().getNroLegajo().toString());
		setCuil(empleado.getDocumentacion().getCuit());
		setNombre(empleado.getApellido() + ", " + empleado.getNombre());
		setFechaUltDeposito(DateUtil.dateToString(fechaUltDeposito));
		setFechaPago(DateUtil.dateToString(fechaPago));

		if(reciboSueldo.getLegajo().getCategoria() != null) {
			setCategoria(reciboSueldo.getLegajo().getCategoria().getNombre());
			if(reciboSueldo.getLegajo().getCategoria().getSindicato() != null) {
				setObraSocial(reciboSueldo.getLegajo().getCategoria().getSindicato().getObraSocial().getNombre());
				setSindicato(reciboSueldo.getLegajo().getCategoria().getSindicato().getNombre());
			}
		}
		setearAntiguedadAndFechaIngreso(reciboSueldo);
		setDni("DNI " + empleado.getDocumentacion().getNroDocumento());
		setTotalHaberes(reciboSueldo.getBruto().toString());
		setTotalRetenciones(reciboSueldo.getTotalRetenciones().toString());
		setTotalNoRemun(reciboSueldo.getTotalNoRemunerativo().toString());
		setTotalDeducciones(reciboSueldo.getTotalDeducciones().toString());

		double neto = reciboSueldo.getNeto().doubleValue();
		double parteDecimal = neto - new Double(neto).intValue();
		String netoFormatted = GenericUtils.getDecimalFormat().format(neto);
		String netoLetras = GenericUtils.convertirNumeroATexto(neto);
		setNeto("$ " + netoFormatted + " 	Son pesos " + netoLetras + (parteDecimal > 0 ? " centavos" : ""));

		items = new ArrayList<ItemReciboSueldoTO>();
		CreadorItemReciboSueldoTOVisitor visitor  = new CreadorItemReciboSueldoTOVisitor(items);
		for(ItemReciboSueldo irs : reciboSueldo.getItems()) {
			irs.aceptarVisitor(visitor);
		}

		List<String> anotacionesList = AnotacionesHelper.getInstance().getAnotacionesList(reciboSueldo);
		if(!anotacionesList.isEmpty()) {
			for(int i = 0; i < 3; i++) {
				items.add(new ItemReciboSueldoTO());
			}
			for(String a : anotacionesList) {
				ItemReciboSueldoTO i = new ItemReciboSueldoTO();
				i.setConcepto(a);
				items.add(i);
			}
		}
		
	}

	private void setearAntiguedadAndFechaIngreso(ReciboSueldo reciboSueldo) {
		Date fechaDesde = null;
		Date fechaHasta = null;
		if(reciboSueldo.getQuincena() == null) { //Es mensual
			fechaDesde = DateUtil.getFecha(reciboSueldo.getAnio(), reciboSueldo.getMes().getNroMes(), 1);
			fechaHasta = DateUtil.getUltimoDiaMes(fechaDesde);
		} else { //Es quincenal
			boolean primerQuincena = reciboSueldo.getQuincena().getId() == EQuincena.PRIMERA.getId();
			fechaDesde = primerQuincena ? DateUtil.getFecha(reciboSueldo.getAnio(), reciboSueldo.getMes().getNroMes(), 1) : DateUtil.getFecha(reciboSueldo.getAnio(), reciboSueldo.getMes().getNroMes(), CANTIDAD_DIAS_QUINCENA);
			fechaHasta = primerQuincena ? DateUtil.getFecha(reciboSueldo.getAnio(), reciboSueldo.getMes().getNroMes(), CANTIDAD_DIAS_QUINCENA) : DateUtil.getUltimoDiaMes(fechaDesde);
		}
		setAntiguedad(AntiguedadHelper.getInstance().calcularAntiguedad(fechaHasta, reciboSueldo).toString());
		setFechaIngreso(DateUtil.dateToString(AntiguedadHelper.getInstance().getFechaAlta(fechaHasta, reciboSueldo.getLegajo())));
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getLegajo() {
		return legajo;
	}

	public void setLegajo(String legajo) {
		this.legajo = legajo;
	}

	public String getCuil() {
		return cuil;
	}

	public void setCuil(String cuil) {
		this.cuil = cuil;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getObraSocial() {
		return obraSocial;
	}

	public void setObraSocial(String obraSocial) {
		this.obraSocial = obraSocial;
	}

	public String getSindicato() {
		return sindicato;
	}

	public void setSindicato(String sindicato) {
		this.sindicato = sindicato;
	}

	public String getAntiguedad() {
		return antiguedad;
	}

	public void setAntiguedad(String antiguedad) {
		this.antiguedad = antiguedad;
	}

	public List<ItemReciboSueldoTO> getItems() {
		return items;
	}

	public void setItems(List<ItemReciboSueldoTO> items) {
		this.items = items;
	}

	public JRDataSource getItemsDS() {
		return new JRBeanCollectionDataSource(items);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getParameters() {
		Map parameterMap = new HashMap();
		parameterMap.put("PERIODO", periodo);
		parameterMap.put("LEGAJO", legajo);
		parameterMap.put("CUIL", cuil);
		parameterMap.put("CATEGORIA", categoria);
		parameterMap.put("NOMBRE", nombre);
		parameterMap.put("OBRA_SOCIAL", obraSocial);
		parameterMap.put("SINDICATO", sindicato);
		parameterMap.put("ANTIGUEDAD", antiguedad);
		parameterMap.put("FECHA_INGRESO", fechaIngreso);
		parameterMap.put("DNI", dni);
		parameterMap.put("LUGAR_DE_PAGO", "Buenos Aires - Fábrica");
		parameterMap.put("TOTAL_HABERES", totalHaberes);
		parameterMap.put("TOTAL_RETENCIONES", totalRetenciones);
		parameterMap.put("TOTAL_NO_REM", totalNoRemun);
		parameterMap.put("TOTAL_DEDUCC", totalDeducciones);
		parameterMap.put("NETO", neto);
		parameterMap.put("FECHA_ULT_DEPOSITO", fechaUltDeposito);
		parameterMap.put("FECHA_DE_PAGO", fechaPago);

		parameterMap.put("SUBREPORT_DIR", "ar/com/textillevel/reportes/");

		return parameterMap;
	}

	public String getTotalHaberes() {
		return totalHaberes;
	}

	public void setTotalHaberes(String totalHaberes) {
		this.totalHaberes = totalHaberes;
	}

	public String getTotalRetenciones() {
		return totalRetenciones;
	}

	public void setTotalRetenciones(String totalRetenciones) {
		this.totalRetenciones = totalRetenciones;
	}

	public String getTotalNoRemun() {
		return totalNoRemun;
	}

	public void setTotalNoRemun(String totalNoRemun) {
		this.totalNoRemun = totalNoRemun;
	}

	public String getNeto() {
		return neto;
	}

	public void setNeto(String neto) {
		this.neto = neto;
	}

	public String getFechaUltDeposito() {
		return fechaUltDeposito;
	}

	public void setFechaUltDeposito(String fechaUltDeposito) {
		this.fechaUltDeposito = fechaUltDeposito;
	}

	public String getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}

	public String getTotalDeducciones() {
		return totalDeducciones;
	}

	public void setTotalDeducciones(String totalDeducciones) {
		this.totalDeducciones = totalDeducciones;
	}

}