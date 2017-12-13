package com.Connect4.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PieceLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;
	private String color;
	private Graphics2D g2d;

	public PieceLabel(String c) {
		color = c;
	}
	
	public void paint(Graphics g) {
	    g2d = (Graphics2D) g;
	    Shape circle = new Ellipse2D.Double(20, 10, 30, 30);
	    g2d.setStroke(new BasicStroke(5));
	    g2d.setColor(Color.BLACK);
	    g2d.draw(circle);
	    
	    if (color.equals("red")){
		    g2d.setColor(Color.RED);
		    g2d.fill(circle);
	    } else if (color.equals("black")) { //it is black
	 	    g2d.fill(circle);
	    } else { //it is blank
	    	g2d.setColor(Color.LIGHT_GRAY);
	    	g2d.fill(circle);
	    }
	}
	
	public void changeColor(String c){
		color = c;
		paint(g2d);
	}

}
