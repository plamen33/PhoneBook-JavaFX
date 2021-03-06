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
import phonebook.interfaces.impls.ContactData;
import phonebook.objects.Person;
import phonebook.utils.DialogManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    private ContactData phoneBookImpl = new ContactData();

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

    private boolean searchCheck = false;

    public void setMainStage(Stage mainStage){
        this.mainStage = mainStage;
    }


    private void initListeners() {
        // we add Listener to listen for changed data and display actual count of records
        phoneBookImpl.getContacts().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> c) {
                updateCountLabel();
            }
        });

        tablePhoneBook.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Person selectedPerson = (Person) tablePhoneBook.getSelectionModel().getSelectedItem();
                    editDialogController.setPerson(selectedPerson);
                    showDialog();
                    if (searchCheck){
                        phoneBookImpl.getContacts().clear();
                        phoneBookImpl.loadContacts();
                        searchCheck = false;
                    }

                    editDialogController.updatePerson(selectedPerson);
                    phoneBookImpl.saveContacts();
                }
            }
        });
    }
    private void fillData() {
        phoneBookImpl.loadContacts();
        backupList = FXCollections.observableArrayList();
        backupList.addAll(phoneBookImpl.getContacts());
        tablePhoneBook.setItems(phoneBookImpl.getContacts());
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
        labelCount.setText(resourceBundle.getString("count")+": " + phoneBookImpl.getContacts().size());
    }

    public void actionButtonPressed(ActionEvent actionEvent){

        Object source = actionEvent.getSource();
        // if what is clicked is not Button get out of the method
        if(!(source instanceof Button)){
            return;
        }
        Person selectedPerson = (Person) tablePhoneBook.getSelectionModel().getSelectedItem();
        Button clickedButton = (Button) source;
        if (searchCheck){
            phoneBookImpl.getContacts().clear();
            phoneBookImpl.loadContacts();
            searchCheck = false;
        }


        switch (clickedButton.getId()){
            case "btnAdd":
                editDialogController.setPerson(new Person());
                showDialog();
                if(editDialogController.getPerson().getNames() == ""|| editDialogController.getPerson().getPhone() == ""){
                    break;
                }
                phoneBookImpl.add(editDialogController.getPerson());
                phoneBookImpl.saveContacts();
                break;
            case "btnEdit":
                if (!personIsSelected(selectedPerson)) {
                    return;
                }
                editDialogController.setPerson(selectedPerson);
                showDialog();
//                phoneBookImpl.update(selectedPerson);
                editDialogController.updatePerson(selectedPerson);
                phoneBookImpl.saveContacts();
                break;
            case "btnDelete":
                if (!personIsSelected(selectedPerson)) {
                    return;
                }
                phoneBookImpl.delete(selectedPerson);
                phoneBookImpl.saveContacts();
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
        if(searchCheck){
            phoneBookImpl.getContacts().clear();
            phoneBookImpl.loadContacts();
        }
        ObservableList<Person> obsList = FXCollections.observableArrayList();
        obsList.addAll(phoneBookImpl.getContacts());
        phoneBookImpl.getContacts().clear();

        for (Person person: obsList) {
            if(person.getNames().toLowerCase().contains(txtSearch.getText().toLowerCase())|| person.getPhone().toLowerCase().contains(txtSearch.getText().toLowerCase())){
                phoneBookImpl.getContacts().add(person);
            }
        }
        if(phoneBookImpl.getContacts().isEmpty()){
            DialogManager.showInfoDialog(resourceBundle.getString("notification"), resourceBundle.getString("no_records"));
            phoneBookImpl.loadContacts();
            searchCheck = false;
        }
        if(txtSearch.getText().equals("")){
            searchCheck = false;
        }
        else{
            searchCheck = true;
        }
    }

}