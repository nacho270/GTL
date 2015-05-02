package ar.com.textillevel.entidades.cuenta.to;

public enum ETipoDocumento {
	
	FACTURA(1),
	NOTA_DEBITO(2),
	RECIBO(3),
	NOTA_CREDITO(4),
	ORDEN_PAGO(5),
	CHEQUE(6),
	REMITO_ENTRADA(7),
	REMITO_SALIDA(8),
	REMITO_ENTRADA_PROV(9),
	FACTURA_PROV(10),
	NOTA_CREDITO_PROV(11),
	NOTA_DEBITO_PROV(12);

	private int id;

	private ETipoDocumento(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}