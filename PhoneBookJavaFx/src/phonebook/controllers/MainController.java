package phonebook.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
public class MainController {

    @FXML
    private Button addButton;

    public void showDialog(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/edit.fxml"));
            stage.setTitle("Edit Record");
            stage.setMinHeight(150);
            stage.setMinWidth(300);
            stage.setResizable(false); // the window cannot be resized
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow()); // here we set the parent window
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
