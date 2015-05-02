package ar.com.textillevel.mobile.modules.remito.entrada.cliente;

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
import ar.com.textillevel.mobile.modules.remito.entrada.cliente.to.PiezaRemitoMobileTO;
import ar.com.textillevel.mobile.modules.remito.entrada.cliente.to.RemitoEntradaMobileTO;

public class RemitoEntradaClienteFragment extends FragmentDocumentWithController<RemitoEntradaMobileTO, RemitoEntradaClienteFragmentController> {

    public RemitoEntradaClienteFragment(RemitoEntradaMobileTO document) {
		super(document);
		super.setController(new RemitoEntradaClienteFragmentController(this));
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.remito_entrada_cliente_fragment, container, false);
        super.setDatosCabecera(new DatosCabeceraRemitoEntradaRenderer(getDocument(), rootView));
        
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.re_cardList_piezas);
        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        recList.setHasFixedSize(true);

        PiezaRemitoMobileTO header = createItemHeader();
        List<PiezaRemitoMobileTO> itemsConHeader = new ArrayList<PiezaRemitoMobileTO>();
        itemsConHeader.add(header);
        itemsConHeader.addAll(getDocument().getPiezas());
        
        PiezaRemitoAdapter adapter = new PiezaRemitoAdapter(itemsConHeader);
        recList.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recList.addItemDecoration(itemDecoration);
        
        recList.setItemAnimator(new DefaultItemAnimator());
        
        return rootView;
    }

	private PiezaRemitoMobileTO createItemHeader() {
		PiezaRemitoMobileTO p = new PiezaRemitoMobileTO();
		p.setMetros("METROS");
		p.setNumero("NÚMERO");
		p.setObservaciones("OBSERVACIONES");
		p.setOdt("ODT");
		return p;
	}
}
