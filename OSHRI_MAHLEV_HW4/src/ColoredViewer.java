
import javafx.scene.paint.Color;

import javafx.application.Application;



public abstract class ColoredViewer extends Application implements IView {
	@Override
	public void init(){}; 

	 enum actionType{DEFAULT(Color.BLACK),NEXT(Color.GREEN),PREVIOUS(Color.RED),UPDATE(Color.BLUE);
		 private Color color;
		 actionType(Color color){
		 this.color=color;
	 
	 };
	 public Color getValue(){
		 return color;
	 }
	 }
	public abstract  void changeColor(actionType color);

	
	
		
	
	
	 

}
