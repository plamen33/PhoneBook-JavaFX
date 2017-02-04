package phonebook.start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import phonebook.controllers.MainController;
import phonebook.interfaces.impls.CollectionPhoneBook;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../fxml/main.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("phonebook.bundles.Locale", new Locale("en")));
        Parent fxmlMain = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setMainStage(primaryStage);

        primaryStage.setTitle(fxmlLoader.getResources().getString("phone_book"));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(400);
        Scene scene = new Scene(fxmlMain, 300, 200); // ++
        scene.getStylesheets().add(getClass().getResource("../css/style.css").toExternalForm()); //++
        primaryStage.setScene(scene);/// ++
        primaryStage.getIcons().add(new Image(getClass().getResource("../images/phonebook.png").toExternalForm()));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}