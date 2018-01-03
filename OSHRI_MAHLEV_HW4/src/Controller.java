import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;



public class Controller implements ActionListener {
	public static final String  CREATE_EVENT="Create";
	public static final String  UPDATE_EVENT="Update";
	public static final String  PREVIOUS_EVENT="<";
	public static final String  NEXT_EVENT=">";
	public static final String  FIRST_EVENT="First";
	public static final String  LAST_EVENT="Last";
	public static final String  EDIT_EVENT="Edit contact";
	public static final String  EXPORT_EVENT="Export";
	public static final String  LOAD_FILE_EVENT="load file";
	public static final String  SORT_EVENT="SORT";
	public static final String  SHOW_EVENT="SHOW";
	public static final String  STARTING_VIEWER_EVENTS="starting app";
	public static final String MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT = "MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT ";
	public static final String MODEL_TEXTTOEDITFIELD_UPDATED_EVENT = "MODEL_TEXTTOEDITFIELD_UPDATED_EVENT ";
	public static final String MODEL_SHOW_EVENT_FINISHED = "MODEL_SHOW_EVENT_FINISHED";

	private IModel model;
	private ArrayList<IView> viewers=new ArrayList<IView>(); 
	private IContact iContact=new Contact();
	private String [] contactDataByViewer = new String [iContact.getUiData().length] ;
	private String [] sortDataByViewer = new String [3] ;
	private ColoredViewer.actionType actionTypeChangeColor=ColoredViewer.actionType.DEFAULT;
	public  Controller(IModel model){
		this.model=model;
		model.registerListener(this);
	}
	public void addViewr(IView cmf) {// adding listener to array of viewer
		viewers.add(cmf);
		cmf.registerListener(this);
		cmf.init();
	}
	@Override
	public void actionPerformed(ActionEvent e)  {
		if(e.getActionCommand().equals(CREATE_EVENT)){
			actionTypeChangeColor=ColoredViewer.actionType.UPDATE;
			createContact((IView)e.getSource());
		}
		else if(e.getActionCommand().equals(UPDATE_EVENT)){
			actionTypeChangeColor=ColoredViewer.actionType.UPDATE;
			updateContact((IView)e.getSource());
			

		}
		else if(e.getActionCommand().equals(PREVIOUS_EVENT)){
			actionTypeChangeColor=ColoredViewer.actionType.PREVIOUS;
			previousContact((IView)e.getSource());

		}
		else if(e.getActionCommand().equals(NEXT_EVENT)){
			actionTypeChangeColor=ColoredViewer.actionType.NEXT;
			nextContact((IView)e.getSource());
			

		}
		else if(e.getActionCommand().equals(FIRST_EVENT)){
			actionTypeChangeColor=ColoredViewer.actionType.PREVIOUS;
			firstContact((IView)e.getSource());

		}
		else if(e.getActionCommand().equals(LAST_EVENT)){
			actionTypeChangeColor=ColoredViewer.actionType.NEXT;
			lastContact((IView)e.getSource());


		}
		else if(e.getActionCommand().equals(EDIT_EVENT)){
			editContact((IView)e.getSource());

		}
		else if(e.getActionCommand().equals(EXPORT_EVENT)){
			exportContact((IView)e.getSource());

		}
		else if(e.getActionCommand().equals(LOAD_FILE_EVENT)){
			loadContact((IView)e.getSource());

		}
		else if(e.getActionCommand().equals(SORT_EVENT)){
			sortContacts((IView)e.getSource());

		}
			else if(e.getActionCommand().equals(SHOW_EVENT)){
			showContacts((IView)e.getSource());

		}
		else if(e.getActionCommand().equals(STARTING_VIEWER_EVENTS)){
			startingViewer((IView)e.getSource());
		}
		else if(e.getActionCommand().equals(MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT)){
				updateViewersData();
		}
		else if(e.getActionCommand().equals(MODEL_TEXTTOEDITFIELD_UPDATED_EVENT)){
				setEditFieldsData();
		}
		else if(e.getActionCommand().equals(MODEL_SHOW_EVENT_FINISHED)){
			stopViewerTimer();
	}
	}
	private void stopViewerTimer() {
		for (IView viewer : viewers) {
			viewer.stopTimer();
		}
		
	}
	private void updateViewersData(){
		for (IView viewer : viewers) {
			if (ColoredViewer.class.isInstance(viewer)==true){
				((ColoredViewer)viewer).changeColor(actionTypeChangeColor);
			}
			viewer.setBookContactData(model.getTextToTextFieldModel());
			
		}
	}
	private void setUpdatEnabled(boolean state) {
		for (IView viewer : viewers) {
			viewer.setUpdatEnabled(state);

		}
	}
	private void setEditFieldsData() {
		for (IView viewer : viewers) {
			viewer.setEditFieldsData(model.getTextToTextFieldModel());
		}
	}
	
	private void showContacts(IView source) {
		try {
			model.showContactListByInterval(source.getShowOrderBy());
		} catch (IllegalAccessException e1) {
			System.out.println("cant move while editing contact ");
			
		}

	}
	 
	private void sortContacts(IView source) {
		sortDataByViewer=source.getSortChoises();
		try {
		model.sortFile(sortDataByViewer[0],sortDataByViewer[1],sortDataByViewer[2]);         
		
			//model.lastContact(null);///check !!!
			//model.firstContact(textToTextField);
			//updateViewersData(textToTextField);
		} catch (NullPointerException |IOException e1) {
			System.out.println("There are no contact!");
		} catch (IllegalAccessException e1) {
			System.out.println("cant move while editing contact ");
		}

	}


	private void loadContact(IView source) {
		try {
			model.loadContactFromFile(source.getFileFormat(),source.getFilePath());
			//setEditFieldsData(textToTextField);
			System.out.println("Loaded !");
		} catch (ClassNotFoundException | IOException |NullPointerException e1) {
			System.out.println("File not Found!");
		}//array with data to update the jtextfields



	}



	private void exportContact(IView source) {
		String format=source.getFileFormat();

		try {
			
			model.exportContact(format);
		} catch (Exception e1) {
			System.out.println("The Phonebook is empty , cant export!");
		}

	}


	private void editContact(IView source) {
		//edit only if the book isnt empty	
     try {
		model.editContact();
		setUpdatEnabled(true);
	} catch (IllegalAccessException e) {
		 System.out.println("There are no contacts!");
	}

		


	}



	private void lastContact(IView source) {
		try {
			model.lastContact();

		} catch (NullPointerException |IOException e1) {
			System.out.println("There are no contact!");
		} catch (IllegalAccessException e1) {
			System.out.println("cant move while editing contact ");
		}

	}


	private void firstContact(IView source) {
		try {
			model.firstContact();

		} catch (NullPointerException | IOException e1) {
			System.out.println("There are no contact!");
		} catch (IllegalAccessException e1) {
			System.out.println("cant move while editing contact ");
		}

	}


	private void nextContact(IView source) {
		try{
			model.nextContact();

		}
		catch(NullPointerException ex){
			System.out.println("next contact not exist");
		} catch (IllegalAccessException e1) {
			System.out.println("cant move while editing contact ");
		}

	}


	private void previousContact(IView source) {
		try{
			model.previousContact();

		}
		catch(NullPointerException ex){
			System.out.println("previous contact not exist");
		} catch (IllegalAccessException e1) {
			System.out.println("cant move while editing contact ");
		}


	}


	private void updateContact(IView source) {
		try {
			contactDataByViewer=source.getBookContactData();
			model.updateContact(contactDataByViewer[0],contactDataByViewer[1],contactDataByViewer[2]);
			setUpdatEnabled(false);
		} catch (IllegalAccessException e1) {
			System.out.println("cant move while editing contact ");
		}



	}


	private void createContact(IView source) {
		try {
			contactDataByViewer=source.getBookContactData();
			model.createContact(contactDataByViewer[0],contactDataByViewer[1],contactDataByViewer[2]);

		} catch (IOException e1) {
			System.out.println("cant create contact");
		}
		catch(NullPointerException ex){
			updateViewersData();
		} catch (IllegalAccessException e) {
			System.out.println("cant create contact");
		}


	}

	private void startingViewer(IView source){
		try {
			model.firstContactExist();
		} catch (IllegalAccessException | IOException e) {
			System.out.println("cant start the app well");
		}
	}

    
}

