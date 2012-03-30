package tma.sdc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends MDictActivity {
private TextView lbl;
private String lastQuery;
//private MediaPlayer mp;
private WebView wb;
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.search);
	        lbl=(TextView)findViewById(R.id.textView1);
	        wb=(WebView)findViewById(R.id.webView1);
	        wb.setWebViewClient(new MyWebSettings(this));
	        wb.getSettings().setJavaScriptEnabled(true);
	        wb.setScrollBarStyle(wb.SCROLLBARS_INSIDE_OVERLAY);
	        //get the intent, verify the action is search and get the query
	        handleIntent(getIntent());
	   	    }
	   @Override
	   protected void	onNewIntent(Intent intent)
	   {
		   setIntent(intent);
		   handleIntent(intent);
	   }
	   public void handleIntent(Intent it)
	   {
		   if(Intent.ACTION_SEARCH.equals(it.getAction()))
	   		{
	   		String query=it.getStringExtra(SearchManager.QUERY);
	   		LookUp(query);
	   		
	   		}
	   }
	   @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			if(item.getItemId()==R.id.mnOptionPronounce)
			{
				Pronounce(this.lastQuery);
			}
			return super.onOptionsItemSelected(item);
		}
	   public void Pronounce(String query)
	   {
		   try
		   {
               ZipFile zip = new ZipFile("/mnt/sdcard/en.zip");
               ZipEntry entry = zip.getEntry(query+".wav");
               if (entry != null) {
                   InputStream in = zip.getInputStream(entry);
                   // see Note #3.
                   File tempFile = File.createTempFile("audio", ".wav", this.getFilesDir());
                   FileOutputStream out = new FileOutputStream(tempFile);
                   IOUtils.copy(in, out);      
                   MediaPlayer mp=new MediaPlayer();
                   FileInputStream fis=new FileInputStream(tempFile);
                   mp.setOnPreparedListener(new OnPreparedListener() {
                	    public void onPrepared(MediaPlayer m) {
                	        m.start();
                	    }
                	});
                   mp.setDataSource(fis.getFD());
                   mp.prepareAsync();
                  
                   tempFile.deleteOnExit();
               }
               else
               {
            	   Toast.makeText(getApplicationContext(),"Not found",3).show();
               }
		   }catch(Exception e)
		   {
			  this.wb.loadData("", "text/html","utf-8");
			  wb.loadData(e.getMessage(),"text/html","utf-8");
		   }
	   }
 
	   /* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stu
		super.onDestroy();
	}
	public void LookUp(String query)
	   {
		 //. String path="/mnt/sdcard/dict.db";
		query=query.trim();
		   this.lastQuery=query;
		   File dbfile = new File("/mnt/sdcard/dc1.db" ); 
		   SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(dbfile, null);
		   String tbl=query.substring(0,1);
		   Cursor cr = db.rawQuery("select word,content from "+tbl+" where word='"+query+"'", null);
		   String html="<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"><script language=\"javascript\" type=\"text/javascript\">function show(){ if(this.id==\"contentShow\"){this.id=\"contentHide\";} else {this.id=\"contentShow\";} }function onload(){ var list=document.body.getElementsByTagName(\"div\"); for(i=0;i<list.length;i++)  {   if(list[i].id==\"contentShow\")  { list[i].onclick=show;}  }  }</script><link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"></head><body onload=\"onload();\"> " ;
		   if(cr.getCount()>0){
		   cr.moveToFirst();
		   String result=html+cr.getString(1).replaceAll("<br />","").replaceAll("\"","'")+"</body></html>";
		   wb.loadDataWithBaseURL("file:///android_asset/",result, "text/html", "utf-8", null);}
		   else
		   {
		    wb.loadData(html+"<span class=color:#ff00ff;>Cannot find \" "+query+"\" defination.</span></body></html>", "text/html","utf-8");
		   }
		   //wb.loadData,"text/html","uft8");
		 // wb.loadUrl("file:///android_asset/hello.html");
		
		   db.close();
	   }

}
class MyWebSettings extends WebViewClient
{
	SearchActivity sa;
	public MyWebSettings(SearchActivity sa)
	{
		this.sa=sa;
		
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView wv,String url)
	{
			if(url.indexOf("entry:")>-1)
			{
				String query=url.substring(8);
				sa.LookUp(query);
			}
			return false;
	}
}
