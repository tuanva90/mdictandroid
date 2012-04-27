package tma.sdc;

import java.io.File;
import java.util.zip.ZipFile;
import android.media.MediaPlayer;
public class ZipPronounceEngine extends PronounceEngine {
	private MediaPlayer mp;
	private ZipFile zip;
	public ZipPronounceEngine(String zipFilePath) throws Exception {
		super("Zip File Pronoune Engine");
		File f=new File(zipFilePath);
		if(f.exists())
		{
			  zip = new ZipFile(zipFilePath);
		}
		else throw new Exception("Cannot create ZipPronounceEngine because the zip file does not exist.");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void Pronounce(String text) {
		// TODO Auto-generated method stub
		//mp
	}

}
