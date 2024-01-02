import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.server.Board;
import org.server.StoneColor;

public class BoardTest {

    @Test
    public void testBoardInitialization() {
        Board board = new Board();
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
        assertEquals(StoneColor.BLACK, board.fields[0][0].getColor());
    }


}
