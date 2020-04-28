import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{

    String host;
    int portNumber;



    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;

    private Consumer<Serializable> callback;

    Client(Consumer<Serializable> call, String hostString, int portNum){

        callback = call;
        host = hostString;
        portNumber = portNum;
    }

    public void run() { //"127.0.0.1"

        try {
            socketClient= new Socket(host,portNumber);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {}

        while(true) {

            try {

                //data = (MorraInfo)in.readObject();






            }
            catch(Exception e) {
                System.out.println("Exc entered");
            }
        }

    }

    public void send(int classToPass) {

        try {

            out.writeObject(null);
            out.reset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
