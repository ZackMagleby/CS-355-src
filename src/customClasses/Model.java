package customClasses;

import java.util.Collections;
import java.util.List;
import java.util.Observer;
import java.util.Vector;

import cs355.model.drawing.CS355Drawing;
import cs355.model.drawing.Shape;

public class Model extends CS355Drawing{

	Vector<Shape> shapeList;
	Vector<Observer> observers;
	int curShapeIndex;
	
	public Model() {
		shapeList = new Vector<Shape>();
		observers = new Vector<Observer>();
		curShapeIndex = -1;
	}
	
	@Override
	public Shape getShape(int index) {
		// TODO Auto-generated method stub
		return shapeList.get(index);
	}

	@Override
	public int addShape(Shape s) {
		shapeList.add(s);
		this.updateAll();
		return shapeList.size()-1;
	}

	@Override
	public void deleteShape(int index) {
		shapeList.remove(index);
		curShapeIndex = -1;
	}

	@Override
	public void moveToFront(int index) {
		Shape shape = shapeList.elementAt(index);
		shapeList.remove(index);
		shapeList.add(shape);
	}

	@Override
	public void movetoBack(int index) {
		Shape shape = shapeList.elementAt(index);
		shapeList.remove(index);
		shapeList.add(0, shape);
	}

	@Override
	public void moveBackward(int index) {
		if(shapeList.size() > 2 && index < shapeList.size()){
			if(index > 0){
				Shape shape = shapeList.get(index);
				shapeList.set(index, shapeList.get(index-1));
				shapeList.set(index-1, shape);						
			}
		}
	}

	@Override
	public void moveForward(int index) {
		if(shapeList.size()>2 && index < shapeList.size()){
			if(index < shapeList.size()){
				Shape shape = shapeList.get(index);
				shapeList.set(index, shapeList.get(index+1));
				shapeList.set(index+1, shape);						
			}
		}
	}

	@Override
	public List<Shape> getShapes() {
		// TODO Auto-generated method stub
		return shapeList;
	}

	@Override
	public List<Shape> getShapesReversed() {
		@SuppressWarnings("unchecked")
		Vector<Shape> tempList = (Vector<Shape>) shapeList.clone(); 
		Collections.reverse(tempList);
		return tempList;
	}

	@Override
	public void setShapes(List<Shape> shapes) {
		Vector<Shape> newShapes = new Vector<Shape>(shapes);
		shapeList = newShapes;
	}
	
	public void addObserver(Observer name){
		observers.add(name);
	}
	
	public void updateAll(){
		for(int i = 0; i < observers.size(); i++){
			observers.get(i).update(null, null);
		}
	}

	public void setShapeIndex(int index){
		curShapeIndex = index;
	}
	
	public int getShapeIndex(){
		return curShapeIndex;
	}
}
