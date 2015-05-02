package ar.com.textillevel.mobile.modules.consultas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.FragmentWithController;
import ar.com.textillevel.mobile.modules.common.to.ETipoDocumento;

public class ConsultasFragment extends FragmentWithController<ConsultasFragmentController> implements View.OnClickListener {

	public ConsultasFragment() {
		super.setController(new ConsultasFragmentController(this));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.consultas_fragment, container, false);
		((Button) rootView.findViewById(R.id.consultas_btn_factura)).setOnClickListener(this);
		((Button) rootView.findViewById(R.id.consultas_btn_nd)).setOnClickListener(this);
		((Button) rootView.findViewById(R.id.consultas_btn_nc)).setOnClickListener(this);
		((Button) rootView.findViewById(R.id.consultas_btn_rc)).setOnClickListener(this);
		((Button) rootView.findViewById(R.id.consultas_btn_rs)).setOnClickListener(this);
		((Button) rootView.findViewById(R.id.consultas_btn_odp)).setOnClickListener(this);
		((Button) rootView.findViewById(R.id.consultas_btn_ch)).setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		getController().btnClickeado(ETipoDocumento.getById(Integer.valueOf((String)v.getTag())));
	}

	public void showDialogInputNumero(final ETipoDocumento tipoDocumento) {
		final EditText txtNro = new EditText(this.getActivity());
		showInputDialog(txtNro,  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				getController().buscarDocumento(tipoDocumento, txtNro.getText().toString());
			}
		});
	}
	
	private void showInputDialog(View view, DialogInterface.OnClickListener listener){
		new AlertDialog.Builder(this.getActivity()).setTitle("Ingrese número")
		.setView(view)
		.setPositiveButton("Aceptar",listener)
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		}).show();
	}

	public void showDialogInputNumeroYString(final ETipoDocumento tipoDocumento) {
		final EditText txtNro = new EditText(this.getActivity());
		txtNro.setHint("Número");
		txtNro.setWidth(600);
		
		final EditText txtString = new EditText(this.getActivity());
		txtString.setHint("Letra");
		txtString.setWidth(600);
		
		TableLayout tl = new TableLayout(getActivity().getBaseContext());
		
		TableRow trNro = new TableRow(getActivity().getBaseContext());
		trNro.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		trNro.addView(txtNro);
		
		TableRow trString =  new TableRow(getActivity().getBaseContext());
		trString.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		trString.addView(txtString);
		
		tl.addView(trString);
		tl.addView(trNro);
		
		showInputDialog(tl, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				getController().buscarDocumentoPorNumeroYString(tipoDocumento, txtNro.getText().toString(), txtString.getText().toString());
			}
		});
	}
}
