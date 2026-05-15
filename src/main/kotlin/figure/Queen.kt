package org.example.figure

import org.example.model.Color
import org.example.model.Position
import org.example.board.Board

class Queen(color: Color, position: Position, hasMoved: Boolean = false) : Figure(color, position, hasMoved) {

    override fun copy(newPosition: Position): Queen = Queen(color, newPosition, hasMoved)

    override fun canMove(to: Position, board: Board): Boolean {
        if (!isQueenMove(to)) {
            return false
        }
        return board.isPathClear(position, to) && board.checkPosition(to, color)
    }

    override fun canAttack(to: Position, board: Board): Boolean {
        if (!isQueenMove(to)) {
            return false
        }
        return board.isPathClear(position, to)
    }

    private fun isQueenMove(to: Position): Boolean {
        val dx = kotlin.math.abs(to.x - position.x)
        val dy = kotlin.math.abs(to.y - position.y)
        return dx == dy || position.x == to.x || position.y == to.y
    }
}