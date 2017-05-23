package customClasses;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
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
	View view;
	State curState;
	java.awt.Point origin;
	java.awt.Point firstPoint;
	java.awt.Point secondPoint;
	java.awt.Point thirdPoint;
	java.awt.Point click;
	java.awt.geom.Point2D.Double relativePoint;
	int viewX;
	int viewY;
	
	Shape curShape;
	boolean shapeSelected;
	boolean lineStartEdit;
	boolean lineEndEdit;
	int curShapeIndex;
	double zoom;
	
	public Controller(Model uploadModel, View uploadView){
		controllorColor = Color.WHITE;
		model = uploadModel;
		view = uploadView;
		zoom = 1;
		shapeSelected = false;
		curShapeIndex = -1;
		lineStartEdit = false;
		lineEndEdit = false;
		viewX = 0;
		viewY = 0;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(curState == State.TRIANGLE1){
			firstPoint = e.getPoint();
				AffineTransform aT = viewToWorld();
				aT.transform(firstPoint, firstPoint);
			curState = State.TRIANGLE2;
		}
		else if(curState == State.TRIANGLE2){
			secondPoint = e.getPoint();
			
			AffineTransform aT = viewToWorld();
			aT.transform(secondPoint, secondPoint);
			
			curState = State.TRIANGLE3;
		}
		else if(curState == State.TRIANGLE3){
			thirdPoint = e.getPoint();
			
			AffineTransform aT = viewToWorld();
			aT.transform(thirdPoint, thirdPoint);
			
			double centerX = (firstPoint.getX() + secondPoint.getX() + thirdPoint.getX())/3;
			double centerY = (firstPoint.getY() + secondPoint.getY() + thirdPoint.getY())/3;
			java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(centerX, centerY);
			
			java.awt.geom.Point2D.Double one = new java.awt.geom.Point2D.Double(firstPoint.getX() - centerX, firstPoint.getY() - centerY);
			java.awt.geom.Point2D.Double two = new java.awt.geom.Point2D.Double(secondPoint.getX() - centerX, secondPoint.getY() - centerY);
			java.awt.geom.Point2D.Double three = new java.awt.geom.Point2D.Double(thirdPoint.getX() - centerX, thirdPoint.getY() - centerY);

			Triangle tri = new Triangle(controllorColor, center, one, two, three);
			model.addShape(tri);
			model.updateAll();
			curState = State.TRIANGLE1;
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
			AffineTransform aT = viewToWorld();
			aT.transform(origin, origin);
		}
		if(curState == State.SELECT){
			click = e.getPoint();
			AffineTransform aT = viewToWorld();
			aT.transform(click, click);

			lineStartEdit = false;
			lineEndEdit = false;	
			if(curShape instanceof Line){
				Line line = (Line)curShape;

				java.awt.geom.Point2D.Double start = line.getStart();
				java.awt.geom.Point2D.Double end = line.getEnd();
				java.awt.geom.Point2D.Double relativeClick = new java.awt.geom.Point2D.Double(click.getX() - line.getCenter().getX(), click.getY() - line.getCenter().getY());
				
				boolean check1 = (relativeClick.getX() < start.getX() + 4/zoom);
				boolean check2 = (relativeClick.getX() > start.getX() - 4/zoom);
				boolean check3 = (relativeClick.getY() < start.getY() + 4/zoom);
				boolean check4 = (relativeClick.getY() > start.getY() - 4/zoom);
				
				boolean check5 = (relativeClick.getX() > end.getX() - 4/zoom);
				boolean check6 = (relativeClick.getX() < end.getX() + 4/zoom);
				boolean check7 = (relativeClick.getY() > end.getY() - 4/zoom);
				boolean check8 = (relativeClick.getY() < end.getY() + 4/zoom);
				
				if(check1 && check2 && check3 && check4){
					lineStartEdit = true;
				}
				if(check5 && check6 && check7 && check8){
					lineEndEdit = true;
				}
			}
			
			boolean clickedInBox = handleCheck();
			if(clickedInBox){
				curState = State.ROTATE;

			}
			else{
				curShape = findSelectedShape();
				if(curShape != null){
					controllorColor = curShape.getColor();
					GUIFunctions.changeSelectedColor(curShape.getColor());									
				}
				else{
					controllorColor = Color.WHITE;
					GUIFunctions.changeSelectedColor(Color.WHITE);
				}
			}

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

	private boolean handleCheck() {
		if(curShape == null){
			return false;
		}
		else{
			java.awt.geom.Point2D.Double transformedClick = new java.awt.geom.Point2D.Double(0,0);
			AffineTransform aT = worldToObj(curShape);
			aT.transform(click, transformedClick);
			
			if(curShape instanceof Triangle){
				Triangle tri = (Triangle)curShape;
				double xValue = (Math.min(Math.min(tri.getA().getY(), tri.getB().getY()), tri.getC().getY()) + Math.max(Math.max(tri.getA().getY(), tri.getB().getY()), tri.getC().getY()))/2 -(4/zoom);
				double yValue = Math.min(Math.min(tri.getA().getY(), tri.getB().getY()), tri.getC().getY())-(12/zoom);
				
				java.awt.geom.Point2D.Double relativeClick = new java.awt.geom.Point2D.Double(transformedClick.getX(), transformedClick.getY());
				
				if(((relativeClick.getX() <= xValue+(8/zoom)) && (relativeClick.getX() >= xValue)) && ((relativeClick.getY() <= yValue+(8/zoom)) && (relativeClick.getY() >= yValue))){
					return true;
				}
				else{
					return false;
				}
			}
			else if(!(curShape instanceof Line)){
				double xValue = -4/zoom;
				double yValue = 0;
				if(curShape instanceof Square){
					yValue = -((Square) curShape).getSize()/2 - (12/zoom);
				}
				else if(curShape instanceof Rectangle){
					yValue = 0 -((Rectangle) curShape).getHeight()/2 - (12/zoom);
				}
				else if(curShape instanceof Circle){
					yValue = -(((Circle) curShape).getRadius()/2) - (12/zoom);
				}
				else if(curShape instanceof Ellipse){
					yValue = -(((Ellipse) curShape).getHeight()/2) - (12/zoom);
				}
							
				if(((transformedClick.getX() <= xValue+(8/zoom)) && (transformedClick.getX() >= xValue)) && ((transformedClick.getY() <= yValue+(8/zoom)) && (transformedClick.getY() >= yValue)) && !(curShape instanceof Line)){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(curState == State.ROTATE){
			curState = State.SELECT;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {		
		secondPoint = e.getPoint();
		AffineTransform aT = viewToWorld();
		aT.transform(secondPoint, secondPoint);
		if(curState == State.ROTATE){
			
			java.awt.geom.Point2D.Double u = new java.awt.geom.Point2D.Double(click.getX() - curShape.getCenter().getX(), click.getY() - curShape.getCenter().getY());
			java.awt.geom.Point2D.Double v = new java.awt.geom.Point2D.Double(secondPoint.getX() - curShape.getCenter().getX(), secondPoint.getY() - curShape.getCenter().getY());

			double angle = Math.atan2(v.getY(), v.getX()) + Math.PI/2;// - Math.atan2(u.getY(), u.getX());
			curShape.setRotation(angle);
						
			model.updateAll();
		}
		else if(curState == State.LINE){
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
			Ellipse newEll = (Ellipse) model.getShape(model.getShapes().size()-1);
			
			newEll.setWidth(Math.abs(origin.getX() - secondPoint.getX()));
			
			newEll.setHeight(Math.abs(origin.getY() - secondPoint.getY()));
			
			double upperLeftX = Math.min(origin.getX(), secondPoint.getX());
			double upperLeftY = Math.min(origin.getY(), secondPoint.getY());
			java.awt.geom.Point2D.Double newcenter = new java.awt.geom.Point2D.Double(upperLeftX + newEll.getWidth()/2, upperLeftY + newEll.getHeight()/2);
			newEll.setCenter(newcenter);
			
			model.updateAll();
		}
		else if(curState == State.SQUARE){
			Square square = (Square) model.getShape(model.getShapes().size()-1);

			double length = Math.min(Math.abs(origin.getX() - secondPoint.getX()), Math.abs(origin.getY() - secondPoint.getY()));
			
			square.setSize(length);
			
			
			
			double upperLeftX = Math.min(origin.getX(), Math.max(secondPoint.getX(), origin.getX() - length));
			double upperLeftY = Math.min(origin.getY(), Math.max(secondPoint.getY(), origin.getY() - length));
			
			double midX = upperLeftX + (square.getSize()/2);
			double midY = upperLeftY + (square.getSize()/2);
			java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(midX, midY);
			
			square.setCenter(center);
			
			model.updateAll();
		}
		else if(curState == State.CIRCLE){
			Circle circle = (Circle) model.getShape(model.getShapes().size()-1);

			double length = Math.min(Math.abs(origin.getX() - secondPoint.getX()), Math.abs(origin.getY() - secondPoint.getY()));
			
			circle.setRadius(length);
			
			double upperLeftX = Math.min(origin.getX(), Math.max(secondPoint.getX(), origin.getX() - length));
			double upperLeftY = Math.min(origin.getY(), Math.max(secondPoint.getY(), origin.getY() - length));
			java.awt.geom.Point2D.Double newUL = new java.awt.geom.Point2D.Double(upperLeftX + circle.getRadius()/2, upperLeftY + circle.getRadius()/2);
			circle.setCenter(newUL);

			model.updateAll();
		}
		else if(curState == State.SELECT){
			if(curShape != null){
				if(lineStartEdit){
					Line newLine = (Line) model.getShape(curShapeIndex);
					
					java.awt.geom.Point2D.Double oldEnd = new java.awt.geom.Point2D.Double(newLine.getEnd().getX() + newLine.getCenter().getX(), newLine.getEnd().getY() + newLine.getCenter().getY());
					java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double((newLine.getEnd().getX() + secondPoint.getX())/2, (newLine.getEnd().getY() + secondPoint.getY())/2);
					
					newLine.setCenter(center);
					
					java.awt.geom.Point2D.Double end = new java.awt.geom.Point2D.Double((oldEnd.getX() - center.getX()), (oldEnd.getY()-center.getY()));
					newLine.setEnd(end);
					java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double((secondPoint.getX() - center.getX()), (secondPoint.getY()-center.getY()));
					newLine.setStart(start);
					model.updateAll();
				}
				else if(lineEndEdit){
					Line newLine = (Line) model.getShape(curShapeIndex);
					
					java.awt.geom.Point2D.Double oldStart = new java.awt.geom.Point2D.Double(newLine.getStart().getX() + newLine.getCenter().getX(), newLine.getStart().getY() + newLine.getCenter().getY());
					java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double((newLine.getStart().getX() + secondPoint.getX())/2, (newLine.getStart().getY() + secondPoint.getY())/2);
					
					newLine.setCenter(center);
					
					java.awt.geom.Point2D.Double start = new java.awt.geom.Point2D.Double((oldStart.getX() - center.getX()), (oldStart.getY()-center.getY()));
					newLine.setStart(start);
					java.awt.geom.Point2D.Double end = new java.awt.geom.Point2D.Double((secondPoint.getX() - center.getX()), (secondPoint.getY()-center.getY()));
					newLine.setEnd(end);
					model.updateAll();
				}
				else{
					java.awt.geom.Point2D.Double drag = new java.awt.geom.Point2D.Double(relativePoint.getX() + (secondPoint.getX()-click.getX()), relativePoint.getY() + (secondPoint.getY()-click.getY()));
					curShape.setCenter(drag);
					model.updateAll();					
				}
				
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
		curState = State.LINE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void squareButtonHit() {
		curState = State.SQUARE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void rectangleButtonHit() {
		curState = State.RECTANGLE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void circleButtonHit() {
		curState = State.CIRCLE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void ellipseButtonHit() {
		curState = State.ELLIPSE;
		curShape = null;
		curShapeIndex = -1;
		model.setShapeIndex(-1);
		model.updateAll();
	}

	@Override
	public void triangleButtonHit() {
		curState = State.TRIANGLE1;
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
		if(zoom == .25){
			GUIFunctions.setHScrollBarKnob(1000);
			GUIFunctions.setVScrollBarKnob(1000);
			GUIFunctions.setHScrollBarPosit(0);
			GUIFunctions.setVScrollBarPosit(0);
			zoom = .5;
		}
		else if(zoom == .5){
			GUIFunctions.setHScrollBarKnob(500);
			GUIFunctions.setVScrollBarKnob(500);
			GUIFunctions.setHScrollBarPosit(0);
			GUIFunctions.setVScrollBarPosit(0);
			zoom = 1;
		}
		else if(zoom == 1){
			GUIFunctions.setHScrollBarKnob(250);
			GUIFunctions.setVScrollBarKnob(250);
			GUIFunctions.setHScrollBarPosit(0);
			GUIFunctions.setVScrollBarPosit(0);
			zoom = 2;
		}
		else if(zoom == 2){
			GUIFunctions.setHScrollBarKnob(125);
			GUIFunctions.setVScrollBarKnob(125);
			GUIFunctions.setHScrollBarPosit(0);
			GUIFunctions.setVScrollBarPosit(0);
			zoom = 4;
		}
		GUIFunctions.printf("Zoom Level: " + (zoom*100) + "%%");
		view.setZoom(zoom);
		model.updateAll();
	}

	@Override
	public void zoomOutButtonHit() {
		if(zoom == .5){
			GUIFunctions.setHScrollBarKnob(2048);
			GUIFunctions.setVScrollBarKnob(2048);
			GUIFunctions.setHScrollBarPosit(0);
			GUIFunctions.setVScrollBarPosit(0);
			viewX = 0;
			viewY = 0;
			zoom = .25;
		}
		else if(zoom == 1){
			GUIFunctions.setHScrollBarKnob(1000);
			GUIFunctions.setVScrollBarKnob(1000);
			GUIFunctions.setHScrollBarPosit(0);
			GUIFunctions.setVScrollBarPosit(0);
			zoom = .5;
		}
		else if(zoom == 2){
			GUIFunctions.setHScrollBarKnob(500);
			GUIFunctions.setVScrollBarKnob(500);
			GUIFunctions.setHScrollBarPosit(0);
			GUIFunctions.setVScrollBarPosit(0);
			zoom = 1;
		}
		else if(zoom == 4){
			GUIFunctions.setHScrollBarKnob(250);
			GUIFunctions.setVScrollBarKnob(250);
			GUIFunctions.setHScrollBarPosit(0);
			GUIFunctions.setVScrollBarPosit(0);
			zoom = 2;
		}
		GUIFunctions.printf("Zoom Level: " + Double.toString((zoom*100)) + "%%");
		view.setZoom(zoom);
		model.updateAll();
	}

	@Override
	public void hScrollbarChanged(int value) {
		viewX = value;
		updateView();
	}

	@Override
	public void vScrollbarChanged(int value) {
		viewY = value;
		updateView();
	}
	
	public void updateView(){
		view.setView(new java.awt.geom.Point2D.Double(viewX, viewY));
		model.updateAll();
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
		curShapeIndex = model.getShapes().size()-1;
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
		if(lineEndEdit || lineStartEdit){
			return curShape;
		}
		List<Shape> stackedShapes = model.getShapesReversed();
		Shape returnShape = null;
		for(int i = 0; i< stackedShapes.size(); i++){
			Shape s = stackedShapes.get(i);
			if(s instanceof Circle){
				Circle circle = (Circle) s;
				
				java.awt.geom.Point2D.Double transformedClick = new java.awt.geom.Point2D.Double(0,0);
				AffineTransform aT = worldToObj(s);
				aT.transform(click, transformedClick);
				
				double finalCompare = Math.pow(transformedClick.getX(), 2) + Math.pow(transformedClick.getY(), 2);
				if(finalCompare <= Math.pow(circle.getRadius()/2, 2)){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Square){
				Square square = (Square) s;
				
				java.awt.geom.Point2D.Double transformedClick = new java.awt.geom.Point2D.Double(0,0);
				AffineTransform aT = worldToObj(s);
				aT.transform(click, transformedClick);
				
				java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(-(square.getSize()/2), -(square.getSize()/2));
				boolean horizCheck = (transformedClick.getX() > UL.getX()) && (transformedClick.getX() < (UL.getX()+square.getSize()));
				boolean vertCheck = (transformedClick.getY() > UL.getY()) && (transformedClick.getY() < (UL.getY() + square.getSize()));
				if(horizCheck && vertCheck){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Ellipse){
				Ellipse ellipse = (Ellipse) s;
				java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(0, 0);
				
				java.awt.geom.Point2D.Double transformedClick = new java.awt.geom.Point2D.Double(0,0);
				AffineTransform aT = worldToObj(s);
				aT.transform(click, transformedClick);
				
				double a = ellipse.getWidth()/2;
				double b = ellipse.getHeight()/2;
				double checkX = (transformedClick.getX() - center.getX())/a;
				double checkY = (transformedClick.getY() - center.getY())/b;
				double finalCompare = Math.pow(checkX, 2) + Math.pow(checkY, 2);
				if(finalCompare <= 1){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Rectangle){
				Rectangle rect = (Rectangle) s;
				
				java.awt.geom.Point2D.Double transformedClick = new java.awt.geom.Point2D.Double(0,0);
				AffineTransform aT = worldToObj(s);
				aT.transform(click, transformedClick);
					
				java.awt.geom.Point2D.Double UL = new java.awt.geom.Point2D.Double(-(rect.getWidth()/2), -(rect.getHeight()/2));
				boolean horizCheck = (transformedClick.getX() > UL.getX()) && (transformedClick.getX() < (UL.getX()+rect.getWidth()));
				boolean vertCheck = (transformedClick.getY() > UL.getY()) && (transformedClick.getY() < (UL.getY() + rect.getHeight()));
				if(horizCheck && vertCheck){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Line){
				Line line = (Line) s;
				java.awt.geom.Point2D.Double p0 = line.getStart();
				java.awt.geom.Point2D.Double p1 = line.getEnd();
				
				java.awt.geom.Point2D.Double transformedClick = new java.awt.geom.Point2D.Double(0,0);
				AffineTransform aT = worldToObj(s);
				aT.transform(click, transformedClick);
				
				p0 = new java.awt.geom.Point2D.Double(p0.getX()+0, p0.getY()+0);
				p1 = new java.awt.geom.Point2D.Double(p1.getX()+0, p1.getY()+0);
				
				java.awt.geom.Point2D.Double numerator = new java.awt.geom.Point2D.Double((p1.getX() - p0.getX()), p1.getY() - p0.getY());

				double squared = Math.pow((p1.getX()-p0.getX()), 2) + Math.pow((p1.getY() - p0.getY()), 2);
				double root = Math.sqrt(squared);
				
				java.awt.geom.Point2D.Double d = new java.awt.geom.Point2D.Double(numerator.getX()/root, numerator.getY()/root);
				
				java.awt.geom.Point2D.Double minus = new java.awt.geom.Point2D.Double(transformedClick.getX() - p0.getX(), transformedClick.getY() - p0.getY());
				double inside = (d.getX() * minus.getX()) + (minus.getY() * d.getY());
				
				java.awt.geom.Point2D.Double oneMore = new java.awt.geom.Point2D.Double(d.getX()*inside, d.getY()*inside);
				java.awt.geom.Point2D.Double qPrime = new java.awt.geom.Point2D.Double(oneMore.getX()+p0.getX(), oneMore.getY()+p0.getY());
				
				double distance = Math.sqrt(Math.pow(transformedClick.getX() - qPrime.getX(), 2) + Math.pow(transformedClick.getY() - qPrime.getY(), 2));
				
				boolean horizCheck = (Math.min(p0.getX(), p1.getX()) <= transformedClick.getX() && transformedClick.getX() <= Math.max(p0.getX(), p1.getX()));
				boolean vertCheck = (Math.min(p0.getY(), p1.getY()) <= transformedClick.getY() && transformedClick.getY() <= Math.max(p0.getY(), p1.getY()));
				
				if(Math.abs(distance) <= 4 && horizCheck && vertCheck){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
					shapeSelected = true;
					break;
				}
			}
			else if(s instanceof Triangle){
				Triangle tri = (Triangle) s;
				
				java.awt.geom.Point2D.Double transformedClick = new java.awt.geom.Point2D.Double(0,0);
				AffineTransform aT = worldToObj(s);
				aT.transform(click, transformedClick);
				
				java.awt.geom.Point2D.Double center = new java.awt.geom.Point2D.Double(0,0);
				java.awt.geom.Point2D.Double A = new java.awt.geom.Point2D.Double(tri.getA().getX() + center.getX(), tri.getA().getY() + center.getY());
				java.awt.geom.Point2D.Double B = new java.awt.geom.Point2D.Double(tri.getB().getX() + center.getX(), tri.getB().getY() + center.getY());
				java.awt.geom.Point2D.Double C = new java.awt.geom.Point2D.Double(tri.getC().getX() + center.getX(), tri.getC().getY() + center.getY());
				//CHECK 1
				java.awt.geom.Point2D.Double trans = new java.awt.geom.Point2D.Double((-1) * (B.getY() - A.getY()), B.getX() - A.getX());
				java.awt.geom.Point2D.Double first = new java.awt.geom.Point2D.Double(transformedClick.getX() - A.getX(), transformedClick.getY() - A.getY());
				double check1 = (first.getX() * trans.getX()) + (first.getY() * trans.getY());
				//CHECK 2
				trans = new java.awt.geom.Point2D.Double((-1) * (C.getY() - B.getY()), C.getX() - B.getX());
				first = new java.awt.geom.Point2D.Double(transformedClick.getX() - B.getX(), transformedClick.getY() - B.getY());
				double check2 = (first.getX() * trans.getX()) + (first.getY() * trans.getY());
				//CHECK 3
				trans = new java.awt.geom.Point2D.Double((-1) * (A.getY() - C.getY()), A.getX() - C.getX());
				first = new java.awt.geom.Point2D.Double(transformedClick.getX() - C.getX(), transformedClick.getY() - C.getY());
				double check3 = (first.getX() * trans.getX()) + (first.getY() * trans.getY());
				
				if((check1 > 0 && check2 > 0 && check3 > 0) || (check1 < 0 && check2 < 0 && check3 < 0)){
					returnShape = s;
					curShapeIndex = stackedShapes.size() -1 - i;
					model.setShapeIndex(curShapeIndex);
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
		}
		model.updateAll();
		return returnShape;
	}
	
	public AffineTransform viewToWorld(){
		AffineTransform aT = new AffineTransform();
		aT.concatenate(new AffineTransform(1,0,0,1,viewX,viewY));
		aT.concatenate(new AffineTransform(1/zoom,0,0,1/zoom,0,0));
		return aT;
	}
	
	public AffineTransform worldToObj(Shape curShape){
		AffineTransform aT = new AffineTransform();
		aT.concatenate(new AffineTransform(Math.cos(curShape.getRotation()), -Math.sin(curShape.getRotation()), Math.sin(curShape.getRotation()), Math.cos(curShape.getRotation()), 0,0));
		aT.concatenate(new AffineTransform(1,0,0,1,-curShape.getCenter().getX(), -curShape.getCenter().getY()));
		return aT;
	}
}
