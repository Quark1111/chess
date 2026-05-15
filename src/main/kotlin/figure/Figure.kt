package org.example.figure

import org.example.model.Color
import org.example.model.Position
import org.example.board.Board

abstract class Figure(
    val color: Color,
    var position: Position,
    var hasMoved: Boolean = false
) {
    abstract fun canMove(to: Position, board: Board): Boolean
    abstract fun canAttack(to: Position, board: Board): Boolean
    abstract fun copy(newPosition: Position): Figure
}