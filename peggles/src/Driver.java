import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Driver extends JPanel implements MouseMotionListener{
	
		
		
		public static void main(String[] args)
		{
			//
			SwingUtilities.invokeLater(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                createAndShowGUI();
	            }
	        });
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			//
			Clip clip = null;
			try {
		        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("prussian.wav").getAbsoluteFile());
		        clip = AudioSystem.getClip();
		        clip.open(audioInputStream);
		        //clip.start();
		    } catch(Exception ex) {
		        System.out.println("Error with playing sound.");
		        ex.printStackTrace();
		    }
			
		JFrame frame = new JFrame();
		
		
		frame.setSize(1920,1080);
		
		
		// brett if u see this weve achieve sorcery 
		
		//nishu //bishu
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Panel00 panel = new Panel00();
		
		
		frame.add(panel);
		
		frame.setVisible(true);
		clip.start();
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
		private static void createAndShowGUI()
	    {
	        JFrame f = new JFrame();
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.getContentPane().add(new ImageFollowingMousePanel());
	        f.setSize(400,400);
	        f.setLocationRelativeTo(null);
	        f.setVisible(true);
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