package ar.com.textillevel.excepciones;


public enum EValidacionException {

	COLOR_EN_OTRA_GAMA							(100,"El color ya esta asignado a otra gama."),
	FACTURA_YA_TIENE_RECIBO						(101,"No se puede eliminar la factura ya que ya esta siendo utilizada en un recibo"),
	FACTURA_NO_ES_LA_ULTIMA						(102,"No se puede eliminar la factura ya que esta no es la �ltima"),
	CLIENTE_INEXISTENTE							(103,"El cliente ingresado no existe"),
	FACTURA_PROV_EXISTE_EN_ORDEN_DE_PAGO		(104, "La factura de proveedor no puede eliminarse/editarse porque existe una orden de pago relacionada"),
	FACTURA_PROV_EXISTE_EN_CORRECCION			(105, "La factura de proveedor no puede eliminarse/editarse porque existe una nota de cr�dito/d�bito relacionada"),
	NOTA_DEBITO_PROV_EXISTE_EN_ORDEN_DE_PAGO	(106, "La nota de d�bito no puede eliminarse porque existe una orden de pago relacionada"),
	NOTA_CREDITO_PROV_EXISTE_EN_ORDEN_DE_PAGO	(107, "La nota de cr�dito no puede eliminarse porque existe una orden de pago relacionada"),
	YA_HAY_PERFIL_ADMIN							(108, "Ya existe un perfil administrador"),
	REMITO_ENTRADA_IMPOSIBLE_BORRAR_O_EDITAR	(109, "No se puede borrar/editar el remito de entrada porque existen dependencias con los siguientes remitos de salida: {0}. Proceda a borrar los remitos de salida primero y luego reintente la operaci�n."),
	REMITO_ENTRADA_PROV_IMPOSIBLE_BORRAR		(110, "No se puede borrar el remito de entrada de proveedor porque existen dependencias con las siguientes facturas {0}. Proceda a borrar dichas factura primero y luego reintetne la operaci�n."),
	REMITO_SALIDA_IMPOSIBLE_BORRAR_O_EDITAR_O_ANULAR		(111, "No se puede borrar/editar/anular el remito de salida porque existen dependencias con las siguientes facturas {0}. Proceda a borrar dichas factura primero y luego reintente la operaci�n."),				
	NOTA_CREDITO_TIENE_FACTURAS_RELACIONADAS	(112, "No se puede borrar la nota de cr�dito porque tiene facturas relacionadas."),
	NOTA_CREDITO_SE_USA_EN_RECIBO				(113, "No se puede borrar la nota de cr�dito porque fue utilizada en un recibo"),
	NOTA_DEBITO_SE_USA_EN_RECIBO				(114, "No se puede borrar la nota de d�bito porque fue utilizada en un recibo"),
	CLAVE_YA_EN_USO								(115, "La clave ingresada ya se encuentra en uso. Por favor ingrese una diferente."),
	RECIBO_IMPOSIBLE_ELIMINAR_NO_ES_EL_ULTIMO	(116, "El recibo no puede eliminarse porque no es el �ltimo."),
	RECIBO_CHEQUES_INCONSISTENTES				(117, "El recibo tiene cheques de otro(s) cliente(s)."),
	RECIBO_IMPOSIBLE_EDITAR_NO_ES_EL_ULTIMO_BY_CLIENTE						(118, "El recibo no puede editarse porque no es el �ltimo para el cliente."),
	FACTURA_TIENE_NOTA_CREDITO					(119,"No se puede eliminar la factura ya que ya esta siendo utilizada en una nota de cr�dito"),
	RECIBO_NRO_RECIBO_EXISTENTE					(120,"Ya existe otro recibo con el mismo n�mero {0}"),
	REMITO_ENTRADA_IMPOSIBLE_BORRAR_O_EDITAR_EXISTE_RE_PROV(121, "No se puede borrar/editar el remito de entrada de proveedor porque existe una dependencia con el remito de proveedor con Nro. {0} y fecha {1}. Proceda a borrar dicho remito de entrada de proveedor y reintente la operaci�n."),
	REMITO_SALIDA_VTA_OR_SAL_01_IMP_ELIMINAR	(122, "Este tipo de remito de salida no puede eliminarse desde esta operaci�n."),
	REMITO_ENTRADA_NO_ES_01_NI_COMPRA_TELA		(123, "Este remito de entrada no puede eliminarse desde esta operaci�n."),
	REMITO_ENTRADA_IMP_BORRAR_PIEZAS_EN_SALIDA  (124, "Este remito de entrada no puede eliminarse ya que algunas de sus piezas ya est�n 'en salida'."),
	CHEQUE_SE_USA_EN_RECIBO						(125, "El cheque no puede eliminarse porque se usa en un recibo."),
	CHEQUE_SE_USA_EN_ODP						(126, "El cheque no puede eliminarse porque se usa en una �rden de pago."),
	CHEQUE_SE_USA_EN_ODP_PERSONA				(127, "El cheque no puede eliminarse porque se usa en una �rden de pago a persona."),
	REMITO_SALIDA_01_OR_COMP_TELA_IMPOSIBLE_GRABAR (128, "No se pudo grabar el remito de salida por la siguiente causa: {0}"),
	CALENDARIO_LABORAL_INVALIDO_MAL_LOS_ANIOS		(129, "El calendario laboral posee feriados que no coinciden con el a�o en cuesti�n del mismo."),
	CALENDARIO_LABORAL_INVALIDO_FERIADOS_SOLAPADOS	(130, "El calendario laboral posee feriados que se solapan entre s�: {0} "),
	CALENDARIO_LABORAL_YA_EXISTE_ANIO				(131, "Ya existe un calendario de feriados para ese a�o."),
	REMITO_ENTRADA_TARIMA_EXISTENTE					(132, "Ya existe la tarima con n�mero {0}."),
	ODT_TIPO_MAQUINA_MISMO_NOMBRE					(133, "Ya existe un tipo de m�quina con nombre {0}."),
	ODT_TIPO_MAQUINA_MISMO_ORDEN					(134, "Ya existe un tipo de m�quina con �rden {0}."),
	ODT_MAQUINA_MISMO_NOMBRE_Y_TIPO_MAQUINA			(135, "Ya existe una m�quina con nombre {0} para un tipo de m�quina {1}."),
	MAQUINA_TERMINACION_MISMO_NOMBRE			    (136, "Ya existe una terminaci�n con con nombre {0}."),
	ACCION_PROCEDIMIENTO_EXISTENTE					(137, "Ya existe la acci�n llamada '{0}' para '{1}'."),
	FORMULA_TENIDO_EXISTENTE_PARA_CLIENTE			(138, "Ya existe una f�rmula con nombre '{0}' para el cliente '{1}'."),
	INSTRUCCION_PROCEDIMIENTO_EXISTENTE				(139, "Ya existe una instrucci�n con las mismas caracter�sticas dentro del sector '{0}'."),
	ODT_NO_SE_PUEDE_BORRAR_SECUENCIA				(140, "No se puede borrar la secuencia de trabajo de la ODT debido a que se encuentra en estado '{0}'"),
	TIPO_ART_NO_SE_PUEDE_ELIMINAR_TIPO_ARTICULO		(141, "No se puede eliminar el Tipo de art�culo dado que se est� utilizando como componente en otro Tipo de art�culo."),
	RECIBO_PAGOS_INCONSISTENTES						(142, "Existen Facturas y/o Notas de d�bito que no son del cliente."),
	
	
	_											(000, "");
	

	private int codigo;
	private String mensaje;

	private EValidacionException(int codigo, String mensaje) {
		this.codigo = codigo;
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	@Override
	public String toString() {
		return getMensaje();
	}

	public static EValidacionException getEnum(int codigo) {
		for(EValidacionException eexception : values()) {
			if(eexception.getCodigo() == codigo) {
				return eexception;
			}
		}
		throw new IllegalArgumentException("No existe un valor del enum con identificador " + codigo);
	}
	
	public InfoValidacionGTL getInfoValidacion(){
		return new InfoValidacionGTL(this.codigo,this.mensaje);
	}
	
}