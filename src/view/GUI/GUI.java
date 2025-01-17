package view.GUI;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.fxml.*;
import view.GUI.SelectWindow.SelectWindow;

import java.io.IOException;

public class GUI extends Application {
    @Override
    public void start(Stage stage) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GUI/SelectWindow/SelectWindow.fxml"));
            Parent root = loader.load();
            Scene selectScene = new Scene(root, 600, 600);

            SelectWindow controller = loader.getController();

            stage.setTitle("Select Statement");
            stage.setScene(selectScene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
