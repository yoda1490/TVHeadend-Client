package org.tvheadend.application;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import org.tvheadend.tvhclient.htsp.HTSService;
import org.tvheadend.tvhclient.model.Subscription;
import org.tvheadend.tvhclient.model.Intent;
import org.tvheadend.tvhclient.model.Channel;

public class ChannelEditor extends DefaultCellEditor {


private static final long serialVersionUID = 1L;
protected JButton button;
private boolean   isPushed;
private ButtonListener bListener = new ButtonListener();

public ChannelEditor(JCheckBox checkBox) {
  super(checkBox);
  button = new JButton();
  button.setOpaque(true);
  button.addActionListener(bListener);
}

public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) { 
  bListener.setRow(row);
  bListener.setColumn(column);
  bListener.setTable(table);

  Intent.getInstance().setLongExtra("channelId", ((Channel)value).id);
  button.setText( (value == null) ? "" : value.toString() );
  button.setIcon(new ImageIcon(((Channel)value).iconBitmap));
  return button;
}

class ButtonListener implements ActionListener{

  private int column, row;
  private JTable table;
  private int nbre = 0;
  private JButton button;

  public void setColumn(int col){this.column = col;}
  public void setRow(int row){this.row = row;}
  public void setTable(JTable table){this.table = table;}

  public JButton getButton(){return this.button;}

  public void actionPerformed(ActionEvent event) {
      //System.out.println("coucou du bouton : " + ((JButton)event.getSource()).getText());
	  ((JButton)event.getSource()).setSelected(true);
	  TVHClientApplication.getInstance().getFrame().setTitle("TVH client - "+((JButton)event.getSource()).getText());;
	  
      
	  TVHClientApplication app = TVHClientApplication.getInstance();
	  //app.addSubscription(new Subscription());
	  
	  Intent.getInstance().setAction(Intent.ACTION_GET_TICKET);
	  HTSService.getInstance().onStartCommand(Intent.getInstance(), 0, 0);
	  
    this.button = ((JButton)event.getSource());
  }
}
}
