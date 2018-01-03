import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ListIterator;

public class FileListIterator <T extends IContact> implements ListIterator<T>,IFinalsInterface{
	
	private RandomAccessFile dataFile;
	private T setObject;
	private boolean lastMoveWasBackWard;
	public FileListIterator (RandomAccessFile dataFile){
		this.dataFile=dataFile;

	}

	@SuppressWarnings("unchecked")
	public T initContactWorkaround(int id, String firstName, String lastName,
			String phoneNumber) {

		return (T) new Contact(id, firstName, lastName, phoneNumber);
	}

	@Override
	public void add(T e) {
		long position=0;
		try {
			position=dataFile.getFilePointer();
			dataFile.seek(dataFile.length());
			e.writeObject(dataFile);
			
		} catch (IOException e1) {
			System.out.println("failed writing contact");
		}
		finally{
			try {
					dataFile.seek(position);				
			} catch (IOException e1) {
				System.out.println("failed writing contact");
			}
		}

	}

	@Override
	public boolean hasNext() {

		try {

			if(dataFile.getFilePointer()==dataFile.length())
				return false;	
			else return true;
		} catch (IOException e) {
			System.out.println("search hasNext failed");
		}
		return false;
	}

	@Override
	public boolean hasPrevious() {
		try {
			if(dataFile.getFilePointer()==0)
				return false;	
			else return true;
		} catch (IOException e) {
			System.out.println("search hasNext failed");
		}
		return false;
	}

	@Override
	public T next() {
		
		String fName=null;
		String lName=null;
		String phoneNum=null;
		try {
			fName = FixedLengthStringIO.readFixedLengthString(FIRST_NAME_SIZE,dataFile);
			lName=FixedLengthStringIO.readFixedLengthString(LAST_NAME_SIZE,dataFile);
			phoneNum=FixedLengthStringIO.readFixedLengthString(PHONE_NUMBER_SIZE,dataFile);
		} catch (IOException e) {
			System.out.println("i/o problem");
		}

		try {
			int id =(int) ((dataFile.getFilePointer()/CONTACT_SIZE/2)-1);
			if(id==-1)
				id=0;
			setObject=initContactWorkaround(id,fName,lName,phoneNum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//id=pointer/size of contact -1
		
		
		lastMoveWasBackWard=false;
		return setObject;
	
	}

	
	@Override
	public int nextIndex() {
		// no need !!!
		return 0;
	}

	@Override
	public T previous() {
		long position=0;
			try {
				position=dataFile.getFilePointer()-CONTACT_SIZE*2;
				dataFile.seek(position);
				setObject=next();
				dataFile.seek(position);	
			} catch (IOException e) {
				System.out.println("i/o problem");
			}				
			
			lastMoveWasBackWard=true;
			return setObject;
	
		
		
	}

	@Override
	public int previousIndex() {
		// no need !!!
		return 0;
	}

	@Override
	public void remove() {
		// no need !!!

	}

	@Override
	public void set(T e) {
		if(setObject!=null){// did next or previous at least one time
		
			long position=0;
			try {
				if(lastMoveWasBackWard==false){//if last move was next
				position=dataFile.getFilePointer()-(CONTACT_SIZE*2);
				}
				//if last move was prev
				else position=dataFile.getFilePointer();
				if(position<0)	
					position=0;
				dataFile.seek(position);
				e.writeObject(dataFile);
				dataFile.seek(position);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else throw new NullPointerException();

	}


}
