package ar.com.textillevel.mobile.modules.factura;

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
import ar.com.textillevel.mobile.modules.factura.cliente.to.FacturaMobTO;
import ar.com.textillevel.mobile.modules.factura.cliente.to.ItemFacturaTO;

public class FacturaClienteFragment extends FragmentDocumentWithController<FacturaMobTO,FacturaClienteFragmentController> {
	
	
    public FacturaClienteFragment(FacturaMobTO factura) {
    	super(factura);
    	super.setController(new FacturaClienteFragmentController(this));
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.factura_cliente_fragment, container, false);
        super.setDatosCabecera(new DatosCabeceraFacturaClienteRenderer(getDocument(), rootView));
        
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        recList.setHasFixedSize(true);

        ItemFacturaTO header = createItemHeader();
        List<ItemFacturaTO> itemsConHeader = new ArrayList<ItemFacturaTO>();
        itemsConHeader.add(header);
        itemsConHeader.addAll(getDocument().getItems());
        ItemFacturaTO totales = createItemTotales();
        itemsConHeader.add(totales);
        
        ItemFacturaAdapter adapter = new ItemFacturaAdapter(itemsConHeader);
        recList.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recList.addItemDecoration(itemDecoration);
        
        recList.setItemAnimator(new DefaultItemAnimator());
        
        return rootView;
    }

	private ItemFacturaTO createItemTotales() {
		ItemFacturaTO totales = new ItemFacturaTO();
		totales.setCantidad("");
		totales.setUnidad("");
		totales.setDescripcion("TOTAL: " + getDocument().getTotalFactura());
		totales.setPrecioUnitario("");
		totales.setImporte("");
		return totales;
	}

	private ItemFacturaTO createItemHeader() {
		ItemFacturaTO header = new ItemFacturaTO();
        header.setCantidad("CANT.");
        header.setUnidad("");
        header.setDescripcion("DESCRIPCIÓN");
        header.setPrecioUnitario("P.U.");
        header.setImporte("IMP.");
		return header;
	}

}