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

	public class Driver
	{
		
		
		public static void main(String[] args)
		{
			
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
		
		
		//rotating launcher 
		
		}
}