

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.Timer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ContactManagerJFX  extends ColoredViewer implements ISortFinals{
	//timer for show
	private Timeline showListByTimer;
	//text fields 
	private TextField jtfFName=new TextField();
	private TextField jtfLName=new TextField();
	private TextField jtfPNumber=new TextField();
	private TextField jtfFPath=new TextField();

	//buttons
	private Button jbtCreate=new Button(Controller.CREATE_EVENT);
	private Button jbtUpdate=new Button(Controller.UPDATE_EVENT);
	private Button jbtBack=new Button(Controller.PREVIOUS_EVENT);
	private Button jbtNext=new Button(Controller.NEXT_EVENT);
	private Button jbtFirst=new Button(Controller.FIRST_EVENT);
	private Button jbtLast=new Button(Controller.LAST_EVENT);
	private Button jbtEdit=new Button(Controller.EDIT_EVENT);
	private Button jbtExport=new Button(Controller.EXPORT_EVENT);
	private Button jbtLoad=new Button(Controller.LOAD_FILE_EVENT);
	private Button jbtSort=new Button(Controller.SORT_EVENT);
	private Button jbtShow=new Button(Controller.SHOW_EVENT);

	//Dynamic Text
	private  Text jlFName=new Text();
	private  Text jlLName=new Text();
	private  Text jlPNumber=new Text();

	//font for text
	private Font font=Font.font("DAVID", FontWeight.BOLD, 17);

	private IContact iContact=new Contact();

	//ObservableList for comoboBox
	ObservableList<String> fileFormatArry=FXCollections.observableArrayList("txt","obj.dat","byte.dat");
	ObservableList<String> sortArry=FXCollections.observableArrayList(SORT_BY_FIELD,SORT_BY_REPEATS,REVERSE_LIST);
	ObservableList<String> fieldArry=FXCollections.observableArrayList(iContact.getUiData()[0],iContact.getUiData()[1],iContact.getUiData()[2]);
	ObservableList<String> ascOrDescArry=FXCollections.observableArrayList(ORDER_BY_DESC,ORDER_BY_ASC);

	//comboBox
	private ComboBox<String> fileFormatBox=new ComboBox<String>(fileFormatArry);
	private ComboBox<String> sortBox=new ComboBox<String>(sortArry);
	private ComboBox<String> fieldBox=new ComboBox<String>(fieldArry);
	private ComboBox<String> ascOrDesc1Box=new ComboBox<String>(ascOrDescArry);
	private ComboBox<String> ascOrDesc2Box=new ComboBox<String>(ascOrDescArry);


	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	public ContactManagerJFX(Stage primaryStage) {
		try {
			start(primaryStage);
			ID204004055.createContactApp(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init(){
		jbtUpdate.setDisable(true);
		proccessEvent(Controller.STARTING_VIEWER_EVENTS);
		setOnActionForButtons();
		showListByTimer=new Timeline(new KeyFrame(new Duration(500), ae->proccessEvent(Controller.SHOW_EVENT)));
		showListByTimer.setCycleCount(Animation.INDEFINITE);

		jlFName.setFont(font);
		jlLName.setFont(font);
		jlPNumber.setFont(font);

	}
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Contacts Manager By Java FX");
		BorderPane mainPane=new BorderPane();
		mainPane.setTop(topPane());
		mainPane.setCenter(centerPane());
		mainPane.setBottom(bottomPane());
		Scene mainScene=new Scene(mainPane,550,520);
		primaryStage.setScene(mainScene);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.show();

	}
	private Node topPane(){
		VBox topPane = new VBox();
		GridPane dataToContactFields=new GridPane();
		HBox buttonOnBottom=new HBox(190);
		dataToContactFields.add(new Label("first name :"), 0, 0);
		dataToContactFields.add(jtfFName, 1, 0);
		dataToContactFields.add(new Label("last name :"), 0, 1);
		dataToContactFields.add(jtfLName, 1, 1);
		dataToContactFields.add(new Label("phone number :"), 0, 2);
		dataToContactFields.add(jtfPNumber, 1, 2);
		dataToContactFields.setPadding(new Insets(15, 100, 5, 100));
		buttonOnBottom.setPadding(new Insets(5, 100, 5, 100));
		buttonOnBottom.getChildren().add(jbtCreate);
		buttonOnBottom.getChildren().add(jbtUpdate);
		topPane.getChildren().addAll(dataToContactFields,buttonOnBottom);
		return topPane;
	}

	private Node centerPane() {
		GridPane centerPane=new GridPane();
		centerPane.add(secPane(), 0, 1);
		centerPane.add(thirdPane(), 0, 2);
		centerPane.add(forthPane(), 0, 3);
		return centerPane;
	}
	@SuppressWarnings("static-access")
	private Node secPane(){
		BorderPane secPane=new BorderPane();
		secPane.setPadding(new Insets(15,0,15,5));
		GridPane leftPane=new GridPane();
		leftPane.add(jbtBack,0,0);
		leftPane.add(jbtFirst,0,1);
		GridPane rightPane=new GridPane();
		rightPane.add(jbtNext,0,0);
		rightPane.add(jbtLast,0,1);
		GridPane centerPane=new GridPane();

		centerPane.add(new Label("first name :"), 0, 0);
		centerPane.add(jlFName, 1, 0);
		centerPane.add(new Label("last name :"), 0, 1);
		centerPane.add(jlLName, 1, 1);
		centerPane.add(new Label("phone number :"), 0, 2);
		centerPane.add(jlPNumber, 1, 2);
		centerPane.setMargin(jlFName, new Insets(0,0,0,100));
		centerPane.setMargin(jlLName, new Insets(0,0,0,100));
		centerPane.setMargin(jlPNumber, new Insets(0,0,0,100));
		centerPane.setPadding(new Insets(0,15,0,15));

		secPane.setBottom(jbtEdit);
		secPane.setMargin(jbtEdit, new Insets(8,0,0,60));
		secPane.setLeft(leftPane);
		secPane.setCenter(centerPane);
		secPane.setRight(rightPane);
		return secPane;
	}

	@SuppressWarnings("static-access")
	private Node thirdPane() {
		BorderPane thirdPane=new BorderPane();
		fileFormatBox.getSelectionModel().selectFirst();
		HBox exportPane=new HBox(fileFormatBox,jbtExport);
		exportPane.setMargin(jbtExport, new Insets(0,0,0,10));
		exportPane.setPadding(new Insets(20,0,0,0));
		thirdPane.setLeft(exportPane);
		jtfFPath.setPromptText("enter file name");
		HBox Load=new HBox();
		Load.getChildren().add(jbtLoad);
		Load.setAlignment(Pos.BASELINE_RIGHT);
		VBox pathPane =new VBox(new Label("file path"),jtfFPath,Load);
		pathPane.setSpacing(7);

		thirdPane.setRight(pathPane);
		return thirdPane;
	}

	private Node forthPane() {
		sortBox.getSelectionModel().selectFirst();
		fieldBox.getSelectionModel().selectFirst();
		ascOrDesc1Box.getSelectionModel().selectFirst();
		HBox forthPane=new HBox(sortBox,fieldBox,ascOrDesc1Box,jbtSort);
		forthPane.setSpacing(27);
		forthPane.setPadding(new Insets(15,0,10,0));

		return forthPane;
	}
	private Node bottomPane() {
		ascOrDesc2Box.getSelectionModel().selectFirst();
		HBox fifthPane =new HBox (ascOrDesc2Box,jbtShow);
		fifthPane.setPadding(new Insets(40,0,0,0));;
		fifthPane.setSpacing(100);
		return fifthPane;
	}

	@Override
	public void registerListener(ActionListener listener) {
		listeners.add(listener);

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
	public void setBookContactData(String[] dataToDynamicJLabel) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				jlFName.setText(dataToDynamicJLabel[0]);
				jlLName.setText(dataToDynamicJLabel[1]);
				jlPNumber.setText(dataToDynamicJLabel[2]);

			}
		});


	}
	@Override
	public void setUpdatEnabled(boolean state) {
		jbtUpdate.setDisable(!state);

	}
	@Override
	public void setEditFieldsData(String[] textToTextField) {
		jtfFName.setText(textToTextField[0]);
		jtfLName.setText(textToTextField[1]);
		jtfPNumber.setText(textToTextField[2]);

	}
	@Override
	public String getFileFormat() {
		System.out.println(fileFormatBox.getSelectionModel().getSelectedItem());
		return fileFormatBox.getSelectionModel().getSelectedItem();
	}
	@Override
	public String getFilePath() {
		return jtfFPath.getText();
	}
	@Override
	public String[] getSortChoises() {
		String [] sortChoises=new 	String [3];
		sortChoises[0]=sortBox.getSelectionModel().getSelectedItem();
		sortChoises[1]=fieldBox.getSelectionModel().getSelectedItem();
		sortChoises[2]=ascOrDesc1Box.getSelectionModel().getSelectedItem();         

		return sortChoises;
	}


	@Override
	public String getShowOrderBy() {
		return ascOrDesc2Box.getSelectionModel().getSelectedItem();
	}
	private void setOnActionForButtons(){
		jbtCreate.setOnAction(e-> proccessEvent(jbtCreate.getText()));
		jbtUpdate.setOnAction(e-> proccessEvent(jbtUpdate.getText()));
		jbtBack.setOnAction(e-> proccessEvent(jbtBack.getText()));
		jbtNext.setOnAction(e-> proccessEvent(jbtNext.getText()));
		jbtFirst.setOnAction(e-> proccessEvent(jbtFirst.getText()));
		jbtLast.setOnAction(e-> proccessEvent(jbtLast.getText()));
		jbtEdit.setOnAction(e-> proccessEvent(jbtEdit.getText()));
		jbtExport.setOnAction(e-> proccessEvent(jbtExport.getText()));
		jbtLoad.setOnAction(e-> proccessEvent(jbtLoad.getText()));
		jbtSort.setOnAction(e-> proccessEvent(jbtSort.getText()));
		jbtShow.setOnAction(e-> showContactsByInterval());
	}
	private void proccessEvent(String command) {
		for (ActionListener actionListener : listeners) {
			actionListener.actionPerformed(new ActionEvent(this, -1, command));
		}
	}

	@Override
	public void changeColor(actionType color) {
		jlFName.setFill(color.getValue());
		jlLName.setFill(color.getValue());
		jlPNumber.setFill(color.getValue());

	}

	@Override
	public void stopTimer() {
		showListByTimer.stop();

	}

	private void showContactsByInterval() {

		showListByTimer.play();


	}



}
