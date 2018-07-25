package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// this class is used to give connection to other classes and make the number of classes connected with the database small
public class DBConnection {

    private Connection con = DriverManager.getConnection("jdbc:mysql://localhost/clinic?useSSL=false",
            "nClinic", "GazaSkyGeeks");

    public Connection getCon() {
        return con;
    }

    public DBConnection() throws SQLException {
    }

}
