package org.example.figure

import org.example.model.Color
import org.example.model.Position
import org.example.board.Board

class Knight(color: Color, position: Position, hasMoved: Boolean = false) : Figure(color, position, hasMoved) {

    override fun copy(newPosition: Position): Knight = Knight(color, newPosition, hasMoved)

    override fun canMove(to: Position, board: Board): Boolean {
        return isKnightMove(to) && board.checkPosition(to, color)
    }

    override fun canAttack(to: Position, board: Board): Boolean {
        return isKnightMove(to)
    }

    private fun isKnightMove(to: Position): Boolean {
        val dx = kotlin.math.abs(to.x - position.x)
        val dy = kotlin.math.abs(to.y - position.y)
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2)
    }
}