package ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.strategy;

import java.util.HashMap;
import java.util.Map;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.EComodinesContrato;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.FileReaderAndReplacer;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.Domicilio;
import ar.com.textillevel.modulos.personal.entidades.legajos.domicilio.InfoDomicilio;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ETipoCobro;

public class StrategyCreacionContratoAPrueba extends StrategyCreacionContrato{

	private static final String PATH_HEADER = "ar/com/textillevel/reportes/contratos/aprueba/header.txt";
	private static final String PATH_PRIMERO = "ar/com/textillevel/reportes/contratos/aprueba/primero.txt";
	private static final String PATH_SEGUNDO = "ar/com/textillevel/reportes/contratos/aprueba/segundo.txt";
	private static final String PATH_TERCERO = "ar/com/textillevel/reportes/contratos/aprueba/tercero.txt";
	private static final String PATH_CUARTO = "ar/com/textillevel/reportes/contratos/aprueba/cuarto.txt";
	private static final String PATH_QUINTO = "ar/com/textillevel/reportes/contratos/aprueba/quinto.txt";
	private static final String PATH_SEXTO = "ar/com/textillevel/reportes/contratos/aprueba/sexto.txt";
	private static final String PATH_SEPTIMO = "ar/com/textillevel/reportes/contratos/aprueba/septimo.txt";
	private static final String PATH_FOOTER = "ar/com/textillevel/reportes/contratos/aprueba/footer.txt";
	
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
			reader.reemplazarComodin(EComodinesContrato.DIA_CONTRATO, DateUtil.dateToString(empleado.getLegajo().getFechaAlta(),DateUtil.SHORT_DATE));
			mapa.put("PRIMERO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_SEGUNDO);
			reader.reemplazarComodin(EComodinesContrato.PUESTO_EMPLEADO, empleado.getLegajo().getPuesto().getNombre());
			reader.reemplazarComodin(EComodinesContrato.HORARIO_EMPLEADO, generarStringHorario(empleado));
			mapa.put("SEGUNDO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_TERCERO);
			reader.reemplazarComodin(EComodinesContrato.DIAS_PRUEBA, String.valueOf(empleado.getContratoEmpleado().getCantidadDias()));
			mapa.put("TERCERO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_CUARTO);
			mapa.put("CUARTO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_QUINTO);
			reader.reemplazarComodin(EComodinesContrato.PRECIO_HORA, GenericUtils.getDecimalFormat().format(empleado.getLegajo().getValorHora().doubleValue()));
			reader.reemplazarComodin(EComodinesContrato.NOMBRE_PERIODO,empleado.getLegajo().getPuesto().getSindicato().getTipoCobro()==ETipoCobro.MENSUAL?"mes completo":"quiencena completa");
			mapa.put("QUINTO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_SEXTO);
			mapa.put("SEXTO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_SEPTIMO);
			mapa.put("SEPTIMO", reader.getContenidoArchivo());
			
			reader = new FileReaderAndReplacer(PATH_FOOTER);
			reader.reemplazarComodin(EComodinesContrato.DIA_FECHA_CONTRATO, String.valueOf(DateUtil.getDia(DateUtil.getHoy())));
			reader.reemplazarComodin(EComodinesContrato.MES_FECHA_CONTRATO, DateUtil.MESES[DateUtil.getMes(DateUtil.getHoy())]);
			reader.reemplazarComodin(EComodinesContrato.ANIO_FECHA_CONTRATO, String.valueOf(DateUtil.getAnio(DateUtil.getHoy())));
			mapa.put("FOOTER", reader.getContenidoArchivo());
			return mapa;
		} catch (Exception e) {
			e.printStackTrace();
		}
//		mapa.put("NOMBRE", empleado.getApellido()+" " +empleado.getNombre());
//		mapa.put("DOCUMENTO", empleado.getDocumentacion().getNroDocumento()!=null?""+empleado.getDocumentacion().getNroDocumento():""+empleado.getDocumentacion().getNroCedula());
//		Domicilio domicilio = infoDomicilio.getDomicilio();
//		mapa.put("DIRECCION", domicilio.getCalle() + " " + domicilio.getNumero() + (domicilio.getPiso()!=null?" PISO " + domicilio.getPiso():"") + (domicilio.getDepartamento()!=null?" DTO " + domicilio.getDepartamento():""));
//		mapa.put("LOCALIDAD", domicilio.getInfoLocalidad().getNombreLocalidad()+".");
//		mapa.put("FECHA_ALTA", DateUtil.dateToString(empleado.getLegajo().getFechaAlta(),DateUtil.SHORT_DATE));
//		mapa.put("PUESTO", empleado.getLegajo().getPuesto().getNombre());
//		mapa.put("HORARIO", generarStringHorario()); //09:00 a 17:00 de lunes a miercoles, de 09:00 a 17:00 de jueves a viernes y de 08:00 a 13:00 los sabados
//		mapa.put("DURACION", String.valueOf(empleado.getContratoEmpleado().getCantidadDias())); //dias
//		
//		if(tipoDeContrato == ETipoContrato.A_PRUEBA){
//			mapa.put("PRECIO_HORA", GenericUtils.getDecimalFormat().format(empleado.getLegajo().getValorHora().doubleValue()));
//		}else{
//			if(empleado.getLegajo().getPuesto().getSindicato().getTipoCobro()==ETipoCobro.MENSUAL){
//				mapa.put("REMUNERACION_MENSUAL", GenericUtils.getDecimalFormat().format(empleado.getLegajo().getSueldoEstimadoMensual()));
//				mapa.put("MES_QUINCENA", "mes");
//			}else{
//				mapa.put("REMUNERACION_MENSUAL", GenericUtils.getDecimalFormat().format(empleado.getLegajo().getSueldoEstimadoQuincenal()));
//				mapa.put("MES_QUINCENA", "quincena");
//			}
//			mapa.put("FECHA_VENCIMIENTO",  DateUtil.dateToString(DateUtil.sumarDias(empleado.getLegajo().getFechaAlta(),empleado.getContratoEmpleado().getCantidadDias()),DateUtil.SHORT_DATE));
//		}
//		
//		mapa.put("NOMBRE_PERIODO_MAS_COMPLETO-A", empleado.getLegajo().getPuesto().getSindicato().getTipoCobro()==ETipoCobro.MENSUAL?"mes completo":"quiencena completa"); //quiencena completa / mes completo
//		mapa.put("NUMERO_DIA_HOY", DateUtil.getDia(DateUtil.getHoy()));
//		mapa.put("NOMBRE_MES_ACTUAL", DateUtil.MESES[DateUtil.getMes(DateUtil.getHoy())]);
//		mapa.put("ANIO", DateUtil.getAnio(DateUtil.getHoy()));
//		String personaFirma = obtenerPersonaFirma();
//		if(personaFirma!=null){
//			mapa.put("PERSONA_FIRMA", personaFirma);
//		}else{
//			return null;
//		}
		return null;
	}
}
