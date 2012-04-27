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
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.util.TimingLogger;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends MDictActivity {
//private TextView lbl;
private String lastQuery;
private WebView wb;
private Dictionary dictionary;
private TextView leftText;
private TextView rightText;
private static final String html="<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"><script language=\"javascript\" type=\"text/javascript\">function show(){ if(this.id==\"contentShow\"){this.id=\"contentHide\";} else {this.id=\"contentShow\";} }function onload(){ var list=document.body.getElementsByTagName(\"div\"); for(i=0;i<list.length;i++)  {   if(list[i].id==\"contentShow\")  { list[i].onclick=show;}  }  }</script><link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"></head><body onload=\"onload();\"> " ;
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
		   requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			super.onCreate(savedInstanceState);
	       
	        setContentView(R.layout.search);
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_1);
	        leftText = (TextView) findViewById(R.id.left_text);
	         rightText = (TextView) findViewById(R.id.right_text);
	    
	
	        //lbl=(TextView)findViewById(R.id.textView1);
	        wb=(WebView)findViewById(R.id.webView1);
	        wb.setWebViewClient(new MyWebSettings(this));
	        wb.getSettings().setJavaScriptEnabled(true);
	        wb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	        LoadConfig();
	        //get the intent, verify the action is search and get the query
	        handleIntent(getIntent());
	      
	    
	   	    }
	   public Dictionary GetDictionary()
	   {
		   return this.dictionary;
	   }
	   public void SetDictionary(Dictionary dict)
	   {
		   this.dictionary=dict;
	   }
	   private void LoadConfig()
	   {
		   SQLiteDataProvider sdp=null;
			   try {
				sdp=new SQLiteDataProvider("/mnt/sdcard/dc1.db");
			} catch (Exception e) {
				Log.e("LoadConfig",e.getMessage());
				e.printStackTrace();
			}
			   if(sdp!=null)
			   {
				  this.dictionary=new Dictionary(sdp);
			   }
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
	   		//GetSuggestion(query);
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
			   TimingLogger logger = new TimingLogger("MyTag", "Initialization");			  
               ZipFile zip = new ZipFile("/mnt/sdcard/pronounce.zip");
               ZipEntry entry = zip.getEntry(query.substring(0,1)+"/"+query+".wav");
               logger.addSplit("Find entry");
               if (entry != null) {
                   InputStream in = zip.getInputStream(entry);
                 	logger.addSplit("Extract entry");
                   // see Note #3.
                   File tempFile = File.createTempFile("audio", ".wav", this.getFilesDir());
                   FileOutputStream out = new FileOutputStream(tempFile);
                   IOUtils.copy(in, out); 
                   logger.addSplit("Copy entry");
                	logger.dumpToLog();
                	
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stu
		this.dictionary.Dispose();
		super.onDestroy();
	}
	public void GetSuggestion(String query)
	{
		query=query.trim().toLowerCase();
		String[] suggestions=this.dictionary.GetSuggestion(query);
		String html="";
		if(suggestions!=null)
		{
			int end=suggestions.length;
			for(int i=0;i<end;i++)
			{
			   html+="<a href=\"entry:\\\\"+suggestions[i]+"\">"+suggestions[i]+"</a><br/>";
		   }
		}
		else
		{
			html="No suggestion found for \""+query+"\"";
		}
		   wb.loadData(html, "text/html", "utf-8");
	}
	public void LookUp(String query)
	{
		   query=query.trim().toLowerCase();
		   this.lastQuery=query;
		   String result=this.dictionary.LookUp(query);
		   if(result!=null)
		   {
			try{
				   wb.loadDataWithBaseURL("file:///android_asset/",html+result, "text/html", "utf-8", null);
			   this.leftText.setText(this.getString(R.string.app_name));
			  this.rightText.setText(query);
			
			   //Toast.makeText(this,wb.toString(), 3);
			}
			catch(Exception ex)
			{
				Log.e("ERROR",ex.toString());
			}
			   //this.setTitle(this.getString(R.string.app_name)+" : "+query);			   
		   }
		   else
		   {
			   this.GetSuggestion(query);
		   }
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
