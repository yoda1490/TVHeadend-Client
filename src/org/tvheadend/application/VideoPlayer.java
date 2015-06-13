package org.tvheadend.application;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.tvheadend.tvhclient.htsp.HTSListener;
import org.tvheadend.tvhclient.model.Intent;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class VideoPlayer implements HTSListener {
	
	

    private final MediaPlayer mediaPlayer;

    private String streamPath;
    

    public VideoPlayer(String video) {
    	this.streamPath=video;
    	
    	
    	
        
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
        
        TVHClientApplication app = TVHClientApplication.getInstance();
        app.setVideoPlayer(this);
    	app.addListener(this);
    	
    	try{
    		enableOSXFullscreen(app.getFrame());
    	}catch(Exception e){
    		System.out.println("Error: "+e);
    	}
    	
    	
    	
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
    
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void enableOSXFullscreen(Window window) {
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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


    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void requestToggleFullScreen(Window window, EmbeddedMediaPlayerComponent mpc)
    {
    	
    	for(Component c: TVHClientApplication.getInstance().getComponentToHideInFS()){
    		c.setVisible(!c.isVisible());
    	}
    	
    	
    	String osName = System.getProperty("os.name").toLowerCase();
    	boolean isMacOs = osName.startsWith("mac os x");
    	if (!isMacOs) 
    	{
    		try{
    			mpc.getMediaPlayer().toggleFullScreen();
    		}catch(Exception e){
    			System.err.println("Error: "+e);
    		}
    	  
    	}else{
    		
    		
    		
	        try {
	            Class appClass = Class.forName("com.apple.eawt.Application");
	            Class params[] = new Class[]{};
	 
	            Method getApplication = appClass.getMethod("getApplication", params);
	            Object application = getApplication.invoke(appClass);
	            Method requestToggleFulLScreen = application.getClass().getMethod("requestToggleFullScreen", Window.class);
	 
	            requestToggleFulLScreen.invoke(application, window);
	        } catch (Exception e) {
	            System.out.println("An exception occurred while trying to toggle full screen mode");
	        }
    	}
    }


	

}


