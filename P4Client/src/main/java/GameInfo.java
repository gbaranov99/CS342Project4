import java.io.Serializable;
public class GameInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public int animalGuesses;
    public int instrumentGuesses;
    public int programmingGuesses;
    public int clientID;
    public int category;
    public int numOfIncorrectLetters;
    public String displayWord;
    public String guessLetter;

	GameInfo() {
        this.animalGuesses = 0;
        this.instrumentGuesses = 0;
        this.programmingGuesses = 0;
        this.clientID = 0;
        this.category = 0;
        this.numOfIncorrectLetters = 0;
        this.displayWord = "";
        this.guessLetter = "";
	}

    GameInfo(int animalGuesses, int instrumentGuesses, int programmingGuesses, int clientID, int category,
             int numOfIncorrectLetters, String displayWord, String guessLetter) {
        this.animalGuesses = animalGuesses;
        this.instrumentGuesses = instrumentGuesses;
        this.programmingGuesses = programmingGuesses;
        this.clientID = clientID;
        this.category = category;
        this.numOfIncorrectLetters = numOfIncorrectLetters;
        this.displayWord = displayWord;
        this.guessLetter = guessLetter;
    }
}
