package cs355.LWJGL;


//You might notice a lot of imports here.
//You are probably wondering why I didn't just import org.lwjgl.opengl.GL11.*
//Well, I did it as a hint to you.
//OpenGL has a lot of commands, and it can be kind of intimidating.
//This is a list of all the commands I used when I implemented my project.
//Therefore, if a command appears in this list, you probably need it.
//If it doesn't appear in this list, you probably don't.
//Of course, your milage may vary. Don't feel restricted by this list of imports.
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;//
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;//
import static org.lwjgl.opengl.GL11.glEnd;//
import static org.lwjgl.opengl.GL11.glLoadIdentity;//
import static org.lwjgl.opengl.GL11.glMatrixMode;//
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;//
import static org.lwjgl.opengl.GL11.glTranslated;//
import static org.lwjgl.opengl.GL11.glVertex3d;//
import static org.lwjgl.opengl.GL11.glViewport;//
import static org.lwjgl.opengl.GL11.glOrtho;
import org.lwjgl.util.glu.GLU;


import java.util.Iterator;

/**
 *
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController 
{
  
  //This is a model of a house.
  //It has a single method that returns an iterator full of Line3Ds.
  //A "Line3D" is a wrapper class around two Point2Ds.
  //It should all be fairly intuitive if you look at those classes.
  //If not, I apologize.
  private WireFrame model = new HouseModel();
  
  Point3D cameraPos = new Point3D(0, -2.5, -10);
  //Point3D cameraPos = new Point3D(0, 0, 0);
  Double rotation = 0.0;
  int mode = 1;
  int farPlane = 1000;
  int nearPlane = 0;
  final double fov = 90.0;
  LWJGLSandbox sandbox = new LWJGLSandbox();
  
  float[][] colors = {{0f, 0f, 1f},
		  {0.2f, 0f, 0.8f},
		  {0.4f, 0f, 0.6f},
		  {0.6f, 0f, 0.4f},
		  {0.8f, .1f, 0.2f},
		  {1f, 1f, 0f}};

  //This method is called to "resize" the viewport to match the screen.
  //When you first start, have it be in perspective mode.
  @Override
  public void resizeGL() 
  {
	  glMatrixMode(GL_PROJECTION);
	  glViewport((int)cameraPos.x, (int)cameraPos.y, sandbox.DISPLAY_WIDTH, sandbox.DISPLAY_HEIGHT);
	  mode = 1;
	  GLU.gluPerspective((float)fov, (float)4/3, (float)nearPlane, (float)farPlane);
	  glColor3f(0.0f, 1.0f, 0.0f);
	  
  }

    @Override
    public void update() 
    {
		//update data as needed
    }

    //This is called every frame, and should be responsible for keyboard updates.
    //An example keyboard event is captured below.
    //The "Keyboard" static class should contain everything you need to finish
    // this up.
    @Override
    public void updateKeyboard() 
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) 
        {
            //System.out.println("You are pressing W!");
            cameraPos.z += Math.cos(Math.toRadians(rotation))/5;
            cameraPos.x -= Math.sin(Math.toRadians(rotation))/5;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_S)) 
        {
            //System.out.println("You are pressing S!");
        	cameraPos.z -= Math.cos(Math.toRadians(rotation))/5;
            cameraPos.x += Math.sin(Math.toRadians(rotation))/5;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_A)) 
        {
            //System.out.println("You are pressing A!");
        	cameraPos.z -= Math.cos(Math.toRadians(rotation+90))/5 ;
            cameraPos.x += Math.sin(Math.toRadians(rotation+90))/5 ;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_D)) 
        {
            //System.out.println("You are pressing D!");
        	cameraPos.z += Math.cos(Math.toRadians(rotation+90))/5 ;
            cameraPos.x -= Math.sin(Math.toRadians(rotation+90))/5 ;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_Q)) 
        {
            //System.out.println("You are pressing Q!");
            rotation -= 1;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_E)) 
        {
           // System.out.println("You are pressing E!");
            rotation += 1;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_R)) 
        {
            //System.out.println("You are pressing R!");
            cameraPos.y -= .1;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_F)) 
        {
            //System.out.println("You are pressing F!");
            cameraPos.y += .1;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_P)) 
        {
           // System.out.println("You are pressing P!");
            mode = 1;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_O)) 
        {
           //System.out.println("You are pressing O!");
            mode = 0;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_H)) 
        {
        	cameraPos = new Point3D(0, -2.5, -10);
        	rotation = 0.0;
        }
    }

    //This method is the one that actually draws to the screen.
    @Override
    public void render() 
    {
        //This clears the screen.
        glClear(GL_COLOR_BUFFER_BIT);
        setMatrices();
    }
    
    
    public void setMatrices(){
    	
    	glMatrixMode(GL_PROJECTION);
    	glLoadIdentity();  

    	if(mode == 0){
    		glOrtho(-20, 20, -20, 20, nearPlane, farPlane);
    	}
    	else{
    		GLU.gluPerspective((float)fov, (float)4/3, (float)nearPlane, (float)farPlane);
    	}
    	
    	glMatrixMode(GL_MODELVIEW);
    	glLoadIdentity();  
    	glRotated(rotation, 0, 1, 0);
    	glTranslated(cameraPos.x, cameraPos.y, cameraPos.z);
    	glPushMatrix();
    	
    	drawLines();
    	
    	GL11.glPopMatrix();
    	
    	glMatrixMode(GL_PROJECTION);
    }
    
    public void drawLines(){
    	//Do your drawing here.
    	glBegin(GL_LINES);
    	int shift = 0;
    	int otherSide = 0;
    	for(int i = 0; i<=5; i++){
    		float[] curColor = colors[i];
	    	Iterator<Line3D> lines = model.getLines();
	    	
	    	glColor3f(curColor[0], curColor[1], curColor[2]);
	    	
	    	if(i==3){
	    		otherSide = 30;
	    		shift = 0;
	    		glRotated(180, 0, 1, 0);
	    	}
    		while(lines.hasNext()){
    			Line3D line = lines.next();
    			Point3D start = line.start;
    			Point3D end = line.end;
    			
    			
    			glVertex3d(start.x + shift, start.y, start.z + otherSide);
    			glVertex3d(end.x + shift, end.y, end.z + otherSide);
    		}		
	    	shift += 15;
	    	//otherSide += otherSide;
    	}
    	
    	glEnd();
    }
}
