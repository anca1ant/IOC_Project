package com.BARcode.mycarpooling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.BARcode.databaseModels.Carpool;
import com.BARcode.utilities.CarpoolAdapter;

public class ShowHistory extends Activity {

	public String dBcarpoolHistory = "";
	public HashMap<String, List<String>> carpoolHistory;
	public ExpandableListView Exp_list;
	public List<String> carpools;
	public CarpoolAdapter adapter;
	
	class ShowHistoryDB extends AsyncTask<String, String, String> {

		private ProgressDialog progressMessage;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(ShowHistory.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String username = (String) params[0];
			String link = String.format(com.BARcode.utilities.Constants.SERVER_URL
					+ "/get_carpools.php?username=%s", username);

			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(link));

				HttpResponse response = client.execute(request);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				StringBuilder sb = new StringBuilder();
				String line = "";

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}

				String[] lines = sb.toString().split("\n");
				for (int i = 0; i < lines.length - 3; i++) {
					dBcarpoolHistory += lines[i] + "\n";
				}
				runOnUiThread(new Runnable() {
				     @Override
				     public void run() {
				    	List<Carpool> all = getDB();
				 		for(int i = 0; i < all.size(); i++){
				 			carpoolHistory.put(all.get(i).getDate() + " " + all.get(i).getTime(), all.get(i).getInfo());
				 		}
				 		Exp_list = (ExpandableListView) findViewById(R.id.exp_list);
				 		carpools = new ArrayList<String>(carpoolHistory.keySet());
				 		adapter = new CarpoolAdapter(ShowHistory.this, carpoolHistory, carpools);
				 		Exp_list.setAdapter(adapter);
				    }
				     
				     
				});
				
				progressMessage.dismiss();
			} catch (Exception e) {
				Log.e("ERROR:", e.getMessage());
			}
			return null;
		}

	}
	public List<Carpool> getDB(){
		List<Carpool> carpoolHistoryList = new ArrayList<Carpool>();
		
		JSONArray ja = null;
	
			try {
				ja = new JSONArray(dBcarpoolHistory);
				for(int i = 0; i < ja.length(); i++){
					Carpool c = new Carpool(ja.getJSONObject(i));
					carpoolHistoryList.add(c);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return carpoolHistoryList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_history);
		
		carpoolHistory = new HashMap<String, List<String>>();
		new ShowHistoryDB().execute(MainActivity.userLoggedIn.getUsername());
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
