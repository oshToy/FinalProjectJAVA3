import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

public interface IModel {
	
	public String[] getTextToTextFieldModel();

	public void registerListener(ActionListener listener);


	public void firstContact() throws IOException, IllegalAccessException;

	public void lastContact() throws IOException, IllegalAccessException;

	public void nextContact() throws IllegalAccessException;

	public void previousContact() throws IllegalAccessException;

	 public void firstContactExist() throws IllegalAccessException, IOException;
	 
	public void updateContact(String fName,String lName,String phoneNum) throws IllegalAccessException;

	public void createContact(String fName, String lName, String phoneNum) throws IOException, IllegalAccessException;

	public void exportContact(String format) throws IOException;
	
	public void editContact() throws IllegalAccessException;

	public void loadContactFromFile(String format, String stringId) throws IOException, ClassNotFoundException ;

	public void  showContactListByInterval(String orderMethod) throws IllegalAccessException;
	
	public void sortFile(String sortBox, String fieldBox, String ascOrDescBox) throws IllegalAccessException, IOException ;
}
