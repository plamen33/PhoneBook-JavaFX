package phonebook.start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phonebook.interfaces.impls.CollectionPhoneBook;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
        primaryStage.setTitle("Address Book JavaFX");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(400);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        testData();
    }
    private void testData() {
        CollectionPhoneBook addressBook = new CollectionPhoneBook();
        addressBook.fillTestData();
        addressBook.print();

    }

    public static void main(String[] args) {
        launch(args);
    }
}