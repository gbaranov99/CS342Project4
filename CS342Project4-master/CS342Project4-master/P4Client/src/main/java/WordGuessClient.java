import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class WordGuessClient extends Application {

	// Variables to keep track of scenes and the client's data
	HashMap<String, Scene> sceneMap;
	Client clientConnection;
//	GameInfo myInfo;

	// Variables for Connect scene
	TextField ipAddress;
	TextField portNumber;
	Button connect;

	// Variables for Category scene
	Button chooseAnimal;
	Button chooseInstrument;
	Button chooseProgramming;
	
	// Variables for Playing scene
	Button start;
	Text curCategory;
	Text guessesLeft;
	Text curWord;
	Text playingMsg;
	TextField guessLetter;
	Button submitGuess;
	Button continueBtn;
	VBox playSceneVbox;
	Text letterBank;
	
	// Variables for Results scene
	Text results;
	Button returnToGame;
	Button playAgain;
	Button quit;
	VBox resultsSceneVbox;
	BackgroundImage bgImage;
	Background background;
	
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
		sceneMap.put("Results", createResultsScene());

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
//					myInfo = new GameInfo();
					
				});
			}, IP, port);

			clientConnection.start();
			primaryStage.setScene(sceneMap.get("Category"));
		});

		// Handles client choosing Animal Category
		chooseAnimal.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Playing"));
			for ( Node child : playSceneVbox.getChildren())
				child.setVisible( false);
			start.setVisible( true);

			clientConnection.clientData.category = 0;
			clientConnection.clientData.status = "Chose Category";
			clientConnection.send( clientConnection.clientData);
		});

		// Handles client choosing Instrument Category
		chooseInstrument.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Playing"));
			for ( Node child : playSceneVbox.getChildren())
				child.setVisible( false);
			start.setVisible( true);

			clientConnection.clientData.category = 1;
			clientConnection.clientData.status = "Chose Category";
			clientConnection.send( clientConnection.clientData);
		});

		// Handles client choosing Programming Category
		chooseProgramming.setOnAction(e -> {
			primaryStage.setScene(sceneMap.get("Playing"));
			for ( Node child : playSceneVbox.getChildren())
				child.setVisible( false);
			start.setVisible( true);

			clientConnection.clientData.category = 2;
			clientConnection.clientData.status = "Chose Category";
			clientConnection.send( clientConnection.clientData);
		});

		// Handles setting start data for playing screen
		start.setOnAction(e -> {

			for ( Node child : playSceneVbox.getChildren())
				child.setVisible( true);
			start.setVisible( false);
			continueBtn.setVisible( false);

			if ( clientConnection.clientData.category == 0) {
				bgImage = new BackgroundImage(new Image("wolf.jpg"), null, null, null, null);
				background = new Background(bgImage);
				playSceneVbox.setBackground(background);
				
				curCategory.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				guessesLeft.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				curWord.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				letterBank.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				playingMsg.setFill(Color.BLACK);
				letterBank.setFill(Color.BLACK);
				curCategory.setFill(Color.BLACK);
				guessesLeft.setFill(Color.BLACK);
				curWord.setFill(Color.BLACK);
				letterBank.setFill(Color.BLACK);
				curCategory.setText("Category: Animal");
				
			}
			else if ( clientConnection.clientData.category == 1) {
				bgImage = new BackgroundImage(new Image("guitar.jpg"), null, null, null, null);
				background = new Background(bgImage);
				playSceneVbox.setBackground(background);
				curCategory.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				guessesLeft.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				curWord.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				letterBank.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				playingMsg.setFill(Color.RED);
				letterBank.setFill(Color.RED);
				curCategory.setFill(Color.BLACK);
				guessesLeft.setFill(Color.BLACK);
				curWord.setFill(Color.RED);
				letterBank.setFill(Color.BLACK);
				curCategory.setText("Category: Instrument");
			}
			else if ( clientConnection.clientData.category == 2) {
				bgImage = new BackgroundImage(new Image("coolguy.jpg"), null, null, null, null);
				background = new Background(bgImage);
				playSceneVbox.setBackground(background);
				curCategory.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				guessesLeft.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				curWord.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				letterBank.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
				playingMsg.setFill(Color.WHITE);
				letterBank.setFill(Color.WHITE);
				curCategory.setFill(Color.WHITE);
				guessesLeft.setFill(Color.WHITE);
				curWord.setFill(Color.WHITE);
				letterBank.setFill(Color.WHITE);
				curCategory.setText("Category: Programming");
			}

			guessesLeft.setText("You have 6 guesses left");

			String s = "";
			for ( int i = 0; i < clientConnection.clientData.displayWord.length(); ++i)
				s += "_ ";
			curWord.setText( s);
		});

		// Handles client sending guess letter to server
		submitGuess.setOnAction(e -> {
			// TODO: make this thing work
			String guess = guessLetter.getText();
			if ( guess.length() == 1) {
				clientConnection.clientData.guessLetter = guess;
				clientConnection.clientData.status = "Made Guess";
				clientConnection.send( clientConnection.clientData);
				clientConnection.letterBank.add(guess);
				submitGuess.setDisable( true);
				continueBtn.setVisible( true);
				String s = "Letters you have guessed so far: ";
	            for ( int i = 0; i < clientConnection.letterBank.size(); ++i)
	                s += clientConnection.letterBank.get(i) + " ";
	            letterBank.setText( s);
			}
			else {
				guessLetter.setText("invalid! please guess a letter");
			}

		});

		// update all play screen data and check for round win/loss
		continueBtn.setOnAction(e -> {
			if ( clientConnection.clientData.status.compareTo("Round Won") == 0) {
				clientConnection.clientData.status = ""; // reset status
				if ( clientConnection.clientData.category == 0) {
					chooseAnimal.setDisable(true);
					clientConnection.clientData.animalGuesses = -1;
				}
				else if ( clientConnection.clientData.category == 1) {
					chooseInstrument.setDisable(true);
					clientConnection.clientData.instrumentGuesses = -1;
				}
				else if ( clientConnection.clientData.category == 2) {
					chooseProgramming.setDisable(true);
					clientConnection.clientData.programmingGuesses = -1;
				}

				primaryStage.setScene(sceneMap.get("Results"));

				if ( clientConnection.clientData.animalGuesses == -1
						&& clientConnection.clientData.instrumentGuesses == -1
						&& clientConnection.clientData.programmingGuesses == -1) {
					results.setText( "You Won the Game !");
					bgImage = new BackgroundImage(new Image("endgame.jpg"), null, null, null, null);
					background = new Background(bgImage);
					resultsSceneVbox.setBackground(background);
					results.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
					results.setFill(Color.WHITE);
					results.setVisible( true);
					playAgain.setVisible( true);
					quit.setVisible( true);
				}
				else {
					results.setText( "You Won the Round ! The word was " + clientConnection.clientData.displayWord);
					bgImage = new BackgroundImage(new Image("default.jpg"), null, null, null, null);
					background = new Background(bgImage);
					resultsSceneVbox.setBackground(background);
					returnToGame.setVisible( true);
					results.setVisible( true);
				}
			}
			else if ( clientConnection.clientData.numOfIncorrectLetters >= 6) {
				if ( clientConnection.clientData.category == 0)
					clientConnection.clientData.animalGuesses++;

				else if ( clientConnection.clientData.category == 1)
					clientConnection.clientData.instrumentGuesses++;

				else if ( clientConnection.clientData.category == 2)
					clientConnection.clientData.programmingGuesses++;

				primaryStage.setScene(sceneMap.get("Results"));

				if ( clientConnection.clientData.animalGuesses >= 3
						|| clientConnection.clientData.instrumentGuesses >= 3
						|| clientConnection.clientData.programmingGuesses >= 3) {
					results.setText("You Lost the Game !");
					bgImage = new BackgroundImage(new Image("endgame.jpg"), null, null, null, null);
					background = new Background(bgImage);
					resultsSceneVbox.setBackground(background);
					results.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
					results.setFill(Color.WHITE);
					results.setVisible( true);
					playAgain.setVisible(true);
					quit.setVisible(true);
				}
				else {
					results.setText( "You Lost the Round !");
					bgImage = new BackgroundImage(new Image("default.jpg"), null, null, null, null);
					background = new Background(bgImage);
					resultsSceneVbox.setBackground(background);
					results.setVisible( true);
					returnToGame.setVisible( true);
				}
			}

			clientConnection.send( clientConnection.clientData);

			submitGuess.setDisable( false);
			continueBtn.setVisible( false);
			guessLetter.clear();

			String s = "You have " + (6 - clientConnection.clientData.numOfIncorrectLetters) + " guesses left";
			guessesLeft.setText( s);

			s = "";
			for ( int i = 0; i < clientConnection.clientData.displayWord.length(); ++i)
				s += clientConnection.clientData.displayWord.charAt( i) + " ";
			curWord.setText( s);

		});

		// reset data and return to choose category screen
		returnToGame.setOnAction(e -> {
			clientConnection.clientData.category = -1;
			clientConnection.clientData.numOfIncorrectLetters = 0;
			clientConnection.clientData.displayWord = "";
			clientConnection.clientData.guessLetter = "";
			clientConnection.clientData.status = "";
			clientConnection.letterBank.clear();
			bgImage = new BackgroundImage(new Image("default.jpg"), null, null, null, null);
			background = new Background(bgImage);
			playSceneVbox.setBackground(background);
			letterBank.setText("Letters you have guessed so far: ");
			primaryStage.setScene(sceneMap.get("Category"));
			clientConnection.send( clientConnection.clientData);
			for ( Node child : resultsSceneVbox.getChildren())
				child.setVisible( false);
			resultsSceneVbox.setVisible( true);
		});

		// reset all data and return to category screen
		// TODO: reset words map on server side as well
		playAgain.setOnAction(e -> {
			clientConnection.clientData.animalGuesses = 0;
			clientConnection.clientData.instrumentGuesses = 0;
			clientConnection.clientData.programmingGuesses = 0;
			clientConnection.clientData.category = -1;
			clientConnection.clientData.numOfIncorrectLetters = 0;
			clientConnection.clientData.displayWord = "";
			clientConnection.clientData.guessLetter = "";
			clientConnection.clientData.status = "new game";
			clientConnection.letterBank.clear();
			letterBank.setText("Letters you have guessed so far: ");
			primaryStage.setScene(sceneMap.get("Category"));
			clientConnection.send( clientConnection.clientData);
			for ( Node child : resultsSceneVbox.getChildren())
				child.setVisible( false);
			resultsSceneVbox.setVisible( true);
			chooseAnimal.setDisable( false);
			chooseInstrument.setDisable( false);
			chooseProgramming.setDisable( false);
			bgImage = new BackgroundImage(new Image("default.jpg"), null, null, null, null);
			background = new Background(bgImage);
			playSceneVbox.setBackground(background);
		});

		quit.setOnAction(e -> {
			Platform.exit();
			System.exit(0);
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
		bgImage = new BackgroundImage(new Image("default.jpg"), null, null, null, null);
		background = new Background(bgImage);
		vbox.setBackground(background);
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
		start = new Button("Start");
		continueBtn = new Button( "Continue");

		letterBank = new Text("Letters you have guessed so far: ");
		curCategory = new Text("Current Category: ");
		guessesLeft = new Text("You have 6 guesses left");

		playingMsg = new Text("Your word so far is: ");
		curWord = new Text( "?");
		HBox hbox1 = new HBox(playingMsg, curWord);

		hbox1.setAlignment(Pos.CENTER);
		Text guessMsg = new Text("Guess a letter: ");
		guessLetter = new TextField("");
		HBox hbox2 = new HBox(guessMsg, guessLetter);
		hbox2.setAlignment(Pos.CENTER);

		submitGuess = new Button("Finalize Guess");

		playSceneVbox = new VBox(start,letterBank, curCategory, guessesLeft, hbox1, hbox2, submitGuess, continueBtn);
		playSceneVbox.setSpacing(20);
		playSceneVbox.setAlignment(Pos.CENTER);

		for ( Node child : playSceneVbox.getChildren())
			child.setVisible( false);
		start.setVisible( true);

		BorderPane pane = new BorderPane();
		pane.setCenter(playSceneVbox);

		return new Scene(pane, 500, 500);
	}

	// Scene that shows round/game win/loss and allows to return to game / play again /quit
	public Scene createResultsScene() {
		results = new Text("");
		returnToGame = new Button("Return To Game");
		playAgain = new Button("Play Again");
		quit = new Button("Quit");

		resultsSceneVbox = new VBox( results, returnToGame, playAgain, quit);
		resultsSceneVbox.setSpacing(20);
		resultsSceneVbox.setAlignment(Pos.CENTER);

		for ( Node child: resultsSceneVbox.getChildren())
			child.setVisible( false);
		results.setVisible( true);

		BorderPane pane = new BorderPane();
		pane.setCenter(resultsSceneVbox);

		return new Scene(pane, 500, 500);
	}

}
