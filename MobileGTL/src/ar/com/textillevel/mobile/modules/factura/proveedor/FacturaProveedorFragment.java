package ar.com.textillevel.mobile.modules.factura.proveedor;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.DividerItemDecoration;
import ar.com.textillevel.mobile.modules.common.FragmentDocumentWithController;
import ar.com.textillevel.mobile.modules.factura.proveedor.to.FacturaProvMobTO;
import ar.com.textillevel.mobile.modules.factura.proveedor.to.ItemDocumentoProvMobTO;

public class FacturaProveedorFragment extends FragmentDocumentWithController<FacturaProvMobTO, FacturaProveedorFragmentController> {

    public FacturaProveedorFragment(FacturaProvMobTO factura) {
    	super(factura);
    	super.setController(new FacturaProveedorFragmentController(this));
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.factura_prov_fragment, container, false);
        super.setDatosCabecera(new DatosCabeceraFacturaProveedorRenderer(getDocument(), rootView));

        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        recList.setHasFixedSize(true);

        ItemDocumentoProvMobTO header = createItemHeader();
        List<ItemDocumentoProvMobTO> itemsConHeader = new ArrayList<ItemDocumentoProvMobTO>();
        itemsConHeader.add(header);
        itemsConHeader.addAll(getDocument().getItems());
        
        if(getDocument().getSubtotal()!= null && !(getDocument().getSubtotal().equals("0.00") || getDocument().getSubtotal().equals("0"))) {
        	itemsConHeader.add(createItemTotal("SUBTOTAL", getDocument().getSubtotal()));
        }

        if(getDocument().getDescuento()!= null && !(getDocument().getDescuento().equals("0.00") || getDocument().getDescuento().equals("0"))) {
        	itemsConHeader.add(createItemTotal("DESCUENTO", getDocument().getDescuento()));
        }
        if(!(getDocument().getImpVarios().equals("0.00") || getDocument().getImpVarios().equals("0"))) {
        	itemsConHeader.add(createItemTotal("IMP. VARIOS", getDocument().getImpVarios()));
        }	
        if(!(getDocument().getPercepIVA().equals("0.00") || getDocument().getPercepIVA().equals("0"))) {
        	itemsConHeader.add(createItemTotal("PERCEP. IVA", getDocument().getPercepIVA()));
        }
        if(!(getDocument().getImpuestoTO().getTotal().equals("0.00") || getDocument().getImpuestoTO().getTotal().equals("0"))) {
        	itemsConHeader.add(createItemTotal("TOTAL IMPUESTOS", getDocument().getImpuestoTO().getTotal()));
        }
        itemsConHeader.add(createItemTotal("TOTAL FACTURA", getDocument().getTotalFactura()));

        ItemFacturaProveedorAdapter adapter = new ItemFacturaProveedorAdapter(itemsConHeader);
        recList.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recList.addItemDecoration(itemDecoration);
        
        
        recList.setItemAnimator(new DefaultItemAnimator());
        
        return rootView;
    }

	private ItemDocumentoProvMobTO createItemHeader() {
		ItemDocumentoProvMobTO header = new ItemDocumentoProvMobTO();
		header.setCantidadMasUnidad("CANT.");
        header.setDescripcion("DESCRIPCIÓN");
        header.setDescuento("DESC.");
        header.setFactor("FACT.");
        header.setPrecioUnitario("P.U.");
        header.setImporte("IMP.");
		return header;
	}

	private ItemDocumentoProvMobTO createItemTotal(String label, String valor) {
		ItemDocumentoProvMobTO header = new ItemDocumentoProvMobTO();
		header.setCantidadMasUnidad("");
        header.setDescripcion(label);
        header.setDescuento("");
        header.setFactor("");
        header.setPrecioUnitario("");
        header.setImporte(valor);
		return header;
	}
	
}