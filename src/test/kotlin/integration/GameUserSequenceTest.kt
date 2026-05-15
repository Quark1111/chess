package integration

import game.*
import org.example.model.*
import org.example.rules.*
import rules.Rules
import kotlin.test.Test
import kotlin.test.assertEquals

class GameUserSequenceTest {

    @Test
    fun `user sequence b2-b4 c7-c5 b4-c5 b7-b5 c5-b6 a7-a5`() {
        val game = Game(Player("White"), Player("Black"), ClassicRules(), Rules.Classic)
        var response = game.makeMove("b2-b4")
        assertEquals(MoveResult.Accept, response.result)
        response = game.makeMove("c7-c5")
        assertEquals(MoveResult.Accept, response.result)
        response = game.makeMove("b4-c5")
        assertEquals(MoveResult.Accept, response.result)
        response = game.makeMove("b7-b5")
        assertEquals(MoveResult.Accept, response.result)
        response = game.makeMove("c5-b6")
        assertEquals(MoveResult.Accept, response.result)
        response = game.makeMove("a7-a5")
        assertEquals(MoveResult.Accept, response.result)
    }
}