import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class Server {
    int count = 1;
    int totalPlayers = 1;
    HashMap<Integer, ArrayList> words = new HashMap<>();
    HashMap<Integer, GameInfo> games = new HashMap<>();
    ArrayList<ClientThread> clients = new ArrayList<>();
    TheServer server;
    private Consumer<Serializable> callback;
    int portNumber;
    ArrayList<String> animals = fileToArray("Animals.txt");
    ArrayList<String> instruments = fileToArray("Instruments.txt");
    ArrayList<String> programs = fileToArray("ProgrammingConcepts.txt");


    Server(Consumer<Serializable> call, int portNum){

        callback = call;
        server = new TheServer();
        server.start();
        portNumber = portNum;

    }

    public static ArrayList<String> fileToArray(String fileName) {
        try {
            //FileInputStream fis = new FileInputStream(fileName);
            Scanner sc = new Scanner(new File(fileName));    //file to be scanned
            ArrayList<String> arr = new ArrayList<>();
            while (sc.hasNextLine()) {
                String word = sc.nextLine();
                arr.add(word);
            }
            sc.close();
            return arr;
        }
        catch (Exception e) {
            System.out.println("fileToArray error." + e.getMessage());
            return null;
        }
    }
    public class TheServer extends Thread {

        public void run() {

            try (ServerSocket mysocket = new ServerSocket(portNumber);) {
                System.out.println("Server is waiting for a client!");


                while (true) {

                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    clients.add(c);
                    callback.accept("Number of clients:" + clients.size());

                    c.start();
                    count++;
                    totalPlayers++;

                }
            }//end of try
            catch (Exception e) {
                callback.accept("Server socket did not launch");
            }
        }//end of while
    }


    class ClientThread extends Thread{


        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        public void updateClient(int ID, GameInfo d) {

            ClientThread t = clients.get(ID-1);
            try {
                t.out.writeObject(d);
                t.out.reset();
            }
            catch(Exception e) {}

        }

        public void run(){

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            updateClient(totalPlayers, new GameInfo(0, 0, 0, totalPlayers, -1,
            0, "", "", ""));

            while(true) {
                try {

                    GameInfo data = (GameInfo) in.readObject(); // read in data

                    if (data.status.equals("category chosen")) {
                        if (data.category == 0) { // animal
                            String newWord;

                        }
                        else if (data.category == 1) { // instruments
                            String newWord;
                        }
                        else if (data.category == 2) { // programming
                            String newWord;
                        }
                    }
/*
                    if (data.category != -1) {

                        if (data.category == 0) { // animal
                            if (data.numOfIncorrectLetters == 6) {
                                data.category = -1;
                                data.animalGuesses++;
                                data.numOfIncorrectLetters = 0;
                            }

                        }
                        else if (data.category == 1) { // instruments

                        }
                        else if (data.category == 2) { // programming

                        }*/



                        games.put(data.clientID, data); // keep track of game

                }
                catch(Exception e) {
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    //updateClients(currentGameInfo);
                    //clients.remove(this);
                    callback.accept("Number of clients:" + clients.size());
                    break;
                }
            }
        }//end of run


    }//end of client thread
}
