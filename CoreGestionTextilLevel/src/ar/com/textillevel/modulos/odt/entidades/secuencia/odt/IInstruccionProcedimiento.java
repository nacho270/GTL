package ar.com.textillevel.modulos.odt.entidades.secuencia.odt;

import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;

public interface IInstruccionProcedimiento {

	public String getObservaciones();

	public ESectorMaquina getSectorMaquina();

	public ETipoInstruccionProcedimiento getTipo();
	
	public String getDescrSimple();

	public String getDescrDetallada();

}