package org.example.figure

import org.example.board.Board
import org.example.model.Color
import org.example.model.Position

class King(color: Color, position: Position, hasMoved: Boolean = false) : Figure(color, position, hasMoved) {

    override fun copy(newPosition: Position): King = King(color, newPosition, hasMoved)

    override fun canMove(to: Position, board: Board): Boolean {
        return isOneStep(to) && board.checkPosition(to, color)
    }

    override fun canAttack(to: Position, board: Board): Boolean {
        return isOneStep(to)
    }

    private fun isOneStep(to: Position): Boolean {
        val dx = kotlin.math.abs(to.x - position.x)
        val dy = kotlin.math.abs(to.y - position.y)
        return dx <= 1 && dy <= 1
    }
}