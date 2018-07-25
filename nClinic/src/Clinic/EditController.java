package Clinic;

import Database.DBConnection;
import com.jfoenix.controls.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditController implements Initializable {

    // 1. Gaining the connection from the DBConnection class in Database package
    // 2. Create observable list to populate the table
    private DBConnection db;
    {
        try {
            db = new DBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Connection con = db.getCon();
    private ObservableList<Queries> editOL = FXCollections.observableArrayList();
    private PreparedStatement ps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadData();

        // add a listener to the table items to populate the areas of patient visit and appointment information
        table_view_in_edit.getSelectionModel().selectedItemProperty().addListener(e -> {

            Queries q = table_view_in_edit.getSelectionModel().getSelectedItem();

            // data which are guaranteed to be in the database for every appointment
            patiant_name_In_edit.setText(q.getName());
            reserveDate_picker_In_edit.setValue(LocalDate.parse(q.getReserveDate()));
            reserveTime_picker_In_edit.setValue(LocalTime.parse(q.getReserveTime()));

            // data which could be null
            if (q.getAttendTime() != null) {
                attendDate_picker_In_edit.setValue(LocalDate.parse(q.getAttendDate()));
            } else {
                attendDate_picker_In_edit.setValue(null);
            }

            // data which could be null
            if (q.getAttendTime() != null) {
                attendTime_picker_In_edit.setValue(LocalTime.parse(q.getAttendTime()));
            } else {
                attendTime_picker_In_edit.setValue(null);
            }

            // data which could be null
            // TODO try removing the extra after finishing the project
            if (q.getVisit_type().equals("كشفية")){
                rb_examination.setSelected(true);
                rb_revision.setSelected(false); //extra
            }else if (q.getVisit_type().equals("متابعة")){
                rb_revision.setSelected(true);
                rb_examination.setSelected(false); //extra
            }else {
                rb_revision.setSelected(false);
                rb_examination.setSelected(false);
            }

            // data which could be null
            // notice that there is a space after حضر
            // since i did it to make the app differentiate between the two values of attendance
            if (q.getAttend().equals("حضر ")){
                chb_attended.setSelected(true);
            }else if (q.getAttend().equals("لم يحضر")) {
                chb_attended.setSelected(false);
            }else {
                chb_attended.setSelected(false);
            }

            // data which could be null
            if (q.getAttend_type().equals("بموعد")){
                chb_with_app.setSelected(true);
            }else if (q.getAttend_type().equals("بدون موعد")){
                chb_with_app.setSelected(false);
            }else {
                chb_with_app.setSelected(false);
            }

            // data which could be null
            if (q.getPaymentDate() != null) {
                paymentDate_picker_In_edit.setValue(LocalDate.parse(q.getPaymentDate()));
            }else {
                paymentDate_picker_In_edit.setValue(null);
            }

            // data which could be null
            if (q.getPayment() != null) {
                payment_value.setText(q.getPayment());
            }else {
                payment_value.setText(null);
            }

        });
    }

    // populate the table with data
    private void loadData() {

        // connect the table column with the attributes of the patient from the Queries class
        id_In_editTable.setCellValueFactory(new PropertyValueFactory<>("pstiantID"));
        name_In_editTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        date_In_editTable.setCellValueFactory(new PropertyValueFactory<>("reserveDate"));
        time_In_editTable.setCellValueFactory(new PropertyValueFactory<>("reserveTime"));

        try {

            // database related code "MySql"
            ps = con.prepareStatement("select * from visitInfo ");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                // load data to the observable list
                editOL.add(new Queries(new SimpleStringProperty(rs.getString("id")),
                        new SimpleStringProperty(rs.getString("patiantID")),
                        new SimpleStringProperty(rs.getString("patiant_name")),
                        new SimpleStringProperty(rs.getString("visit_type")),
                        new SimpleStringProperty(rs.getString("payment_value")),
                        new SimpleStringProperty(rs.getString("reserve_date")),
                        new SimpleStringProperty(rs.getString("reserve_time")),
                        new SimpleStringProperty(rs.getString("attend_date")),
                        new SimpleStringProperty(rs.getString("attend_time")),
                        new SimpleStringProperty(rs.getString("payment_date")),
                        new SimpleStringProperty(rs.getString("attend")),
                        new SimpleStringProperty(rs.getString("attend_type"))

                ));

            }

            // assigning the observable list to the table
            table_view_in_edit.setItems(editOL);

            ps.close();
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        Networking n = new Networking();
//        n.stopThread();
//        n.networkConnection();

    }

    // edit screen components
    @FXML
    private TableView<Queries> table_view_in_edit = new TableView<>();
    @FXML
    private TableColumn<Queries, String> id_In_editTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> name_In_editTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> date_In_editTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> time_In_editTable = new TableColumn<>();
    @FXML
    private JFXTextField patiant_name_In_edit = new JFXTextField();
    @FXML
    private JFXDatePicker reserveDate_picker_In_edit = new JFXDatePicker();
    @FXML
    private JFXRadioButton rb_examination = new JFXRadioButton();
    @FXML
    private JFXRadioButton rb_revision = new JFXRadioButton();
    @FXML
    private JFXTimePicker reserveTime_picker_In_edit = new JFXTimePicker();
    @FXML
    private JFXTimePicker attendTime_picker_In_edit = new JFXTimePicker();
    @FXML
    private JFXDatePicker attendDate_picker_In_edit = new JFXDatePicker();
    @FXML
    private JFXCheckBox chb_attended = new JFXCheckBox();
    @FXML
    private JFXCheckBox chb_with_app = new JFXCheckBox();
    @FXML
    private JFXDatePicker paymentDate_picker_In_edit = new JFXDatePicker();
    @FXML
    private JFXTextField payment_value = new JFXTextField();

    @FXML
    void delete_button1(ActionEvent event) {

        Queries q = table_view_in_edit.getSelectionModel().getSelectedItem();

        // alert the user if the patient is not selected
        if (q == null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطا في الحذف");
            alert.setHeaderText("يجب تحديد المريض اولا");

            alert.showAndWait();

        }else {

            // confirmation message to delete the patient
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("تأكيد");
            alert.setHeaderText("تاكيد الحذف");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                try {

                    // database related code "MySql"
                    ps = con.prepareStatement("delete from visitInfo where id = ? ");
                    ps.setString(1, q.getId());

                    ps.executeUpdate();
                    editOL.clear();

                    loadData();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }else {
                alert.close();
            }

        }

    }

    @FXML
    private Label error1;

    // first edit button (appointment related)
    @FXML
    void edit_button1(ActionEvent event) {

        String visitType = returnVisitType();
        String attended = returnAttended();
        String attendType = returnAttendType();

        Queries q = table_view_in_edit.getSelectionModel().getSelectedItem();

        // check for required fields
        if (reserveDate_picker_In_edit.getValue() == null || reserveTime_picker_In_edit.getValue() == null
                || attendDate_picker_In_edit.getValue() == null || attendTime_picker_In_edit.getValue() == null || visitType.isEmpty()){

            error1.setVisible(true);

        }else {

            // confirmation message
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("تأكيد");
            alert.setHeaderText("تاكيد التعديل");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                try {

                    // database related code "MySql"
                    ps = con.prepareStatement("update visitInfo set reserve_date = ?," +
                            " reserve_time= ?," +
                            " attend_date = ?," +
                            " attend_time = ?," +
                            " visit_type = ?," +
                            " attend = ?," +
                            " attend_type = ?" +
                            " where id = ? ");
                    ps.setString(1, reserveDate_picker_In_edit.getValue().toString());
                    ps.setString(2, reserveTime_picker_In_edit.getValue().toString());
                    ps.setString(3, attendDate_picker_In_edit.getValue().toString());
                    ps.setString(4, attendTime_picker_In_edit.getValue().toString());
                    ps.setString(5, visitType);
                    ps.setString(6, attended);
                    ps.setString(7, attendType);
                    ps.setString(8, q.getId());

                    ps.executeUpdate();

                    // doesn't need to call loadData method because the change is in the Queries data
                    // and will be displayed in the Queries interface

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                alert.close();
            }

            error1.setVisible(false);
            clearfields1();

        }

    }

    private String returnVisitType(){
        if (rb_examination.isSelected()){
            return "كشفية";
        }else{
            return "متابعة";
        }
    }

    private String returnAttended(){
        if (chb_attended.isSelected()){
            // the space after حضر is for the code to separate the two values right
            // and display them in the Queries interface
            return "حضر ";
        }else{
            return "لم يحضر";
        }
    }

    private String returnAttendType(){
        if (chb_with_app.isSelected()){
            return "بموعد";
        }else{
            return "بدون موعد";
        }
    }

    @FXML
    private Label error2;

    // second edit button (payment related)
    @FXML
    void edit_button2(ActionEvent event) {

        String payment = payment_value.getText();

        Queries q = table_view_in_edit.getSelectionModel().getSelectedItem();

        // check for required fields
        if (paymentDate_picker_In_edit.getValue() == null || payment == null) {
            error2.setVisible(true);
        } else {

            // confirmation message
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("تأكيد");
            alert.setHeaderText("تاكيد التعديل");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                try {

                    // database related code "MySql"
                    ps = con.prepareStatement("update visitInfo set payment_date = ?," +
                            " payment_value= ?" +
                            " where id = ? ");
                    ps.setString(1, paymentDate_picker_In_edit.getValue().toString());
                    ps.setString(2, payment);
                    ps.setString(3, q.getId());

                    ps.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                alert.close();
            }

            error2.setVisible(false);
            clearfields2();

        }
    }

    // appointment related
    private void clearfields1(){
        reserveDate_picker_In_edit.setValue(null);
        reserveTime_picker_In_edit.setValue(null);
        attendDate_picker_In_edit.setValue(null);
        attendTime_picker_In_edit.setValue(null);
        rb_examination.setSelected(false);
        rb_revision.setSelected(false);
        chb_attended.setSelected(false);
        chb_with_app.setSelected(false);
    }

    // payment related
    private void clearfields2(){
        paymentDate_picker_In_edit.setValue(null);
        payment_value.setText("");
    }

    @FXML
    void refresh_button(ActionEvent event) {
        editOL.clear();
        loadData();
    }

}
