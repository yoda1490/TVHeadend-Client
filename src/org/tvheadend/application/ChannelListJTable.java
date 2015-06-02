package org.tvheadend.application;
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



import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.tvheadend.tvhclient.htsp.HTSListener;
import org.tvheadend.tvhclient.model.Channel;
import org.tvheadend.tvhclient.model.ChannelTag;
import org.tvheadend.tvhclient.model.Intent;

/**
 *
 * @author john-tornblom
 */
public class ChannelListJTable extends JTable implements HTSListener {

    private static final long serialVersionUID = 1L;
	private ChannelListAdapter chAdapter;
    ArrayList<ChannelTag> tagAdapter;
    private JOptionPane tagDialog;
    private JLabel tagTextView;
    private BufferedImage tagImageView;
    private JButton tagBtn;
    private ChannelTag currentTag;
   

    
    public ChannelListJTable(){
    	super();
    	this.setModel(new ChannelListModel());
    	getColumn(getColumnName(0)).setCellRenderer(new ChanenelRenderer());
    	getColumn(getColumnName(0)).setCellEditor(new ChannelEditor(new JCheckBox()));
    	setRowHeight(60);
    	
    	//this.setSize(10,10);
    	chAdapter = new ChannelListAdapter(new ArrayList<Channel>());
    	TVHClientApplication app = TVHClientApplication.getInstance();
    	app.addListener(this);
    	
    }
    
    public ChannelListAdapter getChAdapter(){
    	return this.chAdapter;
    }
    
    private ChannelListJTable getJTable(){
    	return this;
    }
    

    public void onMessage(String action, final Object obj) {
    	synchronized(obj){
    		
	    	Thread thread = null;
	        if (action.equals(Intent.ACTION_LOADING)) {
	
	        	thread = new Thread(){
	
	                public void run() {
	                    boolean loading = (Boolean) obj;
	                    //setLoading(loading);
	                }
	            };
	        } else if (action.equals(Intent.ACTION_CHANNEL_ADD)) {
	        	thread = new Thread(){
	
	                public void run() {
	                    chAdapter.add((Channel) obj);
	                    //chAdapter.notifyDataSetChanged();
	                    chAdapter.sort();
	                }
	            };
	        } else if (action.equals(Intent.ACTION_CHANNEL_DELETE)) {
	        	thread = new Thread(){
	
	                public void run() {
	                    chAdapter.remove((Channel) obj);
	                    //chAdapter.notifyDataSetChanged();
	                }
	            };
	        } else if (action.equals(Intent.ACTION_CHANNEL_UPDATE)) {
	        	thread = new Thread(){
	
	                public void run() {
	                    Channel channel = (Channel) obj;
	                    //System.out.println(channel.name+" adapt: "+chAdapter);
	                    chAdapter.updateView( channel);
	                }
	            };
	            
	        } else if (action.equals(Intent.ACTION_TAG_ADD)) {
	        	thread = new Thread(){
	
	                public void run() {
	                    ChannelTag tag = (ChannelTag) obj;
	                    tagAdapter.add(tag);
	                }
	            };
	        } else if (action.equals(Intent.ACTION_TAG_DELETE)) {
	        	thread = new Thread(){
	
	                public void run() {
	                    ChannelTag tag = (ChannelTag) obj;
	                    tagAdapter.remove(tag);
	                }
	            };
	        } else if (action.equals(Intent.ACTION_TAG_UPDATE)) {
	            //NOP
	        }
	        
	        if(thread != null){
	        	thread.start();
	        }
    	}
    }
    	

    class ChannelListAdapter extends ArrayList<Channel> {

        ChannelListAdapter(List<Channel> list) {
            super(list);
        }
        
        
        public void sort() {
        	synchronized(this){
        		sort();
        	}
    
        }

        

        public void updateView(Channel channel) {
        	
            this.add(channel);
            //System.out.println("Nb Ch: "+this.size());
            
            for (int i = 0; i < this.size(); i++) {
            	
            	
            	ChannelListModel model = (ChannelListModel) getJTable().getModel();
            	model.addChannel(this.get(i));
            	
            	
            	//System.out.println(this.get(i).name);
            	
            	getJTable().repaint();
            	//View view = listView.getChildAt(i);
                //int pos = listView.getPositionForView(view);
                //Channel ch = (Channel) .getItemAtPosition(pos);

                //if (view.getTag() == null || ch == null) {
                //    continue;
                //}

               // if (channel.id != ch.id) {
               //     continue;
               // }

                //ChannelWrapper wrapper = (ChannelWrapper) view.getTag();
                //wrapper.repaint(channel);
                //break;
            }
        }

        public JPanel getView(int position, JPanel convertView, JPanel parent) {
        	JPanel row = convertView;
            //ChannelWrapper wrapper;

            //Channel ch = getItem(position);
            //Channel ch = get(position);
            

            if (row == null) {
               // wrapper = new ChannelWrapper(row);
                

            } else {
                
            }

            //wrapper.repaint(ch);
            return row;
        }
    }
    
    public class ChanenelRenderer extends JButton implements TableCellRenderer{

    	

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocus, int row, int col) {
    	    try{
    	    	Image image = ((Channel)value).iconBitmap;
    	    	ImageIcon imageIcon = new ImageIcon(image);
    	    	setIcon(imageIcon);
    	    	//System.out.println(String.valueOf(((Channel)value).id));
    	    	
				//add((Component) ((value != null) ? new JLabel(imageIcon) : new JLabel(value.toString())));
    	    }catch(Exception e){
    	    	if(value != null){
    	    		setIcon(null);
    	    		setText( ( (Channel)value).name );
    	    	}else{
    	    		setIcon(null);
    	    		setText( "NULL");
    	    	}
    	    }
    	    return this;
    	  }
    	}
    
    
    class ChannelListModel extends AbstractTableModel{
    	private Object[][] data= {{"loading ..."}};
        private String[] title = {"  TV Channels"};

        public ChannelListModel(){
          super();
        }

        public int getColumnCount() {
          return this.title.length;
        }

        public int getRowCount() {
          return this.data.length;
        }

        public Object getValueAt(int row, int col) {
          return this.data[row][col];
        }         
        
        public String getColumnName(int col) {
        	  return this.title[col];
        }
        
        public Class getColumnClass(int col){
        	return this.data[0][col].getClass();
        }
        
        public boolean isCellEditable(int row, int col){
        	  return true; 
        }
        
        
        public void addChannel(Channel channel){
        	
        	
        	List<Channel> channels = TVHClientApplication.getInstance().getChannels();
        	
        	synchronized(channels){
        		
        		
        		
	        	data = new Channel[channels.size()][1];
	        	
	        	
	        	Collections.sort(channels, new Comparator<Channel>() {
	                @Override
	                public int compare(Channel  ch1, Channel  ch2)
	                {
	                    return  ch1.number - ch2.number;
	                }
	            });
	        	
	        	for(int i =0; i<channels.size(); i++) {
	        		data[i][0] = channels.get(i);
	        		
	        	}
	        	
	        	
        	}
        	
        	
        }
        
        
      }
}

