package cs355.solution;

import java.awt.Color;

import cs355.GUIFunctions;
import customClasses.Controller;
import customClasses.Model;
import customClasses.View;

/**
 * This is the main class. The program starts here.
 * Make you add code below to initialize your model,
 * view, and controller and give them to the app.
 */
public class CS355 {

	/**
	 * This is where it starts.
	 * @param args = the command line arguments
	 */
	public static void main(String[] args) {
		
		Model freshModel = new Model();
		
		View viewUse = new View(freshModel);
		Controller controllerUse = new Controller(freshModel, viewUse);
		
		freshModel.addObserver(viewUse);

		// Fill in the parameters below with your controller and view.
		GUIFunctions.createCS355Frame(controllerUse, viewUse);
		GUIFunctions.changeSelectedColor(Color.WHITE);
		GUIFunctions.setHScrollBarMin(0);
		GUIFunctions.setVScrollBarMin(0);
		GUIFunctions.setHScrollBarMax(500);
		GUIFunctions.setVScrollBarMax(500);
		GUIFunctions.refresh();
	}
}
