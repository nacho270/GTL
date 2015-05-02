package ar.com.textillevel.mobile.modules.ordenpago;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.FragmentDocumentWithController;
import ar.com.textillevel.mobile.modules.common.to.ChequeTO;
import ar.com.textillevel.mobile.modules.common.to.NotaCreditoTO;
import ar.com.textillevel.mobile.modules.common.to.PagoTO;
import ar.com.textillevel.mobile.modules.ordenpago.to.OrdenDePagoMobileTO;

public class OrdenDePagoFragment extends FragmentDocumentWithController<OrdenDePagoMobileTO, OrdenDePagoFragmentController>{

	private TableLayout tablePagos;
	private TableLayout tableNC;
	private TableLayout tableTransf;
	private TableLayout tableCheque;
	private TableLayout tableTotales;
	
	public OrdenDePagoFragment(OrdenDePagoMobileTO odp) {
		super(odp);
		super.setController(new OrdenDePagoFragmentController(this));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.orden_de_pago_fragment, container, false);
		setDatosCabecera(new DatosCabeceraOrdenDePagoRenderer(getDocument(), rootView));
		agregarDatosPagos(rootView);
		agregarDatosNC(rootView);
		agregarDatosTransferencia(rootView);
		agregarDatosCheque(rootView);
		agregarTotales(rootView);
		return rootView;
	}

	private void agregarDatosTransferencia(View rootView) {
		if(getDocument().getInfoTransferencia()!=null){
			this.tableTransf = (TableLayout) rootView.findViewById(R.id.odp_tableLayoutTransf);
			tableTransf.addView(OrdenDePagoRowFactory.createRowTituloValor("NRO. TRANSF: ", getDocument().getInfoTransferencia().getNroTx(),getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tableTransf.addView(OrdenDePagoRowFactory.createRowTituloValor("IMPORTE: ", getDocument().getInfoTransferencia().getImporteTransfBancaria(),getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tableTransf.addView(OrdenDePagoRowFactory.createRowTituloValor("OBSERVACIONES: ", getDocument().getInfoTransferencia().getObservacionesTx(),getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}else{
			rootView.findViewById(R.id.odp_tr_container_transf).setVisibility(TableLayout.GONE);
			rootView.findViewById(R.id.odp_tr_title_transf).setVisibility(TableLayout.GONE);
		}
	}

	private void agregarTotales(View rootView) {
		this.tableTotales = (TableLayout) rootView.findViewById(R.id.odp_tableLayoutTotales);
		tableTotales.addView(OrdenDePagoRowFactory.createRowTituloValor("TOTAL RET. GANANCIAS: ", getDocument().getTotGanancias(),getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableTotales.addView(OrdenDePagoRowFactory.createRowTituloValor("TOTAL RET. IIBB: ", getDocument().getTotIngBrut(),getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableTotales.addView(OrdenDePagoRowFactory.createRowTituloValor("TOTAL RET. IVA: ", getDocument().getTotIva(),getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableTotales.addView(OrdenDePagoRowFactory.createRowTituloValor("TOTAL EFECTIVO: ", getDocument().getTotEfectivo(),getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableTotales.addView(OrdenDePagoRowFactory.createRowTituloValor("TOTAL ORDEN: ", getDocument().getTotal(),getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void agregarDatosCheque(View rootView) {
		if(getDocument().getCheques()!=null && !getDocument().getCheques().isEmpty()){
			List<ChequeTO> itemsConHeader = crearItemsCheque();
			this.tableCheque = (TableLayout) rootView.findViewById(R.id.odp_tableLayoutCheque);
			for(ChequeTO cheque : itemsConHeader){
				tableCheque.addView(OrdenDePagoRowFactory.createRowCheque(cheque,cheque.getIdTipoDocumento(), cheque.getIdDocumento(), getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	        }
		}else{
			rootView.findViewById(R.id.odp_tr_container_cheque).setVisibility(TableLayout.GONE);
			rootView.findViewById(R.id.odp_tr_title_cheque).setVisibility(TableLayout.GONE);
		}
	}
	
	private void agregarDatosNC(View rootView) {
		if(getDocument().getNotasCredito()!=null && !getDocument().getNotasCredito().isEmpty()){
			List<NotaCreditoTO> itemsConHeader = crearItemsNC();
			this.tableNC = (TableLayout) rootView.findViewById(R.id.odp_tableLayoutNC);
			for(NotaCreditoTO nota : itemsConHeader){
				tableNC.addView(OrdenDePagoRowFactory.createRowNC(nota,getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	        }
		}else{
			rootView.findViewById(R.id.odp_tr_container_nc).setVisibility(TableLayout.GONE);
			rootView.findViewById(R.id.odp_tr_title_nc).setVisibility(TableLayout.GONE);
		}
	}

	private void agregarDatosPagos(View rootView) {
        List<PagoTO> itemsConHeader = crearItemsPago();
        this.tablePagos = (TableLayout) rootView.findViewById(R.id.odp_tableLayoutPagos);
        for(PagoTO pago : itemsConHeader){
        	tablePagos.addView(OrdenDePagoRowFactory.createRowPago(pago,getActivity().getBaseContext(),R.drawable.cell_shape_movimientos,getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
	}
	
	private List<ChequeTO> crearItemsCheque() {
		ChequeTO header = new ChequeTO();
        header.setBanco("BANCO");
        header.setCuit("CUIT");
        header.setImporte("IMPORTE");
        header.setNumero("NUMERO");
        header.setNumeroInterno("NUM. INTERNO");
        
        List<ChequeTO> itemsConHeader = new ArrayList<ChequeTO>();
        itemsConHeader.add(header);
        itemsConHeader.addAll(getDocument().getCheques());
        
        ChequeTO totales = new ChequeTO();
        totales.setBanco("");
        totales.setNumero("TOTAL CHEQUES");
        totales.setNumeroInterno(getDocument().getTotalCheques());
        itemsConHeader.add(totales);
		return itemsConHeader;
	}
	
	private List<NotaCreditoTO> crearItemsNC() {
		NotaCreditoTO header = new NotaCreditoTO();
        header.setFecha("FECHA");
        header.setDescrNC("NOTA");
        header.setImporte("IMPORTE");
        
        List<NotaCreditoTO> itemsConHeader = new ArrayList<NotaCreditoTO>();
        itemsConHeader.add(header);
        itemsConHeader.addAll(getDocument().getNotasCredito());
        
        NotaCreditoTO totales = new NotaCreditoTO();
        totales.setFecha("");
        totales.setDescrNC("TOTAL NC");
        totales.setImporte(getDocument().getTotalPagos());
        itemsConHeader.add(totales);
		return itemsConHeader;
	}

	private List<PagoTO> crearItemsPago() {
		PagoTO header = new PagoTO();
        header.setFecha("FECHA");
        header.setConcepto("CONCEPTO");
        header.setImportePagado("IMPORTE");
        
        List<PagoTO> itemsConHeader = new ArrayList<PagoTO>();
        itemsConHeader.add(header);
        itemsConHeader.addAll(getDocument().getPagos());
        
        PagoTO totales = new PagoTO();
        totales.setFecha("");
        totales.setConcepto("TOTAL PAGOS");
        totales.setImportePagado(getDocument().getTotalPagos());
        itemsConHeader.add(totales);
		return itemsConHeader;
	}
}
