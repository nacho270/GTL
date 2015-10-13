package ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.strategy;

import java.util.HashMap;
import java.util.Map;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.EComodinesContrato;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.FileReaderAndReplacer;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.Domicilio;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.InfoDomicilio;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ETipoCobro;

public class StrategyCreacionContratoAPlazoFijo extends StrategyCreacionContrato {

	private static final String PATH_HEADER = "ar/com/textillevel/reportes/contratos/plazofijo/header.txt";
	private static final String PATH_PRIMERO = "ar/com/textillevel/reportes/contratos/plazofijo/primero.txt";
	private static final String PATH_SEGUNDO = "ar/com/textillevel/reportes/contratos/plazofijo/segundo.txt";
	private static final String PATH_TERCERO = "ar/com/textillevel/reportes/contratos/plazofijo/tercero.txt";
	private static final String PATH_CUARTO = "ar/com/textillevel/reportes/contratos/plazofijo/cuarto.txt";
	private static final String PATH_QUINTO = "ar/com/textillevel/reportes/contratos/plazofijo/quinto.txt";
	private static final String PATH_SEXTO = "ar/com/textillevel/reportes/contratos/plazofijo/sexto.txt";
	private static final String PATH_SEPTIMO = "ar/com/textillevel/reportes/contratos/plazofijo/septimo.txt";
	private static final String PATH_OCTAVO = "ar/com/textillevel/reportes/contratos/plazofijo/octavo.txt";
	private static final String PATH_INTERMEDIO = "ar/com/textillevel/reportes/contratos/plazofijo/intermedio.txt";
	private static final String PATH_FOOTER = "ar/com/textillevel/reportes/contratos/plazofijo/footer.txt";
	
	@Override
	public Map<String, Object> crearContrato(Empleado empleado) {
		Map<String, Object> mapa = new HashMap<String, Object>();
		FileReaderAndReplacer reader;
		try {
			String personaFirma = obtenerPersonaFirma();
			if(personaFirma==null){
				return null;
			}
			InfoDomicilio infoDomicilio = empleado.getDomicilios().get(empleado.getDomicilios().size()-1);
			Domicilio domicilio = infoDomicilio.getDomicilio();
			reader = new FileReaderAndReplacer(PATH_HEADER);
			reader.reemplazarComodin(EComodinesContrato.PERSONA_FIRMA, personaFirma);
			reader.reemplazarComodin(EComodinesContrato.NOMBRE_EMPLEADO,  empleado.getApellido()+" " +empleado.getNombre());
			reader.reemplazarComodin(EComodinesContrato.NRO_DOC_EMPLEADO,  empleado.getDocumentacion().getNroDocumento()!=null?""+empleado.getDocumentacion().getNroDocumento():""+empleado.getDocumentacion().getNroCedula());
			reader.reemplazarComodin(EComodinesContrato.DIRECCION_EMPLEADO, domicilio.getCalle() + " " + domicilio.getNumero() + (domicilio.getPiso()!=null?" PISO " + domicilio.getPiso():"") + (domicilio.getDepartamento()!=null?" DTO " + domicilio.getDepartamento():""));
			reader.reemplazarComodin(EComodinesContrato.LOCALIDAD_EMPLEADO, domicilio.getInfoLocalidad().getNombreLocalidad());
			mapa.put("HEADER", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_PRIMERO);
			reader.reemplazarComodin(EComodinesContrato.NOMBRE_EMPLEADO, empleado.getApellido()+" " +empleado.getNombre());
			mapa.put("PRIMERO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_SEGUNDO);
			reader.reemplazarComodin(EComodinesContrato.PUESTO_EMPLEADO, empleado.getLegajo().getPuesto().getNombre());
			reader.reemplazarComodin(EComodinesContrato.HORARIO_EMPLEADO, generarStringHorario(empleado));
			mapa.put("SEGUNDO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_TERCERO);
			reader.reemplazarComodin(EComodinesContrato.NOMBRE_EMPLEADO, empleado.getApellido()+" " +empleado.getNombre());
			mapa.put("TERCERO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_CUARTO);
			reader.reemplazarComodin(EComodinesContrato.DIAS_PRUEBA, String.valueOf(empleado.getContratoEmpleado().getCantidadDias()));
			reader.reemplazarComodin(EComodinesContrato.DIA_CONTRATO, DateUtil.dateToString(empleado.getLegajo().getFechaAlta(),DateUtil.SHORT_DATE));
			reader.reemplazarComodin(EComodinesContrato.FECHA_VENCIMIENTO, DateUtil.dateToString(DateUtil.sumarDias(empleado.getLegajo().getFechaAlta(),empleado.getContratoEmpleado().getCantidadDias()),DateUtil.SHORT_DATE));
			mapa.put("CUARTO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_INTERMEDIO);
			mapa.put("INTERMEDIO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_QUINTO);
			reader.reemplazarComodin(EComodinesContrato.PRECIO_HORA, GenericUtils.getDecimalFormat().format(empleado.getLegajo().getValorHora().doubleValue()));
			reader.reemplazarComodin(EComodinesContrato.NOMBRE_PERIODO,empleado.getLegajo().getPuesto().getSindicato().getTipoCobro()==ETipoCobro.MENSUAL?"mes completo":"quiencena completa");
			mapa.put("QUINTO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_SEXTO);
			if(empleado.getLegajo().getPuesto().getSindicato().getTipoCobro()==ETipoCobro.MENSUAL){
				reader.reemplazarComodin(EComodinesContrato.MES_QUINCENA, "mes");
				reader.reemplazarComodin(EComodinesContrato.REMUNERACION_MENS_QUIN, GenericUtils.getDecimalFormat().format(empleado.getLegajo().getSueldoEstimadoMensual()));
			}else{
				reader.reemplazarComodin(EComodinesContrato.MES_QUINCENA, "quincena");
				reader.reemplazarComodin(EComodinesContrato.REMUNERACION_MENS_QUIN,GenericUtils.getDecimalFormat().format(empleado.getLegajo().getSueldoEstimadoQuincenal()));
			}
			mapa.put("SEXTO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_SEPTIMO);
			mapa.put("SEPTIMO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_OCTAVO);
			mapa.put("OCTAVO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_FOOTER);
			reader.reemplazarComodin(EComodinesContrato.DIA_FECHA_CONTRATO, String.valueOf(DateUtil.getDia(DateUtil.getHoy())));
			reader.reemplazarComodin(EComodinesContrato.MES_FECHA_CONTRATO, DateUtil.MESES[DateUtil.getMes(DateUtil.getHoy())]);
			reader.reemplazarComodin(EComodinesContrato.ANIO_FECHA_CONTRATO, String.valueOf(DateUtil.getAnio(DateUtil.getHoy())));
			mapa.put("FOOTER", reader.getContenidoArchivo());
			return mapa;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
