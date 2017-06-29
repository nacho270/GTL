package ar.com.textillevel.entidades.cuenta.to;

import java.util.ArrayList;
import java.util.List;

import ar.com.textillevel.entidades.enums.ETipoFactura;

public enum ETipoDocumento {
	
	FACTURA(1,1, null),
	NOTA_DEBITO(2,2, null),
	RECIBO(3,4, null),
	NOTA_CREDITO(4,3, null),
	ORDEN_PAGO(5,null, "111"),
	CHEQUE(6,null, null),
	REMITO_ENTRADA(7,null, null),
	REMITO_SALIDA(8,null, "222"),
	REMITO_ENTRADA_PROV(9,null, null),
	FACTURA_PROV(10,null, null),
	NOTA_CREDITO_PROV(11,null, null),
	NOTA_DEBITO_PROV(12,null, null),
	ORDEN_PAGO_PERSONA(13, null, "333");

	private int id;
	private Integer idTipoDocAFIP;
	private String prefijoCodigoBarras;
	
	private ETipoDocumento(int id, Integer idTipoDocAFIP, String prefijoCodigoBarras) {
		this.id = id;
		this.idTipoDocAFIP = idTipoDocAFIP;
		this.prefijoCodigoBarras = prefijoCodigoBarras;
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

	public Integer getIdTipoDocAFIP() {
		return idTipoDocAFIP;
	}
	public String getDescripcion() {
		return toString().replace("_", " ");
	}

	public String getPrefijoCodigoBarras() {
		return prefijoCodigoBarras;
	}

	public static ETipoDocumento getByPrefijo(String prefijo) {
		for(ETipoDocumento etd : values()) {
			if (etd.getPrefijoCodigoBarras() != null && etd.getPrefijoCodigoBarras().equals(prefijo)) {
				return etd;
			}
		}
		return null;
	}

	public static ETipoDocumento[] getTiposDocumentoConAFIP() {
		List<ETipoDocumento> tiposDocumento = new ArrayList<>();
		for (ETipoDocumento tipo : values()) {
			if (tipo.idTipoDocAFIP != null) {
				tiposDocumento.add(tipo);
			}
		}
		return tiposDocumento.toArray(new ETipoDocumento[tiposDocumento.size()]);
	}
}