import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.server.Board;
import org.server.BuildStandardBoard;
import org.server.StoneColor;

public class BuildStandardBoardTest {

    @Test
    public void testIsStoneBreathing() {
        BuildStandardBoard builder = new BuildStandardBoard();
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
        BuildStandardBoard builder = new BuildStandardBoard();
        Board board = new Board();
        board.buildBoard = builder;

        // Assuming initial state
        assertFalse(builder.isKoViolation());

        // Assuming a move is made
        board.placeStone(0, 0, StoneColor.BLACK, StoneColor.WHITE);


        // Assuming another move is made
        board.placeStone(1, 0, StoneColor.WHITE, StoneColor.BLACK);
        board.placeStone(1, 1, StoneColor.WHITE, StoneColor.BLACK);
        board.placeStone(0, 1, StoneColor.WHITE, StoneColor.BLACK);


        // Assuming the same move is made (ko violation)
        board.placeStone(0, 0, StoneColor.BLACK, StoneColor.WHITE);
        assertTrue(builder.isKoViolation());
    }

    @Test
    public void testIsValidCoordinate() {
        BuildStandardBoard builder = new BuildStandardBoard();
        assertTrue(builder.isValidCoordinate(0, 0));
        assertFalse(builder.isValidCoordinate(-1, 0));
        assertFalse(builder.isValidCoordinate(0, 20));
    }
}