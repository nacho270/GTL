package ar.com.textillevel.mobile.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;

import android.os.AsyncTask;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.SessionData;
import ar.com.textillevel.mobile.modules.common.handlers.PostExecuteHandler;
import ar.com.textillevel.mobile.util.json.JSONParser;

public class CallAPI extends AsyncTask<String, String, String> {

	private String URL;
	private List<NameValuePair> params;
	private PostExecuteHandler handler;
	private int method;
	
	public static final int POST_METHOD = 1;
	public static final int GET_METHOD = 2;
	
	public CallAPI(String uRL, int method, List<NameValuePair> params, PostExecuteHandler handler) {
		this.URL = uRL;
		this.method = method;
		if(SessionData.getInstance().getUsuarioEnSesion()!=null){
			this.params = new ArrayList<NameValuePair>(params.size()+1);
			this.params.add(new BasicNameValuePair("session_token", SessionData.getInstance().getUsuarioEnSesion().getToken()));
			this.params.addAll(params);
		}else{
			this.params = params;
		}
		this.handler = handler;
	}

	@Override
	protected String doInBackground(String... params) {
		DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
		return method == POST_METHOD ? doPost(httpclient): doGet(httpclient);
	}

	private String executeHttpMethod(HttpClient httpclient, HttpRequestBase method) {
		InputStream inputStream = null;
		try{
			HttpResponse response = httpclient.execute(method);
			
			if(response.getStatusLine().getStatusCode() != 200){
				return JSONParser.toJson(new ErrorTO(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()));
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null){
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private String doGet(DefaultHttpClient httpclient) {
		try{
			String getParams = URL + "?";
			for(NameValuePair nvp : this.params){
				getParams += nvp.getName()+"="+URLEncoder.encode(nvp.getValue(),"UTF-8")+"&";
			}
			getParams = getParams.substring(0, getParams.length()-1);
			HttpGet httpGet = new HttpGet(getParams);
			return executeHttpMethod(httpclient, httpGet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String doPost(DefaultHttpClient httpclient) {
		try {
			HttpPost httpPost = new HttpPost(URL);
			httpPost.setEntity(new UrlEncodedFormEntity(this.params));
			return executeHttpMethod(httpclient, httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(String result) {
		this.handler.handle(result);
	}
}
