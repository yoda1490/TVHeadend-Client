package org.tvheadend.application;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.tvheadend.tvhclient.htsp.HTSListener;
import org.tvheadend.tvhclient.model.Intent;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.media.Media;
import uk.co.caprica.vlcj.player.media.callback.seekable.RandomAccessFileMedia;

public class VideoPlayer implements HTSListener {
	
	

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private String streamPath;
    

    public VideoPlayer(String video) {
    	this.streamPath=video;
    	
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        
        TVHClientApplication app = TVHClientApplication.getInstance();
    	app.addListener(this);
    	
    }
    
    
    
    
    public EmbeddedMediaPlayerComponent getMediaPlayer(){
    	return mediaPlayerComponent;
    }
    
    public void setStream(String path){
    	this.streamPath=path;
    }
    
    
    public void play(){
    	mediaPlayerComponent.getMediaPlayer().play();
    }
    
    public void plause(){
    	mediaPlayerComponent.getMediaPlayer().pause();
    }
    
    public void stop(){
    	mediaPlayerComponent.getMediaPlayer().stop();
    }
    
    public void start(){
    	if(streamPath.length() != 0){
        	//mediaPlayerComponent.getMediaPlayer().setPlaySubItems(true);
        	mediaPlayerComponent.getMediaPlayer().playMedia(streamPath);
        }
    }
    
    
    public static void main(final String[] args) {
    	 new NativeDiscovery().discover();
		   JFrame frame = new JFrame("TVHeadend Client");
		        
		   final VideoPlayer player = new VideoPlayer("http://192.168.1.25:9981/stream/channel/2b2c9903fc8dd072711696fd7536020b");
		   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        
		   frame.setContentPane(player.getMediaPlayer());
		   frame.setVisible(true);
		   player.start();
    }




	@Override
	public void onMessage(String action, Object obj) {
		if(action.equals(Intent.PLAY_VIDEO)){
			if(!this.getMediaPlayer().getMediaPlayer().isPlaying()){
				this.setStream(obj.toString());
				this.start();
			}
		}else if(action.equals(Intent.PLAY_HTTP)){
			//System.out.println("Play: "+obj.toString());
			
			Intent intent = Intent.getInstance();
			String httpStreamUrl = "http://"+intent.getStringExtra("hostname")+":"+intent.getStringExtra("httpPort")+"/"+obj.toString();
			this.setStream(httpStreamUrl);
			this.start();
		}
		
	}




	

}


