package ar.com.textillevel.mobile.modules.consultas;

import ar.com.textillevel.mobile.modules.common.FragmentController;
import ar.com.textillevel.mobile.modules.common.to.ETipoDocumento;
import ar.com.textillevel.mobile.modules.cuenta.common.DocumentoClickResolver;

public class ConsultasFragmentController extends FragmentController<ConsultasFragment> {

	public ConsultasFragmentController(ConsultasFragment f) {
		super(f);
	}

	public void btnClickeado(ETipoDocumento tipoDocumento) {
		if(tipoDocumento != ETipoDocumento.CHEQUE){
			getFragment().showDialogInputNumero(tipoDocumento);
		}else{
			getFragment().showDialogInputNumeroYString(tipoDocumento);
		}
	}

	public void buscarDocumento(ETipoDocumento tipoDocumento, String numero, String nroSucursal) {
		DocumentoClickResolver.getInstance().buscarDocumentoPorNumero(tipoDocumento, numero, nroSucursal, getFragment().getActivity());
	}

	public void buscarDocumentoPorNumeroYString(ETipoDocumento tipoDocumento, String numero, String str) {
		DocumentoClickResolver.getInstance().buscarDocumentoPorNumeroYString(tipoDocumento, numero, str, getFragment().getActivity());		
	}
}
