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


import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.tvheadend.tvhclient.model.Channel;
import org.tvheadend.tvhclient.model.Programme;

/**
 *
 * @author john-tornblom
 */
public class ChannelListViewWrapper {

    private JLabel name;
    private JLabel nowTitle;
    private JLabel nowTime;
    private JLabel nextTitle;
    private JLabel nextTime;
    private BufferedImage icon;
    private BufferedImage nowProgressImage;
    private JProgressBar nowProgress;

    public ChannelListViewWrapper(JPanel base) {
        /**name = (JLabel) base.findViewById(R.id.ch_name);
        nowTitle = (JLabel) base.findViewById(R.id.ch_now_title);

        nowProgressImage = (BufferedImage) base.findViewById(R.id.ch_elapsedtime);
        **/
        nowProgress = new JProgressBar(0, 100);
        //nowProgressImage.setBackgroundDrawable(nowProgress);

        //nowTime = (JLabel) base.findViewById(R.id.ch_now_time);
        //nextTitle = (JLabel) base.findViewById(R.id.ch_next_title);
        //nextTime = (JLabel) base.findViewById(R.id.ch_next_time);
        //icon = (JLabel) base.findViewById(R.id.ch_icon);
    }

    public void repaint(Channel channel) {
        nowTime.setText("");
        nowTitle.setText("");
        nextTime.setText("");
        nextTitle.setText("");
        nowProgress.setValue(0);

        name.setText(channel.name);
        name.invalidate();

        
        if (channel.isRecording()) {
            //icon.setImageResource(R.drawable.ic_rec_small);
        } else {
        	//icon.setImageDrawable(null);
        }
        //icon.invalidate();

        Iterator<Programme> it = channel.epg.iterator();
        if (!channel.isTransmitting && it.hasNext()) {
            //nowTitle.setText(R.string.ch_no_transmission);
        } else if (it.hasNext()) {
            Programme p = it.next();
            /*nowTime.setText(
                    DateFormat.getTimeFormat(nowTime.getContext()).format(p.start)
                    + " - "
                    + DateFormat.getTimeFormat(nowTime.getContext()).format(p.stop));
**/
            double duration = (p.stop.getTime() - p.start.getTime());
            double elapsed = new Date().getTime() - p.start.getTime();
            double percent = elapsed / duration;

            //nowProgressImage.setVisibility(BufferedImage.VISIBLE);
            nowProgress.setValue((int) Math.floor(percent));
            nowTitle.setText(p.title);
        } else {
            //nowProgressImage.setVisibility(ImageView.GONE);
        }
        //nowProgressImage.invalidate();
        nowTime.invalidate();
        nowTitle.invalidate();

        if (it.hasNext()) {
            Programme p = it.next();
            /*
            nextTime.setText(
                    DateFormat.getTimeFormat(nextTime.getContext()).format(p.start)
                    + " - "
                    + DateFormat.getTimeFormat(nextTime.getContext()).format(p.stop));
**/
            nextTitle.setText(p.title);
        }
        nextTime.invalidate();
        nextTitle.invalidate();
    }
}
