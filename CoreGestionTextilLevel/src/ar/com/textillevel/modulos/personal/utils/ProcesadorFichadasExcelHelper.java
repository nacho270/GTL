package ar.com.textillevel.modulos.personal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.FileUtil;
import ar.com.textillevel.modulos.personal.entidades.fichadas.FichadaLegajo;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.EFormaIngresoFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.ETipoFichada;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.facade.api.local.EmpleadoFacadeLocal;
import ar.com.textillevel.modulos.personal.facade.api.local.FichadaLegajoFacadeLocal;

public class ProcesadorFichadasExcelHelper {

	private static final Logger logger = Logger.getLogger(ProcesadorFichadasExcelHelper.class);

	private static FichadaLegajoFacadeLocal fichadaFacade;
	private static EmpleadoFacadeLocal empleadoFacade;

	private static final int COLUMNA_LEGAJO = 0;
	private static final int COLUMNA_TARJETA = 1;
	private static final int COLUMNA_FECHA = 2;
	private static final int COLUMNA_HORA = 3;
	private static final int COLUMNA_TIPO_FICHADA = 4;

	public static void procesarArchivo(String path) {
		FileInputStream fiArchivo = null;
		try {
			File archivo = new File(path);
			boolean ok = false;
			while(!ok){
				try{
					fiArchivo = new FileInputStream(archivo);
					ok = true;
				}catch(FileNotFoundException f){
					Thread.sleep(100);
				}
			}
			HSSFWorkbook workbook = new HSSFWorkbook(fiArchivo);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row = null;
			int total = 0;
			if (sheet != null) {
				Timestamp fechaHoraUltimaFichada = getFichadaFacade().getFechaHoraUltimaFichada();
				logger.info("PROCESANDO EL ARCHIVO DE FICHADAS: " + path);
				int cantidadProcesadas = 0;
				int z = sheet.getLastRowNum()+1;
				for (int i = 1; i <= z; i++) { // 1 para que arrance desde el titulo
					total++;
					row = sheet.getRow(i);
					if (row != null) {
						FichadaLegajo fichadaNueva = new FichadaLegajo();
						fichadaNueva.setFormaIngresoFichada(EFormaIngresoFichada.AUTOMATICA);
						try {
							procesarFila(row, fichadaNueva);
						} catch (LegajoNoExistenteException le) {
							logger.error("NO SE ENCONTRO EL LEGAJO Nº: " + le.getNroLegajo());
							continue;
						} catch(FichadaSinFechaException fe){
							logger.error("SE ENCONTRO UNA FICHADA SIN LA FECHA CARGADA CORRECTAMENTE. FILA Nº " + fe.getRowNum());
							continue;
						}
						if(fechaHoraUltimaFichada==null || !fichadaNueva.getHorario().before(fechaHoraUltimaFichada)){
							cantidadProcesadas++;
							getFichadaFacade().save(fichadaNueva);
						}
					}
				}
				fiArchivo.close();
				row = null;
				sheet = null;
				workbook = null;
				System.gc();
				String pathProcesados = path.substring(0,path.lastIndexOf("/")).replaceAll("/", "\\\\")+"\\PROCESADOS\\";
				FileUtil.verificarYCrearDirectorios(pathProcesados);
				File archivoMovido = new File(pathProcesados+path.substring(path.lastIndexOf("/"), path.length())+"."+DateUtil.dateToString(DateUtil.getAhora(), DateUtil.DEFAULT_DATE_WITHOUT_SEPARATOR+"-HH_mm_ss"));
				FileUtil.moverArchivo(archivo,archivoMovido);
				logger.info("SE HA TERMINADO DE PROCESAR EL ARCHIVO DE FICHADAS: " + path + ". SE PROCESARON " + cantidadProcesadas + " FICHADAS DE " + total + ".");
			} else {
				logger.error("NO SE HA ENCONTRADO LA HOJA 'SALIDA' EN EL ARCHIVO LEIDO");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally{
			try{
				if(fiArchivo!=null){
					fiArchivo.close();
				}
			}catch(IOException ioe){
				
			}
		}
	}

	private static void procesarFila(HSSFRow row, FichadaLegajo fichadaNueva) throws LegajoNoExistenteException,FichadaSinFechaException {
		HSSFCell cell;
		Date fechaFichada = null;
		String horaFichada = null;
		for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
			cell = row.getCell((short) cellIndex);
			if (cell != null) {
				switch (cellIndex) {
					case COLUMNA_LEGAJO: {
						LegajoEmpleado leg = getEmpleadoFacade().getLegajoByNumero(Double.valueOf(cell.getNumericCellValue()).intValue());
						if (leg == null) {
							LegajoNoExistenteException excep = new LegajoNoExistenteException();
							excep.setNroLegajo(Double.valueOf(cell.getNumericCellValue()).intValue());
							throw excep;
						}
						fichadaNueva.setLegajo(leg);
						break;
					}
					case COLUMNA_FECHA: {
						fechaFichada = DateUtil.stringToDate(cell.getStringCellValue(), DateUtil.SHORT_DATE);
						break;
					}
					case COLUMNA_TARJETA: {
						//TODO: VER SI SE PUEDE HACER ALGO CON ESTO
						break;
					}
					case COLUMNA_HORA: {
						horaFichada = cell.getStringCellValue();
						break;
					}
					case COLUMNA_TIPO_FICHADA: {
						fichadaNueva.setTipoFichada(ETipoFichada.getByInicial(cell.getStringCellValue()));
						break;
					}
					default: {
						continue;
					}
				}
			}
		}
		if(horaFichada!=null & fechaFichada!=null ){ //ultima celda, proceso la hora
			String[] split = horaFichada.split(":");
			Integer horas = Integer.valueOf(split[0]);
			Integer minutos = Integer.valueOf(split[1]);
			fechaFichada = DateUtil.setHoras(fechaFichada, horas);
			fechaFichada = DateUtil.setMinutos(fechaFichada, minutos);
			fichadaNueva.setHorario(new Timestamp(fechaFichada.getTime()));
		}else{
			FichadaSinFechaException excep = new FichadaSinFechaException();
			excep.setRowNum(row.getRowNum());
			throw excep;
		}
	}

	private static class LegajoNoExistenteException extends Exception {

		private static final long serialVersionUID = 53599771687230679L;

		private Integer nroLegajo;

		public LegajoNoExistenteException() {
			super();
		}

		public Integer getNroLegajo() {
			return nroLegajo;
		}

		public void setNroLegajo(Integer nroLegajo) {
			this.nroLegajo = nroLegajo;
		}
	}

	private static class FichadaSinFechaException extends Exception {

		private static final long serialVersionUID = 53599771687230679L;

		private Integer rowNum;

		public FichadaSinFechaException() {
			super();
		}
		
		public Integer getRowNum() {
			return rowNum;
		}

		
		public void setRowNum(Integer rowNum) {
			this.rowNum = rowNum;
		}
	}
	
	private static FichadaLegajoFacadeLocal getFichadaFacade() {
		if (fichadaFacade == null) {
			fichadaFacade = GTLPersonalBeanFactoryLocal.getInstance2().getBean2(FichadaLegajoFacadeLocal.class);
		}
		return fichadaFacade;
	}

	private static EmpleadoFacadeLocal getEmpleadoFacade() {
		if (empleadoFacade == null) {
			empleadoFacade = GTLPersonalBeanFactoryLocal.getInstance2().getBean2(EmpleadoFacadeLocal.class);
		}
		return empleadoFacade;
	}
}
