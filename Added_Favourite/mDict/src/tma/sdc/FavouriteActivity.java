package tma.sdc;

import java.util.ArrayList;

import tma.sdc.services.DBAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FavouriteActivity extends MDictActivity{
	
	private DBAdapter mDbAdapter = null;
	private ListView lv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favourite);
		Bundle bund = getIntent().getExtras();
		lv = (ListView)findViewById(R.id.favourite_listResult);
		
		mDbAdapter = new DBAdapter(FavouriteActivity.this);
		mDbAdapter.open();
		ArrayList<String> strListCategories = mDbAdapter.getAllWordFromCategory(bund.getString(DBAdapter.KEY_CATEGORY_NAME));
		ListAdapter listAdapter = new ArrayAdapter<String>(FavouriteActivity.this, R.layout.favourite_row, R.id.favourite_txtWord, strListCategories);
		lv.setAdapter(listAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bund = new Bundle();
				bund.putString(DBAdapter.KEY_WORD, (String) lv.getItemAtPosition(arg2));
				Intent inten = new Intent(FavouriteActivity.this, SearchActivity.class);
				inten.putExtras(bund);
				startActivity(inten);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mDbAdapter.close();
		super.onDestroy();
	}
}
