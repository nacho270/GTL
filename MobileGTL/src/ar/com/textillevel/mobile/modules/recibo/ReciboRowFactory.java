package ar.com.textillevel.mobile.modules.recibo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import ar.com.textillevel.mobile.modules.common.to.ChequeTO;
import ar.com.textillevel.mobile.modules.common.to.NotaCreditoTO;
import ar.com.textillevel.mobile.modules.common.to.PagoTO;
import ar.com.textillevel.mobile.modules.cuenta.common.DocumentoClickResolver;
import ar.com.textillevel.mobile.util.GenericUtils;

public class ReciboRowFactory {
	
	public static TableRow createRowNC(final NotaCreditoTO nota, Context context, int bgId, final Activity activity){
		return createRowFechaConceptoImporte(nota.getFecha(), nota.getDescrNC(), nota.getImporte(), nota.getIdDocumento(), nota.getIdTipoDocumento(), context, bgId, activity);
	}
	
	public static TableRow createRowPago(final PagoTO pago, Context context, int bgId, final Activity activity){
		return createRowFechaConceptoImporte(pago.getFecha(), pago.getConcepto(), pago.getImportePagado(), pago.getIdDocumento(), pago.getIdTipoDocumento(),context, bgId, activity);
	}
	
	private static TableRow createRowFechaConceptoImporte(String fecha, String concepto, String importe, final Integer idDocumento, final Integer idTipoDocumento, Context context, int bgId, final Activity activity){
		TableRow tr = new TableRow(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);

		TextView tvFecha = createCommonTextView(context, bgId);
		tvFecha.setTextColor(Color.WHITE);
		tvFecha.setText(fecha);
		tvFecha.setGravity(Gravity.START);
		tvFecha.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvFecha.setPadding(5, 5, 5, 5);
		
		TextView tvConcepto = createCommonTextView(context, bgId);
		tvConcepto.setTextColor(Color.WHITE);
		tvConcepto.setText(concepto);
		tvConcepto.setGravity(Gravity.START);
		tvConcepto.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvConcepto.setPadding(5, 5, 5, 5);
		
		TextView tvImporte = createCommonTextView(context, bgId);
		tvImporte.setTextColor(Color.WHITE);
		tvImporte.setText(importe);
		tvImporte.setGravity(Gravity.START);
		tvImporte.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvImporte.setPadding(5, 5, 5, 5);
		
		tr.addView(tvFecha);
		tr.addView(tvConcepto);
		tr.addView(tvImporte);
		
		tr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(idDocumento!=null && idTipoDocumento!=null){
					DocumentoClickResolver.getInstance().clickDocumento(idTipoDocumento, idDocumento, activity);
				}
			}
		});
		
		return tr;
	}
	
	public static View createRowTituloValor(String titulo, String valor, Context context, int bgId, Activity activity) {
		TableRow tr = new TableRow(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);
		
		valor = GenericUtils.isNullOrEmpty(valor)?" - ": valor;
		
		TextView tvTitulo = createCommonTextView(context, bgId);
		tvTitulo.setTextColor(Color.WHITE);
		tvTitulo.setText(titulo);
		tvTitulo.setGravity(Gravity.START);
		tvTitulo.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvTitulo.setPadding(5, 5, 5, 5);
		
		TextView tvValor = createCommonTextView(context, bgId);
		tvValor.setTextColor(Color.WHITE);
		tvValor.setText(valor);
		tvValor.setGravity(Gravity.START);
		tvValor.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvValor.setPadding(5, 5, 5, 5);
		
		tr.addView(tvTitulo);
		tr.addView(tvValor);
		
		return tr;
	}
	
	public static TableRow createRowCheque(final ChequeTO cheque, final Integer idTipoDocumento, final Integer idDocumento, Context context, int bgId, final Activity activity){
		TableRow tr = new TableRow(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);

		TextView tvBanco = createCommonTextView(context, bgId);
		tvBanco.setTextColor(Color.WHITE);
		tvBanco.setText(cheque.getBanco());
		tvBanco.setGravity(Gravity.START);
		tvBanco.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvBanco.setPadding(5, 5, 5, 5);
		
		TextView tvNumero = createCommonTextView(context, bgId);
		tvNumero.setTextColor(Color.WHITE);
		tvNumero.setText(cheque.getNumero());
		tvNumero.setGravity(Gravity.START);
		tvNumero.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvNumero.setPadding(5, 5, 5, 5);
		
		TextView tvNumeroInterno = createCommonTextView(context, bgId);
		tvNumeroInterno.setTextColor(Color.WHITE);
		tvNumeroInterno.setText(cheque.getNumeroInterno());
		tvNumeroInterno.setGravity(Gravity.START);
		tvNumeroInterno.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvNumeroInterno.setPadding(5, 5, 5, 5);
		
		TextView tvCuit = createCommonTextView(context, bgId);
		tvCuit.setTextColor(Color.WHITE);
		tvCuit.setText(cheque.getCuit());
		tvCuit.setGravity(Gravity.START);
		tvCuit.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvCuit.setPadding(5, 5, 5, 5);
		
		TextView tvImporte = createCommonTextView(context, bgId);
		tvImporte.setTextColor(Color.WHITE);
		tvImporte.setText(cheque.getImporte());
		tvImporte.setGravity(Gravity.START);
		tvImporte.setTextAlignment(TextView.TEXT_ALIGNMENT_GRAVITY);
		tvImporte.setPadding(5, 5, 5, 5);
		
		tr.addView(tvBanco);
		tr.addView(tvNumero);
		tr.addView(tvNumeroInterno);
		tr.addView(tvCuit);
		tr.addView(tvImporte);
		
		tr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(idDocumento!=null && idTipoDocumento!=null){
					DocumentoClickResolver.getInstance().clickDocumento(idTipoDocumento, idDocumento, activity);
				}
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
}
