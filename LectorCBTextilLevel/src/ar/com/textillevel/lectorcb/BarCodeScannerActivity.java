package ar.com.textillevel.lectorcb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarCodeScannerActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_scanner);
		Button btn = (Button)findViewById(R.id.btnScanAgain);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initScan();
			}
		});
		
		initScan();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		TextView txt = (TextView)findViewById(R.id.txtResult);
		txt.setText("");
		IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		String capture2 = intentResult.toString();
		if(capture2 == null || capture2 == "") {
			txt.setText("Capture = EMPTY");
		} else {
			txt.setText(capture2);
			writeFileCB(capture2);
		}
	}

	private void writeFileCB(String capture2) { 
		try {
			File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "cb_leido.txt");
			BufferedWriter bf = new BufferedWriter(new FileWriter(folder));
			bf.append(capture2);
			bf.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void initScan() {
		IntentIntegrator scanIntegrator = new IntentIntegrator(BarCodeScannerActivity.this);
		scanIntegrator.initiateScan();
	}

}