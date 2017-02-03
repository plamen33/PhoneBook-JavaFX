package phonebook.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import phonebook.objects.Person;
import phonebook.utils.DialogManager;

import java.net.URL;
import java.util.ResourceBundle;

public class EditDialogController implements Initializable{
    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtNames;

    @FXML
    private TextField txtPhone;

    private Person person;

    private ResourceBundle resourceBundle;

    public void setPerson(Person person) {
        if (person == null){
            return;
        }
        this.person = person;
        txtNames.setText(person.getNames());
        txtPhone.setText(person.getPhone());
    }
    public Person getPerson() {
        return person;
    }
    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }

    public void actionSave(ActionEvent actionEvent) {
        if(!checkValues()){
            return;
        }
        person.setPhone(txtPhone.getText());
        person.setNames(txtNames.getText());
        actionClose(actionEvent);
    }

    private boolean checkValues(){
        if(txtNames.getText().trim().length()== 0 || txtPhone.getText().trim().length()== 0){
            DialogManager.showInfoDialog(resourceBundle.getString("error"), resourceBundle.getString("fill_fields"));
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
    }
}
