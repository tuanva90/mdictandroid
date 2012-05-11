package tma.sdc.services;

/**
 * 
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author LEHIEU
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	/**
	 * DB info
	 */
	private static String DATABASE_PATH = "/data/data/tma.sdc.activities/databases/";
	//private static String DATABASE_PATH = "file:///android_asset/";
	private static final String DATABASE_NAME = "mFavouriteDB";
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_CREATE = "create table Favourite (_id integer primary key autoincrement, "
			+ "word text , category_name text);";
	private static final String DATABASE_CREATE_FAVOURITE = "create table Category (_id integer primary key autoincrement, "
			+ "category_name text);";
	private Context mCtx;
	
	public DBHelper(Context context) {		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mCtx = context;
		Log.i("FavouriteDB", "DBHelper - constructor - start");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		boolean dbExist = checkDataBase();
		Log.i("FavouriteDB", "DBHelper - onCreate - dbExist =" + dbExist );
		if(dbExist){
    		//do nothing - database already exist
			Log.i("FavouriteDB", "DBHelper - onCreate - before getWritable");
			this.getWritableDatabase(); //use this to call onUpgrade function
			Log.i("FavouriteDB", "DBHelper - onCreate - after getWritable");
    	}else{
    	db.execSQL("drop table if exists Favourite");
		db.execSQL(DATABASE_CREATE);
		db.execSQL(DATABASE_CREATE_FAVOURITE);
		Log.i("FavouriteDB", "DBHelper - onCreate - CreateDB");
    	}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (newVersion != oldVersion){
			Log.i("FavouriteDB", "DBHelper - onUpgrade - *****************");
			mCtx.deleteDatabase(DATABASE_NAME);
			onCreate(db);
		}
	}
	
	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub			
		super.close();
	}
	
	/**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DATABASE_PATH + DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		
    	}catch(Exception e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
}
