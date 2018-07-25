package Database;

import Clinic.QueriesController;
import Clinic.ReciptionController;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// this class is used to process the messages from the client
public class Networking {

    private QueriesController qc;
    private ReciptionController rc;
    private DBConnection db;
    {
        try {
            qc = new QueriesController();
            rc = new ReciptionController();
            db = new DBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection con = db.getCon();
    private PreparedStatement ps;

    // this method is called in the reception class after creating an instance of this class (may be important to know)
    public void networkConnection() {
        new Thread(() -> {

            try {
                ServerSocket ss = new ServerSocket(5555);
                Socket s = ss.accept();

                DataInputStream inputFromClient = new DataInputStream(s.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(s.getOutputStream());

                while (true) {
                    // messages received from the client contain the command and the information of the message separated by ";"
                    // and the content of the message are separated by "," and every thing gets split in the following code

                    String messageFromClient = inputFromClient.readUTF(); // receive the message

                    //check the command gained from client
                    System.out.println(messageFromClient);

                    if (!messageFromClient.isEmpty()){

                        // first split to get the command
                        String[] commandInfo = messageFromClient.split(";");

                        String[] messageContent; // wide scope for multiple use
                        PreparedStatement ps2;
                        ResultSet rs;

                        switch (commandInfo[0]){

                            case "insertMS" :

                                messageContent = commandInfo[1].split(",");

                                try {
                                    ps = con.prepareStatement("insert into `patiantInfo` values(null,?,?,?,?,?);");
                                    ps.setString(1, messageContent[0]);
                                    ps.setString(2, messageContent[1]);
                                    ps.setString(3, messageContent[2]);
                                    ps.setString(4, messageContent[3]);
                                    ps.setString(5, messageContent[4]);

                                    ps.executeUpdate();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

//                                rc.clearTmOLAndLoad();

                                break;

                            case "deleteMS" :

                                try {
                                    ps = con.prepareStatement("delete from patiantInfo where id = ?");
                                    ps.setString(1, commandInfo[1]);

                                    qc.ps = con.prepareStatement("delete from visitInfo where patiantID = ?");
                                    qc.ps.setString(1, commandInfo[1]);

                                    ps.executeUpdate();
                                    qc.ps.executeUpdate();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

//                                rc.clearTmOLAndLoad();

                                break;

                            case "editMS" :

                                try{

                                    messageContent = commandInfo[1].split(",");

                                    ps = con.prepareStatement("update patiantInfo set name = ?," +
                                            " age = ?," +
                                            " sex = ?," +
                                            " address = ?," +
                                            " connect = ?" +
                                            " where id = ?");
                                    ps.setString(1, messageContent[0]);
                                    ps.setString(2, messageContent[1]);
                                    ps.setString(3, messageContent[2]);
                                    ps.setString(4, messageContent[3]);
                                    ps.setString(5, messageContent[4]);
                                    ps.setString(6, messageContent[5]);

                                    ps.executeUpdate();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

//                                rc.clearTmOLAndLoad();

                                break;

                            case "insertMSF1" :

                                try {

                                    messageContent = commandInfo[1].split(",");

                                    qc.ps = qc.con.prepareStatement("insert into visitInfo values (null,?,?,?,?,null,null,null,null,null,null,null) ");
                                    qc.ps.setString(1, messageContent[0]);
                                    qc.ps.setString(2, messageContent[1]);
                                    qc.ps.setString(3, messageContent[2]);
                                    qc.ps.setString(4, messageContent[3]);
                                    qc.ps.executeUpdate();

                                    ps2 = con.prepareStatement("select num from visitCount");
                                    rs = ps2.executeQuery();

                                    int num;
                                    while (rs.next()) {
                                        num = rs.getInt("num");
                                        num++;
                                        ps = con.prepareStatement("update visitCount set num = ?");
                                        ps.setString(1, String.valueOf(num));
                                        ps.executeUpdate();
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

//                                rc.clearTmOLAndLoad();

                                break;

                            case "updateMSF2" :

                                try{

                                    messageContent = commandInfo[1].split(",");

                                    qc.ps = qc.con.prepareStatement("update visitInfo set attend_date = ?," +
                                            " attend_time = ?," +
                                            " visit_type = ?," +
                                            " attend = ?," +
                                            " attend_type = ?" +
                                            " where patiantID = ? and id = ? ");

                                    ps2 = con.prepareStatement("select num from visitCount");
                                    rs = ps2.executeQuery();

                                    qc.ps.setString(1, messageContent[0]);
                                    qc.ps.setString(2, messageContent[1]);
                                    qc.ps.setString(3, messageContent[2]);
                                    qc.ps.setString(4, messageContent[3]);
                                    qc.ps.setString(5, messageContent[4]);
                                    qc.ps.setString(6, messageContent[5]);
                                    while(rs.next()){
                                        qc.ps.setString(7, rs.getString("num"));
                                    }

                                    qc.ps.executeUpdate();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

//                                rc.clearTmOLAndLoad();

                                break;

                            case "updateMSF3" :

                                try{

                                    messageContent = commandInfo[1].split(",");

                                    qc.ps = qc.con.prepareStatement("update visitInfo set payment_date = ?," +
                                            " payment_value = ?" +
                                            " where patiantID = ? and id = ? ");

                                    ps2 = con.prepareStatement("select num from visitCount");
                                    rs = ps2.executeQuery();

                                    qc.ps.setString(1, messageContent[0]);
                                    qc.ps.setString(2, messageContent[1]);
                                    qc.ps.setString(3, messageContent[2]);
                                    while(rs.next()){
                                        qc.ps.setString(4, rs.getString("num"));
                                    }

                                    qc.ps.executeUpdate();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

//                                rc.clearTmOLAndLoad();

                                break;

                            case "deleteES" :
                                System.out.println("done ");
                                System.out.println(commandInfo[1]);

                                try {

                                    qc.ps = con.prepareStatement("delete from visitInfo where id = ? ");
                                    qc.ps.setString(1, commandInfo[1]);

                                    qc.ps.executeUpdate();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                break;

                            case "" :



                                break;

                        }

                    }

                }

            } catch (SocketException e){
                System.out.println("\nuser disconnected \n");
            } catch (Exception e){
                e.printStackTrace();
            }

        }).start();

    }

}
