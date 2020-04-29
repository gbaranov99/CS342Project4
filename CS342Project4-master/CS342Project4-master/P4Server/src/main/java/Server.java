import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

public class Server {
    int count = 0;
    int totalPlayers = 0;
    HashMap<Integer, ArrayList> words = new HashMap<>();
    HashMap<Integer, String> currentWord = new HashMap<>();
    HashMap<Integer, GameInfo> games = new HashMap<>();
    ArrayList<ClientThread> clients = new ArrayList<>();
    TheServer server;
    private Consumer<Serializable> callback;
    int portNumber;
	Reader rd = new Reader();
    ArrayList<String> animals = rd.readFile("Animals.txt");
    ArrayList<String> instruments = rd.readFile("Instruments.txt");
    ArrayList<String> programs = rd.readFile("ProgrammingConcepts.txt");
    
    public static String displayWordFunc(String letter, String word,String previousDisplay, boolean init) {
    	if(init) {
    		for (int i = 0; i < word.length();i++ ) {
    			previousDisplay+="_";
    		}
    		return previousDisplay;
    	}
    	else {
    		

			char[] wordChar = word.toCharArray();
			char ch = letter.charAt(0);
			char[] displayWordLetters = previousDisplay.toCharArray();

			for (int i = 0; i < previousDisplay.length(); ++i) {
				if (wordChar[i] == ch) {
					displayWordLetters[i] = ch;
				}
			}
			
			return String.valueOf( displayWordLetters);

    	}
    	
    }

    Server(Consumer<Serializable> call, int portNum){

        callback = call;
        server = new TheServer();
        server.start();
        portNumber = portNum;

    }

      
    public class TheServer extends Thread {

        public void run() {

            try (ServerSocket mysocket = new ServerSocket(portNumber);) {
                System.out.println("Server is waiting for a client!");


                while (true) {

                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    count++;
                    totalPlayers++;
                    callback.accept("client has connected to server: " + "client ID" + totalPlayers);
                    clients.add(c);
                    callback.accept("Number of clients:" + count);

                    c.start();
                    

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

            System.out.println( "SENDING GAMEINFO OBJECT"); d.display(); // debugging info

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
                    System.out.println( "RECEIVING GAMEINFO OBJECT"); data.display(); // debugging info
                    if ( data.status.compareTo("Chose Category") == 0) {
                    	callback.accept("Client : " + data.clientID+ " Status: Chose Category");
                        String newWord = "";
                        Random random = new Random();
                        if(!words.containsKey(data.clientID)) {
                        	words.put(data.clientID, new ArrayList<String>());
                        }
                        if ( data.category == 0) {
                            do {
                                newWord = animals.get(random.nextInt(animals.size()));
                            }
                            while ( words.get( data.clientID).contains( newWord));
                            callback.accept("Client "+ data.clientID+ " chose the Animal category");
                            callback.accept("Client's given word is "+newWord);
                            callback.accept("------------------------------------");
                            ArrayList<String> temp = words.get(data.clientID);
                            temp.add(newWord);
                            words.put(data.clientID, temp);
                        }
                        if ( data.category == 1) {
                            do {
                                newWord = instruments.get(random.nextInt(instruments.size()));
                            }
                           while ( words.get( data.clientID).contains( newWord));
                            callback.accept("Client "+ data.clientID+ " chose the Instrument category");
                            callback.accept("Client's given word is "+newWord);
                            callback.accept("------------------------------------");
                           ArrayList<String> temp = words.get(data.clientID);
                           temp.add(newWord);
                           words.put(data.clientID, temp);
                        }
                        
                        if ( data.category == 2) {
                            do {
                                newWord = programs.get(random.nextInt(programs.size()));
                            }
                            while ( words.get( data.clientID).contains( newWord));
                            callback.accept("Client "+ data.clientID+ " chose the Programming Concepts category");
                            callback.accept("Client's given word is "+newWord);
                            callback.accept("------------------------------------");
                            ArrayList<String> temp = words.get(data.clientID);
                            temp.add(newWord);
                            words.put(data.clientID, temp);
                        }
                       
                        data.status = ""; // reset current game status
                        currentWord.put( data.clientID, newWord);

                        data.displayWord = displayWordFunc(null,newWord,"", true);
                    }
                    
                    if(data.status.equals("new game")){
                    	callback.accept("Client: "+data.clientID+" started a new games");
                        callback.accept("------------------------------------");
                    	words.put(data.clientID,new ArrayList<String>());
                    }

                    // if a letter guess is made, check if it is correct and update game info
                    if ( data.status.compareTo("Made Guess") == 0) {
                        data.status = "";
                        callback.accept("Client: "+data.clientID+" guessed: " + data.guessLetter);
                        callback.accept("------------------------------------");
                        String letter = data.guessLetter;
                        
                        if ( currentWord.get( new Integer( data.clientID)).contains( letter)) {
                            data.displayWord = displayWordFunc(letter, currentWord.get( new Integer( data.clientID)), data.displayWord,false);
                        }
                        else
                            data.numOfIncorrectLetters++;
                    }

                    // if word is guessed, let the client know
                    if ( data.displayWord.compareTo( currentWord.get( new Integer( data.clientID))) == 0)
                        data.status = "Round Won";

                    games.put(data.clientID, data); // keep track of game

                    updateClient( data.clientID, data);



                }
                catch(Exception e) {
                    System.out.println(e.getCause());
                    callback.accept("OOOOPPs...Something wrong with the socket from a client: " + "....closing down!");
                    //updateClients(currentGameInfo);
                    Server.this.count--;
                    //clients.remove(this);
                    
                    callback.accept("Number of clients:" + Server.this.count);
                    break;
                }
            }
        }//end of run


    }//end of client thread
}
