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
package org.tvheadend.tvhclient.model;

//import android.graphics.Bitmap;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author john-tornblom
 */
public class Channel implements Comparable<Channel> {

    public long id;
    public String name;
    public String icon;
    public int number;
    public Set<Programme> epg = Collections.synchronizedSortedSet(new TreeSet<Programme>());
    public Set<Recording> recordings = Collections.synchronizedSortedSet(new TreeSet<Recording>());
    public List<Integer> tags;
    public Image iconBitmap;
    public boolean isTransmitting;
    
    public Channel (long id, String name, String icon, int number, Set<Programme> epg, Set<Recording> recordings, List<Integer> tags, Image iconBitmap, boolean isTransmitting){
    	this.id = id;
    	this.name = name;
    	this.icon = icon;
    	this.number = number;
    	this.epg = epg;
    	this.recordings = recordings;
    	this.tags = tags;
    	this.iconBitmap = iconBitmap;
    	this.isTransmitting = isTransmitting;
    	
    	
    }
    
    public Channel() {
		
	}

	public int compareTo(Channel that) {
        return this.number - that.number;
    }

    public boolean hasTag(long id) {
        if (id == 0) {
            return true;
        }

        for (Integer i : tags) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    public boolean isRecording() {
        for (Recording rec : recordings) {
            if ("recording".equals(rec.state)) {
                return true;
            }
        }
        return false;
    }
    
    public Channel clone(){
		return new Channel(id, name, icon, number, epg, recordings, tags, iconBitmap, isTransmitting);
    	
    }
    
    public String toString(){
    	return this.name;
    }
    
}
