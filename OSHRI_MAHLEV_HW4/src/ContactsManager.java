

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.Timer;

public class ContactsManager implements IFinalsInterface,ISortFinals,IModel{

	private boolean IsShowed;
	private RandomAccessFile dataFile;
	private FileListIterator<Contact> iterator;
	private boolean lastMoveWasBackWard;
	private boolean editMode;//to edit the specfic contact
	private Contact workingContact;
	private String[] textToTextField=new String[new Contact().getUiData().length];;
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	private String[] dataInString=new String[new Contact().getUiData().length];//srting array contains all the contact data , return to Frame
	public ContactsManager(String fileName) throws IOException  {
		dataFile=new RandomAccessFile(new File(fileName) , "rw");
		iterator=new FileListIterator<Contact>(dataFile);

	}
	public void registerListener(ActionListener listener) {
		this.listeners.add(listener);

	}


	public  void nextContact() throws IllegalAccessException  {
		if(editMode==false){
			if (iterator.hasNext()){


				workingContact=(Contact) iterator.next();
				
				
				if(lastMoveWasBackWard==true){
					workingContact=(Contact) iterator.next();
				}
				lastMoveWasBackWard=false;
				textToTextField= returnDataToFrameByArray(workingContact);
				
				 proccessEvent(Controller.MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT,textToTextField);
			}
			else throw new NullPointerException();
		}
		else throw new IllegalAccessException();
	}
	public void previousContact() throws IllegalAccessException  {
		if(editMode==false){
			if (iterator.hasPrevious())
			{
				workingContact=(Contact)iterator.previous();
				if(lastMoveWasBackWard==false){
					if(iterator.hasPrevious())
						workingContact=(Contact) iterator.previous();
					else {
						nextContact();
						throw new NullPointerException();
					}
				}
				lastMoveWasBackWard=true;
				textToTextField=returnDataToFrameByArray(workingContact);
				 proccessEvent(Controller.MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT,textToTextField);
			}
			else throw new NullPointerException();
		}
		else throw new IllegalAccessException();
	}
	public void lastContact() throws IOException, IllegalAccessException{
		if(editMode==false){
			while(iterator.hasNext()){
				workingContact=iterator.next();
			}
			if(workingContact==null){//todo
				nextContact();
				lastMoveWasBackWard=false;
				 proccessEvent(Controller.MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT,textToTextField);
			}
			else{
			lastMoveWasBackWard=false;
			textToTextField=returnDataToFrameByArray(workingContact);
			 proccessEvent(Controller.MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT,textToTextField);
			}
		}
		else throw new IllegalAccessException();
	}

	public void firstContact() throws IOException, IllegalAccessException{
		if(editMode==false){
			while(iterator.hasPrevious()){
				workingContact=iterator.previous();
			
			}
			if(workingContact==null){//if the first contact
				nextContact();
				lastMoveWasBackWard=true;
				 proccessEvent(Controller.MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT,textToTextField);
			}
			else{
			lastMoveWasBackWard=true;
			textToTextField=returnDataToFrameByArray(workingContact);
			 proccessEvent(Controller.MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT,textToTextField);
			}
		}
		else throw new IllegalAccessException();
	}
	public String[] getTextToTextFieldModel() {
		return textToTextField;
	}
	public void editContact() throws IllegalAccessException {

		if(emptyBook()==false){
		editMode=true;
		proccessEvent(Controller.MODEL_TEXTTOEDITFIELD_UPDATED_EVENT,textToTextField); 
		}else{
		 throw new IllegalAccessException();}
	}
	public void updateContact(String fName,String lName,String phoneNum) throws IllegalAccessException  {

		workingContact=new Contact(getId(),fName, lName, phoneNum);

		try{
			iterator.set(workingContact);
			if(lastMoveWasBackWard==false){//pointer after contact
				dataFile.seek(dataFile.getFilePointer()+CONTACT_SIZE*2);
			}


		}
		catch(NullPointerException | IOException ex){//first object , never did next or prev

			if(numberOfContactOnfile()>0){
				iterator.next();
				iterator.previous();
				iterator.set(workingContact);
			}
			//there is one contact !!!
			else {
				iterator.previous();
				iterator.next();
				iterator.set(workingContact);
				try {
					dataFile.seek(dataFile.getFilePointer()+CONTACT_SIZE*2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		editMode=false;
		textToTextField=returnDataToFrameByArray(workingContact);
		 proccessEvent(Controller.MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT,textToTextField);
	

	}

	public void exportContact(String format) throws IOException {
		if(emptyBook()){
			throw new IOException();
		}
		Contact conctact=new Contact(getId(),textToTextField[0],textToTextField[1],textToTextField[2]);
		File file=new File(getId()+"."+format);

		conctact.export(format,file);


	}
	public void createContact(String fName, String lName, String phoneNum) throws IOException, IllegalAccessException {
		if(editMode==false){
		workingContact=new Contact(numberOfContactOnfile(),fName,lName,phoneNum);
		iterator.add(workingContact);
		if(iterator.hasPrevious()||numberOfContactOnfile()>0)//contact isnt the first ever made
			throw new NullPointerException();
		else{	
			iterator.next();//if first time put file pointer after the first one 
			lastMoveWasBackWard=false;
		}
		textToTextField= returnDataToFrameByArray(workingContact);
		 proccessEvent(Controller.MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT,textToTextField);

	}
		else  throw new IllegalAccessException();
	}
    public void firstContactExist(){
    	
    	if(emptyBook()==false){//show first contact id exist
    		try {
    			nextContact();
    		} catch (IllegalAccessException e) {
    			System.out.println("cant move while editing contact ");
    		}

    	}
    }
	public void loadContactFromFile(String format, String stringId) throws IOException, ClassNotFoundException {
		//return array of strings to avoid static jtextfields


		//System.out.println(contact.getObjectSize()); // object-size is well !! checked

		DataInputStream fileStream=new DataInputStream(new FileInputStream(stringId+"."+format));

		if(format.equals("obj.dat")){
			ObjectInputStream ObjectStream=new ObjectInputStream(fileStream);
			workingContact=(Contact) ObjectStream.readObject();
			workingContact.setId(-1);//useless contact after using
			textToTextField= returnDataToFrameByArray(workingContact);
			proccessEvent(Controller.MODEL_TEXTTOEDITFIELD_UPDATED_EVENT,textToTextField); 



		}
		else if(format.equals("byte.dat")){
			String fName= FixedLengthStringIO.readFixedLengthString(FIRST_NAME_SIZE,fileStream);
			String lName=FixedLengthStringIO.readFixedLengthString(LAST_NAME_SIZE,fileStream);
			String phoneNum=FixedLengthStringIO.readFixedLengthString(PHONE_NUMBER_SIZE,fileStream);
			workingContact=new Contact(-1,fName,lName,phoneNum);
			textToTextField= returnDataToFrameByArray(workingContact);
			proccessEvent(Controller.MODEL_TEXTTOEDITFIELD_UPDATED_EVENT,textToTextField);
		}

		else if(format.equals("txt")){
			Scanner s=new Scanner(fileStream);
			for (int i = 0; i < workingContact.getUiData().length; i++) {
				s.nextLine();
				dataInString[i]=s.nextLine();
			}

			fileStream.close();
			s.close();
			proccessEvent(Controller.MODEL_TEXTTOEDITFIELD_UPDATED_EVENT,dataInString);
		}
		fileStream.close();
	}
@Override
	public void  showContactListByInterval( String orderMethod) throws IllegalAccessException {
		if(emptyBook()==false){
		if(editMode==false){
			if (IsShowed==false){//first time doing show
				 firstTimeShow(orderMethod,textToTextField);		
			}
			else{//not the first time 
	
				if(orderMethod.equals(ORDER_BY_ASC)){
					if(iterator.hasNext()){//asc and not the end file
						 nextContact();//process event inside
					}
					else{//asc and  the end of file
						IsShowed=false;
						proccessEvent(Controller.MODEL_SHOW_EVENT_FINISHED);//finish show
					}
				}
				else{//ORDER_BY_EESC
					if(iterator.hasPrevious()){//asc and not the end file
						 previousContact();//process event inside
					}
					else{//asc and  the end of file
						IsShowed=false;
						proccessEvent(Controller.MODEL_SHOW_EVENT_FINISHED);//finish show
					}
				}
			}
			
		}
		else {proccessEvent(Controller.MODEL_SHOW_EVENT_FINISHED);;//finish show
		throw new IllegalAccessException();
		}	
	}
		else {proccessEvent(Controller.MODEL_SHOW_EVENT_FINISHED);//finish show
			System.out.println("the book is empty , cant show contact by interval ");
		}
		}
	

	
	public void sortFile(String sortMethod, String fieldName, String orderMethod) throws IllegalAccessException, IOException {
		if(editMode==false){
		switch(sortMethod){
		case SORT_BY_FIELD:{
	
			sortByField(fieldName,orderMethod);
			break;
		}
		case SORT_BY_REPEATS:{
			sortByCount(fieldName,orderMethod);
			break;
		}
		case REVERSE_LIST:{
			reverseOrderOfContactsFromFile();
			break;
		}
		}
		lastContact();///check !!!
		firstContact();
		}
		else throw new IllegalAccessException();
	}
	private void proccessEvent(String modelShowEventFinished) {
		for (ActionListener listener : listeners) {
			listener.actionPerformed(new ActionEvent(this, -1, modelShowEventFinished));
		}
		
	}
	private void proccessEvent(String event, String[] textToTextField) {
		if(event.equals(Controller.MODEL_TEXTTOEDITFIELD_UPDATED_EVENT))
			setTextToTextFieldModel(textToTextField);
		else if(event.equals(Controller.MODEL_TEXTTOEDITFIELD_UPDATED_EVENT)){}
				
		for (ActionListener listener : listeners) {
			listener.actionPerformed(new ActionEvent(this, -1, event));
		}
	}
	private String[] returnDataToFrameByArray(Contact contact){
		dataInString[0]=contact.getFirstName();
		dataInString[1]=contact.getLastName();
		dataInString[2]=contact.getPhoneNumber();

		return dataInString;
	}

	

	private int numberOfContactOnfile() {
		int res=0;
		try {
			res=(int) ((dataFile.length()/CONTACT_SIZE/2)-1);
			if(res==-1)
				res=0;
		} catch (IOException e) {
			System.out.println("IOException");
		}
		return res ;
	}



	private void sortByCount(String fieldName, String orderMethod) {
		Map<Contact,Integer> countedMap=new HashMap<Contact,Integer> ();//first map
		TreeMap<Integer, Contact>countedTreeMap; //after sort


		try {
			dataFile.seek(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean fieldHasAppeared=false;
		while(iterator.hasNext()){
			workingContact=iterator.next();
			fieldHasAppeared=false;		

			for (Entry<Contact, Integer> entry : countedMap.entrySet()) {
				if(workingContact.equalsByField(fieldName, entry.getKey())){//duplicate
					fieldHasAppeared=true;
					entry.setValue(entry.getValue()+1);//+1
					break;

				}}

			if(fieldHasAppeared==false)
				countedMap.put(workingContact, 1);//not appeared



		}
		countedTreeMap=sortMapByCount(countedMap,fieldName);
		insertCollectionTofile(countedTreeMap.values());//ASC is default
		if(orderMethod.equals(ORDER_BY_ASC)){
			reverseOrderOfContactsFromFile();
		}


	}

	private TreeMap<Integer, Contact> sortMapByCount(Map<Contact, Integer> countedMap,String fieldName) {
		TreeMap<Integer,Contact> countedTreeMap=new TreeMap<Integer, Contact>(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {//never return 0 = never delete duplicate 
				if(o1>=o2)
					return 1;
				return -1;
			}

		});

		for (Entry<Contact, Integer> entry : countedMap.entrySet()) {
			countedTreeMap.put(entry.getValue(), entry.getKey());
		}


		return countedTreeMap;

	}

	private void reverseOrderOfContactsFromFile() {
		Stack<Contact> reverseOrderStack=new Stack<Contact>();
		try {
			dataFile.seek(0);

			while(iterator.hasNext()){
				reverseOrderStack.push(iterator.next());
			}
			dataFile.setLength(0);

			while(reverseOrderStack.isEmpty()==false){
				this.iterator.add(reverseOrderStack.pop());
			}
		} catch (IOException e) {
			System.out.println("Failed to reverse file");
		}

	}

	private void sortByField(String fieldName, String orderMethod) {
		Set<Contact> sortedSet;
		switch (fieldName) {
		case FIRST_NAME_FIELD:{
			sortedSet=new TreeSet<Contact>(new Comparator<Contact>() {

				@Override
				public int compare(Contact o1,Contact o2) {
					int res= o1.getFirstName().compareTo(o2.getFirstName());
					if(orderMethod.equals(ORDER_BY_DESC)){
						res*=-1;
					}

					return res;
				}					 
			});


			insertWholeFileToSet(sortedSet);
			insertCollectionTofile(sortedSet);
			break;
		}
		case LAST_NAME_FIELD:{
			sortedSet=new TreeSet<Contact>(new Comparator<Contact>() {

				@Override
				public int compare(Contact o1,Contact o2) {
					int res= o1.getLastName().compareTo(o2.getLastName());
					if(orderMethod.equals(ORDER_BY_DESC)){
						res*=-1;
					}

					return res;
				}					 
			});
			insertWholeFileToSet(sortedSet);
			insertCollectionTofile(sortedSet);
			break;
		}
		case PHONE_NUMBER_FIELD:{
			sortedSet=new TreeSet<Contact>(new Comparator<Contact>() {

				@Override
				public int compare(Contact o1,Contact o2) {
					int res= o1.getPhoneNumber().compareTo(o2.getPhoneNumber());
					if(orderMethod.equals(ORDER_BY_DESC)){
						res*=-1;
					}

					return res;
				}					 
			});
			insertWholeFileToSet(sortedSet);
			insertCollectionTofile(sortedSet);
			break;

		}
		}
	}



	private void insertWholeFileToSet(Set<Contact> sortedSet) {
		try {
			dataFile.seek(0);
		} catch (IOException e) {
			System.out.println("failed to sort");
		}
		while(iterator.hasNext()){

			sortedSet.add(iterator.next());
		}

	}
	private void insertCollectionTofile(Collection<Contact> sortedCollection) {
		try {
			dataFile.setLength(0);
		} catch (IOException e) {
			System.out.println("failed to copy Set to file");
		}
		Iterator<Contact> setIterator2=sortedCollection.iterator(); 
		while(setIterator2.hasNext()){
			this.iterator.add(setIterator2.next());


		}

	}





	private String[] firstTimeShow(String orderMethod,String[] textToTextField) throws IllegalAccessException {//for first contact of showlistbyinterval
		IsShowed=true;
		try {
			if(orderMethod.equals(ORDER_BY_ASC)){

				 firstContact();
			}
			else {

				 lastContact();
				 proccessEvent(Controller.MODEL_TEXTTOTEXTFIELD_UPDATED_EVENT,textToTextField);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}


	private int getId() {//for file 
		int res=0;
		try {
			if(lastMoveWasBackWard==false){
				res=(int) ((dataFile.getFilePointer()/CONTACT_SIZE/2)-1);
			}
			else res=(int) ((dataFile.getFilePointer()/CONTACT_SIZE/2));
			if(res==-1)
				res=0;
		} catch (IOException e) {
			System.out.println("IOException");
		}
		return res ;
	}

	private void setTextToTextFieldModel(String[] textToTextFieldModel) {
		this.textToTextField = textToTextFieldModel;
	}
	private boolean emptyBook() {
		try {
			if(dataFile.length()>0){
				return false;
			}

		} catch (IOException e) {
			System.out.println("File not exist.");
		}
		return true;
	}


}
