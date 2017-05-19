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
		Color curColor = Color.WHITE;
		String outlineType = null;
		Shape curShape = null;
		for (int i = 0; i < model.getShapes().size(); i++) {
			cs355.model.drawing.Shape shape = model.getShape(i);
			boolean curSelect = (model.getShapeIndex() == i);
			if(shape instanceof Line){
				Line line = (Line)shape; 
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(line.getCenter().getX(), line.getCenter().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(line.getColor());
				g2d.drawLine((int)line.getStart().getX(), (int)line.getStart().getY(), (int)line.getEnd().getX(), (int)line.getEnd().getY());
				if(curSelect){
					curShape = shape;
					outlineType = "LINE";
				}
			}
			else if(shape instanceof Ellipse){
				Ellipse ellipse = (Ellipse) shape;
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(ellipse.getCenter().getX(), ellipse.getCenter().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(ellipse.getColor());
				g2d.fillOval(0, 0, (int)ellipse.getWidth(), (int)ellipse.getHeight());
				if(curSelect){
					curShape = shape;
					outlineType = "ELLIPSE";
				}
			}
			else if(shape instanceof Circle){
				Circle circle = (Circle) shape;
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(circle.getCenter().getX(), circle.getCenter().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(circle.getColor());
				g2d.fillOval(0, 0, (int)circle.getRadius(), (int)circle.getRadius());
				if(curSelect){
					curShape = shape;
					outlineType = "CIRCLE";
				}
			}
			else if(shape instanceof Rectangle){
				Rectangle rect = (Rectangle) shape;
				java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(0-(rect.getWidth()/2), 0-(rect.getHeight()/2));
				int[] xPoints = {(int)UL.getX(), (int)(UL.getX() + rect.getWidth()), (int)(UL.getX() + rect.getWidth()), (int)UL.getX()};
				int[] yPoints = {(int)UL.getY(), (int)UL.getY(), (int)(UL.getY() + rect.getHeight()), (int)(UL.getY() + rect.getHeight())};
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(rect.getCenter().getX(), rect.getCenter().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(rect.getColor());
				g2d.fillPolygon(xPoints, yPoints, 4);
				if(curSelect){
					curShape = shape;
					outlineType = "RECT";
				}
			}
			else if(shape instanceof Square){
				Square square = (Square)shape;
				java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(0-(square.getSize()/2), 0-(square.getSize()/2));
				int[] xPoints = {(int)UL.getX(), (int)(UL.getX() + square.getSize()),(int)(UL.getX() + square.getSize()), (int)UL.getX()};
				int[] yPoints = {(int)UL.getY(), (int)UL.getY(), (int)(UL.getY() + square.getSize()), (int)(UL.getY() + square.getSize())};
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(square.getCenter().getX(), square.getCenter().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(square.getColor());
				g2d.fillPolygon(xPoints, yPoints, 4);
				if(curSelect){
					curShape = shape;
					outlineType = "SQUARE";
				}
			}
			else if(shape instanceof Triangle){
				Triangle tri = (Triangle) shape;
				int[] xPoints = {(int)(tri.getA().getX()), (int)(tri.getB().getX()), (int)(tri.getC().getX())};
				int[] yPoints = {(int)(tri.getA().getY()), (int)(tri.getB().getY()), (int)(tri.getC().getY())};
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.translate(tri.getCenter().getX(), tri.getCenter().getY());
				//rotate
				g2d.setTransform(objToWorld);
				g2d.setColor(tri.getColor());
				g2d.fillPolygon(xPoints, yPoints, 3);
				if(curSelect){
					curShape = shape;
					outlineType = "TRI";
				}
			}
		}
		//draw box
		if(model.getShapeIndex()>-1){
			curColor = Color.WHITE;		
			if(curShape.getColor() == Color.WHITE){
				curColor = Color.RED;
			}
			g2d.setColor(curColor);
			AffineTransform objToWorld = new AffineTransform();
			objToWorld.translate(curShape.getCenter().getX(), curShape.getCenter().getY());
			//rotate
			g2d.setTransform(objToWorld);
			switch(outlineType){
				case "LINE":
					Line line = (Line)curShape; 
					java.awt.geom.Point2D.Double start = line.getStart();
					java.awt.geom.Point2D.Double end = line.getEnd();
					g2d.drawRect((int)start.getX()-4, (int)start.getY()-4, 8, 8);
					g2d.drawRect((int)end.getX()-4, (int)end.getY()-4, 8, 8);
					break;
				case "SQUARE":
					Square square = (Square) curShape;
					java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(0-(square.getSize()/2), 0-(square.getSize()/2));
					g2d.drawLine((int)UL.getX(), (int)UL.getY(), (int)(UL.getX()+square.getSize()), (int)(UL.getY()));
					g2d.drawLine((int)(UL.getX()+square.getSize()), (int)(UL.getY()), (int)(UL.getX()+square.getSize()), (int)(UL.getY()+square.getSize()));
					g2d.drawLine((int)UL.getX(), (int)UL.getY(), (int)(UL.getX()), (int)(UL.getY()+square.getSize()));
					g2d.drawLine((int)UL.getX(), (int)(UL.getY() + square.getSize()), (int)(UL.getX()+square.getSize()), (int)(UL.getY()+square.getSize()));
					
					g2d.drawRect((int)(UL.getX()+square.getSize()/2-4), (int)(UL.getY()-12), 8, 8);
					
					break;
				case "RECT":
					Rectangle rect = (Rectangle) curShape;
					java.awt.geom.Point2D.Double upperLeft = new java.awt.geom.Point2D.Double(0-(rect.getWidth()/2), 0-(rect.getHeight()/2));
					g2d.drawLine((int)upperLeft.getX(), (int)upperLeft.getY(), (int)(upperLeft.getX()+rect.getWidth()), (int)(upperLeft.getY()));
					g2d.drawLine((int)(upperLeft.getX()+rect.getWidth()), (int)(upperLeft.getY()), (int)(upperLeft.getX()+rect.getWidth()), (int)(upperLeft.getY()+rect.getHeight()));
					g2d.drawLine((int)upperLeft.getX(), (int)upperLeft.getY(), (int)(upperLeft.getX()), (int)(upperLeft.getY()+rect.getHeight()));
					g2d.drawLine((int)upperLeft.getX(), (int)(upperLeft.getY() + rect.getHeight()), (int)(upperLeft.getX()+rect.getWidth()), (int)(upperLeft.getY()+rect.getHeight()));
					
					g2d.drawRect((int)(upperLeft.getX()+rect.getWidth()/2-4), (int)(upperLeft.getY()-12), 8, 8);
					
					break;
				case "CIRCLE":
					Circle circle = (Circle) curShape;
					g2d.drawLine(0, (int)0, (int)(0+circle.getRadius()), (int)(0));
					g2d.drawLine((int)(0 +circle.getRadius()), (int)(0), (int)(0+circle.getRadius()), (int)(0+circle.getRadius()));
					g2d.drawLine(0, (int)0, (int)(0), (int)(0+circle.getRadius()));
					g2d.drawLine(0, (int)(0 + circle.getRadius()), (int)(0+circle.getRadius()), (int)(0+circle.getRadius()));
					
					g2d.drawRect((int)(0+circle.getRadius()/2-4), (int)(0-12), 8, 8);
					
					break;
				case "ELLIPSE":
					Ellipse ellipse = (Ellipse) curShape;
					g2d.drawLine((int)0, (int)0, (int)(0+ellipse.getWidth()), (int)(0));
					g2d.drawLine((int)(0+ellipse.getWidth()), (int)(0), (int)(0+ellipse.getWidth()), (int)(0+ellipse.getHeight()));
					g2d.drawLine((int)0, (int)0, (int)(0), (int)(0+ellipse.getHeight()));
					g2d.drawLine((int)0, (int)(0 + ellipse.getHeight()), (int)(0+ellipse.getWidth()), (int)(0+ellipse.getHeight()));
					
					g2d.drawRect((int)(0+ellipse.getWidth()/2-4), (int)(0-12), 8, 8);
					
					break;
				case "TRI":
					Triangle tri = (Triangle) curShape;
					g2d.drawLine((int)(tri.getA().getX() + 0), (int)(tri.getA().getY() + 0), (int)(tri.getB().getX() + 0), (int)(tri.getB().getY() + 0));
					g2d.drawLine((int)(tri.getB().getX() + 0), (int)(tri.getB().getY() + 0), (int)(tri.getC().getX() + 0), (int)(tri.getC().getY() + 0));
					g2d.drawLine((int)(tri.getC().getX() + 0), (int)(tri.getC().getY() + 0), (int)(tri.getA().getX() + 0), (int)(tri.getA().getY() + 0));
					
					double xValue = (Math.min(Math.min(tri.getA().getY(), tri.getB().getY()), tri.getC().getY()) + Math.max(Math.max(tri.getA().getY(), tri.getB().getY()), tri.getC().getY()))/2 -4;
					double yValue = Math.min(Math.min(tri.getA().getY(), tri.getB().getY()), tri.getC().getY())-12;
					g2d.drawRect((int)xValue, (int)yValue, 8, 8);
					break;
			}
		}

	}

}
