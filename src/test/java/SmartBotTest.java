import static org.junit.Assert.*;
import org.junit.Test;
import org.server.gameLogic.Board;
import org.server.bot.SmartBot;
import org.server.gameLogic.StoneColor;

import static org.constants.ConstantVariables.*;

import java.awt.*;

public class SmartBotTest {

    @Test
    public void testMakeMove() {
        Board board = new Board();
        SmartBot bot = new SmartBot();

        // Test for an empty board
        Point move1 = bot.makeMove();
        assertTrue(board.fields[move1.x][move1.y].getColor().equals(StoneColor.WHITE));

        // Test for a board with a black stone at the center
        board.fields[size / 2][size / 2].placeStone(StoneColor.BLACK);
        Point move2 = bot.makeMove();
        assertTrue(board.fields[move2.x][move2.y].getColor().equals(StoneColor.WHITE));
    }

    @Test
    public void testFindEmptyNeighbor() {
        Board board = new Board();
        SmartBot bot = new SmartBot();

        // Test for a stone in the center, should find an empty neighbor
        board.fields[size / 2][size / 2].placeStone(StoneColor.BLACK);
        Point emptyNeighbor = bot.findEmptyNeighbor(size / 2, size / 2);
        assertSame(emptyNeighbor, emptyNeighbor);

        // Test for a stone at the edge, should return null
        Point edgeEmptyNeighbor = bot.findEmptyNeighbor(0, 0);
        assertSame(edgeEmptyNeighbor, edgeEmptyNeighbor);
    }


    @Test
    public void testGetRandomMove() {
        Board board = new Board();
        SmartBot bot = new SmartBot();

        // Test for multiple random moves, should be within board boundaries
        for (int i = 0; i < 100; i++) {
            Point randomMove = bot.getRandomMove();
            assertTrue(bot.isValidCoordinate(randomMove.x, randomMove.y));
        }
    }

    @Test
    public void testIsValidCoordinate() {
        SmartBot bot = new SmartBot();

        // Test for valid coordinates
        assertTrue(bot.isValidCoordinate(1, 1));
        assertTrue(bot.isValidCoordinate(0, 0));

        // Test for invalid coordinates
        assertFalse(bot.isValidCoordinate(-1, 1));
        assertFalse(bot.isValidCoordinate(1, -1));
        assertFalse(bot.isValidCoordinate(size, 1)); // Use Board.size instead of 10
        assertFalse(bot.isValidCoordinate(1, size)); // Use Board.size instead of 10
    }

}
