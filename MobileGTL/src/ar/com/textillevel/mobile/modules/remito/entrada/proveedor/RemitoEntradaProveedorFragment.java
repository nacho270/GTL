package ar.com.textillevel.mobile.modules.remito.entrada.proveedor;

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
import ar.com.textillevel.mobile.modules.remito.entrada.proveedor.to.PiezaRemitoProvMobileTO;
import ar.com.textillevel.mobile.modules.remito.entrada.proveedor.to.RemitoEntradaProvMobileTO;

public class RemitoEntradaProveedorFragment extends FragmentDocumentWithController<RemitoEntradaProvMobileTO, RemitoEntradaProveedorFragmentController> {

    public RemitoEntradaProveedorFragment(RemitoEntradaProvMobileTO document) {
		super(document);
		super.setController(new RemitoEntradaProveedorFragmentController(this));
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.remito_entrada_proveedor_fragment, container, false);
        super.setDatosCabecera(new DatosCabeceraRemitoEntradaProveedorRenderer(getDocument(), rootView));

        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.re_p_cardList_piezas);
        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        recList.setHasFixedSize(true);

        PiezaRemitoProvMobileTO header = createItemHeader();
        List<PiezaRemitoProvMobileTO> itemsConHeader = new ArrayList<PiezaRemitoProvMobileTO>();
        itemsConHeader.add(header);
        itemsConHeader.addAll(getDocument().getPiezas());
        
        PiezaRemitoProveedorAdapter adapter = new PiezaRemitoProveedorAdapter(itemsConHeader);
        recList.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recList.addItemDecoration(itemDecoration);
        
        recList.setItemAnimator(new DefaultItemAnimator());
        
        return rootView;
    }

	private PiezaRemitoProvMobileTO createItemHeader() {
		PiezaRemitoProvMobileTO p = new PiezaRemitoProvMobileTO();
		p.setCantidadMasUnidad("CANT.");
		p.setContenedor("CONT.");
		p.setCantContenedor("CANT. CONT.");
		p.setDescripcion("DESCRIPCION");
		return p;
	}
}
