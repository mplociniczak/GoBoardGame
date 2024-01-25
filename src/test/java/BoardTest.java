import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.server.gameLogic.Board;
import org.server.gameLogic.StoneColor;
import org.server.gameLogic.BoardBuilder;
import org.server.gameLogic.BuildStandardBoard;

public class BoardTest {

    @Test
    public void testBoardInitialization() {
        BoardBuilder board = new BuildStandardBoard();
        assertNotNull(board);
    }

    @Test
    public void testIsIntersectionEmpty() {
        Board board = new Board();

        assertTrue(board.buildBoard.isIntersectionEmpty(0, 0));

    }

    @Test
    public void testPlaceStone() {
        Board board = new Board();
        board.placeStone(0, 0, StoneColor.BLACK, StoneColor.WHITE);

        // Assuming fields[0][0] is updated with a black stone
        assertEquals(StoneColor.BLACK, Board.fields[0][0].getColor());
    }


}
