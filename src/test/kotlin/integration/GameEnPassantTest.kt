package integration

import game.*
import org.example.model.*
import org.example.figure.*
import rules.Rules
import kotlin.test.Test
import org.example.rules.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GameEnPassantTest {

    @Test
    fun `en passant capture works`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)
        game.makeMove("b2-b4")
        game.makeMove("c7-c5")
        game.makeMove("b4-c5")
        game.makeMove("b7-b5")
        val response = game.makeMove("c5-b6")
        assertEquals(MoveResult.Accept, response.result)
        val pawn = game.board.getFigure(Position(1, 5))
        assertTrue(pawn is Pawn && pawn.color == Color.WHITE)
        assertNull(game.board.getFigure(Position(1, 4)))
    }

    @Test
    fun `en passant not allowed after delay`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)

        game.makeMove("b2-b4")
        game.makeMove("c7-c5")
        game.makeMove("b4-c5")
        game.makeMove("b7-b5")
        game.makeMove("a2-a3")
        game.makeMove("a7-a6")

        val response = game.makeMove("c5-b6")
        assertEquals(MoveResult.Error, response.result)
    }
}