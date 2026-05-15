package org.example.figure

import org.example.model.Color
import org.example.model.Position
import org.example.board.Board

class Pawn(color: Color, position: Position, hasMoved: Boolean = false) : Figure(color, position, hasMoved) {

    override fun copy(newPosition: Position): Pawn = Pawn(color, newPosition, hasMoved)

    override fun canMove(to: Position, board: Board): Boolean {
        val dist = if (color == Color.WHITE) 1 else -1
        val dx = to.x - position.x
        val dy = to.y - position.y

        if (dx == 0 && ((dy * dist == 2 && !hasMoved) || dy * dist == 1)) {
            return board.isPathClear(position, to) && board.getFigure(to) == null
        }
        if (kotlin.math.abs(dx) == 1 && dy * dist == 1) {
            return board.checkPosition(to, color) && board.getFigure(to) != null
        }
        return false
    }

    override fun canAttack(to: Position, board: Board): Boolean {
        val dist = if (color == Color.WHITE) 1 else -1
        val dx = to.x - position.x
        val dy = to.y - position.y
        return (dx == 1 && dy * dist == 1) || (dx == -1 && dy * dist == 1)
    }
}