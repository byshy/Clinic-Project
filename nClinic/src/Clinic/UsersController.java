package Clinic;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

// not used yet
public class UsersController {

    @FXML
    private JFXTextField main_username;

    @FXML
    private JFXTextField main_pass;

    @FXML
    private JFXTextField second_username;

    @FXML
    private JFXTextField second_pass;

    @FXML
    private JFXButton logout_bt;

    @FXML
    void save(ActionEvent event) {

    }

    @FXML
    void switch_acc(ActionEvent event) {

    }

    @FXML
    void logout(ActionEvent event) {
        System.exit(-1);
    }

}
