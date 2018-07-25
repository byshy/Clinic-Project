package Database;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

// tis class is used to send messages to the server
public class Networking {

    private DataOutputStream toServer;
    private DataInputStream fromServer; //not used yet

    public Networking() {
        try {
            Socket socket = new Socket("localhost", 5555);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void output(String s) throws IOException {
        System.out.println(s);
        toServer.writeUTF(s);
        toServer.flush();
        System.out.println("Done");
    }
}
