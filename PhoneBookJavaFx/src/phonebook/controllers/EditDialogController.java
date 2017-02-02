package phonebook.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import phonebook.objects.Person;

public class EditDialogController {
    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtNames;

    @FXML
    private TextField txtPhone;

    private Person person;

    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }
    public void setPerson(Person person) {
        this.person = person;

        txtNames.setText(person.getNames());
        txtPhone.setText(person.getPhone());
    }
}
