package org.tvheadend.application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.tvheadend.tvhclient.htsp.HTSListener;
import org.tvheadend.tvhclient.htsp.HTSMessage;
import org.tvheadend.tvhclient.htsp.HTSService;
import org.tvheadend.tvhclient.model.Intent;
import org.tvheadend.tvhclient.model.Packet;


public class VideoReceiver implements HTSListener{
	
	private static VideoReceiver videoReceiver = new VideoReceiver();
	private FileOutputStream out;
	private Boolean isWritting = false;
	private int lastPacket = 0;
	
	
	private VideoReceiver(){
		TVHClientApplication app = TVHClientApplication.getInstance();
		app.addListener(this);
		
		try {
			out = new FileOutputStream(Intent.STREAM_FILE);
		} catch (FileNotFoundException e) {
			System.err.println("Unable to open stream file: "+e);
		}
	}
	
	public static VideoReceiver getInstance()
	{	
		return videoReceiver;
	}
	
	@Override
	public void onMessage(String action, Object obj) {
		if(action.equals(Intent.ACTION_TICKET_ADD)){
			if(Intent.getInstance().getStringExtra("playerMethod").equals("htsp")){
				  Intent.getInstance().setAction(Intent.ACTION_SUBSCRIBE);
				  HTSService.getInstance().onStartCommand(Intent.getInstance(), 0, 0);
			  }else{
				  System.out.println("HTTP Method");
				  Intent.getInstance().setAction(Intent.ACTION_PLAY);
				  HTSService.getInstance().onStartCommand(Intent.getInstance(), 0, 0);
			  }
			
		}else if(action.equals(Intent.ACTION_PLAYBACK_PACKET)){
			synchronized(isWritting){
				
				isWritting = true;
				Packet packet = (Packet)obj;
				int packetId = packet.stream.index;
				
				
				if(packet.stream.index > lastPacket){
					if(packet.payload != null){
						try{
							out.write(packet.payload);
							//lastPacket = packetId;
						}catch(Exception e){
							System.err.println("Error: "+e);
						}
						
						isWritting = false;
					}
					
				}
				
				TVHClientApplication.getInstance().videoStreamReady(Intent.STREAM_FILE);
			}
		}else if(action.equals(Intent.ACTION_PLAY)){
			//System.out.println("ACTION PLAY");
			TVHClientApplication.getInstance().videoHTTPReady((String) obj);
			
		}
		
	}

}
