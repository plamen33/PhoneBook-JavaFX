package phonebook.controllers;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import phonebook.interfaces.impls.CollectionPhoneBook;
import phonebook.objects.Person;
import phonebook.utils.DialogManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    private CollectionPhoneBook phoneBookImpl = new CollectionPhoneBook();

    private Stage mainStage;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private CustomTextField txtSearch;

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

    private ResourceBundle resourceBundle;

    private ObservableList<Person> backupList;

    public void setMainStage(Stage mainStage){
        this.mainStage = mainStage;
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
        backupList = FXCollections.observableArrayList();
        backupList.addAll(phoneBookImpl.getPersonList());
        tablePhoneBook.setItems(phoneBookImpl.getPersonList());
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource("../fxml/edit.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("phonebook.bundles.Locale", new Locale("en")));
            fxmlEdit = fxmlLoader.load();
            editDialogController = fxmlLoader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void updateCountLabel() {
        labelCount.setText(resourceBundle.getString("count")+": " + phoneBookImpl.getPersonList().size());
    }

    public void actionButtonPressed(ActionEvent actionEvent){

        Object source = actionEvent.getSource();
        // if what is clicked is not Button get out of the method
        if(!(source instanceof Button)){
            return;
        }
        Person selectedPerson = (Person) tablePhoneBook.getSelectionModel().getSelectedItem();
        Button clickedButton = (Button) source;


        switch (clickedButton.getId()){
            case "btnAdd":
                editDialogController.setPerson(new Person());
                showDialog();
                if(editDialogController.getPerson().getNames() == ""|| editDialogController.getPerson().getPhone() == ""){
                    break;
                }
                phoneBookImpl.add(editDialogController.getPerson());
                break;
            case "btnEdit":
                if (!personIsSelected(selectedPerson)) {
                    return;
                }
                editDialogController.setPerson(selectedPerson);
                showDialog();
                break;
            case "btnDelete":
                if (!personIsSelected(selectedPerson)) {
                    return;
                }
                phoneBookImpl.delete(selectedPerson);
                break;

        }

    }
    private boolean personIsSelected(Person selectedPerson) {
        if(selectedPerson == null){
            DialogManager.showErrorDialog(resourceBundle.getString("error"), resourceBundle.getString("select_person"));
            return false;
        }
        return true;
    }
    private void showDialog() {

        if (editDialogStage==null) {   //// we initialize the stage once - it is the first time when we have null
            editDialogStage = new Stage();
            editDialogStage.setTitle(resourceBundle.getString("edit"));
            editDialogStage.setMinHeight(150);
            editDialogStage.setMinWidth(300);
            editDialogStage.setResizable(false); // the window cannot be resized
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }

        editDialogStage.showAndWait();  // to wait closing window

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        //tablePhoneBook.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        columnNames.setCellValueFactory(new PropertyValueFactory<Person, String>("names"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));
        setupClearButtonField(txtSearch);
        initListeners();
        fillData();
        initLoader();
    }

    // clear button implementation from controlfx
    private void setupClearButtonField(CustomTextField customTextField) {
        try {
            Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
            m.setAccessible(true);
            m.invoke(null, customTextField, customTextField.rightProperty());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // implementation of search
    public void actionSearch(ActionEvent actionEvent){
        phoneBookImpl.getPersonList().clear();

        for (Person person: backupList) {
            if(person.getNames().toLowerCase().contains(txtSearch.getText().toLowerCase())|| person.getPhone().toLowerCase().contains(txtSearch.getText().toLowerCase())){
                phoneBookImpl.getPersonList().add(person);
            }
        }
    }

}