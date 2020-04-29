import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GuessTest {

	@Test
	void initTest() {
		Server serverConnection = new Server(null, 0);
		assertEquals("Server", serverConnection.getClass().getName(), "serverConnection not properly created");
	}

	@Test
	void initGameInfo() {
		GameInfo g = new GameInfo();
		assertEquals("GameInfo", g.getClass().getName(), "gameInfo not properly created");
	}

	@Test
	void initPortNum() {
		Server serverConnection = new Server(null, 5555);
		assertEquals(5555, serverConnection.portNumber, "port number not properly created");
	}

	@Test
	void TestDisplayWordFunc() {
		assertEquals("_____", Server.displayWordFunc("", "apple", "", true), "DisplayWordFunc not properly working");
	}

	@Test
	void TestDisplayWordFunc1() {
		assertEquals("a____", Server.displayWordFunc("a", "apple", "_____", false), "DisplayWordFunc not properly working");
	}

	@Test
	void TestDisplayWordFunc2() {
		assertEquals("_____", Server.displayWordFunc("a", "apple", "", true), "DisplayWordFunc not properly working");
	}

	@Test
	void TestDisplayWordFunc3() {
		assertEquals("a___e", Server.displayWordFunc("e", "apple", "a___e", false), "DisplayWordFunc not properly working");
	}

	@Test
	void TestDisplayWordFunc4() {
		assertEquals("app_e", Server.displayWordFunc("p", "apple", "app_e", false), "DisplayWordFunc not properly working");
	}

	@Test
	void TestGameInfoDefault() {
		GameInfo g = new GameInfo();
		assertEquals(0, g.animalGuesses, "gameInfo not properly created");
		assertEquals(0, g.instrumentGuesses, "gameInfo not properly created");
		assertEquals(0, g.programmingGuesses, "gameInfo not properly created");
		assertEquals(0, g.clientID, "gameInfo not properly created");
		assertEquals(0, g.category, "gameInfo not properly created");
		assertEquals(0, g.numOfIncorrectLetters, "gameInfo not properly created");
		assertEquals("", g.displayWord, "gameInfo not properly created");
		assertEquals("", g.status, "gameInfo not properly created");
	}

	@Test
	void TestGameInfoParamterized() {
		GameInfo g = new GameInfo(1, 2, 3, 4, 5, 6, "Greg", "Shawn", "Furqaan");
		assertEquals(1, g.animalGuesses, "gameInfo not properly created");
		assertEquals(2, g.instrumentGuesses, "gameInfo not properly created");
		assertEquals(3, g.programmingGuesses, "gameInfo not properly created");
		assertEquals(4, g.clientID, "gameInfo not properly created");
		assertEquals(5, g.category, "gameInfo not properly created");
		assertEquals(6, g.numOfIncorrectLetters, "gameInfo not properly created");
		assertEquals("Greg", g.displayWord, "gameInfo not properly created");
		assertEquals("Shawn", g.guessLetter, "gameInfo not properly created");
		assertEquals("Furqaan", g.status, "gameInfo not properly created");
	}

}