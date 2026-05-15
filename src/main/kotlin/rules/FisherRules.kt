package org.example.rules

import org.example.model.*
import org.example.figure.*
import org.example.board.Board
import rules.ChessRules

class FischerRules : ChessRules() {
    override fun canCastle(to: Position, from: Position, board: Board, color: Color): Boolean {
        if (to.y != from.y) {
            return false
        }

        val king = board.getFigure(from)
        if (king !is King || king.hasMoved) {
            return false
        }

        val isKingSide = to.x > from.x
        val step = if (isKingSide) 1 else -1

        var rookX = from.x + step
        var rook: Rook? = null
        while (rookX in 0..7) {
            val figure = board.getFigure(Position(rookX, from.y))
            if (figure is Rook && figure.color == color) {
                rook = figure
                break
            }
            rookX += step
        }

        if (rook == null || rook.hasMoved) {
            return false
        }

        val targetKingX = if (isKingSide) 6 else 2
        var x = from.x
        while (x != targetKingX + step) {
            if (isSquareAttacked(board, Position(x, from.y), color)) {
                return false
            }
            x += step
        }

        x = from.x + step
        while (x != targetKingX + step) {
            val piece = board.getFigure(Position(x, from.y))
            if (piece != null && piece != rook) {
                return false
            }
            x += step
        }

        val targetRookX = if (isKingSide) 5 else 3
        x = rook.position.x + step
        while (x != targetRookX + step) {
            val piece = board.getFigure(Position(x, from.y))
            if (piece != null && piece != king) {
                return false
            }
            x += step
        }
        return true
    }
}