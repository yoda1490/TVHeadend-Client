/*
 *  Copyright (C) 2011 John Törnblom
 *
 * This file is part of TVHGuide.
 *
 * TVHGuide is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TVHGuide is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TVHGuide.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.tvheadend.application;


import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;








import javax.swing.JPanel;


//import org.tvheadend.tvhclient.R;
import org.tvheadend.tvhclient.htsp.HTSListener;
import org.tvheadend.tvhclient.model.Channel;
import org.tvheadend.tvhclient.model.ChannelTag;
import org.tvheadend.tvhclient.model.HttpTicket;
import org.tvheadend.tvhclient.model.Intent;
import org.tvheadend.tvhclient.model.Packet;
import org.tvheadend.tvhclient.model.Programme;
import org.tvheadend.tvhclient.model.Recording;
import org.tvheadend.tvhclient.model.Subscription;

/**
 *
 * @author john-tornblom
 */
public class TVHClientApplication  {

    
    private final List<HTSListener> listeners = new ArrayList<HTSListener>();
    private final List<ChannelTag> tags = Collections.synchronizedList(new ArrayList<ChannelTag>());
    private final List<Channel> channels = Collections.synchronizedList(new ArrayList<Channel>());
    private final List<Recording> recordings = Collections.synchronizedList(new ArrayList<Recording>());
    private final List<Subscription> subscriptions = Collections.synchronizedList(new ArrayList<Subscription>());
    private volatile boolean loading = false;
    private VideoPlayer videoPlayer;
    
    private Frame frame;
    private ArrayList<Component> componentToHideInFS = new ArrayList<Component>();
    
    private TVHClientApplication(){
    	
    }
    
    
    
    public Frame getFrame(){
    	return frame;
    }
    
    public void setFrame(Frame frame){
    	this.frame = frame;
    }
    
    
    public ArrayList<Component> getComponentToHideInFS(){
    	return componentToHideInFS;
    }
    
    public void addComponentToHideInFS(Component componentToHideInFS){
    	this.componentToHideInFS.add(componentToHideInFS);
    }
    
    public void removeComponentToHideInFS(Component componentToHideInFS){
    	this.componentToHideInFS.remove(componentToHideInFS);
    }
    
    public void setVideoPlayer(VideoPlayer vp){
    	this.videoPlayer = vp;
    }
    
    public VideoPlayer getVideoPlayer(){
    	return this.videoPlayer;
    }
    
    private static TVHClientApplication tvhClientApplication = new TVHClientApplication();
    
    
    public static TVHClientApplication getInstance()
	{	return tvhClientApplication;
	}
    
    public void addListener(HTSListener l) {
    	listeners.add(l);
    }

    public void removeListener(HTSListener l) {
        listeners.remove(l);
    }

    private void broadcastMessage(String action, Object obj) {
    	synchronized (listeners) {
    		for (HTSListener l : listeners) {
    			l.onMessage(action, obj);
            }
        }
    }

    public void broadcastError(final String error) {
        //Don't show error if no views are open
        synchronized (listeners) {
            if (listeners.isEmpty()) {
                return;
            }
        }
        
        broadcastMessage(Intent.ACTION_ERROR, error);
    }

    public void broadcastPacket(Packet p) {
        broadcastMessage(Intent.ACTION_PLAYBACK_PACKET, p);
    }

    public List<ChannelTag> getChannelTags() {
        return tags;
    }

    public void addChannelTag(ChannelTag tag) {
        tags.add(tag);

        if (!loading) {
            broadcastMessage(Intent.ACTION_TAG_ADD, tag);
        }
    }

    public void removeChannelTag(ChannelTag tag) {
        tags.remove(tag);

        if (!loading) {
            broadcastMessage(Intent.ACTION_TAG_DELETE, tag);
        }
    }

    public void removeChannelTag(long id) {
        for (ChannelTag tag : getChannelTags()) {
            if (tag.id == id) {
                removeChannelTag(tag);
                return;
            }
        }
    }

    public ChannelTag getChannelTag(long id) {
        for (ChannelTag tag : getChannelTags()) {
            if (tag.id == id) {
                return tag;
            }
        }
        return null;
    }

    public void updateChannelTag(ChannelTag tag) {
        if (!loading) {
            broadcastMessage(Intent.ACTION_TAG_UPDATE, tag);
        }
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
        if (!loading) {
        	broadcastMessage(Intent.ACTION_CHANNEL_ADD, channel);
        }
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);

        if (!loading) {
            broadcastMessage(Intent.ACTION_CHANNEL_DELETE, channel);
        }
    }

    public Channel getChannel(long id) {
        for (Channel ch : getChannels()) {
            if (ch.id == id) {
                return ch;
            }
        }
        return null;
    }

    public void removeChannel(long id) {
        for (Channel ch : getChannels()) {
            if (ch.id == id) {
                removeChannel(ch);
                return;
            }
        }
    }

    public void updateChannel(Channel ch) {
        if (!loading) {
            broadcastMessage(Intent.ACTION_CHANNEL_UPDATE, ch);
        }
    }

    public void addProgramme(Programme p) {
        if (!loading) {
            broadcastMessage(Intent.ACTION_PROGRAMME_ADD, p);
        }
    }

    public void removeProgramme(Programme p) {
        if (!loading) {
            broadcastMessage(Intent.ACTION_PROGRAMME_DELETE, p);
        }
    }

    public void updateProgramme(Programme p) {
        if (!loading) {
            broadcastMessage(Intent.ACTION_PROGRAMME_UPDATE, p);
        }
    }

    public void addRecording(Recording rec) {
        recordings.add(rec);

        if (!loading) {
            broadcastMessage(Intent.ACTION_DVR_ADD, rec);
        }
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void removeRecording(Recording rec) {
        recordings.remove(rec);

        if (!loading) {
            broadcastMessage(Intent.ACTION_DVR_DELETE, rec);
        }
    }

    public Recording getRecording(long id) {
        for (Recording rec : getRecordings()) {
            if (rec.id == id) {
                return rec;
            }
        }
        return null;
    }

    public void removeRecording(long id) {
        for (Recording rec : getRecordings()) {
            if (rec.id == id) {
                removeRecording(rec);
                return;
            }
        }
    }

    public void updateRecording(Recording rec) {
        if (!loading) {
            broadcastMessage(Intent.ACTION_DVR_UPDATE, rec);
        }
    }

    public void setLoading(boolean b) {
        if (loading != b) {
            broadcastMessage(Intent.ACTION_LOADING, b);
        }
        loading = b;
    }

    public void clearAll() {
        tags.clear();
        recordings.clear();

        for (Channel ch : channels) {
            ch.epg.clear();
            ch.recordings.clear();
        }
        channels.clear();

        for (Subscription s : subscriptions) {
            s.streams.clear();
        }
        subscriptions.clear();

        ChannelTag tag = new ChannelTag();
        tag.id = 0;
        //tag.name = getString(R.string.pr_all_channels);
        tags.add(tag);
    }

    public void addSubscription(Subscription s) {
        subscriptions.add(s);

        if (!loading) {
            broadcastMessage(Intent.ACTION_SUBSCRIPTION_ADD, s);
        }
    }
    
    public void playHTTP(String s) {
    	if (!loading) {
            broadcastMessage(Intent.PLAY_HTTP, s);
        }
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void removeSubscription(Subscription s) {
        s.streams.clear();
        subscriptions.remove(s);

        if (!loading) {
            broadcastMessage(Intent.ACTION_SUBSCRIPTION_DELETE, s);
        }
    }

    public Subscription getSubscription(long id) {
        for (Subscription s : getSubscriptions()) {
            if (s.id == id) {
                return s;
            }
        }
        return null;
    }

    public void removeSubscription(long id) {
        for (Subscription s : getSubscriptions()) {
            if (s.id == id) {
                removeSubscription(s);
                return;
            }
        }
    }

    public void updateSubscription(Subscription s) {
        if (!loading) {
            broadcastMessage(Intent.ACTION_SUBSCRIPTION_UPDATE, s);
        }
    }
    
   
    public void addTicket(HttpTicket t) {
        broadcastMessage(Intent.ACTION_TICKET_ADD, t);
    }
    
    public boolean isLoading() {
        return loading;
    }
    
    
    public void videoStreamReady(String s){
    	if (!loading) {
    		broadcastMessage(Intent.PLAY_VIDEO, s);
    	}
    }
    
    public void loading(String s){
    	if (!loading) {
    		broadcastMessage(Intent.PLAY_LOADING, s);
    	}
    }
    
    public void videoHTTPReady(String s){
    	if (!loading) {
    		broadcastMessage(Intent.PLAY_HTTP, s);
    	}
    }
    
    
    public void videoRunning(String s){
    	if (!loading) {
    		broadcastMessage(Intent.VIDEO_RUNNING, s);
    	}
    }
    
    
    public void videoStop(String s){
    	if (!loading) {
    		broadcastMessage(Intent.STOP_PLAY, s);
    	}
    }
    
/*
	public static ArrayList<String> getContentTypes(Context ctx) {
		ArrayList<String> ret = new ArrayList<String>();
		
		String[] s = ctx.getResources().getStringArray(R.array.pr_content_type0);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x00 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type1);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x10 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type2);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x20 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type3);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x30 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type4);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x40 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type5);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x50 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type6);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x60 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type7);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x70 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type8);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x80 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type9);
        for(int i=0; i<s.length; i++) {
        	ret.append(0x90 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type10);
        for(int i=0; i<s.length; i++) {
        	ret.append(0xa0 + i, s[i]);
        }
        
        s = ctx.getResources().getStringArray(R.array.pr_content_type11);
        for(int i=0; i<s.length; i++) {
        	ret.append(0xb0 + i, s[i]);
        }
        
		return ret;
	}**/
}
