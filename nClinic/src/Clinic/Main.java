package Clinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    //main class of the project
    //launch the login screen
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/login.fxml"));
        primaryStage.setTitle("Dr.Nidal AbuHadrous");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream( "/Interfaces/half-moon.png" )));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
