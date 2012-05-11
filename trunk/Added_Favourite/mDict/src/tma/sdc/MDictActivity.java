package tma.sdc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;

import tma.sdc.R;
public class MDictActivity extends Activity {
	
	
	private SQLiteDataProvider sdp=null;
   
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ListView lv = (ListView)findViewById(R.id.listview);        

	        LoadConfig();
	        Cursor mcursor = getSQLiteDataBase().rawQuery("Select id as _id, word from a limit 100", null);
        	//startManagingCursor(mcursor);     
        	if(mcursor != null)
        		mcursor.moveToFirst();
        	List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        	String[] from = new String[] {mcursor.getColumnName(1)};
	        int[] to = new int[] { R.id.item1 };
	 
        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.listitems, from, to);
        SimpleCursorAdapter lAdapter = new SimpleCursorAdapter(this, R.layout.listitems, mcursor, from, to);
        lv.setAdapter(lAdapter);
        Log.d("SUCCESS", mcursor.getColumnName(1));
      //  lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	String item="";
               try
               {
                item = ((TextView)view.findViewById(R.id.item1)).getText().toString();
                Intent inten = new Intent(getApplicationContext(), SearchActivity.class);
                Bundle bund = new Bundle();
                bund.putString("word", item);
                inten.putExtras(bund);
                startActivity(inten);
               }
               catch(Exception e)
               {
            	   Log.d("ERROR", "ListView --> onItemClickListener --> " + e.getMessage());
               }
               
            }
        });       
       // handleIntent(getIntent());
     
    }
    
    public SQLiteDatabase getSQLiteDataBase()
    {
    	if(sdp == null)
    		LoadConfig();
    	return sdp.getdb();
    } 
    
  //Load config
    private void LoadConfig()
    { 		   
			 try {
				sdp=new SQLiteDataProvider("/mnt/sdcard/dc1.db");
			 } 
			 catch (Exception e) {
				Log.e("LoadConfig",e.getMessage());
				e.printStackTrace();
			}
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
        	Uri uir = intent.getData();
		    Cursor cs = managedQuery(uir, null, null, null, null);
		    if(cs == null)
		    	finish();
		    else
		    {
		    	cs.moveToFirst();
		    	int windex = cs.getColumnIndexOrThrow(DictionaryDatabase.KEY_WORD);
		    	String item = cs.getString(windex);
		    	Intent inten = new Intent(getApplicationContext(), SearchActivity.class);
		    	Bundle bund = new Bundle();
		    	bund.putString("word", item);
		    	inten.putExtras(bund);
		    	startActivity(inten);
		    }
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String item = intent.getStringExtra(SearchManager.QUERY);
            Intent wordIntent = new Intent(getApplicationContext(), SearchActivity.class);
            Bundle bund = new Bundle();
            bund.putString("word", item);
            wordIntent.putExtras(bund);
            startActivity(wordIntent);
           // showResults(query);
        }
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inf=this.getMenuInflater();
		inf.inflate(R.menu.mainmenu ,menu);		
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.mnOptionExit)
		{
			finish();
		}
		else 
			if(item.getItemId()==R.id.mnOptionSearch)
			{
				this.onSearchRequested();
			}
			else
				if(item.getItemId() == R.id.mnOptionFavourite)
				{
					Intent inten = new Intent(MDictActivity.this, ListCategoriesActivity.class);
					startActivity(inten);
				}
		
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(sdp != null)
		{
			sdp.Close();
			sdp = null;
		}
		super.onDestroy();
	}
	
}

