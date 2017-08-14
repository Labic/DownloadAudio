package AudioDownload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
	
	static String DEFAULT_PATH_SAVE = "./";

	public static void main (String args[]) {
		// args[0] = Lista de Links a serem baixados
		// arg[1]  = endereço de onde será salvo


		if (args.length==0)
			return;

		if (args.length>1)
			DEFAULT_PATH_SAVE = args[1];

		

		ArrayList<String> links = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {

			
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				links.add(sCurrentLine);
				System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}		

		for (String link : links) {
			if (link .contains("cbn")) 
				Radio.CBN_audio(link );		
			
			if (link .contains("jovempan")) 
				Radio.JovemPan_audio(link );		
			
			if (link .contains("bandnewsfm")) 
				Radio.BandNews_audio(link );		
			
			if (link .contains("radioagencianacional")) 
				Radio.AgênciNacional_audio(link );
		}	
	}
	
	
	public static String Capture(String link) {
		String output = "";
		URL url;
		try {
			url = new URL(link);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();

			if(con!=null){

				//System.out.println("****** Content of the URL ********");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				while (br.ready()) 
					output = output.concat(br.readLine() + "\r\n");   
				
				br.close();
			} 
			//con.disconnect();
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}

	
	public static void MP3Download(String link, String filename) {
		String filepath = DEFAULT_PATH_SAVE + "/" + filename; 

		try {
			URLConnection conn = new URL(link).openConnection();
			InputStream is = conn.getInputStream();

			OutputStream outstream = new FileOutputStream(new File(filepath));
			byte[] buffer = new byte[4096];
			int len;
			while ((len = is.read(buffer)) > 0) {
				outstream.write(buffer, 0, len);
			}
			outstream.close();
			is.close();
		} 
		
		catch (MalformedURLException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
	}
	
}


