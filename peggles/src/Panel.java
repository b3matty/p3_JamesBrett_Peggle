import javax.imageio.ImageIO;
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
	
	double[] p2d = {280, 200};
	double[] v2d = {0,0};
	
	final int xpos = 280;
	int points = 0;
	int balls = 2;
	int sides = 13;
	
	double snorm = 400;
	double sd = 450;
	double sv = 0;
	double paddle = 130;
	double rtheta = 0;
	double ltheta = 0;
	
	boolean setlock = false;
	boolean rdown, ldown;
	
	int preset[][] = {
		{150, 50, 0, 0,0}, //right paddle
		{0, 0, 0, 0,0}, //left paddle
		{0, 0, 0, 0,0}, //first bouncey thingy
		{0, 0, 0, 0,0}, //right wall
		{0, 0, 0, 0,0}, //top wall
		{0, 0, 0, 0,0} //left wall	
	};
	
	int[][] pegs = {
		{0, 50, 50, 50},
		{0, 0, 0, 0},
		{0, 0, 0, 0},
		{0, 0, 0, 0}	
	};
	
	int lines[][] = new int[100][5];
	
	
	public Panel() {
		super();
		t.start();
		addKeyListener(new Key());
		setFocusable(true);
		
		for(int i = 0; i < preset.length; i++){
			lines[i] = preset[i];
		}
		int pre = preset.length;
		int ct =0;
		
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
		v2d[1] += G;
		p2d[1] += v2d[1];
		p2d[0] += v2d[0];
		
		
		
		if(p2d[1] > 1000 && balls > 0){
			p2d[0] = 280;
			p2d[1] = 100;
			v2d[0] = 0;
			v2d[1] = 0;
			balls--;
		}
		if(p2d[0] == 280 && p2d[1] > sd){
			p2d[1] = sd;
			v2d[1] = Math.min(v2d[1], sv);
		}
		
		if(setlock == false){
			sv *= 0.95; //the dampening coefficient for the springiness
			sv -= (sd - snorm)/30;
			sd += sv;
		}
		
		
		lines[0][2] = lines[0][0] + (int) (Math.cos(ltheta) * paddle);
		lines[0][3] = lines[0][1] + (int) (Math.sin(ltheta) * paddle);
		lines[1][0] = lines[1][2] + (int) (-Math.cos(rtheta) * paddle);
		lines[1][1] = lines[1][3] + (int) (Math.sin(rtheta) * paddle);
		int rX = (int) p2d[0];
		int rY = (int) p2d[1];
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

			double bmag = Math.sqrt(v2d[0] * v2d[0] + v2d[1] * v2d[1]);
			double lineslope = ((double)(x2 - x1))/((double)(y2 - y1));
			double pegslope = v2d[0] / v2d[1];
			
			double binter = p2d[0] - pegslope * p2d[1];
			double linter = x1 - lineslope * y1;
			
			double y = (binter - linter)/(lineslope - pegslope);
			double sx = y * pegslope + binter;
			
			double la = Math.atan2(y2 - y1, x2 - x1);
			double ba = Math.atan2(v2d[1], v2d[0]);
			
			double da = 2 * la -  ba;
					
			
			if(sx >= Math.min(x2, x1) && sx <= Math.max(x1, x2) && 
			   Math.min(y1, y2) <= y && Math.max(y1, y2) >= y){
				double interdist = Math.sqrt(Math.pow(sx - p2d[0],2) + Math.pow(y - p2d[1],2));
				double tiny = 0.0001;
				double futuredist = Math.sqrt(Math.pow(sx - (p2d[0] + Math.cos(ba) * tiny),2) + Math.pow(y - (p2d[1] + Math.sin(ba) * tiny),2));
				
				if(interdist <=  bmag + r && futuredist < interdist){ 
					if(i > preset.length){
						int ball = (int) Math.floor((i - preset.length)/sides);
						//System.out.println(pegs[ball][2]);
						points += pegs[ball][3] * bmag;
					}
					v2d[0] = Math.cos(da) * bmag;
					v2d[1] = Math.sin(da) * bmag;
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