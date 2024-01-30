import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.server.database.Game;
import org.server.database.GameDAO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTests {

    private GameDAO gameDAO;

    @BeforeEach
    void setUp() {
        // Inicjalizacja GameDAO i ewentualne konfiguracje
        gameDAO = new GameDAO();
    }

    @Test
    void testSaveAndRetrieveGame() {
        GameDAO gameDAO = new GameDAO();
        Game game = new Game();
        game.setStartTime(new Date());
        game.setEndTime(new Date());

        gameDAO.saveGame(game);

        Game retrievedGame = gameDAO.getGameById(game.getId());
        assertNotNull(retrievedGame);

        assertDatePartsEqual(game.getStartTime(), retrievedGame.getStartTime());
        assertDatePartsEqual(game.getEndTime(), retrievedGame.getEndTime());
    }

    private void assertDatePartsEqual(Date expected, Date actual) {
        Calendar calExpected = Calendar.getInstance();
        calExpected.setTime(expected);
        Calendar calActual = Calendar.getInstance();
        calActual.setTime(actual);

        assertEquals(calExpected.get(Calendar.YEAR), calActual.get(Calendar.YEAR));
        assertEquals(calExpected.get(Calendar.MONTH), calActual.get(Calendar.MONTH));
        assertEquals(calExpected.get(Calendar.DAY_OF_MONTH), calActual.get(Calendar.DAY_OF_MONTH));
        assertEquals(calExpected.get(Calendar.HOUR_OF_DAY), calActual.get(Calendar.HOUR_OF_DAY));
        assertEquals(calExpected.get(Calendar.MINUTE), calActual.get(Calendar.MINUTE));
    }

    @Test
    void testGetAllGames() {
        GameDAO gameDAO = new GameDAO();
        List<Game> games = gameDAO.getAllGames();

        assertNotNull(games);
        assertTrue(games.size() > 0);
    }

    @Test
    void testUpdateGame() {
        GameDAO gameDAO = new GameDAO();

        // Utwórz i zapisz nową grę
        Game game = new Game();
        game.setStartTime(new Date());
        gameDAO.saveGame(game);

        // Aktualizuj grę
        Date newEndTime = new Date();
        game.setEndTime(newEndTime);
        gameDAO.updateGame(game);

        // Pobierz i sprawdź aktualizację
        Game updatedGame = gameDAO.getGameById(game.getId());
        assertDatePartsEqual(newEndTime, updatedGame.getEndTime());
    }

    // Możesz dodać więcej testów związanych z GameDAO tutaj
}

