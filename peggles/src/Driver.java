import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.awt.geom.AffineTransform;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Driver extends JPanel implements MouseMotionListener{
	// JFrame for menu
    static JFrame menu;
    
    // JButton
    static JButton b, b1, b2;
 
    // label to display text
    static JLabel l;
	
    
	public static void main(String[] args)
	{
		//music
		Clip clip1 = null;
		try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("morning.wav").getAbsoluteFile());
	        clip1 = AudioSystem.getClip();
	        clip1.open(audioInputStream);
	        
	        //clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
		clip1.start();
		
		BooleanControl muteControl = (BooleanControl) clip1.getControl(BooleanControl.Type.MUTE);
   		if(muteControl != null){
   		    muteControl.setValue(false); // True to mute the line, false to unmute
   		}

   		clip1.loop(0); // Stops the sound after it runs through its buffer
   		clip1.flush();
   		
	
	//main menu
	// create a new frame to store text field and button
   menu = new JFrame("Peggle");
   menu.setLocationRelativeTo(null);
   
  JFrame frame = new JFrame();
  frame.setSize(1010,1080);
  
   
   // create a label to display text
   l = new JLabel("PEGGLE");	

   // create a new buttons
   b = new JButton("Play");
   b1 = new JButton("Options");
   b2 = new JButton("Credits");

   // create a panel to add buttons
   
   
   
   
   JPanel p = new JPanel();
   Panel panel = new Panel();
   //Panel panel = new Panel();
   frame.add(panel);
   
   //	frame.getContentPane().add(new ImageFollowingMousePanel());
   // add buttons and textfield to panel
   p.add(b);
   p.add(b1);
   p.add(b2);
   p.add(l);
   
   
  // Background background = new background; 
 
   
   
   // setbackground of panel
   p.setBackground(Color.blue);
   panel.setBackground(Color.black);
   // add panel to frame
   menu.add(p);
   
   // set the size of frame
   
   menu.setSize(600, 400);

   menu.show();
 	
   
   b.addActionListener(new ActionListener()
	{
	  public void actionPerformed(ActionEvent e)
	  {
	    // display/center the jdialog when the button is pressed
		  menu.setVisible(false);
		  frame.setVisible(true);
		  //ImageFollowingMouseTest.createAndShowGUI();
		  
		//music
		muteControl.setValue(true);
		Clip clip2 = null;
		try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("prussian.wav").getAbsoluteFile());
	        clip2 = AudioSystem.getClip();
	        clip2.open(audioInputStream);
	        clip2.start();
	        clip2.loop(10);
	        
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	    }
		
		
		
	  }
	});
   
   
   
   b1.addActionListener(new ActionListener()
	{
	  public void actionPerformed(ActionEvent e)
	  {
	    menu.setVisible(false);
	    JFrame options = new JFrame();
	    options.setSize(400,400);
		options.setVisible(true);
		
	  }
	});
   
   b2.addActionListener(new ActionListener()
	{
	  public void actionPerformed(ActionEvent e)
	  {
	    menu.setVisible(false);
	    JFrame credits = new JFrame();
	    credits.setSize(400,400);
	    credits.setVisible(true);
		
	  }
	});
   
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	 		
}