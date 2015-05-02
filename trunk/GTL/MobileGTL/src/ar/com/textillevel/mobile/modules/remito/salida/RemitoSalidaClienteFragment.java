package ar.com.textillevel.mobile.modules.remito.salida;

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
import ar.com.textillevel.mobile.modules.remito.salida.to.RemitoSalidaMobileTO;

public class RemitoSalidaClienteFragment extends FragmentDocumentWithController<RemitoSalidaMobileTO, RemitoSalidaClienteFragmentController> {

    public RemitoSalidaClienteFragment(RemitoSalidaMobileTO document) {
		super(document);
		super.setController(new RemitoSalidaClienteFragmentController(this));
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.remito_salida_cliente_fragment, container, false);
        super.setDatosCabecera(new DatosCabeceraRemitoSalidaRenderer(getDocument(), rootView));
        
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.rs_cardList_piezas);
        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        recList.setHasFixedSize(true);

        PiezaRemitoMobileTO header = createItemHeader();
        List<PiezaRemitoMobileTO> itemsConHeader = new ArrayList<PiezaRemitoMobileTO>();
        itemsConHeader.add(header);
        itemsConHeader.addAll(getDocument().getPiezas());
        
        PiezaRemitoSalidaAdapter adapter = new PiezaRemitoSalidaAdapter(itemsConHeader);
        recList.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recList.addItemDecoration(itemDecoration);
        
        recList.setItemAnimator(new DefaultItemAnimator());
        
        
        return rootView;
    }

	private PiezaRemitoMobileTO createItemHeader() {
		PiezaRemitoMobileTO p = new PiezaRemitoMobileTO();
		p.setMetros("MTS.");
		p.setNumero("NRO.");
		p.setMetrosEntrada("MTS. ENT.");
		p.setObservaciones("OBSERVACIONES");
		return p;
	}


}