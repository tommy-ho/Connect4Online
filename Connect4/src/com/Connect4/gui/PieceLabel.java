package com.Connect4.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JLabel;

public class PieceLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;
	private String color;
	private int[] circParams;
	private Graphics2D g2d;

	public PieceLabel(String c, String s) {
		color = c;
		if (s.equals("standard")){
			circParams = new int[]{20, 10, 30, 30};
		} else {
			circParams = new int[]{15, 5, 20, 20};
		}
	}
	
	public void paint(Graphics g) {
	    g2d = (Graphics2D) g;
	    Shape circle = new Ellipse2D.Double(circParams[0], circParams[1], circParams[2], circParams[3]);
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
