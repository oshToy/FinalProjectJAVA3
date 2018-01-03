import java.awt.event.ActionListener;

import javax.swing.Timer;



public interface IView {

	public void init();

	public void registerListener(ActionListener listener);	
	
	public String[] getBookContactData();   

    public void setBookContactData(String[] dataToDynamicJLabel);

	public void setUpdatEnabled(boolean state);

	public void setEditFieldsData(String[] textToTextField);

	public String getFileFormat();

	public String getFilePath();

	public String[] getSortChoises();

	public void stopTimer();

	public String getShowOrderBy();
}