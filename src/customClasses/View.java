package customClasses;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.util.Observable;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImageOp;

import cs355.GUIFunctions;
import cs355.model.scene.CS355Scene;
import cs355.model.scene.Point3D;
import cs355.model.scene.Line3D;
import cs355.model.drawing.*;
import cs355.view.ViewRefresher;
import sun.java2d.pipe.DrawImage;

public class View implements ViewRefresher {
	
	Model model;
	java.awt.geom.Point2D.Double viewPort;
	double zoom;
	
	boolean ThreeD;
	Point3D camera = new Point3D(-30, 2.5, -10);
	double rotation = (Math.PI);
	int farPlane = 1000;
	int nearPlane = 0;
	final double fov = 45.0;
	CS355Scene scene = null;
	
	CustomImage image = null;
	boolean imageShow;

	public View(Model uploadModel){
		model = uploadModel;
		viewPort = new java.awt.geom.Point2D.Double(0, 0);
		zoom = 1;
		ThreeD = false;
		imageShow = false;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d){
		if(ThreeD){
			if(scene != null){
				draw3d(g2d);
			}
			return;
		}
		if(imageShow){
			if(image != null){
				drawImage(g2d);
			}
			return;
		}
		Color curColor = Color.WHITE;
		String outlineType = null;
		Shape curShape = null;
		for (int i = 0; i < model.getShapes().size(); i++) {
			cs355.model.drawing.Shape shape = model.getShape(i);
			AffineTransform aT = objToView(shape);
			g2d.setTransform(aT);
			g2d.setColor(shape.getColor());
			boolean curSelect = (model.getShapeIndex() == i);
			if(shape instanceof Line){
				Line line = (Line)shape; 
				g2d.drawLine((int)line.getStart().getX(), (int)line.getStart().getY(), (int)line.getEnd().getX(), (int)line.getEnd().getY());
				if(curSelect){
					curShape = shape;
					outlineType = "LINE";
				}
			}
			else if(shape instanceof Ellipse){
				Ellipse ellipse = (Ellipse) shape;
				g2d.fillOval((int)(0 - ellipse.getWidth()/2), (int)(0 - ellipse.getHeight()/2), (int)ellipse.getWidth(), (int)ellipse.getHeight());
				if(curSelect){
					curShape = shape;
					outlineType = "ELLIPSE";
				}
			}
			else if(shape instanceof Circle){
				Circle circle = (Circle) shape;
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
			AffineTransform aT = objToView(curShape);
			g2d.setTransform(aT);
			switch(outlineType){
				case "LINE":
					Line line = (Line)curShape; 
					java.awt.geom.Point2D.Double start = line.getStart();
					java.awt.geom.Point2D.Double end = line.getEnd();
					
					g2d.drawRect((int)(start.getX()-(4/zoom)), (int)(start.getY()-(4/zoom)), (int)(8/zoom), (int)(8/zoom));
					g2d.drawRect((int)(end.getX()-(4/zoom)), (int)(end.getY()-(4/zoom)), (int)(8/zoom), (int)(8/zoom));
					
					g2d.drawLine((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY());
					break;
				case "SQUARE":
					Square square = (Square) curShape;
					java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(0-(square.getSize()/2), 0-(square.getSize()/2));
					g2d.drawRect((int)UL.getX(), (int)UL.getY(), (int)square.getSize(), (int)square.getSize());
					
					g2d.drawRect((int)(UL.getX()+square.getSize()/2-(4/zoom)), (int)(UL.getY()-(12/zoom)), (int)(8/zoom), (int)(8/zoom));
					
					break;
				case "RECT":
					Rectangle rect = (Rectangle) curShape;
					java.awt.geom.Point2D.Double upperLeft = new java.awt.geom.Point2D.Double(0-(rect.getWidth()/2), 0-(rect.getHeight()/2));
					g2d.drawRect((int)upperLeft.getX(), (int)upperLeft.getY(), (int)rect.getWidth(), (int)rect.getHeight());

					g2d.drawRect((int)(upperLeft.getX()+rect.getWidth()/2-(4/zoom)), (int)(upperLeft.getY()-(12/zoom)), (int)(8/zoom), (int)(8/zoom));
					
					break;
				case "CIRCLE":
					Circle circle = (Circle) curShape;
					g2d.drawRect(-(int)(circle.getRadius()/2), -(int)(circle.getRadius()/2), (int)circle.getRadius(), (int)circle.getRadius());
		
					g2d.drawRect((int)(-4/zoom), -(int)((circle.getRadius()/2) + (12/zoom)), (int)(8/zoom), (int)(8/zoom));
					
					break;
				case "ELLIPSE":
					Ellipse ellipse = (Ellipse) curShape;
					g2d.drawRect(-(int)(ellipse.getWidth()/2), -(int)ellipse.getHeight()/2, (int)ellipse.getWidth(), (int)ellipse.getHeight());
					
					g2d.drawRect((int)(-4/zoom), -(int)((ellipse.getHeight()/2) + (12/zoom)), (int)(8/zoom), (int)(8/zoom));
					
					break;
				case "TRI":
					Triangle tri = (Triangle) curShape;
					g2d.drawLine((int)(tri.getA().getX() + 0), (int)(tri.getA().getY() + 0), (int)(tri.getB().getX() + 0), (int)(tri.getB().getY() + 0));
					g2d.drawLine((int)(tri.getB().getX() + 0), (int)(tri.getB().getY() + 0), (int)(tri.getC().getX() + 0), (int)(tri.getC().getY() + 0));
					g2d.drawLine((int)(tri.getC().getX() + 0), (int)(tri.getC().getY() + 0), (int)(tri.getA().getX() + 0), (int)(tri.getA().getY() + 0));
					
					double xValue = (Math.min(Math.min(tri.getA().getY(), tri.getB().getY()), tri.getC().getY()) + Math.max(Math.max(tri.getA().getY(), tri.getB().getY()), tri.getC().getY()))/2 -(4/zoom);
					double yValue = Math.min(Math.min(tri.getA().getY(), tri.getB().getY()), tri.getC().getY())-(12/zoom);
					g2d.drawRect((int)xValue, (int)yValue, (int)(8/zoom), (int)(8/zoom));
					break;
			}
		}

	}

	private void drawImage(Graphics2D g2d) {
		AffineTransform affineTransform = worldToView();
		g2d.setTransform(affineTransform);
		g2d.drawImage(image.getImage(), null, 100, 100);
	}

	private void draw3d(Graphics2D g2d) {
	    	double[][] transformMatrix = {
	    			{1,0,0,-camera.x},
	    			{0,1,0,-camera.y},
	    			{0,0,1,-camera.z},
	    			{0,0,0,1}};
	    	
	    	double[][] rotationMatrix = {
	    			{Math.cos(rotation),0,Math.sin(rotation),0},
	    			{0,1,0,0},
	    			{-Math.sin(rotation),0,Math.cos(rotation),0},
	    			{0,0,0,1}};
	    	
	    	double[][] worldToCamera = multiplyMat(rotationMatrix, transformMatrix);
	    	
	    	double zoomY = 1/Math.tan(fov/2);
	    	double zoomX = (3*zoomY)/4;
	    	double clip1 = (farPlane+nearPlane)/(farPlane-nearPlane);
	    	double clip2 = (farPlane*nearPlane*-2)/(farPlane-nearPlane);
	    	
	    	double[][] clipMatrix = {
	    			{zoomX,0,0,0},
	    			{0,zoomY,0,0},
	    			{0,0,clip1,clip2},
	    			{0,0,1,0}};
	    	
	    	double[][] worldToClip = multiplyMat(clipMatrix, worldToCamera);
	    	
	    	int size = scene.instances().size();
	    	int shift = 0;
	    	for(int i = 0; i < size; i++){
	    		List<Line3D> lines = scene.instances().get(i).getModel().getLines();
	    		for(int j = 0; j < lines.size(); j++){
	    			Line3D line = lines.get(j);
	    			double[] startMatrix = {
	    					line.start.x + shift,line.start.y,line.start.z,1};
	    			double[] endMatrix = {
	    					line.end.x + shift,line.end.y,line.end.z,1};
	    			
	    			double[] startClip = matrixVec4(worldToClip,startMatrix);
	    			double[] endClip = matrixVec4(worldToClip,endMatrix);
	    			
	    			if(clipCheck(startClip) && clipCheck(endClip)){    		    						
	    				double[] normalStart = {startClip[0]/startClip[3], startClip[1]/startClip[3], startClip[2]/startClip[3], 1};
	    				double[] normalEnd = {endClip[0]/endClip[3], endClip[1]/endClip[3], endClip[2]/endClip[3], 1};
	    				double[] prepStartForScreen = {normalStart[0], normalStart[1], 1};
	    				double[] prepEndForScreen = {normalEnd[0], normalEnd[1], 1};
	    				
	    				double[][] screenMatrix = {
	    						{1024, 0, 1024},
	    						{0, -1024, 1024},
	    						{0, 0, 1}};
	    				
	    				double[] screenStart = matrixVec3(screenMatrix, prepStartForScreen);
	    				double[] screenEnd = matrixVec3(screenMatrix, prepEndForScreen);
	    				
	    				AffineTransform affineTransform = worldToView();
	    				g2d.setTransform(affineTransform);
	    				g2d.drawLine((int)screenStart[0], (int)screenStart[1], (int)screenEnd[0], (int)screenEnd[1]);
	    			}
	    		}		
	    		shift += 15;
	    	}
	}

	public void setView(java.awt.geom.Point2D.Double newView){
		viewPort = newView;
	}
	
	public void setZoom(double newZoom){
		zoom = newZoom;
	}
	
	public AffineTransform objToView(Shape curShape){
		AffineTransform aT = new AffineTransform();
		aT.concatenate(new AffineTransform(zoom,0,0,zoom,0,0));
		aT.concatenate(new AffineTransform(1,0,0,1,-viewPort.getX(),-viewPort.getY()));
		aT.concatenate(new AffineTransform(1,0,0,1,curShape.getCenter().getX(), curShape.getCenter().getY()));
		aT.concatenate(new AffineTransform(Math.cos(curShape.getRotation()), Math.sin(curShape.getRotation()), -Math.sin(curShape.getRotation()), Math.cos(curShape.getRotation()), 0,0));
		return aT;
	}
	
	public AffineTransform worldToView(){
		AffineTransform aT = new AffineTransform();
		aT.concatenate(new AffineTransform(zoom,0,0,zoom,0,0));
		aT.concatenate(new AffineTransform(1,0,0,1,-viewPort.getX(),-viewPort.getY()));
		return aT;
	}

	public void toggleThreeD(){
		ThreeD = !ThreeD;
	}
	
	public void toggleImage(){
		imageShow = !imageShow;
	}
	
	public void setCamera(Point3D newCam){
		camera = newCam;
	}
	
	public void setRotation(double newRot){
		rotation = newRot;
	}
	
	public void setScene(CS355Scene newScene){
		scene = newScene;
	}
	
	public void setImage(CustomImage newImage){
		image = newImage;
	}
	
	public double[][] multiplyMat(double[][] A, double[][] B){
		double[][] C = new double[4][4];
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				for (int k = 0; k < 4; k++){
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		return C;	
	}
	
	public double[] matrixVec4(double[][] A, double[] B){
		double[] C = new double[4];
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
					C[i] += A[i][j] * B[j];
			}
		}
		return C;
	}
	
	public double[] matrixVec3(double[][] A, double[] B){
		double[] C = new double[3];
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
					C[i] += A[i][j] * B[j];
			}
		}
		return C;
	}
	
	public boolean clipCheck(double[] A){
		if(-A[3] <= A[0] && A[0] <= A[3]){
			if(-A[3] <= A[1] && A[1] <= A[3]){
				if(-A[3] <= A[2] && A[2] <= A[3]){
					return true;
				}
			}
		}
		return false;
	}
}
