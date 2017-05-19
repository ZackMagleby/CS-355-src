package customClasses;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.Year;
import java.util.Iterator;
import java.util.List;

import cs355.GUIFunctions;
import cs355.controller.CS355Controller;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class Controller implements CS355Controller {

	
	Color controllorColor;
	Model model;
	State curState;
	java.awt.Point origin;
	java.awt.Point firstPoint;
	java.awt.Point secondPoint;
	java.awt.Point thirdPoint;
	java.awt.Point click;
	java.awt.geom.Point2D.Double relativePoint;
	
	Shape curShape;
	boolean shapeSelected;
	int curShapeIndex;
	
	public Controller(Model uploadModel){
		controllorColor = Color.WHITE;
		model = uploadModel;
		shapeSelected = false;
		curShapeIndex = -1;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(curState == State.TRIANGLE1){
			firstPoint = e.getPoint();
			curState = State.TRIANGLE2;
			GUIFunctions.printf("CLICK SECOND POINT");
		}
		else if(curState == State.TRIANGLE2){
			secondPoint = e.getPoint();
			curState = State.TRIANGLE3;
			GUIFunctions.printf("CLICK THIRD POINT");
		}
		else if(curState == State.TRIANGLE3){
			thirdPoint = e.getPoint();
			
			double centerX = (firstPoint.getX() + secondPoint.getX() + thirdPoint.getX())/3;
			double centerY = (firstPoint.getY() + secondPoint.getY() + thirdPoint.getY())/3;
			java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(centerX, centerY);
			
			java.awt.geom.Point2D.Double one = new java.awt.geom.Point2D.Double(firstPoint.getX() - centerX, firstPoint.getY() - centerY);
			java.awt.geom.Point2D.Double two = new java.awt.geom.Point2D.Double(secondPoint.getX() - centerX, secondPoint.getY() - centerY);
			java.awt.geom.Point2D.Double three = new java.awt.geom.Point2D.Double(thirdPoint.getX() - centerX, thirdPoint.getY() - centerY);
			
//			java.awt.geom.Point2D.Double a;
//			java.awt.geom.Point2D.Double b;
//			java.awt.geom.Point2D.Double c;
//			
//			if(Math.min(Math.min(one.getX(), two.getX()), three.getX()) == one.getX()){
//				a = new java.awt.geom.Point2D.Double(one.getX(), one.getY());
//				if(Math.min(two.getX(), three.getX())== two.getX()){
//					b = new java.awt.geom.Point2D.Double(two.getX(), two.getY());
//					c = new java.awt.geom.Point2D.Double(three.getX(), three.getY());
//				}
//				else{
//					c = new java.awt.geom.Point2D.Double(two.getX(), two.getY());
//					b = new java.awt.geom.Point2D.Double(three.getX(), three.getY());					
//				}
//			}
//			else if(Math.min(Math.min(one.getX(), two.getX()), three.getX()) == two.getX()){
//				a = new java.awt.geom.Point2D.Double(two.getX(), two.getY());
//				if(Math.min(one.getX(), three.getX())== one.getX()){
//					b = new java.awt.geom.Point2D.Double(one.getX(), one.getY());
//					c = new java.awt.geom.Point2D.Double(three.getX(), three.getY());
//				}
//				else{
//					c = new java.awt.geom.Point2D.Double(one.getX(), one.getY());
//					b = new java.awt.geom.Point2D.Double(three.getX(), three.getY());					
//				}				
//			}
//			else{
//				a = new java.awt.geom.Point2D.Double(three.getX(), three.getY());
//				if(Math.min(two.getX(), one.getX())== one.getX()){
//					b = new java.awt.geom.Point2D.Double(one.getX(), one.getY());
//					c = new java.awt.geom.Point2D.Double(two.getX(), two.getY());
//				}
//				else{
//					c = new java.awt.geom.Point2D.Double(two.getX(), two.getY());
//					b = new java.awt.geom.Point2D.Double(one.getX(), one.getY());					
//				}				
//			}

			Triangle tri = new Triangle(controllorColor, center, one, two, three);
			model.addShape(tri);
			model.updateAll();
			curState = State.TRIANGLE1;
			GUIFunctions.printf("CLICK FIRST POINT");
		}

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
		if(curState != State.TRIANGLE1 || curState != State.TRIANGLE2 || curState != State.TRIANGLE3 || curState != State.SELECT){
			origin = e.getPoint();	
		}
		if(curState == State.SELECT){
			click = e.getPoint();
			curShape = findSelectedShape();
			if(curShape != null){
				relativePoint = new java.awt.geom.Point2D.Double(curShape.getCenter().getX(), curShape.getCenter().getY());				
			}
			return;
		}

		
		if(curState == State.LINE){
			java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double(origin.getX(), origin.getY());
			Line newLine = new Line(controllorColor, start, start);
			model.addShape(newLine);
		}
		else if(curState == State.RECTANGLE){
			java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double(origin.getX(), origin.getY());
			Rectangle rect = new Rectangle(controllorColor, start, 0, 0);
			model.addShape(rect);
		}
		else if(curState == State.ELLIPSE){
			java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double(origin.getX(), origin.getY());
			Ellipse ellipse = new Ellipse(controllorColor, start, 0, 0);
			model.addShape(ellipse);
		}
		else if(curState == State.SQUARE){
			java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double(origin.getX(), origin.getY());
			Square square = new Square(controllorColor, start, 0);
			model.addShape(square);
		}
		else if(curState == State.CIRCLE){
			java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double(origin.getX(), origin.getY());
			Circle circle = new Circle(controllorColor, start, 0);
			model.addShape(circle);			
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(curState == State.LINE){
			secondPoint = e.getPoint();
			java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(secondPoint.getX() - origin.getX(), secondPoint.getY() - origin.getY());
			Line newLine = (Line) model.getShape(model.getShapes().size()-1);
			newLine.setCenter(center);
			
			java.awt.geom.Point2D.Double end = new java.awt.geom.Point2D.Double((secondPoint.getX() - center.getX()), (secondPoint.getY()-center.getY()));
			newLine.setEnd(end);
			java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double((origin.getX() - center.getX()), (origin.getY()-center.getY()));
			newLine.setStart(start);
			model.updateAll();
		}
		else if(curState == State.RECTANGLE){
			secondPoint = e.getPoint();
			Rectangle newRect = (Rectangle) model.getShape(model.getShapes().size()-1);
			
			newRect.setWidth(Math.abs(origin.getX() - secondPoint.getX()));
			
			newRect.setHeight(Math.abs(origin.getY() - secondPoint.getY()));
			
			double upperLeftX = Math.min(origin.getX(), secondPoint.getX());
			double upperLeftY = Math.min(origin.getY(), secondPoint.getY());
			
			double midX = upperLeftX + (newRect.getWidth()/2);
			double midY = upperLeftY + (newRect.getHeight()/2);
			java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(midX, midY);
			
			newRect.setCenter(center);
			
			model.updateAll();
		}
		else if(curState == State.ELLIPSE){
			secondPoint = e.getPoint();
			Ellipse newEll = (Ellipse) model.getShape(model.getShapes().size()-1);
			
			newEll.setWidth(Math.abs(origin.getX() - secondPoint.getX()));
			
			newEll.setHeight(Math.abs(origin.getY() - secondPoint.getY()));
			
			double upperLeftX = Math.min(origin.getX(), secondPoint.getX());
			double upperLeftY = Math.min(origin.getY(), secondPoint.getY());
			java.awt.geom.Point2D.Double newcenter = new java.awt.geom.Point2D.Double(upperLeftX, upperLeftY);
			newEll.setCenter(newcenter);
			
			model.updateAll();
		}
		else if(curState == State.SQUARE){
			secondPoint = e.getPoint();
			Square square = (Square) model.getShape(model.getShapes().size()-1);

			//double length = Math.abs(Math.max(origin.getX() - secondPoint.getX() , origin.getY() - secondPoint.getY()));
			double length = Math.min(Math.abs(origin.getX() - secondPoint.getX()), Math.abs(origin.getY() - secondPoint.getY()));
			
			square.setSize(length);
			
			
			
			double upperLeftX = Math.min(origin.getX(), Math.max(secondPoint.getX(), origin.getX() - length));
			double upperLeftY = Math.min(origin.getY(), Math.max(secondPoint.getY(), origin.getY() - length));
			//java.awt.geom.Point2D.Double newUL = new java.awt.geom.Point2D.Double(upperLeftX, upperLeftY);
			
			double midX = upperLeftX + (square.getSize()/2);
			double midY = upperLeftY + (square.getSize()/2);
			java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(midX, midY);
			
			square.setCenter(center);
			
			model.updateAll();
		}
		else if(curState == State.CIRCLE){
			secondPoint = e.getPoint();
			Circle circle = (Circle) model.getShape(model.getShapes().size()-1);

			//double length = Math.abs(Math.max(origin.getX() - secondPoint.getX() , origin.getY() - secondPoint.getY()));
			double length = Math.min(Math.abs(origin.getX() - secondPoint.getX()), Math.abs(origin.getY() - secondPoint.getY()));
			
			circle.setRadius(length);
			
			double upperLeftX = Math.min(origin.getX(), Math.max(secondPoint.getX(), origin.getX() - length));
			double upperLeftY = Math.min(origin.getY(), Math.max(secondPoint.getY(), origin.getY() - length));
			java.awt.geom.Point2D.Double newUL = new java.awt.geom.Point2D.Double(upperLeftX, upperLeftY);
			circle.setCenter(newUL);

			model.updateAll();
		}
		else if(curState == State.SELECT){
			if(curShape != null){
				secondPoint = e.getPoint();
				java.awt.geom.Point2D.Double drag = new java.awt.geom.Point2D.Double(relativePoint.getX() + (secondPoint.getX()-click.getX()), relativePoint.getY() + (secondPoint.getY()-click.getY()));
				curShape.setCenter(drag);
				model.updateAll();				
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void colorButtonHit(Color c) {
		controllorColor = c;
		GUIFunctions.changeSelectedColor(c);
		if(curShape != null){
			curShape.setColor(c);
			model.updateAll();
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void lineButtonHit() {
		GUIFunctions.printf("");
		curState = State.LINE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void squareButtonHit() {
		GUIFunctions.printf("");
		curState = State.SQUARE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void rectangleButtonHit() {
		GUIFunctions.printf("");
		curState = State.RECTANGLE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void circleButtonHit() {
		GUIFunctions.printf("");
		curState = State.CIRCLE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void ellipseButtonHit() {
		GUIFunctions.printf("");
		curState = State.ELLIPSE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void triangleButtonHit() {
		curState = State.TRIANGLE1;
		GUIFunctions.printf("CLICK FIRST POINT");
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();

	}

	@Override
	public void selectButtonHit() {
		curState = State.SELECT;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void zoomInButtonHit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoomOutButtonHit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hScrollbarChanged(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vScrollbarChanged(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openScene(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggle3DModelDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(Iterator<Integer> iterator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openImage(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveImage(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleBackgroundDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveDrawing(File file) {
		model.save(file);

	}

	@Override
	public void openDrawing(File file) {
		model.open(file);
		model.updateAll();
	}

	@Override
	public void doDeleteShape() {
		if(curShape != null){
			model.deleteShape(curShapeIndex);
			model.updateAll();
		}

	}

	@Override
	public void doEdgeDetection() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSharpen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMedianBlur() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doUniformBlur() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doGrayscale() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doChangeContrast(int contrastAmountNum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMoveForward() {
		if(curShapeIndex < model.getShapes().size()){
			model.moveForward(curShapeIndex);
			curShapeIndex++;
			model.setShapeIndex(curShapeIndex);
			model.updateAll();			
		}
	}

	@Override
	public void doMoveBackward() {
		if(curShapeIndex > 0){
			model.moveBackward(curShapeIndex);
			curShapeIndex--;
			model.setShapeIndex(curShapeIndex);
			model.updateAll();			
		}
	}

	@Override
	public void doSendToFront() {
		model.moveToFront(curShapeIndex);
		curShapeIndex = model.getShapes().size();
		model.setShapeIndex(curShapeIndex);
		model.updateAll();
	}

	@Override
	public void doSendtoBack() {
		model.movetoBack(curShapeIndex);
		curShapeIndex = 0;
		model.setShapeIndex(curShapeIndex);
		model.updateAll();
	}
	
	private Shape findSelectedShape(){
		List<Shape> stackedShapes = model.getShapesReversed();
		Shape returnShape = null;
		for(int i = 0; i< stackedShapes.size(); i++){
			Shape s = stackedShapes.get(i);
			if(s instanceof Circle){
				Circle circle = (Circle) s;
				java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(circle.getCenter().getX()+(circle.getRadius()/2), circle.getCenter().getY()+(circle.getRadius()/2));
				double distX = click.getX() - center.getX();
				double distY = click.getY() - center.getY();
				double finalCompare = Math.pow(distX, 2) + Math.pow(distY, 2);
				if(finalCompare <= Math.pow(circle.getRadius()/2, 2)){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					GUIFunctions.printf("CIRCLE SELECTED");
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Square){
				Square square = (Square) s;
				java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(square.getCenter().getX()-(square.getSize()/2), square.getCenter().getY() - (square.getSize()/2));
				boolean horizCheck = (click.getX() > UL.getX()) && (click.getX() < (UL.getX()+square.getSize()));
				boolean vertCheck = (click.getY() > UL.getY()) && (click.getY() < (UL.getY() + square.getSize()));
				if(horizCheck && vertCheck){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					GUIFunctions.printf("SQUARE SELECTED");
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Ellipse){
				Ellipse ellipse = (Ellipse) s;
				java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(ellipse.getCenter().getX()+(ellipse.getWidth()/2), ellipse.getCenter().getY()+(ellipse.getHeight()/2));
				//java.awt.geom.Point2D.Double center = ellipse.getCenter();
				double a = ellipse.getWidth()/2;
				double b = ellipse.getHeight()/2;
				double checkX = (click.getX() - center.getX())/a;
				double checkY = (click.getY() - center.getY())/b;
				double finalCompare = Math.pow(checkX, 2) + Math.pow(checkY, 2);
				if(finalCompare <= 1){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					GUIFunctions.printf("ELLIPSE SELECTED");
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Rectangle){
				Rectangle rect = (Rectangle) s;
				java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(rect.getCenter().getX()-(rect.getWidth()/2), rect.getCenter().getY() - (rect.getHeight()/2));
				boolean horizCheck = (click.getX() > UL.getX()) && (click.getX() < (UL.getX()+rect.getWidth()));
				boolean vertCheck = (click.getY() > UL.getY()) && (click.getY() < (UL.getY() + rect.getHeight()));
				if(horizCheck && vertCheck){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					GUIFunctions.printf("RECTANGLE SELECTED");
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Line){
				//Slide 8 Parametric DIstance to Line
				Line line = (Line) s;
				java.awt.geom.Point2D.Double p0 = line.getStart();
				java.awt.geom.Point2D.Double p1 = line.getEnd();
				
				p0 = new java.awt.geom.Point2D.Double(p0.getX()+line.getCenter().getX(), p0.getY()+line.getCenter().getY());
				p1 = new java.awt.geom.Point2D.Double(p1.getX()+line.getCenter().getX(), p1.getY()+line.getCenter().getY());
				
				java.awt.geom.Point2D.Double numerator = new java.awt.geom.Point2D.Double((p1.getX() - p0.getX()), p1.getY() - p0.getY());
				
				//java.awt.geom.Point2D.Double denominator = new java.awt.geom.Point2D.Double(p1.getX()-p0.getX(), p1.getY() - p0.getY());
				double squared = Math.pow((p1.getX()-p0.getX()), 2) + Math.pow((p1.getY() - p0.getY()), 2);
				double root = Math.sqrt(squared);
				
				java.awt.geom.Point2D.Double d = new java.awt.geom.Point2D.Double(numerator.getX()/root, numerator.getY()/root);
				
				java.awt.geom.Point2D.Double minus = new java.awt.geom.Point2D.Double(click.getX() - p0.getX(), click.getY() - p0.getY());
				double inside = (d.getX() * minus.getX()) + (minus.getY() * d.getY());
				
				java.awt.geom.Point2D.Double oneMore = new java.awt.geom.Point2D.Double(d.getX()*inside, d.getY()*inside);
				java.awt.geom.Point2D.Double qPrime = new java.awt.geom.Point2D.Double(oneMore.getX()+p0.getX(), oneMore.getY()+p0.getY());
				
				double distance = Math.sqrt(Math.pow(click.getX() - qPrime.getX(), 2) + Math.pow(click.getY() - qPrime.getY(), 2));
				
				boolean horizCheck = (p0.getX() <= click.getX() && click.getX() <= p1.getX());
				boolean vertCheck = (p0.getY() <= click.getY() && click.getY() <= p1.getY());
				
				if(Math.abs(distance) <= 4 && horizCheck && vertCheck){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					GUIFunctions.printf("LINE SELECTED");
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Triangle){
				Triangle tri = (Triangle) s;
				java.awt.geom.Point2D.Double center = tri.getCenter();
				java.awt.geom.Point2D.Double A = new java.awt.geom.Point2D.Double(tri.getA().getX() + center.getX(), tri.getA().getY() + center.getY());
				java.awt.geom.Point2D.Double B = new java.awt.geom.Point2D.Double(tri.getB().getX() + center.getX(), tri.getB().getY() + center.getY());
				java.awt.geom.Point2D.Double C = new java.awt.geom.Point2D.Double(tri.getC().getX() + center.getX(), tri.getC().getY() + center.getY());
				//CHECK 1
				java.awt.geom.Point2D.Double trans = new java.awt.geom.Point2D.Double((-1) * (B.getY() - A.getY()), B.getX() - A.getX());
				java.awt.geom.Point2D.Double first = new java.awt.geom.Point2D.Double(click.getX() - A.getX(), click.getY() - A.getY());
				double check1 = (first.getX() * trans.getX()) + (first.getY() * trans.getY());
				//CHECK 2
				trans = new java.awt.geom.Point2D.Double((-1) * (C.getY() - B.getY()), C.getX() - B.getX());
				first = new java.awt.geom.Point2D.Double(click.getX() - B.getX(), click.getY() - B.getY());
				double check2 = (first.getX() * trans.getX()) + (first.getY() * trans.getY());
				//CHECK 3
				trans = new java.awt.geom.Point2D.Double((-1) * (A.getY() - C.getY()), A.getX() - C.getX());
				first = new java.awt.geom.Point2D.Double(click.getX() - C.getX(), click.getY() - C.getY());
				double check3 = (first.getX() * trans.getX()) + (first.getY() * trans.getY());
				
				if((check1 > 0 && check2 > 0 && check3 > 0) || (check1 < 0 && check2 < 0 && check3 < 0)){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					GUIFunctions.printf("TRIANGLE SELECTED");
					shapeSelected = true;
					break;
				}
			}
		}
		if(returnShape == null){
			shapeSelected = false;
			curShapeIndex = -1;
			model.setShapeIndex(curShapeIndex);
			curShape = null;
			GUIFunctions.printf("NO SHAPE SELECTED");
		}
		model.updateAll();
		return returnShape;
	}

}
