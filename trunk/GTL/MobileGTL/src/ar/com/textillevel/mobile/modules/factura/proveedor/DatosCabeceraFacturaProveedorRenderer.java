package ar.com.textillevel.mobile.modules.factura.proveedor;

import java.util.HashSet;
import java.util.Set;

import android.view.View;
import android.widget.RelativeLayout;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.DatosCabeceraRenderer;
import ar.com.textillevel.mobile.modules.common.DocumentLinkeableLinearLayout;
import ar.com.textillevel.mobile.modules.common.to.ETipoDocumento;
import ar.com.textillevel.mobile.modules.factura.proveedor.to.FacturaProvMobTO;
import ar.com.textillevel.mobile.modules.factura.proveedor.to.ImpuestoFacturaProvMobTO;
import ar.com.textillevel.mobile.modules.factura.proveedor.to.ItemImpuestoFacturaProvMobTO;
import ar.com.textillevel.mobile.util.StringUtil;

public class DatosCabeceraFacturaProveedorRenderer extends DatosCabeceraRenderer<FacturaProvMobTO> {

	public DatosCabeceraFacturaProveedorRenderer(FacturaProvMobTO document, View rootView) {
		super(document, rootView);
	}

	@Override
	public void render() {
		setTextOnTextView(R.id.factura_p_tipo, getLblDocumento() + " - N° " + getDocument().getNroFactura(), getRootView());
		setTextOnTextView(R.id.factura_p_fecha,"FECHA: " + getDocument().getFecha(), getRootView());
		setTextOnTextView(R.id.factura_p_owner,"PROVEEDOR: " + getDocument().getProveedor().getRazonSocial(), getRootView());
		setTextOnTextView(R.id.factura_p_impuestos,"IMPUESTOS: " + calcularImpuestos(getDocument().getImpuestoTO()), getRootView());
		if(!getDocument().getDocsRelacionados().isEmpty()) {
			DocumentLinkeableLinearLayout ly = new DocumentLinkeableLinearLayout(getLblDocsRelacionados(), getDocument().getDocsRelacionados(), getRootView().getContext());
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, R.id.factura_p_impuestos);
			((RelativeLayout)getRootView().findViewById(R.id.rl_cab_fact_p)).addView(ly, lp);
		}
	}

	private String getLblDocsRelacionados() {
		if(getDocument().getTipoDocumento().equals(ETipoDocumento.FACTURA_PROV.getId() + "")) {
			return "remito";
		} else {
			return "factura";
		}
	}

	private String getLblDocumento() {
		if(getDocument().getTipoDocumento().equals(ETipoDocumento.FACTURA_PROV.getId() + "")) {
			return "FACTURA \"A\"";
		} else if(getDocument().getTipoDocumento().equals(ETipoDocumento.NOTA_CREDITO_PROV.getId() + "")){
			return "NOTA DE CRÉDITO";
		} else {
			return "NOTA DE DÉBITO";
		}
	}

	private String calcularImpuestos(ImpuestoFacturaProvMobTO impuestoTO) {
		Set<String> impuestos = new HashSet<String>();
		for(ItemImpuestoFacturaProvMobTO imp : impuestoTO.getItemsImpuesto()) {
			impuestos.add(imp.getDescripcion());
		}
		return StringUtil.getCadena(impuestos, ", ");
	}

}