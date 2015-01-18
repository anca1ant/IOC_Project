package com.BARcode.mycarpooling;

import static com.BARcode.utilities.Constants.ADD_CARPOOL;
import static com.BARcode.utilities.Utilities.fieldsValid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddCarpool extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_carpool);
	}
	
	//cand se intra pe butonul de next
	public void nextOptions(View view){
		EditText sourceET = (EditText) findViewById(R.id.addSource);
		EditText destinationET = (EditText) findViewById(R.id.addDestination);
		EditText dateET = (EditText) findViewById(R.id.carpoolDate);
		EditText hourET = (EditText) findViewById(R.id.carpoolTime);
		EditText priceET = (EditText) findViewById(R.id.carpoolPrice);
		EditText durationET = (EditText) findViewById(R.id.carpoolDuration);
		
		String source = sourceET.getText().toString();
		String destination = destinationET.getText().toString();
		String date = dateET.getText().toString();
		String hour = hourET.getText().toString();
		String price = priceET.getText().toString();
		String duration = durationET.getText().toString();
		
		TextView notAllFieldsCompleted = (TextView) findViewById(R.id.notAllFieldsCompletedAC);
		if (!fieldsValid(source, destination, date, hour, price, duration)) {
			notAllFieldsCompleted.setVisibility(View.VISIBLE);
		} else {
			notAllFieldsCompleted.setVisibility(View.INVISIBLE);
			
			String[] params = new String[6];
			params[0] = source; params[1] = destination; params[2] = date; 
			params[3] = hour; params[4] = price; params[5] = duration;
			
			Intent intent = new Intent(this, AddCarpool2.class);
			intent.putExtra(ADD_CARPOOL, params);
	    	startActivity(intent);
		}
	}

}
