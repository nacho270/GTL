package ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos;

public enum EComodinesContrato {
	
	// COMMONS
	NOMBRE_EMPLEADO("@/nombre_empleado@/"),
	PERSONA_FIRMA("@/persona_firma@/"),
	NRO_DOC_EMPLEADO("@/nro_doc_empleado@/"),
	DIRECCION_EMPLEADO("@/direccion_empleado@/"),
	LOCALIDAD_EMPLEADO("@/localidad_empleado@/"),
	DIA_CONTRATO("@/dia_contrato@/"),
	PUESTO_EMPLEADO("@/puesto_empleado@/"),
	HORARIO_EMPLEADO("@/horario_empleado@/"),
	DIAS_PRUEBA("@/cant_dias_prueba@/"),
	NOMBRE_PERIODO("@/quincena_mes@/"),
	DIA_FECHA_CONTRATO("@/dia_fecha_contrato@/"),
	MES_FECHA_CONTRATO("@/mes_fecha_contrato@/"),
	ANIO_FECHA_CONTRATO("@/anio_fecha_contrato@/"),
	
	//A PRUEBA
	PRECIO_HORA("@/precio_hora_empleado@/"),
	
	//PLAZO FIJO
	FECHA_VENCIMIENTO("@/fecha_vencimiento@/"),
	MES_QUINCENA("@/mes_quincena@/"),
	REMUNERACION_MENS_QUIN("@/remuneracion_mes_quincena@/")
	;

	private EComodinesContrato(String comodin) {
		this.comodin = comodin;
	}

	private String comodin;

	public String getComodin() {
		return comodin;
	}

	public void setComodin(String comodin) {
		this.comodin = comodin;
	}
}
