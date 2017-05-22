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
	java.awt.geom.Point2D.Double viewPort;
	double zoom;

	public View(Model uploadModel){
		model = uploadModel;
		viewPort = new java.awt.geom.Point2D.Double(0, 0);
		zoom = 1;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d){
		Color curColor = Color.WHITE;
		String outlineType = null;
		Shape curShape = null;
		for (int i = 0; i < model.getShapes().size(); i++) {
			cs355.model.drawing.Shape shape = model.getShape(i);
			boolean curSelect = (model.getShapeIndex() == i);
			if(shape instanceof Line){
				Line line = (Line)shape; 
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.concatenate(new AffineTransform(zoom,0,0,zoom,0,0));
				objToWorld.concatenate(new AffineTransform(1,0,0,1,shape.getCenter().getX(), shape.getCenter().getY()));
				objToWorld.concatenate(new AffineTransform(Math.cos(shape.getRotation()), Math.sin(shape.getRotation()), -Math.sin(shape.getRotation()), Math.cos(shape.getRotation()), 0,0));
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
				objToWorld.concatenate(new AffineTransform(zoom,0,0,zoom,0,0));
				objToWorld.concatenate(new AffineTransform(1,0,0,1,shape.getCenter().getX(), shape.getCenter().getY()));
				objToWorld.concatenate(new AffineTransform(Math.cos(shape.getRotation()), Math.sin(shape.getRotation()), -Math.sin(shape.getRotation()), Math.cos(shape.getRotation()), 0,0));
				g2d.setTransform(objToWorld);
				g2d.setColor(ellipse.getColor());
				g2d.fillOval((int)(0 - ellipse.getWidth()/2), (int)(0 - ellipse.getHeight()/2), (int)ellipse.getWidth(), (int)ellipse.getHeight());
				if(curSelect){
					curShape = shape;
					outlineType = "ELLIPSE";
				}
			}
			else if(shape instanceof Circle){
				Circle circle = (Circle) shape;
				AffineTransform objToWorld = new AffineTransform();
				objToWorld.concatenate(new AffineTransform(zoom,0,0,zoom,0,0));
				objToWorld.concatenate(new AffineTransform(1,0,0,1,shape.getCenter().getX(), shape.getCenter().getY()));
				objToWorld.concatenate(new AffineTransform(Math.cos(shape.getRotation()), Math.sin(shape.getRotation()), -Math.sin(shape.getRotation()), Math.cos(shape.getRotation()), 0,0));
				g2d.setTransform(objToWorld);
				g2d.setColor(circle.getColor());
				g2d.fillOval((int)(0-circle.getRadius()/2), (int)(0-circle.getRadius()/2), (int)circle.getRadius(), (int)circle.getRadius());
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
				objToWorld.concatenate(new AffineTransform(zoom,0,0,zoom,0,0));
				objToWorld.concatenate(new AffineTransform(1,0,0,1,shape.getCenter().getX(), shape.getCenter().getY()));
				objToWorld.concatenate(new AffineTransform(Math.cos(shape.getRotation()), Math.sin(shape.getRotation()), -Math.sin(shape.getRotation()), Math.cos(shape.getRotation()), 0,0));
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
				objToWorld.concatenate(new AffineTransform(zoom,0,0,zoom,0,0));
				objToWorld.concatenate(new AffineTransform(1,0,0,1,shape.getCenter().getX(), shape.getCenter().getY()));
				objToWorld.concatenate(new AffineTransform(Math.cos(shape.getRotation()), Math.sin(shape.getRotation()), -Math.sin(shape.getRotation()), Math.cos(shape.getRotation()), 0,0));
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
				objToWorld.concatenate(new AffineTransform(zoom,0,0,zoom,0,0));
				objToWorld.concatenate(new AffineTransform(1,0,0,1,shape.getCenter().getX(), shape.getCenter().getY()));
				objToWorld.concatenate(new AffineTransform(Math.cos(shape.getRotation()), Math.sin(shape.getRotation()), -Math.sin(shape.getRotation()), Math.cos(shape.getRotation()), 0,0));
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
			objToWorld.concatenate(new AffineTransform(zoom,0,0,zoom,0,0));
			objToWorld.concatenate(new AffineTransform(1,0,0,1,curShape.getCenter().getX(), curShape.getCenter().getY()));
			objToWorld.concatenate(new AffineTransform(Math.cos(curShape.getRotation()), Math.sin(curShape.getRotation()), -Math.sin(curShape.getRotation()), Math.cos(curShape.getRotation()), 0,0));
			g2d.setTransform(objToWorld);
			switch(outlineType){
				case "LINE":
					Line line = (Line)curShape; 
					java.awt.geom.Point2D.Double start = line.getStart();
					java.awt.geom.Point2D.Double end = line.getEnd();
					g2d.drawRect((int)start.getX()-4, (int)start.getY()-4, 8, 8);
					g2d.drawRect((int)end.getX()-4, (int)end.getY()-4, 8, 8);
					g2d.drawLine((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY());
					break;
				case "SQUARE":
					Square square = (Square) curShape;
					java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(0-(square.getSize()/2), 0-(square.getSize()/2));
					g2d.drawRect((int)UL.getX(), (int)UL.getY(), (int)square.getSize(), (int)square.getSize());
					
					g2d.drawRect((int)(UL.getX()+square.getSize()/2-4), (int)(UL.getY()-12), 8, 8);
					
					break;
				case "RECT":
					Rectangle rect = (Rectangle) curShape;
					java.awt.geom.Point2D.Double upperLeft = new java.awt.geom.Point2D.Double(0-(rect.getWidth()/2), 0-(rect.getHeight()/2));
					g2d.drawRect((int)upperLeft.getX(), (int)upperLeft.getY(), (int)rect.getWidth(), (int)rect.getHeight());

					g2d.drawRect((int)(upperLeft.getX()+rect.getWidth()/2-4), (int)(upperLeft.getY()-12), 8, 8);
					
					break;
				case "CIRCLE":
					Circle circle = (Circle) curShape;
					g2d.drawRect(-(int)(circle.getRadius()/2), -(int)(circle.getRadius()/2), (int)circle.getRadius(), (int)circle.getRadius());
		
					g2d.drawRect((int)(-4), -(int)(circle.getRadius()/2)-12, 8, 8);
					
					break;
				case "ELLIPSE":
					Ellipse ellipse = (Ellipse) curShape;
					g2d.drawRect(-(int)(ellipse.getWidth()/2), -(int)ellipse.getHeight()/2, (int)ellipse.getWidth(), (int)ellipse.getHeight());
					
					g2d.drawRect((int)(-4), -(int)(ellipse.getHeight()/2)-12, 8, 8);
					
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

	public void setView(java.awt.geom.Point2D.Double newView){
		viewPort = newView;
	}
	
	public void setZoom(double newZoom){
		zoom = newZoom;
	}

}
