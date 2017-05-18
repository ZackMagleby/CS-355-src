package customClasses;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Observable;


import cs355.GUIFunctions;
import cs355.model.drawing.*;
import cs355.view.ViewRefresher;

public class View implements ViewRefresher {
	
	Model model;

	public View(Model uploadModel){
		model = uploadModel;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d) {
		for (int i = 0; i < model.getShapes().size(); i++) {
			cs355.model.drawing.Shape shape = model.getShape(i);
			boolean curSelect = (model.getShapeIndex() == i);
			Color curColor = Color.WHITE;
			if(shape.getColor() == Color.WHITE){
				curColor = Color.RED;
			}
			if(shape instanceof Line){
				Line line = (Line)shape; 
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(-line.getTranslation().getX(), -line.getTranslation().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(line.getColor());
				g2d.drawLine((int)line.getStart().getX(), (int)line.getStart().getY(), (int)line.getEnd().getX(), (int)line.getEnd().getY());
				if(curSelect){
					g2d.setColor(curColor);
				}
			}
			else if(shape instanceof Ellipse){
				Ellipse ellipse = (Ellipse) shape;
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(-ellipse.getTranslation().getX(), -ellipse.getTranslation().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(ellipse.getColor());
				g2d.fillOval((int)ellipse.getCenter().getX(), (int)ellipse.getCenter().getY(), (int)ellipse.getWidth(), (int)ellipse.getHeight());
				if(curSelect){
					g2d.setColor(curColor);
					g2d.drawLine((int)ellipse.getCenter().getX(), (int)ellipse.getCenter().getY(), (int)(ellipse.getCenter().getX()+ellipse.getWidth()), (int)(ellipse.getCenter().getY()));
					g2d.drawLine((int)(ellipse.getCenter().getX()+ellipse.getWidth()), (int)(ellipse.getCenter().getY()), (int)(ellipse.getCenter().getX()+ellipse.getWidth()), (int)(ellipse.getCenter().getY()+ellipse.getHeight()));
					g2d.drawLine((int)ellipse.getCenter().getX(), (int)ellipse.getCenter().getY(), (int)(ellipse.getCenter().getX()), (int)(ellipse.getCenter().getY()+ellipse.getHeight()));
					g2d.drawLine((int)ellipse.getCenter().getX(), (int)(ellipse.getCenter().getY() + ellipse.getHeight()), (int)(ellipse.getCenter().getX()+ellipse.getWidth()), (int)(ellipse.getCenter().getY()+ellipse.getHeight()));
				}
			}
			else if(shape instanceof Circle){
				Circle circle = (Circle) shape;
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(-circle.getTranslation().getX(), -circle.getTranslation().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(circle.getColor());
				g2d.fillOval((int)circle.getCenter().getX(), (int)circle.getCenter().getY(), (int)circle.getRadius(), (int)circle.getRadius());
				if(curSelect){
					g2d.setColor(curColor);
					g2d.drawLine((int)circle.getCenter().getX(), (int)circle.getCenter().getY(), (int)(circle.getCenter().getX()+circle.getRadius()), (int)(circle.getCenter().getY()));
					g2d.drawLine((int)(circle.getCenter().getX()+circle.getRadius()), (int)(circle.getCenter().getY()), (int)(circle.getCenter().getX()+circle.getRadius()), (int)(circle.getCenter().getY()+circle.getRadius()));
					g2d.drawLine((int)circle.getCenter().getX(), (int)circle.getCenter().getY(), (int)(circle.getCenter().getX()), (int)(circle.getCenter().getY()+circle.getRadius()));
					g2d.drawLine((int)circle.getCenter().getX(), (int)(circle.getCenter().getY() + circle.getRadius()), (int)(circle.getCenter().getX()+circle.getRadius()), (int)(circle.getCenter().getY()+circle.getRadius()));
				}
			}
			else if(shape instanceof Rectangle){
				Rectangle rect = (Rectangle) shape;
				java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(rect.getCenter().getX()-(rect.getWidth()/2), rect.getCenter().getY()-(rect.getHeight()/2));
				int[] xPoints = {(int)UL.getX(), (int)(UL.getX() + rect.getWidth()), (int)(UL.getX() + rect.getWidth()), (int)UL.getX()};
				int[] yPoints = {(int)UL.getY(), (int)UL.getY(), (int)(UL.getY() + rect.getHeight()), (int)(UL.getY() + rect.getHeight())};
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(-rect.getTranslation().getX(), -rect.getTranslation().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(rect.getColor());
				g2d.fillPolygon(xPoints, yPoints, 4);
				if(curSelect){
					g2d.setColor(curColor);
					g2d.drawLine((int)UL.getX(), (int)UL.getY(), (int)(UL.getX()+rect.getWidth()), (int)(UL.getY()));
					g2d.drawLine((int)(UL.getX()+rect.getWidth()), (int)(UL.getY()), (int)(UL.getX()+rect.getWidth()), (int)(UL.getY()+rect.getHeight()));
					g2d.drawLine((int)UL.getX(), (int)UL.getY(), (int)(UL.getX()), (int)(UL.getY()+rect.getHeight()));
					g2d.drawLine((int)UL.getX(), (int)(UL.getY() + rect.getHeight()), (int)(UL.getX()+rect.getWidth()), (int)(UL.getY()+rect.getHeight()));
				}
			}
			else if(shape instanceof Square){
				Square square = (Square)shape;
				java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(square.getCenter().getX()-(square.getSize()/2), square.getCenter().getY()-(square.getSize()/2));
				int[] xPoints = {(int)UL.getX(), (int)(UL.getX() + square.getSize()),(int)(UL.getX() + square.getSize()), (int)UL.getX()};
				int[] yPoints = {(int)UL.getY(), (int)UL.getY(), (int)(UL.getY() + square.getSize()), (int)(UL.getY() + square.getSize())};
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(-square.getTranslation().getX(), -square.getTranslation().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(square.getColor());
				g2d.fillPolygon(xPoints, yPoints, 4);
				if(curSelect){
					g2d.setColor(curColor);
					g2d.drawLine((int)UL.getX(), (int)UL.getY(), (int)(UL.getX()+square.getSize()), (int)(UL.getY()));
					g2d.drawLine((int)(UL.getX()+square.getSize()), (int)(UL.getY()), (int)(UL.getX()+square.getSize()), (int)(UL.getY()+square.getSize()));
					g2d.drawLine((int)UL.getX(), (int)UL.getY(), (int)(UL.getX()), (int)(UL.getY()+square.getSize()));
					g2d.drawLine((int)UL.getX(), (int)(UL.getY() + square.getSize()), (int)(UL.getX()+square.getSize()), (int)(UL.getY()+square.getSize()));					
				}
			}
			else if(shape instanceof Triangle){
				Triangle tri = (Triangle) shape;
				java.awt.geom.Point2D.Double center = tri.getCenter();
				int[] xPoints = {(int)(tri.getA().getX() + center.getX()), (int)(tri.getB().getX() + center.getX()), (int)(tri.getC().getX() + center.getX())};
				int[] yPoints = {(int)(tri.getA().getY() + center.getY()), (int)(tri.getB().getY() + center.getY()), (int)(tri.getC().getY() + center.getY())};
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(-tri.getTranslation().getX(), -tri.getTranslation().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(tri.getColor());
				g2d.fillPolygon(xPoints, yPoints, 3);
				if(curSelect){
					g2d.setColor(curColor);
					g2d.drawLine((int)(tri.getA().getX() + center.getX()), (int)(tri.getA().getY() + center.getY()), (int)(tri.getB().getX() + center.getX()), (int)(tri.getB().getY() + center.getY()));
					g2d.drawLine((int)(tri.getB().getX() + center.getX()), (int)(tri.getB().getY() + center.getY()), (int)(tri.getC().getX() + center.getX()), (int)(tri.getC().getY() + center.getY()));
					g2d.drawLine((int)(tri.getC().getX() + center.getX()), (int)(tri.getC().getY() + center.getY()), (int)(tri.getA().getX() + center.getX()), (int)(tri.getA().getY() + center.getY()));
				}
			}
		}

	}

}
