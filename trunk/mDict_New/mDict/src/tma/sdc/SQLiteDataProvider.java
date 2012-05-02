package tma.sdc;

import java.io.File;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.TimingLogger;

public class SQLiteDataProvider extends DataProvider {
	private SQLiteDatabase db;
	public SQLiteDatabase getdb()
	{
		return db;
	}
	public SQLiteDataProvider(String sqliteFilePath) throws Exception
	{
		File f=new File(sqliteFilePath);
		if(f.exists())
		{
			db=SQLiteDatabase.openDatabase(sqliteFilePath, null,SQLiteDatabase.NO_LOCALIZED_COLLATORS);//1 open Read Only
		}
		else throw new Exception("Database file is not exits.");
	}
	@Override
	public String GetName()
	{
		return "SQLite Data Provider";
	}
	@Override
	public String GetDefination(String word) {
		// TODO Auto-generated method stub
		String returnValue="<h1>Connect to database failed.</h1>";
		if(db.isOpen())
		{
			Cursor cs=db.rawQuery("Select Word,Content from "+word.substring(0,1)+" where Word='"+word+"'" ,null);	
			if(cs.getCount()>0)
			{
				  cs.moveToFirst();
				  returnValue=cs.getString(1).replaceAll("<br />","").replaceAll("\"","'");
			}
			else
			{
				returnValue=null;
			}
		}
		return returnValue;
	}

	@Override
	public String[] GetSuggestion(String prefix) {
		// TODO Auto-generated method stub
		String[] returnValue=null;
		if(db.isOpen())
		{
			Cursor cs=db.rawQuery("Select Word from "+prefix.substring(0,1)+" where Word like '"+prefix+"%' limit 15",null);
			if(cs.getCount()>0)
			{
				  returnValue=new String[cs.getCount()];
				  int start=0;
				  while(cs.moveToNext())
				  {
					  returnValue[start++]=cs.getString(0);
				  }
			}
//			else
//			{
//				//returnValue=new String[]{"No suggestion found."};
//			}
		}
//		else
//		{
//			//r/eturnValue=new String[]{"Connect to database failed."};
//		}
		return returnValue;
	}
	@Override
	public void Close() {
		// TODO Auto-generated method stub
		db.close();
		db=null;
		System.gc();
	}
	
	public Cursor getAllWord(String table) {
		// TODO Auto-generated method stub
		
		Cursor cs = null;
		if(db.isOpen())
		{
			cs=db.rawQuery("Select Word,Content from "+table, null);				
		}
		return cs;
	}
	//get word by id
	public Cursor getWordById(String id) {
		// TODO Auto-generated method stub
		
		Cursor cs = null;
		if(db.isOpen())
		{
			cs=db.rawQuery("Select Word,Content from a where id = " + id, null);				
		}
		return cs;
	}
	
	//get word by id
		public Cursor getWordMatch(String word) {
			// TODO Auto-generated method stub
			
			Cursor cs = null;
			if(db.isOpen())
			{
				cs=db.rawQuery("Select Word,Content from a where Word like '" + word + "%' limit 20", null);				
			}
			return cs;
		}


}
