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

public class Main {
	
	public static void main (String args[]) {
		
		//ArrayList<String> files = IN_OUT_FILE.read_file(new File(args[0]));
		
		  String files[] = new String[3];
		  //CBN
		  //files[0] = "http://cbn.globoradio.globo.com/media/audio/106455/biotoscana-estreia-na-bolsa.htm";
		  //files[1] = "http://cbn.globoradio.globo.com/media/audio/106539/armacao-com-tres-voltantes-tornou-o-palmeiras-um-t.htm";
		  //files[2] = "http://cbn.globoradio.globo.com/media/audio/106541/oposicao-realiza-paralisacao-contra-o-governo-de-n.htm";
		  
		  //JovemPan
		  files[0] = "http://jovempan.uol.com.br/podcasts/programas-podcasts/3-em-1-programas-podcasts";
		
		  //BandNews
		  files[0] = "http://www.bandnewsfm.com.br/2017/07/25/alckmin-sinaliza-que-psdb-e-dem-podem-se-aliar-para-disputa-presidencia-em-2018/";
		  
		  //EBC Radio Agência Nacional
		  files[0] = "http://radioagencianacional.ebc.com.br/geral/audio/2017-07/nacional-informa-lima-barreto-e-o-autor-homenageado-da-flip";
		  files[1] = "http://radioagencianacional.ebc.com.br/geral/audio/2017-07/ministerio-publico-investiga-mortes-em-hospital-de-roraima";
		  files[2] = "http://radioagencianacional.ebc.com.br/geral/audio/2017-07/nacional-informa-publicada-medida-provisoria-que-cria-agencia-nacional-de";
		
		for (String file: files) {
			if (file.contains("cbn")) {
				Radio.CBN_audio(file);		
			}
			if (file.contains("jovempan")) {
				Radio.JovemPan_audio(file);		
			}
			if (file.contains("bandnewsfm")) {
				Radio.BandNews_audio(file);		
			}
			if (file.contains("radioagencianacional")) {
				Radio.AgênciNacional_audio(file);		
			}
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
				   
				while (br.ready()) {
					output = output.concat(br.readLine() + "\r\n");   
				}
			    		   
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
		
		try {
			URLConnection conn = new URL(link).openConnection();
			InputStream is = conn.getInputStream();
		
			OutputStream outstream = new FileOutputStream(new File(filename));
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


