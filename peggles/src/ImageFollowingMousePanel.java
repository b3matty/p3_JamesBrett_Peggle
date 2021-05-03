import java.awt.Component;
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
import javax.swing.JPanel;
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

class ImageFollowingMousePanel extends JPanel implements MouseMotionListener
{
    private final BufferedImage image;
    private Point imagePosition = new Point(150,150);
    private double imageAngleRad = 0;

    public ImageFollowingMousePanel()
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