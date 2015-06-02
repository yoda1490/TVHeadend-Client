package org.tvheadend.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPVideo {
	private BufferedReader buffer ;
	private String url;
	
	private String title="";
	private int channelNumber=0;
	private String streamUrl="";
	
	//public
	public HTTPVideo(String url){
		this.url=url;
		URL input = null;
		try {
			input = new URL(url);

			buffer = new BufferedReader(new InputStreamReader(input.openStream()));
		
		 String inputLine;
		 
	        while ((inputLine = buffer.readLine()) != null){
	            System.out.println("M3U Playlist: "+inputLine);
	            if(inputLine.startsWith("#EXTINF")){
	            	String[] tab = inputLine.split(":");
	            	channelNumber = Integer.parseInt(tab[1].split(",")[1].replaceAll("\\s+",""));
	            	title = tab[2];
	            }else if(inputLine.startsWith("http")){
	            	streamUrl=inputLine;
	            }
	        }
	        
	        buffer.close();
	        
		}catch (MalformedURLException e) {
				System.err.println("Error: malformed URL");
				//e.printStackTrace();
	    } catch (IOException e) {
				System.err.println("Error IOException: "+e);
				//e.printStackTrace();
	    } 
		
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getStreamURL(){
		return streamUrl;
	}
	
	public int getChannelNumber(){
		return channelNumber;
	}
	
	
	public static void main(final String[] args) {
        HTTPVideo httpVideo = new HTTPVideo("http://192.168.1.25:9981/play/stream/channel/2b2c9903fc8dd072711696fd7536020b?title=6%20%3A%20M6");
        System.out.println("Chaine: "+httpVideo.getTitle()+"  Number: "+ httpVideo.getChannelNumber()+" URL:"+httpVideo.getStreamURL());
    }
}
