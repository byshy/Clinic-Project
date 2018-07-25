package Clinic;

import Database.DBConnection;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class QueriesController implements Initializable {

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
    public Connection con = db.getCon();
    private ObservableList<Queries> appointmentOL = FXCollections.observableArrayList();
    public PreparedStatement ps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    // populate the table with data
    public void loadData() {

        // connect the table column with the attributes of the patient from the Queries class
        id_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("pstiantID"));
        name_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        visitType_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("visit_type"));
        reserveDate_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("reserveDate"));
        reserveTime_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("reserveTime"));
        attendDate_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("attendDate"));
        attendTime_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("attendTime"));
        payment_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("payment"));
        paymentDate_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        attendance_In_queriesTable.setCellValueFactory(new PropertyValueFactory<>("attend"));
        attendanceType_In_queriesTable1.setCellValueFactory(new PropertyValueFactory<>("attend_type"));

        try {

            // database related code "MySql"
            ps = con.prepareStatement("select * from visitInfo");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                // load data to the observable list
                appointmentOL.add(new Queries(new SimpleStringProperty(rs.getString("id")),
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
            table_view_in_queries.setItems(appointmentOL);

            ps.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // filter the table based on more then one attribute
        // TODO missing the filtering based on date
        ObjectProperty<Predicate<Queries>> nameFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<Queries>> visitTypeFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<Queries>> attendedFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<Queries>> withAppFilter = new SimpleObjectProperty<>();
//        ObjectProperty<Predicate<Queries>> fromDate = new SimpleObjectProperty<>();
//        ObjectProperty<Predicate<Queries>> toDate = new SimpleObjectProperty<>();

        nameFilter.bind(Bindings.createObjectBinding(() ->
                        row -> row.getName().contains(patiant_name_In_queries.getText()),
                patiant_name_In_queries.textProperty()));

        visitTypeFilter.bind(Bindings.createObjectBinding(() ->
                        row -> row.getVisit_type().contains(returnVisitType()),
                visitType3.selectedToggleProperty()));

        if (!rb_attended.isSelected() && !rb_didnt_attend.isSelected()) {
            attendedFilter.bind(Bindings.createObjectBinding(() ->
                            row -> row.getAttend().contains(returnAttended()),
                    attended.selectedToggleProperty()));
        } else if (rb_attended.isSelected() || rb_didnt_attend.isSelected()) {
            attendedFilter.bind(Bindings.createObjectBinding(() ->
                            row -> row.getAttend().equals(returnAttended()),
                    attended.selectedToggleProperty()));
        }

        withAppFilter.bind(Bindings.createObjectBinding(() ->
                        row -> row.getAttend_type().contains(returnAttendedType()),
                attendType.selectedToggleProperty()));

//        fromDate.bind(Bindings.createObjectBinding(() ->
//                row -> row.getAttendDate().contains() ),
//                );

        FilteredList<Queries> filteredData = new FilteredList<>(appointmentOL, p -> true);

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(
                () -> nameFilter.get().and(visitTypeFilter.get()).and(attendedFilter.get()).and(withAppFilter.get()),
                nameFilter, visitTypeFilter, attendedFilter, withAppFilter));

        table_view_in_queries.setItems(filteredData);

    }

    private String returnVisitType() {
        if (rb_examination.isSelected()) {
            return "كشفية";
        } else if (rb_revision.isSelected()) {
            return "متابعة";
        }
        return "";
    }

    private String returnAttended() {
        if (rb_attended.isSelected()) {
            // the space after حضر is for the code to separate the two values right
            // and display them in the Queries interface
            return "حضر ";
        } else if (rb_didnt_attend.isSelected()) {
            return "لم يحضر";
        }
        return " ";
    }

    private String returnAttendedType() {
        if (rb_with_app.isSelected()) {
            return "بموعد";
        } else if (rb_without_app.isSelected()) {
            return "بدون موعد";
        }
        return "";
    }

    // queries screen components
    @FXML
    private ToggleGroup visitType3 = new ToggleGroup();
    @FXML
    private ToggleGroup attended = new ToggleGroup();
    @FXML
    private ToggleGroup attendType = new ToggleGroup();
    @FXML
    private JFXTextField patiant_name_In_queries = new JFXTextField();
    @FXML
    private JFXDatePicker date_picker1_In_queries = new JFXDatePicker();
    @FXML
    private JFXDatePicker date_picker2_In_queries = new JFXDatePicker();
    @FXML
    private JFXRadioButton rb_examination = new JFXRadioButton();
    @FXML
    private JFXRadioButton rb_revision = new JFXRadioButton();
    @FXML
    private JFXRadioButton rb_attended = new JFXRadioButton();
    @FXML
    private JFXRadioButton rb_didnt_attend = new JFXRadioButton();
    @FXML
    private JFXRadioButton rb_with_app = new JFXRadioButton();
    @FXML
    private JFXRadioButton rb_without_app = new JFXRadioButton();
    @FXML
    private TableView<Queries> table_view_in_queries = new TableView<>();
    @FXML
    private TableColumn<Queries, String> id_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> name_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> visitType_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> reserveDate_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> reserveTime_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> attendDate_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> attendTime_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> payment_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> paymentDate_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> attendance_In_queriesTable = new TableColumn<>();
    @FXML
    private TableColumn<Queries, String> attendanceType_In_queriesTable1 = new TableColumn<>();

    @FXML
    void clearAll(ActionEvent event) {
        patiant_name_In_queries.setText("");
        rb_examination.setSelected(false);
        rb_revision.setSelected(false);
        rb_attended.setSelected(false);
        rb_didnt_attend.setSelected(false);
        rb_with_app.setSelected(false);
        rb_without_app.setSelected(false);
        appointmentOL.clear();
        loadData();
    }

    @FXML
    void refreshTable(ActionEvent event) {
        // TODO make the table auto refresh when the FXML is loaded instead of clicking refreshTable button
        appointmentOL.clear();
        loadData();
    }

}
