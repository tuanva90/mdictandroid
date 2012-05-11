package tma.sdc;

import java.util.ArrayList;

import tma.sdc.services.DBAdapter;
import android.R.anim;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddActivity extends MDictActivity{
	
	private DBAdapter mDbAdapter = null;
	private String strWord = null;
	private String strCategory = null;
	private Spinner ddlToLocation;
	private Button btAdd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//------------>>>>>>>>>>set containview here............
		setContentView(R.layout.add_favourite);
		Intent intent = getIntent();
		Bundle bund = intent.getExtras();
		strWord = bund.getString(DBAdapter.KEY_WORD);
		mDbAdapter = new DBAdapter(getApplicationContext());
		//setResult(1221, intent);
        mDbAdapter.open();
        ddlToLocation = (Spinner)findViewById(R.id.ddlTo);
        btAdd = (Button)findViewById(R.id.btnAddToFavourite);
        btAdd.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				EditText ed = (EditText)findViewById(R.id.txtNewCategory);
				if(ed.getVisibility() == 0)
				{
					if(mDbAdapter.checkIfCategoryExist(ed.getText().toString()))
					{
						Toast.makeText(getApplicationContext(), "Caterogy name existed", Toast.LENGTH_SHORT).show();
					}
					else
					{
						strCategory = ed.getText().toString();
						mDbAdapter.insertCategory(ed.getText().toString());
						addWordToFavourite();
						finish();
					}
				}
				else
				{
					strCategory = (String)ddlToLocation.getSelectedItem();
					addWordToFavourite();
					Toast.makeText(getApplicationContext(), "Insert successful", Toast.LENGTH_SHORT).show();
					finish();
					
				}
			}
		});
        ArrayList<String> arrlist = mDbAdapter.getAllCategoryName();
        arrlist.add("Other");
        String [] liststr = arrlist.toArray(new String[arrlist.size()]);
        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_spinner_item, liststr);
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlToLocation.setAdapter(arrAdapter);
        ddlToLocation.setOnItemSelectedListener(mListenerTo);
             
	}
	private OnItemSelectedListener mListenerTo = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long arg3) { // ontitemselected spinner event
			int index = ddlToLocation.getSelectedItemPosition();
			String st = (String)ddlToLocation.getSelectedItem();
			TextView tview = (TextView)findViewById(R.id.tvNewCategory);
			EditText edNew = (EditText)findViewById(R.id.txtNewCategory);
			if(st.equals("Other"))
			{
				tview.setVisibility(0);
				edNew.setVisibility(0);
				
			}	
			else
			{
				tview.setVisibility(4);
				edNew.setVisibility(4);
				
			}
		}
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	public void addWordToFavourite()
	{
		if (mDbAdapter.checkIfExist(strWord) == false) {
			Log.i("checkIfExist", " Details - checkIfExist false");
			mDbAdapter.insertItem(strWord, strCategory);

			// Create the alert box
			AlertDialog.Builder alertbox = new AlertDialog.Builder(
					AddActivity.this);

			// Set the message to display
			alertbox.setMessage("Add favourite successfully!");

			// Add a neutral button to the alert box and assign a
			// click listener
			alertbox.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {
						// Click listener on the neutral button of
						// alert box
						public void onClick(DialogInterface arg0,
								int arg1) {

						}
					});
			// show alert box
			alertbox.show();
		} else {
			Log.i("checkIfExist", " Details - checkIfExist true");
			// Create the alert box
			AlertDialog.Builder alertbox = new AlertDialog.Builder(
					AddActivity.this);

			// Set the message to display
			alertbox.setMessage("This WORD existed in your favourite list!");

			// Add a neutral button to the alert box and assign a
			// click listener
			alertbox.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {
						// Click listener on the neutral button of
						// alert box
						public void onClick(DialogInterface arg0,
								int arg1) {

						}
					});
			// show alert box
			alertbox.show();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stu
		mDbAdapter.close();
		super.onDestroy();
	}
}
