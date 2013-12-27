package be.arno.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JSonActivity extends Activity {
	
	private static final String LOG_TAG = "ClientJSON";
	
	EditText edtxHostname;
	EditText edtxPort;
	EditText edtxItemId;
	TextView txvwDisplay;
	
	private static final String HOST_NAME = "http://192.168.0.13";
	private static final String ADRESSE_PORT = ":3000";
	private static final String PATH_METHOD = "/items/";
	private static final String JSON_PARAMS = ".json";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_json);
		
		txvwDisplay  = (TextView)findViewById(R.id.json_txvwDisplay);
		edtxHostname = (EditText)findViewById(R.id.json_edtxHostname);
		edtxPort     = (EditText)findViewById(R.id.json_edtxPort);
		edtxItemId   = (EditText)findViewById(R.id.json_edtxItemId);

		edtxHostname.setText(HOST_NAME);
		edtxPort.setText("3000");
		edtxItemId.setText("1");
		
		Button bttnGo = (Button)findViewById(R.id.json_bttnGo);
		bttnGo.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						ResultatsGoogleJSON task = new ResultatsGoogleJSON();
						task.execute(edtxHostname.getText().toString()
								   + ADRESSE_PORT
							       + PATH_METHOD
							       + edtxItemId.getText().toString()
							       + JSON_PARAMS);

						String retour = "";
						
						try { retour = task.get(); }
						catch (InterruptedException e) { e.printStackTrace(); }
						catch (ExecutionException e)   { e.printStackTrace(); }

						txvwDisplay.setText(retour);
					}
				});
	}



	private class ResultatsGoogleJSON extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			
			try {
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				
				if (httpEntity != null) {
					InputStream inputStream = httpEntity.getContent();
					
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					StringBuilder stringBuilder = new StringBuilder();
					
					String line = bufferedReader.readLine();
					while (line != null) {
						stringBuilder.append(line + "\n");
						line = bufferedReader.readLine();
					}
					bufferedReader.close();
					
					//Analyse du retour
					JSONObject jsonObject = new JSONObject(stringBuilder.toString());
					//JSONObject jsonResponseData = jsonObject.getJSONObject("responseData");

					//On parcourt l'arbre des données reçues.
					//String nbrResultats = jsonResponseData.getJSONObject("items").getString("name");
					String resultats = "";
					resultats += jsonObject.getString("name") + "\n";
					resultats += jsonObject.getString("date") + "\n";
					resultats += jsonObject.getBoolean("bool") + "\n";
					resultats += jsonObject.getString("created_at") + "\n";
					resultats += jsonObject.getString("updated_at");

					return resultats;

				}
				
			} catch (IOException e) {
				Log.e(LOG_TAG, e.getMessage());
			} catch (JSONException e) {
				System.out.println("hello");
				Log.e(LOG_TAG, e.getMessage());
			}
			return("");

		}
	}
}