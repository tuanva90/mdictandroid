package tma.sdc;

import java.util.ArrayList;

import tma.sdc.services.DBAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListCategoriesActivity extends MDictActivity{
	private DBAdapter mDbAdapter = null;
	private ListView lv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favourite);
		
		lv = (ListView)findViewById(R.id.favourite_listResult);
		mDbAdapter = new DBAdapter(getApplicationContext());
		mDbAdapter.open();
		ArrayList<String> strListCategories = mDbAdapter.getAllCategoryName();
		ListAdapter listAdapter = new ArrayAdapter<String>(ListCategoriesActivity.this, R.layout.favourite_row, R.id.favourite_txtWord, strListCategories.toArray(new String[strListCategories.size()]));
		lv.setAdapter(listAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bund = new Bundle();
				bund.putString(DBAdapter.KEY_CATEGORY_NAME, (String) lv.getItemAtPosition(arg2));
				Intent inten = new Intent(ListCategoriesActivity.this, FavouriteActivity.class);
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
