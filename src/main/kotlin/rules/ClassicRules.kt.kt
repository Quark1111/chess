package org.example.rules

import org.example.model.*
import org.example.figure.*
import org.example.board.Board
import rules.ChessRules

class ClassicRules : ChessRules() {
    override fun canCastle(to: Position, from: Position, board: Board, color: Color): Boolean {
        val deltaX = kotlin.math.abs(to.x - from.x)
        val deltaY = to.y - from.y
        if (deltaX != 2 || deltaY != 0) {
            return false
        }

        val king = board.getFigure(from)
        if (king !is King || king.hasMoved) {
            return false
        }

        val isKingSide = to.x - from.x > 0
        val rookX = if (isKingSide) 7 else 0
        val rook = board.getFigure(Position(rookX, to.y))
        if (rook !is Rook || rook.hasMoved) {
            return false
        }

        val step = if (isKingSide) 1 else -1
        var currentX = from.x
        val targetKingX = if (isKingSide) 6 else 2

        while (currentX != targetKingX + step) {
            val positionToCheck = Position(currentX, to.y)
            if (isSquareAttacked(board, positionToCheck, color)) {
                return false
            }
            currentX += step
        }

        val isPathClear = if (isKingSide) {
            board.isPathClear(Position(4, from.y), Position(7, to.y))
        } else {
            board.isPathClear(Position(0, from.y), Position(4, to.y))
        }

        return isPathClear
    }
}