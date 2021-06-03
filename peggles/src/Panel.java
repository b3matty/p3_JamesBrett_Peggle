import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Panel extends JPanel implements MouseListener, ActionListener, MouseMotionListener{
	Timer t = new Timer(1, new Listener());
	int counter = 0;
	double G = 0.1;
	
	double[] ball = {550, 500}; //ball starting x and y loc
	double[] spring = {0,0};
	
	final int xpos = 550;
	int points = 0;
	int balls = 10; //number of balls you have available
	int sides = 13;
	
	double snorm = 800;
	double sd = 550;
	double setVelocity = 0;
	double paddleLength = 180;
	double rtheta = 0;
	double ltheta = 0;
	
	boolean setlock = false;
	boolean rdown, ldown;
	
	int preset[][] = {
		{0, 900, 500, 750,1}, //left paddle
		{530, 1100, 530, 900,1}, //right paddle
		{550, 0, 600, 20, 1}, //diagonal barrier
		{530, 75, 530, 1100, 1}, //right barrier
		{-1, 0, 1000, 0, 1}, //top barrier
		{0, -1, 0, 1100, 1}, //left barrier
		
	};
	
	int[][] pegs = {
		{(int)(Math.random()*510)+10, (int)(Math.random()*620)+20, (int)(Math.random()*30)+35, 50},
		{(int)(Math.random()*510)+10, (int)(Math.random()*620)+20, (int)(Math.random()*30)+35, 50},
		{(int)(Math.random()*510)+10, (int)(Math.random()*620)+20, (int)(Math.random()*30)+35, 50},
		{(int)(Math.random()*510)+10, (int)(Math.random()*620)+20, (int)(Math.random()*30)+35, 50},
		{(int)(Math.random()*510)+10, (int)(Math.random()*620)+20, (int)(Math.random()*30)+35, 50},
		{(int)(Math.random()*510)+10, (int)(Math.random()*620)+20, (int)(Math.random()*30)+35, 50}
	};
	
	int lines[][] = new int[100][5];
	
	
	public Panel() {
		super();
		t.start();
		
		
		setFocusable(true);
		
		addKeyListener(new Key());
		
		for(int i = 0; i < preset.length; i++){
			lines[i] = preset[i];
		}
		int pre = preset.length;
		int ct = 0;
		
		for(int k = 0; k < pegs.length; k++){
			int px = pegs[k][0], py = pegs[k][1], length = pegs[k][2], width = pegs[k][1];
			for(double i = 0; i < 2 * Math.PI; i+= 2 * Math.PI/ sides){
				ct++;
				lines[pre + ct][0] = px + (int) (length * Math.cos(i));
				lines[pre + ct][1] = py + (int) (length * Math.sin(i));
				lines[pre + ct][2] = px + (int) (length * Math.cos(i - 2 *Math.PI / sides));
				lines[pre + ct][3] = py + (int) (length * Math.sin(i - 2 * Math.PI / sides));
			}
		}
		
		
	}
	private class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			repaint();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		spring[1] += G;
		ball[1] += spring[1];
		ball[0] += spring[0];
		
		
		
		if(ball[1] > 1000 && balls > 0){
			ball[0] = 550;
			ball[1] = 0;
			spring[0] = 0;
			spring[1] = 0;
			balls--;
		}
		if(ball[0] == 550 && ball[1] > sd){
			ball[1] = sd;
			spring[1] = Math.min(spring[1], setVelocity);
		}
		
		if(setlock == false){
			setVelocity *= 0.95; 
			setVelocity -= (sd - snorm)/30;
			sd += setVelocity;
		}
		double rc = 0.1;
		if(rdown){
			rtheta = Math.max(-0.5, rtheta - rc);
		}else{
			rtheta = Math.min(0.5, rtheta + rc);
		}
		if(ldown){
			ltheta = Math.max(-0.5, ltheta - rc);
		}else{
			ltheta = Math.min(0.5, ltheta + rc); 
		}
		
		lines[0][2] = lines[0][0] + (int) (Math.cos(ltheta) * paddleLength);
		lines[0][3] = lines[0][1] + (int) (Math.sin(ltheta) * paddleLength);
		lines[1][0] = lines[1][2] + (int) (-Math.cos(rtheta) * paddleLength);
		lines[1][1] = lines[1][3] + (int) (Math.sin(rtheta) * paddleLength);
		
		
		int rX = (int) ball[0];
		int rY = (int) ball[1];
		int r = 10;
		g.setColor(Color.blue);
		g.drawArc(rX - r, rY - r, 2 * r, 2 * r, 0, 360);
		g.setColor(Color.black);
		for(int i = 0; i < lines.length; i++){
			int x1 = lines[i][0],
				y1 = lines[i][1],
				x2 = lines[i][2];
			double y2 = lines[i][3] + 0.0001;
			if(i > preset.length){
				g.setColor(Color.red);
			}
			g.drawLine(x1, y1, x2, (int) Math.round(y2));

			double mult = Math.sqrt(spring[0] * spring[0] + spring[1] * spring[1]);
			double lineslope = ((double)(x2 - x1))/((double)(y2 - y1));
			double pegslope = spring[0] / spring[1];
			
			double var1 = ball[0] - pegslope * ball[1]; //difference between the loc of the ball and slope*y
			double var2 = x1 - lineslope * y1; 
			
			
			double angle1 = Math.atan2(y2 - y1, x2 - x1); //calculates the inverse of a tangent line to get the angle.
			double angle2 = Math.atan2(spring[1], spring[0]);
			
			double da = 2 * angle1 -  angle2;
			double y = (var1 - var2)/(lineslope - pegslope);
			double sx = y * pegslope + var1;
			
			
					
			
			if(sx >= Math.min(x2, x1) && sx <= Math.max(x1, x2) && 
			   Math.min(y1, y2) <= y && Math.max(y1, y2) >= y){
				double interdist = Math.sqrt(Math.pow(sx - ball[0],2) + Math.pow(y - ball[1],2));
				double tiny = 0.0001;
				double futuredist = Math.sqrt(Math.pow(sx - (ball[0] + Math.cos(angle2) * tiny),2) + Math.pow(y - (ball[1] + Math.sin(angle2) * tiny),2));
				
				if(interdist <=  mult + r && futuredist < interdist){ //if the ball collides with the pegs
					if(i > preset.length){
						int ball = (int) Math.floor((i - preset.length)/sides);
						points += pegs[ball][3] * mult;
						
						try {
						Clip clip2 = null;
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("bab.wav").getAbsoluteFile());
				        clip2 = AudioSystem.getClip();
				        clip2.open(audioInputStream);
				        clip2.start();
						} catch(Exception ex) {
							
						}
						for(int j = 0; j < lines.length; j++) {
							for(int c = 0; c < 4; c++) {
								lines[i][c]= 0;
							}
						}
					}
					spring[0] = Math.cos(da) * mult;
					spring[1] = Math.sin(da) * mult;
				}
			}
		}
		
		g.setColor(Color.black);
		
		g.fillRect(xpos - 5, (int)sd + 10, 10, 20);
		
		g.drawString("Score: " + points + " Balls: " + balls, 10, 15);
		
		
	}
	private class Key extends KeyAdapter {
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				setlock = true;
				sd += 3;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				ldown = true;
				
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rdown = true;
			}
		}
		public void keyReleased(KeyEvent e){
			setlock = false;
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				ldown = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rdown = false;
				
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}