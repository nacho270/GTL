package main;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ReadXL {

	/* ruta al fichero excel a leer */
	public static String ficheroEntrada = "c:\\/Temp/fichadas.xls";

	public static void main(String argv[]) {
		try {
			// Lo primero es crear un workbook que representa todo el documento
			// XLS
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(ficheroEntrada));
			// Creamos un objeto sheet con la hoja del documento a leer.
			// (Se ha recuperado referenciando la hoja por su nombre, también se
			// puede recuperar por indice getSheetAt(int index) );
			HSSFSheet sheet = workbook.getSheet("SALIDA");
			HSSFRow row = null;
			HSSFCell cell = null;
			for (int i = 0, z = sheet.getLastRowNum(); i < z; i++) {
				row = sheet.getRow(i);
				if (row != null) {
					System.out.print("\n" + i + "\t");
					for (int ii = 0, zz = row.getLastCellNum(); ii < zz; ii++) {
						cell = row.getCell((short) ii);
						if (cell != null) {
							// Imprimimos por pantalla el contenido de la celda
							// seleccionada
							if (HSSFCell.CELL_TYPE_STRING == cell.getCellType())
								System.out.print(cell.getStringCellValue() + "\t\t");
							if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType())
								System.out.print(cell.getNumericCellValue() + "\t\t");
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Excepción en ReadXL : " + e);
		}
	}
}
