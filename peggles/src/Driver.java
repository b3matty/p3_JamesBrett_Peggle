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
  frame.setSize(1920,1080);
  
   
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
   
   // add panel to frame
   menu.add(p);
   
   // set the size of frame
   
   menu.setSize(600, 400);

   menu.show();
 	
   //Menu buttons
   b.addActionListener(new ActionListener()
{
  public void actionPerformed(ActionEvent e)
  {
    // display/center the jdialog when the button is pressed
	  menu.setVisible(false);
	  frame.setVisible(true);
	  ImageFollowingMouseTest.createAndShowGUI();
	  
	//music
	muteControl.setValue(true);
	Clip clip2 = null;
	try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("prussian.wav").getAbsoluteFile());
        clip2 = AudioSystem.getClip();
        clip2.open(audioInputStream);
        clip2.start();
        clip2.loop(5);
    } catch(Exception ex) {
        System.out.println("Error with playing sound.");
    }
	
	
	
  }
});
   
	// brett if u see this weve achieve sorcery 
	
	//nishu //bishu
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	
	
	
	
	
	
	}
	
	//
	private BufferedImage image;
    private Point imagePosition = new Point(150,150);
    private double imageAngleRad = 0;

    public void ImageFollowingMousePanel()
    {
        BufferedImage i = null;	
        try
        {
            i = ImageIO.read(new File("duckinballLAUNCHER.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        image = i;
        addMouseMotionListener(this);
    }
	
	protected void paintComponent(Graphics gr) 
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        g.setRenderingHint(
            RenderingHints.KEY_RENDERING, 
            RenderingHints.VALUE_RENDER_QUALITY);

        int cx = image.getWidth() / 2;
        int cy = image.getHeight() / 2;
        AffineTransform oldAT = g.getTransform();
        g.translate(cx+imagePosition.x, cy+imagePosition.y);
        g.rotate(imageAngleRad);
        g.translate(-cx, -cy);
        g.drawImage(image, 0, 0, null);
        g.setTransform(oldAT);

    }
//	public boolean isGameOver() {
//		if(balls < 0) {
//			frame.setVisible(false);
//		}
//	}
	//
	@Override
    public void mouseDragged(MouseEvent e)
    {
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        double dx = e.getX() - imagePosition.getX();
        double dy = e.getY() - imagePosition.getY();
        imageAngleRad = Math.atan2(dy, dx);
        repaint();
    }
	    
		
}