package ar.com.textillevel.entidades.cuenta.to;

import ar.com.textillevel.entidades.enums.ETipoFactura;

public enum ETipoDocumento {
	
	FACTURA(1,1),
	NOTA_DEBITO(2,2),
	RECIBO(3,4),
	NOTA_CREDITO(4,3),
	ORDEN_PAGO(5,null),
	CHEQUE(6,null),
	REMITO_ENTRADA(7,null),
	REMITO_SALIDA(8,null),
	REMITO_ENTRADA_PROV(9,null),
	FACTURA_PROV(10,null),
	NOTA_CREDITO_PROV(11,null),
	NOTA_DEBITO_PROV(12,null);

	private int id;
	private Integer idTipoDocAFIP;
	
	private ETipoDocumento(int id, Integer idTipoDocAFIP) {
		this.id = id;
		this.idTipoDocAFIP = idTipoDocAFIP;
	}

	public int getId() {
		return id;
	}

	public Integer getIdTipoDocAFIP(ETipoFactura tipo) {
		if(tipo != null && this==ETipoDocumento.FACTURA && tipo==ETipoFactura.B) {
			return 6; //ID de Factura B en la AFIP
		}
		return idTipoDocAFIP;
	}

}