package unit.FigureTest

import org.example.figure.*
import org.example.model.*
import org.example.board.Board
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FiguresTest {

    @Test
    fun `king can move one step in any direction`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val king = King(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), king)

        assertTrue(king.canMove(Position(4, 5), board))
        assertTrue(king.canMove(Position(4, 3), board))
        assertTrue(king.canMove(Position(5, 4), board))
        assertTrue(king.canMove(Position(3, 4), board))
        assertTrue(king.canMove(Position(5, 5), board))
        assertTrue(king.canMove(Position(5, 3), board))
        assertTrue(king.canMove(Position(3, 5), board))
        assertTrue(king.canMove(Position(3, 3), board))
    }

    @Test
    fun `king cannot move two steps`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val king = King(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), king)

        assertFalse(king.canMove(Position(4, 6), board))
        assertFalse(king.canMove(Position(6, 4), board))
    }

    @Test
    fun `queen moves horizontally, vertically and diagonally`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val queen = Queen(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), queen)

        assertTrue(queen.canMove(Position(4, 7), board))
        assertTrue(queen.canMove(Position(7, 4), board))
        assertTrue(queen.canMove(Position(7, 7), board))
        assertTrue(queen.canMove(Position(0, 0), board))
    }

    @Test
    fun `queen cannot jump over pieces`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val queen = Queen(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), queen)
        board.setFigure(Position(4, 6), Pawn(Color.WHITE, Position(4, 6)))

        assertFalse(queen.canMove(Position(4, 7), board))
    }

    @Test
    fun `rook moves horizontally and vertically`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val rook = Rook(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), rook)

        assertTrue(rook.canMove(Position(4, 7), board))
        assertTrue(rook.canMove(Position(4, 0), board))
        assertTrue(rook.canMove(Position(7, 4), board))
        assertTrue(rook.canMove(Position(0, 4), board))
    }

    @Test
    fun `rook cannot move diagonally`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val rook = Rook(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), rook)

        assertFalse(rook.canMove(Position(5, 5), board))
        assertFalse(rook.canMove(Position(7, 7), board))
    }

    @Test
    fun `bishop moves diagonally`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val bishop = Bishop(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), bishop)

        assertTrue(bishop.canMove(Position(7, 7), board))
        assertTrue(bishop.canMove(Position(0, 0), board))
        assertTrue(bishop.canMove(Position(1, 7), board))
        assertTrue(bishop.canMove(Position(7, 1), board))
    }

    @Test
    fun `bishop cannot move horizontally`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val bishop = Bishop(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), bishop)

        assertFalse(bishop.canMove(Position(4, 7), board))
        assertFalse(bishop.canMove(Position(7, 4), board))
    }

    @Test
    fun `knight moves in L-shape`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val knight = Knight(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), knight)

        assertTrue(knight.canMove(Position(6, 5), board))
        assertTrue(knight.canMove(Position(6, 3), board))
        assertTrue(knight.canMove(Position(5, 6), board))
        assertTrue(knight.canMove(Position(3, 6), board))
        assertTrue(knight.canMove(Position(2, 5), board))
        assertTrue(knight.canMove(Position(2, 3), board))
        assertTrue(knight.canMove(Position(3, 2), board))
        assertTrue(knight.canMove(Position(5, 2), board))
    }

    @Test
    fun `knight can jump over pieces`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val knight = Knight(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), knight)
        board.setFigure(Position(5, 5), Pawn(Color.WHITE, Position(5, 5)))

        assertTrue(knight.canMove(Position(6, 5), board))
    }

    @Test
    fun `white pawn moves one step forward`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val pawn = Pawn(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), pawn)

        assertTrue(pawn.canMove(Position(4, 5), board))
        assertFalse(pawn.canMove(Position(4, 3), board))
    }

    @Test
    fun `white pawn can move two steps from start`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val pawn = Pawn(Color.WHITE, Position(4, 1))
        board.setFigure(Position(4, 1), pawn)
        assertTrue(pawn.canMove(Position(4, 3), board))
    }

    @Test
    fun `white pawn cannot move two steps after moving`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val pawn = Pawn(Color.WHITE, Position(4, 2), hasMoved = true)
        board.setFigure(Position(4, 2), pawn)
        assertFalse(pawn.canMove(Position(4, 4), board))
    }

    @Test
    fun `white pawn captures diagonally`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val pawn = Pawn(Color.WHITE, Position(4, 4))
        board.setFigure(Position(4, 4), pawn)
        board.setFigure(Position(5, 5), Pawn(Color.BLACK, Position(5, 5)))
        assertTrue(pawn.canMove(Position(5, 5), board))
    }

    @Test
    fun `black pawn moves one step forward`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val pawn = Pawn(Color.BLACK, Position(4, 4))
        board.setFigure(Position(4, 4), pawn)

        assertTrue(pawn.canMove(Position(4, 3), board))
        assertFalse(pawn.canMove(Position(4, 5), board))
    }

    @Test
    fun `black pawn can move two steps from start`() {
        val board = Board(Array(8) { arrayOfNulls(8) })
        val pawn = Pawn(Color.BLACK, Position(4, 6))
        board.setFigure(Position(4, 6), pawn)
        assertTrue(pawn.canMove(Position(4, 4), board))
    }
}