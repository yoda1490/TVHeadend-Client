package org.tvheadend.tvhclient.model;

import java.util.Hashtable;

import org.tvheadend.application.TVHClientApplication;

public class Intent {
	
	
	Hashtable<String,String> config;
	Channel channel;
	String action;
	
	public static final String ACTION_CHANNEL_ADD = "org.me.tvhclient.CHANNEL_ADD";
    public static final String ACTION_CHANNEL_DELETE = "org.me.tvhclient.CHANNEL_DELETE";
    public static final String ACTION_CHANNEL_UPDATE = "org.me.tvhclient.CHANNEL_UPDATE";
    public static final String ACTION_TAG_ADD = "org.me.tvhclient.TAG_ADD";
    public static final String ACTION_TAG_DELETE = "org.me.tvhclient.TAG_DELETE";
    public static final String ACTION_TAG_UPDATE = "org.me.tvhclient.TAG_UPDATE";
    public static final String ACTION_DVR_ADD = "org.me.tvhclient.DVR_ADD";
    public static final String ACTION_DVR_DELETE = "org.me.tvhclient.DVR_DELETE";
    public static final String ACTION_DVR_UPDATE = "org.me.tvhclient.DVR_UPDATE";
    public static final String ACTION_PROGRAMME_ADD = "org.me.tvhclient.PROGRAMME_ADD";
    public static final String ACTION_PROGRAMME_DELETE = "org.me.tvhclient.PROGRAMME_DELETE";
    public static final String ACTION_PROGRAMME_UPDATE = "org.me.tvhclient.PROGRAMME_UPDATE";
    public static final String ACTION_SUBSCRIPTION_ADD = "org.me.tvhclient.SUBSCRIPTION_ADD";
    public static final String ACTION_SUBSCRIPTION_DELETE = "org.me.tvhclient.SUBSCRIPTION_DELETE";
    public static final String ACTION_SUBSCRIPTION_UPDATE = "org.me.tvhclient.SUBSCRIPTION_UPDATE";
    public static final String ACTION_SIGNAL_STATUS = "org.me.tvhclient.SIGNAL_STATUS";
    public static final String ACTION_PLAYBACK_PACKET = "org.me.tvhclient.PLAYBACK_PACKET";
    public static final String ACTION_LOADING = "org.me.tvhclient.LOADING";
    public static final String ACTION_TICKET_ADD = "org.me.tvhclient.TICKET";
    public static final String ACTION_ERROR = "org.me.tvhclient.ERROR";
    public static final String PLAY_VIDEO = "org.me.tvhclient.PLAY_VIDEO";
    public static final String PLAY_LOADING = "org.me.tvhclient.PLAY_LOADING";
    public static final String PLAY_HTTP = "org.me.tvhclient.PLAY_HTTP";
    public static final String STOP_PLAY = "org.me.tvhclient.STOP_PLAY";
    
    
    
	public static final String ACTION_CONNECT = "org.me.tvhclient.htsp.CONNECT";
    public static final String ACTION_DISCONNECT = "org.me.tvhclient.htsp.DISCONNECT";
    public static final String ACTION_EPG_QUERY = "org.me.tvhclient.htsp.EPG_QUERY";
    public static final String ACTION_GET_EVENT = "org.me.tvhclient.htsp.GET_EVENT";
    public static final String ACTION_GET_EVENTS = "org.me.tvhclient.htsp.GET_EVENTS";
    public static final String ACTION_DVR_CANCEL = "org.me.tvhclient.htsp.DVR_CANCEL";
    public static final String ACTION_SUBSCRIBE = "org.me.tvhclient.htsp.SUBSCRIBE";
    public static final String ACTION_PLAY = "org.me.tvhclient.htsp.PLAY";
    public static final String ACTION_UNSUBSCRIBE = "org.me.tvhclient.htsp.UNSUBSCRIBE";
    public static final String ACTION_FEEDBACK = "org.me.tvhclient.htsp.FEEDBACK";
    public static final String ACTION_GET_TICKET = "org.me.tvhclient.htsp.GET_TICKET";
    public static final String VIDEO_RUNNING = "org.me.tvhclient.htsp.VIDEO_RUNNING";
    
    public static final String CACHE_DIR="./cache/";
    public static final String STREAM_FILE="./stream/output";
    

    public static final String TAG = "HTSService";
    
    public static final int START_NOT_STICKY=2;
	
    private static Intent intent = new Intent();
    
    public static Intent getInstance(){
    	return intent;
    }
    
    
    
	private Intent(){
		 action = ACTION_CONNECT;
		 config = new Hashtable<String,String>();
		 config.put("force","false");
		 config.put("port","9982");
		 config.put("httpPort","9981");
		 config.put("username","a");
		 config.put("password","a");
		 config.put("hostname","192.168.1.25");
		 config.put("eventId", "0");
		 config.put("id", "0");
		 config.put("channelId", "0");
		 config.put("videoCodec", Stream.STREAM_TYPE_MPEG4VIDEO);
		 config.put("audioCodec", Stream.STREAM_TYPE_AAC);
		 
		 config.put("playerMethod", "htsp");
		
	}
	
	public boolean getBooleanExtra(String key, boolean defaultValue){
		
		if(config.containsKey(key)){
			if(config.get(key).equals("true")){
				return true;
			}else{
				return false;
			}
		}else{
			return defaultValue;
		}
		
	}
    
    public int getIntExtra(String key, int defaultValue){
    	if(config.containsKey(key)){
    		return Integer.parseInt(config.get(key));
    	}else{
			return defaultValue;
		}
    }
    
    public String getStringExtra(String key){
    	if(config.containsKey(key)){
    		return config.get(key);
    	}else{
			return "NOVALUE";
		}
    	
    }
    
    public long getLongExtra(String key, long defaultValue){
    	if(config.containsKey(key)){
    		return Long.parseLong(config.get(key));
    	}else{
			return defaultValue;
		}
    	
    }
    
    public void setLongExtra(String key, long value){
    	config.put(key, new String(""+value));
    	
    }
    
    public void setStringExtra(String key, String value){
    	config.put(key, value);
    	
    }
    
    

    
    public String getAction(){
    	return this.action;
    }
    
    
    
    public void setAction(String action){
    	this.action = action;
    }
    
    
    public void setChannel(Channel ch){
    	this.channel = ch;
    }
    
    public Channel getChannel(){
    	return channel;
    }
    
    
   
}
