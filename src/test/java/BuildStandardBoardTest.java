import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.server.gameLogic.Board;
import org.server.gameLogic.BuildStandardBoard;
import org.server.gameLogic.StoneColor;
import org.server.gameLogic.BoardBuilder;

public class BuildStandardBoardTest {

    @Test
    public void testIsStoneBreathing() {
        BoardBuilder builder = new BuildStandardBoard();
        Board board = new Board();
        board.buildBoard = builder;

        // Assuming initial state
        assertTrue(builder.isStoneBreathing(0, 0));

        // Assuming a stone is placed at (1, 0)
        board.placeStone(1, 0, StoneColor.BLACK, StoneColor.WHITE);
        board.placeStone(1, 1, StoneColor.BLACK, StoneColor.WHITE);
        board.placeStone(0, 1, StoneColor.BLACK, StoneColor.WHITE);
        assertFalse(builder.isStoneBreathing(0, 0));
    }

    @Test
    public void testIsKoViolation() {
        Board board = new Board();


        // Assuming initial state
        assertFalse(board.buildBoard.isKoViolation());

        // Assuming a move is made
        board.placeStone(0, 0, StoneColor.BLACK, StoneColor.WHITE);


        // Assuming another move is made
        board.placeStone(1, 0, StoneColor.WHITE, StoneColor.BLACK);
        board.placeStone(1, 1, StoneColor.WHITE, StoneColor.BLACK);
        board.placeStone(0, 1, StoneColor.WHITE, StoneColor.BLACK);


        // Assuming the same move is made (ko violation)
        board.placeStone(0, 0, StoneColor.BLACK, StoneColor.WHITE);
        assertTrue(board.buildBoard.isKoViolation());
    }

    @Test
    public void testIsValidCoordinate() {
        Board board = new Board();
        assertTrue(board.buildBoard.isValidCoordinate(0, 0));
        assertFalse(board.buildBoard.isValidCoordinate(-1, 0));
        assertFalse(board.buildBoard.isValidCoordinate(0, 20));
    }
}