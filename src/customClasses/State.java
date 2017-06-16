package customClasses;

public enum State {
	   WAITING(1),
	   LINE(2),
	   SQUARE(3),
	   RECTANGLE(4),
	   TRIANGLE1(5),
	   CIRCLE(6),
	   ELLIPSE(7),
	   TRIANGLE2(8),
	   TRIANGLE3(9),
	   SELECT(10),
	   ROTATE(11),
	   THREED(12),
	   IMAGE(13);
	
	   private int value;
	   
	   private State(int value) {
	      this.value = value;
	   }
	   
	   public void setState(int value) {
		      this.value = value;
	   }
	   public int getState() {
	      return value;
	   }
}
