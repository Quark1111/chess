package integration

import game.*
import org.example.model.*
import org.example.rules.*
import org.example.figure.*
import rules.Rules
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GamePromotionTest {

    @Test
    fun `pawn promotion triggers NeedPromotion event`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)
        game.board.setFigure(Position(4, 6), null)
        game.board.setFigure(Position(4, 7), null)
        game.board.setFigure(Position(4, 6), Pawn(Color.WHITE, Position(4, 6), true))
        val response = game.makeMove("e7-e8")
        assertEquals(MoveResult.Accept, response.result)
        assertEquals(MoveEvent.NeedPromotion, response.event)
    }

    @Test
    fun `complete promotion to queen works`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)
        game.board.setFigure(Position(4, 6), null)
        game.board.setFigure(Position(4, 7), null)
        game.board.setFigure(Position(4, 6), Pawn(Color.WHITE, Position(4, 6), true))
        game.makeMove("e7-e8")
        val response = game.completePromotion(PromotionPiece.QUEEN)
        assertEquals(MoveResult.Accept, response.result)
        val piece = game.board.getFigure(Position(4, 7))
        assertTrue(piece is Queen && piece.color == Color.WHITE)
    }

    @Test
    fun `complete promotion to knight works`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)
        game.board.setFigure(Position(4, 6), null)
        game.board.setFigure(Position(4, 7), null)
        game.board.setFigure(Position(4, 6), Pawn(Color.WHITE, Position(4, 6), true))
        game.makeMove("e7-e8")
        val response = game.completePromotion(PromotionPiece.KNIGHT)
        assertEquals(MoveResult.Accept, response.result)
        val piece = game.board.getFigure(Position(4, 7))
        assertTrue(piece is Knight && piece.color == Color.WHITE)
    }
}