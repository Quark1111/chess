package org.example.figure

import org.example.model.Color
import org.example.model.Position
import org.example.board.Board

class Rook(color: Color, position: Position, hasMoved: Boolean = false) : Figure(color, position, hasMoved) {

    override fun copy(newPosition: Position): Rook = Rook(color, newPosition, hasMoved)

    override fun canMove(to: Position, board: Board): Boolean {
        if (!isStraight(to)) {
            return false
        }
        return board.isPathClear(position, to) && board.checkPosition(to, color)
    }

    override fun canAttack(to: Position, board: Board): Boolean {
        if (!isStraight(to)) {
            return false
        }
        return board.isPathClear(position, to)
    }

    private fun isStraight(to: Position): Boolean {
        return position.x == to.x || position.y == to.y
    }
}