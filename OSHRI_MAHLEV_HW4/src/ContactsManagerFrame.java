import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;


/**
 * @author oshri
 *
 */
public class ContactsManagerFrame extends JFrame  implements ISortFinals,IView,ActionListener{

	private static final long serialVersionUID = 1L;
	//timer for show
	private Timer showListByTimer;
	//text fields 
	private JTextField jtfFName=new JTextField();
	private JTextField jtfLName=new JTextField();
	private JTextField jtfPNumber=new JTextField();
	private JTextField jtfFPath=new JTextField();

	//buttons
	private JButton jbtCreate=new JButton(Controller.CREATE_EVENT);
	private JButton jbtUpdate=new JButton(Controller.UPDATE_EVENT);
	private JButton jbtBack=new JButton(Controller.PREVIOUS_EVENT);
	private JButton jbtNext=new JButton(Controller.NEXT_EVENT);
	private JButton jbtFirst=new JButton(Controller.FIRST_EVENT);
	private JButton jbtLast=new JButton(Controller.LAST_EVENT);
	private JButton jbtEdit=new JButton(Controller.EDIT_EVENT);
	private JButton jbtExport=new JButton(Controller.EXPORT_EVENT);
	private JButton jbtLoad=new JButton(Controller.LOAD_FILE_EVENT);
	private JButton jbtSort=new JButton(Controller.SORT_EVENT);
	private JButton jbtShow=new JButton(Controller.SHOW_EVENT);

	//Dynamic label
	protected  JLabel jlFName=new JLabel();
	protected  JLabel jlLName=new JLabel();
	protected  JLabel jlPNumber=new JLabel();


	private IContact iContact=new Contact();

	//array for comoboBox
	String []fileFormatArry={"txt","obj.dat","byte.dat"};
	String []sortArry={SORT_BY_FIELD,SORT_BY_REPEATS,REVERSE_LIST};
	String []fieldArry={iContact.getUiData()[0],iContact.getUiData()[1],iContact.getUiData()[2]};
	String []ascOrDescArry={ORDER_BY_ASC,ORDER_BY_DESC};
	//comboBox
	private JComboBox<String> fileFormatBox=new JComboBox<String>(fileFormatArry);
	private JComboBox<String> sortBox=new JComboBox<String>(sortArry);
	private JComboBox<String> fieldBox=new JComboBox<String>(fieldArry);
	private JComboBox<String> ascOrDesc1Box=new JComboBox<String>(ascOrDescArry);
	private JComboBox<String> ascOrDesc2Box=new JComboBox<String>(ascOrDescArry);



	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();



	public ContactsManagerFrame() {


		this.setLayout(new GridLayout(3, 1,0,10) );

		JPanel firstPanel=CreateFirstPanel();
		this.add(firstPanel);//Insert first panel to main panel

		JPanel secondPanel=CreateSecondPanel();
		this.add(secondPanel);//Insert second panel to main panel

		JPanel thirdPanel=CreateThirdPanel();		
		this.add(thirdPanel);//Insert third panel to main panel,end of creating panel !
		
		jbtUpdate.setEnabled(false);
		setListenerForButtons();
		
	}
	public void init() {//frame settings
		setTitle("Contacts Manager By Java Swing");
		setSize(600,850);				
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	public void registerListener(ActionListener listener) {
		this.listeners.add(listener);

	}
	private void setListenerForButtons() {
		jbtCreate.addActionListener(this);
		jbtUpdate.addActionListener(this);
		jbtBack.addActionListener(this);
		jbtNext.addActionListener(this);
		jbtFirst.addActionListener(this);
		jbtLast.addActionListener(this);
		jbtEdit.addActionListener(this);
		jbtExport.addActionListener(this);
		jbtLoad.addActionListener(this);
		jbtSort.addActionListener(this);
		jbtShow.addActionListener(this);


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(Controller.SHOW_EVENT)){
			showContactsByInterval();
		}
			
		else proccessEvent(e.getActionCommand());
	
	}
	private void proccessEvent(String command) {
		for (ActionListener actionListener : listeners) {
			actionListener.actionPerformed(new ActionEvent(this, -1, command));
		}
	}

	@Override
	public void setBookContactData(String[] dataToDynamicJLabel) {
		jlFName.setText(dataToDynamicJLabel[0]);
		jlLName.setText(dataToDynamicJLabel[1]);
		jlPNumber.setText(dataToDynamicJLabel[2]);

	}


	@Override
	public void setUpdatEnabled(boolean state) {
		jbtUpdate.setEnabled(state);

	}

	@Override
	public void setEditFieldsData(String[] textToTextField) {
		jtfFName.setText(textToTextField[0]);
		jtfLName.setText(textToTextField[1]);
		jtfPNumber.setText(textToTextField[2]);
	}

	@Override
	public String[] getBookContactData() {
		String[] res=new String[iContact.getUiData().length];
		res[0]=jtfFName.getText();
		res[1]=jtfLName.getText();
		res[2]=jtfPNumber.getText();
	
		return res;
	}
	@Override
	public String getFileFormat() {
		return fileFormatBox.getSelectedItem().toString();
	}
	public String getFilePath() {
		return jtfFPath.getText().toString();
	}
	@Override
	public String[] getSortChoises() {
		String [] sortChoises=new 	String [3];
		sortChoises[0]=sortBox.getSelectedItem().toString();
		sortChoises[1]=fieldBox.getSelectedItem().toString();
		sortChoises[2]=ascOrDesc1Box.getSelectedItem().toString();         

		return sortChoises;
	}

	@Override
	public String getShowOrderBy() {
		return ascOrDesc2Box.getSelectedItem().toString();
	}
	private void showContactsByInterval() {
		if(showListByTimer==null||showListByTimer.isRunning()==false){
			//if the timer runing and not the first time dont enter here, want to finish the inerval!
			showListByTimer=new Timer(500,new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent e) {
					proccessEvent(Controller.SHOW_EVENT);
				}
			} );
			showListByTimer.start();
	
	
		}
	
	}
	private JPanel CreateFirstPanel() {
		JPanel upperPanel=new JPanel(new GridLayout(4,2,25,35));
		upperPanel.add(new JLabel(iContact.getUiData()[0]+" :"));
		upperPanel.add(jtfFName);
		upperPanel.add(new JLabel(iContact.getUiData()[1]+" :"));
		upperPanel.add(jtfLName);
		upperPanel.add(new JLabel(iContact.getUiData()[2]+" :"));
		upperPanel.add(jtfPNumber);
		upperPanel.add(jbtCreate);
		upperPanel.add(jbtUpdate);
		return upperPanel;
	}
	private JPanel CreateSecondPanel() {
		//create center panel
		JPanel centerPanel=new JPanel(new BorderLayout(3,3));
		//Center-left panel
		JPanel CenterLeftPanel=new JPanel(new GridLayout(2,1,2,7));
		jbtBack.setFont(new Font("David",1,20));
		CenterLeftPanel.add(jbtBack);
		CenterLeftPanel.add(jbtFirst);
		centerPanel.add(CenterLeftPanel,BorderLayout.WEST);
		//Center-center panel
		JPanel CenterCenterPanel=new JPanel(new GridLayout(4,2,60,50));
		CenterCenterPanel.add(new JLabel(iContact.getUiData()[0]+" :"));
		CenterCenterPanel.add(jlFName);
		CenterCenterPanel.add(new JLabel(iContact.getUiData()[1]+" :"));
		CenterCenterPanel.add(jlLName);
		CenterCenterPanel.add(new JLabel(iContact.getUiData()[2] +" :"));
		CenterCenterPanel.add(jlPNumber);
		CenterCenterPanel.add(jbtEdit);
		centerPanel.add(CenterCenterPanel,BorderLayout.CENTER);
		JPanel CenterRightPanel=new JPanel(new GridLayout(2,1,2,7));
		jbtNext.setFont(new Font("David",1,20));
		//Center-right panel
		CenterRightPanel.add(jbtNext);
		CenterRightPanel.add(jbtLast);
		centerPanel.add(CenterRightPanel,BorderLayout.EAST);
		return centerPanel;
	}
	private JPanel CreateThirdPanel() {
		JPanel thirdPanel=new JPanel(new BorderLayout(20,20));
		JPanel upperPanel=new JPanel(new GridLayout(1,2,20,20));
		//bottom-left panel
		JPanel upperLeftPanel=new JPanel(new GridLayout(1,2,7,2));
		JPanel upperLeftLeftPanel=new JPanel(new GridLayout(4,1,40,35));
		upperLeftLeftPanel.add(new JLabel(""));
		upperLeftLeftPanel.add(fileFormatBox);
		upperLeftPanel.add(upperLeftLeftPanel);
		upperLeftPanel.add(jbtExport);
		upperPanel.add(upperLeftPanel);
		//bottom-right panel
		JPanel upperRightPanel=new JPanel(new GridLayout(3,1,5,50));
		upperRightPanel.add(new JLabel("file path:"));
		upperRightPanel.add(jtfFPath);
		upperRightPanel.add(jbtLoad);
		upperPanel.add(upperRightPanel);
		JPanel lowerPanel=new JPanel(new GridLayout(2,1,1,15));
		//bottom-left panel
		JPanel lowerUpPanel=new JPanel(new GridLayout(1,4,20,2));
		lowerUpPanel.add(sortBox);
		lowerUpPanel.add(fieldBox);
		lowerUpPanel.add(ascOrDesc1Box);
		lowerUpPanel.add(jbtSort);
		//bottom-right panel
		JPanel lowerBottomPanel=new JPanel(new GridLayout(1,2,20,0));
		lowerBottomPanel.add(ascOrDesc2Box);
		lowerBottomPanel.add(jbtShow);
		lowerPanel.add(lowerUpPanel);
		lowerPanel.add(lowerBottomPanel);
		thirdPanel.add(upperPanel, BorderLayout.CENTER);
		thirdPanel.add(lowerPanel, BorderLayout.SOUTH);
		return thirdPanel;
	}
	@Override
	public void stopTimer() {
		
		if(showListByTimer!=null&&showListByTimer.isRunning()==true)
		showListByTimer.stop();
		
	}


}

