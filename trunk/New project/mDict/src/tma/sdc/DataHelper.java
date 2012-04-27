package tma.sdc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DataHelper extends SQLiteOpenHelper {
private static final String dbName="config";
	public DataHelper(Context context)
	{
		super(context,dbName,null,2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String cmd="CREATE TABLE History(ID INTEGER,WORD TEXT)";	
		db.execSQL(cmd);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("Drop table if exists History");
		onCreate(db);
	}

}
