import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class Contact implements IContact , IFinalsInterface,Serializable{
	@Override
	public String toString() {
		return "Contact [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
				+ phoneNumber + "]";
	}
	private transient static final long serialVersionUID = 1L;
	transient private int id=-1;

	private String firstName,lastName,phoneNumber;
	public Contact(){

	}
	public Contact(int id,String firstName,String lastName,String phoneNumber){
		this.id=id;
		this.firstName=firstName;
		this.lastName=lastName;
		this.phoneNumber=phoneNumber;

	}
	@Override
	public void writeObject(RandomAccessFile dataFile) throws IOException {

		FixedLengthStringIO.writeFixedLengthString(firstName ,FIRST_NAME_SIZE ,dataFile);
		FixedLengthStringIO.writeFixedLengthString(lastName ,LAST_NAME_SIZE,dataFile);
		FixedLengthStringIO.writeFixedLengthString(phoneNumber ,PHONE_NUMBER_SIZE,dataFile);




	}
	@Override
	public void export(String format, File file) throws IOException {
		DataOutputStream fileStream=new DataOutputStream(new FileOutputStream(file));
		if(format.equals("obj.dat")){
			ObjectOutputStream ObjectStream=new ObjectOutputStream(fileStream);
			ObjectStream.writeObject(this);
			ObjectStream.close();
		}
		else if(format.equals("byte.dat")){
			FixedLengthStringIO.writeFixedLengthString(firstName ,FIRST_NAME_SIZE ,fileStream);
			FixedLengthStringIO.writeFixedLengthString(lastName ,LAST_NAME_SIZE,fileStream);
			FixedLengthStringIO.writeFixedLengthString(phoneNumber ,PHONE_NUMBER_SIZE,fileStream);
			fileStream.close();
		}
		else if(format.equals("txt")){
			String [] attrbuiteList=getUiData();
			PrintWriter pw=new PrintWriter(file);
			pw.write(attrbuiteList[0].toString()+":\n");
			FixedLengthStringIO.writeFixedLengthStringTxt(firstName, FIRST_NAME_SIZE, pw);
			pw.write("\n");
			pw.write(attrbuiteList[1].toString()+":\n");
			FixedLengthStringIO.writeFixedLengthStringTxt(lastName, LAST_NAME_SIZE, pw);
			pw.write("\n");
			pw.write(attrbuiteList[2].toString()+":\n");
			FixedLengthStringIO.writeFixedLengthStringTxt(phoneNumber, PHONE_NUMBER_SIZE, pw);

			pw.close();
		}
		fileStream.close();
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String[] getUiData() {
		String[] UiData={FIRST_NAME_FIELD,LAST_NAME_FIELD,PHONE_NUMBER_FIELD};
		return UiData;
	}
	@Override
	public int getObjectSize() {	
		return CONTACT_SIZE*2;
	}
	public boolean equalsByField(String fieldName,Contact c1){
		switch (fieldName) {
		case FIRST_NAME_FIELD:{
			if(this.firstName.equals(c1.getFirstName())){
				return true;		
			}
			break;
		}
		case LAST_NAME_FIELD:{
			if(this.lastName.equals(c1.getLastName())){
				return true;
			}
			break;
		}
		case PHONE_NUMBER_FIELD:{
			if(this.phoneNumber.equals(c1.getPhoneNumber())){
				return true;
			}
			break;
		}		
		}
		return false;
	}
}

