package ar.com.textillevel.mobile.modules.cuenta.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaOwnerTO;
import ar.com.textillevel.mobile.modules.cuenta.to.MovimientoTO;
import ar.com.textillevel.mobile.util.GenericUtils;

public class MovimientoTableRowFactory {
	
	public static TableRow createRowMovimiento(final MovimientoTO mto, MovimientoRenderer renderer, Context context, int bgId, final Activity activity){
		TableRow tr = new TableRow(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);

		TextView tvDescripcion = createCommonTextView(context, bgId);
		tvDescripcion.setTextColor(Color.WHITE);
		tvDescripcion.setText(mto.getDescripcion());
		tvDescripcion.setGravity(Gravity.LEFT);
		tvDescripcion.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvDescripcion.setPadding(5, 5, 5, 5);
		
		
		TextView tvDebe = null;
		TextView tvHaber = null;
		TextView tvTransporte = null;
		if(!mto.isTransporte()){
			tvDebe = createCommonTextView(context, bgId);
			tvDebe.setTextColor(renderer.getTextColorDebe());
			tvDebe.setText(renderer.getStrMontoDebe(mto.getMonto()));
			tvDebe.setPadding(5, 5, 5, 5);
	
			tvHaber = createCommonTextView(context, bgId);
			tvHaber.setText(renderer.getStrMontoHaber(mto.getMonto()));
			tvHaber.setTextColor(renderer.getTextColorHaber());
			tvHaber.setPadding(5, 5, 5, 5);
		}else{
			tvTransporte = createCommonTextView(context, bgId);
			tvTransporte.setTextColor(Color.WHITE);
			tvTransporte.setText(" ");
			tvTransporte.setPadding(5, 5, 5, 5);
			TableRow.LayoutParams p = new TableRow.LayoutParams();
			p.span = 2;
			tvTransporte.setLayoutParams(p);
		}

		TextView tvSaldoParcial = createCommonTextView(context, bgId);
		tvSaldoParcial.setText(GenericUtils.formatMonto(mto.getSaldoParcial()));
		if(!mto.isTransporte()){
			tvSaldoParcial.setTextColor(renderer.getTextColorSaldoParcial(mto.getSaldoParcial()));
		}else{
			tvSaldoParcial.setTextColor(Color.WHITE);
		}
		tvSaldoParcial.setPadding(5, 5, 5, 5);
		
		TextView tvObservaciones = createCommonTextView(context, bgId);
		tvObservaciones.setTextColor(Color.WHITE);
		tvObservaciones.setText(mto.getObservaciones());
		tvObservaciones.setGravity(Gravity.LEFT);
		tvObservaciones.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvObservaciones.setPadding(5, 5, 5, 5);
		
		tr.addView(tvDescripcion);
		if(!mto.isTransporte()){
			tr.addView(tvDebe);
			tr.addView(tvHaber);
		}else{
			tr.addView(tvTransporte);
		}
		tr.addView(tvSaldoParcial);
		tr.addView(tvObservaciones);
		
		tr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DocumentoClickResolver.getInstance().clickDocumento(mto.getIdTipoDocumento(), mto.getIdDocuemento(), activity);
			}
		});

		return tr;
	}
	
	private static TextView createCommonTextView(Context context, int bgId){
		TextView tv = new TextView(context);
		tv.setBackgroundColor(Color.BLACK);
		tv.setBackgroundResource(bgId);
		tv.setGravity(Gravity.CENTER);
		tv.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		return tv;
	}

	public static View createRowSinMovimientos(Context context, int bgId) {
		return createSingleLineRow("SIN MOVIMIENTOS", "", context, bgId);
	}

	public static View createHeaderRow(Context context, int bgId) {
		TableRow tr = new TableRow(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);

		TextView tvDescripcion = createCommonTextView(context, bgId);
		tvDescripcion.setTextColor(Color.WHITE);
		tvDescripcion.setText("Descripción");
		tvDescripcion.setTypeface(tvDescripcion.getTypeface(), Typeface.BOLD);
//		tvDescripcion.setPaintFlags(tvDescripcion.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		
		TextView tvDebe = createCommonTextView(context, bgId);
		tvDebe.setTextColor(Color.WHITE);
		tvDebe.setText("Debe");
		tvDebe.setTypeface(tvDescripcion.getTypeface(), Typeface.BOLD);

		TextView tvHaber = createCommonTextView(context, bgId);
		tvHaber.setTextColor(Color.WHITE);
		tvHaber.setText("Haber");
		tvHaber.setTypeface(tvDescripcion.getTypeface(), Typeface.BOLD);

		TextView tvSaldoParcial = createCommonTextView(context, bgId);
		tvSaldoParcial.setText("Saldo");
		tvSaldoParcial.setTextColor(Color.WHITE);
		tvSaldoParcial.setTypeface(tvDescripcion.getTypeface(), Typeface.BOLD);
		
		TextView tvObservaciones = createCommonTextView(context, bgId);
		tvObservaciones.setText("Observaciones");
		tvObservaciones.setTextColor(Color.WHITE);
		tvObservaciones.setTypeface(tvDescripcion.getTypeface(), Typeface.BOLD);
		
		tr.addView(tvDescripcion);
		tr.addView(tvDebe);
		tr.addView(tvHaber);
		tr.addView(tvSaldoParcial);
		tr.addView(tvObservaciones);
		
		return tr;
	}

	public static View createRowSaldoCuenta(Float saldo, Context context, int bgId) {
		return createSingleLineRow("Saldo", GenericUtils.formatMonto(saldo), context, bgId);
	}

	public static View createRowOwnerCuenta(CuentaOwnerTO owner, Context context, int bgId) {
		return createSingleLineRow("Cuenta", owner.getRazonSocial(), context, bgId);	
	}

	public static View createRowEmail(CuentaOwnerTO owner, Context context, int bgId) {
		return createSingleLineRow("Email", owner.getEmail(), context, bgId);
	}
	
	public static View createRowDireccion(CuentaOwnerTO owner, Context context, int bgId) {
		return createSingleLineRow("Dirección", owner.getDireccion(), context, bgId);	}
	
	public static View createRowLocalidad(CuentaOwnerTO owner, Context context, int bgId) {
		return createSingleLineRow("Localidad", owner.getLocalidad(), context, bgId);
	}
	
	public static View createRowTelefono(CuentaOwnerTO owner, Context context, int bgId) {
		return createSingleLineRow("Teléfono", owner.getTelefono(), context, bgId);
	}
	
	public static View createRowCelular(CuentaOwnerTO owner, Context context, int bgId) {
		return createSingleLineRow("Celular", owner.getCelular(), context, bgId);
	}
	
	private static View createSingleLineRow(String title, String value, Context context, int bgId){
		TableRow tr = new TableRow(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);
		TextView tvLblOwner = createCommonTextView(context, bgId);
		tvLblOwner.setTextColor(Color.WHITE);
		tvLblOwner.setGravity(Gravity.LEFT);
		tvLblOwner.setText(title + (value == null || !value.trim().equals("")?": ": "") + (value==null?" - " : value));
		tvLblOwner.setTypeface(tvLblOwner.getTypeface(), Typeface.BOLD);
		tr.addView(tvLblOwner);
		tvLblOwner.setPadding(5, 5, 5, 5);
		TableRow.LayoutParams p = new TableRow.LayoutParams();
		p.span = 5;
		tvLblOwner.setLayoutParams(p);
		
		return tr;
	}

	public static View createEmptyRow(Context context, int bgId) {
		TableRow tr = new TableRow(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);

		TextView tvLblOwner = createCommonTextView(context, bgId);
		tvLblOwner.setBackgroundColor(Color.WHITE);
		tvLblOwner.setTextColor(Color.WHITE);
		tvLblOwner.setGravity(Gravity.LEFT);
		tvLblOwner.setText(" ");

		tr.addView(tvLblOwner);
		
		return tr;
	}

	public static View createTitleRow(Context context, int bgId) {
		TableRow tr =  new TableRow(context);
		TextView tvTitle = createCommonTextView(context, bgId);
		tvTitle.setTextColor(Color.BLACK);
		tvTitle.setBackgroundColor(Color.WHITE);
		tvTitle.setGravity(Gravity.CENTER);
		tvTitle.setText("Movimientos");
		tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);
		tvTitle.setTextSize(18);
		TableRow.LayoutParams p = new TableRow.LayoutParams();
		p.span = 4;
		tvTitle.setLayoutParams(p);

		tr.addView(tvTitle);
		return tr;
	}
	
	public static View createRowCuentaNoEncontrada(Context context, int bgId) {
		return createSingleLineRow("NO SE HAN ENCONTRADO DATOS", "", context, bgId);
	}
}
