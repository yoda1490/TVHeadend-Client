package org.tvheadend.application;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.tvheadend.tvhclient.htsp.HTSListener;
import org.tvheadend.tvhclient.model.Intent;

<<<<<<< HEAD
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
=======
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.media.Media;
import uk.co.caprica.vlcj.player.media.callback.seekable.RandomAccessFileMedia;
>>>>>>> parent of 35cd31e... Added fullscreen mode ;)

public class VideoPlayer implements HTSListener {
	
	

    private final MediaPlayer mediaPlayer;

    private String streamPath;
    

    public VideoPlayer(String video) {
    	this.streamPath=video;
    	
<<<<<<< HEAD
    	
    	
        
        MediaPlayer mediaPlayer = new MediaPlayer(media){
            @Override
            public void mouseClicked(MouseEvent e) {
            	requestToggleFullScreen(TVHClientApplication.getInstance().getFrame(), this);
            }
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        };
=======
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
>>>>>>> parent of 35cd31e... Added fullscreen mode ;)
        
        TVHClientApplication app = TVHClientApplication.getInstance();
        app.setVideoPlayer(this);
    	app.addListener(this);
    	
<<<<<<< HEAD
    	try{
    		enableOSXFullscreen(app.getFrame());
    	}catch(Exception e){
    		System.out.println("Error: "+e);
    	}
    	
    	
    	
=======
>>>>>>> parent of 35cd31e... Added fullscreen mode ;)
    }
    
    
    
    
    public EmbeddedMediaPlayerComponent getMediaPlayer(){
    	return mediaPlayer;
    }
    
    public void setStream(String path){
    	this.streamPath=path;
    }
    
    
    public void play(){
    	mediaPlayer.play();
    }
    
    public void plause(){
    	mediaPlayer.pause();
    }
    
    public void stop(){
    	mediaPlayer.stop();
    }
    
    public void start(){
    	if(streamPath.length() != 0){
        	//mediaPlayerComponent.getMediaPlayer().setPlaySubItems(true);
    		//mediaPlayerComponent.getMediaPlayer().setAspectRatio("16:10");
    		mediaPlayer.playMedia(streamPath);
        	
    		
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
		if(action.equals(Intent.PLAY_LOADING)){
			System.out.println(obj.toString());
			String httpStreamUrl = "./res/loading.gif";
			this.setStream(httpStreamUrl);
			this.start();
		}else if(action.equals(Intent.PLAY_VIDEO)){
			if(!this.getMediaPlayer().isPlaying()){
				this.setStream(obj.toString());
				this.start();
				//Tell module that stream is running
				TVHClientApplication.getInstance().videoRunning("HTTP");
			}
		}else if(action.equals(Intent.PLAY_HTTP)){
			//System.out.println("Play: "+obj.toString());
			
			Intent intent = Intent.getInstance();
			String httpStreamUrl = "http://"+intent.getStringExtra("hostname")+":"+intent.getStringExtra("httpPort")+"/"+obj.toString();
			this.setStream(httpStreamUrl);
			this.start();
			//Tell module that stream is running
			TVHClientApplication.getInstance().videoRunning("HTTP");
		}else if(action.equals(Intent.STOP_PLAY)){
			this.stop();
			
		}
		
		
	}




	

}


