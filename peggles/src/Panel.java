import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
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
	double gravy = 0.1; //gravity
	
	double[] ball = {550, 500}; //ball starting x and y loc
	double[] spring = {0,0};
	
	final int xpos = 550;
	int points = 0;
	int balls = 6; //number of balls you have available
	int sides = 13;

	boolean setlock = false;
	boolean rightDown, leftDown;
	
	double snorm = 800;
	double sd = 550;
	double setVelocity = 0;
	double paddleLength = 180;
	double rightTheta = 0;
	double leftTheta = 0;
	
	int preset[][] = {
		{0, 900, 500, 750,1}, //left paddle
		{530, 1100, 530, 900,1}, //right paddle
		{550, 0, 600, 20, 1}, //diagonal barrier
		{530, 75, 530, 1100, 1}, //right barrier
		{-1, 0, 1000, 0, 1}, //top barrier
		{0, -1, 0, 1100, 1}, //left barrier
		
	};
	
	int[][] pegs = {
		{(int)(Math.random()*500)+10, (int)(Math.random()*700)+100, (int)(Math.random()*30)+35, 50}, //(Randomize x loc, randomize y loc, randomize radius)
		{(int)(Math.random()*500)+10, (int)(Math.random()*700)+100, (int)(Math.random()*30)+35, 50},
		{(int)(Math.random()*500)+10, (int)(Math.random()*700)+100, (int)(Math.random()*30)+35, 50},
		{(int)(Math.random()*500)+10, (int)(Math.random()*700)+100, (int)(Math.random()*30)+35, 50},
		{(int)(Math.random()*500)+10, (int)(Math.random()*700)+100, (int)(Math.random()*30)+35, 50},
		{(int)(Math.random()*500)+10, (int)(Math.random()*700)+100, (int)(Math.random()*30)+35, 50}
	};
	
	int linear[][] = new int[100][5];
	
	
	public Panel() {
		super();
		t.start();

		setFocusable(true);
		
		addKeyListener(new Key());
		
		for(int i = 0; i < preset.length; i++){
			linear[i] = preset[i]; //put data from preset[][] 2d array into linear[][]
		}
		int pre = preset.length;
		int counter = 0;
		
		for(int j = 0; j < pegs.length; j++){
			int pointX = pegs[j][0], pointY = pegs[j][1], length = pegs[j][2], width = pegs[j][1];
			for(double i = 0; i < 2 * Math.PI; i+= 2 * Math.PI/ sides){
				counter++;
				
				linear[pre + counter][0] = pointX+(int)(length*Math.cos(i));
				
				linear[pre + counter][1] = pointY+(int)(length*Math.sin(i));
				
				linear[pre + counter][2] = pointX+(int)(length*Math.cos(i-2*Math.PI/sides));
				
				linear[pre + counter][3] = pointY+(int)(length*Math.sin(i-2*Math.PI/sides));
			}
		}
		 
		
	}

	private class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			repaint();
		}
	}
	
	public void paintComponent(Graphics g){
		
//		if(balls == 0) { //play ending music when you run out of balls
//			//music
//			Clip clip2 = null;
//			try {
//		        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("ode1.wav").getAbsoluteFile());
//		        clip2 = AudioSystem.getClip();
//		        clip2.open(audioInputStream);
//		        //clip.start();
//		    } catch(Exception ex) {
//		        System.out.println("Error with playing sound.");
//		        ex.printStackTrace();
//		    }
//			clip2.start();
//		}
		
		super.paintComponent(g);
		spring[1] += gravy;
		
		ball[1] += spring[1];
		ball[0] += spring[0];
		
		
		
		if(ball[1] > 1000 && balls > 0){
			ball[0] = 550;
			ball[1] = 100;
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
			setVelocity -= (sd - snorm)/40;
			sd += setVelocity;
		}
		double rad = 0.1;
		if(rightDown){
			rightTheta = Math.max(-0.5, rightTheta - rad);
		}
		else{
			rightTheta = Math.min(0.5, rightTheta + rad);
		}
		if(leftDown){
			leftTheta = Math.max(-0.5, leftTheta - rad);
		}
		else{
			leftTheta = Math.min(0.5, leftTheta + rad); 
		}
		int rX = (int) ball[0];
		int rY = (int) ball[1];
		int r = 10;
		linear[0][2]=linear[0][0]+(int)(Math.cos(leftTheta)*paddleLength);
		
		linear[0][3]=linear[0][1]+(int)(Math.sin(leftTheta)*paddleLength);
		
		linear[1][0]=linear[1][2]+(int)(-Math.cos(rightTheta)*paddleLength);
		
		linear[1][1]=linear[1][3]+(int)(Math.sin(rightTheta)*paddleLength);
		
		
		
		g.setColor(Color.CYAN);
		g.drawArc(rX-r,rY-r,2*r,2*r,0,360);
		g.setColor(Color.white);
		
		for(int i = 0; i < linear.length; i++){
			int x1 = linear[i][0],
				y1 = linear[i][1],
				x2 = linear[i][2];
			double y2 = linear[i][3] + 0.0001;
			
			if(i > preset.length){
				g.setColor(Color.red);
			}
			
			g.drawLine(x1, y1, x2, (int) Math.round(y2));

			double mult = Math.sqrt(spring[0]*spring[0]+spring[1]*spring[1]);
			
			double linearlope = ((double)(x2 - x1))/((double)(y2 - y1));
			
			double pegslope = spring[0] / spring[1];
			
			double var1 = ball[0] - pegslope * ball[1]; //difference between the loc of the ball and slope*y
			
			double var2 = x1 - linearlope * y1; 
			
			
			double angle1 = Math.atan2(y2 - y1, x2 - x1); //calculates the inverse of a tangent line to get the angle.
			
			double angle2 = Math.atan2(spring[1], spring[0]);
			
			double diffa = 2 * angle1 -  angle2;
			
			double y = (var1 - var2)/(linearlope - pegslope);
			
			double sx = y * pegslope + var1;
			
			
					
			
			if(sx >= Math.min(x2, x1) && sx <= Math.max(x1, x2) && Math.min(y1, y2) <= y && Math.max(y1, y2) >= y){
				double interdist = Math.sqrt(Math.pow(sx - ball[0],2) + Math.pow(y - ball[1],2));
				double bound = 0.0001;
				double futuredist = Math.sqrt(Math.pow(sx - (ball[0] + Math.cos(angle2) * bound),2) + Math.pow(y - (ball[1] + Math.sin(angle2) * bound),2));
				
				if(futuredist < interdist && interdist <= mult+r){ //if the ball collides with the pegs
					if(i >= preset.length){
						int ball = (int) Math.floor((i - preset.length)/sides);
						points += pegs[ball][3] * mult;
						
						try { //play bababooey upon collision 
						Clip clip2 = null;
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("bab.wav").getAbsoluteFile());
				        clip2 = AudioSystem.getClip();
				        clip2.open(audioInputStream);
				        clip2.start();
						} catch(Exception ex) {
							
						}
						
						for(int c = 0; c < linear[c].length; c++) {
							linear[i][c]= 0;
						}
						
						
						
					}
					spring[0] = Math.cos(diffa) * mult;
					
					spring[1] = Math.sin(diffa) * mult;
				}
			}
		}
		
		g.setColor(Color.white);
		
		g.fillRect(xpos - 5, (int)sd + 10, 10, 20);
		
		g.drawString("Score: " + points + " Balls: " + balls, 10, 15);
		
	}
	
	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getBalls() {
		return balls;
	}
	public void setBalls(int balls) {
		this.balls = balls;
	}
	
	private class Key extends KeyAdapter {
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				setlock = true;
				sd += 3;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				leftDown = true;
				
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rightDown = true;
			}
		}
		public void keyReleased(KeyEvent e){
			setlock = false;
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				leftDown = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rightDown = false;
				
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