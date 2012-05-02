package tma.sdc;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DictionaryDatabase {

	//The columns we'll include in the dictionary table
    public static final String KEY_WORD = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String KEY_DEFINITION = SearchManager.SUGGEST_COLUMN_TEXT_2;

   // private final DictionaryOpenHelper mDatabaseOpenHelper;
    //New field
    private SQLiteDataProvider sdp=null;
    public SQLiteDatabase getSQLiteDataBase()
    {
    	if(sdp == null)
    		LoadConfig();
    	return sdp.getdb();
    }    
    //End 

    /**
     * Constructor
     * @param context The Context within which to work, used to create the DB
     */
    public DictionaryDatabase(Context context) {
    	LoadConfig();
    }

    //Load config
    private void LoadConfig()
    { 		   
			 try {
				sdp=new SQLiteDataProvider("/mnt/sdcard/dc1.db");
			 } 
			 catch (Exception e) {
				//Log.e("LoadConfig",e.getMessage());
				e.printStackTrace();
			}
    }
    /**
     * Returns a Cursor positioned at the word specified by rowId
     *
     * @param rowId id of word to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching word, or null if not found.
     */
    public Cursor getWord(String word) {
        SQLiteDatabase db = getSQLiteDataBase();
        // Cursor cs=db.rawQuery("Select id as _id, Word as " + KEY_WORD + ", content as " + KEY_DEFINITION + ", id as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID + " from a where Word like '"+ query+"%' limit 15",null);
         Cursor cs = null;
         try
         {
         cs=db.rawQuery("Select Word as " + KEY_WORD + ", content as " + KEY_DEFINITION + " from " + word.substring(0, 1) + " where Word = '" + word + "'", null);
         }
         catch(Exception e)
         {
        	 Log.d("ERROR", e.getMessage());
         }
         if (cs == null) {
             return null;
         } else if (!cs.moveToFirst()) {
             cs.close();
             Log.d("ERROR", "getWord(rowId, columns)->cs.movetofirst()--->nulls");
             return null;
         }
         return cs;
    }
    
    public Cursor getWordRefresh(String word) {
        SQLiteDatabase db = getSQLiteDataBase();
        // Cursor cs=db.rawQuery("Select id as _id, Word as " + KEY_WORD + ", content as " + KEY_DEFINITION + ", id as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID + " from a where Word like '"+ query+"%' limit 15",null);
         Cursor cs = null;
         try
         {
         cs=db.rawQuery("Select id as _id, Word as " + KEY_WORD + ", content as " + KEY_DEFINITION + ", id as " + SearchManager.SUGGEST_COLUMN_SHORTCUT_ID + ", id as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID + " from " + word.substring(0, 1) + " where Word = '" + word + "'", null);
         }
         catch(Exception e)
         {
        	 Log.d("ERROR", e.getMessage());
         }
         if (cs == null) {
             return null;
         } else if (!cs.moveToFirst()) {
             cs.close();
             Log.d("ERROR", "getWord(rowId, columns)->cs.movetofirst()--->nulls");
             return null;
         }
         return cs;
    }

    /**
     * Returns a Cursor over all words that match the given query
     *
     * @param query The string to search for
     * @param columns The columns to include, if null then all are included
     * @return Cursor over all words that match, or null if none found.
     */
    
    public Cursor getWordMatches(String query) {       
        SQLiteDatabase db = getSQLiteDataBase();
       // Cursor cs=db.rawQuery("Select id as _id, Word as " + KEY_WORD + ", content as " + KEY_DEFINITION + ", id as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID + " from a where Word like '"+ query+"%' limit 15",null);
        Cursor cs=db.rawQuery("Select id as _id, Word as " + KEY_WORD + ", Word as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID + " from " + query.substring(0, 1) + " where Word like '"+ query+"%' limit 15",null);
        if (cs == null) {
            return null;
        } else if (!cs.moveToFirst()) {
            cs.close();
            Log.d("ERROR", "cs.movetofirst()--->nulls");
            return null;
        }
        return cs;       
    }
}
