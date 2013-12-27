package be.arno.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	TextView myText;
	Button bttn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i("un", "un");

		myText = (TextView)findViewById(R.id.txvwResult);
		bttn = (Button)findViewById(R.id.bttn);
		
		myText.setText(GetHtml("http://192.168.0.13:3000/items/1"));
		// GetHtml("http://192.168.0.13:3000/items/1");
		
		bttn.setOnClickListener(
				new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), JSonActivity.class);
				startActivity(intent);
			}
		});
	
	
	}
	
	public String GetHtml(String adresse) {
		
		Log.i("deux", "deux");
		
		GetHtml_AsyncTask task = new GetHtml_AsyncTask();
						
		task.execute(adresse);
		
		String retour = "";
		try {
			retour = task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retour;
	
	}


	private class GetHtml_AsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			Log.i("trois", "trois");

			DefaultHttpClient httpClient = new DefaultHttpClient();
			StringBuffer stringBuffer = new StringBuffer("");
			
			try {
				URI uri = new URI(params[0]);
				
				HttpGet myRequest = new HttpGet(uri);
				
				HttpResponse res = httpClient.execute(myRequest);
				
				// Log.i("quatre", "quatre");
				// myText.setText(" -- " + res.getStatusLine().getStatusCode());
				
				InputStream inputStream = res.getEntity().getContent();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				
				String ligneLue = bufferedReader.readLine();
				while (ligneLue != null) {
					stringBuffer.append(ligneLue);
					stringBuffer.append("\n");
					ligneLue = bufferedReader.readLine();
				}
				return stringBuffer.toString();
			} catch (Exception e) {
				return e.getMessage();
				
			}
		}
	}
}