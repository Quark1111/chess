package integration

import game.*
import org.example.model.*
import org.example.figure.*
import org.example.rules.ClassicRules
import rules.Rules
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameCastlingTest {

    @Test
    fun `white king side castling works`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)

        game.board.setFigure(Position(5, 0), null)
        game.board.setFigure(Position(6, 0), null)

        val response = game.makeMove("0-0")

        assertEquals(MoveResult.Accept, response.result)
        assertEquals(MoveEvent.None, response.event)

        val king = game.board.getFigure(Position(6, 0))
        assertTrue(king is King && king.color == Color.WHITE)

        val rook = game.board.getFigure(Position(5, 0))
        assertTrue(rook is Rook && rook.color == Color.WHITE)

        assertEquals(Color.BLACK, game.currentTurn)
    }

    @Test
    fun `castling fails if path is blocked`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)

        val response = game.makeMove("0-0")

        assertEquals(MoveResult.Error, response.result)
    }

    @Test
    fun `queen side castling works`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)

        game.board.setFigure(Position(1, 0), null)
        game.board.setFigure(Position(2, 0), null)
        game.board.setFigure(Position(3, 0), null)

        val response = game.makeMove("0-0-0")

        assertEquals(MoveResult.Accept, response.result)

        val king = game.board.getFigure(Position(2, 0))
        assertTrue(king is King && king.color == Color.WHITE)

        val rook = game.board.getFigure(Position(3, 0))
        assertTrue(rook is Rook && rook.color == Color.WHITE)
    }
}