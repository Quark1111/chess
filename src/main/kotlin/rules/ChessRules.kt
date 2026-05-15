package rules

import org.example.model.*
import org.example.figure.*
import org.example.board.Board
import kotlin.math.abs

abstract class ChessRules {

    fun isCheck(board: Board, color: Color): Boolean {
        val kingPosition = board.findKing(color) ?: return false
        return isSquareAttacked(board, kingPosition, color)
    }
    fun isCorrect(board: Board, from: Position, to: Position, color: Color): Boolean {
        val figure = board.getFigure(from) ?: return false
        if (figure.color != color) {
            return false
        }
        if (!figure.canMove(to, board)) {
            return false
        }
        if (wouldCauseCheck(board, from, to, color)) {
            return false
        }
        return true
    }
    fun canEnPassant(previousMove: String, currentMove: String, board: Board, color: Color): Boolean {
        if (previousMove.length != 5 || currentMove.length != 5) {
            return false
        }

        val previousFrom = fromString(previousMove.substring(0, 2))
        val previousTo = fromString(previousMove.substring(3, 5))
        val currentFrom = fromString(currentMove.substring(0, 2))
        val currentTo = fromString(currentMove.substring(3, 5))

        if (previousFrom == null || previousTo == null || currentFrom == null || currentTo == null) {
            return false
        }

        val previousPiece = board.getFigure(previousTo) ?: return false
        if (previousPiece !is Pawn) {
            return false
        }

        val currentPiece = board.getFigure(currentFrom) ?: return false
        if (currentPiece !is Pawn || currentPiece.color != color) return false

        if (previousPiece.color == currentPiece.color) {
            return false
        }

        if (abs(previousTo.y - previousFrom.y) != 2 || previousTo.x != previousFrom.x) {
            return false
        }

        val direction = if (color == Color.WHITE) 1 else -1
        if (abs(currentTo.x - currentFrom.x) != 1 || currentTo.y != currentFrom.y + direction) {
            return false
        }

        if (board.getFigure(currentTo) != null) {
            return false
        }

        if (previousTo.y != currentFrom.y || previousTo.x != currentTo.x) return false

        return true
    }

    fun isSquareAttacked(board: Board, position: Position, friendlyColor: Color): Boolean {
        for (x in 0..7) {
            for (y in 0..7) {
                val figure = board.getFigure(Position(x, y)) ?: continue
                if (figure.color != friendlyColor) {
                    if (figure.canAttack(position, board)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun getAllValidMoves(board: Board, color: Color): List<Pair<Position, Position>> {
        val moves = mutableListOf<Pair<Position, Position>>()

        for (x in 0..7) {
            for (y in 0..7) {
                val figure = board.getFigure(Position(x, y)) ?: continue
                if (figure.color != color) continue

                for (toX in 0..7) {
                    for (toY in 0..7) {
                        val to = Position(toX, toY)
                        if (figure.canMove(to, board) && !wouldCauseCheck(board, figure.position, to, color)) {
                            moves.add(figure.position to to)
                        }
                    }
                }
            }
        }

        return moves
    }

    fun wouldCauseCheck(board: Board, from: Position, to: Position, color: Color): Boolean
    {
        val testBoard = board.copy()
        testBoard.moveFigure(from, to)
        return isCheck(testBoard, color)
    }

    fun isCheckmate(board: Board, color: Color): Boolean {
        return isCheck(board, color) && getAllValidMoves(board, color).isEmpty()
    }

    fun isStalemate(board: Board, color: Color): Boolean {
        return !isCheck(board, color) && getAllValidMoves(board, color).isEmpty()
    }

    abstract fun canCastle(to: Position, from: Position, board: Board, color: Color): Boolean
}