package Clinic;

import Database.DBConnection;
import Database.Networking;
import com.jfoenix.controls.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReciptionController implements Initializable {

    // 1. Gaining the connection from the DBConnection class in Database package
    // 2. Create observable list to populate the table
    // 3. create a new instance of the network class
    // and a String object for the outgoing message
    private DBConnection db;
    {
        try {
            db = new DBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Connection con = db.getCon();
    private ObservableList<TableModel> tmOL = FXCollections.observableArrayList();
    private PreparedStatement ps;
    private Networking n = new Networking();
    private String messageToServer;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        // populate the combo box
        location.getItems().add("شمال غزة");
        location.getItems().add("غزة");
        location.getItems().add("الوسطى");
        location.getItems().add("خانيونس");
        location.getItems().add("رفح");
        location.setPromptText("غير محدد");

        loadData();

        // add a listener to the table items to populate the information of the selected item (patient)
        table_view_In_reciption.getSelectionModel().selectedItemProperty().addListener(e -> {

            TableModel row = table_view_In_reciption.getSelectionModel().getSelectedItem();

            try {
                new_patiant_In_reciption.setText(row.getName());
                new_patiant_In_reciptionFragment1.setText(row.getName());
                new_patiant_In_reciptionFragment2.setText(row.getName());
                new_patiant_In_reciptionFragment3.setText(row.getName());
            }catch (NullPointerException ex){}

            if (row.getAge().contains(".")) {
                // the age may consist of two sections one for the years and other for months
                // months for adults is not needed instead its used for children less than a year
                // and the age varies from 0.1 to 0.11 (one month to eleven months)
                String[] age = row.getAge().split("\\.");
                ageY_In_reciption.setText(age[0]);
                ageM_In_reciption.setText(age[1]);
            } else {
                ageY_In_reciption.setText(row.getAge());
                ageM_In_reciption.setText("");
            }

            String sex = row.getSex();
            if (sex.contains("ذكر")) {
                sexM_In_reciption.setSelected(true);
            } else {
                sexF_In_reciption.setSelected(true);
            }
            location.setValue(row.getAddress());
            connect_In_reciption.setText(row.getConnect());

            save_patiant_buttonID_In_reciption.setDisable(true);

            attended_In_reciption.setSelected(false);
            withAppointment_In_reciption.setSelected(false);

            reservation.setVisible(true);
            attendance.setVisible(false);
            payment.setVisible(false);

        });

    }

    // reception screen components
    @FXML
    private JFXComboBox<String> location = new JFXComboBox();
    @FXML
    private AnchorPane reservation;
    @FXML
    private AnchorPane attendance;
    @FXML
    private AnchorPane payment;

    @FXML
    void refresh_table(ActionEvent event) {
        tmOL.clear();
        loadData();
    }

    @FXML
    private JFXButton save_patiant_buttonID_In_reciption;

    // populate the table with data
    private void loadData() {

        // connect the table column with the attributes of the patient from the TableModel class
        id_In_reciptionTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_In_reciptionTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        age_In_reciptionTable.setCellValueFactory(new PropertyValueFactory<>("age"));
        sex_In_reciptionTable.setCellValueFactory(new PropertyValueFactory<>("sex"));
        address_In_reciptionTable.setCellValueFactory(new PropertyValueFactory<>("address"));
        connect_In_reciptionTable.setCellValueFactory(new PropertyValueFactory<>("connect"));

        try {

            // database related code "MySql"
            ps = con.prepareStatement("select * from patiantInfo");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                // load data to the observable list
                tmOL.add(new TableModel(new SimpleStringProperty(rs.getString("id")),
                        new SimpleStringProperty(rs.getString("name")),
                        new SimpleStringProperty(rs.getString("age")),
                        new SimpleStringProperty(rs.getString("sex")),
                        new SimpleStringProperty(rs.getString("address")),
                        new SimpleStringProperty(rs.getString("connect"))));

            }

            // assigning the observable list to the table
            table_view_In_reciption.setItems(tmOL);

            ps.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // filter the table based on the name of the patient
        FilteredList<TableModel> filteredData = new FilteredList<>(tmOL, p -> true);
        patiant_nameIn_reciption.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(row -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String Filter = newValue;

                if (row.getName().contains(Filter)) {
                    return true;
                } else return row.getId().contains(Filter);
            });

        });
        SortedList<TableModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_view_In_reciption.comparatorProperty());
        table_view_In_reciption.setItems(sortedData);

    }

    // reception screen components
    @FXML
    private TableView<TableModel> table_view_In_reciption = new TableView<>();
    @FXML
    private TableColumn<TableModel, String> id_In_reciptionTable = new TableColumn<>();
    @FXML
    private TableColumn<TableModel, String> name_In_reciptionTable = new TableColumn<>();
    @FXML
    private TableColumn<TableModel, String> age_In_reciptionTable = new TableColumn<>();
    @FXML
    private TableColumn<TableModel, String> sex_In_reciptionTable = new TableColumn<>();
    @FXML
    private TableColumn<TableModel, String> address_In_reciptionTable = new TableColumn<>();
    @FXML
    private TableColumn<TableModel, String> connect_In_reciptionTable = new TableColumn<>();
    @FXML
    private JFXTextField patiant_nameIn_reciption = new JFXTextField();
    @FXML
    private Label allFieldsAreRequired;
    @FXML
    private JFXTextField new_patiant_In_reciption = new JFXTextField();
    @FXML
    private JFXTextField ageM_In_reciption;
    @FXML
    private JFXTextField ageY_In_reciption;
    @FXML
    private JFXRadioButton sexF_In_reciption;
    @FXML
    private JFXRadioButton sexM_In_reciption;
    @FXML
    private JFXTextField connect_In_reciption;

    @FXML
    void new_patiant_button_In_reciption(ActionEvent event) {

        clearFields();
        new_patiant_In_reciptionFragment1.setText("");
        new_patiant_In_reciptionFragment2.setText("");
        new_patiant_In_reciptionFragment3.setText("");
        allFieldsAreRequired.setVisible(false);

        reservation.setVisible(true);
        attendance.setVisible(false);
        payment.setVisible(false);

    }

    private String sexInReciption() {

        if (sexM_In_reciption.isSelected()) {
            return "ذكر";
        } else if (sexF_In_reciption.isSelected()) {
            return "انثى";
        }
        return "";

    }

    // this button is not accessible when a patient is focused in the table
    // because all of his information is displayed in the in the input fields
    // so if it got clicked the patient will be duplicated
    @FXML
    void save_patiant_button_In_reciption(ActionEvent event) {

        String name = new_patiant_In_reciption.getText();
        String age = ageY_In_reciption.getText() + getAgeInMonths();
        String sex = sexInReciption();
        String address = location.getValue();
        String connect = connect_In_reciption.getText();

        // check for required fields
        if (name.isEmpty() || sex.isEmpty() || address == null || connect.isEmpty() ||
                (ageM_In_reciption.getText().isEmpty() && ageY_In_reciption.getText().isEmpty())) {

            allFieldsAreRequired.setVisible(true);

        } else {

            try {

                //insert message from the save button in main screen "MS"
                messageToServer = "insertMS;" + name + "," + age + "," + sex + "," + address + "," + connect + "";
                n.output(messageToServer);

                //TODO check if it works
                allFieldsAreRequired.setVisible(false);

            } catch (IOException e) {
                System.out.println("sth is wrong");
            }
        }

        tmOL.clear();
        loadData();
        clearFields();

    }

    private String getAgeInMonths() {
        // returns the months of a patient in special presentation
        // to make it universally used for adults and children in multiple occasions
        if (ageM_In_reciption.getText().equals("")) {
            return "";
        } else if (ageY_In_reciption.getText().equals("")) {
            return "0." + ageM_In_reciption.getText();
        } else {
            return "." + ageM_In_reciption.getText();
        }
    }

    @FXML
    void delete_patiant_button_In_reciption(ActionEvent event) {

        TableModel row = table_view_In_reciption.getSelectionModel().getSelectedItem();

        // alert the user if the patient is not selected
        if (row == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطا في الحذف");
            alert.setHeaderText("يجب تحديد المريض اولا");

            alert.showAndWait();
        } else {

            // confirmation message
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("تأكيد");
            alert.setHeaderText("تاكيد الحذف");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {

                    //delete message from the delete button in main screen "MS"
                    messageToServer = "deleteMS;" + row.getId() + "";
                    n.output(messageToServer);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                alert.close();
            }
        }

        tmOL.clear();
        loadData();
        clearFields();

    }

    @FXML
    void edit_patiant_button_In_reciption(ActionEvent event) {

        String name = new_patiant_In_reciption.getText();
        String age = ageY_In_reciption.getText() + getAgeInMonths();
        String sex = sexInReciption();
        String address = location.getValue();
        String connect = connect_In_reciption.getText();

        // check for required fields
        if (name.isEmpty() || sex.isEmpty() || address == null || connect.isEmpty() ||
                (ageM_In_reciption.getText().isEmpty() && ageY_In_reciption.getText().isEmpty())) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطا في التعديل");
            alert.setHeaderText("يجب تحديد المريض اولا");

            alert.showAndWait();

        } else {

            TableModel row = table_view_In_reciption.getSelectionModel().getSelectedItem();
            if (row == null) {

                // alert the user if the patient is not selected
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطا في التعديل");
                alert.setHeaderText("يجب تحديد المريض اولا");

                alert.showAndWait();

            } else {

                try {

                    //edit message from the edit button in main screen "MS"
                    messageToServer = "editMS;" + name + "," + age + "," + sex + "," + address + "," + connect + "," + row.getId() + "";
                    n.output(messageToServer);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        tmOL.clear();
        loadData();
        clearFields();

    }

    private void clearFields() {
        new_patiant_In_reciption.setText("");
        ageY_In_reciption.setText("");
        ageM_In_reciption.setText("");
        sexM_In_reciption.setSelected(false);
        sexF_In_reciption.setSelected(false);
        location.setValue("غير محدد");
        connect_In_reciption.setText("");

        table_view_In_reciption.getSelectionModel().clearSelection();
        save_patiant_buttonID_In_reciption.setDisable(false);

    }

    // reception screen components
    @FXML
    private JFXTextField new_patiant_In_reciptionFragment1 = new JFXTextField();
    @FXML
    private JFXDatePicker date_picker_In_reciptionFragment1 = new JFXDatePicker();
    @FXML
    private JFXTimePicker time_picker_In_reciptionFragment1 = new JFXTimePicker();

    // move to the attendance fragment
    @FXML
    void nextPage1(ActionEvent event) {
        attendance.setVisible(true);
        reservation.setVisible(false);
        payment.setVisible(false);
    }

    @FXML
    void new_patiant_button_In_reciptionFragment1(ActionEvent event) {

        clearFields();
        new_patiant_In_reciptionFragment1.setText("");
        date_picker_In_reciptionFragment1.setValue(null);
        time_picker_In_reciptionFragment1.setValue(null);

        table_view_In_reciption.getSelectionModel().clearSelection();

        attended_In_reciption.setSelected(false);
        withAppointment_In_reciption.setSelected(false);

    }

    @FXML
    private Label error_fragment1;

    @FXML
    void save_patiant_button_In_reciptionFragment1(ActionEvent event) {

        LocalDate date = date_picker_In_reciptionFragment1.getValue();
        LocalTime time = time_picker_In_reciptionFragment1.getValue();
        TableModel row = table_view_In_reciption.getSelectionModel().getSelectedItem();

        if (!new_patiant_In_reciptionFragment1.getText().isEmpty()) {

            if (date_picker_In_reciptionFragment1.getValue() == null
                    || time_picker_In_reciptionFragment1.getValue() == null) {

                // automatically fetch the current date and time for the user when the fields are empty after the first click
                date_picker_In_reciptionFragment1.setValue(LocalDate.now());
                time_picker_In_reciptionFragment1.setValue(LocalTime.now());
            }

            // on the second click
            else {

                try {

                    //insert message from the save button in main screen fragment 1 "MSF1"
                    messageToServer = "insertMSF1;" + row.getId() + "," + row.getName() + "," + date.toString() + "," + time.toString() + "";
                    n.output(messageToServer);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                error_fragment1.setVisible(false);

                date_picker_In_reciptionFragment1.setValue(null);
                time_picker_In_reciptionFragment1.setValue(null);

            }
        } else {
            error_fragment1.setVisible(true);
        }

    }

    // reception screen contents
    @FXML
    private JFXTextField new_patiant_In_reciptionFragment2 = new JFXTextField();
    @FXML
    private JFXDatePicker date_picker_In_reciptionFragment2;
    @FXML
    private JFXTimePicker time_picker_In_reciptionFragment2;
    @FXML
    private JFXRadioButton examin_In_reciptionFragment2;
    @FXML
    private JFXRadioButton followUP_In_reciptionFragment2;
    @FXML
    private Label error_fragment2;

    // go to payment fragment
    @FXML
    void nextPage2(ActionEvent event) {
        attendance.setVisible(false);
        reservation.setVisible(false);
        payment.setVisible(true);
    }

    // go back to appointment fragment
    @FXML
    void prevPage2(ActionEvent event) {
        attendance.setVisible(false);
        reservation.setVisible(true);
        payment.setVisible(false);
    }

    @FXML
    void new_patiant_button_In_reciptionFragment2(ActionEvent event) {

        clearFields();
        new_patiant_In_reciptionFragment2.setText("");
        date_picker_In_reciptionFragment2.setValue(null);
        time_picker_In_reciptionFragment2.setValue(null);
        examin_In_reciptionFragment2.setSelected(false);
        followUP_In_reciptionFragment2.setSelected(false);

        table_view_In_reciption.getSelectionModel().clearSelection();

        attended_In_reciption.setSelected(false);
        withAppointment_In_reciption.setSelected(false);

    }

    @FXML
    void save_patiant_button_In_reciptionFragment2(ActionEvent event) {

        LocalDate date = date_picker_In_reciptionFragment2.getValue();
        LocalTime time = time_picker_In_reciptionFragment2.getValue();
        TableModel row = table_view_In_reciption.getSelectionModel().getSelectedItem();

        if (examin_In_reciptionFragment2.isSelected() || followUP_In_reciptionFragment2.isSelected()) {
            if (new_patiant_In_reciptionFragment2.getText().isEmpty() || date_picker_In_reciptionFragment2.getValue() == null
                    || time_picker_In_reciptionFragment2.getValue() == null) {

                // automatically fetch the current date and time for the user when the fields are empty after the first click
                date_picker_In_reciptionFragment2.setValue(LocalDate.now());
                time_picker_In_reciptionFragment2.setValue(LocalTime.now());

            }

            // on the second click
            else {

                try {

                    //update message from the save button in main screen fragment 2 "MSF2"
                    //missing the visit count number from old code since no database access now
                    messageToServer = "updateMSF2;" + date.toString() + "," + time.toString() +
                            "," + visitType() + "," + attended() +
                            "," + withAppointment() + "," + row.getId() + "";
                    n.output(messageToServer);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                date_picker_In_reciptionFragment2.setValue(null);
                time_picker_In_reciptionFragment2.setValue(null);
                examin_In_reciptionFragment2.setSelected(false);
                followUP_In_reciptionFragment2.setSelected(false);

                error_fragment2.setVisible(false);

            }
        } else {
            error_fragment2.setVisible(true);
        }

    }

    private String visitType() {
        if (examin_In_reciptionFragment2.isSelected()) {
            return "كشفية";
        } else if (followUP_In_reciptionFragment2.isSelected()) {
            return "متابعة";
        }
        return "";
    }

    // reception screen components
    @FXML
    private JFXCheckBox attended_In_reciption;
    @FXML
    private JFXCheckBox withAppointment_In_reciption;

    private String attended() {

        if (attended_In_reciption.isSelected()) {
            return "حضر ";
        } else {
            return "لم يحضر";
        }

    }

    private String withAppointment() {

        if (withAppointment_In_reciption.isSelected()) {
            return "بموعد";
        } else {
            return "بدون موعد";
        }

    }

    // reception screen components
    @FXML
    private JFXTextField new_patiant_In_reciptionFragment3 = new JFXTextField();
    @FXML
    private JFXTextField payment_In_reciptionFragment3 = new JFXTextField();
    @FXML
    private JFXDatePicker date_picker_In_reciptionFragment3 = new JFXDatePicker();
    @FXML
    private Label error_fragment3;

    // go back to the attendance fragment
    @FXML
    void prevPage3(ActionEvent event) {
        attendance.setVisible(true);
        reservation.setVisible(false);
        payment.setVisible(false);
    }

    @FXML
    void new_patiant_button_In_reciptionFragment3(ActionEvent event) {
        clearFields();
        new_patiant_In_reciptionFragment3.setText("");
        payment_In_reciptionFragment3.setText("");
        date_picker_In_reciptionFragment3.setValue(null);

        table_view_In_reciption.getSelectionModel().clearSelection();

    }

    @FXML
    void save_patiant_button_In_reciptionFragment3(ActionEvent event) {

        LocalDate date = date_picker_In_reciptionFragment3.getValue();
        TableModel row = table_view_In_reciption.getSelectionModel().getSelectedItem();
        String payment = payment_In_reciptionFragment3.getText();

        // check for required fields
        if (payment.isEmpty()) {
            error_fragment3.setVisible(true);
        } else {

            if (new_patiant_In_reciptionFragment3.getText().isEmpty() || date_picker_In_reciptionFragment3.getValue() == null) {

                // automatically fetch the current date for the user when the fields are empty after the first click
                date_picker_In_reciptionFragment3.setValue(LocalDate.now());

            }

            // on the second click
            else {

                try {

                    //update message from the save button in main screen fragment 3 "MSF3"
                    messageToServer = "updateMSF3;" + date.toString() + "," + payment +
                            "," + row.getId() + "";
                    n.output(messageToServer);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                payment_In_reciptionFragment3.setText("");
                date_picker_In_reciptionFragment3.setValue(null);

                error_fragment3.setVisible(false);

            }

        }

    }

    //not important
    @FXML
    public ToggleGroup visitType;

}
