package phonebook.controllers;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import phonebook.interfaces.impls.CollectionPhoneBook;
import phonebook.objects.Person;

import java.io.IOException;

public class MainController {

    private CollectionPhoneBook phoneBookImpl = new CollectionPhoneBook();

    private Stage mainStage;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private TableView tablePhoneBook;

    @FXML
    private TableColumn<Person, String> columnNames;

    @FXML
    private TableColumn<Person, String> columnPhone;

    @FXML
    private Label labelCount;

    // these parameters were taken out here in order them tp be accessed from the level of the class
    private Parent fxmlEdit;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private EditDialogController editDialogController;
    private Stage editDialogStage;

    public void setMainStage(Stage mainStage){
        this.mainStage = mainStage;
    }


    @FXML
    private void initialize() {
        //tablePhoneBook.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        columnNames.setCellValueFactory(new PropertyValueFactory<Person, String>("names"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));

        initListeners();
        fillData();
        initLoader();


    }
    private void initListeners() {
        // we add Listener to listen for changed data and display actual count of records
        phoneBookImpl.getPersonList().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> c) {
                updateCountLabel();
            }
        });

        tablePhoneBook.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    editDialogController.setPerson((Person)tablePhoneBook.getSelectionModel().getSelectedItem());
                    showDialog();
                }
            }
        });
    }
    private void fillData() {
        phoneBookImpl.fillTestData();
        tablePhoneBook.setItems(phoneBookImpl.getPersonList());
    }

    private void initLoader() {
        try {

            fxmlLoader.setLocation(getClass().getResource("../fxml/edit.fxml"));
            fxmlEdit = fxmlLoader.load();
            editDialogController = fxmlLoader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private void updateCountLabel() {
        labelCount.setText("Count Records: " + phoneBookImpl.getPersonList().size());
    }

    public void actionButtonPressed(ActionEvent actionEvent){

        Object source = actionEvent.getSource();
        // if what is clicked is not Button get out of the method
        if(!(source instanceof Button)){
            return;
        }

        Button clickedButton = (Button) source;


        switch (clickedButton.getId()){
            case "btnAdd":
                editDialogController.setPerson(new Person());
                showDialog();
                phoneBookImpl.add(editDialogController.getPerson());
                break;
            case "btnEdit":
                editDialogController.setPerson((Person)tablePhoneBook.getSelectionModel().getSelectedItem());
                showDialog();
                break;
            case "btnDelete":
                phoneBookImpl.delete((Person) tablePhoneBook.getSelectionModel().getSelectedItem());
                break;

        }

    }
    private void showDialog() {

        if (editDialogStage==null) {   //// we initialize the stage once - it is the first time when we have null
            editDialogStage = new Stage();
            editDialogStage.setTitle("Edit Record");
            editDialogStage.setMinHeight(150);
            editDialogStage.setMinWidth(300);
            editDialogStage.setResizable(false); // the window cannot be resized
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }

        editDialogStage.showAndWait();  // to wait closing window

    }
}