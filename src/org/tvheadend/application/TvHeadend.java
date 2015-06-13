package org.tvheadend.application;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.tvheadend.tvhclient.htsp.HTSService;
import org.tvheadend.tvhclient.model.Intent;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class TvHeadend {
	
	 
	private JFrame frame = null;
	private VideoPlayer player;
	private HTTPVideo httpVideo;
	private String logo = "res/logo.png";
	
	
	//public
	public TvHeadend(){
		
		
		
        
		
		
		ChannelListJTable channelListJTable = new ChannelListJTable();
		
		
		HTSService.getInstance().onStartCommand(Intent.getInstance(), 0, 0);
		
		
		//initialize video receiver
		VideoReceiver recorder = VideoReceiver.getInstance();
		
		
		//Scanner scanner = new Scanner(System.in);
		//System.out.print("Enter action: ");
		//String username = scanner.next();
		
		//System.out.println("Channel: "+service.getApplication().getChannels().toString());
		//System.out.println("Channel Tag: "+service.getApplication().getChannelTags().toString());
		//System.out.println("Channel Tag: "+service.getApplication().getChannels().get(0).name);
		
		
		//httpVideo = new HTTPVideo("http://192.168.1.25:9981/play/stream/channel/2b2c9903fc8dd072711696fd7536020b?title=6%20%3A%20M6");
		
		
		frame = new JFrame("TVHeadend Client");
		try {
			BufferedImage icon = ImageIO.read(new File(logo));
			frame.setIconImage(icon);
			//Application.getApplication().setDockIconImage(icon);
			
		} catch (IOException e1) {
			System.err.println("Logo: "+logo+" not found");
		}
		
		
		try{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TVH client");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			System.err.println("Error: "+e);
		}
		frame.setBounds(100, 100, 708, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                	player.getMediaPlayer().release();
                }catch(Exception ex){
                	System.err.println("Error: "+ex);
                }
                System.exit(0);
            }
        });
        
        frame.setLayout(new BorderLayout());
        new NativeDiscovery().discover();
        
        TVHClientApplication.getInstance().setFrame(frame);
        
        player = new VideoPlayer(Intent.STREAM_FILE);
        
        JScrollPane jScrollPane = new JScrollPane(channelListJTable);
        jScrollPane.setPreferredSize(new Dimension(120, frame.getHeight()));
        
        frame.add(player.getMediaPlayer(), BorderLayout.CENTER);
        frame.add(jScrollPane, BorderLayout.WEST);
        
        
        JPanel statusBar = new StatusBar();
        frame.add(statusBar, BorderLayout.SOUTH);
        
        
        frame.setVisible(true);
        //player.start();
        
        
		
	}
	
	
	
	public static void main(final String[] args) {
		
        new TvHeadend();
    }

}
