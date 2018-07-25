package Clinic;

import Database.DBConnection;
import Database.Networking;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DoctorController implements Initializable{

    // 1. Gaining the connection from the DBConnection class in Database package
    // 2. Create observable list to populate the table
    // 3. Created a universal TableModel item
    private DBConnection db;
    {
        try {
            db = new DBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Connection con = db.getCon();
    private ObservableList<TableModel> nameOL = FXCollections.observableArrayList();
    private PreparedStatement ps;
    private TableModel row;

    // this id is used to solve the problem of
    // deselecting the table item when the focus is lost
    // by saving the selected id in a variable for later use
    private String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadData();

        // filter the table based on the name of the patient
        // and clearing the other fields when the name field is empty
        FilteredList<TableModel> filteredData = new FilteredList<>(nameOL, p -> true);
        name_In_doctor.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(row -> {

                if (newValue == null || newValue.isEmpty()) {
                    table_In_doctor.getSelectionModel().clearSelection();
                    name_In_doctor.setText("");
                    age_In_doctor.setText("");
                    sex_In_doctor.setText("");
                    connect_In_doctor.setText("");
                    address_In_doctor.setText("");
                    return true;
                }

                String Filter = newValue;

                return row.getName().contains(Filter);

            });

        });
        SortedList<TableModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_In_doctor.comparatorProperty());
        table_In_doctor.setItems(sortedData);

    }

    // populate the table with data
    private void loadData(){

        // connect the table column with the name attribute of the patient from the TableModel class
        name_In_doctorTable.setCellValueFactory(new PropertyValueFactory<>("name"));

        try {

            // database related code "MySql"
            ps = con.prepareStatement("select * from patiantinfo");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                // load data to the observable list
                nameOL.add(new TableModel(new SimpleStringProperty(rs.getString("id")),
                        new SimpleStringProperty(rs.getString("name")),
                        new SimpleStringProperty(rs.getString("age")),
                        new SimpleStringProperty(rs.getString("sex")),
                        new SimpleStringProperty(rs.getString("address")),
                        new SimpleStringProperty(rs.getString("connect"))));

            }

            // assigning the observable list to the table
            table_In_doctor.setItems(nameOL);

            ps.close();
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // add a listener to the table items to populate the areas of patient information
        table_In_doctor.getSelectionModel().selectedItemProperty().addListener(e -> {

            row = table_In_doctor.getSelectionModel().getSelectedItem();

            name_In_doctor.setText(row.getName());
            age_In_doctor.setText(row.getAge());
            sex_In_doctor.setText(row.getSex());
            connect_In_doctor.setText(row.getConnect());
            address_In_doctor.setText(row.getAddress());

            // saving the id for later use to not lose it when the focus is lost
            id = row.getId();

        });

    }

    // doctor screen components
    @FXML
    private TableView<TableModel> table_In_doctor = new TableView<>();
    @FXML
    private TableColumn<TableModel, String> name_In_doctorTable = new TableColumn<>();
    @FXML
    private JFXTextField name_In_doctor = new JFXTextField();
    @FXML
    private JFXTextField age_In_doctor = new JFXTextField();
    @FXML
    private JFXTextField sex_In_doctor = new JFXTextField();
    @FXML
    private JFXTextField connect_In_doctor = new JFXTextField();
    @FXML
    private JFXTextField address_In_doctor = new JFXTextField();
    @FXML
    private JFXTextField date_In_doctor = new JFXTextField();
    @FXML
    private JFXTextArea cheifComp = new JFXTextArea();
    @FXML
    private JFXTextArea otherComp = new JFXTextArea();
    @FXML
    private JFXTextArea PIH = new JFXTextArea();
    @FXML
    private JFXTextArea PMH = new JFXTextArea();
    @FXML
    private JFXTextArea PSH = new JFXTextArea();
    @FXML
    private JFXTextArea DD = new JFXTextArea();
    @FXML
    private JFXTextArea CT = new JFXTextArea();
    @FXML
    private JFXTextArea MRI = new JFXTextArea();
    @FXML
    private JFXTextArea otherInvests = new JFXTextArea();
    @FXML
    private JFXTextArea plan = new JFXTextArea();
    @FXML
    private JFXTextArea clinicalExam = new JFXTextArea();

    @FXML
    void delete_In_doctor(ActionEvent event) {

    }

    @FXML
    void follow_In_doctor(ActionEvent event) {

    }

    @FXML
    void save_In_doctor(ActionEvent event) {

//        try {
//
//            //TODO needs to be handled in the server (don't forget the null id for the D.B.)
//            messageToServer = "saveDS;" + id + "," + cheifComp.getText() + "," + otherComp.getText() + ","
//                    + PIH.getText() + "," + PMH.getText() + "," + PSH.getText() + ","
//                    + clinicalExam.getText() + "," + DD.getText() + "," + CT.getText() + ","
//                    + MRI.getText() + "," + otherInvests.getText() + "," + plan.getText() + "";
//            n.output(messageToServer);
//
////            ps = con.prepareStatement("insert into medicalInfo values(null,?,?,?,?,?,?,?,?,?,?,?,?)");
////            ps.setString(1,id);
////            ps.setString(2,cheifComp.getText());
////            ps.setString(3,otherComp.getText());
////            ps.setString(4,PIH.getText());
////            ps.setString(5,PMH.getText());
////            ps.setString(6,PSH.getText());
////            ps.setString(7,clinicalExam.getText());
////            ps.setString(8,DD.getText());
////            ps.setString(9,CT.getText());
////            ps.setString(10,MRI.getText());
////            ps.setString(11,otherInvests.getText());
////            ps.setString(12,plan.getText());
////
////            ps.executeUpdate();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        clearFeilds();

    }

    //TODO must work when the save button is triggered
    // clear all the medical information fields preparing for the next patient
    private void clearFeilds(){
        cheifComp.setText("");
        otherComp.setText("");
        PIH.setText("");
        PMH.setText("");
        PSH.setText("");
        clinicalExam.setText("");
        DD.setText("");
        CT.setText("");
        MRI.setText("");
        otherInvests.setText("");
        plan.setText("");
    }

    // for the X button beside the name field
    @FXML
    void clear_name(ActionEvent event) {
        name_In_doctor.setText("");
    }

}
