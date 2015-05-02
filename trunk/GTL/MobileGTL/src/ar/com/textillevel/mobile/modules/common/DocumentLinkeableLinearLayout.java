package ar.com.textillevel.mobile.modules.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import ar.com.textillevel.mobile.modules.cuenta.common.DocumentoClickResolver;

public class DocumentLinkeableLinearLayout extends LinearLayout {
	
	private Map<String, DocumentoRelMobTO> documentosMap;
	private Activity activity; 

	public DocumentLinkeableLinearLayout(String textoLabel, List<DocumentoRelMobTO> docs, Context context) {
		super(context);	
		this.activity = (Activity)context; 
		this.documentosMap = new HashMap<String, DocumentoRelMobTO>();
		setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams llParamsComponent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		TextView lbl = new TextView(context);
		if(docs.size()>1) {
			lbl.setText(textoLabel.toUpperCase() + "(S) :");
		} else {
			lbl.setText(textoLabel.toUpperCase() + ":");
		}
		lbl.setTextColor(Color.BLACK);
		llParamsComponent.setMargins(0, 0, 10, 0);
		
		addView(lbl, llParamsComponent);

		for(DocumentoRelMobTO documento : docs) {
			TextView docTxtView = new TextView(context);
			docTxtView.setTextColor(Color.BLACK);
			
			String textoMostrar = documento.getTextoMostrar();
			documentosMap.put(textoMostrar, documento);
			
			docTxtView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DocumentoRelMobTO documentoRelTO = documentosMap.get(((TextView)v).getText().toString());
					DocumentoClickResolver.getInstance().clickDocumento(documentoRelTO.getIdTipoDocumento(), documentoRelTO.getIdDocumento(), activity);
				}

			});

			SpannableString content = new SpannableString(textoMostrar);
			content.setSpan(new UnderlineSpan(), 0, textoMostrar.length(), 0);
			content.setSpan(new StyleSpan(Typeface.BOLD), 0, textoMostrar.length(), 0);

			docTxtView.setText(content);
			addView(docTxtView, llParamsComponent);
			
		}
		
	}

}
