//oshri mahlev 204004055

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class ID204004055  extends Application {

	public static void main(String[] args){
		launch(args);	
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ContactManagerJFX cmJfx = new ContactManagerJFX(primaryStage);
		
	}

	public static Controller  createContactApp (ContactManagerJFX contactManagerJFX) {
		Controller controller=null;
		try {
			ContactsManager cm = new ContactsManager("contacts.dat");//model
			 controller = new Controller(cm);//controller
			ContactsManagerFrame cmf = new ContactsManagerFrame();//viewer
			controller.addViewr(cmf);
			controller.addViewr(contactManagerJFX);
			
			
			
		} catch (FileNotFoundException e) {
			System.out.println("Main File not Found !");//not supposed to enter here. (well programming)
		}catch (IOException e) {
			System.out.println("I/O problem with the file");//not supposed to enter here. (well programming)
		}		
		return controller;
	}




}
