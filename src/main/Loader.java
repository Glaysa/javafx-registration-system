package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Loader {
    // LOADS AN FXML FILE AND CHANGES THE CURRENT WINDOW
    public void window(String fxml_file, String window_title, Stage stage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml_file));
            Parent root = loader.load();

            stage.setTitle(window_title);
            stage.setScene(new Scene(root, 984, 636));
            stage.show();
        } catch(Exception e){
            System.err.println(e);
        }
    }
}
