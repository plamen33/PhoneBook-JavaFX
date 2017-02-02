package phonebook.controllers;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import phonebook.interfaces.impls.CollectionPhoneBook;
import phonebook.objects.Person;

public class MainController {

    private CollectionPhoneBook phoneBookImpl = new CollectionPhoneBook();

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
    @FXML
    private void initialize() {
        tablePhoneBook.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        columnNames.setCellValueFactory(new PropertyValueFactory<Person, String>("names"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));

        // we add Listener to listen for changed data and display actual count of records
        phoneBookImpl.getPersonList().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> c) {
                updateCountLabel();
            }
        });
        phoneBookImpl.fillTestData();

        tablePhoneBook.setItems(phoneBookImpl.getPersonList());

    }

    private void updateCountLabel() {
        labelCount.setText("Count Records: " + phoneBookImpl.getPersonList().size());
    }

    public void showDialog(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        // if what is clicked is not Button get out of the method
        if(!(source instanceof Button)){
            return;
        }

        Button clickedButton = (Button) source;
        Person selectedPerson = (Person)tablePhoneBook.getSelectionModel().getSelectedItem();

        switch (clickedButton.getId()){
            case "btnAdd":
                System.out.println("add " + selectedPerson);
                break;
            case "btnEdit":
                System.out.println("edit " + selectedPerson);
                break;
            case "btnDelete":
                System.out.println("delete " + selectedPerson);
                break;

        }
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/edit.fxml"));
            stage.setTitle("Edit Record");
            stage.setMinHeight(150);
            stage.setMinWidth(300);
            stage.setResizable(false); // the window cannot be resized
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow()); // here we set the parent window
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
