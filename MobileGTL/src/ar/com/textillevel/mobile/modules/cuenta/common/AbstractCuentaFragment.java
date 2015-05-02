package ar.com.textillevel.mobile.modules.cuenta.common;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.FragmentWithController;
import ar.com.textillevel.mobile.modules.cuenta.to.ContactoTO;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaTO;
import ar.com.textillevel.mobile.modules.cuenta.to.MovimientoTO;

public abstract class AbstractCuentaFragment extends FragmentWithController<CuentaFragmentController> {

	private TableLayout tableMovimientos;
	private EditText txtInput;
	private Button btnBuscar;
	private CuentaTO cuentaRes;

	public AbstractCuentaFragment() {
		super.setController(new CuentaFragmentController(this));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.cuenta_cliente_fragment, container, false);
		btnBuscar = ((Button) rootView.findViewById(R.id.btnBuscar));
		txtInput = (EditText) rootView.findViewById(R.id.editText1);
		final Spinner spCantMovs = (Spinner) rootView.findViewById(R.id.spinnerCantMovs);
		this.tableMovimientos = (TableLayout) rootView.findViewById(R.id.tableLayoutMovimientos);
		fillSpinnerMovimientos(spCantMovs);
		btnBuscar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (txtInput.getText().toString() != null && !txtInput.getText().toString().isEmpty()){
					Integer cantidad = Integer.valueOf((String) spCantMovs.getSelectedItem());
					try {
						Toast.makeText(getActivity().getApplicationContext(), "Buscando...", Toast.LENGTH_SHORT).show();
						getController().buscarMovimientos(getTipoCuenta().getId(), cantidad, txtInput.getText().toString());
					} catch (Exception e) {
						Toast.makeText(getActivity().getBaseContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getActivity().getBaseContext(), "Debe ingresar su búsqueda", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		if(cuentaRes != null) {//cuando el fragment "vuelve" a estar visible (back button por ejemplo) 
			llenarDatos();
		}
		
		return rootView;
	}

	private void fillSpinnerMovimientos(final Spinner spCantMovs) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.cantidad_movimientos, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCantMovs.setAdapter(adapter);
	}

	public void displayResultadoBusquedaContacto(List<ContactoTO> resultadoBusquedaContacto) {
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(this.getActivity());
		builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle("Seleccione uno:");
		final ArrayAdapter<ContactoTO> arrayAdapter = new ArrayAdapter<ContactoTO>(this.getActivity(), android.R.layout.select_dialog_singlechoice);
		arrayAdapter.addAll(resultadoBusquedaContacto);
		builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ContactoTO contacto = arrayAdapter.getItem(which);
				txtInput.setText(String.valueOf(contacto.getId()));
				btnBuscar.callOnClick();
			}
		});
		builderSingle.show();
	}

	public void displayDatosCuenta(CuentaOwnerTO owner) {
		tableMovimientos.addView(MovimientoTableRowFactory.createEmptyRow(getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableMovimientos.addView(MovimientoTableRowFactory.createRowOwnerCuenta(owner,getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableMovimientos.addView(MovimientoTableRowFactory.createRowTelefono(owner,getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableMovimientos.addView(MovimientoTableRowFactory.createRowEmail(owner,getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableMovimientos.addView(MovimientoTableRowFactory.createRowDireccion(owner,getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableMovimientos.addView(MovimientoTableRowFactory.createRowLocalidad(owner,getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableMovimientos.addView(MovimientoTableRowFactory.createRowCelular(owner,getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableMovimientos.addView(MovimientoTableRowFactory.createEmptyRow(getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	public void displaySinResultados() {
		tableMovimientos.addView(MovimientoTableRowFactory.createRowSinMovimientos(getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	public void addRowMovimiento(MovimientoTO mto) {
		tableMovimientos.addView(MovimientoTableRowFactory.createRowMovimiento(mto, getMovimientoRenderer(),getActivity().getBaseContext(),R.drawable.cell_shape_movimientos, getActivity()), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	public void displayHeaderColums() {
		tableMovimientos.addView(MovimientoTableRowFactory.createHeaderRow(getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));		
	}

	public void clearTable() {
		tableMovimientos.removeAllViewsInLayout();
		tableMovimientos.addView(MovimientoTableRowFactory.createEmptyRow(getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableMovimientos.addView(MovimientoTableRowFactory.createTitleRow(getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	public void displayCuentaNoEncontrada() {
		tableMovimientos.addView(MovimientoTableRowFactory.createRowCuentaNoEncontrada(getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));		
	}
	
	public void displaySaldo(Float saldo){
		tableMovimientos.addView(MovimientoTableRowFactory.createEmptyRow(getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tableMovimientos.addView(MovimientoTableRowFactory.createRowSaldoCuenta(saldo,getActivity().getBaseContext(),R.drawable.cell_shape_movimientos),
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	protected abstract MovimientoRenderer getMovimientoRenderer();
	protected abstract ETipoCuenta getTipoCuenta();

	public void setCuentaTO(CuentaTO cuentaRes) {
		this.cuentaRes = cuentaRes;
	}

	public CuentaTO getCuentaRes() {
		return cuentaRes;
	}

	public void llenarDatos() {
		if(cuentaRes.getResultadoBusquedaContacto()!=null && !cuentaRes.getResultadoBusquedaContacto().isEmpty()){
			displayResultadoBusquedaContacto(cuentaRes.getResultadoBusquedaContacto());
			return;
		}
		clearTable();
		if(cuentaRes.getOwner() == null){
			displayCuentaNoEncontrada();
			return;
		}
		displayDatosCuenta(cuentaRes.getOwner());
		if(cuentaRes.getMovimientos()!=null && !cuentaRes.getMovimientos().isEmpty()){
			displayHeaderColums();
			List<MovimientoTO> movimientos = cuentaRes.getMovimientos();
			for(MovimientoTO mto : movimientos){
				addRowMovimiento(mto);
			}
			displaySaldo(cuentaRes.getSaldo());
		}else{
			displaySinResultados();
		}
	}

}