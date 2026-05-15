package system

import game.*
import org.example.model.*
import org.example.rules.ClassicRules
import rules.Rules
import kotlin.test.Test
import kotlin.test.assertEquals

class GameSystemTest {

    @Test
    fun `fool's mate debug`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)
        game.makeMove("f2-f3")
        game.makeMove("e7-e5")
        game.makeMove("g2-g4")
        val result = game.makeMove("d8-h4")
        assertEquals(MoveResult.Checkmate, result.result)
    }

    @Test
    fun `scholar's mate`() {
        val game = Game(Player("W"), Player("B"), ClassicRules(), Rules.Classic)
        game.makeMove("e2-e4")
        game.makeMove("e7-e5")
        game.makeMove("f1-c4")
        game.makeMove("b8-c6")
        game.makeMove("d1-h5")
        game.makeMove("g8-f6")
        val result = game.makeMove("h5-f7")
        assertEquals(MoveResult.Checkmate, result.result)
    }
}