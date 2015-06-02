package org.tvheadend.application;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.tvheadend.tvhclient.htsp.HTSListener;
import org.tvheadend.tvhclient.model.Channel;
import org.tvheadend.tvhclient.model.Intent;
import org.tvheadend.tvhclient.model.Programme;

import uk.co.caprica.vlcj.player.MediaPlayer;

public class StatusBar extends JPanel implements HTSListener{
	
	
	private JProgressBar epg,volume,signal;

	public StatusBar(){
		this.setLayout(new FlowLayout());
		TVHClientApplication app = TVHClientApplication.getInstance();
    	app.addListener(this);
    	epg = new JProgressBar();
    	volume = new JProgressBar();
    	signal = new JProgressBar();
	}

	@Override
	public void onMessage(String action, Object obj) {
		 if (action.equals(Intent.VIDEO_RUNNING)) {
			 Channel ch = Intent.getInstance().getChannel();
			 this.removeAll();
			 TVHClientApplication app = TVHClientApplication.getInstance();
			 MediaPlayer vp = app.getVideoPlayer().getMediaPlayer().getMediaPlayer();
			 if(vp != null){
				display_epg(ch);
			 	display_subtitles(vp);
			 	display_languages(vp);
			 	display_volume(vp);
			 }
			 
	        	
	     }else if(action.equals(Intent.ACTION_SIGNAL_STATUS)){
	    	 display_signal(obj);
	     }
		
	}
	
	
	public void display_epg(Channel ch){
		for (Iterator<Programme> it = ch.epg.iterator(); it.hasNext(); ) {
	        Programme p = it.next();
	        //System.out.println(p.title);
	        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	        Date date = new Date();
	        if(p.stop.after(date)){
	        	long duration = (p.stop.getTime()-p.start.getTime())/1000;
	        	long watched = (p.stop.getTime()-date.getTime())/1000;
	        	System.out.println(duration+":"+watched);
	        	epg.setValue((int) (100-(watched*100/duration)));
	        	epg.setString(p.title);
	        	this.add(new JLabel(p.title+": "+dateFormat.format(p.start)));
	        	this.add(epg);
	        	this.add(new JLabel(dateFormat.format(p.stop)));
	        	this.repaint();
	        	break;
	        }
	    }
	}
	
	
	public void display_subtitles(MediaPlayer mp){
		this.add(new JLabel("Subtitle: "+mp.getTeletextPage()));
	}
	public void display_languages(MediaPlayer mp){
		this.add(new JLabel("Audio: "+mp.getAudioTrack()));
	}
	public void display_volume(MediaPlayer mp){
		volume.setValue(mp.getVolume());
		this.add(new JLabel("Volume: "));
		this.add(volume);
	}
	
	public void display_signal(Object obj){
		//this.add(new JLabel(obj.toString()));
	}
}
