package AudioDownload;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Main {
	
	static String DEFAULT_PATH_SAVE = "./";
	static String DEFAULT_FILENAME = "Downloadeds.txt";
	static String LinksBaixados = "";

	public static void main (String args[]) {
		// args[0] = Endereço do Arquivo da Lista de Links a serem baixados, exemplo: links_radio.txt
		// args[1] = Endereço de onde será salvo, exemplo: /var/www/radios/2017-08-10/


		if (args.length==0)
			return ;

		if (args.length>1)
			DEFAULT_PATH_SAVE = args[1];

		

		ArrayList<String> links = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {

			
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {				
				links.add(sCurrentLine);
				// System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return ;
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

		saveDownloadeds(LinksBaixados);

		
	}

	public static void saveDownloadeds(String content){
		try{
			BufferedWriter  writer = new BufferedWriter( new FileWriter( DEFAULT_FILENAME));
			writer.write(content);
			writer.close();
		}catch (IOException e) {
			e.printStackTrace();
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

		LinksBaixados += filename.split(".mp3")[0] + "," + link+ "," + filepath +"\n";
		System.out.println(link);		

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


