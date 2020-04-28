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
    int count = 1;
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
			System.out.println("WORKS22");
			char ch = letter.charAt(0);
			System.out.println("WORKS23");
			char[] displayWordLetters = previousDisplay.toCharArray();
			System.out.println("WORKS24");

			for (int i = 0; i < previousDisplay.length(); ++i) {
				System.out.println("WORKS24.1");
				if (wordChar[i] == ch) {
					System.out.println("WORKS24.2");
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

                    // If a category has just been selected, assign the player a word from that category
                    // *** maybe turn bits of this into a function cuz there's a lot of repetitive code
                    // TODO: FIX: newWord = animals.get((int) Math.round(Math.random() * 100 % animals.size()));
                    // The random number generation is buggy and sometimes results in an exception (out of bound?)
                    // TODO: checking whether the word has already been played (use words Map - throwing me exception)
                    // TODO: after implementing the above, empty the map if the client presses play again
                    if ( data.status.compareTo("Chose Category") == 0) {
                        System.out.println("WORKS1");
                        String newWord = "";
                        Random random = new Random();
                        if(!words.containsKey(data.clientID)) {
                        	words.put(data.clientID, new ArrayList<String>());
                        }
                        if ( data.category == 0) {
                            System.out.println("WORKS2");
//                            data.animalGuesses++;
                            do {
                                newWord = animals.get(random.nextInt(animals.size()));
                            }
                            while ( words.get( data.clientID).contains( newWord));
                            ArrayList<String> temp = words.get(data.clientID);
                            temp.add(newWord);
                            words.put(data.clientID, temp);
                        }
                        if ( data.category == 1) {
                            System.out.println("WORKS2");
//                            data.instrumentGuesses++;
                            do {
                                newWord = instruments.get(random.nextInt(instruments.size()));
                            }
                           while ( words.get( data.clientID).contains( newWord));
                           ArrayList<String> temp = words.get(data.clientID);
                           temp.add(newWord);
                           words.put(data.clientID, temp);
                        }
                        
                        if ( data.category == 2) {
                            System.out.println("WORKS2");
//                            data.programmingGuesses++;
                            do {
                                newWord = programs.get(random.nextInt(programs.size()));
                            }
                            while ( words.get( data.clientID).contains( newWord) == false);
                            ArrayList<String> temp = words.get(data.clientID);
                            temp.add(newWord);
                            words.put(data.clientID, temp);
                        }
                        System.out.println(newWord);
                        System.out.println("WORKS3");
                        data.status = ""; // reset current game status
                        currentWord.put( data.clientID, newWord);
                        System.out.println("WORKS3.5");
//                        words.get( data.clientID).add( newWord);
                        System.out.println("WORKS4");

                        data.displayWord = displayWordFunc(null,newWord,"", true);
                    }


                    // if a letter guess is made, check if it is correct and update game info
                    if ( data.status.compareTo("Made Guess") == 0) {
                        System.out.println("WORKS20");
                        data.status = "";
                        String letter = data.guessLetter;
                        if ( currentWord.get( new Integer( data.clientID)).contains( letter)) {
                            System.out.println("WORKS21");
                             /*
                            char[] word = ( currentWord.get( new Integer( data.clientID))).toCharArray();
                            System.out.println("WORKS22");
                            char ch = letter.charAt(0);
                            System.out.println("WORKS23");
                            char[] displayWordLetters = data.displayWord.toCharArray();
                            System.out.println("WORKS24");

                            for ( int i = 0; i < data.displayWord.length(); ++i) {
                                System.out.println("WORKS24.1");
                                if ( word[ i] == ch) {
                                    System.out.println("WORKS24.2");
                                    displayWordLetters[ i] = ch;
                                }
                            }*/
                            System.out.println("WORKS24.3");
                            data.displayWord = displayWordFunc(letter, currentWord.get( new Integer( data.clientID)), data.displayWord,false);
                            System.out.println("WORKS25");
                        }
                        else
                            data.numOfIncorrectLetters++;
                    }

                    // if word is guessed, let the client know
                    if ( data.displayWord.compareTo( currentWord.get( new Integer( data.clientID))) == 0)
                        data.status = "Round Won";

                    games.put(data.clientID, data); // keep track of game
                    System.out.println("WORKS5");

                    updateClient( data.clientID, data);



                }
                catch(Exception e) {
                    System.out.println(e.getCause());
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
