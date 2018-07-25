package Clinic;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
import java.util.*;

public class Controller {
    //controller of the login screen

    //components of the login screen

    @FXML
    private JFXTextField user = new JFXTextField();
    @FXML
    private JFXPasswordField pass = new JFXPasswordField();
    @FXML
    private Text error;

    //action for the login button
    //check the user and pass and launch the corresponding interface
    //then hide the login screen
    @FXML
    void login(ActionEvent event) {
        try {
            Stage s = new Stage();
            if (user.getText().contains("") && pass.getText().contains("")) {

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("Interfaces/mainScreen.fxml")));
                s.setScene(new Scene(root));
//                s.setTitle("Dr.Nidal AbuHadrous");
                s.setTitle("Client");
                s.getIcons().add(new Image(Main.class.getResourceAsStream("/Interfaces/half-moon.png")));
                s.setFullScreen(true);
                s.setFullScreenExitHint("");
                s.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();

            } else if (user.getText().contains("user") && pass.getText().contains("123")) {

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("Interfaces/secondaryScreen.fxml")));
                s.setScene(new Scene(root));
//                s.setTitle("Dr.Nidal AbuHadrous");
                s.setTitle("Client");
                s.getIcons().add(new Image(Main.class.getResourceAsStream("/Interfaces/half-moon.png")));
                s.setFullScreen(true);
                s.setFullScreenExitHint("");
                s.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();

            } else {

                user.setText("");
                pass.setText("");
                user.setUnFocusColor(Color.RED);
                pass.setUnFocusColor(Color.RED);
                pass.setFocusColor(Color.RED);
                user.setFocusColor(Color.RED);
                error.setVisible(true);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
