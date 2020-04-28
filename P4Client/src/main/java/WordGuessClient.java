import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class WordGuessClient extends Application {

	// Variables to keep track of scenes and the client's data
	HashMap<String, Scene> sceneMap;
	Client clientConnection;
	GameInfo myInfo;

	// Variables for Connect scene
	TextField ipAddress;
	TextField portNumber;
	Button connect;

	// Variables for Category scene
	Button chooseAnimal;
	Button chooseInstrument;
	Button chooseProgramming;
	
	// Variables for Playing scene
	Text curCategory;
	Text guessesLeft;
	Text curWord;
	TextField guessLetter;
	Button submitGuess;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("(Client) Word Guess!!!");

		// Creates scenes for starting game, choosing category and playing game
		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("Connect", createConnectScene());
		sceneMap.put("Category", createCategoryScene());
		sceneMap.put("Playing", createPlayingScene());

		// Event handlers for all buttons are initialized in start method
		// Allows them to be handled no matter what the current scene is
		//
		// Handles connect button
		connect.setOnAction(e -> {
			String IP = ipAddress.getText();
			int port = Integer.parseInt(portNumber.getText());
			clientConnection = new Client(data -> {
				Platform.runLater(()->{
					// TODO: figure out how to connect GUI to client
					//listItems.getItems().addAll(new MorraInfo(0,0,0,0,0,0,1,1,0));
					myInfo = new GameInfo();
				});
			}, IP, port);

			clientConnection.start();
			primaryStage.setScene(sceneMap.get("Category"));
		});

		// Handles client choosing Animal Category
		chooseAnimal.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Playing"));
		});

		// Handles client choosing Instrument Category
		chooseInstrument.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Playing"));
		});

		// Handles client choosing Programming Category
		chooseProgramming.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Playing"));
		});

		// Handles client sending guess letter to server
		submitGuess.setOnAction(e -> {
			// TODO: make this thing work
			System.out.println("hahalol");
		});


		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.setScene(sceneMap.get("Connect"));
		primaryStage.show();
	}

	// Scene that allows client to connect to a server with a given IP and port number
	public Scene createConnectScene() {
		Text connectMsg = new Text("Enter the ip and port of game to connect to:");

		ipAddress = new TextField("127.0.0.1");
		portNumber = new TextField("5555");
		HBox inputBox = new HBox();
		inputBox.getChildren().addAll(ipAddress, portNumber);
		connect = new Button("Connect to game");

		BorderPane pane = new BorderPane();
		pane.setTop(connectMsg);
		pane.setCenter(inputBox);
		pane.setBottom(connect);

		return new Scene(pane, 500, 500);
	}

	// Scene that allows user to pick a category
	// If a user has won in a category, that category is disabled
	public Scene createCategoryScene() {
		Text chooseCategory = new Text("Welcome to WordGuess!\n" +
				"Pick a category below and start yo guessin!");

		chooseAnimal = new Button("Animals!");
		chooseInstrument = new Button("Instruments!");
		chooseProgramming = new Button("PROGRAMMING CONCEPTS!!!");

		VBox vbox = new VBox(chooseAnimal, chooseInstrument, chooseProgramming);
		vbox.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setTop(chooseCategory);
		pane.setCenter(vbox);

		return new Scene(pane, 500, 500);
	}

	// Scene where gamplay occurs
	public Scene createPlayingScene() {
		// TODO: use client data for word here, number of categories and guesses left
		curCategory = new Text("Current Category: ");
		guessesLeft = new Text("You have _ guesses left");

		Text playingMsg = new Text("Your word so far is: ");
		curWord = new Text("*Word here*");
		HBox hbox1 = new HBox(playingMsg, curWord);

		hbox1.setAlignment(Pos.CENTER);
		Text guessMsg = new Text("Guess a letter: ");
		guessLetter = new TextField("");
		HBox hbox2 = new HBox(guessMsg, guessLetter);
		hbox2.setAlignment(Pos.CENTER);

		submitGuess = new Button("Submit Guess");

		VBox vbox = new VBox(curCategory, guessesLeft, hbox1, hbox2, submitGuess);
		vbox.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(vbox);

		return new Scene(pane, 500, 500);
	}

	// TODO: Create a play again scene if user loses or wins?
}
