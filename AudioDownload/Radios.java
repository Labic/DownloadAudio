package AudioDownload;

class Radio {
	
	public static void CBN_audio(String link) {
		
		String content = Main.Capture(link);
		
		int index  = content.indexOf("data-path_audio=");
		
		if (index != -1) {
			content = content.substring(index+17,index+70);
			int index2 = content.indexOf("\"");
			content = content.substring(0,index2);
			System.out.println("CBN: " + content);
			
			String audioFile = "http://download.sgr.globo.com/audios/encodeds/" + content + ".mp3";
			content = content.replaceAll("/","_");
			Main.MP3Download(audioFile,"CBN_" + content + ".mp3");
		}
	}
	
	
	public static void JovemPan_audio(String link) {
		
		String title = "";
		String content = Main.Capture(link);
		
		int index  = content.indexOf("<div class=\"select select-submit\">");
		
		if (index != -1) {
			content = content.substring(index);
			String partes[] = content.split("\n");
			
			for (String chunk: partes) {
				int index_url = chunk.indexOf("http://storage.mais.uol.com.br");
				int index_url_inc = chunk.indexOf("http://storage.mais.uol.com.br/.mp3");
				int index_onclick = chunk.indexOf("onclick=");
				if ((index_url != -1) && (index_onclick != -1) && (index_url_inc == -1)) {
					int index_title = chunk.indexOf("title=");
					if (index_title != -1) { 
						title = chunk.substring(index_title+7,index_onclick-2);
						String linkAudio = chunk.substring(index_url,index_title-2);
						System.out.println(title + " - " + linkAudio);
						Main.MP3Download(linkAudio,"JovemPan_" + linkAudio.substring(linkAudio.lastIndexOf("/")+1));
					}
				}	
			}
		}
	}
	
	
	public static void BandNews_audio(String link) {
		
		String content = Main.Capture(link);
		
		int index  = content.indexOf("uol.com.br/static/uolplayer/?mediaId=");
		
		if (index != -1) {
			content = content.substring(index+37,index+50);
			int index2 = content.indexOf("\"");
			content = content.substring(0,index2);
			System.out.println("BandNews: " + content);
			
			String audioFile = "http://storage.mais.uol.com.br/" + content + ".mp3";
			Main.MP3Download(audioFile,"BandNews_" + content + ".mp3");
		}
	}
	
	
	public static void AgênciNacional_audio(String link) {
		
		String content = Main.Capture(link);
		
		int indexMP3 = content.indexOf("data-mp3=");
		int indexTitle = content.indexOf("title-audio");
		
		if ((indexMP3 != -1) && (indexTitle != -1)) {
			content = content.substring(indexMP3+10,indexTitle);
			int index = content.indexOf("\"");
			content = content.substring(0,index);
			System.out.println("AgênciNacional: " + content);
		
			Main.MP3Download(content,"AgênciNacional_" + content.substring(content.lastIndexOf("/")+1));
		}
	}
	
}