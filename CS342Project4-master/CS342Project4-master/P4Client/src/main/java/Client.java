import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Client extends Thread{

    String host;
    int portNumber;
	int ID = -1;


    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<String> letterBank = new ArrayList<>();
	GameInfo clientData;

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
				clientData = (GameInfo) in.readObject();
//				callback.accept(clientData);

                System.out.println( "RECEIVING GAMEINFO OBJECT"); clientData.display(); // debugging info

				if (ID == -1) {
					ID = clientData.clientID;
				}

            }
            catch(Exception e) {
                System.out.println("Exc entered");
            }
        }

    }

    public void send(GameInfo data) {

        try {
            System.out.println( "SENDING GAMEINFO OBJECT"); data.display(); // debugging info

            out.writeObject(data);
            out.reset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
